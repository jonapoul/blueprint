package blueprint.analysis

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

internal class DependencyAnalysisProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.analysis"

  val bundleKotlinStdlib = boolProperty(key = "bundleKotlinStdlib", default = true)
  val excludeFileLambda = boolProperty(key = "excludeFileLambda", default = true)
  val ignoreKtx = boolProperty(key = "ignoreKtx", default = true)
  val strictMode = boolProperty(key = "strictMode", default = true)
}
