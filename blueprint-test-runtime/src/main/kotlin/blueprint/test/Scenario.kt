package blueprint.test

import org.gradle.testkit.runner.GradleRunner
import java.io.File

public sealed interface Scenario {
  public val rootDir: File
  public val runner: GradleRunner
}

public fun Scenario.runTask(
  task: String,
  vararg args: String,
): GradleRunner = runner.withArguments(
  listOf(task, "--configuration-cache") + args.toList(),
)
