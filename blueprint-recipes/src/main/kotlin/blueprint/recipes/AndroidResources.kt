@file:Suppress("UnstableApiUsage")

package blueprint.recipes

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryAndroidResources
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
    }

    androidResources {
      if (this is LibraryAndroidResources) {
        enable = androidResources
      }
    }
  }
}
