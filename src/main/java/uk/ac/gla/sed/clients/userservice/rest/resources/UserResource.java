package uk.ac.gla.sed.clients.userservice.rest.resources;

import com.codahale.metrics.annotation.Timed;

import uk.ac.gla.sed.clients.userservice.jdbi.UserAccountDAO;
import uk.ac.gla.sed.clients.userservice.rest.api.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

import java.util.List;

@Path("/user/{username}")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
//    private UserDAO userDAO;
    private UserAccountDAO userAccountDAO;
    private EventBusClient eventBusClient;

    public UserResource(UserAccountDAO userAccountDAO, EventBusClient eventBusClient) {
//        this.userDAO = userDAO;
        this.userAccountDAO = userAccountDAO;
        this.eventBusClient = eventBusClient;
    }

    @GET
    @Timed
    public User getUserDetails(@PathParam("username") String username) {
    	List<Integer> accounts = userAccountDAO.getAccountsForUsername(username);
        if (accounts != null) {
            return new User(username, accounts);
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @POST
    @Timed
    public Response createAccount(@PathParam("username") String username) {
    	// TODO create AccountCreationRequest event and send to event bus
        AccountCreationRequest event = new AccountCreationRequest();

        eventBusClient.sendEvent(event);
        return Response.status(Response.Status.CREATED).build();
    }
}
