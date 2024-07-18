package blueprint.recipes

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude

public fun isTakGovPipeline(): Boolean = System.getenv("ATAK_CI")?.toIntOrNull() == 1

@Suppress("UnstableApiUsage")
public fun Project.atakBlueprint(
  atakSdk: Provider<MinimalExternalModuleDependency>,
  excludes: List<String> = DEFAULT_EXCLUDES,
) {
  configurations.configureEach { config ->
    // Exclude dependencies which are already included in the SDK jar
    excludes.forEach {
      val split = it.split(":")
      config.exclude(group = split[0], module = split[1])
    }
  }

  val compileOnly = configurations.getByName("compileOnly")
  val testImplementation = configurations.getByName("testImplementation")

  // Fetch these off my machine's mavenLocal. On the TPP, takdev plugin handles this instead
  if (!isTakGovPipeline()) {
    dependencies {
      compileOnly(atakSdk)
      testImplementation(atakSdk)
    }
  }
}

public val DEFAULT_EXCLUDES: List<String> = listOf(
  "androidx.activity:activity",
  "androidx.annotation:annotation",
  "androidx.annotation:annotation-experimental",
  "androidx.arch.core:core-common",
  "androidx.arch.core:core-runtime",
  "androidx.collection:collection",
  "androidx.concurrent:concurrent-futures",
  "androidx.core:core",
  "androidx.core:core-ktx",
  "androidx.customview:customview",
  "androidx.customview:customview-poolingcontainer",
  "androidx.exifinterface:exifinterface",
  "androidx.fragment:fragment",
  "androidx.lifecycle:lifecycle-livedata",
  "androidx.lifecycle:lifecycle-livedata-core",
  "androidx.lifecycle:lifecycle-process",
  "androidx.lifecycle:lifecycle-runtime",
  "androidx.lifecycle:lifecycle-runtime-ktx",
  "androidx.lifecycle:lifecycle-viewmodel",
  "androidx.lifecycle:lifecycle-viewmodel-savedstate",
  "androidx.loader:loader",
  "androidx.localbroadcastmanager:localbroadcastmanager",
  "androidx.profileinstaller:profileinstaller",
  "androidx.savedstate:savedstate",
  "androidx.startup:startup-runtime",
  "androidx.tracing:tracingsavedstate",
  "androidx.versionedparcelable:versionedparcelable",
  "androidx.viewpager:viewpager",
  "org.jetbrains.kotlin:kotlin-stdlib-jdk7",
  "org.jetbrains.kotlin:kotlin-stdlib-jdk8",
  "org.jetbrains.kotlinx:kotlinx-coroutines-core",
  "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm",
  "org.jetbrains.kotlinx:kotlinx-coroutines-android",

  /**
   * Don't exclude either of these! If you do, you'll get an error like below when opening a fragment at runtime:
   * java.lang.AbstractMethodError: abstract method "void androidx.lifecycle.DefaultLifecycleObserver.onCreate(
   * androidx.lifecycle.LifecycleOwner)"
   */
//   "androidx.lifecycle:lifecycle-common",
//   "androidx.lifecycle:lifecycle-common-java8",
)
