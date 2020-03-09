package org.acme;

import org.acme.dao.user.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@Path("/hello")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CompletionStage<User> post(User user) {
        return service.post(user);
    }

    @GET
    @Path("/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public CompletionStage<User> hello(@PathParam("param") String param) throws ExecutionException, InterruptedException {
        return service.fromService(param);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<List<User>> hello() {
        return service.all();
    }

}