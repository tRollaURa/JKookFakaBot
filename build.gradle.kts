import java.text.SimpleDateFormat
import java.util.*

plugins {
    kotlin("jvm") version "1.9.23"
}

group = "cn.trollaura"
version = SimpleDateFormat("yyyy年MM月dd日").format(Date())

repositories {
    mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }


}


dependencies {
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("com.github.SNWCreations:JKook:0.52.1")
    implementation("org.json:json:20240303")
    implementation(files("lib/TrLib-20240831.jar"))
}

kotlin {
    jvmToolchain(8)
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    dependsOn(configurations.runtimeClasspath)
}


