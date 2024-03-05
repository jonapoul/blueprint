package blueprint.kotlin

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

internal fun Project.setJavaVersion(javaVersion: Int) {
//   extensions.configure(KotlinTopLevelExtension::class.java) { ext ->
//     ext.jvmToolchain(javaVersion)
//   }

//   extensions.configure(JavaPluginExtension::class.java) { java ->
//     java.sourceCompatibility = JavaVersion.toVersion(javaVersion)
//     java.targetCompatibility = JavaVersion.toVersion(javaVersion)
//   }
}
