package blueprint.recipes

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.withType

public fun Project.testBaseBlueprint(disableReleaseTests: Boolean = false) {
  tasks.withType<Test> {
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
