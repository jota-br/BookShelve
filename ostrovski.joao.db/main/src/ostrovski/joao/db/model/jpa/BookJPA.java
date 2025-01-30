package ostrovski.joao.db.model.jpa;

import jakarta.persistence.*;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class BookJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookId")
    private int bookId;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "bookId"),
            inverseJoinColumns = @JoinColumn(name = "authorId")
    )
    private List<AuthorJPA> authors = new ArrayList<>();

    @Column(name = "releaseDate")
    private LocalDate releaseDate;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "lastUpdatedBy", referencedColumnName = "userId", nullable = true)
    private UserJPA lastUpdatedBy;

    @Column(name = "lastUpdated")
    private LocalDateTime lastUpdated;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "bookId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId")
    )
    private List<CategoryJPA> categories = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_publisher",
            joinColumns = @JoinColumn(name = "bookId"),
            inverseJoinColumns = @JoinColumn(name = "publisherId")
    )
    private List<PublisherJPA> publishers;

    @PrePersist
    protected void onCreate() {
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    public BookJPA() {
    }

    public BookJPA(String title, List<AuthorJPA> authors, LocalDate releaseDate, String description, UserJPA lastUpdatedBy, List<CategoryJPA> categories, List<PublisherJPA> publishers) {
        this.title = title;
        this.authors = authors;
        this.releaseDate = releaseDate;
        this.description = description;
        this.lastUpdatedBy = lastUpdatedBy;
        this.categories = categories;
        this.publishers = publishers;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public UserJPA getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public List<PublisherJPA> getPublishers() {
        return publishers;
    }

    public List<CategoryJPA> getCategories() {
        return categories;
    }
    public List<AuthorJPA> getAuthors() {
        return authors;
    }

    @Override
    public String toString() {
        return "Books{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", releaseDate=" + releaseDate +
                ", description='" + description + '\'' +
                ", lastUpdatedBy=" + lastUpdatedBy.getLogin() +
                ", lastUpdated=" + lastUpdated +
                ", categories=" + categories +
                ", publishers=" + publishers +
                '}';
    }

    public BookJPA updateBookJPA(BookJPA newBook) {

        try {
            if (newBook == null) {
                Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
            } else {
                if (newBook.getTitle() == null || newBook.getLastUpdatedBy() == null) {
                    Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
                } else if (newBook.getReleaseDate() != null) {
                    if (newBook.getReleaseDate().isAfter(ChronoLocalDate.from(LocalDate.now()))) {
                        Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
                    }
                }
            }

            this.title = newBook.getTitle();
            this.authors = newBook.getAuthors();
            this.releaseDate = newBook.getReleaseDate();
            this.description = newBook.getDescription();
            this.lastUpdatedBy = newBook.getLastUpdatedBy();
            this.lastUpdated = LocalDateTime.now();
            this.categories = newBook.getCategories();
            this.publishers = newBook.getPublishers();
            return this;
        } catch (IllegalArgumentException e) {
            Logger.log(e);
            return null;
        }
    }
}
