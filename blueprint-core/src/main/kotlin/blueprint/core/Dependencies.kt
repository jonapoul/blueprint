@file:Suppress("CONTEXT_RECEIVERS_DEPRECATED")

package blueprint.core

import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.plugin.use.PluginDependency

context(DependencyHandlerScope)
public fun String.invoke(plugin: Provider<PluginDependency>) {
  val pluginDependency = plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }
  add(this, pluginDependency)
}
