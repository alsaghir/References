package server.infrastructure.db.postgres;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class V20221203__Base_tables extends BaseJavaMigration {

    /**
     * @param context The context relevant for this migration, containing things like the JDBC connection to use and the
     *                current Flyway configuration.
     * @throws SQLException for any sql error
     */
    @Override
    public void migrate(Context context) throws SQLException {

        Connection connection = context.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("""
                CREATE TABLE APP_VOTES
                   (
                       ID    INT PRIMARY KEY NOT NULL,
                       YES NUMERIC            NOT NULL,
                       NO  NUMERIC            NOT NULL
                   );
                 """)) {

            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement("""
                -- Sequence Tables
                  CREATE TABLE APP_SEQ_GENERATOR
                  (
                      SEQ_NAME  TEXT NOT NULL,
                      SEQ_VALUE BIGINT      NOT NULL
                  );
                """)) {

            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement("""
                -- Must have data
                INSERT INTO APP_SEQ_GENERATOR
                VALUES ('VOTES_SEQ_PK', 1);
                """)) {

            statement.execute();
        }
    }

}
