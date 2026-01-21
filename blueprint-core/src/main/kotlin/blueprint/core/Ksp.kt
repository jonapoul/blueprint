package blueprint.core

import org.gradle.api.Project

public fun Project.kspAllConfigs(dependency: Any) {
  dependencies.apply {
    configurations
      .map { config -> config.name }
      .filter { name -> name.startsWith("ksp") && name != "ksp" }
      .ifEmpty { error("No KSP configurations found in $path") }
      .onEach { name -> logger.info("Applying $dependency to config $name") }
      .forEach { name -> add(name, dependency) }
  }
}
