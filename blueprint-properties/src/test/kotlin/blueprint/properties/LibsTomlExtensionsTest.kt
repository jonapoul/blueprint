@file:Suppress("JUnitMalformedDeclaration")

package blueprint.properties

import ERROR_MESSAGE
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(TestParameterInjector::class)
internal class LibsTomlExtensionsTest {
  @Test
  fun `Dependency success`(
    @TestParameter(
      "dep-api-success",
      "dep-compileOnly-success",
      "dep-implementation-success",
      "dep-runtimeOnly-success",
      "dep-testImplementation-success",
    ) fixtureName: String,
  ) {
    val fixtureDir = File(fixturesDir, fixtureName)
    createRunner(fixtureDir)
      .build()
      .assertAssembleSuccess()
  }

  @Test
  fun `Dependency failure`(
    @TestParameter(
      "dep-api-failure",
      "dep-compileOnly-failure",
      "dep-implementation-failure",
      "dep-runtimeOnly-failure",
      "dep-testImplementation-failure",
    ) fixtureName: String,
  ) {
    val fixtureDir = File(fixturesDir, fixtureName)
    createRunner(fixtureDir)
      .buildAndFail()
      .assertContains(ERROR_MESSAGE)
  }

  @Test
  fun `Plugin failure`() {
    val fixtureDir = File(fixturesDir, "plugin-failure")
    createRunner(fixtureDir).buildAndFail()
  }

  @Test
  fun `Plugin success`() {
    val fixtureDir = File(fixturesDir, "plugin-success")
    createRunner(fixtureDir)
      .build()
      .assertAssembleSuccess()
  }
}
