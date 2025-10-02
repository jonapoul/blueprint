/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.recipes

import com.squareup.sort.SortDependenciesExtension
import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

public fun Project.sortDependenciesBlueprint(
  insertBlankLines: Boolean = false,
) {
  with(pluginManager) {
    apply(SortDependenciesPlugin::class.java)
  }

  extensions.configure<SortDependenciesExtension> {
    this.insertBlankLines.set(insertBlankLines)
  }
}
