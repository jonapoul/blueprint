/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.recipes

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel

public fun Project.ideaBlueprint() {
  with(pluginManager) {
    apply(IdeaPlugin::class.java)
  }

  extensions.configure<IdeaModel> {
    module.apply {
      isDownloadSources = true
      isDownloadJavadoc = true
    }
  }
}
