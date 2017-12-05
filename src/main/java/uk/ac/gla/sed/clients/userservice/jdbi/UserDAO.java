package uk.ac.gla.sed.clients.userservice.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;



public interface UserDAO {
    @SqlUpdate("CREATE users TABLE")
    void createUsersTable();

    @SqlUpdate("DELETE users TABLE IF EXISTS")
    void deleteTableIfExists();

    @SqlUpdate("insert into users (id, name) values (:id, :name)")
  	void insert(@Bind("id") int id, @Bind("name") String name);

  	@SqlQuery("select name from users where id = :id")
 	String findNameById(@Bind("id") int id);
}
