package uk.ac.gla.sed.clients.userservice.rest.resources;

import com.codahale.metrics.annotation.Timed;

import uk.ac.gla.sed.clients.userservice.jdbi.UserAccountDAO;
import uk.ac.gla.sed.clients.userservice.jdbi.UserDAO;
import uk.ac.gla.sed.clients.userservice.rest.api.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("/user/{username}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserDetailsResource {
    private UserDAO userDAO;
    private UserAccountDAO userAccountDAO;

    public UserDetailsResource(UserDAO userDAO, UserAccountDAO userAccountDAO) {
        this.userDAO = userDAO;
        this.userAccountDAO = userAccountDAO;
    }

    @GET
    @Timed
    public User getUserDetails(@PathParam("username") String username) {
        if (userDAO.getUserIfExistsElseNone(username) != null) {
            List<Integer> accounts = userAccountDAO.getAccountsForUsername(username);
            return new User(username, accounts);
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
}
