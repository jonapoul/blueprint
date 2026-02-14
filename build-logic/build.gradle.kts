@file:Suppress("UnstableApiUsage")

plugins { `kotlin-dsl` }

val javaVersion: Provider<Int> =
  providers.fileContents(layout.projectDirectory.file("../.java-version")).asText.map {
    it.trim().toInt()
  }

java {
  sourceCompatibility = JavaVersion.toVersion(javaVersion.get())
  targetCompatibility = JavaVersion.toVersion(javaVersion.get())
}

kotlin { jvmToolchain(javaVersion.get()) }

dependencies {
  fun compileOnlyPlugin(plugin: Provider<PluginDependency>) =
    compileOnly(plugin.map { p -> "${p.pluginId}:${p.pluginId}.gradle.plugin:${p.version}" })

  compileOnlyPlugin(libs.plugins.buildConfig)
  compileOnlyPlugin(libs.plugins.dependencyAnalysis)
  compileOnlyPlugin(libs.plugins.dependencyGuard)
  compileOnlyPlugin(libs.plugins.detekt)
  compileOnlyPlugin(libs.plugins.dokka)
  compileOnlyPlugin(libs.plugins.kotlin)
  compileOnlyPlugin(libs.plugins.publish)
}

gradlePlugin {
  plugins {
    create("convention") {
      id = "blueprint.convention"
      implementationClass = "blueprint.gradle.Convention"
    }
  }
}
