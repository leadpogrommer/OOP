val kotlinVersion: String by extra("1.6.21")

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version ("1.6.21")
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:1.6.21")
    implementation("org.jetbrains.kotlin:kotlin-scripting-common:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host:$kotlinVersion")
    // https://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.1.0.202203080745-r")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:1.7.30")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")

    // https://mvnrepository.com/artifact/org.gradle/gradle-tooling-api
    implementation("org.gradle:gradle-tooling-api:7.3-20210825160000+0000")

    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.11.0")

    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-html-jvm
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.5")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
    implementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    // https://mvnrepository.com/artifact/org.junit.platform/junit-platform-launcher
    implementation("org.junit.platform:junit-platform-launcher:1.8.2")






    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}




tasks.getByName<Test>("test") {
    useJUnitPlatform()
}