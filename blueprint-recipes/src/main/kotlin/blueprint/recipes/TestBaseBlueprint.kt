package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.withType

public class TestBaseBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    val disableRelease = boolPropertyOrElse(key = "blueprint.test.disableRelease", default = false)

    tasks.withType<Test>().configureEach { test ->
      if (disableRelease && test.name.contains("release", ignoreCase = true)) {
        test.enabled = false
      }

      test.testLogging.apply {
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
        showStandardStreams = true
      }
    }
  }
}
