plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.resourcefactory)
}

description = "A fabric mod that converts NBS files into DiamondFire code templates"

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.language.kotlin)
    modImplementation(libs.adventure.platform.fabric)
    implementation(libs.adventure.api)
}

fabricModJson {
    id = "nbs3df"
    clientEntrypoint("codes.reason.nbs3df.NBS3DF")
    depends("fabric-language-kotlin", "*")
    depends("minecraft", libs.versions.minecraft.get())

    author("Reasonless") {
        contact.sources = "https://github.com/Reasonless/"
    }
}
