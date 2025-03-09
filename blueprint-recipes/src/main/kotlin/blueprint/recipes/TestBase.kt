package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.withType

public fun Project.testBaseBlueprint() {
  tasks.withType<Test> {
    val disableReleaseTests = boolPropertyOrElse(key = "blueprint.test.disableRelease", default = false)
    if (disableReleaseTests) {
      if (name.contains("release", ignoreCase = true)) {
        enabled = false
      }
    }

    testLogging.apply {
      exceptionFormat = TestExceptionFormat.FULL
      showCauses = true
      showExceptions = true
      showStackTraces = true
      showStandardStreams = true
    }
  }
}
