package uk.ac.gla.sed.clients.userservice;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import uk.ac.gla.sed.clients.accountsservice.core.EventProcessor;
import uk.ac.gla.sed.clients.accountsservice.health.EventBusHealthCheck;
import uk.ac.gla.sed.clients.accountsservice.jdbi.AccountDAO;
import uk.ac.gla.sed.clients.accountsservice.rest.resources.AccountResource;
import uk.ac.gla.sed.clients.accountsservice.rest.resources.HelloResource;


public class UserServiceApplication extends Application<UserServiceConfiguration> {



    @Override
    public String getName() {
        return "User Service";
    }

    @Override
    public void initialize(final Bootstrap<UserServiceConfiguration> bootstrap) {
        super.initialize(bootstrap);
        
        //test
        bootstrap.addCommand(new MyCommand());
        //test

        bootstrap.addBundle(new DBIExceptionsBundle());

    }

    @Override
    public void run(final UserServiceConfiguration configuration,
                    final Environment environment) {

        // encapsulate complicated setup logic in factories
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final UserDAO dao = jdbi.onDemand(UserDAO.class);

        // need to add some more implementation based on User design


        final EventBusHealthCheck eventBusHealthCheck = new EventBusHealthCheck(eventBusURL);
        environment.healthChecks().register("event-bus", eventBusHealthCheck);

        environment.jersey().register(new UserResource(dao));

    }

    public static void main(final String[] args) throws Exception {
        new UserServiceApplication().run(args);
    }
}
