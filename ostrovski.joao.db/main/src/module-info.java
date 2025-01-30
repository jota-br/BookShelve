module ostrovski.joao.db {
    requires org.mariadb.jdbc;
    requires jdk.compiler;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires javafx.base; requires BookShelve;

    exports ostrovski.joao.db;
    exports ostrovski.joao.db.helpers;
    exports ostrovski.joao.db.model.jpa;
    opens ostrovski.joao.db.model.jpa to org.hibernate.orm.core;
}