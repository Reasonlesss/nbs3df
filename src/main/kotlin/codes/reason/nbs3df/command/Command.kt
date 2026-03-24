package codes.reason.nbs3df.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

abstract class Command {

    abstract fun get(): LiteralArgumentBuilder<FabricClientCommandSource>

    fun literal(name: String): LiteralArgumentBuilder<FabricClientCommandSource> =
        LiteralArgumentBuilder.literal(name)

    fun <T> required(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<FabricClientCommandSource, T> =
        RequiredArgumentBuilder.argument(name, type)

}

fun CommandDispatcher<FabricClientCommandSource>.register(command: Command) {
    this.register(command.get())
}
