package blueprint.versions

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

public class VersionsBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("com.github.ben-manes.versions")
    val properties = VersionsProperties(target)
    target.configureTask(properties)
  }

  private fun Project.configureTask(properties: VersionsProperties) {
    tasks.withType(DependencyUpdatesTask::class.java) { task ->
      task.rejectVersionIf { component ->
        !component.candidate.version.isStable(properties) && component.currentVersion.isStable(properties)
      }
    }
  }

  private fun String.isStable(properties: VersionsProperties): Boolean =
    properties.unstableVersions.none { lowercase().contains(it) }
}
