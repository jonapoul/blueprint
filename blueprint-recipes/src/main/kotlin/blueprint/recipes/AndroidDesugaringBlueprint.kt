package blueprint.recipes

import blueprint.core.getVersion
import blueprint.core.libs
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

public class AndroidDesugaringBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    extensions.getByType(CommonExtension::class).apply {
      compileOptions {
        isCoreLibraryDesugaringEnabled = true
      }

      val desugaringVersion = libs.getVersion("android.desugaring")
      val coreLibraryDesugaring = configurations.getByName("coreLibraryDesugaring")

      dependencies {
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugaringVersion")
      }
    }
  }
}
