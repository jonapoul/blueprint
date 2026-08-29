package blueprint.test

import assertk.Assert
import assertk.assertions.contains
import assertk.assertions.containsMatch
import assertk.assertions.doesNotContain
import org.gradle.testkit.runner.BuildResult

public fun Assert<BuildResult>.outputContains(expected: String): Assert<BuildResult> =
  transform { result ->
    assertThat(result.output, name = "output").contains(expected)
    result
  }

public fun Assert<BuildResult>.outputContainsLine(expected: String): Assert<BuildResult> =
  outputContains("\n$expected\n")

public fun Assert<BuildResult>.outputDoesNotContain(expected: String): Assert<BuildResult> =
  transform { result ->
    assertThat(result.output, name = "output").doesNotContain(expected)
    result
  }

public fun Assert<BuildResult>.outputContainsMatch(expected: Regex): Assert<BuildResult> =
  transform { result ->
    assertThat(result.output, name = "output").containsMatch(expected)
    result
  }

// To avoid line indent issues when asserting outputContains
public fun Assert<BuildResult>.trimmedOutputContains(expected: String): Assert<BuildResult> =
  transform { result ->
    assertThat(result.output.trimLines(), name = "output").contains(expected)
    result
  }

public fun Assert<BuildResult>.trimmedOutputContains(vararg expected: String): Assert<BuildResult> =
  expected.fold(this) { assertion, value -> assertion.trimmedOutputContains(value) }

private fun String.trimLines() = trimIndent().lines().joinToString("\n") { it.trimEnd() }
