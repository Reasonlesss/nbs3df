package cloud.emilys.nbs3df.util

import java.io.File

fun File.toByteReader(): ByteReader = ByteReader(readBytes())