package ostrovski.joao.db.model.jpa;

import jakarta.persistence.*;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;

import java.time.LocalDateTime;

@Entity
@Table(name = "countries")
public class CountryJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "countryId")
    private int countryId;

    @Column(name = "countryName", nullable = false, unique = true)
    private String countryName;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "lastUpdatedBy", referencedColumnName = "userId")
    private UserJPA lastUpdatedBy;

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

    public CountryJPA() {
    }

    public CountryJPA(String countryName, UserJPA lastUpdatedBy) {
        this.countryName = countryName;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public UserJPA getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        return "CountryJPA{" +
                "countryId=" + countryId +
                ", countryName='" + countryName + '\'' +
                ", lastUpdatedBy=" + lastUpdatedBy.getLogin() +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    public CountryJPA updateCountryJPA(CountryJPA newCountry) {

        try {
            if (newCountry == null) {
                Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
            } else {
                if (newCountry.getCountryName() == null || newCountry.getLastUpdatedBy() == null) {
                    Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
                }
            }

            this.countryName = newCountry.getCountryName();
            this.lastUpdatedBy = newCountry.getLastUpdatedBy();
            this.lastUpdated = LocalDateTime.now();
            return this;
        } catch (IllegalArgumentException e) {
            Logger.log(e);
            return null;
        }
    }
}
