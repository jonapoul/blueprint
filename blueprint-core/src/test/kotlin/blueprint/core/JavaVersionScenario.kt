package blueprint.core

import assertk.Assert
import assertk.assertThat
import assertk.assertions.support.expected
import blueprint.test.DEFAULT_REPOSITORIES_KTS
import blueprint.test.GRADLE_VERSION
import blueprint.test.Scenario
import blueprint.test.ScenarioTest
import blueprint.test.assertThatTask
import blueprint.test.buildsSuccessfully
import blueprint.test.failsBuild
import blueprint.test.outputContainsLine
import blueprint.test.outputContainsMatch
import java.io.File
import kotlin.test.Test
import org.gradle.testkit.runner.GradleRunner

internal class JavaVersionScenario : ScenarioTest() {
  override val gradleVersion = GRADLE_VERSION

  override val fileTree = fileTree {
    "settings.gradle.kts"(DEFAULT_REPOSITORIES_KTS)

    "build.gradle.kts"(
      $$"""
      import blueprint.core.*

      plugins { id("dev.jonpoulton.blueprint") }

      fun <T : Any> registerTask(name: String, property: Provider<T>) = tasks.register(name) {
        inputs.property("property", property)
        inputs.property("name", name)
        doLast { logger.lifecycle("$name = ${property.get()}") }
      }

      registerTask("printJavaVersion", javaVersion())
      registerTask("printJvmTarget", jvmTarget())
      registerTask("printJavaLanguageVersion", javaLanguageVersion())
      registerTask("printJavaVersionString", javaVersionString())
      """
        .trimIndent()
    )
  }

  @Test
  fun `Fail if file doesn't exist`() = runScenario {
    assertThat(javaVersionFile()).doesNotExist()

    assertThatAllTasks()
      .failsBuild()
      .outputContainsMatch("> Java version file does not exist: .*?/.java-version".toRegex())
  }

  @Test
  fun `Fail if file is empty`() = runScenario {
    javaVersionFile().writeText("")

    assertThatAllTasks()
      .failsBuild()
      .outputContainsMatch("> Java version file is empty: .*?/.java-version".toRegex())
  }

  @Test
  fun `Fail if file isn't an integer`() = runScenario {
    javaVersionFile().writeText("11.1")

    assertThatAllTasks()
      .failsBuild()
      .outputContainsMatch("> Java version must be a valid integer, but was: '11.1'".toRegex())
  }

  @Test
  fun `Still works if file has spaces or newlines`() = runScenario {
    javaVersionFile()
      .writeText(
        @Suppress("TrimMultilineRawString")
        """
        11



      """
      )

    assertThatTask(":printJavaVersion")
      .buildsSuccessfully()
      .outputContainsLine("printJavaVersion = 11")
  }

  @Test
  fun `Parse Java 11`() = runScenario {
    javaVersionFile().writeText("11")

    assertThatAllTasks()
      .buildsSuccessfully()
      .outputContainsLine("printJavaVersion = 11")
      .outputContainsLine("printJvmTarget = JVM_11")
      .outputContainsLine("printJavaLanguageVersion = 11")
      .outputContainsLine("printJavaVersionString = 11")
  }

  private fun Scenario.javaVersionFile(): File = rootDir.resolve(".java-version")

  private fun Scenario.assertThatAllTasks(): Assert<GradleRunner> =
    assertThatTask(
      ":printJavaLanguageVersion",
      ":printJavaVersion",
      ":printJavaVersionString",
      ":printJvmTarget",
    )

  private fun Assert<File>.doesNotExist() = transform { actual ->
    if (actual.exists()) expected("to not exist") else actual
  }
}
