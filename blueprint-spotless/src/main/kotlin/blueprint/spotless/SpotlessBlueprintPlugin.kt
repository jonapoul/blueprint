package blueprint.spotless

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import org.gradle.api.Plugin
import org.gradle.api.Project

public class SpotlessBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("com.diffplug.spotless")
    val properties = SpotlessProperties(target)
    target.configureExtension(properties)
  }

  private fun Project.configureExtension(properties: SpotlessProperties) {
    extensions.configure(SpotlessExtension::class.java) { ext ->
      ext.format("xml") { fmt ->
        fmt.target("*.xml")
        fmt.eclipseWtp(EclipseWtpFormatterStep.XML)
      }
      ext.format("misc") { fmt ->
        val miscFiles = properties.miscFiles.split(",").toTypedArray()
        fmt.target(*miscFiles)
        fmt.indentWithSpaces()
        fmt.trimTrailingWhitespace()
        fmt.endWithNewline()
      }
      ext.freshmark { fmt ->
        fmt.target("*.md")
      }
      ext.json { fmt ->
        fmt.target("*.json")
        fmt.simple()
      }
      ext.yaml { fmt ->
        fmt.target("*.yml", "*.yaml")
        fmt.jackson()
      }
    }
  }
}
