plugins {
    kotlin("jvm") version "2.0.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:atomicfu:0.21.0")

    testImplementation(kotlin("test"))

    testImplementation("org.jetbrains.kotlinx:lincheck:2.34")

    testImplementation("junit:junit:4.13")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")


}

tasks.test {
    useJUnitPlatform()



}
kotlin {
    jvmToolchain(21)
}