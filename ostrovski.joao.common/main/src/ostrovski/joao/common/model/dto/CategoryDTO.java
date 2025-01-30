package ostrovski.joao.common.model.dto;

import java.time.LocalDateTime;

public record CategoryDTO (int categoryId, String categoryName, UserDTO lastUpdatedBy,
                           LocalDateTime lastUpdated) {
}
