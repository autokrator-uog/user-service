package uk.ac.gla.sed.clients.userservice;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import uk.ac.gla.sed.clients.userservice.core.EventProcessor;
import uk.ac.gla.sed.clients.userservice.health.EventBusHealthCheck;
import uk.ac.gla.sed.clients.userservice.jdbi.UserAccountDAO;
import uk.ac.gla.sed.clients.userservice.jdbi.UserDAO;
import uk.ac.gla.sed.clients.userservice.resources.UserResource;


public class UserServiceApplication extends Application<UserServiceConfiguration> {



    @Override
    public String getName() {
        return "User Service";
    }

    @Override
    public void initialize(final Bootstrap<UserServiceConfiguration> bootstrap) {
        super.initialize(bootstrap);

        bootstrap.addBundle(new DBIExceptionsBundle());

    }

    @Override
    public void run(final UserServiceConfiguration configuration,
                    final Environment environment) {

        // encapsulate complicated setup logic in factories
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final UserDAO daoUser = jdbi.onDemand(UserDAO.class);
        final UserAccountDAO daoUserAccount = jdbi.onDemand(UserAccountDAO.class);

        // creating data
        daoUser.deleteTableIfExists();
        daoUser.createUsersTable();
        daoUser.createUser("John", "password");
        daoUser.createUser("Adam", "password");
        
        daoUserAccount.deleteTableIfExists();
        daoUserAccount.createUsersAccountTable();
        


        /* MANAGED LIFECYCLES */
        final EventProcessor eventProcessor = new EventProcessor(
                eventBusURL,
                daoUser,
                daoUserAccount,
                environment.lifecycle().executorService("eventproessor").build()
        );
        environment.lifecycle().manage(eventProcessor);

        /* HEALTH CHECKS */
        final EventBusHealthCheck eventBusHealthCheck = new EventBusHealthCheck(eventBusURL);
        environment.healthChecks().register("event-bus", eventBusHealthCheck);

        environment.jersey().register(new UserResource(daoUser,daoUserAccount));

    }

    public static void main(final String[] args) throws Exception {
        new UserServiceApplication().run(args);
    }
}
