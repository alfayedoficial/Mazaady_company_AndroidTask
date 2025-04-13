plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrainsKotlinKapt)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.navigation.safe.args)
}

android {
    namespace = "com.afapps.mazaadyAndroidTask"
    compileSdk = 35

    signingConfigs {
        create("signingConfigRelease") {
            storeFile = file(KeyHelper.getValue(KeyHelper.KEY_STORE_FILE))
            storePassword = KeyHelper.getValue(KeyHelper.KEY_STORE_PASS)
            keyAlias = KeyHelper.getValue(KeyHelper.KEY_ALIAS)
            keyPassword = KeyHelper.getValue(KeyHelper.KEY_PASS)
        }
    }

    defaultConfig {
        applicationId = "com.afapps.mazaadyAndroidTask"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("signingConfigRelease")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions("appType")
    productFlavors {
        create("_dev") {
            dimension = "appType"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            signingConfig = signingConfigs.getByName("signingConfigRelease")

            addManifestPlaceholders(
                mapOf(
                    "appIcon" to "@mipmap/ic_launcher_dev",
                    "appIconRound" to "@mipmap/ic_launcher_dev"
                )
            )
            resValue("string", "app_name", "Mazaady AndroidTask$versionNameSuffix")
            buildConfigField("String", "SERVER_URL", "\"${KeyHelper.getValue(KeyHelper.KEY_SERVER_URL_DEV)}\"")
            buildConfigField("String", "POSTER_URL", "\"${KeyHelper.getValue(KeyHelper.KEY_POSTER_IMAGE_URL)}\"")

        }


        create("_prod") {
            dimension = "appType"
            applicationIdSuffix = ""
            versionNameSuffix = ""
            signingConfig = signingConfigs.getByName("signingConfigRelease")

            addManifestPlaceholders(
                mapOf(
                    "appIcon" to "@mipmap/ic_launcher",
                    "appIconRound" to "@mipmap/ic_launcher"
                )
            )

            buildConfigField("String", "SERVER_URL", "\"${KeyHelper.getValue(KeyHelper.KEY_SERVER_URL_PROD)}\"")
            buildConfigField("String", "POSTER_URL", "\"${KeyHelper.getValue(KeyHelper.KEY_POSTER_IMAGE_URL)}\"")
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    /*-----------APP Config Dependencies---------*/
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)

    /*-----------UI Dependencies---------*/
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.coil)
    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.androidx.paging.runtime.ktx)


    /*-----------Thread Dependencies---------*/
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    /*-----------Room Dependencies---------*/
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)

    /*-----------Network Dependencies--------*/
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    /*-----------Dependency Injection Dependencies---------*/
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.android)

    /*-----------Testing Dependencies---------*/
    testImplementation(libs.junit)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation (libs.androidx.core)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.rules)
    testImplementation(libs.robolectric)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.paging.common)

}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}