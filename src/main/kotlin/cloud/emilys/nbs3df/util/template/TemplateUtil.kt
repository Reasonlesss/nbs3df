package cloud.emilys.nbs3df.util.template

import com.google.gson.JsonObject
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData

fun ItemStack.setCodeTemplate(author: String, template: String) {
    val templateData = JsonObject()
    templateData.addProperty("author", author)
    templateData.addProperty("version", 1)
    templateData.addProperty("name", this.displayName.string)
    templateData.addProperty("code", template)

    val compound = CompoundTag()
    val publicBukkitValues = CompoundTag()
    publicBukkitValues.putString("hypercube:codetemplatedata", templateData.toString())
    compound.put("PublicBukkitValues", publicBukkitValues)

    this[DataComponents.CUSTOM_DATA] = CustomData.of(compound)
}
