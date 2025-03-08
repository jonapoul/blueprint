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

dependencies {
  compileOnly(libs.plugin.dependencyGuard)
  compileOnly(libs.plugin.dokka)
  compileOnly(libs.plugin.kotlin)
  compileOnly(libs.plugin.publish)
}

gradlePlugin {
  plugins {
    create(id = "blueprint.convention.kotlin", impl = "blueprint.gradle.ConventionKotlin")
    create(id = "blueprint.convention.publish", impl = "blueprint.gradle.ConventionPublish")
  }
}

fun NamedDomainObjectContainer<PluginDeclaration>.create(id: String, impl: String) = create(id) {
  this.id = id
  implementationClass = impl
}
