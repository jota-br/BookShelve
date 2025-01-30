package ostrovski.joao.db.helpers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.model.dto.BookDTO;
import ostrovski.joao.db.model.jpa.BookJPA;

import java.util.Map;

public class PaginatedQuery {

    public static ObservableList<BookDTO> query(int page, int size, String regex, String bookInstanceVariable, String requestedEntityQueryColumn) {

        Transaction transaction = null;
        Session session = null;
        try {
            session = GetSessionJPA.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String query = getQueryString("SELECT book FROM BookJPA book JOIN book.bookInstanceVariable e WHERE e.requestedEntityQueryColumn LIKE :query ORDER BY title", bookInstanceVariable, requestedEntityQueryColumn);

            Query<BookJPA> result = session.createQuery(query, BookJPA.class);
            result.setParameter("query", "%" + regex + "%");
            result.setFirstResult((page - 1) * size);
            result.setMaxResults(size);
            transaction.commit();

            ObservableList<BookDTO> oList = FXCollections.observableArrayList();
            oList.addAll(result.list().stream()
                    .map(TransformToDTO::transformToBookDTO)
                    .toList());
            return oList;
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

    private static String getQueryString(String queryString, String bookInstanceVariable, String requestedEntityQueryColumn) {
        String query = queryString;
        if (requestedEntityQueryColumn.equals("title") && bookInstanceVariable.isEmpty()) {
            query = query.replace("JOIN book.bookInstanceVariable e WHERE e.requestedEntityQueryColumn", "WHERE book.requestedEntityQueryColumn");
        } else {
            query = query.replace("bookInstanceVariable", bookInstanceVariable);
        }
        query = query.replace("requestedEntityQueryColumn", requestedEntityQueryColumn);
        return query;
    }

    public static Map<String, Integer> queryCount(Session session, int size, String regex, String bookInstanceVariable, String requestedEntityQueryColumn) {
        String query = getQueryCountString("SELECT COUNT(book) FROM BookJPA book JOIN book.bookInstanceVariable e WHERE e.requestedEntityQueryColumn LIKE :query", bookInstanceVariable, requestedEntityQueryColumn);

        Query<Long> result = session.createQuery(query, Long.class);
        result.setParameter("query", "%" + regex + "%");
        Long totalCount = result.getSingleResult();
        return Map.of("totalRecords", (int) ((long) totalCount), "totalPages", (int) Math.ceil((double) totalCount / size));
    }

    private static String getQueryCountString(String queryString, String bookInstanceVariable, String requestedEntityQueryColumn) {
        String query = queryString;
        if (requestedEntityQueryColumn.equals("title") && bookInstanceVariable.isEmpty()) {
            query = query.replace("JOIN book.bookInstanceVariable e WHERE e.requestedEntityQueryColumn", "WHERE book.requestedEntityQueryColumn");
        } else {
            query = query.replace("bookInstanceVariable", bookInstanceVariable);
        }
        query = query.replace("requestedEntityQueryColumn", requestedEntityQueryColumn);
        return query;
    }
}
