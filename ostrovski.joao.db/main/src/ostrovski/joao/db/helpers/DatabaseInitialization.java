package ostrovski.joao.db.helpers;

import ostrovski.joao.common.helpers.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseInitialization {

    public static final String SCHEMA = "bookShelve";
//    public static final Map<String, String> ERROR_CODES = new HashMap<>();

    public static void checkSchema() {

        try (Connection con = JdbcDataSource.getDataSource().getConnection(); Statement statement = con.createStatement()) {
            statement.execute("USE " + SCHEMA);
            createTables(con);
        } catch (SQLException e) {
            System.err.printf("SQL State: %s%nError Code: %s%nMessage: %s%n", e.getSQLState(), e.getErrorCode(), e.getMessage());

            try (Connection con = JdbcDataSource.getDataSource().getConnection(); Statement statement = con.createStatement()) {
                statement.execute("CREATE SCHEMA IF NOT EXISTS `" + SCHEMA + "`");
                statement.execute("USE " + SCHEMA);
                createTables(con);
                insertSampleData(con);
            } catch (SQLException ex) {
                System.err.printf("SQL State: %s%nError Code: %s%nMessage: %s%n", e.getSQLState(), e.getErrorCode(), e.getMessage());
            }
        } catch (NullPointerException e) {
            Logger.log(e);
        }
    }

    public static void executeStatement(Connection con, String toExecute) {

        try (Statement statement = con.createStatement(); PreparedStatement ps = statement.getConnection().prepareStatement(toExecute)) {
            ps.execute();
        } catch (SQLException e) {
            System.err.printf("SQL State: %s%nError Code: %s%nMessage: %s%n", e.getSQLState(), e.getErrorCode(), e.getMessage());
        }
    }

    public static void createTables(Connection con) {

        Path path = Path.of("ostrovski.joao.db/main/resources/ostrovski/joao/db/dbinit.sql");
        try {
            String data = Files.readString(path);
            Pattern pattern = Pattern.compile("(?s)CREATE TABLE IF NOT EXISTS\\s+(\\w+)\\s*\\((.+?)\\);");
            Matcher matcher = pattern.matcher(data);

            while (matcher.find()) {
                String toExecute = "CREATE TABLE IF NOT EXISTS " + matcher.group(1) + "(" + matcher.group(2) + ");";
                executeStatement(con, toExecute);

            }
        } catch (IOException e) {
            Logger.log(e);
        }
    }

    public static void insertSampleData(Connection con) {

        Path path = Path.of("ostrovski.joao.db/main/resources/ostrovski/joao/db/sampleData.sql");

        try (var file = Files.lines(path)) {
            file
                    .filter(s -> !s.isEmpty())
                    .forEach(s -> executeStatement(con, s));
        } catch (IOException e) {
            Logger.log(e);
        }
    }
}
