val ktor_version: String by project
val version: String by project

plugins {
    application
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
}

application {
    applicationDefaultJvmArgs = listOf("-Dapple.awt.UIElement=true")
    mainClass.set("app.MainKt")
}

group = "com.fadegor05"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("com.github.ajalt.mordant:mordant:3.0.1")
    implementation("com.github.ajalt.mordant:mordant-coroutines:3.0.1")
    implementation("org.slf4j:slf4j-api:2.0.0")  // Если еще не добавлено
    implementation("ch.qos.logback:logback-classic:1.4.5")  // Добавьте это
    implementation("org.openjfx:javafx-controls:17")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")

}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Exec>("createMsi") {
    group = "build"
    description = "Creates MSI file from fatJar"
    dependsOn("fatJar")
    doFirst {
        val jarTask = tasks.named("fatJar").get() as Jar
        val jarFile = jarTask.archiveFile.get().asFile

        commandLine(
            "jpackage",
            "--input", "./build/libs",
            "--name", "CubeRestart",
            "--main-jar", jarFile.name,
            "--main-class", "app.MainKt",
            "--type", "msi",
            "--resource-dir", "./resources",
            "--win-shortcut",
            "--win-console"
        )
    }
}

tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Assembles a fat JAR including all dependencies."

    // Укажите основной класс
    manifest {
        attributes["Main-Class"] = "app.MainKt" // Замените на ваш главный класс
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Добавляем ваш код
    from(sourceSets.main.get().output)

    // Добавляем все зависимости
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.exists() }.map { if (it.isDirectory) it else zipTree(it) }
    })

    archiveClassifier.set("all") // Добавляет суффикс "-all" к имени JAR
}

kotlin {
    jvmToolchain(17)
}