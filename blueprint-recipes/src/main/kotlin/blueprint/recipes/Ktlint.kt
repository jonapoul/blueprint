/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import blueprint.core.getValue
import blueprint.core.isAndroid
import blueprint.core.provideDelegate
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

public fun Project.ktlintBlueprint(
  ktlintCliVersion: Provider<String>,
  ktlintComposeVersion: Provider<String>? = null,
) {
  ktlintBlueprint(
    ktlintCliVersion = ktlintCliVersion.get(),
    ktlintComposeVersion = ktlintComposeVersion?.get(),
  )
}

public fun Project.ktlintBlueprint(
  ktlintCliVersion: VersionConstraint,
  ktlintComposeVersion: VersionConstraint? = null,
) {
  ktlintBlueprint(
    ktlintCliVersion = ktlintCliVersion.toString(),
    ktlintComposeVersion = ktlintComposeVersion?.toString(),
  )
}

public fun Project.ktlintBlueprint(
  ktlintCliVersion: String,
  ktlintComposeVersion: String? = null,
) {
  with(plugins) {
    apply("org.jlleitschuh.gradle.ktlint")
  }

  extensions.getByType(KtlintExtension::class).apply {
    android.set(boolPropertyOrElse(key = "blueprint.ktlint.isAndroid", default = isAndroid()))
    verbose.set(boolPropertyOrElse(key = "blueprint.ktlint.verbose", default = true))
    enableExperimentalRules.set(boolPropertyOrElse(key = "blueprint.ktlint.experimentalRules", default = false))
    version.set(ktlintCliVersion)
    reporters {
      it.reporter(ReporterType.HTML)
    }
  }

  val ktlintRuleset by configurations
  dependencies {
    if (ktlintComposeVersion != null) {
      ktlintRuleset("com.twitter.compose.rules:ktlint:$ktlintComposeVersion")
    }
  }
}
