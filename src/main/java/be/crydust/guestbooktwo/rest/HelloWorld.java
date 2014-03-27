package be.crydust.guestbooktwo.rest;

import java.util.Arrays;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/helloworld")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class HelloWorld {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of HelloWorld
     */
    public HelloWorld() {
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHtml() {
        return "<html><body><h1>Hello in HTML from JAX-RS!</h1></body></html>";
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Greeting getGreeting() {
        return new Greeting("JAX-RS");
    }

    @GET
    @Path("/list")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Greeting> getGreetingList() {
        return Arrays.asList(
                new Greeting("JAX-RS zero"),
                new Greeting("JAX-RS one"));
    }

    @GET
    @Path("/complicated")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ComplicatedGreeting getComplicatedGreeting() {
        return ComplicatedGreeting.createComplicatedGreeting();
    }

    @POST
    @Path("/echo")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ComplicatedGreeting echo(ComplicatedGreeting in) {
        return in;
    }
}
