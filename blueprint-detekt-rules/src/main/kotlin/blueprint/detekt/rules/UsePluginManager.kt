package blueprint.detekt.rules

import blueprint.detekt.utils.GRADLE_PROJECT
import blueprint.detekt.utils.isSubtypeOf
import blueprint.detekt.utils.report
import dev.detekt.api.Config
import dev.detekt.api.RequiresAnalysisApi
import dev.detekt.api.Rule
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.analysis.api.resolution.KaVariableAccessCall
import org.jetbrains.kotlin.analysis.api.resolution.singleCallOrNull
import org.jetbrains.kotlin.psi.KtSimpleNameExpression

/**
 * Flags accesses to `Project.plugins` in favour of `Project.pluginManager`.
 *
 * `PluginContainer` (returned by `Project.plugins`) extends `PluginManager` but also extends
 * `Collection`, exposing mutating APIs (`add`, `remove`, etc.) that should never be called directly
 * — Gradle manages the container lifecycle internally. Using the narrower `PluginManager` interface
 * makes intent clearer and is consistent with Gradle's own documentation and tooling
 * recommendations.
 *
 * Noncompliant:
 * ```kotlin
 * plugins.apply("com.android.application")
 * this.plugins.hasPlugin("org.jetbrains.kotlin.android")
 * ```
 *
 * Compliant:
 * ```kotlin
 * pluginManager.apply("com.android.application")
 * pluginManager.hasPlugin("org.jetbrains.kotlin.android")
 * ```
 */
internal class UsePluginManager(config: Config) :
  Rule(
    config = config,
    description = "Project.pluginManager should be preferred over Project.plugins",
  ),
  RequiresAnalysisApi {
  override fun visitSimpleNameExpression(expression: KtSimpleNameExpression) {
    super.visitSimpleNameExpression(expression)
    if (expression.getReferencedName() != PLUGINS) return

    analyze(expression) {
      val receiverType =
        expression.resolveToCall()?.singleCallOrNull<KaVariableAccessCall>()?.dispatchReceiver?.type
          ?: return@analyze

      if (receiverType.isSubtypeOf(GRADLE_PROJECT)) {
        expression.report(description)
      }
    }
  }

  private companion object {
    const val PLUGINS = "plugins"
  }
}
