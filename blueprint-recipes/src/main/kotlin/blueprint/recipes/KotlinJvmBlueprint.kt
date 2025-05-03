package blueprint.recipes

import blueprint.core.javaVersionInt
import blueprint.core.jvmTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public class KotlinJvmBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KotlinBaseBlueprint::class)
    }

    tasks.withType<KotlinCompile> {
      compilerOptions {
        jvmTarget.set(project.jvmTarget())
      }
    }

    extensions.getByType(KotlinBaseExtension::class).apply {
      jvmToolchain(javaVersionInt())
    }
  }
}
