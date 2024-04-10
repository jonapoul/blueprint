@file:Suppress(
  "DeprecatedCallableAddReplaceWith",
  "UnusedParameter",
  "UnusedReceiverParameter",
  "UNUSED_PARAMETER",
  "TooManyFunctions"
)

import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory.PluginFactory
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory.SubDependencyFactory as SDF
import org.gradle.kotlin.dsl.DependencyHandlerScope as DependenciesBlock
import org.gradle.kotlin.dsl.PluginDependenciesSpecScope as PluginsBlock

internal const val ERROR_MESSAGE = "You probably meant to enter some child of the given libs.versions.toml object."

@Deprecated(message = ERROR_MESSAGE, level = DeprecationLevel.ERROR)
public fun DependenciesBlock.implementation(factory: SDF): Unit = error("Not supported!")

@Deprecated(message = ERROR_MESSAGE, level = DeprecationLevel.ERROR)
public fun DependenciesBlock.api(factory: SDF): Unit = error("Not supported!")

@Deprecated(message = ERROR_MESSAGE, level = DeprecationLevel.ERROR)
public fun DependenciesBlock.compileOnly(factory: SDF): Unit = error("Not supported!")

@Deprecated(message = ERROR_MESSAGE, level = DeprecationLevel.ERROR)
public fun DependenciesBlock.runtimeOnly(factory: SDF): Unit = error("Not supported!")

@Deprecated(message = ERROR_MESSAGE, level = DeprecationLevel.ERROR)
public fun DependenciesBlock.testImplementation(factory: SDF): Unit = error("Not supported!")

@Deprecated(message = ERROR_MESSAGE, level = DeprecationLevel.ERROR)
public fun DependenciesBlock.androidTestImplementation(factory: SDF): Unit = error("Not supported!")

@Deprecated(message = ERROR_MESSAGE, level = DeprecationLevel.ERROR)
public fun PluginsBlock.alias(factory: PluginFactory): Unit = error("Not supported!")
