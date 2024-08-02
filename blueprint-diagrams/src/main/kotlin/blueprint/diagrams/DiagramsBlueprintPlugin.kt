package blueprint.diagrams

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
import guru.nidi.graphviz.model.Link
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.parse.Parser
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.TaskProvider

public class DiagramsBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("com.vanniktech.dependency.graph.generator")
    val extension = target.extensions.create("diagramsBlueprint", DiagramsBlueprintExtension::class.java)

    val modulePngTask = target.registerModuleTasks(extension)
    val dependenciesPngTask = target.registerDependencyTasks(extension)

    // Aggregate task
    target.tasks.register("generatePngs") { task ->
      task.dependsOn(modulePngTask)
      task.dependsOn(dependenciesPngTask)
    }
  }

  private fun Project.registerModuleTasks(
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
        if (extension.showLegend.get()) graph.addLegend(extension)
        graph.graphAttrs().add(
          Rank.sep(extension.rankSeparation.get()),
          Font.size(extension.nodeFontSize.get()),
          Rank.dir(extension.rankDir.get()),
        )
      },
    )
    val dotTask = tasks.register("generateModulesDotfile", ProjectDependencyGraphGeneratorTask::class.java) { task ->
      task.group = "reporting"
      task.description = "Generates a project dependency graph for $path"
      task.projectGenerator = projectGenerator
      task.outputDirectory = projectDir
    }

    val tidyDotFileTask = tasks.register("tidyDotFile", TidyDotFileTask::class.java) { task ->
      task.dotFile.set(dotTask.get().getOutputFile())
      task.toRemove.set(extension.removeModulePrefix)
      task.replacement.set(extension.replacementModulePrefix)
      task.dependsOn(dotTask)
    }

    val tempDotTask = tasks.register("tempModulesDotfile", ProjectDependencyGraphGeneratorTask::class.java) { task ->
      task.group = JavaBasePlugin.VERIFICATION_GROUP
      task.projectGenerator = projectGenerator
      task.outputDirectory = project.layout.buildDirectory.file("diagrams-modules-temp").get().asFile
    }

    val tempTidyDotFileTask = tasks.register("tempTidyDotFile", TidyDotFileTask::class.java) { task ->
      task.dotFile.set(tempDotTask.get().getOutputFile())
      task.toRemove.set(extension.removeModulePrefix)
      task.replacement.set(extension.replacementModulePrefix)
      task.dependsOn(tempDotTask)
    }

    tasks.register("checkModulesDotfile", CheckDotFileTask::class.java) { task ->
      task.group = JavaBasePlugin.VERIFICATION_GROUP
      task.taskPath.set(dotTask.get().path)
      task.expectedDotFile.set(dotTask.get().getOutputFile())
      task.actualDotFile.set(tempDotTask.get().getOutputFile())
      task.dependsOn(tempTidyDotFileTask)
      if (extension.checkModulesDotfile.get()) {
        tasks.findByName("check")?.dependsOn(task)
      }
    }

    return tasks.register("generateModulesPng", GenerateGraphVizPngTask::class.java) { task ->
      task.reportDir.convention(layout.projectDirectory)
      task.dotFile.convention(task.reportDir.file("project-dependency-graph.dot"))
      task.pngFile.convention(task.reportDir.file("project-dependency-graph.png"))
      task.errorFile.convention(task.reportDir.file("project-dependency-error.log"))
      task.dependsOn(tidyDotFileTask)
    }
  }

  private fun MutableGraph.addLegend(extension: DiagramsBlueprintExtension) {
    // Add the actual legend
    val legend = buildLegend(extension)
    add(legend)

    // Find the root module, probably ":app"
    val rootName = extension.topLevelProject.get()
    val rootNode = rootNodes()
      .filterNotNull()
      .distinct()
      .firstOrNull { it.name().toString() == rootName }
      ?: error("No node matching '$rootName'")

    // Add a link from the legend to the root module
    val link = Link.to(rootNode).with(Style.INVIS)
    add(legend.addLink(link))
  }

  private fun buildLegend(extension: DiagramsBlueprintExtension): MutableGraph {
    val rows = extension.moduleTypes.get().map { type ->
      "<TR><TD>${type.string}</TD><TD BGCOLOR=\"${type.color}\">module-name</TD></TR>"
    }
    return Parser().read(
      """
        graph cluster_legend {
          label="$LEGEND_LABEL"
          graph [fontsize=${extension.legendTitleFontSize.get()}]
          node [style=filled, fillcolor="${extension.legendBackground.get()}"];
          Legend [shape=none, margin=0, fontsize=${extension.legendFontSize.get()}, label=<
            <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
              ${rows.joinToString(separator = "\n")}
            </TABLE>
          >];
        }
      """.trimIndent(),
    )
  }

  private fun Project.registerDependencyTasks(
    extension: DiagramsBlueprintExtension,
  ): TaskProvider<GenerateGraphVizPngTask> {
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
          .graphAttrs().add(Rank.sep(extension.rankSeparation.get()), Font.size(extension.nodeFontSize.get()))
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

    tasks.register("checkDependenciesDotfile", CheckDotFileTask::class.java) { task ->
      task.group = JavaBasePlugin.VERIFICATION_GROUP
      task.taskPath.set(dotTask.get().path)
      task.expectedDotFile.set(dotTask.get().getOutputFile())
      task.actualDotFile.set(tempDotTask.get().getOutputFile())
      task.dependsOn(tempDotTask)
      if (extension.checkDependenciesDotfile.get()) {
        tasks.findByName("check")?.dependsOn(task)
      }
    }

    return tasks.register("generateDependenciesPng", GenerateGraphVizPngTask::class.java) { task ->
      task.reportDir.convention(layout.projectDirectory)
      task.dotFile.convention(task.reportDir.file("dependency-graph.dot"))
      task.pngFile.convention(task.reportDir.file("dependency-graph.png"))
      task.errorFile.convention(task.reportDir.file("dependency-error.log"))
      task.dependsOn(dotTask)
    }
  }

  private companion object {
    const val LEGEND_LABEL = "Legend"
  }
}
