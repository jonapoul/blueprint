/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.recipes

import com.android.build.gradle.internal.tasks.factory.dependsOn
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import java.io.File

public fun Project.detektDefaultConfigFile(): File = rootProject.file("detekt.yml")

public fun Project.detektBlueprint(
  configFile: File = detektDefaultConfigFile(),
  detektAllConfig: DetektAll = DetektAll.Ignore,
) {
  with(plugins) {
    apply("io.gitlab.arturbosch.detekt")
  }

  extensions.getByType(DetektExtension::class).apply {
    config.setFrom(configFile)
    buildUponDefaultConfig = true
  }

  tasks.withType<Detekt> {
    reports.html.required.set(true)
    exclude { it.file.path.contains("generated") }
  }

  if (detektAllConfig is DetektAll.Apply) {
    val check by tasks.getting
    val detektAll by tasks.registering

    val detektTasks = tasks.withType<Detekt>()
    detektTasks.map { it.name }.forEach { detektAll.dependsOn(it) }
    detektTasks.configureEach { task ->
      if (!detektAllConfig.ignoreRelease || !task.name.contains("release", ignoreCase = true)) {
        check.dependsOn(task)
      }
    }
  }
}

public sealed interface DetektAll {
  public data object Ignore : DetektAll

  public data class Apply(
    val ignoreRelease: Boolean,
  ) : DetektAll
}
