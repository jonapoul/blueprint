package blueprint.core

import blueprint.test.DEFAULT_REPOSITORIES_KTS
import blueprint.test.GRADLE_VERSION
import blueprint.test.Scenario
import blueprint.test.ScenarioTest
import blueprint.test.assertThatTasks
import blueprint.test.buildGradleKts
import blueprint.test.buildsSuccessfully
import blueprint.test.outputContainsLine
import blueprint.test.outputContainsMatch
import blueprint.test.settingsGradleKts
import kotlin.test.Test

class GitScenario : ScenarioTest() {
  override val gradleVersion = GRADLE_VERSION

  override val fileTree = fileTree {
    settingsGradleKts(
      DEFAULT_REPOSITORIES_KTS +
        """
        include(":a", ":b")
        """
          .trimIndent()
    )

    val buildScript =
      $$"""
      import blueprint.core.*

      plugins { id("dev.jonpoulton.blueprint") }

      fun <T : Any> registerTask(name: String, property: Provider<T>) = tasks.register(name) {
        inputs.property("property", property)
        inputs.property("name", name)
        doLast { logger.lifecycle("$name = ${property.get()}") }
      }

      // Project-receiver overloads share a single provider instance across the whole build.
      registerTask("printHash", gitVersionHash())
      registerTask("printCode", gitVersionCode())
      registerTask("printDate", gitVersionDate())
      """
        .trimIndent()

    "a" { buildGradleKts(buildScript) }
    "b" { buildGradleKts(buildScript) }
  }

  @Test
  fun `Shared git providers resolve consistently across projects`() = runScenario {
    val hash = initGitRepo()

    assertThatTasks(
        ":a:printHash",
        ":b:printHash",
        ":a:printCode",
        ":b:printCode",
        ":a:printDate",
        ":b:printDate",
      )
      .buildsSuccessfully()
      .outputContainsLine("printHash = $hash")
      .outputContainsMatch("""printCode = \d+""".toRegex())
      .outputContainsMatch("""printDate = \d{4}\.\d{2}\.\d{2}""".toRegex())
  }

  private fun Scenario.initGitRepo(): String {
    git("init", "--initial-branch=main")
    git("config", "user.email", "test@example.com")
    git("config", "user.name", "Test")
    rootDir.resolve(".gitignore").writeText("")
    git("add", ".gitignore")
    git("commit", "--no-gpg-sign", "-m", "Initial commit")
    return git("rev-parse", "HEAD").trim()
  }

  private fun Scenario.git(vararg args: String): String {
    val process =
      ProcessBuilder(listOf("git") + args).directory(rootDir).redirectErrorStream(true).start()
    val output = process.inputStream.bufferedReader().use { it.readText() }
    val exit = process.waitFor()
    check(exit == 0) { "git ${args.joinToString(" ")} failed ($exit):\n$output" }
    return output
  }
}
