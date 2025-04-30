package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import com.squareup.sort.SortDependenciesExtension
import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

public class SortDependenciesBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(SortDependenciesPlugin::class)
    }

    val insertBlankLines = boolPropertyOrElse(key = "blueprint.sortDependencies.insertBlankLines", default = false)
    extensions.configure<SortDependenciesExtension> {
      this.insertBlankLines.set(insertBlankLines)
    }
  }
}
