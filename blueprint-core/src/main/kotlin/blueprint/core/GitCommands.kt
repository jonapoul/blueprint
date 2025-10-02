/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.core

import org.gradle.api.Project

public fun Project.gitVersionHash(): String = execCommand("git", "rev-parse", "--short=8", "HEAD")

public fun Project.gitVersionCode(): Int = execCommand("git", "show", "-s", "--format=%ct").toInt()

public fun Project.execCommand(vararg args: String): String = providers
  .exec { it.commandLine(*args) }
  .standardOutput
  .asText
  .get()
  .trim('\n', ' ')
