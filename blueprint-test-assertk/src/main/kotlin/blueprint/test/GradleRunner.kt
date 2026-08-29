package blueprint.test

import assertk.Assert
import assertk.assertThat
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

public fun Scenario.assertThatTask(task: String): Assert<GradleRunner> =
  assertThat(runner.withArguments(task))

public fun Scenario.assertThatTasks(vararg tasks: String): Assert<GradleRunner> =
  assertThat(runner.withArguments(*tasks))

public fun Assert<GradleRunner>.buildsSuccessfully(): Assert<BuildResult> = transform { it.build() }

public fun Assert<GradleRunner>.failsBuild(): Assert<BuildResult> = transform { it.buildAndFail() }

public fun Assert<GradleRunner>.withArgument(arg: String): Assert<GradleRunner> = withArguments(arg)

public fun Assert<GradleRunner>.withArguments(vararg args: String): Assert<GradleRunner> =
  transform { runner ->
    runner.withArguments(runner.arguments + args)
  }

public fun Assert<GradleRunner>.withoutConfigurationCache(): Assert<GradleRunner> =
  withArguments("--no-configuration-cache")

public fun Assert<GradleRunner>.withConfigurationCache(): Assert<GradleRunner> =
  withArguments("--configuration-cache")

public fun Assert<GradleRunner>.withGradleProperty(
  name: String,
  value: Any,
): Assert<GradleRunner> = withArguments("-P$name=$value")
