package ostrovski.joao.db.model.jpa;

import jakarta.persistence.*;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "salt", nullable = false)
    private String salt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId", referencedColumnName = "profileId", nullable = true)
    private ProfileJPA profile;

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "lastUpdated")
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    public UserJPA() {
    }

    public UserJPA(String login, String hash, String salt, ProfileJPA profile, boolean isActive, LocalDateTime lastUpdated) {
        this.login = login;
        this.hash = hash;
        this.salt = salt;
        this.profile = profile;
        this.isActive = isActive;
        this.lastUpdated = lastUpdated;
    }

    public int getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public ProfileJPA getProfile() {
        return profile;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        return "UserJPA{" +
                "userId=" + userId +
                ", login='" + login + '\'' +
                ", hash='" + hash + '\'' +
                ", salt='" + salt + '\'' +
                ", profile=" + profile.getProfileName() +
                ", isActive=" + isActive +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    public UserJPA updateUserJPA(UserJPA newUser) {

        try {
            if (newUser == null) {
                Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
            } else {
                if (newUser.getLogin() == null || newUser.getHash() == null || newUser.getSalt() == null || newUser.getProfile() == null) {
                    Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
                }
            }

            this.login = newUser.getLogin();
            this.hash = newUser.getHash();
            this.salt = newUser.getSalt();
            this.profile = newUser.getProfile();
            this.isActive = newUser.isActive();
            this.lastUpdated = LocalDateTime.now();
            return this;
        } catch (IllegalArgumentException e) {
            Logger.log(e);
            return null;
        }
    }
}
