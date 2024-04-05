package blueprint.core

import com.lordcodes.turtle.GitCommands
import com.lordcodes.turtle.ShellFailedException
import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.shellRun
import org.gradle.api.Project

public fun Project.gitVersionName(): String {
  return runGitCommandOrNull(args = listOf("rev-parse", "--short=8", "HEAD"))
    ?: error("Failed getting git version from ${project.path}")
}

public fun Project.gitVersionCode(): Int {
  return runGitCommandOrNull(args = listOf("show", "-s", "--format=%ct"))?.toInt()
    ?: error("Failed getting git code from ${project.path}")
}

public fun Project.runGitCommandOrElse(command: String = "git", args: List<String>, default: String): String =
  runGitCommandOrNull(command, args) ?: default

public fun Project.runGitCommandOrNull(command: String = "git", args: List<String>): String? {
  return try {
    shellRun(
      workingDirectory = rootProject.rootDir,
      command = command,
      arguments = args,
    )
  } catch (e: ShellFailedException) {
    logger.error("Shell failed trying to run $command $args", e)
    null
  } catch (e: ShellRunException) {
    logger.error("Command failed running $command $args", e)
    null
  }
}

public fun Project.runGitCommand(command: GitCommands.() -> String): String =
  shellRun(workingDirectory = rootProject.rootDir) { git.command() }
