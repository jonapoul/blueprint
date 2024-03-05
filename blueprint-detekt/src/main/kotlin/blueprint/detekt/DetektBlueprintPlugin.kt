package blueprint.detekt

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class DetektBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("io.gitlab.arturbosch.detekt")
    val properties = DetektProperties(target)

    target.configureExtension(properties)
    target.configureTask(properties)
  }

  private fun Project.configureExtension(properties: DetektProperties) {
    extensions.configure(DetektExtension::class.java) { ext ->
      ext.buildUponDefaultConfig = properties.buildUponDefaultConfig
      ext.config.setFrom(rootProject.files(properties.configFilePath))
      ext.ignoreFailures = properties.ignoreFailures
    }
  }

  private fun Project.configureTask(properties: DetektProperties) {
    tasks.withType(Detekt::class.java) { task ->
      task.reports.apply {
        html.required.set(properties.htmlReports)
        xml.required.set(properties.xmlReports)
        txt.required.set(properties.txtReports)
        sarif.required.set(properties.sarifReports)
        md.required.set(properties.mdReports)
      }
    }
  }
}
