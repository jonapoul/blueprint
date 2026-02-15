package blueprint.core

import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.plugin.use.PluginDependency

context(scope: DependencyHandlerScope)
public fun String.invoke(plugin: Provider<PluginDependency>) {
  val pluginDependency = plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }
  scope.add(this, pluginDependency)
}
