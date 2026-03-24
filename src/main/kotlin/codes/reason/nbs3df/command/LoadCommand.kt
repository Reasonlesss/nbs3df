package codes.reason.nbs3df.command

import codes.reason.nbs3df.screen.SongSelectorScreen
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import javax.swing.JFileChooser


object LoadCommand : Command() {

    override fun get(): LiteralArgumentBuilder<FabricClientCommandSource> {
        return literal("load")
                .executes {
                    Minecraft.getInstance().execute {
                        Minecraft.getInstance().setScreen(SongSelectorScreen(Path.of(".")))
                    }
                    1
                }
    }


    private fun resolvePath(input: String): Path {
        val baseDir = FabricLoader.getInstance().gameDir
        val inputPath = Paths.get(input)
        if (inputPath.isAbsolute) {
            return inputPath
        }
        return baseDir.resolve(inputPath)
    }

}