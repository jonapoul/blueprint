package blueprint.core

import org.gradle.api.Plugin
import org.gradle.api.Project

public class BlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    // no-op, just brings the others into the classpath
  }
}
