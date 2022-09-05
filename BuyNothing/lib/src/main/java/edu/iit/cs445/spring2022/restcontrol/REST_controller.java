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
	// initialize boundary interface
	BoundaryInterface bi = new BuyNothingManager();
	
	// initialize json conversion object
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	
    // VIEW ALL ACCOUNTS OR FILTERED SET
	@Path("accounts")
    @GET
    public Response viewAccounts(@QueryParam("key") String key, 
							     @DefaultValue("01-Jan-2000") @QueryParam("start_date") String start,
							     @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {		
		// if no keyword provided, display all accounts and return 200 on success
    	if (key == null) {
            String s = gson.toJson(bi.viewAllAccounts());
            return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// display all accounts within criteria and return 200 on success
    	try {
        	String s = gson.toJson(bi.searchAccounts(key, start, end));
        	return Response.status(Response.Status.OK).entity(s).build();
    	}
     	// return 400 if request is not valid
    	catch (AssertionError e) {
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts";
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }
	
    // VIEW SPECIFIC ACCOUNT
    @Path("accounts/{uid}")
    @GET
    public Response viewAccount(@PathParam("uid") String acc_id) {    	
    	// display a single account and return 200 on success
    	try {
            Account a = bi.viewAccount(acc_id);
            String s = gson.toJson(a);
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if account does not exist
    	catch (NoSuchElementException e) {
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id;
    		int err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    }
    
    // CREATE NEW ACCOUNT
    @Path("accounts")
    @POST
    public Response createAccount(@Context UriInfo uriInfo, String json) {    	
    	// create raw account from request body
        Account raw_account = gson.fromJson(json, Account.class);
        
        // create an account and return 201 on success
        try {
        	Account a = bi.createAccount(raw_account);
        	String id = a.getID();
            String s = gson.toJson(a);
            // Build the URI for the "Location:" header
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(id.toString());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
     	// return 400 if request is not valid
        catch (AssertionError e) {
        	String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts";
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
        	return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
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
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id+"activate";
    		int err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.NOT_FOUND).entity(s).build();
    	}
     	// return 400 if request is not valid
    	catch (AssertionError e) {
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id+"activate";
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }

    // UPDATE ACCOUNT
    @Path("accounts/{uid}")
    @PUT
    public Response updateAccount(@PathParam("uid") String acc_id, String json) {
    	// create new account from request body
    	Account new_account = gson.fromJson(json, Account.class);
    	
    	// update account and return 204 on success
	    try {
    		bi.updateAccount(acc_id, new_account);
    		return Response.status(Response.Status.NO_CONTENT).build();
	    }
	    // return 404 if the account does not exist
	    catch (NoSuchElementException e) {
	    	String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id;
    		int err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	    	return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
     	// return 400 if request is not valid
	    catch (AssertionError e) {
	    	String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
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
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id;
    		int err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    }
    
    // VIEW ALL ASKS OR FILTERED SET
    @Path("asks")
	@GET
	public Response viewAsks(@DefaultValue("") @QueryParam("key") String key,
							 @DefaultValue("") @QueryParam("v_by") String uid,
							 @DefaultValue("") @QueryParam("is_active") String is_active,
 							 @DefaultValue("01-Jan-2000") @QueryParam("start_date") String start,
 							 @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {
    	// if no keyword provided, display all accounts and return 200 on success
    	if (key.equals("")) {
    		String s;
    		if (!uid.equals("")) {
    			switch (is_active) {
        		case "":
        			s = gson.toJson(bi.viewAllMyAsks(uid));
        			break;
        		case "true":
        			s = gson.toJson(bi.viewMyAsks(uid, true));
        			break;
        		case "false":
        			s = gson.toJson(bi.viewMyAsks(uid, false));
        			break;
        		default:
        			throw new AssertionError("Invalid value for parameter \'is_active\'.");
        		}
    		}
    		else {
    			s = gson.toJson(bi.viewAllAsks());
    		}
            return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// display all accounts within criteria and return 200 on success
     	try {
         	String s = gson.toJson(bi.searchAsks(key, start, end));
         	return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// return 400 if request is not valid
     	catch (AssertionError e) {
     		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/asks";
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
     		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
     	}
	}
 
    // VIEW SPECIFIC ASK
    @Path("asks/{aid}")
    @GET
    public Response viewAsk(@PathParam("aid") String ask_id) {
    	// display a single account and return 200 on success
    	try {
            Ask a = bi.viewAsk(ask_id);
            String s = gson.toJson(a);
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if account does not exist
    	catch (NoSuchElementException e) {
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/asks/"+ask_id;
    		int err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    }
    
    // VIEW MY ASKS
    @Path("/accounts/{uid}/asks")
    @GET
    public Response viewMyAsks(@PathParam("uid") String acc_id,
    						   @DefaultValue("") @QueryParam("is_active") String is_active) {
    	// collect all asks for a user and return 200 on success
	  	try {
	  		String s;
	  		switch (is_active) {
	  			case "":
	  				s = gson.toJson(bi.viewAllMyAsks(acc_id));
	  				break;
	  			case "true":
	  				s = gson.toJson(bi.viewMyAsks(acc_id, true));
	  				break;
	  			case "false":
	  				s = gson.toJson(bi.viewMyAsks(acc_id, false));
	  				break;
	  			default:
	  				throw new AssertionError("Invalid value for parameter \'is_active\'.");	  				
	  		}
	  		return Response.status(Response.Status.OK).entity(s).build();
	  	}
	  	// return 404 if the account does not exist
	  	catch (NoSuchElementException e) {
	  		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id+"/asks";
    		int err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	        return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
	  	// return 400 if request is not valid
	  	catch (AssertionError e) {
	  		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id+"/asks";
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	  		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	  	}
    }
    
    // CREATE NEW ASK
    @Path("accounts/{uid}/asks")
    @POST
    public Response createAsk(@PathParam("uid") String acc_id, @Context UriInfo uriInfo, String json) {
        Gson gs = new Gson();
        Ask raw_ask = gs.fromJson(json, Ask.class);
     
        // create an account and return 201 on success
        try {
        	Ask a = bi.createAsk(acc_id, raw_ask);
        	String id = a.getID();
            Gson gson = new Gson();
            String s = gson.toJson(a);
            // Build the URI for the "Location:" header
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(id.toString());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
        // return 400 if request is not valid
        catch (AssertionError e) {
        	String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
        	String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id;
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
        	return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
        }
    }
    
    // DEACTIVATE ASK
    @Path("/accounts/{uid}/asks/{aid}/deactivate")
    @GET
    public Response deactivateAsk(@PathParam("uid") String acc_id, @PathParam("aid") String ask_id) {
    	// initialize json conversion object
  		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	// deactivate ask and return 200 on success
    	try {
    		Ask res = bi.deactivateAsk(acc_id, ask_id);
    		String s = gson.toJson(res);
    		return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 404 if the account or ask does not exist
    	catch (NoSuchElementException e) {
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id+"/asks/"+ask_id+"/deactivate";
    		int err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.NOT_FOUND).entity(s).build();
    	}
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id+"/asks/"+ask_id+"/deactivate";
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }

    // UPDATE ASK
    @Path("accounts/{uid}/asks/{aid}")
    @PUT
    public Response updateAsk(@PathParam("uid") String acc_id, @PathParam("aid") String ask_id, String json) {
    	// create new ask from request body
    	Ask new_ask = gson.fromJson(json, Ask.class);
    	
    	// update account and return 204 on success
	    try {
    		bi.updateAsk(ask_id, new_ask);
    		return Response.status(Response.Status.NO_CONTENT).build();
	    }
	    // return 404 if the account does not exist
	    catch (NoSuchElementException e) {
	    	String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id+"/asks/"+ask_id;
    		int err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	    	return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
	    // return 400 if request is not valid
	    catch (AssertionError e) {
	    	String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id+"/asks/"+ask_id;
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	    }
    }

    // DELETE ASK
    @Path("accounts/{uid}/asks/{aid}")
    @DELETE
    public Response deleteAsk(@PathParam("uid") String acc_id, @PathParam("aid") String ask_id) {
        // delete account and return 204 on success
    	try {
    		bi.deleteAsk(acc_id, ask_id);
    	    return Response.status(Response.Status.NO_CONTENT).build();
    	} 
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id+"/asks/"+ask_id;
    		int err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    		String err_title = "Your request data didn't pass validation";
    		String err_msg = e.getMessage();
    		String err_inst = "/accounts/"+acc_id+"/asks/"+ask_id;
    		int err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }
}

