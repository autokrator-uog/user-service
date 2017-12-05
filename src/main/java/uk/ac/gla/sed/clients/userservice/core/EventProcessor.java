package uk.ac.gla.sed.clients.userservice.core;

import io.dropwizard.lifecycle.Managed;
import uk.ac.gla.sed.clients.userservice.core.events.PendingTransaction;
import uk.ac.gla.sed.clients.userservice.core.handlers.PendingTransactionHandler;
import uk.ac.gla.sed.clients.userservice.jdbi.UserDAO;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

import java.util.concurrent.ExecutorService;

public class EventProcessor implements Managed {
    private final EventBusClient eventBusClient;
    private final PendingTransactionHandler pendingTransactionHandler;
    private final ExecutorService workers;

    public EventProcessor(String eventBusURL, AccountDAO dao, ExecutorService es) {
        this.eventBusClient = new EventBusClient(eventBusURL);
        this.pendingTransactionHandler = new PendingTransactionHandler(dao, this.eventBusClient);
        this.workers = es;
    }

    @Override
    public void start() throws Exception {
        this.eventBusClient.start();
        workers.submit(new ConsumeEventTask());
    }

    @Override
    public void stop() throws Exception {
        this.eventBusClient.stop();
    }
