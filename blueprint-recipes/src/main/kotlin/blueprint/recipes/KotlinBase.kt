package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public fun Project.kotlinBaseBlueprint(
  freeCompilerArgs: List<String> = DEFAULT_KOTLIN_FREE_COMPILER_ARGS,
  explicitApi: Boolean = boolPropertyOrElse(key = "blueprint.kotlin.explicitApi", default = false),
) {
  tasks.withType<KotlinCompile> {
    kotlinOptions {
      this.freeCompilerArgs += freeCompilerArgs
      if (explicitApi) {
        this.freeCompilerArgs += "-Xexplicit-api=strict"
      }
    }
  }

  extensions.findByType(KotlinTopLevelExtension::class)?.apply {
    if (explicitApi) explicitApi()
  }
}
