package blueprint.recipes

public val DEFAULT_COMPOSE_EXPERIMENTAL_APIS: List<String> = listOf(
  "androidx.compose.foundation.ExperimentalFoundationApi",
  "androidx.compose.ui.ExperimentalComposeUiApi",
)

public val DEFAULT_KOTLIN_FREE_COMPILER_ARGS: List<String> = listOf(
  "-Xsam-conversions=class", // used in sample project plugintemplate. Not sure why
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
