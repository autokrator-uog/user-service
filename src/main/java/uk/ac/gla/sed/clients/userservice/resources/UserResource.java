package uk.ac.gla.sed.clients.userservice.resources;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.jersey.params.LongParam;
import uk.ac.gla.sed.clients.userservice.jdbi.UserAccountDAO;
import uk.ac.gla.sed.clients.userservice.jdbi.UserDAO;
import uk.ac.gla.sed.clients.userservice.api.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.postgresql.core.Notification;

@Path("/user/{userID}")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private UserDAO daoUser;
    private UserAccountDAO daoUserAccount;
    public UserResource(UserDAO daoUser, UserAccountDAO daoUserAccount) {
        this.daoUser = daoUser;
        this.daoUserAccount = daoUserAccount;
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
							return null;
    	/*
	        final long id = doa.add(userId.get());
	        return Response.created(UriBuilder.fromResource(UserResource.class)
	                                          .build(userId.get(), id))
	                       .build();
    	*/
    }
}
