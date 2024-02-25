package blueprint.versions

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

internal class VersionsProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.versions"

  val unstableVersions = stringListProperty(
    key = "unstableVersions",
    default = listOf("alpha", "beta", "rc"),
  )
}
