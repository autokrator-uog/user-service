package uk.ac.gla.sed.clients.userservice.rest.resources;

import com.codahale.metrics.annotation.Timed;
import uk.ac.gla.sed.clients.userservice.core.handlers.CreateAccountHandler;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/user/{username}/newaccount")
public class CreateAccountResource {
    private CreateAccountHandler createAccountHandler;
    public CreateAccountResource(CreateAccountHandler createAccountHandler) {
        this.createAccountHandler = createAccountHandler;
    }

    @POST
    @Timed
    public Response createAccount(@PathParam("username") String username, @Context UriInfo uriInfo) {
        createAccountHandler.requestCreationFor(username);
        return Response.seeOther(
                uriInfo.getRequestUriBuilder()
                        .path("status")
                        .build()
        ).build();
    }

    @GET
    @Path("/status")
    @Timed
    public Response checkAccountCreationStatus(@PathParam("username") String username, @Context UriInfo uriInfo) {
        if (createAccountHandler.isUserStillWaiting(username)) {
            return Response.seeOther(uriInfo.getRequestUri()).build();
        } else {
            return Response.seeOther(
                    uriInfo.getBaseUriBuilder()
                            .path("user")
                            .path(username)
                            .build()
            ).build();
        }
    }
}
