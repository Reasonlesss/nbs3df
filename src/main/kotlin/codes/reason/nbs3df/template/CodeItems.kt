package codes.reason.nbs3df.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ItemData

@Serializable
sealed class CodeItem<T : ItemData> {
    abstract val data: T
}

@Serializable
@SerialName("txt")
class StringItem(override val data: StringData) : CodeItem<StringItem.StringData>() {
    @Serializable
    class StringData(val name: String) : ItemData()
}

@Serializable
@SerialName("num")
class NumberItem(override val data: NumberData) : CodeItem<NumberItem.NumberData>() {
    @Serializable
    class NumberData(val name: String) : ItemData()
}

@Serializable
@SerialName("var")
class VariableItem(override val data: VariableData) : CodeItem<VariableItem.VariableData>() {
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
    @Serializable
    class SoundData(
        val pitch: Float,
        val vol: Float,
        val sound: String? = null,
        val key: String? = null,
        val variant: String? = null
    ) : ItemData()
}

fun number(value: String) = NumberItem(NumberItem.NumberData(value))
fun string(value: String) = StringItem(StringItem.StringData(value))
fun local(value: String) = VariableItem(VariableItem.VariableData(value, VariableItem.Scope.LOCAL))
fun line(value: String) = VariableItem(VariableItem.VariableData(value, VariableItem.Scope.LINE))
fun tag(option: String, tag: String, action: String, block: String) = BlockTagItem(BlockTagItem.BlockTagData(
    option, tag, action, block
))