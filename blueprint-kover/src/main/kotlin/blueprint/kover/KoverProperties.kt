package blueprint.kover

import blueprint.core.BlueprintProperties
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.MetricType
import org.gradle.api.Project

internal class KoverProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.kover"

  val minCoverage = intProperty(key = "coverage.minPercent", default = 50)

  val metricType = stringProperty(
    key = "coverage.metricType",
    default = MetricType.INSTRUCTION.name,
  ).metricType()

  val aggregationType = stringProperty(
    key = "coverage.aggregationType",
    default = AggregationType.COVERED_PERCENTAGE.name,
  ).aggregationType()

  val excludeComposables = boolProperty(key = "exclusions.compose", default = true)
  val excludedClassesFile = stringPropertyOrNull(key = "exclusions.file")
  val extraExclusions = stringPropertyOrNull(key = "exclusions.extrasFile")
}
