package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryBuildFeatures
import com.android.build.gradle.api.AndroidBasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

public class AndroidResourcesBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(AndroidBasePlugin::class)
    }

    val useViewBinding = boolPropertyOrElse(key = "blueprint.android.useViewBinding", default = false)

    extensions.getByType(CommonExtension::class).apply {
      buildFeatures {
        resValues = true
        viewBinding = useViewBinding

        if (this is LibraryBuildFeatures) {
          androidResources = true
        }
      }
    }
  }
}
