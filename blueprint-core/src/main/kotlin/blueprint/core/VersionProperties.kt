package blueprint.core

import org.gradle.api.Project

public class VersionProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix: String = "blueprint.versions"

  public val alakazam: String = stringProperty(key = "alakazam", BuildConfig.VERSION_ALAKAZAM)
  public val androidxArch: String = stringProperty(key = "androidx.arch", BuildConfig.VERSION_ANDROIDX_ARCH)
  public val androidxCoreKtx: String = stringProperty(key = "androidx.coreKtx", BuildConfig.VERSION_ANDROIDX_COREKTX)
  public val androidxJunit: String = stringProperty(key = "androidx.junit", BuildConfig.VERSION_ANDROIDX_JUNIT)
  public val androidxRules: String = stringProperty(key = "androidx.rules", BuildConfig.VERSION_ANDROIDX_RULES)
  public val androidxRunner: String = stringProperty(key = "androidx.runner", BuildConfig.VERSION_ANDROIDX_RUNNER)
  public val composeBom: String = stringProperty(key = "compose.bom", BuildConfig.VERSION_COMPOSE_BOM)
  public val composeCompiler: String = stringProperty(key = "compose.compiler", BuildConfig.VERSION_COMPOSE_COMPILER)
  public val coroutines: String = stringProperty(key = "coroutines", BuildConfig.VERSION_COROUTINES)
  public val desugaring: String = stringProperty(key = "desugaring", BuildConfig.VERSION_DESUGARING)
  public val dagger: String = stringProperty(key = "dagger", BuildConfig.VERSION_DAGGER)
  public val junit: String = stringProperty(key = "junit", BuildConfig.VERSION_JUNIT)
  public val kotlin: String = stringProperty(key = "kotlin", BuildConfig.VERSION_KOTLIN)
  public val mockk: String = stringProperty(key = "mockk", BuildConfig.VERSION_MOCKK)
  public val robolectric: String = stringProperty(key = "robolectric", BuildConfig.VERSION_ROBOLECTRIC)
  public val turbine: String = stringProperty(key = "turbine", BuildConfig.VERSION_TURBINE)
}
