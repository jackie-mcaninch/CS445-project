package edu.iit.cs445.spring2022.restcontrol;
import java.util.NoSuchElementException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.iit.cs445.spring2022.buynothing.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

// Pingable at http://localhost:8080/bn/api
//   bn:			the basename of the WAR file, see the gradle.build file
//   api:			see the @ApplicationPath annotation in BuyNothingApp.java
//   accounts:		see the @Path declaration above the first @GET in this file

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class REST_controller {
	BoundaryInterface bi = new BuyNothingManager();

	
    // VIEW ALL ACCOUNTS OR FILTERED SET
	@Path("accounts")
    @GET
    public Response viewAccounts(@QueryParam("key") String key, 
							     @DefaultValue("01-Jan-2000") @QueryParam("start_date") String start,
							     @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {
        // if no keyword provided, display all accounts and return 200 on success
    	if (key == null) {
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String s = gson.toJson(bi.viewAllAccounts());
            return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// display all accounts within criteria and return 200 on success
    	try {
        	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        	String s = gson.toJson(bi.searchAccounts(key, start, end));
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
    
    
    // CREATE ACCOUNT
    @Path("accounts")
    @POST
    public Response createAccount(@Context UriInfo uriInfo, String json) {
        Gson gs = new Gson();
        Account raw_account = gs.fromJson(json, Account.class);
        
        // create an account and return 201 on success
        try {
        	Account a = bi.createAccount(raw_account);
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
        	String err_msg = bi.assessBadAccountInfo(raw_account);
        	return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
        }
    }

    // ACTIVATE ACCOUNT
    @Path("accounts/{uid}/activate")
    @GET
    public Response activateAccount(@PathParam("uid") String acc_id) {
    	Gson gson = new Gson();
    	// activate account and return 200 on success
    	try {
    		bi.activateAccount(acc_id);
    		String s = gson.toJson(bi.findAccountByID(acc_id));
        	return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
    		String err_msg = "This account does not exist.";
    		return Response.status(Response.Status.NOT_FOUND).entity(err_msg).build();
    	}
    	// return 400 if required data is missing
    	catch (AssertionError e) {
    		Account a = bi.findAccountByID(acc_id);
    		// TODO CHANGE THIS ITS WRONG WHOOPS
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = bi.assessBadAccountInfo(a);
    		String err_inst = "/accounts/"+acc_id+"/activate";
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
    	}
    }

    // UPDATE ACCOUNT
    @Path("accounts/{uid}")
    @PUT
    public Response updateAccount(@PathParam("uid") String acc_id, String json) {
    	Gson gson = new Gson();
    	Account new_account = gson.fromJson(json, Account.class);
    	// update account and return 204 on success
	    try {
    		bi.updateAccount(acc_id, new_account);
    		return Response.status(Response.Status.NO_CONTENT).build();
	    }
	    // return 404 if the account does not exist
	    catch (NoSuchElementException e) {
	    	String err_msg = "Account does not exist.";
	    	return Response.status(Response.Status.NOT_FOUND).entity(err_msg).build();
	    }
	    // return 400 if required data is missing
	    catch (AssertionError e) {
	    	String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = bi.assessBadAccountInfo(new_account);
    		String err_inst = "/accounts/"+acc_id;
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
	    	String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	    }
    }

    // DELETE ACCOUNT
    @Path("accounts/{uid}")
    @DELETE
    public Response deleteAccount(@PathParam("uid") String acc_id) {
        // delete account and return 204 on success
    	try {
    		bi.deleteAccount(acc_id);
    	    return Response.status(Response.Status.NO_CONTENT).build();
    	} 
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("No account found for ID: " + acc_id).build();
        }
    }
  
    // VIEW SPECIFIC ACCOUNT
    @Path("accounts/{uid}")
    @GET
    public Response viewAccount(@PathParam("uid") String acc_id) {
    	// display a single account and return 200 on success
    	try {
            Account a = bi.viewAccount(acc_id);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String s = gson.toJson(a);
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if account does not exist
    	catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("No account found for ID: " + acc_id).build();
        }
    }

//    // CREATE ASK
//    @Path("/accounts/{uid}/asks")
//    @POST
//    public Response createAsk(@Context UriInfo uriInfo, String json) {
//        Gson gs = new Gson();
//        Ask raw_ask = gs.fromJson(json, Ask.class);
//        // create an ask and return 201 on success
//        try {
//        	if (bi.findByID(raw_ask.getAccountID()).isNil()) throw new NoSuchElementException();
//        	Ask a = bi.createAsk(raw_ask);
//        	String id = a.getID();
//            Gson gson = new Gson();
//            String s = gson.toJson(a);
//            // Build the URI for the "Location:" header
//            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
//            builder.path(id.toString());
//
//            // The response includes header and body data
//            return Response.created(builder.build()).entity(s).build();
//        }
//        // return 404 if the account does not exist
//        catch (NoSuchElementException e) {
//        	String err_msg = "No account found for ID: " + raw_ask.getAccountID();
//        	return Response.status(Response.Status.NOT_FOUND).entity(err_msg).build();
//        }
//        // return 400 if required data is missing
//        catch (AssertionError e) {
//        	String err_msg = bi.assessBadAccountInfo(raw_ask);
//        	return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
//        }
//        // return 400 if type is invalid
//        catch (IllegalArgumentException e) {
//        	String err_msg = "Ask must have a type of \"gift,\" \"borrow,\" or \"help.\"";
//        	return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
//        }
//    }
//    
//    // DEACTIVATE ASK
//    @Path("/accounts/{uid}/asks/{aid}/deactivate")
//    @GET
//    public Response deactivateAsk(@PathParam("uid") String acc_id, @PathParam("aid") String ask_id) {
//    	// deactivate ask and return 200 on success
//    	try {
//    		if (bi.findByID(acc_id).isNil() || bi.findByID(ask_id).isNil()) throw new NoSuchElementException();
//    		if (bi.findByID(ask_id).getAccountID().equals(acc_id)) {
//    			bi.deactivateAsk(ask_id);
//    			return Response.status(Response.Status.OK).build();
//    		}
//    		// account numbers do not match
//    		else throw new AssertionError();
//    	}
//    	// return 404 if the account or ask does not exist
//    	catch (NoSuchElementException e) {
//    		String err_msg = "Account or ask does not exist";
//    		return Response.status(Response.Status.NOT_FOUND).entity(err_msg).build();
//    	}
//    	// return 400 if the account numbers do not match
//    	catch (AssertionError e) {
//    		String err_msg = "This ask was not created by account "+ acc_id +".";
//    		return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
//    	}
//    }
////    
//    // UPDATE ASK
//    @Path("/accounts/{uid}/asks/{aid}")
//    @PUT
//    public Response updateAsk(@PathParam("uid") String acc_id, @PathParam("aid") String ask_id, String json) {
//    	Gson gson = new Gson();
//    	Ask new_ask = gson.fromJson(json, Ask.class);
//    	// update ask and return 204 on success
//	    try {
//	    	if (bi.findByID(acc_id).isNil()) throw new NoSuchElementException();
//    		bi.updateAsk(ask_id, new_ask);
//    		return Response.status(Response.Status.NO_CONTENT).build();
//	    }
//	    // return 404 if the account does not exist
//	    catch (NoSuchElementException e) {
//	    	String err_msg = "Account does not exist.";
//	    	return Response.status(Response.Status.NOT_FOUND).entity(err_msg).build();
//	    }
//	    // return 400 if required data is missing
//	    catch (AssertionError e) {
//	    	String err_msg = bi.assessBadAccountInfo(new_ask);
//	    	return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
//	    }
//	    // return 400 if wrong type
//	    catch (IllegalArgumentException e) {
//	    	String err_msg = "Ask must have a type of \"gift,\" \"borrow,\" or \"help.\"";
//        	return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
//        }
//    }
//    
//    // DELETE ASK
//    @Path("/accounts/{uid}/asks/{aid}")
//    @DELETE
//    public Response deleteAsk(@PathParam("uid") String acc_id, @PathParam("aid") String ask_id) {
//    	try {
//    		if (bi.findByID(acc_id).isNil()) throw new NoSuchElementException();
//    		if (bi.findByID(ask_id).getAccountID().equals(acc_id)) {
//    			bi.deleteAsk(ask_id);
//        		//delete all subordinate resources
//        		bi.deleteByToID(ask_id);
//        	    return Response.status(Response.Status.NO_CONTENT).build();
//        	}
//    		// account numbers do not match
//    		else throw new AssertionError();
//    	} 
//    	// return 404 if the account does not exist
//    	catch (NoSuchElementException e) {
//    		String err_msg = "Account or ask does not exist.";
//            return Response.status(Response.Status.NOT_FOUND).entity(err_msg).build();
//        }
//    	// return 400 if the account numbers do not match
//    	catch (AssertionError e) {
//    		String err_msg = "This ask was not created by account "+ acc_id +".";
//    		return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
//    	}
//    }
//    
//    // VIEW MY ASKS
//    @Path("/accounts/{uid}/asks{?is_active=[true|false]}")
//    @GET
//    public Response viewMyAsks(@PathParam("uid") String acc_id, @QueryParam("is_active") boolean is_active) {
//    	// collect all asks for a user and return 200 on success
//    	try {
//    		if (bi.findByID(acc_id).isNil()) throw new NoSuchElementException();
//    		List<Ask> myAsks = bi.viewMyAsks(acc_id, is_active);
//    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        	String s = gson.toJson(myAsks);
//    		return Response.status(Response.Status.OK).entity(s).build();
//    	}
//    	// return 404 if the account does not exist
//    	catch (NoSuchElementException e) {
//    		String err_msg = "Account does not exist.";
//            return Response.status(Response.Status.NOT_FOUND).entity(err_msg).build();
//        }
//    	// return 400 if arguments are invalid
//    	catch (Exception e) {
//    		String err_msg = "Invalid parameters.";
//    		return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
//    	}
//    }
//    
//    // VIEW ALL ASKS
//    @Path("/asks?v_by=viewed_by_id&is_active=[true|false]")
//    @GET
//    public Response viewAllAsks() {
//    	// collect all asks for all users and return 200 on success
//		List<Ask> allAsks = bi.viewAllAsks();
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//    	String s = gson.toJson(allAsks);
//		return Response.status(Response.Status.OK).entity(s).build();
//    }
    
//    // VIEW ASK
//    @Path("/asks/{aid}")
//    @GET
//    public Response viewAsk(@PathParam("aid") String ask_id) {
//    	// display specified ask and return 200 on success
//    	try {
//    		String s;
//	    	Gson gson = new Gson();
//	    	s = gson.toJson(bi.viewAsk(ask_id));
//	    	return Response.status(Response.Status.OK).entity(s).build();
//    	}
//    	catch (NoSuchElementException e) {
//    		String err_msg = "No ask for id: "+ ask_id;
//    		return Response.status(Response.Status.NOT_FOUND).entity(err_msg).build();
//    	}
//    }
//    
//    // SEARCH ASKS
//    @Path("/asks?key=keyword{&start_date=dd-MMM-YYYY&end_date=dd-MMM-YYYY}")
//    @GET
//    public Response searchAsks(@QueryParam("key") String key, @QueryParam("start_date") String start, @QueryParam("end_date") String end) {
//    	// display all accounts within criteria and return 200 on success
//    	try {
//    		List<Ask> a_list = bi.searchAsks(key, start, end);
//        	Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        	String s = gson.toJson(a_list);
//        	return Response.status(Response.Status.OK).entity(s).build();
//    	}
//    	// return 400 if date is invalid
//    	catch (AssertionError e) {
//    		String err_msg = "Start date must be before end date.";
//    		return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
//    	}
//    	catch (IllegalArgumentException e) {
//    		String err_msg = "Please enter correct date format.";
//    		return Response.status(Response.Status.BAD_REQUEST).entity(err_msg).build();
//    	}
//    }
}

