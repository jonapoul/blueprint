package blueprint.diagrams

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

public class CheckDotFileTask @Inject constructor(objects: ObjectFactory) : DefaultTask() {
  @get:Input
  public val taskPath: Property<String> = objects.property(String::class.java)

  @get:InputFile
  public val expectedDotFile: RegularFileProperty = objects.fileProperty()

  @get:InputFile
  public val actualDotFile: RegularFileProperty = objects.fileProperty()

  init {
    // never cache
    outputs.upToDateWhen { false }
  }

  @TaskAction
  public fun execute() {
    val expectedContents = expectedDotFile.get().asFile.readLines()
    val actualContents = actualDotFile.get().asFile.readLines()

    require(expectedContents == actualContents) {
      """
        Dotfile needs updating! Run `gradle ${taskPath.get()}` to regenerate $expectedDotFile.

        Expected:
        ${expectedContents.joinToString("\n")}

        Actual:
        ${expectedContents.joinToString("\n")}
      """.trimIndent()
    }
  }
}
