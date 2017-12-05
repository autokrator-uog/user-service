package uk.ac.gla.sed.clients.userservice.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface UserDAO {
    @SqlUpdate("CREATE TABLE users")
    void createUsersTable();
    
    @SqlUpdate("DROP TABLE IF EXISTS users")
    void deleteTableIfExists();

    @SqlUpdate("INSERT INTO users (username, password) values (:username, :password)")
  	void createUser(@Bind("username") String username, @Bind("password") String password);

}
