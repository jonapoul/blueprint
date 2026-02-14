package blueprint.test

import java.io.File

public class FileTree private constructor(
  private val mutableFiles: MutableMap<String, String>,
) {
  public val files: Map<String, String> = mutableFiles

  public operator fun set(path: String, contents: String): String? = mutableFiles.put(key = path, value = contents)

  public operator fun get(path: String): String? = mutableFiles[path]

  public class Builder(
    private val relativeRootPath: String,
  ) {
    internal val files: MutableMap<String, String> = mutableMapOf()

    public fun build(): FileTree = FileTree(files)

    public operator fun String.invoke(config: Builder.() -> Unit) {
      val subfolder = relativeRootPath / this
      files.putAll(
        Builder(subfolder)
          .apply(config)
          .build()
          .files,
      )
    }

    public operator fun String.invoke(content: String) {
      files[relativeRootPath / this] = content
    }

    public operator fun String.div(other: String): String = when {
      !isEmpty() && !other.isEmpty() -> "$this${File.separator}$other"
      isEmpty() -> other
      else -> this
    }
  }
}

public const val DEFAULT_REPOSITORIES_KTS: String = """
  pluginManagement {
    repositories {
      mavenCentral()
      google()
      gradlePluginPortal()
    }
  }

  dependencyResolutionManagement {
    repositories {
      google()
      mavenCentral()
    }
  }
"""
