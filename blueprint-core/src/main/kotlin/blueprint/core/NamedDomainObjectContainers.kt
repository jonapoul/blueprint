/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.core

import org.gradle.api.NamedDomainObjectContainer

public fun <T : Any> NamedDomainObjectContainer<T>.getOrCreate(
  name: String,
): T = findByName(name) ?: create(name)

public fun <T : Any> NamedDomainObjectContainer<T>.getOrCreate(
  name: String,
  action: T.() -> Unit,
): T = getOrCreate(name).apply(action)
