package blueprint.recipes

import blueprint.core.javaVersionInt
import blueprint.core.jvmTarget
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public fun Project.kotlinJvmBlueprint(
  kotlinVersion: Provider<String>,
  freeCompilerArgs: List<String> = DEFAULT_KOTLIN_FREE_COMPILER_ARGS,
) {
  kotlinJvmBlueprint(kotlinVersion.get(), freeCompilerArgs)
}

public fun Project.kotlinJvmBlueprint(
  kotlinVersion: VersionConstraint,
  freeCompilerArgs: List<String> = DEFAULT_KOTLIN_FREE_COMPILER_ARGS,
) {
  kotlinJvmBlueprint(kotlinVersion.toString(), freeCompilerArgs)
}

public fun Project.kotlinJvmBlueprint(
  kotlinVersion: String,
  freeCompilerArgs: List<String> = DEFAULT_KOTLIN_FREE_COMPILER_ARGS,
) {
  kotlinBaseBlueprint(freeCompilerArgs)

  tasks.withType<KotlinCompile> {
    compilerOptions {
      jvmTarget.set(project.jvmTarget())
    }
  }

  extensions.getByType(KotlinBaseExtension::class).apply {
    jvmToolchain(javaVersionInt())
  }

  val implementation = configurations.getByName("implementation")
  dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}")
  }
}
