package blueprint.ktlint

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

internal class KtlintProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.ktlint"

  val android = boolProperty(key = "android", default = false)
  val coloredOutput = boolProperty(key = "coloredOutput", default = true)
  val enableExperimentalRules = boolProperty(key = "enableExperimentalRules", default = false)
  val ignoreFailures = boolProperty(key = "ignoreFailures", default = false)
  val verbose = boolProperty(key = "verbose", default = false)
  val version = stringProperty(key = "version", default = BuildConfig.KTLINT_CLI_VERSION)
}
