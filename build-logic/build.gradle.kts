import java.util.Properties

plugins {
  `kotlin-dsl`
}

val rootProps = Properties()
val propsFile = File(rootDir.parentFile, "gradle.properties")
rootProps.load(propsFile.inputStream())
val javaVersion = rootProps["blueprint.javaVersion"]?.toString()?.toInt() ?: error("Require javaVersion property")

java {
  sourceCompatibility = JavaVersion.toVersion(javaVersion)
  targetCompatibility = JavaVersion.toVersion(javaVersion)
}

kotlin {
  jvmToolchain(javaVersion)
}

dependencies {
  fun DependencyHandler.compileOnlyPlugin(dependency: Provider<PluginDependency>) =
    compileOnly(dependency.get().run { create("$pluginId:$pluginId.gradle.plugin:$version") })

  compileOnlyPlugin(libs.plugins.detekt)
  compileOnlyPlugin(libs.plugins.dokka)
  compileOnlyPlugin(libs.plugins.kotlin)
  compileOnlyPlugin(libs.plugins.publish)
  compileOnlyPlugin(libs.plugins.spotless)
}

gradlePlugin {
  plugins {
    create("convention") {
      id = "blueprint.convention"
      implementationClass = "blueprint.gradle.Convention"
    }
  }
}
