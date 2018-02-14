package uk.ac.gla.sed.clients.userservice.core;

import io.dropwizard.lifecycle.Managed;
import uk.ac.gla.sed.clients.userservice.core.events.AccountCreated;
import uk.ac.gla.sed.clients.userservice.core.handlers.CreateAccountHandler;
import uk.ac.gla.sed.clients.userservice.jdbi.UserAccountDAO;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;
import uk.ac.gla.sed.shared.eventbusclient.internal.messages.RegisterMessage;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class EventProcessor implements Managed {
    private final EventBusClient eventBusClient;
    private final CreateAccountHandler createAccountHandler;
    private final ExecutorService workers;

    public EventProcessor(String eventBusURI, UserAccountDAO userAccountDAO, ExecutorService es) {
        this(new EventBusClient(eventBusURI), userAccountDAO, es);
    }

    public EventProcessor(EventBusClient eventBusClient, UserAccountDAO userAccountDAO, ExecutorService es) {
        this.eventBusClient = eventBusClient;
        this.createAccountHandler = new CreateAccountHandler(userAccountDAO, eventBusClient);
        this.workers = es;
    }

    @Override
    public void start() throws Exception {
        this.eventBusClient.start();
        ArrayList<String> interestedEvents = new ArrayList<>();
        interestedEvents.add("AccountCreated");
        RegisterMessage registration = new RegisterMessage("user", interestedEvents);
        eventBusClient.register(registration);
        workers.submit(new ConsumeEventTask());
    }

    @Override
    public void stop() throws Exception {
        this.eventBusClient.stop();
    }

    class ConsumeEventTask implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    Event incomingEvent = eventBusClient.getIncomingEventsQueue().take();
                    switch (incomingEvent.getType()) {
                        case "AccountCreated":
                            AccountCreated parsedEvent = new AccountCreated(incomingEvent);
                            createAccountHandler.processAccountCreatedEvent(parsedEvent);
                            break;
                        default:
                            // ignore
                            break;
                    }

                } catch (InterruptedException interrupt) {
                    System.out.println("ConsumeEventTask interrupted...");
                    return;
                }
            }
        }
    }

    public CreateAccountHandler getCreateAccountHandler() {
        return createAccountHandler;
    }

    public EventBusClient getEventBusClient() {
        return eventBusClient;
    }
}
