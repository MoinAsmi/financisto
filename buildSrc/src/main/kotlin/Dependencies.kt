import org.gradle.api.JavaVersion

object Versions {
    // Languages and Tooling
    const val jvmTarget = "1.8"
    const val kotlin = "1.8.20"
    const val androidGradlePlugin = "8.0.0"
    
    // SDK Versions
    const val compileSdk = 33
    const val minSdk = 21
    const val targetSdk = 33
    
    // AndroidX
    const val coreKtx = "1.10.1"
    const val appCompat = "1.6.1"
    const val activity = "1.7.1"
    const val fragment = "1.5.7"
    const val lifecycle = "2.6.1"
    const val navigation = "2.5.3"
    const val room = "2.5.1"
    const val recyclerView = "1.3.0"
    
    // Material Design
    const val material = "1.9.0"
    
    // Kotlin
    const val coroutines = "1.7.1"
    
    // DI
    const val koin = "3.4.0"
    
    // Logging
    const val timber = "5.0.1"
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    
    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val activity = "androidx.activity:activity-ktx:${Versions.activity}"
        const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    }
    
    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }
    
    object Material {
        const val material = "com.google.android.material:material:${Versions.material}"
    }
    
    object DI {
        const val koin = "io.insert-koin:koin-android:${Versions.koin}"
    }
    
    object Logging {
        const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    }
}

object Config {
    val javaVersion = JavaVersion.VERSION_11
    const val jvmTarget = Versions.jvmTarget
    
    object Android {
        const val compileSdk = Versions.compileSdk
        const val minSdk = Versions.minSdk
        const val targetSdk = Versions.targetSdk
    }
}
