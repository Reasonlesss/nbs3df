package codes.reason.nbs3df.util

class ByteReader(private val array: ByteArray) {
    private var currentIndex = 0

    fun readByte() : Byte {
        if (currentIndex >= array.size) {
            throw IndexOutOfBoundsException("No more bytes to read from array")
        }
        return array[currentIndex++]
    }

    fun readUByte() : Int = readByte().toInt() and 0xFF

    fun readShort() : Short {
        val b1 = readUByte()
        val b2 = readUByte()

        return ((b2 shl 8) or b1).toShort()
    }

    fun readInt(): Int {
        val b0 = readUByte()
        val b1 = readUByte()
        val b2 = readUByte()
        val b3 = readUByte()
        return (b3 shl 24) or (b2 shl 16) or (b1 shl 8) or b0
    }

    fun readString(): String {
        val length = readInt()
        val builder = StringBuilder()
        for (i in 0 until length) {
            var c = this.readByte().toInt().toChar()
            if (c == 0x0D.toChar()) {
                c = ' '
            }
            builder.append(c)
        }
        return builder.toString()
    }

    fun readBoolean(): Boolean = readByte() != 0.toByte()
}
