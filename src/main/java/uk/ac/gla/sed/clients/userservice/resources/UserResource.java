package uk.ac.gla.sed.clients.Usersservice.resources;

import com.codahale.metrics.annotation.Timed;
import uk.ac.gla.sed.clients.Usersservice.jdbi.UserDAO;
import uk.ac.gla.sed.clients.Usersservice.rest.api.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user/{userID}")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private UserDAO dao;
    public UserResource(UserDAO dao) {
        this.dao = dao;
    }

    @GET
    @Timed
    public User getUserDetails(@PathParam("userID") int userId) {
        BigDecimal balance = dao.getBalance(UserId);
        if (balance == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new User(UserId, balance);
    }
}
