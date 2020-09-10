package bsa.java.concurrency.image.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class SearchResultDtoImpl implements SearchResultDTO {
    private final UUID imageId;
    private final Double matchPercent;
    private final String imageUrl;

}
