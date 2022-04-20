package edu.iit.cs445.spring2022.buynothing;

import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

// Pingable at http://localhost:8080/rest-lamp/api/demo/lamps
//   rest-lamp:		the basename of the WAR file, see the gradle.build file
//   api:			see the @ApplicationPath annotation in LampDemo.java
//   demo:			see the @Path annotation *above* the REST_controller declaration in this file
//   lamps:			see the @Path declaration above the first @GET in this file

@Path("bn/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class REST_controller {
    private BNBoundaryInterface accbi = new AccountManager();
    private BNBoundaryInterface askbi = new AskManager();
    private BNBoundaryInterface givbi = new GiveManager();
    private BNBoundaryInterface thkbi = new ThankManager();
    private BNBoundaryInterface notbi = new NoteManager();
    private BNBoundaryInterface repbi = new ReportManager();
    
    @Path("/accounts")
    @POST
    public Response createAccount(@Context UriInfo uriInfo, String json) {
        String id;
        // creates a new account
        Gson gs = new Gson();
        Account raw_account = gs.fromJson(json, Account.class);
        Account a = bbi.createAccount(raw_account);
        
        id = a.getID();
        Gson gson = new Gson();
        String s = gson.toJson(a);
        // Build the URI for the "Location:" header
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(id.toString());

        // The response includes header and body data
        return Response.created(builder.build()).entity(s).build();
    }
    
    @Path("/account/{uid}/activate")
    @GET
    public Response activateAccount(@PathParam("uid") String acc_id) {
    	//Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	//TODO: convert error message into json
    	try {
    		int err_code = bbi.activate(acc_id);
    		switch (err_code) {
    			case 0: return Response.status(Response.Status.OK).build();
    			case -1: return Response.status(Response.Status.BAD_REQUEST).entity("Account needs a name before activation.").build();
    			case -2: return Response.status(Response.Status.BAD_REQUEST).entity("Account needs street before activation.").build();
    			case -3: return Response.status(Response.Status.BAD_REQUEST).entity("Account needs zip code before activation.").build();
    			case -4: return Response.status(Response.Status.BAD_REQUEST).entity("Account needs phone number before activation.").build();
    			case -5: return Response.status(Response.Status.BAD_REQUEST).entity("Account needs picture URL before activation.").build();
    			default: return Response.status(Response.Status.OK).build();
    		}
    	}
    	catch (Exception e) {
    		return Response.status(Response.Status.NOT_FOUND).build();
    	}
    }
    
    @Path("/account/{uid}")
    @PUT
    public Response updateAccount(@PathParam("uid") String acc_id, String json) {
    	Gson gson = new Gson();
    	Account new_account = gson.fromJson(json, Account.class);
	    try {
    		int err_code = bbi.replace(acc_id, new_account);
	        switch (err_code) {
		        case 0: return Response.status(Response.Status.OK).build();
				case -1: return Response.status(Response.Status.BAD_REQUEST).entity("Account is missing a name.").build();
				case -2: return Response.status(Response.Status.BAD_REQUEST).entity("Account is missing a street.").build();
				case -3: return Response.status(Response.Status.BAD_REQUEST).entity("Account is missing a zip code.").build();
				case -4: return Response.status(Response.Status.BAD_REQUEST).entity("Account is missing a phone number.").build();
				case -5: return Response.status(Response.Status.BAD_REQUEST).entity("Account is missing a picture URL.").build();
				default: return Response.status(Response.Status.OK).build();
	        }
	    }
	    catch (Exception e) {
	    	return Response.status(Response.Status.NOT_FOUND).build();
	    }
    }
    
    @Path("/accounts/{uid}")
    @DELETE
    public Response deleteLamp(@PathParam("uid") String acc_id) {
        try {
    		bbi.deleteLamp(acc_id);
    	    return Response.status(Response.Status.NO_CONTENT).build();
    	} catch (Exception e) {
            // return a 404
            return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + lid).build();
        } 
    }
    
    @Path("/accounts")
    @GET
    public Response getAllAccounts() {
        // returns JSON of all accounts created
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(bbi.getAllAccounts());
        return Response.status(Response.Status.OK).build();
    }
    


    @Path("/accounts/{id}")
    @GET
    public Response getSpecificAccount(@PathParam("id") String acc_id) {
        // call the "Get Lamp Detail" use case
        Account a = bbi.getAccountDetail(acc_id);
        if (a.isNil()) {
            // return a 404
            return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + acc_id).build();
        } else {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String s = gson.toJson(a);
            return Response.ok(s).build();
        }
    }

    @Path("/accounts/{id}")
    @PUT
    public Response controlAccount(@PathParam("id") String acc_id, String json) {
        // call the "Update lamp" use case
        Gson gson = new Gson();
        //return Response.ok().build();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}

