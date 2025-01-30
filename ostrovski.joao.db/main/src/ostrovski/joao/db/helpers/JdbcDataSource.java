package ostrovski.joao.db.helpers;

import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class JdbcDataSource {

    public static DataSource getDataSource() {

        var dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/");
            dataSource.setUser(System.getenv("SQL_USER"));
            dataSource.setPassword(System.getenv("SQL_PASS"));
        } catch (SQLException e) {
            System.err.printf("SQL State: %s%nError Code: %s%nMessage: %s%n", e.getSQLState(), e.getErrorCode(), e.getMessage());
            return null;
        }
        return dataSource;
    }
}
