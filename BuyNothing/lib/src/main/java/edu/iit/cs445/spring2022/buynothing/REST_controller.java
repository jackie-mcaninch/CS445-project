package edu.iit.cs445.spring2022.buynothing;

import java.util.List;

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
    private AccountManager 	accman = new AccountManager();
    private AskManager 		askman = new AskManager();
    private GiveManager 	givman = new GiveManager();
    private ThankManager 	thkman = new ThankManager();
    private NoteManager 	notman = new NoteManager();
    private ReportManager 	repman = new ReportManager();
    
    @Path("/accounts")
    @POST
    public Response createAccount(@Context UriInfo uriInfo, String json) {
        String id;
        // creates a new account
        Gson gs = new Gson();
        Account raw_account = gs.fromJson(json, Account.class);
        Account a = accman.createAccount(raw_account);
        
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
    	//TODO: convert error message into json?
    	try {
    		int err_code = accman.activateAccount(acc_id);
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
    		int err_code = accman.replaceAccount(acc_id, new_account);
	        switch (err_code) {
		        case 0: return Response.status(Response.Status.OK).build();
				case -1: return Response.status(Response.Status.BAD_REQUEST).entity("Account is missing a name.").build();
				case -2: return Response.status(Response.Status.BAD_REQUEST).entity("Account is missing a street.").build();
				case -3: return Response.status(Response.Status.BAD_REQUEST).entity("Account is missing a zip code.").build();
				case -4: return Response.status(Response.Status.BAD_REQUEST).entity("Account is missing a phone number.").build();
				case -5: return Response.status(Response.Status.BAD_REQUEST).entity("Account is missing a picture URL.").build();
				default: return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error occurred!").build();
	        }
	    }
	    catch (Exception e) {
	    	return Response.status(Response.Status.NOT_FOUND).build();
	    }
    }
    
    @Path("/accounts/{uid}")
    @DELETE
    public Response deleteAccount(@PathParam("uid") String acc_id) {
        try {
    		accman.deleteAccount(acc_id);
    	    return Response.status(Response.Status.NO_CONTENT).build();
    	} catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + acc_id).build();
        } 
    }
    
    @Path("/accounts")
    @GET
    public Response getAllAccounts() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(accman.getAllAccounts());
        return Response.status(Response.Status.OK).entity(s).build();
    }
    
    @Path("/accounts/{uid}")
    @GET
    public Response viewAccount(@PathParam("uid") String acc_id) {
    	try {
            Account a = accman.viewAccount(acc_id);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String s = gson.toJson(a);
            return Response.status(Response.Status.OK).entity(s).build();
    	} catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + acc_id).build();
        }
    }

    @Path("/accounts?key=keyword{&start_date=DD-MM-YYYY&end_date=DD-MM-YYYY}")
    @GET
    public Response searchAccounts(@QueryParam("key") String key, @QueryParam("start_date") String start, @QueryParam("end_date") String end) {
    	List<Account> a_list = accman.searchAccounts(key, start, end);
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	String s = gson.toJson(a_list);
    	return Response.status(Response.Status.OK).entity(s).build();
    }
}

