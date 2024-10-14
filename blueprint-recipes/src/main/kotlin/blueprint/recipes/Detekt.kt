package blueprint.recipes

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.withType

public fun Project.detektBlueprint(
  configFile: ConfigurableFileCollection = rootProject.files("detekt.yml"),
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
    val detektAll by tasks.creating

    tasks.withType<Detekt>().configureEach { task ->
      if (!detektAllConfig.ignoreRelease || !task.name.contains("release", ignoreCase = true)) {
        check.dependsOn(task)
        detektAll.dependsOn(task)
      }
    }
  }
}

public sealed interface DetektAll {
  public object Ignore : DetektAll

  public data class Apply(
    val ignoreRelease: Boolean,
  ) : DetektAll
}
