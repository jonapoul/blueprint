package blueprint.detekt.rules

import blueprint.detekt.utils.isSubtypeOf
import blueprint.detekt.utils.report
import dev.detekt.api.Config
import dev.detekt.api.Configuration
import dev.detekt.api.RequiresAnalysisApi
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.analysis.api.resolution.singleFunctionCallOrNull
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtCallExpression

/**
 * Flags eager collection operators on Gradle's `DomainObjectCollection` types in favour of their
 * lazy equivalents.
 *
 * Eager operators force realisation of container elements at configuration time, defeating task
 * avoidance and increasing build times. This rule catches both Gradle-specific eager APIs (`all`,
 * `whenObjectAdded`) and Kotlin stdlib collection functions (`forEach`, `filter`, `map`, `first`,
 * etc.) when called on Gradle collections.
 *
 * Noncompliant:
 * ```kotlin
 * tasks.all { enabled = true }
 * configurations.forEach { println(it.name) }
 * val filtered = tasks.filter { it.name.startsWith("test") }
 * ```
 *
 * Compliant:
 * ```kotlin
 * tasks.configureEach { enabled = true }
 * val testTasks = tasks.matching { it.name.startsWith("test") }
 * ```
 */
internal class LazyCollectionOperators(config: Config) :
  Rule(
    config = config,
    description =
      "When working with Gradle collection objects, prefer lazy operators over eager ones",
  ),
  RequiresAnalysisApi {
  @Configuration("Additional eager method names to flag on DomainObjectCollection types")
  private val additionalEagerMethods: List<String> by config(defaultValue = emptyList())

  override fun visitCallExpression(expression: KtCallExpression) {
    super.visitCallExpression(expression)
    val calleeName = expression.calleeExpression?.text ?: return

    analyze(expression) {
      val call = expression.resolveToCall()?.singleFunctionCallOrNull() ?: return@analyze
      val applied = call.partiallyAppliedSymbol

      // Gradle-specific eager member methods with targeted replacement suggestions
      val gradleReplacement = GRADLE_REPLACEMENTS[calleeName]
      if (gradleReplacement != null || calleeName in additionalEagerMethods) {
        val dispatchType = applied.dispatchReceiver?.type ?: return@analyze
        if (dispatchType.isSubtypeOf(DomainObjectCollection)) {
          val message =
            if (gradleReplacement != null) {
              "Prefer $gradleReplacement over $calleeName for lazy configuration"
            } else {
              "Avoid calling eager method $calleeName on Gradle collections"
            }
          expression.report(message)
        }
        return@analyze
      }

      // Stdlib collection extensions called on Gradle collections: only check extension receivers
      // so that Gradle member methods (configureEach, named, matching, etc.) are not flagged.
      val extensionReceiverType = applied.extensionReceiver?.type ?: return@analyze
      if (
        extensionReceiverType.isSubtypeOf(DomainObjectCollection) &&
          extensionReceiverType.isSubtypeOf(KotlinIterable)
      ) {
        expression.report(
          "Avoid calling $calleeName on Gradle collections - it forces eager realization of all elements"
        )
      }
    }
  }

  private companion object {
    val DomainObjectCollection = FqName("org.gradle.api.DomainObjectCollection")
    val KotlinIterable = FqName("kotlin.collections.Iterable")

    val GRADLE_REPLACEMENTS = mapOf("all" to "configureEach", "whenObjectAdded" to "configureEach")
  }
}
