import org.gradle.kotlin.dsl.androidTestImplementation

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.plugin.serialization")
}
android {
  namespace = "com.example.bduisample"
  compileSdk = 35
  defaultConfig {
    applicationId = "com.example.bduisample"
    minSdk = 24
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildFeatures { viewBinding = true }
  kotlinOptions { jvmTarget = "1.8" }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}
dependencies {
  implementation("androidx.coordinatorlayout:coordinatorlayout:1.3.0")
  implementation("com.google.code.gson:gson:2.11.0")
  androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.13.0")
  implementation("androidx.core:core-ktx:1.13.1")
  implementation("io.coil-kt:coil:2.7.0")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

  androidTestImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
  androidTestImplementation("com.kaspersky.android-components:kaspresso:1.5.3")
  androidTestImplementation("io.github.kakaocup:kakao:3.6.0")
}
