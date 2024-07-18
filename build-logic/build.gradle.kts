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

dependencies {
  implementation(libs.plugin.dependencyGuard)
  implementation(libs.plugin.dokka)
  implementation(libs.plugin.kotlin)
  implementation(libs.plugin.publish)

  // https://stackoverflow.com/a/70878181/15634757
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
