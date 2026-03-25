package cloud.emilys.nbs3df.screen

import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.io.path.extension
import kotlin.io.path.isHidden

object FileNavigator {

    fun searchPath(path: Path, showHiddenFiles: Boolean) : CompletableFuture<List<FileEntry>> {
        return CompletableFuture
            .supplyAsync {
                val stream = Files.list(path)
                    .sorted(
                        compareBy<Path> { Files.isRegularFile(it) }
                            .thenBy { it.fileName.toString().lowercase() }
                    )
                    .map {
                        if (!showHiddenFiles && it.isHidden()) {
                            return@map null
                        }
                        when {
                            Files.isDirectory(it) -> FileEntry(it, FileType.DIRECTORY)
                            it.extension == "nbs" -> FileEntry(it, FileType.SONG)
                            else -> null
                        }
                    }
                    .filter { it != null }
                val list = buildList {
                    if (path.parent != null) {
                        add(FileEntry(path.parent, FileType.PARENT))
                    }
                    addAll(stream.toList().filterNotNull())
                }
                stream.close()
                return@supplyAsync list
            }
    }

}

data class FileEntry(val path: Path, val type: FileType) {}
enum class FileType {
    SONG,
    DIRECTORY,
    PARENT
}