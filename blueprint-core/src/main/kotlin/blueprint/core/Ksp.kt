/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.core

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

public fun Project.kspAllConfigs(dependency: Any) {
  dependencies {
    configurations
      .map { config -> config.name }
      .filter { name -> name.startsWith("ksp") && name != "ksp" }
      .ifEmpty { error("No KSP configurations found in $path") }
      .onEach { name -> logger.info("Applying $dependency to config $name") }
      .forEach { name -> add(name, dependency) }
  }
}
