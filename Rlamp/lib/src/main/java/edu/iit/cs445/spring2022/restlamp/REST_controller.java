package edu.iit.cs445.spring2022.restlamp;

import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

// Pingable at http://localhost:8080/JaxrsRestLamp/api/demo/lamps
//   JaxrsRestLamp:	the name of the WAR file, see the gradle.build file
//   api:			see the @ApplicationPath annotation in LampDemo.java
//   demo:			see the @Path annotation *above* the REST_controller declaration in this file
//   lamps:			see the @Path declaration above the first @GET in this file

@Path("demo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class REST_controller {
    private BoundaryInterface bi = new LampManager();
    
    @Path("/lamps")
    @GET
    public Response getAllLamps() {
        // calls the "Get All Lamps" use case
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(bi.getAllLamps());
        return Response.status(Response.Status.OK).entity(s).build();
    }
    
    
    @Path("/lamps")
    @POST
    public Response makeLamp(@Context UriInfo uriInfo) {
        UUID id;
        // calls the "Create Lamp" use case
        Lamp l = bi.createLamp();

        id = l.getID();
        Gson gson = new Gson();
        String s = gson.toJson(l);
        // Build the URI for the "Location:" header
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(id.toString());

        // The response includes header and body data
        return Response.created(builder.build()).entity(s).build();
    }

    @Path("/lamps/{id}")
    @GET
    public Response getSpecificLamp(@PathParam("id") UUID lid) {
        // call the "Get Lamp Detail" use case
        Lamp l = bi.getLampDetail(lid);
        if (l.isNil()) {
            // return a 404
            return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + lid).build();
        } else {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String s = gson.toJson(l);
            return Response.ok(s).build();
        }
    }

    @Path("/lamps/{id}")
    @PUT
    public Response controlLamp(@PathParam("id") UUID lid, String json) {
        // call the "Update lamp" use case
        Gson gson = new Gson();
        Lamp il = gson.fromJson(json, Lamp.class);
        bi.replaceLamp(lid, il);
        return Response.ok().build();
    }
    
    @Path("/lamps/{id}")
    @DELETE
    public Response deleteLamp(@PathParam("id") UUID lid) {
        // call the "Delete Lamp" use case
    	try {
    		bi.deleteLamp(lid);
    		// return a 204
    	    return Response.status(Response.Status.NO_CONTENT).build();
    		//Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //String s = gson.toJson(l);
            //return Response.ok(s).build();
    	} catch (Exception e) {
            // return a 404
            return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + lid).build();
        } 
    }
}
