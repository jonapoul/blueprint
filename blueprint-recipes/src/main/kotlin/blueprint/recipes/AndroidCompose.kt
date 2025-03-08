@file:Suppress("UnstableApiUsage")

package blueprint.recipes

import blueprint.core.getValue
import blueprint.core.provideDelegate
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionConstraint
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
  androidComposeBlueprint(
    composeCompilerVersion = composeCompilerVersion.get(),
    composeBomVersion = composeBomVersion.get(),
    composeLintVersion = composeLintVersion?.get(),
    experimentalApis = experimentalApis,
    writeMetrics = writeMetrics,
  )
}

public fun Project.androidComposeBlueprint(
  composeCompilerVersion: VersionConstraint,
  composeBomVersion: VersionConstraint,
  composeLintVersion: VersionConstraint?,
  experimentalApis: List<String> = DEFAULT_COMPOSE_EXPERIMENTAL_APIS,
  writeMetrics: Boolean = true,
) {
  androidComposeBlueprint(
    composeCompilerVersion = composeCompilerVersion.toString(),
    composeBomVersion = composeBomVersion.toString(),
    composeLintVersion = composeLintVersion?.toString(),
    experimentalApis = experimentalApis,
    writeMetrics = writeMetrics,
  )
}

public fun Project.androidComposeBlueprint(
  composeCompilerVersion: String,
  composeBomVersion: String,
  composeLintVersion: String?,
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
      kotlinCompilerExtensionVersion = composeCompilerVersion
    }
  }

  val implementation by configurations
  val lintChecks by configurations

  dependencies {
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    if (composeLintVersion != null) {
      lintChecks("com.slack.lint.compose:compose-lint-checks:$composeLintVersion")
    }
  }

  tasks.withType<KotlinCompile> {
    compilerOptions {
      freeCompilerArgs.addAll(experimentalApis.map { "-opt-in=$it" })

      if (writeMetrics) {
        // From https://chrisbanes.me/posts/composable-metrics/
        val propertyRoot = "plugin:androidx.compose.compiler.plugins.kotlin"
        val metricReportDir = project.layout.buildDirectory.dir("compose_metrics").get().asFile
        freeCompilerArgs.addAll(listOf("-P", "$propertyRoot:reportsDestination=${metricReportDir.absolutePath}"))
        freeCompilerArgs.addAll(listOf("-P", "$propertyRoot:metricsDestination=${metricReportDir.absolutePath}"))
      }
    }
  }
}
