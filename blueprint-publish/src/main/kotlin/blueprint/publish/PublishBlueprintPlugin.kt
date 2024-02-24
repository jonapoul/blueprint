package blueprint.publish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import java.io.File

public class PublishBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("com.vanniktech.maven.publish")
    target.pluginManager.apply("org.jetbrains.dokka")
    val properties = PublishProperties(target)
    target.configureExtension(properties)
  }

  private fun Project.configureExtension(properties: PublishProperties) {
    extensions.configure(PublishingExtension::class.java) { ext ->
      ext.repositories { handler ->
        handler.maven { repo ->
          repo.name = "MavenLocalProper"
          repo.url = File(properties.mavenLocalPath).toURI()
        }
      }
    }
  }
}
