package blueprint.analysis

import com.autonomousapps.DependencyAnalysisExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class DependencyAnalysisBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("com.autonomousapps.dependency-analysis")
    val properties = DependencyAnalysisProperties(target)
    target.configureExtension(properties)
  }

  private fun Project.configureExtension(properties: DependencyAnalysisProperties) {
    extensions.configure(DependencyAnalysisExtension::class.java) { ext ->
      // See https://github.com/autonomousapps/dependency-analysis-gradle-plugin/wiki/Customizing-plugin-behavior
      ext.issues { issues ->
        issues.all { handler ->
          if (properties.ignoreKtx) {
            handler.ignoreKtx(ignore = true)
          }

          handler.onAny { issue ->
            if (properties.strictMode) {
              issue.severity(value = "fail")
            }

            if (properties.excludeFileLambda) {
              // https://github.com/autonomousapps/dependency-analysis-gradle-plugin/issues/884
              issue.exclude("() -> java.io.File?")
            }
          }
        }
      }

      ext.dependencies { handler ->
        if (properties.bundleKotlinStdlib) {
          handler.bundle("kotlin-stdlib") { it.includeGroup("org.jetbrains.kotlin") }
        }
      }
    }
  }
}
