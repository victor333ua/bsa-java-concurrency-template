package bsa.java.concurrency.image;

import bsa.java.concurrency.exceptions.MyIoException;
import bsa.java.concurrency.fs.FileSystem;
import bsa.java.concurrency.fs.FileSystemImpl;
import bsa.java.concurrency.image.dto.SearchResultDTO;
import bsa.java.concurrency.image.dto.SearchResultDtoImpl;
import bsa.java.concurrency.imageProcessing.ImageProcessing;
import bsa.java.concurrency.imageProcessing.ImageProcessingScalesImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ImageService {
    private final ImageProcessing ip;
    private final FileSystem fs;
    private final ImageRepository iRepo;
    private final List<ImagePersist> persist = new LinkedList<>();

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    public ImageService(FileSystemImpl fs, ImageProcessingScalesImage ip, ImageRepository ir) {
        this.ip = ip;
        this.fs = fs;
        this.iRepo = ir;
    }

    public void saveAll(MultipartFile[] files) {
            Arrays.asList(files)
                    .parallelStream()
                    .forEach(this::saveOne);
    }

    public void saveOne(MultipartFile file) {
        try {
            var bytes = file.getBytes();

            CompletableFuture<Long> hashFuture =
                    CompletableFuture.supplyAsync(() -> ip.calculateDHash(bytes));

            var id = UUID.randomUUID();
            CompletableFuture<Path> saveFileFuture =
                    CompletableFuture.supplyAsync(() -> fs.save(id, bytes));

            long hash = hashFuture.get(1000, TimeUnit.MILLISECONDS);
            Path pathTo = saveFileFuture.get(100, TimeUnit.MILLISECONDS);

            var image = new ImagePersist(id, hash, pathTo.toString());

            if(activeProfile.equals("db")) iRepo.save(image);
            else persist.add(image);

        } catch (IOException e) {
            throw new MyIoException(e.getMessage());
        } catch (ExecutionException e) {
            throw new MyIoException("execution exception in async");
        } catch (TimeoutException e) {
            throw new MyIoException("timeout exception: ");
        } catch (InterruptedException e) {
            throw new MyIoException("interrupted exception");
        }
    }
        public SearchResultDTO checkMatching(MultipartFile file, double threshold) {
        try {
            var bytes = file.getBytes();
            long hash = ip.calculateDHash(bytes);
            double match;

            if(activeProfile.equals("db")) match = iRepo.getMatch(hash);
            else {
                if (persist.isEmpty()) match = 0;
                else {
                    match = persist.stream()
                            .map(imagePersist -> calculateHamming(hash, imagePersist.getHash()))
                            .max(Comparator.naturalOrder())
                            .get();
                }
            }

            if(match > threshold) return
                    SearchResultDtoImpl.builder()
                    .imageId(null)
                    .matchPercent(match)
                    .imageUrl("we have image like this")
                    .build();

            var id = UUID.randomUUID();
            Path pathTo = fs.save(id, bytes);

            var image = new ImagePersist(id, hash, pathTo.toString());

            if(activeProfile.equals("db")) iRepo.save(image);
            else  persist.add(image);

            return SearchResultDtoImpl.builder()
                    .imageId(id)
                    .matchPercent(match)
                    .imageUrl(fs.getServerPathToFile(pathTo))
                    .build();

        } catch (IOException e) {
            throw new MyIoException(e.getMessage());
        }
    }
    @Transactional
    public void deleteOne(UUID uuid) {
        if(activeProfile.equals("db"))
            if(iRepo.findById(uuid).isPresent()) iRepo.deleteById(uuid);

        else persist.removeIf(ip -> ip.getId() == uuid);
        fs.deleteFile(uuid);
    }
    @Transactional
    public void deleteAll() {
        if(activeProfile.equals("db")) iRepo.deleteAll();
        else persist.clear();
        fs.deleteAll();
    }

    private double calculateHamming(long hash1, long hash2) {
        long diff = hash1 ^ hash2;
        int count = 0;
        while(diff != 0) {
            count += diff & 1;
            diff >>>= 1;
        }
        return 1 - count/64.;
    }
}