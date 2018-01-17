package uk.ac.gla.sed.clients.userservice.bdd;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.junit.rules.ExternalResource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import uk.ac.gla.sed.clients.userservice.jdbi.UserAccountDAO;
import uk.ac.gla.sed.clients.userservice.jdbi.UserDAO;

class DbTestFixture extends ExternalResource {
    private DBI dbi;
    private Handle handle;

    public DbTestFixture() {}

    @Override
    protected void before() throws Throwable {
        Environment environment = new Environment("test-env", Jackson.newObjectMapper(), null, new MetricRegistry(), null);
        dbi = new DBIFactory().build(environment, getDataSourceFactory(), "test");
        handle = dbi.open();
        migrateDatabase();
    }

    private void migrateDatabase() {
        final UserDAO userDAO = dbi.onDemand(UserDAO.class);
        final UserAccountDAO userAccountDAO = dbi.onDemand(UserAccountDAO.class);

        // create table structures
        userAccountDAO.deleteTableIfExists();
        userDAO.deleteTableIfExists();

        userDAO.createUsersTable();
        userAccountDAO.createUsersAccountTable();
    }

    @Override
    protected void after() {
        final UserDAO userDAO = dbi.onDemand(UserDAO.class);
        final UserAccountDAO userAccountDAO = dbi.onDemand(UserAccountDAO.class);

        userAccountDAO.deleteTableIfExists();
        userDAO.deleteTableIfExists();

        handle.close();
    }

    private DataSourceFactory getDataSourceFactory() {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();

        // SQLITE TEST DB
//        dataSourceFactory.setDriverClass("org.sqlite.JDBC");
//        dataSourceFactory.setUrl("jdbc:sqlite:test.db");
//        dataSourceFactory.setUser("test");
//        dataSourceFactory.setPassword("test");

        // POSTGRES LIVE DB
        dataSourceFactory.setDriverClass("org.postgresql.Driver");
        dataSourceFactory.setUrl("jdbc:postgresql://postgresql:5432/userservice");
        dataSourceFactory.setUser("userservice");
        dataSourceFactory.setPassword("userservice");

        return dataSourceFactory;
    }

    public DBI getDbi() {
        return dbi;
    }

}
