package ostrovski.joao.common.model.dto;

import java.time.LocalDateTime;

public record CountryDTO (int countryId, String countryName, UserDTO lastUpdatedBy,
                          LocalDateTime lastUpdated) {
}
