plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.skyslycer.commandcreator"
version = "1.2.0"

val shadePattern = "$group.shade"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://oss.sonatype.org/content/groups/public")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.1")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.6")
    implementation("me.mattstudios.utils:matt-framework:1.4")
    implementation("net.kyori:adventure-text-minimessage:4.10.0-SNAPSHOT")
    implementation("net.kyori:adventure-platform-bukkit:4.0.1")
    implementation("com.tchristofferson:ConfigUpdater:2.0-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(16))
}

tasks {
    shadowJar {
        relocate("me.mattstudios.mf", "$shadePattern.mf")
        relocate("net.kyori.adventure", "$shadePattern.adventure")
        relocate("com.tchristofferson.configupdater", "$shadePattern.configupdater")
        val nullClassifier: String? = null
        archiveClassifier.set(nullClassifier)
    }

    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    main = "de.skyslycer.commandcreator.CommandCreator"
    name = "CommandCreator"
    authors = listOf("Skyslycer")
    permissions {
        register("commandcreator.admin") {
            description = "Gives access to the /ccr command."
        }
        register("commandcreator.bypass") {
            description = "Gives access to disabled commands."
        }
    }
}