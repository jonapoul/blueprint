package blueprint.recipes

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

public fun Project.dependencyVersionsBlueprint() {
  with(pluginManager) {
    apply(VersionsPlugin::class.java)
  }

  tasks.withType<DependencyUpdatesTask>().configureEach { task ->
    task.rejectVersionIf { !it.candidate.version.isStable() && it.currentVersion.isStable() }
  }
}

private fun String.isStable(): Boolean = listOf("alpha", "beta", "rc").none { contains(it, ignoreCase = true) }
