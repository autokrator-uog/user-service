package uk.ac.gla.sed.clients.userservice.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;



public interface UserDAO {
    @SqlUpdate("CREATE users TABLE")
    void createUsersTable();

    @SqlUpdate("DELETE users TABLE IF EXISTS")
    void deleteTableIfExists();

    @SqlUpdate("INSERT INTO users (id) VALUES (:id)")
    void createUser(@Bind("id") int userId);
}
