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
  api(libs.plugin.kotlin)
}

gradlePlugin {
  plugins {
    create("blueprint-kotlin-base") {
      id = "dev.jonpoulton.blueprint.kotlin.base"
      displayName = "Kotlin Base Blueprint"
      description = "Gradle plugin to apply and configure Kotlin"
      implementationClass = "blueprint.kotlin.KotlinBaseBlueprintPlugin"
    }
    create("blueprint-kotlin-jvm") {
      id = "dev.jonpoulton.blueprint.kotlin.jvm"
      displayName = "Kotlin JVM Blueprint"
      description = "Gradle plugin to apply and configure Kotlin JVM"
      implementationClass = "blueprint.kotlin.KotlinJvmBlueprintPlugin"
    }
    create("blueprint-kotlin-android") {
      id = "dev.jonpoulton.blueprint.kotlin.android"
      displayName = "Kotlin Android Blueprint"
      description = "Gradle plugin to apply and configure Kotlin Android"
      implementationClass = "blueprint.kotlin.KotlinAndroidBlueprintPlugin"
    }
  }
}

buildConfig {
  packageName("blueprint.kotlin")
  useKotlinOutput { internalVisibility = true }
  buildConfigField("String", "JAVA_VERSION", "\"${libs.versions.java.get()}\"")
}
