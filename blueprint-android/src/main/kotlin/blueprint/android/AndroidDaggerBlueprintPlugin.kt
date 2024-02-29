package blueprint.android

import blueprint.core.VersionProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidDaggerBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val properties = AndroidProperties(target)
    val versions = VersionProperties(target)

    val config = when {
      properties.daggerKapt -> "kapt"
      properties.daggerKsp -> "ksp"
      else -> error("Need to enable KSP or KAPT for Dagger via Gradle properties!")
    }

    target.dependencies.add(config, "com.google.dagger:dagger-compiler:${versions.dagger}")
    target.dependencies.add("implementation", "com.google.dagger:dagger:${versions.dagger}")
  }
}
