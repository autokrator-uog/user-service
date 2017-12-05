package uk.ac.gla.sed.clients.userservice.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface UserDAO {
    @SqlUpdate("CREATE TABLE users(username VARCHAR(128) PRIMARY KEY, passwordhash VARCHAR(512) NOT NULL);")
    void createUsersTable();
    
    @SqlUpdate("DROP TABLE IF EXISTS users CASCADE;")
    void deleteTableIfExists();

    @SqlUpdate("INSERT INTO users (username, passwordhash) values (:username, :password)")
  	void createUser(@Bind("username") String username, @Bind("password") String password);

    @SqlQuery("SELECT username FROM users WHERE username= :username")
    String getUserIfExistsElseNone(@Bind("username") String username);
}
