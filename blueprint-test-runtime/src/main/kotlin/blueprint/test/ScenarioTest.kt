package blueprint.test

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.io.TempDir
import java.io.File

public abstract class ScenarioTest {
  @TempDir public lateinit var rootDir: File

  protected abstract val fileTree: FileTree
  protected abstract val gradleVersion: String

  protected open fun defaultRunner(): GradleRunner = GradleRunner
    .create()
    .withPluginClasspath()
    .withDebug(false)
    .withGradleVersion(gradleVersion)
    .withProjectDir(rootDir)

  protected fun fileTree(config: FileTree.Builder.() -> Unit): FileTree = FileTree
    .Builder(relativeRootPath = "")
    .apply(config)
    .build()

  protected fun runScenario(
    runner: GradleRunner = defaultRunner(),
    test: Scenario.() -> Unit,
  ) {
    fileTree.files.forEach { (path, contents) ->
      val file = rootDir.resolve(path)
      file.parentFile?.mkdirs()
      file.writeText(contents)
    }

    val scenario = ScenarioImpl(rootDir, runner)
    test(scenario)
  }

  private class ScenarioImpl(
    override val rootDir: File,
    override val runner: GradleRunner,
  ) : Scenario
}
