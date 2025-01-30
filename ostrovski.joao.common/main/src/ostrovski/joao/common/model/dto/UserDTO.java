package ostrovski.joao.common.model.dto;

import java.time.LocalDateTime;

public record UserDTO (int userId, String login, String hash, String salt, ProfileDTO profile,
                       boolean isActive, LocalDateTime lastUpdated) {
}
