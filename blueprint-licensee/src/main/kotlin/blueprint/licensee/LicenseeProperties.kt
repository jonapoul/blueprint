package blueprint.licensee

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

internal class LicenseeProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.licensee"

  val useDefaultSpdxIds = boolProperty(key = "useDefaultSpdxIds", default = true)
  val extraSpdxIds = stringListPropertyOrNull(key = "extraSpdxIds")

  val useDefaultUrls = boolProperty(key = "useDefaultUrls", default = true)
  val extraUrls = stringListPropertyOrNull(key = "extraUrls")
}
