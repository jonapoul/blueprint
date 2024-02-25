package blueprint.kover

import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.MetricType

internal fun String.metricType(): MetricType = toEnum()

internal fun String.aggregationType(): AggregationType = toEnum()

internal inline fun <reified E : Enum<E>> String.toEnum(): E {
  val upper = uppercase()
  return enumValues<E>()
    .firstOrNull { it.name == upper }
    ?: error("No ${E::class.simpleName} found matching $this")
}
