package ostrovski.joao.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.model.dto.BookDTO;
import ostrovski.joao.db.helpers.GetSessionJPA;
import ostrovski.joao.db.helpers.TransformToDTO;
import ostrovski.joao.db.model.jpa.AuthorJPA;
import ostrovski.joao.db.model.jpa.BookJPA;
import ostrovski.joao.db.model.jpa.CategoryJPA;
import ostrovski.joao.db.model.jpa.PublisherJPA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpsertService {

    public static synchronized BookDTO processUpsertData(String title, String releaseDate, String description, String categoriesString,
                                            List<String> publisherList, List<String> authorList,
                                            BookDTO oldBook) {

        List<String> authorCleanList = processAuthorData(authorList);
        List<String> publisherCleanList = processPublisherData(publisherList);

        Transaction transaction = null;
        Session session = null;
        try {
            session = GetSessionJPA.getSessionFactory().openSession();

            transaction = session.beginTransaction();
            List<CategoryJPA> categories = CategoryService.processUpsertData(session, categoriesString);
            transaction.commit();

            transaction = session.beginTransaction();
            List<PublisherJPA> publishers = PublisherService.processUpsertData(session, publisherCleanList.get(0), publisherCleanList.get(1));
            transaction.commit();

            transaction = session.beginTransaction();
            List<AuthorJPA> authors = AuthorService.processUpsertData(session, authorCleanList.get(0), authorCleanList.get(1), authorCleanList.get(2));
            transaction.commit();

            transaction = session.beginTransaction();
            BookJPA book = BookService.processUpsertData(session, title, authors, releaseDate, description, categories, publishers, oldBook);
            transaction.commit();

            if (book == null) {
                transaction.rollback();
                return null;
            }

            return TransformToDTO.transformToBookDTO(book);
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
        return null;
    }

    private static List<String> processAuthorData(List<String> authorList) {
        List<String> authorNameList = new ArrayList<>();
        List<String> authorDobList = new ArrayList<>();
        List<String> authorCountryList = new ArrayList<>();

        for (String authorData : authorList) {

            if (!authorData.isEmpty()) {
                String cleanData = authorData.replaceAll("[\\[\\]]", "");
                List<String> splitData = Arrays.stream(cleanData.split(","))
                        .filter(s -> !s.isEmpty())
                        .map(String::trim)
                        .toList();
                authorNameList.add(splitData.get(0));
                authorDobList.add(splitData.get(1));
                authorCountryList.add(splitData.get(2));
            }
        }

        String authorsNames = String.join(",", authorNameList);
        String authorsDatesOfBirth = String.join(",", authorDobList);
        String authorsCountries = String.join(",", authorCountryList);

        return List.of(authorsNames, authorsDatesOfBirth, authorsCountries);
    }

    private static List<String> processPublisherData(List<String> publisherList) {

        List<String> publisherNameList = new ArrayList<>();
        List<String> publisherCountryList = new ArrayList<>();

        for (String publisherData : publisherList) {

            if (!publisherData.isEmpty()) {
                String cleanData = publisherData.replaceAll("[\\[\\]]", "");
                List<String> splitData = Arrays.stream(cleanData.split(","))
                        .filter(s -> !s.isEmpty())
                        .map(String::trim)
                        .toList();
                publisherNameList.add(splitData.get(0));
                publisherCountryList.add(splitData.get(1));
            }
        }

        String publishersNames = String.join(",", publisherNameList);
        String publishersCountries = String.join(",", publisherCountryList);

        return List.of(publishersNames, publishersCountries);
    }
}
