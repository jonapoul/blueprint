package blueprint.core

import blueprint.test.DEFAULT_REPOSITORIES_KTS
import blueprint.test.GRADLE_VERSION
import blueprint.test.Scenario
import blueprint.test.ScenarioTest
import blueprint.test.assertThatTask
import blueprint.test.buildsSuccessfully
import blueprint.test.outputContainsLine
import java.io.File
import kotlin.test.Test

class LocalPropertiesValuesScenario : ScenarioTest() {
  override val gradleVersion = GRADLE_VERSION

  override val fileTree = fileTree {
    "settings.gradle.kts"(DEFAULT_REPOSITORIES_KTS)

    "local.properties"(
      """
      present.key=present-value
      empty.key=
      """
        .trimIndent()
    )

    "custom.properties"("custom.key=custom-value")

    "build.gradle.kts"(
      $$"""
      import blueprint.core.*

      plugins { id("dev.jonpoulton.blueprint") }

      fun <T : Any> registerTask(name: String, property: Provider<T>) = tasks.register(name) {
        inputs.property("property", property)
        inputs.property("name", name)
        doLast { logger.lifecycle("$name = ${property.get()}") }
      }

      fun constant(value: String): Provider<String> =
        objects.property(String::class.java).apply { set(value) }

      val local = localProperties()

      registerTask("printPresent", local.map { it["present.key"] ?: "<absent>" })
      registerTask("printMissingKey", local.map { it["missing.key"] ?: "<absent>" })
      registerTask("printEmptyValue", local.map { (it["empty.key"] ?: "<absent>").ifEmpty { "<empty>" } })
      registerTask("printMapState", local.map { if (it.isEmpty()) "<empty-map>" else it.keys.sorted().joinToString(",") })
      registerTask("printCustom", localProperties("custom.properties").map { it["custom.key"] ?: "<absent>" })

      registerTask("printOptionalPresent", constant(local.getOptional("present.key") ?: "<absent>"))
      registerTask("printOptionalMissing", constant(local.getOptional("missing.key") ?: "<absent>"))
      registerTask("printOptionalEmpty", constant(local.getOptional("empty.key") ?: "<absent>"))
      """
        .trimIndent()
    )
  }

  @Test
  fun `Reads a present key`() = runScenario {
    assertThatTask(":printPresent")
      .buildsSuccessfully()
      .outputContainsLine("printPresent = present-value")
  }

  @Test
  fun `Missing key resolves to absent`() = runScenario {
    assertThatTask(":printMissingKey")
      .buildsSuccessfully()
      .outputContainsLine("printMissingKey = <absent>")
  }

  @Test
  fun `Empty value is retained in the map`() = runScenario {
    assertThatTask(":printEmptyValue")
      .buildsSuccessfully()
      .outputContainsLine("printEmptyValue = <empty>")
  }

  @Test
  fun `Custom filename is read`() = runScenario {
    assertThatTask(":printCustom")
      .buildsSuccessfully()
      .outputContainsLine("printCustom = custom-value")
  }

  @Test
  fun `Missing file yields an empty map`() = runScenario {
    localPropertiesFile().delete()

    assertThatTask(":printMapState", ":printPresent")
      .buildsSuccessfully()
      .outputContainsLine("printMapState = <empty-map>")
      .outputContainsLine("printPresent = <absent>")
  }

  @Test
  fun `getOptional returns the value, or null for missing and empty keys`() = runScenario {
    assertThatTask(":printOptionalPresent", ":printOptionalMissing", ":printOptionalEmpty")
      .buildsSuccessfully()
      .outputContainsLine("printOptionalPresent = present-value")
      .outputContainsLine("printOptionalMissing = <absent>")
      .outputContainsLine("printOptionalEmpty = <absent>")
  }

  private fun Scenario.localPropertiesFile(): File = rootDir.resolve("local.properties")
}
