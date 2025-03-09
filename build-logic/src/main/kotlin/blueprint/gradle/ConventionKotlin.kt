package blueprint.gradle

import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPlugin
import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPluginExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

class ConventionKotlin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KotlinPluginWrapper::class)
      apply(ConventionIdea::class)
      apply(DependencyGuardPlugin::class)
    }

    val javaVersion = properties["javaVersion"]?.toString() ?: error("Require javaVersion property")

    tasks.withType<KotlinCompile> {
      compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaVersion))
        freeCompilerArgs.addAll(
          "-Xsam-conversions=class",
          "-Xexplicit-api=strict",
        )
      }
    }

    extensions.configure<KotlinBaseExtension> {
      explicitApi()
    }

    extensions.configure<JavaPluginExtension> {
      val javaInt = javaVersion.toInt()
      sourceCompatibility = JavaVersion.toVersion(javaInt)
      targetCompatibility = JavaVersion.toVersion(javaInt)
    }

    extensions.configure<DependencyGuardPluginExtension> {
      configuration("compileClasspath")
      configuration("runtimeClasspath")
      configuration("testCompileClasspath")
      configuration("testRuntimeClasspath")
    }
  }
}
