package ostrovski.joao.db.model.jpa;

import jakarta.persistence.*;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
public class ProfileJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profileId")
    private int profileId;

    @Column(name = "profileName", nullable = false, unique = true)
    private String profileName;

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

    public ProfileJPA() {
    }

    public int getProfileId() {
        return profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        return "ProfileJPA{" +
                "profileId=" + profileId +
                ", profileName='" + profileName + '\'' +
                ", isActive=" + isActive +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    public ProfileJPA updateProfileJPA(ProfileJPA newProfile) {

        try {
            if (newProfile == null) {
                Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
            } else {
                if (newProfile.getProfileName() == null) {
                    Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
                }
            }

            this.profileName = newProfile.getProfileName();
            this.isActive = newProfile.isActive();
            this.lastUpdated = LocalDateTime.now();
            return this;
        } catch (IllegalArgumentException e) {
            Logger.log(e);
            return null;
        }
    }
}
