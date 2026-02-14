package blueprint.test

import java.io.File
import org.gradle.testkit.runner.GradleRunner

public sealed interface Scenario {
  public val rootDir: File
  public val runner: GradleRunner
}

public fun Scenario.runTask(task: String, vararg args: String): GradleRunner =
  runner.withArguments(listOf(task, "--configuration-cache") + args.toList())
