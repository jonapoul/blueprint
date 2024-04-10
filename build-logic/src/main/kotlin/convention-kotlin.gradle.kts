import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("com.dropbox.dependency-guard")
}

val libs = the<LibrariesForLibs>()
val javaVersion = libs.versions.java.get()

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = javaVersion
    freeCompilerArgs += listOf(
      "-Xsam-conversions=class",
      "-Xexplicit-api=strict",
    )
  }
}

extensions.configure<KotlinTopLevelExtension> {
  explicitApi()
  jvmToolchain(javaVersion.toInt())
}

dependencyGuard {
  configuration("compileClasspath")
  configuration("runtimeClasspath")
  configuration("testCompileClasspath")
  configuration("testRuntimeClasspath")
}
