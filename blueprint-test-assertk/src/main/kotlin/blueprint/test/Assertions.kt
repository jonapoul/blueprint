package blueprint.test

import assertk.Assert
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsMatch
import assertk.assertions.doesNotContain
import assertk.assertions.support.expected
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.TaskOutcome.FAILED
import org.gradle.testkit.runner.TaskOutcome.SKIPPED
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE

public fun Scenario.assertThatTask(
  task: String,
  vararg args: String,
): Assert<GradleRunner> = assertThat(runTask(task, *args))

public fun Assert<GradleRunner>.buildsSuccessfully(): Assert<BuildResult> = transform { it.build() }

public fun Assert<GradleRunner>.failsBuild(): Assert<BuildResult> = transform { it.buildAndFail() }

public fun Assert<BuildResult>.taskSucceeded(name: String): Assert<BuildResult> = taskHadResult(name, SUCCESS)

public fun Assert<BuildResult>.taskFailed(name: String): Assert<BuildResult> = taskHadResult(name, FAILED)

public fun Assert<BuildResult>.taskSkipped(name: String): Assert<BuildResult> = taskHadResult(name, SKIPPED)

public fun Assert<BuildResult>.taskUpToDate(name: String): Assert<BuildResult> = taskHadResult(name, UP_TO_DATE)

@Suppress("NullableToStringCall")
public fun Assert<BuildResult>.taskHadResult(
  name: String,
  expected: TaskOutcome?,
): Assert<BuildResult> = transform { result ->
  val task = result.task(name)
  if (task?.outcome == expected) {
    result
  } else {
    expected("task result '$expected' for '$name', actual: '${task?.outcome}'. Output:\n${result.output}")
  }
}

public fun Assert<BuildResult>.outputContains(expected: String): Assert<BuildResult> = transform { result ->
  assertThat(result.output, name = "output").contains(expected)
  result
}

public fun Assert<BuildResult>.outputContainsLine(expected: String): Assert<BuildResult> =
  outputContains("\n$expected\n")

public fun Assert<BuildResult>.outputDoesNotContain(expected: String): Assert<BuildResult> = transform { result ->
  assertThat(result.output, name = "output").doesNotContain(expected)
  result
}

public fun Assert<BuildResult>.outputContainsMatch(expected: Regex): Assert<BuildResult> = transform { result ->
  assertThat(result.output, name = "output").containsMatch(expected)
  result
}
