package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import blueprint.core.isAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

public class KtlintBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KtlintPlugin::class)
    }

    extensions.getByType(KtlintExtension::class).apply {
      android.set(boolPropertyOrElse(key = "blueprint.ktlint.isAndroid", default = isAndroid()))
      verbose.set(boolPropertyOrElse(key = "blueprint.ktlint.verbose", default = true))
      enableExperimentalRules.set(boolPropertyOrElse(key = "blueprint.ktlint.experimentalRules", default = false))
      reporters {
        it.reporter(ReporterType.HTML)
      }
    }
  }
}
