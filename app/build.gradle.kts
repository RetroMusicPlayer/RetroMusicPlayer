import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
    id("org.jetbrains.kotlin.plugin.parcelize")
    alias(libs.plugins.google.devtools.ksp)
}

android {
    compileSdk = 35
    namespace = "code.name.monkey.retromusic"

    defaultConfig {
        minSdk = 21
        targetSdk = 36

        vectorDrawables {
            useSupportLibrary = true
        }

        applicationId = namespace
        versionCode = 10650
        versionName = "6.5.0"

        buildConfigField("String", "GOOGLE_PLAY_LICENSING_KEY", "\"${getProperty(getProperties("../public.properties"), "GOOGLE_PLAY_LICENSE_KEY")}\"")
    }
    val signingProperties = getProperties("retro.properties")
    val theSigningConfig = if (signingProperties != null) {
        signingConfigs.create("release") {
            storeFile = file(getProperty(signingProperties, "storeFile"))
            keyAlias = getProperty(signingProperties, "keyAlias")
            storePassword = getProperty(signingProperties, "storePassword")
            keyPassword = getProperty(signingProperties, "keyPassword")
        }
    } else {
        signingConfigs.getByName("debug")
    }

    buildTypes {
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = theSigningConfig
        }
        getByName("debug") {
            signingConfig = theSigningConfig
            applicationIdSuffix = ".debug"
            versionNameSuffix = " DEBUG"
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("normal") {
            dimension = "version"
        }
        create("fdroid") {
            dimension = "version"
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += listOf(
                "META-INF/LICENSE",
                "META-INF/NOTICE",
                "META-INF/java.properties"
            )
        }
    }
    lint {
        abortOnError = true
        warning.addAll(listOf("ImpliedQuantity", "Instantiatable", "MissingQuantity", "MissingTranslation", "StringFormatInvalid"))
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    configurations.configureEach {
        resolutionStrategy.force("com.google.code.findbugs:jsr305:1.3.9")
    }
}


dependencies {
    implementation(project(":appthemehelper"))
    implementation(libs.gridLayout)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.palette.ktx)

    implementation(libs.androidx.mediarouter)
    //Cast Dependencies
    "normalImplementation"(libs.google.play.services.cast.framework)
    //WebServer by NanoHttpd
    "normalImplementation"(libs.nanohttpd)

    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.common.java8)

    implementation(libs.androidx.core.splashscreen)

    "normalImplementation"(libs.google.feature.delivery)
    "normalImplementation"(libs.google.play.review)
    "normalImplementation"(libs.google.play.billing)


            implementation(libs.android.material)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp3.logging.interceptor)

    implementation(libs.afollestad.material.dialogs.core)
    implementation(libs.afollestad.material.dialogs.input)
    implementation(libs.afollestad.material.dialogs.color)
    implementation(libs.afollestad.material.cab)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.glide)
    ksp(libs.glide.ksp)
    implementation(libs.glide.okhttp3.integration)

    implementation(libs.advrecyclerview)

    implementation(libs.fadingedgelayout)

    implementation(libs.keyboardvisibilityevent)
    implementation(libs.jetradarmobile.android.snowfall)

    implementation(libs.chrisbanes.insetter)


    implementation(libs.org.eclipse.egit.github.core)
    implementation(libs.jaudiotagger)
    implementation(libs.slidableactivity)
    implementation(libs.material.intro)
    implementation(libs.dhaval2404.imagepicker)
    implementation(libs.fastscroll.library)
    implementation(libs.customactivityoncrash)
    implementation(libs.tankery.circularSeekBar)

    implementation(libs.androidx.exoplayer)
}

fun getProperties(fileName: String): Properties? {
    val properties = Properties()
    val file = rootProject.file(fileName)
    if (file.exists()) {
        file.inputStream().use { properties.load(it) }
    } else {
        return null
    }
    return properties
}

fun getProperty(properties: Properties?, name: String): String =
    properties?.getProperty(name) ?: "$name missing"