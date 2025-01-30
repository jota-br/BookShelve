package ostrovski.joao.common.model.dto;

import java.time.LocalDateTime;

public record ProfileDTO (int profileId, String profileName,
                          boolean isActive, LocalDateTime lastUpdated) {
}
