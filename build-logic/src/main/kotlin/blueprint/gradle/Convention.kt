package blueprint.gradle

import com.autonomousapps.DependencyAnalysisPlugin
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
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
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
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
      apply(DetektPlugin::class)
      apply(SpotlessPlugin::class)
    }

    kotlin()
    test()
    idea()
    detekt()
    spotless()
  }

  private fun Project.kotlin() {
    val javaVersion = properties["javaVersion"]?.toString() ?: error("Require javaVersion property")

    tasks.withType<KotlinCompile>().configureEach {
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
  }

  private fun Project.test() {
    tasks.withType<Test>().configureEach {
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

  private fun Project.spotless() {
    extensions.configure<SpotlessExtension> {
      format("misc") {
        target("*.gradle", "*.md", ".gitignore")
        trimTrailingWhitespace()
        leadingTabsToSpaces(2)
        endWithNewline()
      }

      format("licenseKotlin") {
        licenseHeaderFile(rootProject.file("config/spotless.kt"), "(package|@file:)")
        target("src/**/*.kt")
      }
    }
  }

  private fun Project.idea() {
    extensions.configure<IdeaModel> {
      module {
        isDownloadSources = true
        isDownloadJavadoc = true
      }
    }
  }

  private fun Project.detekt() {
    extensions.configure<DetektExtension> {
      config.setFrom(rootProject.file("config/detekt.yml"))
      buildUponDefaultConfig = true
    }

    val detektTasks = tasks.withType<Detekt>()
    val detektCheck by tasks.registering { dependsOn(detektTasks) }
    tasks.named("check").configure { dependsOn(detektCheck) }

    detektTasks.configureEach {
      reports.html.required.set(true)
      exclude { it.file.path.contains("generated") }
    }
  }
}
