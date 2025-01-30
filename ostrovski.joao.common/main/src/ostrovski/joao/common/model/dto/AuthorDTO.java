package ostrovski.joao.common.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AuthorDTO (int authorId, String authorName, LocalDate dateOfBirth,
                         CountryDTO country, UserDTO lastUpdatedBy, LocalDateTime lastUpdated) {
}
