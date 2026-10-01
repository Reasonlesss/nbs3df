package cloud.emilys.nbs3df.util.template

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.client.Minecraft
import net.minecraft.nbt.NbtOps
import net.minecraft.util.NullOps
import net.minecraft.world.item.ItemStack

@Serializable
sealed class ItemData

@Serializable
sealed class CodeItem<T : ItemData>() {
    abstract val data: T

    @EncodeDefault
    abstract val version: Int

}

@Serializable
@SerialName("txt")
class StringItem(override val data: StringData) : CodeItem<StringItem.StringData>() {
    @EncodeDefault
    override val version: Int = 0

    @Serializable
    class StringData(val name: String) : ItemData()
}

@Serializable
@SerialName("num")
class NumberItem(override val data: NumberData) : CodeItem<NumberItem.NumberData>() {
    @EncodeDefault
    override val version: Int = 0

    @Serializable
    class NumberData(val name: String) : ItemData()
}

@Serializable
@SerialName("var")
class VariableItem(override val data: VariableData) : CodeItem<VariableItem.VariableData>() {
    @EncodeDefault
    override val version: Int = 0

    @Serializable
    class VariableData(val name: String, val scope: Scope) : ItemData()

    @Serializable
    enum class Scope {
        @SerialName("line")
        LINE,
        @SerialName("local")
        LOCAL,
        @SerialName("unsaved")
        GAME,
        @SerialName("save")
        SAVE
    }
}

@Serializable
@SerialName("bl_tag")
class BlockTagItem(override val data: BlockTagData) : CodeItem<BlockTagItem.BlockTagData>() {
    @EncodeDefault
    override val version: Int = 0

    @Serializable
    class BlockTagData(
        val option: String,
        val tag: String,
        val action: String,
        val block: String
    ) : ItemData()
}

@Serializable
@SerialName("snd")
class SoundItem(override val data: SoundData) : CodeItem<SoundItem.SoundData>() {
    @EncodeDefault
    override val version: Int = 1

    @EncodeDefault
    val mappingVersion = 0;

    @Serializable
    class SoundData(
        val pitch: Float,
        val vol: Float,
        val sound: String? = null,
        val key: String? = null,
        val variant: String? = null
    ) : ItemData()
}

@Serializable
@SerialName("item")
class VanillaItem(override val data: VanillaItemData) : CodeItem<VanillaItem.VanillaItemData>() {
    @EncodeDefault
    override val version: Int = 0

    @Serializable
    class VanillaItemData(
        val item: String
    ) : ItemData()
}

fun ItemStack.toCodeItem(): VanillaItem {
    val registryAccess = Minecraft.getInstance().level!!.registryAccess()
    val item = ItemStack.CODEC.encodeStart(
        registryAccess.createSerializationContext(NbtOps.INSTANCE),
        this
    )
    return VanillaItem(VanillaItem.VanillaItemData(item.orThrow.toString()))
}
