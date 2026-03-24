package codes.reason.nbs3df

import codes.reason.nbs3df.command.LoadCommand
import codes.reason.nbs3df.command.PlayerCommand
import codes.reason.nbs3df.command.register
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.resources.Identifier

class NBS3DF : ClientModInitializer {

    companion object {
        fun id(identifier: String): Identifier = Identifier.fromNamespaceAndPath("nbs3df", identifier)
    }

    override fun onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register { manager, _ ->
            manager.register(LoadCommand)
            manager.register(PlayerCommand)
        }
    }

}`