package uk.ac.gla.sed.clients.userservice.rest.resources;

import com.codahale.metrics.annotation.Timed;
import uk.ac.gla.sed.clients.userservice.jdbi.UserDAO;
import uk.ac.gla.sed.clients.userservice.rest.internal.PasswordHash;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterResource {
    private UserDAO userDAO;

    public RegisterResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @Timed
    public Response register(@FormParam("username") String username, @FormParam("password") String passwordUnhashed) {
        PasswordHash hash = PasswordHash.generateFromPassword(20, passwordUnhashed);
        userDAO.createUser(username, hash.getDigest());

        return Response.status(Response.Status.CREATED).build();
    }
}
