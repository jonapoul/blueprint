@file:Suppress("UnstableApiUsage")

package blueprint.gradle

import com.autonomousapps.DependencyAnalysisPlugin
import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPlugin
import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPluginExtension
import com.github.gmazzo.buildconfig.BuildConfigExtension
import com.github.gmazzo.buildconfig.BuildConfigPlugin
import com.vanniktech.maven.publish.MavenPublishPlugin
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import kotlinx.validation.BinaryCompatibilityValidatorPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.buildConfigField
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.gradle.plugin.devel.tasks.PluginUnderTestMetadata
import org.gradle.util.GradleVersion
import org.jetbrains.dokka.gradle.formats.DokkaJavadocPlugin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtensionConfig
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class Convention : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KotlinPluginWrapper::class)
      apply(MavenPublishPlugin::class)
      apply(DokkaJavadocPlugin::class)
      apply(DetektPlugin::class)
      apply(DependencyAnalysisPlugin::class)
      apply(BuildConfigPlugin::class)
      apply(BinaryCompatibilityValidatorPlugin::class)
      apply(DependencyGuardPlugin::class)
    }

    kotlin()
    test()
    detekt()
    dependencyGuard()
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
        useJUnitPlatform()
        events = setOf(PASSED, SKIPPED, FAILED)
        exceptionFormat = FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
        showStandardStreams = false
        displayGranularity = 2
      }
    }

    extensions.configure(BuildConfigExtension::class) {
      generateAtSync.set(true)
      sourceSets.named("test") {
        packageName.set("blueprint.test")
        useKotlinOutput { topLevelConstants = true }
        buildConfigField("GRADLE_VERSION", GradleVersion.current().version)
      }
    }

    val testPluginClasspath by configurations.registering { isCanBeResolved = true }

    tasks.withType(PluginUnderTestMetadata::class).configureEach {
      pluginClasspath.from(testPluginClasspath)
    }

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    dependencies {
      "testImplementation"(kotlin("stdlib"))
      "testImplementation"(kotlin("test"))
      "testImplementation"(libs.findLibrary("assertk").get())
      "testImplementation"(libs.findLibrary("junit-api").get())
      "testRuntimeOnly"(libs.findLibrary("junit-launcher").get())
    }
  }

  private fun Project.detekt() {
    extensions.configure(DetektExtension::class.java) {
      config.setFrom(rootProject.isolated.projectDirectory.file("config/detekt.yml"))
      buildUponDefaultConfig = true
    }

    val detektTasks = tasks.withType(Detekt::class.java)
    val detektCheck by tasks.registering { dependsOn(detektTasks) }

    pluginManager.withPlugin("base") {
      tasks.named("check").configure { dependsOn(detektCheck) }
    }

    detektTasks.configureEach {
      reports.html.required.set(true)
      reports.sarif.required.set(true)
      exclude { it.file.path.contains("generated") }
    }
  }

  private fun Project.dependencyGuard() {
    extensions.configure(DependencyGuardPluginExtension::class) {
      configuration("compileClasspath")
      configuration("runtimeClasspath")
    }
  }
}
