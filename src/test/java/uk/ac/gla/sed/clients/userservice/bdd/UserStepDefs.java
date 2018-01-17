package uk.ac.gla.sed.clients.userservice.bdd;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java8.En;
import org.mockito.ArgumentCaptor;
import org.skife.jdbi.v2.DBI;
import uk.ac.gla.sed.clients.userservice.core.EventProcessor;
import uk.ac.gla.sed.clients.userservice.core.handlers.CreateAccountHandler;
import uk.ac.gla.sed.clients.userservice.jdbi.UserAccountDAO;
import uk.ac.gla.sed.clients.userservice.jdbi.UserDAO;
import uk.ac.gla.sed.clients.userservice.rest.internal.PasswordHash;
import uk.ac.gla.sed.clients.userservice.rest.resources.CreateAccountResource;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class UserStepDefs implements En {

    private ExecutorService es;
    private EventProcessor eventProcessor;

    private DbTestFixture dbTestFixture;
    private DBI dbi;
    private EventBusClient mockedEventBusClient;
    private UserAccountDAO dao;
    private Response returned;

    @Before
    public void setUp() throws Throwable {
        es = Executors.newSingleThreadExecutor();

        dbTestFixture = new DbTestFixture();
        dbTestFixture.before();
        dbi = dbTestFixture.getDbi();
        dao = dbi.onDemand(UserAccountDAO.class);
        mockedEventBusClient = mock(EventBusClient.class);
        eventProcessor = new EventProcessor(mockedEventBusClient, dao, es);
    }

    private Event getProducedEventOrFail(String eventType) {
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(this.mockedEventBusClient, atLeastOnce()).sendEvent(eventCaptor.capture(), anyObject());

        for (Event val : eventCaptor.getAllValues()) {
            if (val.getType().equals(eventType)) {
                return val;
            }
        }

        fail("Event not produced...");
        return null;
    }

    public UserStepDefs() {
        Given("there is a username (\\w+)", (String personName) -> {
            final UserDAO dao = dbi.onDemand(UserDAO.class);

            PasswordHash hash = PasswordHash.generateFromPassword("asdf");
            dao.createUser(personName, hash.getDigest());
        });



        When("^username (\\w+) requests an account to be created$", (String personName) -> {
            CreateAccountResource testResource = new CreateAccountResource(eventProcessor.getCreateAccountHandler());

            UriInfo testUri = mock(UriInfo.class);
            when(testUri.getRequestUriBuilder()).thenReturn(UriBuilder.fromUri(""));
            returned = testResource.createAccount(personName, testUri);
            System.out.println(returned.toString());
        });

        Then("^an AccountCreationRequest event was created$", () -> {
            getProducedEventOrFail("AccountCreationRequest");
        });

        And("^the user is returned a 303 status code$", () -> {
            assertEquals(returned.getStatus(), 303);
        });
    }
}
