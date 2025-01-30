package ostrovski.joao.db.model.jpa;

import jakarta.persistence.*;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
public class CategoryJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private int categoryId;

    @Column(name = "categoryName")
    private String categoryName;

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

    public CategoryJPA() {
    }

    public CategoryJPA(String categoryName, UserJPA lastUpdatedBy) {
        this.categoryName = categoryName;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public UserJPA getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", lastUpdatedBy=" + lastUpdatedBy.getLogin() +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    public CategoryJPA updateCategoryJPA(CategoryJPA newCategory) {

        try {
            if (newCategory == null) {
                Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
            } else {
                if (newCategory.getCategoryName() == null || newCategory.getLastUpdatedBy() == null) {
                    Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
                }
            }

            this.categoryName = newCategory.getCategoryName();
            this.lastUpdatedBy = newCategory.getLastUpdatedBy();
            this.lastUpdated = LocalDateTime.now();
            return this;
        } catch (IllegalArgumentException e) {
            Logger.log(e);
            return null;
        }
    }
}
