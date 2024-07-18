@file:Suppress("UnstableApiUsage")

package blueprint.recipes

import blueprint.core.getValue
import blueprint.core.provideDelegate
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public fun Project.androidComposeBlueprint(
  composeCompilerVersion: Provider<String>,
  composeBomVersion: Provider<String>,
  composeLintVersion: Provider<String>?,
  experimentalApis: List<String> = DEFAULT_COMPOSE_EXPERIMENTAL_APIS,
  writeMetrics: Boolean = true,
) {
  with(plugins) {
    apply("org.jetbrains.kotlin.android")
  }

  extensions.getByType(CommonExtension::class).apply {
    buildFeatures {
      compose = true
    }

    composeOptions {
      kotlinCompilerExtensionVersion = composeCompilerVersion.get()
    }
  }

  val implementation by configurations
  val lintChecks by configurations

  dependencies {
    implementation(platform("androidx.compose:compose-bom:${composeBomVersion.get()}"))
    if (composeLintVersion != null) {
      lintChecks("com.slack.lint.compose:compose-lint-checks:${composeLintVersion.get()}")
    }
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs += experimentalApis.map { "-opt-in=$it" }

      if (writeMetrics) {
        // From https://chrisbanes.me/posts/composable-metrics/
        val propertyRoot = "plugin:androidx.compose.compiler.plugins.kotlin"
        val metricReportDir = project.layout.buildDirectory.dir("compose_metrics").get().asFile
        freeCompilerArgs += listOf("-P", "$propertyRoot:reportsDestination=${metricReportDir.absolutePath}")
        freeCompilerArgs += listOf("-P", "$propertyRoot:metricsDestination=${metricReportDir.absolutePath}")
      }
    }
  }
}
