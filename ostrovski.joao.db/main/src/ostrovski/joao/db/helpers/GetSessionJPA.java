package ostrovski.joao.db.helpers;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;

public class GetSessionJPA {

    public static SessionFactory getSessionFactory() {
      EntityManagerFactory emf = Persistence.createEntityManagerFactory("ostrovski.joao.db");
      SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
      return sessionFactory;
    }
}
