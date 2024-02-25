package blueprint.kover

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

public class VersionProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix: String = "blueprint.versions"

  public val alakazam: String = stringProperty(key = "alakazam", default = BuildConfig.VERSION_ALAKAZAM)
  public val androidxArch: String = stringProperty(key = "androidx.arch", default = BuildConfig.VERSION_ANDROIDX_ARCH)
  public val androidxCoreKtx: String = stringProperty(key = "androidx.coreKtx", BuildConfig.VERSION_ANDROIDX_COREKTX)
  public val androidxJunit: String = stringProperty(key = "androidx.junit", BuildConfig.VERSION_ANDROIDX_JUNIT)
  public val androidxRules: String = stringProperty(key = "androidx.rules", BuildConfig.VERSION_ANDROIDX_RULES)
  public val androidxRunner: String = stringProperty(key = "androidx.runner", BuildConfig.VERSION_ANDROIDX_RUNNER)
  public val coroutines: String = stringProperty(key = "coroutines", default = BuildConfig.VERSION_COROUTINES)
  public val junit: String = stringProperty(key = "junit", default = BuildConfig.VERSION_JUNIT)
  public val kotlin: String = stringProperty(key = "kotlin", default = BuildConfig.VERSION_KOTLIN)
  public val mockk: String = stringProperty(key = "mockk", default = BuildConfig.VERSION_MOCKK)
  public val robolectric: String = stringProperty(key = "robolectric", default = BuildConfig.VERSION_ROBOLECTRIC)
  public val turbine: String = stringProperty(key = "turbine", default = BuildConfig.VERSION_TURBINE)
}
