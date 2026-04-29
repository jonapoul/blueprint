package blueprint.detekt.rules

import assertk.assertThat
import blueprint.detekt.test.hasNoFindings
import blueprint.detekt.test.hasNumFindings
import blueprint.detekt.test.hasOneFinding
import blueprint.detekt.test.lintedAsKts
import blueprint.detekt.test.messageContains
import blueprint.detekt.test.onFirstFinding
import blueprint.detekt.test.onSecondFinding
import dev.detekt.api.Config
import dev.detekt.test.junit.KotlinCoreEnvironmentTest
import dev.detekt.test.utils.KotlinEnvironmentContainer
import kotlin.test.Test

@KotlinCoreEnvironmentTest
internal class AvoidRootProjectAccessTest(private val env: KotlinEnvironmentContainer) {
  private val rule = AvoidRootProjectAccess(Config.empty)

  @Test
  fun `Don't report rootProject isolated`() {
    val code =
      """
      val iso = rootProject.isolated
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasNoFindings()
  }

  @Test
  fun `Don't report this rootProject isolated`() {
    val code =
      """
      val iso = this.rootProject.isolated
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasNoFindings()
  }

  @Test
  fun `Don't report unrelated properties`() {
    val code =
      """
      val n = name
      val p = path
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasNoFindings()
  }

  @Test
  fun `Report rootProject name`() {
    val code =
      """
      val n = rootProject.name
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasOneFinding().messageContains("rootProject")
  }

  @Test
  fun `Report this rootProject`() {
    val code =
      """
      val rp = this.rootProject
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasOneFinding().messageContains("rootProject")
  }

  @Test
  fun `Report rootProject layout`() {
    val code =
      """
      val dir = rootProject.layout
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasOneFinding().messageContains("rootProject")
  }

  @Test
  fun `Report multiple rootProject accesses`() {
    val code =
      """
      val n = rootProject.name
      val p = rootProject.path
      """
        .trimIndent()

    assertThat(rule)
      .lintedAsKts(env, code)
      .hasNumFindings(expected = 2)
      .onFirstFinding { messageContains("rootProject") }
      .onSecondFinding { messageContains("rootProject") }
  }
}
