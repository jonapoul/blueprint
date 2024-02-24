package blueprint.ktlint

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

public class KtlintBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("org.jlleitschuh.gradle.ktlint")
    val properties = KtlintProperties(target)
    target.configureExtension(properties)
  }

  private fun Project.configureExtension(properties: KtlintProperties) {
    extensions.configure(KtlintExtension::class.java) { ext ->
      ext.android.set(properties.android)
      ext.coloredOutput.set(properties.coloredOutput)
      ext.enableExperimentalRules.set(properties.enableExperimentalRules)
      ext.ignoreFailures.set(properties.ignoreFailures)
      ext.verbose.set(properties.verbose)
      ext.version.set(properties.version)
      ext.reporters {
        it.reporter(ReporterType.HTML)
      }
    }
  }
}
