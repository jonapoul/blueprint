package blueprint.test

import java.io.File
import org.gradle.testkit.runner.GradleRunner

public sealed interface Scenario {
  public val rootDir: File
  public val runner: GradleRunner
}
