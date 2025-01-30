module ostrovski.joao.ui {
    requires javafx.fxml;
    requires javafx.web;
    requires ostrovski.joao.db;
    requires BookShelve; requires ostrovski.joao.services;
    requires org.hibernate.orm.core;
    requires net.bytebuddy;

    opens ostrovski.joao.ui to javafx.graphics, javafx.fxml;
    exports ostrovski.joao.ui;
}