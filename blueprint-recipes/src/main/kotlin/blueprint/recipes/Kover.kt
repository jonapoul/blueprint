/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import blueprint.core.intProperty
import kotlinx.kover.gradle.plugin.KoverGradlePlugin
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("LongParameterList", "NestedBlockDepth")
public fun Project.koverBlueprint(
  minCoverage: Int = intProperty(key = "blueprint.kover.minCoverage"),
  useJacoco: Boolean = boolPropertyOrElse(key = "blueprint.kover.useJacoco", default = false),
  coverageUnit: CoverageUnit = CoverageUnit.INSTRUCTION,
  aggregationType: AggregationType = AggregationType.COVERED_PERCENTAGE,
  excludedClasses: List<String> = DEFAULT_KOVER_EXCLUDE_CLASSES,
  excludedPackages: List<String> = DEFAULT_KOVER_EXCLUDE_PACKAGES,
  excludedAnnotations: List<String> = DEFAULT_KOVER_EXCLUDE_ANNOTATIONS,
) {
  with(plugins) {
    apply(KoverGradlePlugin::class)
  }

  extensions.getByType(KoverProjectExtension::class).apply {
    this.useJacoco.set(useJacoco)
    reports.apply {
      total.apply {
        filters.apply {
          excludes.apply {
            classes(excludedClasses)
            packages(excludedPackages)
            excludedAnnotations.forEach { annotatedBy(it) }
          }
        }

        html.apply {
          onCheck.set(true)
        }

        log.apply {
          onCheck.set(true)
          coverageUnits.set(coverageUnit)
          aggregationForGroup.set(aggregationType)
        }
      }

      verify.apply {
        rule { r ->
          r.disabled.set(project != project.rootProject)
          r.bound { b ->
            b.minValue.set(minCoverage)
            b.coverageUnits.set(coverageUnit)
            b.aggregationForGroup.set(aggregationType)
          }
        }
      }
    }
  }

  val kover = configurations.getByName("kover")
  rootProject.dependencies {
    // Include this module in test coverage
    kover(project)
  }
}
