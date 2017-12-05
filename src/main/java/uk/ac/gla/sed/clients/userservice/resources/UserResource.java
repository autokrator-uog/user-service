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
    public User getUserDetails(@PathParam("userID") LongParam userId) {
    	/*
	        final smthin = smthin.fetch(userId.get());
	        if (smthin != null) {
	            return new User(userId);
	        }
        */
        throw new WebApplicationException(Status.NOT_FOUND);

    }

    @POST
    @Timed
    public Response addUser(@PathParam("user") LongParam userId,
                        @NotNull @Valid Notification notification) {
    	/*
	        final long id = doa.add(userId.get());
	        return Response.created(UriBuilder.fromResource(UserResource.class)
	                                          .build(userId.get(), id))
	                       .build();
    	*/
    }
}
