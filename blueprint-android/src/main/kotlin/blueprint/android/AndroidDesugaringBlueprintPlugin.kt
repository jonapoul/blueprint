package blueprint.android

import blueprint.core.VersionProperties
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidDesugaringBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val versions = VersionProperties(target)

    target.extensions.configure(CommonExtension::class.java) { ext ->
      ext.compileOptions {
        isCoreLibraryDesugaringEnabled = true
      }
    }

    target.dependencies.add(
      "coreLibraryDesugaring",
      "com.android.tools:desugar_jdk_libs:${versions.desugaring}"
    )
  }
}
