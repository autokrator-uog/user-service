package uk.ac.gla.sed.clients.userservice.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface UserAccountDAO {
	@SqlUpdate("CREATE TABLE user_accounts (username String, id int PRIMARY KEY)")
    void createUsersAccountTable();
	
    @SqlUpdate("DROP TABLE IF EXISTS users_accounts")
    void deleteTableIfExists();

    @SqlUpdate("INSERT INTO user_accounts (username, id) values (:username, :id)")
  	void createUserAccount(@Bind("username") String username, @Bind("id") int id);
    
    @SqlQuery("SELECT username FROM user_accounts WHERE id=:id")
    int getUsername(@Bind("id") int id);
    
    @SqlQuery("SELECT id FROM user_accounts WHERE username=:username")
    int getID(@Bind("username") String username);
    


}
