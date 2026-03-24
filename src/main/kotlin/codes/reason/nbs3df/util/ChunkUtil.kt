package codes.reason.nbs3df.util

fun ByteArray.chunked(chunkSize: Int): List<ByteArray> {
    val chunks = mutableListOf<ByteArray>()
    var start = 0
    while (start < this.size) {
        val end = (start + chunkSize).coerceAtMost(this.size)
        chunks.add(this.copyOfRange(start, end))
        start = end
    }
    return chunks.toList()
}