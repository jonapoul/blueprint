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
  api(projects.blueprintAndroid)
  api(projects.blueprintGit)
  api(projects.blueprintKotlin)
}

gradlePlugin {
  plugins {
    create("blueprint-atak-base") {
      id = "dev.jonpoulton.blueprint.atak.base"
      displayName = "ATAK Base Blueprint"
      description = "Gradle plugin to apply and configure ATAK"
      implementationClass = "blueprint.atak.AtakBaseBlueprintPlugin"
    }
    create("blueprint-atak-plugin") {
      id = "dev.jonpoulton.blueprint.atak.plugin"
      displayName = "ATAK Plugin Blueprint"
      description = "Gradle plugin to apply and configure ATAK plugins"
      implementationClass = "blueprint.atak.AtakPluginBlueprintPlugin"
    }
    create("blueprint-atak-library") {
      id = "dev.jonpoulton.blueprint.atak.library"
      displayName = "ATAK Library Blueprint"
      description = "Gradle plugin to apply and configure ATAK libraries"
      implementationClass = "blueprint.atak.AtakLibraryBlueprintPlugin"
    }
  }
}

buildConfig {
  packageName("blueprint.atak")
  buildConfigField("String", "ATAK_BASE_VERSION", "\"${libs.versions.atak.base.get()}\"")
  buildConfigField("String", "ATAK_SDK_VERSION", "\"${libs.versions.atak.sdk.get()}\"")
  useKotlinOutput { internalVisibility = true }
}
