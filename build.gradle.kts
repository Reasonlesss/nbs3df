plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.loom)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.resourcefactory)
    alias(libs.plugins.kotlin.serialization)
}

description = "A fabric mod that converts NBS files into DiamondFire code templates"

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.language.kotlin)
    implementation(libs.serialization.json)
}

tasks.processResources {
    from(file("data")) {
        include("sound_names.json")
        include("sound_files.json")
    }
}

tasks.shadowJar {
    relocate("kotlinx.serialization", "cloud.emilys.nbs3df.shaded.kotlinx.serialization")
    mergeServiceFiles()
    minimize()
}

fabricModJson {
    id = "nbs3df"
    clientEntrypoint("cloud.emilys.nbs3df.NBS3DF")
    depends("fabric-language-kotlin", "*")
    depends("minecraft", libs.versions.minecraft.get())

    author("Reasonless") {
        contact.sources = "https://github.com/Reasonless/"
    }
}
