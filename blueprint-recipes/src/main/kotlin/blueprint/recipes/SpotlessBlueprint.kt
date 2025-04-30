package blueprint.recipes

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

public class SpotlessBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(plugins) {
      apply("com.diffplug.spotless")
    }

    extensions.getByType(SpotlessExtension::class).apply {
      format("misc") { format ->
        format.target("*.gradle", "*.gitignore", "*.pro")
        format.leadingTabsToSpaces()
        format.trimTrailingWhitespace()
        format.endWithNewline()
      }
      json { json ->
        json.target("*.json")
        json.simple()
      }
      yaml { yaml ->
        yaml.target("*.yml", "*.yaml")
        yaml.jackson()
      }
      format("xml") { xml ->
        xml.target("*.xml")
        xml.eclipseWtp(EclipseWtpFormatterStep.XML)
      }
    }
  }
}
