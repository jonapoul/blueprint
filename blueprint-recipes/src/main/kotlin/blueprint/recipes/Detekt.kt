package blueprint.recipes

import blueprint.core.getValue
import blueprint.core.provideDelegate
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

public fun Project.detektBlueprint(
  configFile: ConfigurableFileCollection = rootProject.files("detekt.yml"),
  composeDetektVersion: Provider<String>? = null,
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
  }

  val detektMain = tasks.findByName("detektMain")
  if (detektMain != null) {
    val check by tasks
    check.dependsOn(detektMain)
  }

  val detektPlugins by configurations
  dependencies {
    if (composeDetektVersion != null) {
      detektPlugins("com.twitter.compose.rules:detekt:${composeDetektVersion.get()}")
    }
  }
}
