@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  id("convention-kotlin")
  alias(libs.plugins.buildConfig)
  alias(libs.plugins.dokka)
  alias(libs.plugins.publish)
  `java-gradle-plugin`
}

dependencies {
  compileOnly(gradleApi())
  api(projects.blueprintCore)
  api(projects.blueprintKotlin)
  api(libs.plugin.agp)
  api(libs.plugin.androidCacheFix)
}

gradlePlugin {
  plugins {
    create("blueprint-android-base") {
      id = "dev.jonpoulton.blueprint.android.base"
      displayName = "Android Base Blueprint"
      description = "Gradle plugin to apply and configure Android"
      implementationClass = "blueprint.android.AndroidBaseBlueprintPlugin"
    }
    create("blueprint-android-library") {
      id = "dev.jonpoulton.blueprint.android.library"
      displayName = "Android Library Blueprint"
      description = "Gradle plugin to apply and configure Android libraries"
      implementationClass = "blueprint.android.AndroidLibBlueprintPlugin"
    }
    create("blueprint-android-application") {
      id = "dev.jonpoulton.blueprint.android.application"
      displayName = "Android App Blueprint"
      description = "Gradle plugin to apply and configure Android apps"
      implementationClass = "blueprint.android.AndroidAppBlueprintPlugin"
    }
    create("blueprint-android-desugaring") {
      id = "dev.jonpoulton.blueprint.android.desugaring"
      displayName = "Android Desugaring Blueprint"
      description = "Gradle plugin to configure Android desugaring"
      implementationClass = "blueprint.android.AndroidDesugaringBlueprintPlugin"
    }
    create("blueprint-android-resources") {
      id = "dev.jonpoulton.blueprint.android.resources"
      displayName = "Android Resources Blueprint"
      description = "Gradle plugin to configure Android resource modules"
      implementationClass = "blueprint.android.AndroidResourcesBlueprintPlugin"
    }
    create("blueprint-android-compose") {
      id = "dev.jonpoulton.blueprint.android.compose"
      displayName = "Android Compose Blueprint"
      description = "Gradle plugin to configure Android Jetpack Compose modules"
      implementationClass = "blueprint.android.AndroidComposeBlueprintPlugin"
    }
  }
}

buildConfig {
  packageName("blueprint.android")
  useKotlinOutput { internalVisibility = true }
  buildConfigField("String", "DESUGARING_VERSION", "\"${libs.versions.android.desugaring.get()}\"")
  buildConfigField("String", "COMPOSE_BOM_VERSION", "\"${libs.versions.androidx.compose.bom.get()}\"")
  buildConfigField("String", "COMPOSE_COMPILER_VERSION", "\"${libs.versions.androidx.compose.compiler.get()}\"")
}
