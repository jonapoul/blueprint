/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.core

import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.kotlin.dsl.support.illegalElementType
import kotlin.reflect.KProperty

public operator fun <T : Any> NamedDomainObjectCollection<T>.provideDelegate(
  thisRef: Any?,
  property: KProperty<*>,
): NamedDomainObjectProvider<T> = named(property.name)

public inline operator fun <T : Any, reified U : T> NamedDomainObjectProvider<out T>.getValue(
  thisRef: Any?,
  property: KProperty<*>,
): U = get().let { it as? U ?: throw illegalElementType(this, property.name, U::class, it::class) }
