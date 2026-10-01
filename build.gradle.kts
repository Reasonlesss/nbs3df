plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.resourcefactory)
    alias(libs.plugins.kotlin.serialization)
}

fun Project.git(command: String): String? {
    return try {
        providers.exec { commandLine(("git $command").split(" ")) }
            .standardOutput
            .asText
            .get()
            .trim()
    } catch (_: Exception) {
        null
    }
}

group = "cloud.emilys"
version = git("describe --tags --abbrev=0")
    ?.removePrefix("v")
    ?: "0.0.0"
description = "An open source mod for converting Noteblock Studio songs into DiamondFire Code Templates."

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabric.api)
    implementation(libs.fabric.loader)
    implementation(libs.fabric.language.kotlin)
    include(libs.serialization.json)
}

tasks.processResources {
    from(file("data")) {
        include("sound_names.json")
        include("sound_files.json")
    }
}

fabricModJson {
    id = "nbs3df"
    icon("assets/nbs3df/icon.png")
    clientEntrypoint("cloud.emilys.nbs3df.NBS3DF")
    depends("fabric-language-kotlin", "*")
    depends("minecraft", libs.versions.minecraft.get())

    author("Reasonless") {
        contact.sources = "https://github.com/Reasonless/"
    }
    contributor("Floophead") {
    }
    contributor("RedVortx") {
        contact.sources = "https://github.com/RedVortxDev/"
    }
}
