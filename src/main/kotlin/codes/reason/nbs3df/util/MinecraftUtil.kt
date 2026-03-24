package codes.reason.nbs3df.util

import codes.reason.nbs3df.template.CodeTemplateData
import com.google.gson.JsonObject
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.minecraft.client.Minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.component.ItemLore

fun giveItem(item: ItemStack) {
    val client = Minecraft.getInstance()
    val availableSlot = client.player!!.inventory.freeSlot

    if (!client.player!!.mainHandItem.isEmpty || availableSlot == -1) {
        client.gameMode!!.handleCreativeModeItemAdd(item, client.player!!.inventory.selectedSlot + 36)
        return;
    }

    if (availableSlot < 9) {
        client.gameMode!!.handleCreativeModeItemAdd(item, availableSlot + 36)
    }
    client.player!!.inventory.setItem(availableSlot, item)
}

fun makeTemplateItem(
    name: Component,
    lore: List<Component> = listOf(),
    type: Item,
    template: String,
    author: String = "nbs3df"
): ItemStack {
    val strippedName = PlainTextComponentSerializer.plainText().serialize(name)
    val stack = ItemStack(type)
    stack[DataComponents.CUSTOM_NAME] = MinecraftClientAudiences.of().asNative(name)
    stack[DataComponents.LORE] = ItemLore(lore.map {
        MinecraftClientAudiences.of().asNative(it)
    })

    val templateData = JsonObject()
    templateData.addProperty("author", author)
    templateData.addProperty("version", 1)
    templateData.addProperty("name", strippedName)
    templateData.addProperty("code", template)

    val compound = CompoundTag()
    val publicBukkitValues = CompoundTag()
    publicBukkitValues.putString("hypercube:codetemplatedata", templateData.toString())
    compound.put("PublicBukkitValues", publicBukkitValues)

    stack[DataComponents.CUSTOM_DATA] = CustomData.of(compound)
    return stack
}

fun makeTemplateItem(
    name: Component,
    lore: List<Component> = listOf(),
    type: Item,
    template: CodeTemplateData,
    author: String = "nbs3df"
): ItemStack {
    return makeTemplateItem(name, lore, type, template.encode(), author)
}