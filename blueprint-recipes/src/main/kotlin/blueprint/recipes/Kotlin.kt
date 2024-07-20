package blueprint.recipes

import blueprint.core.stringProperty
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public fun Project.kotlinBlueprint(
  kotlinVersion: Provider<String>,
  explicitApi: Boolean = true,
  freeCompilerArgs: List<String> = DEFAULT_KOTLIN_FREE_COMPILER_ARGS,
) {
  tasks.withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = stringProperty(key = "javaVersion")
      this.freeCompilerArgs += freeCompilerArgs
      if (explicitApi) {
        this.freeCompilerArgs += "-Xexplicit-api=strict"
      }
    }
  }

  extensions.findByType(KotlinTopLevelExtension::class)?.apply {
    if (explicitApi) explicitApi()
  }

  val implementation = configurations.getByName("implementation")
  dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion.get()}")
  }
}
