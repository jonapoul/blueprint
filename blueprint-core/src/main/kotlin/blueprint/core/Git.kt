package blueprint.core

import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.ZoneOffset
import java.util.Locale
import javax.inject.Inject

public fun ProviderFactory.gitVersionHash(): Provider<String> = of(GitVersionHashValueSource::class.java) {}

public fun ProviderFactory.gitVersionCode(): Provider<Int> = of(GitVersionCodeValueSource::class.java) {}

public fun ProviderFactory.gitVersionDate(): Provider<String> = gitVersionCode().map { seconds ->
  val date = Instant
    .ofEpochSecond(seconds.toLong())
    .atZone(ZoneOffset.UTC)
    .toLocalDate()
  "%04d.%02d.%02d".format(Locale.getDefault(), date.year, date.monthValue, date.dayOfMonth)
}

private abstract class GitVersionHashValueSource : ValueSource<String, ValueSourceParameters.None> {
  @get:Inject abstract val execOperations: ExecOperations

  override fun obtain(): String = ByteArrayOutputStream().use { baos ->
    execOperations.exec {
      commandLine("git", "rev-parse", "--short=8", "HEAD")
      standardOutput = baos
    }
    baos.toString().trim()
  }
}

private abstract class GitVersionCodeValueSource : ValueSource<Int, ValueSourceParameters.None> {
  @get:Inject abstract val execOperations: ExecOperations

  override fun obtain(): Int {
    val result = ByteArrayOutputStream().use { baos ->
      execOperations.exec {
        commandLine("git", "show", "-s", "--format=%ct")
        standardOutput = baos
      }
      baos.toString().trim()
    }
    return requireNotNull(result.toIntOrNull()) { "Expected integer output, got $result" }
  }
}
