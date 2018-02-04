package uk.ac.gla.sed.clients.userservice;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import uk.ac.gla.sed.clients.userservice.core.EventProcessor;
import uk.ac.gla.sed.clients.userservice.core.ReceiptProcessor;
import uk.ac.gla.sed.clients.userservice.health.EventBusHealthCheck;
import uk.ac.gla.sed.clients.userservice.jdbi.UserAccountDAO;
import uk.ac.gla.sed.clients.userservice.jdbi.UserDAO;
import uk.ac.gla.sed.clients.userservice.rest.resources.CreateAccountResource;
import uk.ac.gla.sed.clients.userservice.rest.resources.RegisterResource;
import uk.ac.gla.sed.clients.userservice.rest.resources.UserDetailsResource;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;


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
    	String eventBusURL = configuration.getEventBusURL();

        // encapsulate complicated setup logic in factories
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

        final UserDAO userDAO = jdbi.onDemand(UserDAO.class);
        final UserAccountDAO userAccountDAO = jdbi.onDemand(UserAccountDAO.class);

        // creating dummy data
        userAccountDAO.deleteTableIfExists();
        userDAO.deleteTableIfExists();

        userDAO.createUsersTable();
        userAccountDAO.createUsersAccountTable();

        userDAO.createUser("John", "password");
        userDAO.createUser("Adam", "password");


        /* MANAGED LIFECYCLES */
        final EventProcessor eventProcessor = new EventProcessor(
                eventBusURL,
                userAccountDAO,
                environment.lifecycle().executorService("eventproessor").build()
        );
        EventBusClient eventBusClient = eventProcessor.getEventBusClient();
        final ReceiptProcessor receiptProcessor = new ReceiptProcessor(
                eventBusClient,
                environment.lifecycle().executorService("receiptprocessor").build()
        );
        environment.lifecycle().manage(eventProcessor);
        environment.lifecycle().manage(receiptProcessor);

        /* HEALTH CHECKS */
        final EventBusHealthCheck eventBusHealthCheck = new EventBusHealthCheck(eventBusURL);
        environment.healthChecks().register("event-bus", eventBusHealthCheck);

        environment.jersey().register(new RegisterResource(userDAO));
        environment.jersey().register(new UserDetailsResource(userDAO, userAccountDAO));
        environment.jersey().register(new CreateAccountResource(eventProcessor.getCreateAccountHandler()));
    }

    public static void main(final String[] args) throws Exception {
        new UserServiceApplication().run(args);
    }
}
