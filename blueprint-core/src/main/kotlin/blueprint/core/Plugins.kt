package blueprint.core

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.plugins.PluginContainer

public fun PluginContainer.withAnyId(vararg ids: String, action: Action<in Plugin<*>>) {
  ids.forEach { withId(it, action) }
}
