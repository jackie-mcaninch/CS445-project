package edu.iit.cs445.spring2022.restcontrol;
import java.util.Iterator;
import java.util.List;
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

	// set error information for reuse
	String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
    String err_title = "Your request data didn't pass validation";
	String err_msg;
	String err_inst;
	int err_status;
	
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
    		err_msg = e.getMessage();
    		err_inst = "/accounts";
    		err_status = 400;
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
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    }
    
    // CREATE NEW ACCOUNT
    @Path("accounts")
    @POST
    public Response createAccount(@Context UriInfo uri_info, String json) {    	
    	// create raw account from request body
        Account raw_account = gson.fromJson(json, Account.class);
        
        // create an account and return 201 on success
        try {
        	Account a = bi.createAccount(raw_account);
        	String id = a.getID();
            String s = gson.toJson(a);
            // Build the URI for the "Location:" header
            UriBuilder builder = uri_info.getAbsolutePathBuilder();
            builder.path(id.toString());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
     	// return 400 if request is not valid
        catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
        	return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
        }
    }

    // ACTIVATE ACCOUNT
    @Path("accounts/{uid}/activate")
    @GET
    public Response activateAccount(@PathParam("uid") String acc_id) {
    	// activate account and return 200 on success
    	try {
    		bi.activateAccount(acc_id);
    		String s = gson.toJson(bi.findAccountByID(acc_id));
        	return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"activate";
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.NOT_FOUND).entity(s).build();
    	}
     	// return 400 if request is not valid
    	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"activate";
    		err_status = 400;
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
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	    	return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
     	// return 400 if request is not valid
	    catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id;
    		err_status = 400;
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
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id;
    		err_status = 404;
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
    	// if no keyword provided, display all accounts according to criteria and return 200 on success
    	if (key.equals("")) {
    		String s;
    		if (!uid.equals("")) {
    			List<Ask> subset = bi.viewAllAsksViewedBy(uid);
    			Iterator<Ask> ask_iter = subset.listIterator();
    			// filter by is_active parameter
    			while (ask_iter.hasNext()) {
    				Ask a = ask_iter.next();
    				if (is_active.equals("true") && !a.getActiveStatus()) {
    					subset.remove(a);
    				}
    				if (is_active.equals("false") && a.getActiveStatus()) {
    					subset.remove(a);
    				}
    			}
    			s = gson.toJson(subset);
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
    		err_msg = e.getMessage();
    		err_inst = "/asks";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
     		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
     	}
	}
 
    // VIEW SPECIFIC ASK
    @Path("asks/{aid}")
    @GET
    public Response viewAsk(@PathParam("aid") String ask_id) {
    	// display a single ask and return 200 on success
    	try {
            Ask a = bi.viewAsk(ask_id);
            String s = gson.toJson(a);
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if ask does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/asks/"+ask_id;
    		err_status = 404;
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
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/asks";
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	        return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
	  	// return 400 if request is not valid
	  	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/asks";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	  		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	  	}
    }
    
    // CREATE NEW ASK
    @Path("accounts/{uid}/asks")
    @POST
    public Response createAsk(@PathParam("uid") String acc_id, @Context UriInfo uri_info, String json) {
        Gson gs = new Gson();
        Ask raw_ask = gs.fromJson(json, Ask.class);
     
        // create an account and return 201 on success
        try {
        	Ask a = bi.createAsk(acc_id, raw_ask);
        	String id = a.getID();
            String s = gson.toJson(a);
            // Build the URI for the "Location:" header
            UriBuilder builder = uri_info.getAbsolutePathBuilder();
            builder.path(id.toString());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
        // return 400 if request is not valid
        catch (AssertionError e) {
        	err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id;
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
        	return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
        }
    }
    
    // DEACTIVATE ASK
    @Path("/accounts/{uid}/asks/{aid}/deactivate")
    @GET
    public Response deactivateAsk(@PathParam("uid") String acc_id, @PathParam("aid") String ask_id) {
    	// deactivate ask and return 200 on success
    	try {
    		Ask res = bi.deactivateAsk(acc_id, ask_id);
    		String s = gson.toJson(res);
    		return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 404 if the account or ask does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/asks/"+ask_id+"/deactivate";
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.NOT_FOUND).entity(s).build();
    	}
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/asks/"+ask_id+"/deactivate";
    		err_status = 400;
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
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/asks/"+ask_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	    	return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
	    // return 400 if request is not valid
	    catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/asks/"+ask_id;
    		err_status = 400;
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
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/asks/"+ask_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/asks/"+ask_id;
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }
    
    // VIEW ALL GIVES OR FILTERED SET
    @Path("gives")
	@GET
	public Response viewGives(@DefaultValue("") @QueryParam("key") String key,
							  @DefaultValue("") @QueryParam("v_by") String uid,
							  @DefaultValue("") @QueryParam("is_active") String is_active,
 							  @DefaultValue("01-Jan-2000") @QueryParam("start_date") String start,
 							  @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {
    	// if no keyword provided, display all accounts according to criteria and return 200 on success
    	if (key.equals("")) {
    		String s;
    		if (!uid.equals("")) {
    			List<Give> subset = bi.viewAllGivesViewedBy(uid);
    			Iterator<Give> give_iter = subset.listIterator();
    			// filter by is_active parameter
    			while (give_iter.hasNext()) {
    				Give g = give_iter.next();
    				if (is_active.equals("true") && !g.getActiveStatus()) {
    					subset.remove(g);
    				}
    				if (is_active.equals("false") && g.getActiveStatus()) {
    					subset.remove(g);
    				}
    			}
    			s = gson.toJson(subset);
    		}
    		else {
    			s = gson.toJson(bi.viewAllGives());
    		}
            return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// display all accounts within criteria and return 200 on success
     	try {
         	String s = gson.toJson(bi.searchGives(key, start, end));
         	return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// return 400 if request is not valid
     	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/gives";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
     		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
     	}
	}
 
    // VIEW SPECIFIC GIVE
    @Path("gives/{gid}")
    @GET
    public Response viewGive(@PathParam("gid") String giv_id) {
    	// display a single give and return 200 on success
    	try {
            Give g = bi.viewGive(giv_id);
            String s = gson.toJson(g);
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if give does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/gives/"+giv_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    }
    
    // VIEW MY GIVES
    @Path("/accounts/{uid}/gives")
    @GET
    public Response viewMyGives(@PathParam("uid") String acc_id,
    						   @DefaultValue("") @QueryParam("is_active") String is_active) {
    	// collect all gives for a user and return 200 on success
	  	try {
	  		String s;
	  		switch (is_active) {
	  			case "":
	  				s = gson.toJson(bi.viewAllMyGives(acc_id));
	  				break;
	  			case "true":
	  				s = gson.toJson(bi.viewMyGives(acc_id, true));
	  				break;
	  			case "false":
	  				s = gson.toJson(bi.viewMyGives(acc_id, false));
	  				break;
	  			default:
	  				throw new AssertionError("Invalid value for parameter \'is_active\'.");	  				
	  		}
	  		return Response.status(Response.Status.OK).entity(s).build();
	  	}
	  	// return 404 if the account does not exist
	  	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/gives";
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	        return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
	  	// return 400 if request is not valid
	  	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/gives";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	  		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	  	}
    }
    
    // CREATE NEW GIVE
    @Path("accounts/{uid}/gives")
    @POST
    public Response createGive(@PathParam("uid") String acc_id, @Context UriInfo uri_info, String json) {
        Gson gs = new Gson();
        Give raw_give = gs.fromJson(json, Give.class);
     
        // create an account and return 201 on success
        try {
        	Give g = bi.createGive(acc_id, raw_give);
        	String id = g.getID();
            String s = gson.toJson(g);
            // Build the URI for the "Location:" header
            UriBuilder builder = uri_info.getAbsolutePathBuilder();
            builder.path(id.toString());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
        // return 400 if request is not valid
        catch (AssertionError e) {
        	err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id;
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
        	return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
        }
    }
    
    // DEACTIVATE GIVE
    @Path("/accounts/{uid}/gives/{gid}/deactivate")
    @GET
    public Response deactivateGive(@PathParam("uid") String acc_id, @PathParam("gid") String giv_id) {
    	// deactivate give and return 200 on success
    	try {
    		Give res = bi.deactivateGive(acc_id, giv_id);
    		String s = gson.toJson(res);
    		return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 404 if the account or ask does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/gives/"+giv_id+"/deactivate";
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.NOT_FOUND).entity(s).build();
    	}
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/gives/"+giv_id+"/deactivate";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }

    // UPDATE GIVE
    @Path("accounts/{uid}/gives/{gid}")
    @PUT
    public Response updateGive(@PathParam("uid") String acc_id, @PathParam("gid") String giv_id, String json) {
    	// create new ask from request body
    	Give new_give = gson.fromJson(json, Give.class);
    	
    	// update account and return 204 on success
	    try {
    		bi.updateGive(giv_id, new_give);
    		return Response.status(Response.Status.NO_CONTENT).build();
	    }
	    // return 404 if the account does not exist
	    catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/gives/"+giv_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	    	return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
	    // return 400 if request is not valid
	    catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/gives/"+giv_id;
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	    }
    }

    // DELETE GIVE
    @Path("accounts/{uid}/gives/{gid}")
    @DELETE
    public Response deleteGive(@PathParam("uid") String acc_id, @PathParam("gid") String giv_id) {
        // delete account and return 204 on success
    	try {
    		bi.deleteGive(acc_id, giv_id);
    	    return Response.status(Response.Status.NO_CONTENT).build();
    	} 
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/gives/"+giv_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/gives/"+giv_id;
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }




	// VIEW ALL THANKS OR FILTERED SET
    @Path("thanks")
	@GET
	public Response viewThanks(@DefaultValue("") @QueryParam("key") String key,
							   @DefaultValue("") @QueryParam("v_by") String uid,
							   @DefaultValue("") @QueryParam("is_active") String is_active,
							   @DefaultValue("01-Jan-2000") @QueryParam("start_date") String start,
							   @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {
    	// if no keyword provided, display all accounts according to criteria and return 200 on success
    	if (key.equals("")) {
    		String s;
    		if (!uid.equals("")) {
    			List<Thank> subset = bi.viewAllThanksViewedBy(uid);
    			Iterator<Thank> thank_iter = subset.listIterator();
    			// filter by is_active parameter
    			while (thank_iter.hasNext()) {
    				Thank t = thank_iter.next();
    				if (is_active.equals("true") && !t.getActiveStatus()) {
    					subset.remove(t);
    				}
    				if (is_active.equals("false") && t.getActiveStatus()) {
    					subset.remove(t);
    				}
    			}
    			s = gson.toJson(subset);
    		}
    		else {
    			s = gson.toJson(bi.viewAllThanks());
    		}
            return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// display all accounts within criteria and return 200 on success
     	try {
         	String s = gson.toJson(bi.searchThanks(key, start, end));
         	return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// return 400 if request is not valid
     	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/thanks";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
     		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
     	}
	}
 
    // VIEW SPECIFIC THANK
    @Path("thanks/{tid}")
    @GET
    public Response viewThank(@PathParam("tid") String thk_id) {
    	// display a single thank and return 200 on success
    	try {
            Thank t = bi.viewThank(thk_id);
            String s = gson.toJson(t);
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if thank does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/thanks/"+thk_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    }
    
    // VIEW MY THANKS
    @Path("/accounts/{uid}/thanks")
    @GET
    public Response viewMyThanks(@PathParam("uid") String acc_id,
    						     @DefaultValue("") @QueryParam("is_active") String is_active) {
    	// collect all thanks for a user and return 200 on success
	  	try {
	  		String s;
	  		switch (is_active) {
	  			case "":
	  				s = gson.toJson(bi.viewAllMyThanks(acc_id));
	  				break;
	  			case "true":
	  				s = gson.toJson(bi.viewMyThanks(acc_id, true));
	  				break;
	  			case "false":
	  				s = gson.toJson(bi.viewMyThanks(acc_id, false));
	  				break;
	  			default:
	  				throw new AssertionError("Invalid value for parameter \'is_active\'.");	  				
	  		}
	  		return Response.status(Response.Status.OK).entity(s).build();
	  	}
	  	// return 404 if the account does not exist
	  	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/thanks";
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	        return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
	  	// return 400 if request is not valid
	  	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/thanks";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	  		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	  	}
    }

	// VIEW THANKS FROM USER
	@Path("/thanks/received/{uid}")
	@GET
	public Response viewThanksForUser(@PathParam("uid") String acc_id) {
		// collect all thanks addressed to the user and return 200 on success
		try {
			String s = gson.toJson(bi.viewThanksForUser(acc_id));
			return Response.status(Response.Status.OK).entity(s).build();
		}
		// return 404 if account does not exist
		catch (NoSuchElementException e) {
			err_msg = e.getMessage();
    		err_inst = "/thanks/received/"+acc_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	        return Response.status(Response.Status.NOT_FOUND).entity(s).build();
		}
		// return 400 if request is not valid
		catch (AssertionError e) {
			err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/thanks";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	        return Response.status(Response.Status.NOT_FOUND).entity(s).build();
		}
	}
    
    // CREATE NEW THANK
    @Path("accounts/{uid}/thanks")
    @POST
    public Response createThank(@PathParam("uid") String acc_id, @Context UriInfo uri_info, String json) {
        Gson gs = new Gson();
        Thank raw_thank = gs.fromJson(json, Thank.class);
     
        // create an account and return 201 on success
        try {
        	Thank t = bi.createThank(acc_id, raw_thank);
        	String id = t.getID();
            String s = gson.toJson(t);
            // Build the URI for the "Location:" header
            UriBuilder builder = uri_info.getAbsolutePathBuilder();
            builder.path(id.toString());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
        // return 400 if request is not valid
        catch (AssertionError e) {
        	err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id;
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
        	return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
        }
    }
    
    // DEACTIVATE THANK
    @Path("/accounts/{uid}/thanks/{tid}/deactivate")
    @GET
    public Response deactivateThank(@PathParam("uid") String acc_id, @PathParam("tid") String thk_id) {
    	// deactivate thank and return 200 on success
    	try {
    		Thank res = bi.deactivateThank(acc_id, thk_id);
    		String s = gson.toJson(res);
    		return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 404 if the account or ask does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/thanks/"+thk_id+"/deactivate";
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.NOT_FOUND).entity(s).build();
    	}
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/thanks/"+thk_id+"/deactivate";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }

    // UPDATE THANK
    @Path("accounts/{uid}/thanks/{tid}")
    @PUT
    public Response updateThank(@PathParam("uid") String acc_id, @PathParam("tid") String thk_id, String json) {
    	// create new ask from request body
    	Thank new_thank = gson.fromJson(json, Thank.class);
    	
    	// update account and return 204 on success
	    try {
    		bi.updateThank(thk_id, new_thank);
    		return Response.status(Response.Status.NO_CONTENT).build();
	    }
	    // return 404 if the account does not exist
	    catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/thanks/"+thk_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	    	return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
	    // return 400 if request is not valid
	    catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/thanks/"+thk_id;
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	    }
    }

    // DELETE THANK
    @Path("accounts/{uid}/thanks/{tid}")
    @DELETE
    public Response deleteThank(@PathParam("uid") String acc_id, @PathParam("tid") String thk_id) {
        // delete account and return 204 on success
    	try {
    		bi.deleteThank(acc_id, thk_id);
    	    return Response.status(Response.Status.NO_CONTENT).build();
    	} 
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/thanks/"+thk_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/asks/"+thk_id;
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }
	

	// VIEW ALL NOTES OR FILTERED SET
    @Path("notes")
	@GET
	public Response viewNotes(@DefaultValue("") @QueryParam("key") String key,
							   @DefaultValue("") @QueryParam("v_by") String uid,
							   @DefaultValue("") @QueryParam("is_active") String is_active,
							   @DefaultValue("01-Jan-2000") @QueryParam("start_date") String start,
							   @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {
    	// if no keyword provided, display all accounts according to criteria and return 200 on success
    	if (key.equals("")) {
    		String s;
    		if (!uid.equals("")) {
    			List<Note> subset = bi.viewAllNotesViewedBy(uid);
    			Iterator<Note> note_iter = subset.listIterator();
    			// filter by is_active parameter
    			while (note_iter.hasNext()) {
    				Note n = note_iter.next();
    				if (is_active.equals("true") && !n.getActiveStatus()) {
    					subset.remove(n);
    				}
    				if (is_active.equals("false") && n.getActiveStatus()) {
    					subset.remove(n);
    				}
    			}
    			s = gson.toJson(subset);
    		}
    		else {
    			s = gson.toJson(bi.viewAllNotes());
    		}
            return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// display all accounts within criteria and return 200 on success
     	try {
         	String s = gson.toJson(bi.searchNotes(key, start, end));
         	return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// return 400 if request is not valid
     	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/notes";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
     		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
     	}
	}
 
    // VIEW SPECIFIC NOTE
    @Path("notes/{nid}")
    @GET
    public Response viewNote(@PathParam("nid") String not_id) {
    	// display a single note and return 200 on success
    	try {
            Note n = bi.viewNote(not_id);
            String s = gson.toJson(n);
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if note does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/notes/"+not_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    }
    
    // VIEW MY NOTES
    @Path("/accounts/{uid}/notes")
    @GET
    public Response viewMyNotes(@PathParam("uid") String acc_id,
    						     @DefaultValue("") @QueryParam("is_active") String is_active) {
    	// collect all notes for a user and return 200 on success
	  	try {
	  		String s;
	  		switch (is_active) {
	  			case "":
	  				s = gson.toJson(bi.viewAllMyNotes(acc_id));
	  				break;
	  			case "true":
	  				s = gson.toJson(bi.viewMyNotes(acc_id, true));
	  				break;
	  			case "false":
	  				s = gson.toJson(bi.viewMyNotes(acc_id, false));
	  				break;
	  			default:
	  				throw new AssertionError("Invalid value for parameter \'is_active\'.");	  				
	  		}
	  		return Response.status(Response.Status.OK).entity(s).build();
	  	}
	  	// return 404 if the account does not exist
	  	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/notes";
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	        return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
	  	// return 400 if request is not valid
	  	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/notes";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	  		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	  	}
    }
    
    // CREATE NEW NOTE
    @Path("notes")
    @POST
    public Response createNote(@Context UriInfo uri_info, String json) {
        Gson gs = new Gson();
        Note raw_note = gs.fromJson(json, Note.class);
     
        // create an account and return 201 on success
        try {
        	Note n = bi.createNote(raw_note);
        	String id = n.getID();
            String s = gson.toJson(n);
            // Build the URI for the "Location:" header
            UriBuilder builder = uri_info.getAbsolutePathBuilder();
            builder.path(id.toString());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
        // return 400 if request is not valid
        catch (AssertionError e) {
        	err_msg = e.getMessage();
    		err_inst = "/notes";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
        	return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
        }
    }
    
    // DEACTIVATE NOTE
    @Path("/accounts/{uid}/notes/{nid}/deactivate")
    @GET
    public Response deactivateNote(@PathParam("uid") String acc_id, @PathParam("nid") String not_id) {
    	// deactivate note and return 200 on success
    	try {
    		Note res = bi.deactivateNote(acc_id, not_id);
    		String s = gson.toJson(res);
    		return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 404 if the account or ask does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/notes/"+not_id+"/deactivate";
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.NOT_FOUND).entity(s).build();
    	}
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/notes/"+not_id+"/deactivate";
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }

    // UPDATE NOTE
    @Path("notes/{nid}")
    @PUT
    public Response updateNote(@PathParam("uid") String acc_id, @PathParam("nid") String not_id, String json) {
    	// create new ask from request body
    	Note new_note = gson.fromJson(json, Note.class);
    	
    	// update account and return 204 on success
	    try {
    		bi.updateNote(not_id, new_note);
    		return Response.status(Response.Status.NO_CONTENT).build();
	    }
	    // return 404 if the account does not exist
	    catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/notes/"+not_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
	    	return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	    }
	    // return 400 if request is not valid
	    catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/notes/"+not_id;
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	    }
    }

    // DELETE NOTE
    @Path("accounts/{uid}/notes/{nid}")
    @DELETE
    public Response deleteNote(@PathParam("uid") String acc_id, @PathParam("nid") String not_id) {
        // delete account and return 204 on success
    	try {
    		bi.deleteNote(acc_id, not_id);
    	    return Response.status(Response.Status.NO_CONTENT).build();
    	} 
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/notes/"+not_id;
    		err_status = 404;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
            return Response.status(Response.Status.NOT_FOUND).entity(s).build();
        }
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		err_msg = e.getMessage();
    		err_inst = "/accounts/"+acc_id+"/asks/"+not_id;
    		err_status = 400;
    		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
    		String s = gson.toJson(err);
    		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
    	}
    }
}
