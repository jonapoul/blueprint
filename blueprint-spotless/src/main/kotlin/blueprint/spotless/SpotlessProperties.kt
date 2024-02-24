package blueprint.spotless

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

internal class SpotlessProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.spotless"

  val miscFiles = stringProperty(key = "miscFiles", default = "*.gradle,.gitignore,*.pro")
}
