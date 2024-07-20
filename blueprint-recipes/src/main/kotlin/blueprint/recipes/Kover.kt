package blueprint.recipes

import blueprint.core.intProperty
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.KoverReportExtension
import kotlinx.kover.gradle.plugin.dsl.MetricType
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

@Suppress("LongParameterList")
public fun Project.koverBlueprint(
  minCoverage: Int = intProperty(key = "kover.minCoverage"),
  metricType: MetricType = MetricType.INSTRUCTION,
  aggregationType: AggregationType = AggregationType.COVERED_PERCENTAGE,
  excludedClasses: List<String> = DEFAULT_KOVER_EXCLUDE_CLASSES,
  excludedPackages: List<String> = DEFAULT_KOVER_EXCLUDE_PACKAGES,
  excludedAnnotations: List<String> = DEFAULT_KOVER_EXCLUDE_ANNOTATIONS,
) {
  with(plugins) {
    apply("org.jetbrains.kotlinx.kover")
  }

  val isAtak = project.plugins.any { it.javaClass.simpleName == "TakDevPlugin" }
  val isAndroid = project.extensions.findByType(LibraryExtension::class) != null ||
    project.extensions.findByType(AppExtension::class) != null

  val androidVariant = when {
    isAtak -> "civDebug"
    isAndroid -> "debug"
    else -> null // kotlin JVM, no variant to merge with
  }

  extensions.getByType(KoverReportExtension::class).apply {
    defaults { defaults ->
      if (androidVariant != null) {
        defaults.mergeWith(androidVariant)
      }

      defaults.filters { filters ->
        filters.excludes { filter ->
          filter.classes(excludedClasses)
          filter.packages(excludedPackages)
          excludedAnnotations.forEach { filter.annotatedBy(it) }
        }
      }

      defaults.html {
        it.onCheck = true
      }

      defaults.log {
        it.onCheck = true
        it.coverageUnits = metricType
        it.aggregationForGroup = aggregationType
      }

      defaults.verify { verify ->
        verify.onCheck = project == rootProject
        verify.rule { rule ->
          rule.isEnabled = true
          rule.bound {
            it.minValue = minCoverage
            it.metric = metricType
            it.aggregation = aggregationType
          }
        }
      }
    }

    if (androidVariant != null) {
      androidReports(androidVariant) {
        // No-op, all same config as default
      }
    }
  }


  val kover = configurations.getByName("kover")
  rootProject.dependencies {
    // Include this module in test coverage
    kover(project)
  }
}
