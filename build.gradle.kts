plugins {
    kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "com.github.kamunyan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven{
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly(fileTree("lib/CrackShot.jar"))
    implementation("org.jetbrains.exposed:exposed-core:0.37.3")
    implementation("org.jetbrains.exposed:exposed-dao:0.37.3")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.37.3")
    implementation("mysql:mysql-connector-java:8.0.25")
    implementation("xyz.xenondevs:particle:1.7")
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}


tasks {
    shadowJar{
        mergeServiceFiles()
    }


    val dataContent = copySpec {
        from("/build/libs/")
        include("*.jar")
    }

    register("initConfig", Copy::class) {
        val tokens = mapOf("version" to "2.3.1")
        inputs.properties(tokens)
        into("C://Minecraft/LCDServer/plugins")
        includeEmptyDirs = false
        with(dataContent)
    }
}
