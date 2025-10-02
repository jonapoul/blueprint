/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("JUnitMalformedDeclaration")

package blueprint.core

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.assertTrue

@RunWith(TestParameterInjector::class)
internal class TypedPropertiesTest {
  @Test
  fun nullValues(
    @TestParameter(
      "null-bool",
      "null-double",
      "null-float",
      "null-int",
      "null-string",
    ) fixtureName: String,
  ) {
    val fixtureDir = File(fixturesDir, fixtureName)
    val result = createRunner(fixtureDir, propertyValue = null).build()
    result.assertPropertyValuePrinted(value = "null")
    result.assertAssembleSuccess()
  }

  @Test
  fun `Nullable bool`() = testSuccess(fixture = "nullable-bool", value = true, expected = "true")

  @Test
  fun `Nullable double`() = testSuccess(fixture = "nullable-double", value = 1.23, expected = "1.23")

  @Test
  fun `Nullable float`() = testSuccess(fixture = "nullable-float", value = 1.23f, expected = "1.23")

  @Test
  fun `Nullable int`() = testSuccess(fixture = "nullable-int", value = 123, expected = "123")

  @Test
  fun `Nullable string`() = testSuccess(fixture = "nullable-string", value = "abc", expected = "abc")

  @Test
  fun `bool success`() = testSuccess(fixture = "required-bool-success", value = true, expected = "true")

  @Test
  fun `double success`() = testSuccess(fixture = "required-double-success", value = 1.23, expected = "1.23")

  @Test
  fun `float success`() = testSuccess(fixture = "required-float-success", value = 1.23f, expected = "1.23")

  @Test
  fun `int success`() = testSuccess(fixture = "required-int-success", value = 123, expected = "123")

  @Test
  fun `string success`() = testSuccess(fixture = "required-string-success", value = "abc", expected = "abc")

  @Test
  fun `bool failure`() = testFailure(fixture = "required-bool-failure")

  @Test
  fun `double failure`() = testFailure(fixture = "required-double-failure")

  @Test
  fun `float failure`() = testFailure(fixture = "required-float-failure")

  @Test
  fun `int failure`() = testFailure(fixture = "required-int-failure")

  @Test
  fun `string failure`() = testFailure(fixture = "required-string-failure")

  private fun <T> testSuccess(fixture: String, value: T, expected: String) {
    val fixtureDir = File(fixturesDir, fixture)
    val result = createRunner(fixtureDir, value).build()
    result.assertPropertyValuePrinted(value = expected)
    result.assertAssembleSuccess()
  }

  private fun testFailure(fixture: String) {
    val fixtureDir = File(fixturesDir, fixture)
    val result = createRunner(fixtureDir).buildAndFail()
    assertTrue(result.output.contains("MissingPropertyException"))
  }
}
