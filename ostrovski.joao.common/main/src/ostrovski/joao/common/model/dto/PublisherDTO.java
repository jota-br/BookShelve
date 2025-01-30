package ostrovski.joao.common.model.dto;

import java.time.LocalDateTime;

public record PublisherDTO (int publisherId, String publisherName, CountryDTO country,
                            UserDTO lastUpdatedBy, LocalDateTime lastUpdated) {
}
