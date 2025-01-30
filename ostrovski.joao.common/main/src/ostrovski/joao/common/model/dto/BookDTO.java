package ostrovski.joao.common.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BookDTO (int bookId, String title, List<AuthorDTO> authors, LocalDate releaseDate,
                       String description, UserDTO lastUpdatedBy, LocalDateTime lastUpdated,
                       List<CategoryDTO> categories, List<PublisherDTO> publishers) {
}
