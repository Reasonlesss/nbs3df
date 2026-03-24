package codes.reason.nbs3df.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

fun ByteArray.toBase64(): String {
    return Base64.getEncoder().encodeToString(this)
}

fun String.decodeBase64(): ByteArray {
    return Base64.getDecoder().decode(this)
}

fun ByteArray.compress(): ByteArray {
    ByteArrayOutputStream().use { bos ->
        GZIPOutputStream(bos).use { gos ->
            gos.write(this)
        }
        return bos.toByteArray()
    }
}

fun ByteArray.decompress(): ByteArray {
    ByteArrayInputStream(this).use { bis ->
        GZIPInputStream(bis).use { gis ->
            return gis.readBytes()
        }
    }
}