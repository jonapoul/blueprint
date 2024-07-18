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
  // Generated files
  "*.BuildConfig",
  "*_Factory*",

  // DI Modules
  "*.di.*",
)

public val DEFAULT_KOVER_EXCLUDE_PACKAGES: List<String> = listOf(
  // TBC
)

public val DEFAULT_KOVER_EXCLUDE_ANNOTATIONS: List<String> = listOf(
  "androidx.compose.runtime.Composable",
)
