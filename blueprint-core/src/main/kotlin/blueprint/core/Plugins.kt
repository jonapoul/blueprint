package blueprint.core

import org.gradle.api.Action
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.plugins.PluginManager

public fun PluginManager.withAnyId(vararg ids: String, action: Action<in AppliedPlugin>) {
  ids.forEach { withPlugin(it, action) }
}
