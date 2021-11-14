plugins {
    kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "com.github.kamunyan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://jitpack.io")}
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly(fileTree("lib/CrackShot.jar"))
    implementation("xyz.xenondevs:particle:1.6.6")
    implementation("com.github.Be4rJP:ChiyogamiLib:f5e45239d5")
}

tasks {
    shadowJar {
        relocate("kotlin", "com.github.leftcrafterdead")
        minimize()
    }

    val dataContent = copySpec {
        from("/build/libs/")
        include("*.jar")
    }

    register("initConfig", Copy::class) {
        val tokens = mapOf("version" to "2.3.1")
        inputs.properties(tokens)
        into("C://Minecraft/paperServer/L4DServer/plugins")
        includeEmptyDirs = false
        with(dataContent)
    }
}
