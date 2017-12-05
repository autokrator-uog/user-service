package uk.ac.gla.sed.clients.userservice.jdbi;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface UserAccountDAO {
	@SqlUpdate("CREATE TABLE user_accounts (" +
            "username VARCHAR(128) NOT NULL REFERENCES users(username) ON DELETE CASCADE ON UPDATE CASCADE, " +
            "accountid int PRIMARY KEY NOT NULL" +
            ");")
    void createUsersAccountTable();
	
    @SqlUpdate("DROP TABLE IF EXISTS user_accounts;")
    void deleteTableIfExists();

    @SqlUpdate("INSERT INTO user_accounts (username, accountid) values (:username, :id)")
  	void createUserAccount(@Bind("username") String username, @Bind("id") int id);
    
    @SqlQuery("SELECT accountId FROM user_accounts WHERE username=:username")
    List<Integer> getAccountsForUsername(@Bind("username") String username);
}
