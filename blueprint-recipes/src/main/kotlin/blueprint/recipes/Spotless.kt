package blueprint.recipes

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

public fun Project.spotlessBlueprint(extra: SpotlessExtension.() -> Unit = {}) {
  with(plugins) {
    apply("com.diffplug.spotless")
  }

  extensions.getByType(SpotlessExtension::class).apply {
    format("misc") { format ->
      format.target("*.gradle", "*.gitignore", "*.pro")
      format.indentWithSpaces()
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
    extra()
  }
}
