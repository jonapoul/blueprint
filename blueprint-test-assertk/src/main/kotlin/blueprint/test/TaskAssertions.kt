package blueprint.test

import assertk.Assert
import assertk.assertions.prop
import assertk.assertions.support.expected
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome

public fun Assert<BuildResult>.taskSucceeded(name: String): Assert<BuildResult> =
  taskHadResult(name, SUCCESS)

public fun Assert<BuildResult>.taskFailed(name: String): Assert<BuildResult> =
  taskHadResult(name, FAILED)

public fun Assert<BuildResult>.taskWasSkipped(name: String): Assert<BuildResult> =
  taskHadResult(name, SKIPPED)

public fun Assert<BuildResult>.taskWasUpToDate(name: String): Assert<BuildResult> =
  taskHadResult(name, UP_TO_DATE)

@Suppress("NullableToStringCall")
public fun Assert<BuildResult>.taskHadResult(
  name: String,
  expected: TaskOutcome?,
): Assert<BuildResult> = transform { result ->
  val task = result.task(name)
  if (task?.outcome == expected) {
    result
  } else {
    expected(
      "task result '$expected' for '$name', actual: '${task?.outcome}'. Output:\n${result.output}"
    )
  }
}

public fun Assert<BuildResult>.tasksSucceeded(vararg names: String): Assert<BuildResult> =
  tasksHadResult(SUCCESS, *names)

public fun Assert<BuildResult>.tasksFailed(vararg names: String): Assert<BuildResult> =
  tasksHadResult(FAILED, *names)

public fun Assert<BuildResult>.tasksWereSkipped(vararg names: String): Assert<BuildResult> =
  tasksHadResult(SKIPPED, *names)

public fun Assert<BuildResult>.tasksWereUpToDate(vararg names: String): Assert<BuildResult> =
  tasksHadResult(UP_TO_DATE, *names)

public fun Assert<BuildResult>.tasksHadResult(
  expected: TaskOutcome?,
  vararg names: String,
): Assert<BuildResult> =
  names.fold(this) { assertion, name -> assertion.taskHadResult(name, expected) }

public fun Assert<BuildResult>.allTasksSuccessful(): Assert<List<BuildTask>> =
  prop(BuildResult::getTasks).allSuccessful()

public fun Assert<BuildResult>.noTasksFailed(): Assert<BuildResult> = transform { result ->
  val failures = result.tasks.filter { task -> task.outcome == FAILED }
  if (failures.isEmpty()) {
    result
  } else {
    expected("no tasks to fail: failures=$failures, all=${result.tasks}")
  }
}

public fun Assert<List<BuildTask>>.allSuccessful(): Assert<List<BuildTask>> = transform { tasks ->
  val nonSuccesses = tasks.filter { task -> task.outcome != SUCCESS }
  if (nonSuccesses.isEmpty()) {
    tasks
  } else {
    val successes = tasks.filter { t -> t.outcome == SUCCESS }
    expected("all tasks to succeed: failures=$nonSuccesses, successes=$successes")
  }
}
