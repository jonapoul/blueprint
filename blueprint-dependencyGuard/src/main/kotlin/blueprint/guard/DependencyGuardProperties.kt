package blueprint.guard

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

internal class DependencyGuardProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.guard"

  val customConfigurations = stringListPropertyOrNull(key = "customConfigurations")
}
