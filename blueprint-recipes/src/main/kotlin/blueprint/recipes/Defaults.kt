package blueprint.recipes

import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

public val DEFAULT_COMPOSE_EXPERIMENTAL_APIS: List<String> = listOf(
  "androidx.compose.foundation.ExperimentalFoundationApi",
  "androidx.compose.ui.ExperimentalComposeUiApi",
)

public val DEFAULT_KOTLIN_FREE_COMPILER_ARGS: List<String> = listOf(
  "-Xjvm-default=all-compatibility",
  "-opt-in=kotlin.RequiresOptIn",
)

public val DEFAULT_KOVER_EXCLUDE_CLASSES: List<String> = listOf(
  "*.BuildConfig",
  "*_Factory*",
  "*Hilt_*",
)

public val DEFAULT_KOVER_EXCLUDE_PACKAGES: List<String> = listOf(
  "*hilt_aggregated_deps.*",
  "*.di.*",
)

public val DEFAULT_KOVER_EXCLUDE_ANNOTATIONS: List<String> = listOf(
  "androidx.compose.runtime.Composable",
  "dagger.Generated",
  "dagger.Module",
  "dagger.Provides",
  "javax.annotation.processing.Generated",
)

public val DEFAULT_COMPOSE_PLATFORMS: Set<KotlinPlatformType> = setOf(
  KotlinPlatformType.androidJvm,
)

public val Project.defaultStabilityFile: RegularFile
  get() = rootProject.layout.projectDirectory.file("compose-stability.conf")

public val DEFAULT_LICENSE_SPDX_IDS: List<String> = listOf(
  "Apache-2.0",
  "MIT",
  "BSD-3-Clause",
  "BSD-2-Clause",
  "EPL-1.0",
)

public val DEFAULT_POWER_ASSERT_FUNCTIONS: Set<String> = setOf(
  "kotlin.assert",
  "kotlin.test.assertEquals",
  "kotlin.test.assertFalse",
  "kotlin.test.assertIs",
  "kotlin.test.assertNotNull",
  "kotlin.test.assertNull",
  "kotlin.test.assertTrue",
)
