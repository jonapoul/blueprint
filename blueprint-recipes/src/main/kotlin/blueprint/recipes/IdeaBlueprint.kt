package blueprint.recipes

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel

public class IdeaBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(IdeaPlugin::class)
    }

    extensions.configure<IdeaModel> {
      module.apply {
        isDownloadSources = true
        isDownloadJavadoc = true
      }
    }
  }
}
