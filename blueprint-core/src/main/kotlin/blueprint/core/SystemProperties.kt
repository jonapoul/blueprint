/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.core

public val isIntellijSyncing: Boolean
  get() = System.getProperty("idea.sync.active") == "true"
