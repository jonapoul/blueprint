package blueprint.core

import blueprint.test.GRADLE_VERSION
import blueprint.test.ScenarioTest
import blueprint.test.assertThatTask
import blueprint.test.buildGradleKts
import blueprint.test.buildsSuccessfully
import blueprint.test.libsVersionsToml
import blueprint.test.outputContainsLine
import blueprint.test.settingsGradleKts
import kotlin.test.Test

class VersionCatalogScenario : ScenarioTest() {
  override val gradleVersion = GRADLE_VERSION

  override val fileTree = fileTree {
    settingsGradleKts()

    buildGradleKts(
      """
      import blueprint.core.get
      import blueprint.core.plugin
      import blueprint.core.version
      import blueprint.core.libs as blueprintLibs

      plugins { id("dev.jonpoulton.blueprint") }

      val libs = blueprintLibs

      tasks.register("printJunitApi") {
        val junitApi = libs["junit.api"].map { it.toString() }
        doLast { logger.lifecycle(junitApi.get()) }
      }

      tasks.register("printJunitVersion") {
        val junit = libs.version("junit").toString()
        doLast { logger.lifecycle(junit) }
      }

      tasks.register("printAssertkVersion") {
        val assertk = libs["assertk"].map { it.version }
        doLast { logger.lifecycle(assertk.get()) }
      }

      tasks.register("printKotlinPluginId") {
        val kotlinId = libs.plugin("kotlin").map { it.pluginId }
        doLast { logger.lifecycle(kotlinId.get()) }
      }
      """
        .trimIndent()
    )

    libsVersionsToml(
      """
      [versions]
      junit = "6.0.2"

      [libraries]
      assertk = { module = "com.willowtreeapps.assertk:assertk", version = "0.28.1" }
      junit-api = { module = "org.junit.jupiter:junit-jupiter-api", version.ref = "junit" }

      [plugins]
      kotlin = { id = "org.jetbrains.kotlin.jvm", version = "2.0.21" }
      """
        .trimIndent()
    )
  }

  @Test
  fun `Print junit library string`() = runScenario {
    assertThatTask(":printJunitApi")
      .buildsSuccessfully()
      .outputContainsLine("org.junit.jupiter:junit-jupiter-api:6.0.2")
  }

  @Test
  fun `Print junit version string`() = runScenario {
    assertThatTask(":printJunitVersion").buildsSuccessfully().outputContainsLine("6.0.2")
  }

  @Test
  fun `Print assertk version string`() = runScenario {
    assertThatTask(":printAssertkVersion").buildsSuccessfully().outputContainsLine("0.28.1")
  }

  @Test
  fun `Print kotlin plugin ID`() = runScenario {
    assertThatTask(":printKotlinPluginId")
      .buildsSuccessfully()
      .outputContainsLine("org.jetbrains.kotlin.jvm")
  }
}
