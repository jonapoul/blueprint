package blueprint.gradle

import com.autonomousapps.DependencyAnalysisPlugin
import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.dokka.gradle.formats.DokkaJavadocPlugin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class Convention : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KotlinPluginWrapper::class)
      apply(IdeaPlugin::class)
      apply(MavenPublishPlugin::class)
      apply(DokkaJavadocPlugin::class)
      apply(DependencyAnalysisPlugin::class.java)
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

    extensions.configure<IdeaModel> {
      module {
        isDownloadSources = true
        isDownloadJavadoc = true
      }
    }
  }
}
