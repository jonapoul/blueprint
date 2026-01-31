package blueprint.gradle

import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPlugin
import com.vanniktech.maven.publish.MavenPublishPlugin
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.dokka.gradle.formats.DokkaJavadocPlugin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtensionConfig
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class Convention : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KotlinPluginWrapper::class)
      apply(IdeaPlugin::class)
      apply(MavenPublishPlugin::class)
      apply(DokkaJavadocPlugin::class)
      apply(DetektPlugin::class)
      apply(DependencyGuardPlugin::class)
    }

    kotlin()
    test()
    idea()
    detekt()
  }

  private fun Project.kotlin() {
    val javaVersion = providers.gradleProperty("blueprint.javaVersion")

    tasks.withType(KotlinCompile::class.java).configureEach {
      compilerOptions {
        jvmTarget.set(javaVersion.map(JvmTarget::fromTarget))
        freeCompilerArgs.addAll(
          "-Xsam-conversions=class",
          "-Xexplicit-api=strict",
        )
      }
    }

    extensions.configure(KotlinTopLevelExtensionConfig::class.java) {
      explicitApi()
    }

    val javaInt = javaVersion.map { JavaVersion.toVersion(it.toInt()) }
    extensions.configure(JavaPluginExtension::class.java) {
      sourceCompatibility = javaInt.get()
      targetCompatibility = javaInt.get()
    }
  }

  private fun Project.test() {
    tasks.withType(Test::class.java).configureEach {
      testLogging {
        events = setOf(PASSED, SKIPPED, FAILED)
        exceptionFormat = FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
        showStandardStreams = false
        displayGranularity = 2
      }
    }
  }

  private fun Project.idea() {
    extensions.configure(IdeaModel::class.java) {
      module {
        isDownloadSources = true
        isDownloadJavadoc = true
      }
    }
  }

  private fun Project.detekt() {
    extensions.configure(DetektExtension::class.java) {
      config.setFrom(rootProject.file("config/detekt.yml"))
      buildUponDefaultConfig = true
    }

    val detektTasks = tasks.withType(Detekt::class.java)
    val detektCheck by tasks.registering { dependsOn(detektTasks) }
    tasks.named("check").configure { dependsOn(detektCheck) }

    detektTasks.configureEach {
      reports.html.required.set(true)
      exclude { it.file.path.contains("generated") }
    }
  }
}
