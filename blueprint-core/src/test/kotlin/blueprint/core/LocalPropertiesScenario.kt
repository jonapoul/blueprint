package blueprint.core

import assertk.Assert
import assertk.assertions.support.expected
import blueprint.test.DEFAULT_REPOSITORIES_KTS
import blueprint.test.GRADLE_VERSION
import blueprint.test.ScenarioTest
import blueprint.test.assertThatTask
import blueprint.test.buildsSuccessfully
import blueprint.test.outputContainsLine
import kotlin.test.Test
import org.gradle.testkit.runner.BuildResult

class LocalPropertiesScenario : ScenarioTest() {
  override val gradleVersion = GRADLE_VERSION

  override val fileTree = fileTree {
    "settings.gradle.kts"(
      DEFAULT_REPOSITORIES_KTS +
        """
        include(":a", ":b")
        """
          .trimIndent()
    )

    "local.properties"(
      """
      my.key=my-value
      """
        .trimIndent()
    )

    val buildScript =
      $$"""
      import blueprint.core.*

      plugins { id("dev.jonpoulton.blueprint") }

      fun <T : Any> registerTask(name: String, property: Provider<T>) = tasks.register(name) {
        inputs.property("property", property)
        inputs.property("name", name)
        doLast { logger.lifecycle("$name = ${property.get()}") }
      }

      // Project-receiver overload shares a single provider instance across the whole build.
      registerTask("printProperty", localProperties().map { it["my.key"] })
      """
        .trimIndent()

    "a" { "build.gradle.kts"(buildScript) }
    "b" { "build.gradle.kts"(buildScript) }
  }

  @Test
  fun `Shared local properties resolve consistently across projects`() = runScenario {
    // --info surfaces the "Reading local properties from ..." line emitted once per actual file
    // read.
    assertThatTask(":a:printProperty", ":b:printProperty", "--info")
      .buildsSuccessfully()
      .outputContainsLine("printProperty = my-value")
      .readsLocalPropertiesExactly(times = 1)
  }

  // Both projects consume the value, but the shared BuildService means the file is parsed only
  // once.
  private fun Assert<BuildResult>.readsLocalPropertiesExactly(times: Int): Assert<BuildResult> =
    transform { result ->
      val reads =
        result.output.lineSequence().count { it.contains("Reading local properties from") }
      if (reads == times) {
        result
      } else {
        expected("local.properties to be read $times time(s), but was read $reads time(s)")
      }
    }
}
