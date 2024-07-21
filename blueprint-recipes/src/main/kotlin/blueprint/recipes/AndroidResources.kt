@file:Suppress("UnstableApiUsage")

package blueprint.recipes

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryBuildFeatures
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

public fun Project.androidResourcesBlueprint(
  resValues: Boolean = true,
  androidResources: Boolean = resValues,
  viewBinding: Boolean,
) {
  extensions.getByType(CommonExtension::class).apply {
    buildFeatures {
      this.resValues = resValues
      this.viewBinding = viewBinding

      if (this is LibraryBuildFeatures) {
        this.androidResources = androidResources
      }
    }
  }
}
