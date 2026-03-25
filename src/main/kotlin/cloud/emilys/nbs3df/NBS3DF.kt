package cloud.emilys.nbs3df

import cloud.emilys.nbs3df.util.template.setCodeTemplate
import cloud.emilys.nbs3df.preview.SongPreviewHandler
import cloud.emilys.nbs3df.screen.FileNavigatorScreen
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.toasts.SystemToast
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack

class NBS3DF : ClientModInitializer {

    companion object {
        private val NO_CREATIVE = SystemToast.SystemToastId()
        private val NOT_ENOUGH_SPACE = SystemToast.SystemToastId()
        private val SKIPPED_ITEMS = SystemToast.SystemToastId()

        fun id(identifier: String): Identifier = Identifier.fromNamespaceAndPath("nbs3df", identifier)

        fun giveIfNotPresent(items: List<ItemStack>) {
            val minecraft = Minecraft.getInstance()
            val player = Minecraft.getInstance().player!!
            val gameMode = Minecraft.getInstance().gameMode!!
            var skipped = 0
            var givenItems = 0
            if (!player.isCreative) {
                minecraft.toastManager.addToast(SystemToast(
                    NO_CREATIVE,
                    Component.translatable("nbs3df.toast.noCreative.title"),
                    Component.translatable("nbs3df.toast.noCreative.description", items.size),
                ))
                return
            }
            val inventory = player.inventory
            for (stack in items) {
                if (inventory.findSlotMatchingItem(stack) != -1) {
                    skipped++
                    continue
                }

                val slot = player.inventory.freeSlot

                if (slot == -1) {
                    minecraft.toastManager.addToast(SystemToast(
                        NOT_ENOUGH_SPACE,
                        Component.translatable("nbs3df.toast.notEnoughSpace.title"),
                        Component.translatable("nbs3df.toast.notEnoughSpace.description", items.size - givenItems),
                    ))
                    return
                }

                val packetSlot = when {
                    slot < 9 -> slot + 36
                    else -> slot
                }

                gameMode.handleCreativeModeItemAdd(stack, packetSlot)
                inventory.setItem(slot, stack)

                givenItems++
            }
            if (skipped > 0) {
                minecraft.toastManager.addToast(SystemToast(
                    SKIPPED_ITEMS,
                    Component.translatable("nbs3df.toast.skippedItems.title"),
                    Component.translatable("nbs3df.toast.skippedItems.description", skipped, items),
                ))
            }
            Minecraft.getInstance().soundManager.play(
                SimpleSoundInstance.forUI(
                    SoundEvents.CHICKEN_EGG,
                    1.0f
                )
            )
        }
    }

    override fun onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register { manager, _ ->
            manager.register(
                LiteralArgumentBuilder.literal<FabricClientCommandSource>("nbs")
                    .then(LiteralArgumentBuilder.literal<FabricClientCommandSource>("player")
                        .executes {
                            val player = PlayerIconGenerator.makePlayerIcon()
                            player.setCodeTemplate("nbs3df", PlayerTemplate.TEMPLATE)
                            giveIfNotPresent(listOf(player))
                            1
                        })
                    .then(LiteralArgumentBuilder.literal<FabricClientCommandSource>("import")
                        .executes {
                            Minecraft.getInstance().execute {
                                Minecraft.getInstance().setScreen(FileNavigatorScreen(FabricLoader.getInstance().gameDir))
                            }
                            1
                        })
            )

        }
        SongPreviewHandler.setup()
    }

}