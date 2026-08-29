package blueprint.test

import assertk.Assert
import assertk.assertions.contains as assertkContains
import assertk.assertions.contains
import assertk.assertions.doesNotContain as assertkDoesNotContain
import assertk.assertions.exists as assertkExists
import assertk.assertions.exists
import assertk.assertions.support.expected
import java.io.File

public fun Assert<File>.contentEquals(expected: String): Assert<File> = transform { file ->
  val contents = file.readText().removeSuffix("\n")
  if (contents == expected) {
    file
  } else {
    expected(
      message = "string with length ${expected.length}, got ${contents.length}:\n",
      expected = expected,
      actual = contents,
    )
  }
}

public fun Assert<File>.contentContains(expected: String): Assert<File> = transform { file ->
  assertThat(file.readText()).contains(expected)
  file
}

public fun Assert<File>.exists(): Assert<File> = transform { file ->
  assertThat(file).assertkExists()
  file
}

// Copied from assertk repo but they haven't published in ages
// https://github.com/assertk-org/assertk/blob/main/assertk/src/jvmMain/kotlin/assertk/assertions/file.kt
public fun Assert<File>.doesNotExist(): Assert<File> = transform { actual ->
  if (!actual.exists()) {
    actual
  } else {
    expected("$actual not to exist")
  }
}

public fun Assert<File>.childExists(path: String): Assert<File> = transform { dir ->
  assertThat(dir.resolve(path)).exists()
  dir
}

public fun Assert<File>.childDoesNotExist(path: String): Assert<File> = transform { dir ->
  assertThat(dir.resolve(path)).doesNotExist()
  dir
}

public fun Assert<String>.contains(expected: String): Assert<String> = transform { actual ->
  assertThat(actual).assertkContains(expected)
  actual
}

public fun Assert<String>.doesNotContain(expected: String): Assert<String> = transform { actual ->
  assertThat(actual).assertkDoesNotContain(expected)
  actual
}

public fun Assert<String>.isEqualToTrimmed(expected: String): Assert<String> = transform { actual ->
  val stripped = actual.removeSuffix("\n")
  if (expected == stripped) {
    actual
  } else {
    expected(
      message = "string with length ${expected.length}, got ${stripped.length}:\n",
      expected = expected,
      actual = stripped,
    )
  }
}
