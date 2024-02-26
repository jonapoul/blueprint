package blueprint.kover

import blueprint.core.isAndroid
import blueprint.core.isAndroidApp
import blueprint.core.isAtak
import kotlinx.kover.gradle.plugin.dsl.KoverReportExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import java.io.File

public class KoverBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("org.jetbrains.kotlinx.kover")
    val properties = KoverProperties(target)
    val versions = VersionProperties(target)

    target.tasks.withType(Test::class.java) { it.configure() }
    target.extensions.configure(KoverReportExtension::class.java) { it.configure(target, properties) }
    target.addTestingDependencies(versions)

    target.rootProject.dependencies.apply {
      add("kover", target)
    }
  }

  private fun Test.configure() {
    testLogging { l ->
      l.exceptionFormat = TestExceptionFormat.FULL
      l.showCauses = true
      l.showExceptions = true
      l.showStackTraces = true
      l.showStandardStreams = true
    }
  }

  private fun KoverReportExtension.configure(project: Project, properties: KoverProperties) {
    val isRootProject = project == project.rootProject
    val androidVariant = when {
      project.isAtak() && project.isAndroidApp() -> "civDebug"
      project.isAndroid() -> "debug"
      else -> null // kotlin JVM, no variant to merge with
    }

    filters { f ->
      f.excludes { e ->
        val excludedClasses = project.readLinesFromFile(properties.excludedClassesFile) ?: DEFAULT_EXCLUDED_CLASSES
        val extraClasses = project.readLinesFromFile(properties.extraExclusions) ?: emptyList()
        e.classes(excludedClasses + extraClasses)
      }
    }

    defaults { d ->
      if (androidVariant != null) {
        d.mergeWith(androidVariant)
      }

      d.html { h ->
        h.onCheck = isRootProject
      }

      d.log { l ->
        l.onCheck = isRootProject
        l.coverageUnits = properties.metricType
        l.aggregationForGroup = properties.aggregationType
      }

      d.verify { v ->
        v.onCheck = isRootProject
        v.rule { r ->
          r.isEnabled = isRootProject
          r.bound { b ->
            b.minValue = properties.minCoverage
            b.metric = properties.metricType
            b.aggregation = properties.aggregationType
          }
        }
      }
    }

    if (androidVariant != null) {
      androidReports(androidVariant) {
        // No-op, all same config as default
      }
    }
  }

  private fun Project.readLinesFromFile(filename: String?): List<String>? {
    return filename
      ?.let { File(rootProject.projectDir, it) }
      ?.readLines()
      ?.filter { it.isNotBlank() }
      ?.ifEmpty { null }
  }

  private fun Project.addTestingDependencies(versions: VersionProperties) {
    configurations.findByName("testImplementation") ?: return

    dependencies.apply {
      fun String.testImplementation() = add("testImplementation", this)

      "dev.jonpoulton.alakazam:testing-core:${versions.alakazam}".testImplementation()
      "junit:junit:${versions.junit}".testImplementation()
      "org.jetbrains.kotlin:kotlin-test-common:${versions.kotlin}".testImplementation()
      "org.jetbrains.kotlin:kotlin-test-junit:${versions.kotlin}".testImplementation()
      "org.jetbrains.kotlinx:kotlinx-coroutines-test:${versions.coroutines}".testImplementation()
      "io.mockk:mockk:${versions.mockk}".testImplementation()
      "app.cash.turbine:turbine:${versions.turbine}".testImplementation()

      if (isAndroid()) {
        "androidx.arch.core:core-testing:${versions.androidxArch}".testImplementation()
        "androidx.test:core-ktx:${versions.androidxCoreKtx}".testImplementation()
        "androidx.test.ext:junit:${versions.androidxJunit}".testImplementation()
        "androidx.test:rules:${versions.androidxRules}".testImplementation()
        "androidx.test:runner:${versions.androidxRunner}".testImplementation()
        "io.mockk:mockk-android:${versions.mockk}".testImplementation()
        "org.robolectric:robolectric:${versions.robolectric}".testImplementation()
      }
    }
  }

  private companion object {
    val DEFAULT_EXCLUDED_CLASSES = listOf(
      // Generated
      "*.BuildConfig",
      "*_Factory*",
      "*Hilt_*",
      "*hilt_aggregated_deps.*",

      // DI Modules
      "*.di.*",

      // UI
      "*.ui.*Activity*",
      "*.ui.*Adapter*",
      "*.ui.*Fragment*",
      "*.ui.*Layout*",
      "*.ui.*ViewHolder*",
    )
  }
}
