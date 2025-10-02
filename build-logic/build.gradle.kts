import java.util.Properties

plugins {
  `kotlin-dsl`
}

val rootProps = Properties()
val propsFile = File(rootDir.parentFile, "gradle.properties")
rootProps.load(propsFile.inputStream())
val javaVersion = rootProps["javaVersion"]?.toString()?.toInt() ?: error("Require javaVersion property")

java {
  sourceCompatibility = JavaVersion.toVersion(javaVersion)
  targetCompatibility = JavaVersion.toVersion(javaVersion)
}

kotlin {
  jvmToolchain(javaVersion)
}

fun DependencyHandler.plugin(dependency: Provider<PluginDependency>) =
  dependency.get().run { create("$pluginId:$pluginId.gradle.plugin:$version") }

dependencies {
  compileOnly(plugin(libs.plugins.dependencyAnalysis))
  compileOnly(plugin(libs.plugins.dokka))
  compileOnly(plugin(libs.plugins.kotlin))
  compileOnly(plugin(libs.plugins.publish))
  compileOnly(plugin(libs.plugins.spotless))
}

gradlePlugin {
  plugins {
    create(id = "blueprint.convention", impl = "blueprint.gradle.Convention")
  }
}

fun NamedDomainObjectContainer<PluginDeclaration>.create(
  id: String,
  impl: String,
) = create(id) {
  this.id = id
  implementationClass = impl
}
