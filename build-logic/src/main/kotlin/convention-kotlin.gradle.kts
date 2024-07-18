import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("com.dropbox.dependency-guard")
}

val libs = the<LibrariesForLibs>()
val javaVersion = properties["javaVersion"]?.toString() ?: error("Require javaVersion property")

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
}

java {
  val javaInt = javaVersion.toInt()
  sourceCompatibility = JavaVersion.toVersion(javaInt)
  targetCompatibility = JavaVersion.toVersion(javaInt)
}

dependencyGuard {
  configuration("compileClasspath")
  configuration("runtimeClasspath")
  configuration("testCompileClasspath")
  configuration("testRuntimeClasspath")
}
