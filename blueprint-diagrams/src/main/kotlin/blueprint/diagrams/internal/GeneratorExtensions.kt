package blueprint.diagrams.internal

import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorTask
import com.vanniktech.dependency.graph.generator.ProjectDependencyGraphGeneratorTask
import java.io.File

internal fun ProjectDependencyGraphGeneratorTask.getOutputFile(): File {
  return File(outputDirectory, "project-dependency-graph.dot")
}

internal fun DependencyGraphGeneratorTask.getOutputFile(): File {
  return File(outputDirectory, "dependency-graph.dot")
}
