plugins {
  `kotlin-dsl`
}

val javaVersion = libs.versions.java.get()

java {
  sourceCompatibility = JavaVersion.toVersion(javaVersion)
  targetCompatibility = JavaVersion.toVersion(javaVersion)
}

kotlin {
  jvmToolchain(javaVersion.toInt())
}

dependencies {
  implementation(libs.plugin.dependencyGuard)
  implementation(libs.plugin.kotlin)

  // https://stackoverflow.com/a/70878181/15634757
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
