package blueprint.detekt.rules

import assertk.assertThat
import blueprint.detekt.test.hasNoFindings
import blueprint.detekt.test.hasNumFindings
import blueprint.detekt.test.lintedAsKts
import blueprint.detekt.test.messageContains
import blueprint.detekt.test.onFinding
import blueprint.detekt.test.onFirstFinding
import blueprint.detekt.test.onSecondFinding
import blueprint.detekt.test.onThirdFinding
import dev.detekt.api.Config
import dev.detekt.test.junit.KotlinCoreEnvironmentTest
import dev.detekt.test.utils.KotlinEnvironmentContainer
import kotlin.test.Test

@KotlinCoreEnvironmentTest
internal class AvoidAfterEvaluateTest(private val env: KotlinEnvironmentContainer) {
  private val rule = AvoidAfterEvaluate(Config.empty)

  @Test
  fun `Don't report non-afterEvaluate calls`() {
    val code =
      """
      configurations.register("config")
      tasks.register("myTask")
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasNoFindings()
  }

  @Test
  fun `Report afterEvaluate`() {
    val code =
      """
      afterEvaluate { }
      this.afterEvaluate { }
      val p: Project = TODO()
      p.afterEvaluate { }
      rootProject.afterEvaluate { }
      """
        .trimIndent()

    assertThat(rule)
      .lintedAsKts(env, code)
      .hasNumFindings(expected = 4)
      .onFirstFinding { messageContains("afterEvaluate") }
      .onSecondFinding { messageContains("afterEvaluate") }
      .onThirdFinding { messageContains("afterEvaluate") }
      .onFinding(number = 4) { messageContains("afterEvaluate") }
  }
}
