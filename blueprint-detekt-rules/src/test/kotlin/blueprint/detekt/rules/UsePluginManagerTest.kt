package blueprint.detekt.rules

import assertk.assertThat
import blueprint.detekt.test.hasNoFindings
import blueprint.detekt.test.hasNumFindings
import blueprint.detekt.test.lintedAsKts
import blueprint.detekt.test.messageContains
import blueprint.detekt.test.onFirstFinding
import blueprint.detekt.test.onSecondFinding
import blueprint.detekt.test.onThirdFinding
import dev.detekt.api.Config
import dev.detekt.test.junit.KotlinCoreEnvironmentTest
import dev.detekt.test.utils.KotlinEnvironmentContainer
import kotlin.test.Test

@KotlinCoreEnvironmentTest
internal class UsePluginManagerTest(private val env: KotlinEnvironmentContainer) {
  private val rule = UsePluginManager(Config.empty)

  @Test
  fun `Don't report pluginManager access`() {
    val code =
      """
      pluginManager.apply("some.plugin")
      pluginManager.hasPlugin("some.plugin")
      pluginManager.withPlugin("some.plugin") { }
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasNoFindings()
  }

  @Test
  fun `Don't report unrelated plugins property`() {
    val code =
      """
      val plugins: List<String> = emptyList()
      println(plugins)
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasNoFindings()
  }

  @Test
  fun `Report plugins with implicit receiver`() {
    val code =
      """
      plugins.apply("some.plugin")
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasNumFindings(expected = 1).onFirstFinding {
      messageContains("pluginManager")
    }
  }

  @Test
  fun `Report plugins with explicit this receiver`() {
    val code =
      """
      this.plugins.apply("some.plugin")
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasNumFindings(expected = 1).onFirstFinding {
      messageContains("pluginManager")
    }
  }

  @Test
  fun `Report plugins with explicit Project receiver`() {
    val code =
      """
      val p: Project = TODO()
      p.plugins.apply("some.plugin")
      """
        .trimIndent()

    assertThat(rule).lintedAsKts(env, code).hasNumFindings(expected = 1).onFirstFinding {
      messageContains("pluginManager")
    }
  }

  @Test
  fun `Report multiple plugins accesses`() {
    val code =
      """
      plugins.apply("plugin.one")
      this.plugins.hasPlugin("plugin.two")
      val p: Project = TODO()
      p.plugins.withType<Plugin<*>> { }
      """
        .trimIndent()

    assertThat(rule)
      .lintedAsKts(env, code)
      .hasNumFindings(expected = 3)
      .onFirstFinding { messageContains("pluginManager") }
      .onSecondFinding { messageContains("pluginManager") }
      .onThirdFinding { messageContains("pluginManager") }
  }
}
