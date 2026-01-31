package blueprint.core

import org.gradle.api.Project

public fun Project.kspAllConfigs(dependency: Any) {
  with(dependencies) {
    configurations
      .matching { config -> config.name.startsWith("ksp") && config.name != "ksp" }
      .configureEach {
        logger.info("Applying {} to config {}", dependency, name)
        add(name, dependency)
      }
  }
}
