package blueprint.core

import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.ZoneOffset
import java.util.Locale
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.process.ExecOperations

public fun Project.gitVersionHash(): Provider<String> =
  GitInfoService.registerOrGet(this).versionHash

public fun Project.gitVersionCode(): Provider<Int> = GitInfoService.registerOrGet(this).versionCode

public fun Project.gitVersionDate(): Provider<String> = gitVersionCode().toVersionDate()

private fun Provider<Int>.toVersionDate(): Provider<String> = map { seconds ->
  val date = Instant.ofEpochSecond(seconds.toLong()).atZone(ZoneOffset.UTC).toLocalDate()
  "%04d.%02d.%02d".format(Locale.getDefault(), date.year, date.monthValue, date.dayOfMonth)
}

internal abstract class GitInfoService : BuildService<GitInfoService.Parameters> {
  interface Parameters : BuildServiceParameters {
    val versionHash: Property<String>
    val versionCode: Property<Int>
  }

  val versionHash: Provider<String>
    get() = parameters.versionHash

  val versionCode: Provider<Int>
    get() = parameters.versionCode

  companion object {
    fun registerOrGet(project: Project): GitInfoService =
      project.gradle.sharedServices
        .registerIfAbsent("gitInfoService", GitInfoService::class.java) { spec ->
          val workingDir = project.layout.projectDirectory
          spec.parameters.versionHash.set(
            project.providers.of(GitVersionHashValueSource::class.java) { source ->
              source.parameters.workingDir.set(workingDir)
            }
          )
          spec.parameters.versionCode.set(
            project.providers.of(GitVersionCodeValueSource::class.java) { source ->
              source.parameters.workingDir.set(workingDir)
            }
          )
        }
        .get()
  }
}

private interface GitValueSourceParameters : ValueSourceParameters {
  val workingDir: DirectoryProperty
}

private abstract class GitVersionHashValueSource : ValueSource<String, GitValueSourceParameters> {
  @get:Inject abstract val execOperations: ExecOperations

  override fun obtain(): String =
    ByteArrayOutputStream().use { baos ->
      execOperations.exec { spec ->
        spec.commandLine("git", "rev-parse", "HEAD")
        spec.standardOutput = baos
        spec.workingDir = parameters.workingDir.get().asFile
      }
      baos.toString().trim()
    }
}

private abstract class GitVersionCodeValueSource : ValueSource<Int, GitValueSourceParameters> {
  @get:Inject abstract val execOperations: ExecOperations

  override fun obtain(): Int {
    val result =
      ByteArrayOutputStream().use { baos ->
        execOperations.exec { spec ->
          spec.commandLine("git", "show", "-s", "--format=%ct")
          spec.standardOutput = baos
          spec.workingDir = parameters.workingDir.get().asFile
        }
        baos.toString().trim()
      }
    return requireNotNull(result.toIntOrNull()) { "Expected integer output, got $result" }
  }
}
