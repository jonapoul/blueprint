package blueprint.recipes

import blueprint.core.boolPropertyOrElse
import com.android.build.gradle.internal.tasks.factory.dependsOn
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType

public class DetektBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(DetektPlugin::class)
    }

    extensions.getByType(DetektExtension::class).apply {
      config.setFrom(rootProject.file("detekt.yml"))
      buildUponDefaultConfig = true
    }

    tasks.withType<Detekt> {
      reports.html.required.set(true)
      exclude { it.file.path.contains("generated") }
    }

    val configureAll = boolPropertyOrElse(key = "blueprint.detekt.all", default = false)
    val ignoreRelease = boolPropertyOrElse(key = "blueprint.detekt.all.ignoreRelease", default = false)

    if (configureAll) {
      val check by tasks.getting
      val detektAll by tasks.registering

      val detektTasks = tasks.withType<Detekt>()
      detektTasks.forEach { detektAll.dependsOn(it.name) }
      detektTasks.configureEach { task ->
        if (!ignoreRelease || !task.name.contains("release", ignoreCase = true)) {
          check.dependsOn(task)
        }
      }
    }
  }
}
