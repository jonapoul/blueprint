package blueprint.core

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * No-op, only used to bring the other utilities into the classpath.
 */
public class BlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = Unit
}
