package ostrovski.joao.db;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.db.helpers.GetSessionJPA;
import ostrovski.joao.db.helpers.SessionExecute;
import ostrovski.joao.db.model.jpa.ProfileJPA;
import ostrovski.joao.db.model.jpa.UserJPA;

import java.time.LocalDateTime;

public class UserRepository {

    public static UserJPA userRegistration(String login, String salt, String hash, int profileId) {

        Session session = null;
        Transaction transaction = null;
        try {
            session = GetSessionJPA.getSessionFactory().openSession();
            UserJPA user = getUserByLogin(session, login);
            if (user == null) {
                ProfileJPA profile = session.find(ProfileJPA.class, profileId); // 1=Admin, 2=Manager, 3 = User
                transaction = session.beginTransaction();
                user = SessionExecute.executeUpsert(session, new UserJPA(login, hash, salt, profile, true, LocalDateTime.now()));
                transaction.commit();
                return user;
            }
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

    public static UserJPA getUserByLogin(Session session, String login) {

        try {
            String jpql = "SELECT c FROM UserJPA c WHERE c.login = :login";
            TypedQuery<UserJPA> query = session.createQuery(jpql, UserJPA.class);
            query.setParameter("login", login);
            if (query.getSingleResult() != null) {
                return query.getSingleResult();
            }
        } catch (Exception e) {
            Logger.log(e);
        }
        return null;
    }
}
