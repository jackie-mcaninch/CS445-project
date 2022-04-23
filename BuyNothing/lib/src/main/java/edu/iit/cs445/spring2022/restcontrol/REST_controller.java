package edu.iit.cs445.spring2022.restcontrol;

import java.util.List;
import java.util.NoSuchElementException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.iit.cs445.spring2022.buynothing.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

// Pingable at http://localhost:8080/bn/api
//   bn:			the basename of the WAR file, see the gradle.build file
//   api:			see the @Path annotation *above* the REST_controller declaration in this file
//   accounts:		see the @Path declaration above the first @GET in this file

@Path("api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class REST_controller {
    private AccountManager 	accman = new AccountManager();
    private AskManager 		askman = new AskManager();
    private GiveManager 	givman = new GiveManager();
    private ThankManager 	thkman = new ThankManager();
    private NoteManager 	notman = new NoteManager();

    // CREATE ACCOUNT
    @Path("/accounts")
    @POST
    public Response createAccount(@Context UriInfo uriInfo, String json) {
        Gson gs = new Gson();
        Account raw_account = gs.fromJson(json, Account.class);
        // create an account and return 201 on success
        try {
        	Account a = accman.createAccount(raw_account);
        	String id = a.getID();
            Gson gson = new Gson();
            String s = gson.toJson(a);
            // Build the URI for the "Location:" header
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(id.toString());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
        // return 400 if required data is missing
        catch (AssertionError e) {
        	String err_msg = accman.assessMissingInfo(raw_account.getID());
        	return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
        }
    }

    // ACTIVATE ACCOUNT
    @Path("/account/{uid}/activate")
    @GET
    public Response activateAccount(@PathParam("uid") String acc_id) {
    	// activate account and return 200 on success
    	try {
    		accman.activateAccount(acc_id);
        	return Response.status(Response.Status.OK).build();
    	}
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
    		String err_msg = "This account does not exist.";
    		return Response.status(Response.Status.NOT_FOUND).entity(err_msg).build();
    	}
    	// return 400 if required data is missing
    	catch (AssertionError e) {
    		String err_msg = accman.assessMissingInfo(acc_id);
    		return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
    	}
    }

    // UPDATE ACCOUNT
    @Path("/account/{uid}")
    @PUT
    public Response updateAccount(@PathParam("uid") String acc_id, String json) {
    	Gson gson = new Gson();
    	Account new_account = gson.fromJson(json, Account.class);
    	// update account and return 204 on success
	    try {
    		accman.updateAccount(acc_id, new_account);
    		return Response.status(Response.Status.NO_CONTENT).build();
	    }
	    // return 404 if the account does not exist
	    catch (NoSuchElementException e) {
	    	String err_msg = "Account does not exist.";
	    	return Response.status(Response.Status.NOT_FOUND).entity(err_msg).build();
	    }
	    // return 400 if required data is missing
	    catch (AssertionError e) {
	    	String err_msg = accman.assessMissingInfo(new_account.getID());
	    	return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
	    }
    }

    // DELETE ACCOUNT
    @Path("/accounts/{uid}")
    @DELETE
    public Response deleteAccount(@PathParam("uid") String acc_id) {
        // delete account and return 204 on success
    	try {
    		accman.deleteAccount(acc_id);
    	    return Response.status(Response.Status.NO_CONTENT).build();
    	} 
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("No account found for ID: " + acc_id).build();
        } 
    }

    // VIEW ALL ACCOUNTS
    @Path("/accounts")
    @GET
    public Response viewAllAccounts() {
    	// display all accounts and return 200
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(accman.viewAllAccounts());
        return Response.status(Response.Status.OK).entity(s).build();
    }
  
    // VIEW SPECIFIC ACCOUNT
    @Path("/accounts/{uid}")
    @GET
    public Response viewAccount(@PathParam("uid") String acc_id) {
    	// display a single account and return 200 on success
    	try {
            Account a = accman.viewAccount(acc_id);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String s = gson.toJson(a);
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if account does not exist
    	catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + acc_id).build();
        }
    }

    // SEARCH ACCOUNTS
    @Path("/accounts?key=keyword{&start_date=DD-MM-YYYY&end_date=DD-MM-YYYY}")
    @GET
    public Response searchAccounts(@QueryParam("key") String key, @QueryParam("start_date") String start, @QueryParam("end_date") String end) {
    	// display all accounts within criteria and return 200 on success
    	try {
    		List<Account> a_list = accman.searchAccounts(key, start, end);
        	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        	String s = gson.toJson(a_list);
        	return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 400 if date is invalid
    	catch (AssertionError e) {
    		String err_msg = "Start date must be before end date.";
    		return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
    	}
    	catch (IllegalArgumentException e) {
    		String err_msg = "Please enter correct date format.";
    		return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
    	}
    	
    }
}

