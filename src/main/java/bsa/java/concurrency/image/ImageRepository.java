package bsa.java.concurrency.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImagePersist, UUID> {

    @Query(value = "select * from matcher(:hash)", nativeQuery = true)
    double getMatch(long hash);
}
