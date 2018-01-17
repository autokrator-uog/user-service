package uk.ac.gla.sed.clients.userservice.core.handlers;

import uk.ac.gla.sed.clients.userservice.core.events.AccountCreated;
import uk.ac.gla.sed.clients.userservice.core.events.AccountCreationRequest;
import uk.ac.gla.sed.clients.userservice.jdbi.UserAccountDAO;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CreateAccountHandler {
	private static final Logger LOG = Logger.getLogger(CreateAccountHandler.class.getName());

	private Integer lastUsed = 0;
    private Map<Integer, String> requestToUsernameMap = new HashMap<>();

	private UserAccountDAO dao;
    private EventBusClient eventBusClient;

	public CreateAccountHandler(UserAccountDAO dao, EventBusClient eventBusClient) {
		this.dao = dao;
		this.eventBusClient = eventBusClient;
	}

	public synchronized void processAccountCreatedEvent(AccountCreated event) {
        String username = requestToUsernameMap.remove(
                Integer.valueOf(event.getRequestId())
        );

		dao.createUserAccount(username, event.getAccountId());
		LOG.info("Created account entry for user " + event.getRequestId());
	}

	public synchronized void requestCreationFor(String username) {
        requestToUsernameMap.put(lastUsed++, username);
        AccountCreationRequest event = new AccountCreationRequest("1");

        eventBusClient.sendEvent(event, null);
    }

    public synchronized boolean isUserStillWaiting(String username) {
	    return requestToUsernameMap.containsValue(username);
    }
}
