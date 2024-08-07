package blueprint.recipes

import blueprint.core.javaVersionInt
import blueprint.core.javaVersionString
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public fun Project.kotlinJvmBlueprint(
  kotlinVersion: Provider<String>,
  freeCompilerArgs: List<String> = DEFAULT_KOTLIN_FREE_COMPILER_ARGS,
) {
  kotlinBaseBlueprint(freeCompilerArgs)

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = javaVersionString()
    }
  }

  extensions.getByType(KotlinTopLevelExtension::class).apply {
    jvmToolchain(javaVersionInt())
  }

  val implementation = configurations.getByName("implementation")
  dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion.get()}")
  }
}
