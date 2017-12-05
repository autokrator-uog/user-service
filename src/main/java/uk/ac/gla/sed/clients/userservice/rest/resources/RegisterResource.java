package uk.ac.gla.sed.clients.userservice.rest.resources;

import com.codahale.metrics.annotation.Timed;
import uk.ac.gla.sed.clients.userservice.jdbi.UserDAO;
import uk.ac.gla.sed.clients.userservice.rest.api.RegisterRequestBean;
import uk.ac.gla.sed.clients.userservice.rest.internal.PasswordHash;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/register")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegisterResource {
    private UserDAO userDAO;

    public RegisterResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @Timed
    public Response register(RegisterRequestBean registerRequest, @Context UriInfo uriInfo) {
        // FutureTODO validate username/password input against policy

        PasswordHash hash = PasswordHash.generateFromPassword(registerRequest.getPassword());
        userDAO.createUser(registerRequest.getUsername(), hash.getDigest());

        return Response.created(
                uriInfo.getBaseUriBuilder()
                        .path("user")
                        .path(registerRequest.getUsername())
                        .build()
        ).build();
    }
}
