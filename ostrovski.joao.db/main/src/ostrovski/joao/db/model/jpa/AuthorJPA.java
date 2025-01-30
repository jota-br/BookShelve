package ostrovski.joao.db.model.jpa;

import jakarta.persistence.*;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

@Entity
@Table(name = "authors")
public class AuthorJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authorId")
    private int authorId;

    @Column(name = "authorName")
    private String authorName;

    @Column(name = "dateOfBirth")
    private LocalDate dateOfBirth;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId", referencedColumnName = "countryId")
    private CountryJPA country;

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

    public AuthorJPA() {
    }

    public AuthorJPA(String authorName, LocalDate dateOfBirth, CountryJPA country, UserJPA lastUpdatedBy) {
        this.authorName = authorName;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
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
        return "Author{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", country=" + country.getCountryName() +
                ", lastUpdatedBy=" + lastUpdatedBy.getLogin() +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    public AuthorJPA updateAuthorJPA(AuthorJPA newAuthor) {

        try {
        if (newAuthor == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
        } else {
            if (newAuthor.getAuthorName() == null || newAuthor.getLastUpdatedBy() == null) {
                Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
            } else if (newAuthor.getDateOfBirth() != null) {
                if (newAuthor.getDateOfBirth().isAfter(ChronoLocalDate.from(LocalDate.now()))) {
                    Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
                }
            }
        }

        this.authorName = newAuthor.getAuthorName();
        this.dateOfBirth = newAuthor.getDateOfBirth();
        this.country = newAuthor.getCountry();
        this.lastUpdatedBy = newAuthor.getLastUpdatedBy();
        this.lastUpdated = LocalDateTime.now();
        return this;
        } catch (IllegalArgumentException e) {
            Logger.log(e);
            return null;
        }
    }
}
