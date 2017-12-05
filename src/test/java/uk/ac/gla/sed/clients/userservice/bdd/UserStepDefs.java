package uk.ac.gla.sed.clients.userservice.bdd;

import cucumber.api.java.Before;
import cucumber.api.java8.En;
import org.skife.jdbi.v2.DBI;
import uk.ac.gla.sed.clients.userservice.jdbi.UserDAO;
import uk.ac.gla.sed.clients.userservice.rest.internal.PasswordHash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UserStepDefs implements En {
    private DbTestFixture db;
    private DBI dbi;

    @Before
    public void setUpdDBFixture() throws Throwable {
        db = new DbTestFixture();
        db.before();
        dbi = db.getDbi();
    }

    public UserStepDefs() {
        Given("there is a username (\\w+)", (String personName) -> {
            final UserDAO dao = dbi.onDemand(UserDAO.class);

            PasswordHash hash = PasswordHash.generateFromPassword("asdf");
            dao.createUser(personName, hash.getDigest());
        });
    }
}
