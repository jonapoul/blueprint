package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin

public class ComposeBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(plugins) {
      apply(ComposeCompilerGradleSubplugin::class)
    }

    extensions.findByType(CommonExtension::class.java)?.apply {
      buildFeatures {
        compose = true
      }
    }

    val writeMetrics = boolPropertyOrElse(key = "blueprint.compose.writeMetrics", default = true)

    extensions.configure<ComposeCompilerGradlePluginExtension> {
      if (writeMetrics) {
        val metricReportDir = project.layout.buildDirectory.dir("compose_metrics")
        metricsDestination.set(metricReportDir)
        reportsDestination.set(metricReportDir)
      }
    }
  }
}
