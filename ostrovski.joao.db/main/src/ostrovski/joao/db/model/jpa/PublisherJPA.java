package ostrovski.joao.db.model.jpa;

import jakarta.persistence.*;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;

import java.time.LocalDateTime;

@Entity
@Table(name = "publishers")
public class PublisherJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisherId")
    private int publisherId;

    @Column(name = "publisherName", nullable = false, unique = true)
    private String publisherName;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId", referencedColumnName = "countryId", nullable = true)
    private CountryJPA country;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "lastUpdatedBy", referencedColumnName = "userId", nullable = true)
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

    public PublisherJPA() {
    }

    public PublisherJPA(String publisherName, CountryJPA country, UserJPA lastUpdatedBy) {
        this.publisherName = publisherName;
        this.country = country;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public CountryJPA getCountry() {
        return country;
    }

    public UserJPA getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        return "PublisherJPA{" +
                "publisherId=" + publisherId +
                ", publisherName='" + publisherName + '\'' +
                ", country=" + country.getCountryName() +
                ", lastUpdatedBy=" + lastUpdatedBy.getLogin() +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    public PublisherJPA updatePublisherJPA(PublisherJPA newPublisher) {

        try {
            if (newPublisher == null) {
                Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
            } else {
                if (newPublisher.getPublisherName() == null || newPublisher.getLastUpdatedBy() == null) {
                    Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
                }
            }

            this.publisherName = newPublisher.getPublisherName();
            this.country = newPublisher.getCountry();
            this.lastUpdatedBy = newPublisher.getLastUpdatedBy();
            this.lastUpdated = LocalDateTime.now();
            return this;
        } catch (IllegalArgumentException e) {
            Logger.log(e);
            return null;
        }
    }
}
