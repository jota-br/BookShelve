package ostrovski.joao.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.model.dto.AuthorDTO;
import ostrovski.joao.common.model.dto.CategoryDTO;
import ostrovski.joao.common.model.dto.PublisherDTO;
import ostrovski.joao.db.helpers.GetSessionJPA;
import ostrovski.joao.db.helpers.TransformToDTO;
import ostrovski.joao.db.model.jpa.AuthorJPA;
import ostrovski.joao.db.model.jpa.CategoryJPA;
import ostrovski.joao.db.model.jpa.PublisherJPA;

import java.util.List;

public class EditDialogService {

    public static AuthorDTO processAuthorsEditDialogData(int id, String authorsNames, String authorsDatesOfBirth, String authorsCountries) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = GetSessionJPA.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            List<AuthorJPA> authors = AuthorService.processUpsertData(session, authorsNames, authorsDatesOfBirth, authorsCountries, id);

            if (authors.getFirst() == null) {
                transaction.rollback();
                return null;
            }

            transaction.commit();

            return TransformToDTO.transformToAuthorDTO(authors.getFirst());
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

    public static PublisherDTO processPublishersEditDialogData(int id, String publishersNames, String publishersCountries) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = GetSessionJPA.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            List<PublisherJPA> publishers = PublisherService.processUpsertData(session, publishersNames, publishersCountries, id);

            if (publishers.getFirst() == null) {
                transaction.rollback();
                return null;
            }

            transaction.commit();

            return TransformToDTO.transformToPublisherDTO(publishers.getFirst());
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

    public static CategoryDTO processCategoriesEditDialogData(int id, String categoriesString) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = GetSessionJPA.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            List<CategoryJPA> categories = CategoryService.processUpsertData(session, categoriesString, id);

            if (categories == null || categories.getFirst() == null) {
                transaction.rollback();
                return null;
            }

            transaction.commit();

            return TransformToDTO.transformToCategoryDTO(categories.getFirst());
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
}