@file:Suppress("UnstableApiUsage", "LongParameterList")

package blueprint.recipes

import blueprint.core.getValue
import blueprint.core.provideDelegate
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public fun Project.androidComposeBlueprint(
  composeBomVersion: Provider<String>,
  composeLintVersion: Provider<String>?,
  experimentalApis: List<String> = DEFAULT_COMPOSE_EXPERIMENTAL_APIS,
  writeMetrics: Boolean = true,
  targetPlatforms: Set<KotlinPlatformType> = DEFAULT_COMPOSE_PLATFORMS,
  stabilityFile: RegularFile = defaultStabilityFile,
) {
  androidComposeBlueprint(
    composeBomVersion = composeBomVersion.get(),
    composeLintVersion = composeLintVersion?.get(),
    experimentalApis = experimentalApis,
    writeMetrics = writeMetrics,
    targetPlatforms = targetPlatforms,
    stabilityFile = stabilityFile,
  )
}

public fun Project.androidComposeBlueprint(
  composeBomVersion: VersionConstraint,
  composeLintVersion: VersionConstraint?,
  experimentalApis: List<String> = DEFAULT_COMPOSE_EXPERIMENTAL_APIS,
  writeMetrics: Boolean = true,
  targetPlatforms: Set<KotlinPlatformType> = DEFAULT_COMPOSE_PLATFORMS,
  stabilityFile: RegularFile = defaultStabilityFile,
) {
  androidComposeBlueprint(
    composeBomVersion = composeBomVersion.toString(),
    composeLintVersion = composeLintVersion?.toString(),
    experimentalApis = experimentalApis,
    writeMetrics = writeMetrics,
    targetPlatforms = targetPlatforms,
    stabilityFile = stabilityFile,
  )
}

public fun Project.androidComposeBlueprint(
  composeBomVersion: String,
  composeLintVersion: String?,
  experimentalApis: List<String> = DEFAULT_COMPOSE_EXPERIMENTAL_APIS,
  writeMetrics: Boolean = true,
  targetPlatforms: Set<KotlinPlatformType> = DEFAULT_COMPOSE_PLATFORMS,
  stabilityFile: RegularFile = defaultStabilityFile,
) {
  with(plugins) {
    apply(KotlinAndroidPluginWrapper::class)
    apply(ComposeCompilerGradleSubplugin::class)
  }

  extensions.getByType(CommonExtension::class).apply {
    buildFeatures {
      compose = true
    }
  }

  extensions.configure<ComposeCompilerGradlePluginExtension> {
    if (writeMetrics) {
      val metricReportDir = project.layout.buildDirectory.dir("compose_metrics").get().asFile
      metricsDestination.set(metricReportDir)
      reportsDestination.set(metricReportDir)
    }

    stabilityConfigurationFiles.add(stabilityFile)
    targetKotlinPlatforms.set(targetPlatforms)
  }

  tasks.withType<KotlinCompile> {
    compilerOptions {
      freeCompilerArgs.addAll(experimentalApis.map { "-opt-in=$it" })
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
}
