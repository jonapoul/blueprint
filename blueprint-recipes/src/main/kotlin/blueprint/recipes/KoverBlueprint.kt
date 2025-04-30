@file:Suppress("NestedBlockDepth")

package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import blueprint.core.intProperty
import kotlinx.kover.gradle.plugin.KoverGradlePlugin
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

public class KoverBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KoverGradlePlugin::class)
    }

    val minCoverage = intProperty(key = "blueprint.kover.minCoverage")
    val useJacoco = boolPropertyOrElse(key = "blueprint.kover.useJacoco", default = false)

    extensions.getByType(KoverProjectExtension::class).apply {
      this.useJacoco.set(useJacoco)
      reports.apply {
        total.apply {
          filters.apply {
            excludes.apply {
              // classes(excludedClasses)
              // packages(excludedPackages)
              // excludedAnnotations.forEach { annotatedBy(it) }
            }
          }

          html.apply {
            onCheck.set(true)
          }

          log.apply {
            onCheck.set(true)
            // coverageUnits.set(coverageUnit)
            // aggregationForGroup.set(aggregationType)
          }
        }

        verify.apply {
          rule { r ->
            r.disabled.set(project != project.rootProject)
            r.bound { b ->
              b.minValue.set(minCoverage)
              // b.coverageUnits.set(coverageUnit)
              // b.aggregationForGroup.set(aggregationType)
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
}
