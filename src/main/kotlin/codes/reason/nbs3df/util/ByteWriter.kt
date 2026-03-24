package codes.reason.nbs3df.util

class ByteWriter(private val list: MutableList<Byte> = mutableListOf()) {
    fun writeByte(byte: Int) {
        list += byte.toByte()
    }

    fun writeByte(byte: Byte) {
        writeByte(byte.toInt())
    }

    fun writeShort(value: Short) {
        writeByte(value.toInt() shr 0)
        writeByte(value.toInt() shr 8)
    }

    fun writeShort(value: Int) {
        writeByte(value shr 0)
        writeByte(value shr 8)
    }

    fun toByteArray(): ByteArray = list.toByteArray()

}