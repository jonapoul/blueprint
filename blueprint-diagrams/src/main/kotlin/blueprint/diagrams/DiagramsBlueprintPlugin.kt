package blueprint.diagrams

import blueprint.diagrams.internal.DiagramsProperties
import blueprint.diagrams.internal.depColour
import blueprint.diagrams.internal.getOutputFile
import blueprint.diagrams.internal.toNiceString
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension.Generator
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension.ProjectGenerator
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorTask
import com.vanniktech.dependency.graph.generator.ProjectDependencyGraphGeneratorTask
import guru.nidi.graphviz.attribute.Font
import guru.nidi.graphviz.attribute.Label
import guru.nidi.graphviz.attribute.Rank
import guru.nidi.graphviz.attribute.Shape
import guru.nidi.graphviz.attribute.Style
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.parse.Parser
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.TaskProvider

public class DiagramsBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("com.vanniktech.dependency.graph.generator")
    val properties = DiagramsProperties(target)

    val extension = target.extensions.create("diagramsBlueprint", DiagramsBlueprintExtension::class.java)

    if (!properties.generateModules && properties.generateDependencies) {
      target.logger.warn("No diagrams will be generated from ${target.path}")
      return
    }

    val modulePngTask = if (properties.generateModules) {
      target.registerModuleTasks(properties, extension)
    } else {
      null
    }

    val dependenciesPngTask = if (properties.generateDependencies) {
      target.registerDependencyTasks(properties)
    } else {
      null
    }

    // Aggregate task
    target.tasks.register("generatePngs") { task ->
      modulePngTask?.let { task.dependsOn(it) }
      dependenciesPngTask?.let { task.dependsOn(it) }
    }
  }

  private fun Project.registerModuleTasks(
    properties: DiagramsProperties,
    extension: DiagramsBlueprintExtension,
  ): TaskProvider<GenerateGraphVizPngTask> {

    val projectGenerator = ProjectGenerator(
      outputFormats = listOf(),
      projectNode = { node, proj ->
        val moduleTypeFinder = extension.moduleTypeFinder.get()
        node.add(Shape.BOX).add(moduleTypeFinder.color(proj))
      },
      includeProject = { proj -> proj != proj.rootProject },
      graph = { graph ->
        val moduleTypes = extension.moduleTypes.get()
        if (properties.showLegend) graph.add(buildLegend(properties, moduleTypes))
        graph.graphAttrs().add(Rank.sep(properties.rankSeparation), Font.size(properties.nodeFontSize))
      },
    )
    val dotTask = tasks.register("generateModulesDotfile", ProjectDependencyGraphGeneratorTask::class.java) { task ->
      task.group = "reporting"
      task.description = "Generates a project dependency graph for $path"
      task.projectGenerator = projectGenerator
      task.outputDirectory = projectDir
    }

    val tidyDotFileTask = tasks.register("tidyDotFileTask", TidyDotFileTask::class.java) { task ->
      task.dotFile.set(dotTask.get().getOutputFile())
      task.toRemove.set(properties.removeModulePrefix)
      task.replacement.set(properties.replacementModulePrefix)
      task.dependsOn(dotTask)
    }

    val tempDotTask = tasks.register("tempModulesDotfile", ProjectDependencyGraphGeneratorTask::class.java) { task ->
      task.group = JavaBasePlugin.VERIFICATION_GROUP
      task.projectGenerator = projectGenerator
      task.outputDirectory = project.layout.buildDirectory.file("diagrams-modules-temp").get().asFile
    }

    val tempTidyDotFileTask = tasks.register("tempTidyDotFileTask", TidyDotFileTask::class.java) { task ->
      task.dotFile.set(tempDotTask.get().getOutputFile())
      task.toRemove.set(properties.removeModulePrefix)
      task.replacement.set(properties.replacementModulePrefix)
      task.dependsOn(tempDotTask)
    }

    val checkDotTask = tasks.register("checkModulesDotfile", CheckDotFileTask::class.java) { task ->
      task.group = JavaBasePlugin.VERIFICATION_GROUP
      task.taskPath.set(dotTask.get().path)
      task.expectedDotFile.set(dotTask.get().getOutputFile())
      task.actualDotFile.set(tempDotTask.get().getOutputFile())
      task.dependsOn(tempTidyDotFileTask)
    }
    tasks.findByName("check")?.dependsOn(checkDotTask)

    return tasks.register("generateModulesPng", GenerateGraphVizPngTask::class.java) { task ->
      task.reportDir.convention(layout.projectDirectory)
      task.dotFile.convention(task.reportDir.file("project-dependency-graph.dot"))
      task.pngFile.convention(task.reportDir.file("project-dependency-graph.png"))
      task.errorFile.convention(task.reportDir.file("project-dependency-error.log"))
      task.dependsOn(tidyDotFileTask)
    }
  }

  private fun buildLegend(properties: DiagramsProperties, moduleTypes: Set<ModuleType>): MutableGraph {
    val rows = moduleTypes.map {
      "<TR><TD>${it.string}</TD><TD BGCOLOR=\"${it.color}\">module-name</TD></TR>"
    }
    return Parser().read(
      """
        graph cluster_legend {
          label="Legend"
          graph [fontsize=${properties.legendTitleFontSize}]
          node [style=filled, fillcolor="${properties.legendBackground}"];
          Legend [shape=none, margin=0, fontsize=${properties.legendFontSize}, label=<
            <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
              ${rows.joinToString(separator = "\n")}
            </TABLE>
          >];
        }
      """.trimIndent(),
    )
  }

  private fun Project.registerDependencyTasks(properties: DiagramsProperties): TaskProvider<GenerateGraphVizPngTask> {
    val dependencyGenerator = Generator(
      outputFormats = listOf(),
      dependencyNode = { node, dep ->
        node
          .add(Shape.RECTANGLE)
          .add(dep.depColour())
          .add(Label.of(dep.toNiceString(target = this)))
      },
      graph = { graph ->
        graph
          .graphAttrs().add(Rank.sep(properties.rankSeparation), Font.size(properties.nodeFontSize))
          .nodeAttrs().add(Style.FILLED)
      },
    )

    val dotTask = tasks.register("generateDependenciesDotfile", DependencyGraphGeneratorTask::class.java) { task ->
      task.group = "reporting"
      task.description = "Generates a dependency graph for $path"
      task.generator = dependencyGenerator
      task.outputDirectory = projectDir
    }

    val tempDotTask = tasks.register("tempDependenciesDotfile", DependencyGraphGeneratorTask::class.java) { task ->
      task.group = JavaBasePlugin.VERIFICATION_GROUP
      task.generator = dependencyGenerator
      task.outputDirectory = project.layout.buildDirectory.file("diagrams-dependencies-temp").get().asFile
    }

    val checkDotTask = tasks.register("checkDependenciesDotfile", CheckDotFileTask::class.java) { task ->
      task.group = JavaBasePlugin.VERIFICATION_GROUP
      task.taskPath.set(dotTask.get().path)
      task.expectedDotFile.set(dotTask.get().getOutputFile())
      task.actualDotFile.set(tempDotTask.get().getOutputFile())
      task.dependsOn(tempDotTask)
    }
    tasks.findByName("check")?.dependsOn(checkDotTask)

    return tasks.register("generateDependenciesPng", GenerateGraphVizPngTask::class.java) { task ->
      task.reportDir.convention(layout.projectDirectory)
      task.dotFile.convention(task.reportDir.file("dependency-graph.dot"))
      task.pngFile.convention(task.reportDir.file("dependency-graph.png"))
      task.errorFile.convention(task.reportDir.file("dependency-error.log"))
      task.dependsOn(dotTask)
    }
  }
}
