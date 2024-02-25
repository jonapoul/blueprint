package blueprint.doctor

import com.osacky.doctor.DoctorExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

public class DoctorBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    if (target != target.rootProject) {
      throw GradleException("Must apply Doctor plugin to root project, not ${target.path}")
    }

    target.pluginManager.apply("com.osacky.doctor")
    val properties = DoctorProperties(target)
    target.configureExtension(properties)
  }

  private fun Project.configureExtension(properties: DoctorProperties) {
    extensions.configure(DoctorExtension::class.java) { ext ->
      ext.javaHome {
        it.ensureJavaHomeMatches.set(properties.ensureJavaHomeMatches)
        it.ensureJavaHomeIsSet.set(properties.ensureJavaHomeIsSet)
        it.failOnError.set(properties.failOnError)
      }

      ext.disallowMultipleDaemons.set(properties.disallowMultipleDaemons)
      ext.downloadSpeedWarningThreshold.set(properties.downloadSpeedWarningThreshold)
      ext.failOnEmptyDirectories.set(properties.failOnEmptyDirectories)
      ext.allowBuildingAllAndroidAppsSimultaneously.set(properties.allowBuildingAllAndroidAppsSimultaneously)
    }
  }
}
