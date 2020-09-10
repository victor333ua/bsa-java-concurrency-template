package bsa.java.concurrency.fs;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
// Если вам интересно, почему файловая система предоставляет асинхронный интерфейс, хотя зачастую мы используем блокирующие вызовы к файловой системе, то ответ весьма прост:
// этот интерфейс расчитан на то, что в будущем мы будем использовать CDN для хранения файлов и использовать асинхронный API для выполнения HTTP запросов при работе с ним.
// При работе с файловой системой вы можете использовать блокирующие вызовы, просто оберните результат в CompletableFuture
public interface FileSystem {

    CompletableFuture<Path> saveFile(String path, byte[] file);

    Path save(UUID uuid, byte[] file);
    String getServerPathToFile(Path path);
    void deleteFile(UUID uuid);
    void deleteAll();
    byte[] bytesFromBufImage(BufferedImage bi);
}
