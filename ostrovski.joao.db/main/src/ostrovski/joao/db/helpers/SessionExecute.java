package ostrovski.joao.db.helpers;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import ostrovski.joao.common.helpers.Logger;

public class SessionExecute {

    public static <T> T executeFindById(Session session, Class<T> entityClass, int id) {

        return session.find(entityClass, id);
    }

    public static <T> T executeFind(Session session, String regex, Class<T> entityClass, String comparingField) {

        var ec = entityClass.getSimpleName();
        String jpql = "SELECT c FROM " + ec + " c WHERE c.%s = ?1".formatted(comparingField);
        TypedQuery<T> query = session.createQuery(jpql, entityClass);
        query.setParameter(1, regex);
        if (!query.getResultList().isEmpty()) {
            return query.getResultList().getFirst();
        }
        return null;
    }

    public static <T> T executeUpsert(Session session, T classObj) {

        try {
            session.merge(classObj);
            return classObj;
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    public static <T> boolean executeDelete(Session session, T classObj) {

        try {
            session.remove(classObj);
            return true;
        } catch (Exception e) {
            Logger.log(e);
            return false;
        }
    }
}
