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
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ConventionKotlin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KotlinPluginWrapper::class)
      apply(DependencyGuardPlugin::class)
    }

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
