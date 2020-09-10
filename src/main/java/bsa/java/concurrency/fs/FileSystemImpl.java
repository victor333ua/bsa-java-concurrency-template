package bsa.java.concurrency.fs;

import bsa.java.concurrency.exceptions.MyIoException;
import bsa.java.concurrency.imageProcessing.ImageProcessingScalesImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class FileSystemImpl implements FileSystem {
    private final Path path2Images;
    private final String imagesPath;

    public FileSystemImpl(@Value("${imagesPath}") String path) {
        imagesPath = path;
        path2Images = Paths.get(imagesPath);
        if (!Files.exists(path2Images)) {
            try {
                Files.createDirectories(path2Images);
            } catch (IOException e) {
                throw new MyIoException(e.getMessage());
            }
        }
    }

    @Override
    public CompletableFuture<Path> saveFile(String path, byte[] file) {
        return null;
    }

    public Path save(UUID uuid, byte[] file) {
        try {
            Path pathFile = getFilePath(uuid);
            Files.createFile(pathFile);
            Files.write(pathFile, file);
            return pathFile;
        } catch (Exception e) {
            throw new MyIoException(e.getMessage());
        }
    }

    public void deleteFile(UUID uuid) {
        try {
            Files.deleteIfExists(getFilePath(uuid));
        } catch (IOException e) {
            throw new MyIoException(e.getMessage());
        }
    }

    public void deleteAll() {
        var dir = new File(path2Images.toString());
        var arr = dir.listFiles();
        if(arr == null || arr.length == 0) return;

        Arrays.asList(arr).forEach(File::delete);

    }

    public String getServerPathToFile(Path path) {
        return "http://localhost:8080/" + path.toString();
    }

    private Path getFilePath(UUID uuid) {
        return Paths.get(path2Images.toString(), uuid + ".jpg");
    }

    public byte[] bytesFromBufImage(BufferedImage bi) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;

        } catch (Exception e) {
            throw new MyIoException(e.getMessage());
        }
    }
}
