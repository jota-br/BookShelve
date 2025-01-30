package ostrovski.joao.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ostrovski.joao.common.helpers.DateChecker;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.helpers.ToUpperCase;
import ostrovski.joao.common.model.dto.BookDTO;
import ostrovski.joao.db.helpers.GetSessionJPA;
import ostrovski.joao.db.helpers.SessionExecute;
import ostrovski.joao.db.helpers.TransformToDTO;
import ostrovski.joao.db.model.jpa.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    public static BookJPA processUpsertData(Session session, String title, List<AuthorJPA> authors, String releaseDate, String description,
                                            List<CategoryJPA> categories, List<PublisherJPA> publishers, BookDTO oldBook) {

        if (title.isEmpty()) {
            Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
            return null;
        }

        String titleClean = ToUpperCase.firstLetters(title.trim());
        String releaseDateClean = releaseDate.trim();
        String descriptionClean = description.trim();

        if (!DateChecker.dateChecker(releaseDateClean)) {
            releaseDateClean = null;
        }

        if (authors == null) {
            authors = new ArrayList<>();
        }

        if (categories == null) {
            categories = new ArrayList<>();
        }

        if (publishers == null) {
            publishers = new ArrayList<>();
        }

        UserJPA user = session.find(UserJPA.class, UserSession.getInstance().getUserInSessionId());

        return BookService.updateOrInsert(session, titleClean, authors, releaseDateClean, descriptionClean, user, categories, publishers, oldBook);
    }

    public static BookJPA updateOrInsert(Session session, String title, List<AuthorJPA> authors, String releaseDate, String description,
                                         UserJPA user, List<CategoryJPA> categories, List<PublisherJPA> publishers, BookDTO oldBook) {
        BookJPA newBook;
        LocalDate releaseDateParsed = null;
        if (releaseDate != null && !releaseDate.isEmpty()) {
            releaseDateParsed = LocalDate.parse(releaseDate);
        }

        List<AuthorJPA> foundAuthor = new ArrayList<>();
        List<PublisherJPA> foundPublishers = new ArrayList<>();
        List<CategoryJPA> foundCategories = new ArrayList<>();

        for (AuthorJPA author : authors) {
            AuthorJPA findAuthor = SessionExecute.executeFind(session, author.getAuthorName(), AuthorJPA.class, "authorName");
            if (findAuthor != null) {
                foundAuthor.add(findAuthor);
            }
        }

        for (CategoryJPA category : categories) {
            CategoryJPA findCategory = SessionExecute.executeFind(session, category.getCategoryName(), CategoryJPA.class, "categoryName");
            if (findCategory != null) {
                foundCategories.add(findCategory);
            }
        }

        for (PublisherJPA publisher : publishers) {
            PublisherJPA findPublisher = SessionExecute.executeFind(session, publisher.getPublisherName(), PublisherJPA.class, "publisherName");
            if (findPublisher != null) {
                foundPublishers.add(findPublisher);
            }
        }

        if (oldBook != null) {
            BookJPA bookToUpdate = SessionExecute.executeFindById(session, BookJPA.class, oldBook.bookId());

            newBook = bookToUpdate.updateBookJPA(
                    new BookJPA(title, foundAuthor, releaseDateParsed, description, user, foundCategories, foundPublishers)
            );
        } else {
            newBook = SessionExecute.executeUpsert(session,
                    new BookJPA(title, foundAuthor, releaseDateParsed, description, user, foundCategories, foundPublishers)
            );
        }
        return newBook;
    }

    public static void executeDelete(BookDTO book) {

        Transaction transaction = null;
        Session session = null;
        try {
            session = GetSessionJPA.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            BookJPA toDelete = SessionExecute.executeFindById(session, BookJPA.class, book.bookId());
            boolean deleted = SessionExecute.executeDelete(session, toDelete);
            transaction.commit();
        } catch (Exception e) {
            Logger.log(e);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public static BookDTO findBookByID(int id) {

        Session session = null;
        try {
            session = GetSessionJPA.getSessionFactory().openSession();
            BookJPA book = SessionExecute.executeFindById(session, BookJPA.class, id);
            return TransformToDTO.transformToBookDTO(book);
        } catch (Exception e) {
            Logger.log(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return null;
    }
}
