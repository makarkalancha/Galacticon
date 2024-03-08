plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

//val support_version = rootProject.extra.get("support_version") as String

android {
    namespace = "com.makco.galacticon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.makco.galacticon"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        resValue("string", "nasa_api_key", (project.findProperty("NASA_API_KEY") as? String).orEmpty())
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //TODO !!!!find a way to externalize version variables!!!!
//    implementation("androidx.appcompat:appcompat:$rootProject.appCompatVersion")
    implementation("androidx.appcompat:appcompat:1.4.0")

//    implementation("androidx.activity:activity-ktx:$rootProject.activityVersion")
    implementation("androidx.activity:activity-ktx:1.4.0")
//    implementation("androidx.constraintlayout:constraintlayout:$rootProject.constraintLayoutVersion")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
//    implementation("com.google.android.material:material:$rootProject.materialVersion")
    implementation("com.google.android.material:material:1.4.0")
//    implementation("com.android.support:recyclerview-v7:28.0.0")
//    implementation("androidx.legacy:legacy-support-v4:1.0.0")
//    implementation("androidx.legacy:legacy-support-v13:1.0.0")
//    implementation("androidx.cardview:cardview:1.0.0")
//    implementation("androidx.appcompat:appcompat:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")

    implementation("com.squareup.okhttp3:okhttp:3.10.0")
    implementation("com.squareup.picasso:picasso:2.5.2")



}