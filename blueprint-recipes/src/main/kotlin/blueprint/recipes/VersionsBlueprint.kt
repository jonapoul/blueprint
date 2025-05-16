package blueprint.recipes

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType

public class VersionsBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(VersionsPlugin::class)
    }

    tasks.withType<DependencyUpdatesTask>().configureEach { task ->
      task.rejectVersionIf { !it.candidate.version.isStable() && it.currentVersion.isStable() }
    }
  }

  private fun String.isStable(): Boolean = listOf("alpha", "beta", "rc").none { contains(it, ignoreCase = true) }
}
