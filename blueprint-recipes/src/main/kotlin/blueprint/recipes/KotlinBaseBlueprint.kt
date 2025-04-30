package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import blueprint.core.stringListPropertyOrElse
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public class KotlinBaseBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    val explicitApi = boolPropertyOrElse(key = "blueprint.kotlin.explicitApi", default = false)

    val compilerArgs = stringListPropertyOrElse(
      key = "blueprint.kotlin.compilerArgs",
      default = DEFAULT_KOTLIN_COMPILER_ARGS,
    )

    tasks.withType<KotlinCompile> {
      compilerOptions {
        freeCompilerArgs.addAll(compilerArgs)

        if (explicitApi) {
          freeCompilerArgs.add("-Xexplicit-api=strict")
        }
      }
    }

    extensions.findByType(KotlinBaseExtension::class)?.apply {
      if (explicitApi) explicitApi()
    }
  }
}
