package blueprint.core

import blueprint.test.GRADLE_VERSION
import blueprint.test.ScenarioTest
import blueprint.test.assertThatTask
import blueprint.test.buildGradleKts
import blueprint.test.buildsSuccessfully
import blueprint.test.failsBuild
import blueprint.test.gradleProperties
import blueprint.test.outputContainsLine
import blueprint.test.settingsGradleKts
import blueprint.test.withGradleProperty
import kotlin.test.Test

class TypedPropertiesScenario : ScenarioTest() {
  override val gradleVersion = GRADLE_VERSION

  override val fileTree = fileTree {
    settingsGradleKts()

    buildGradleKts(
      """
      import blueprint.core.*

      plugins { id("dev.jonpoulton.blueprint") }

      fun <T : Any> registerTask(name: String, property: Provider<T>) = tasks.register(name) {
        inputs.property("property", property)
        doLast { logger.lifecycle(property.get().toString()) }
      }

      val key = properties["key"]?.toString().orEmpty()
      registerTask("printBool", providers.boolProperty(key))
      registerTask("printDouble", providers.doubleProperty(key))
      registerTask("printFloat", providers.floatProperty(key))
      registerTask("printInt", providers.intProperty(key))
      registerTask("printList", providers.stringListProperty(key))
      registerTask("printString", providers.gradleProperty(key))
      """
        .trimIndent()
    )

    gradleProperties(
      """
      bool=true
      boolBinary=0
      boolCapital=FALSE
      double=1.23
      int=123
      stringList=a,b,c
      """
        .trimIndent()
    )
  }

  @Test
  fun `Print integer`() = runScenario {
    mapOf(
        ":printDouble" to "123.0",
        ":printFloat" to "123.0",
        ":printInt" to "123",
        ":printString" to "123",
        ":printBool" to "false",
      )
      .forEach { (task, expected) ->
        assertThatTask(task)
          .withGradleProperty(name = "key", value = "int")
          .buildsSuccessfully()
          .outputContainsLine(expected)
      }
  }

  @Test
  fun `Print bool`() = runScenario {
    mapOf(":printBool" to "true", ":printString" to "true").forEach { (task, expected) ->
      assertThatTask(task)
        .withGradleProperty(name = "key", value = "bool")
        .buildsSuccessfully()
        .outputContainsLine(expected)
    }

    listOf(":printDouble", ":printFloat", ":printInt").forEach { task ->
      assertThatTask(task).withGradleProperty(name = "key", value = "bool").failsBuild()
    }
  }

  @Test
  fun `Print bool binary`() = runScenario {
    mapOf(":printBool" to "false").forEach { (task, expected) ->
      assertThatTask(task)
        .withGradleProperty(name = "key", value = "boolBinary")
        .buildsSuccessfully()
        .outputContainsLine(expected)
    }
  }
}
