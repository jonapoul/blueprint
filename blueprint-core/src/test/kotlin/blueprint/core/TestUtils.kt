package blueprint.core

import org.gradle.api.GradleException
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal fun createRunner(fixtureDir: File, propertyValue: Any? = null): GradleRunner {
  if (!fixtureDir.exists()) throw GradleException("Fixture doesn't exist: $fixtureDir")
  val gradleRoot = File(fixtureDir, "gradle").also { it.mkdir() }
  File("../gradle/wrapper").copyRecursively(File(gradleRoot, "wrapper"), overwrite = true)

  val args = mutableListOf(
    "clean",
    "assemble",
    "--stacktrace",
    "-PblueprintVersion=${System.getProperty("blueprintVersion")!!}",
    "-PkotlinVersion=${System.getProperty("kotlinVersion")!!}",
  )
  if (propertyValue != null) {
    args += "-P$TARGET_PROPERTY_NAME=$propertyValue"
  }

  return GradleRunner.create()
    .withProjectDir(fixtureDir)
    .withDebug(true) // Run in-process
    .withArguments(args)
    .forwardOutput()
}

internal fun BuildResult.assertAssembleSuccess() {
  assertNotEquals(TaskOutcome.FAILED, task(":assemble")?.outcome)
}

internal fun BuildResult.assertContains(message: String) {
  assertTrue(
    actual = output.contains(message),
    message = "Should find \"$message\", got:\n$output"
  )
}

internal fun BuildResult.assertPropertyValuePrinted(value: String) {
  val expected = "$TARGET_PROPERTY_NAME = $value"
  assertTrue(
    actual = output.contains(expected),
    message = "Should find \"$expected\", got:\n$output"
  )
}

internal val fixturesDir = File("src/test/fixtures")

internal const val TARGET_PROPERTY_NAME = "testProperty"
