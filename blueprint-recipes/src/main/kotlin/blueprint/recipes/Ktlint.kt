package blueprint.recipes

import blueprint.core.getValue
import blueprint.core.isAndroid
import blueprint.core.provideDelegate
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

public fun Project.ktlintBlueprint(
  ktlintCliVersion: Provider<String>,
  ktlintComposeVersion: Provider<String>? = null,
) {
  with(plugins) {
    apply("org.jlleitschuh.gradle.ktlint")
  }

  extensions.getByType(KtlintExtension::class).apply {
    android.set(isAndroid())
    version.set(ktlintCliVersion.get())
    reporters {
      it.reporter(ReporterType.HTML)
    }
  }

  val ktlintRuleset by configurations
  dependencies {
    if (ktlintComposeVersion != null) {
      ktlintRuleset("com.twitter.compose.rules:ktlint:${ktlintComposeVersion.get()}")
    }
  }
}
