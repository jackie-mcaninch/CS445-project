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
							     @DefaultValue("01-Jan-1970") @QueryParam("start_date") String start,
							     @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {		
    	try {
			// if no keyword provided, display all accounts and return 200 on success
			if (key == null) {
				String s = gson.toJson(bi.viewAllAccounts());
				return Response.status(Response.Status.OK).entity(s).build();
			}
        	// display all accounts within search criteria and return 200 on success
    		else {
				String s = gson.toJson(bi.searchAccounts(key, start, end));
				return Response.status(Response.Status.OK).entity(s).build();
			}
    	}
     	// return 400 if request is not valid
    	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts");
    	}
    }
	
    // VIEW SPECIFIC ACCOUNT
    @Path("accounts/{uid}")
    @GET
    public Response viewAccount(@PathParam("uid") String acc_id) {    	
    	// display a single account and return 200 on success
    	try {
            String s = gson.toJson(bi.viewAccount(acc_id));
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if account does not exist
    	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id);
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
            String s = gson.toJson(a);
            // Build the URI for the "Location:" header
            UriBuilder builder = uri_info.getAbsolutePathBuilder();
            builder.path(a.getID());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
     	// return 400 if request is not valid
        catch (AssertionError e) {
			return badRequestHandler(e.getMessage(), "/accounts");
        }
    }

    // ACTIVATE ACCOUNT
    @Path("accounts/{uid}/activate")
    @GET
    public Response activateAccount(@PathParam("uid") String acc_id) {
    	// activate account and return 200 on success
    	try {
    		String s = gson.toJson(bi.activateAccount(acc_id));
        	return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
			return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"activate");
    	}
     	// return 400 if request is not valid
    	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"activate");
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
			return notFoundHandler(e.getMessage(), "/accounts/"+acc_id);
	    }
     	// return 400 if request is not valid
	    catch (AssertionError e) {
			return badRequestHandler(e.getMessage(), "/accounts/"+acc_id);
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
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id);
        }
    }


    // VIEW ALL ASKS OR FILTERED SET
    @Path("asks")
	@GET
	public Response viewAsks(@DefaultValue("") @QueryParam("key") String key,
							 @DefaultValue("") @QueryParam("v_by") String uid,
							 @DefaultValue("") @QueryParam("is_active") String is_active,
 							 @DefaultValue("01-Jan-1970") @QueryParam("start_date") String start,
 							 @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {
     	try {
			String s;
			// if no keyword provided, display all accounts according to criteria and return 200 on success
			if (key.equals("")) {
				s = gson.toJson(bi.viewAsks(uid, is_active));
			}
			// otherwise, display all accounts within search criteria and return 200 on success
			else {
				s = gson.toJson(bi.searchAsks(key, start, end));
			}
         	return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// return 400 if request is not valid
     	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/asks");
     	}
	}
 
    // VIEW SPECIFIC ASK
    @Path("asks/{aid}")
    @GET
    public Response viewAsk(@PathParam("aid") String ask_id) {
    	// display a single ask and return 200 on success
    	try {
            String s = gson.toJson(bi.viewAsk(ask_id));
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if ask does not exist
    	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/asks/"+ask_id);
        }
    }
    
    // VIEW MY ASKS
    @Path("/accounts/{uid}/asks")
    @GET
    public Response viewMyAsks(@PathParam("uid") String acc_id,
    						   @DefaultValue("") @QueryParam("is_active") String is_active) {
    	// collect all asks for a user and return 200 on success
	  	try {
			String s = gson.toJson(bi.viewMyAsks(acc_id, is_active));
	  		return Response.status(Response.Status.OK).entity(s).build();
	  	}
	  	// return 404 if the account does not exist
	  	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/asks");
	    }
	  	// return 400 if request is not valid
	  	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/asks");
	  	}
    }
    
    // CREATE NEW ASK
    @Path("accounts/{uid}/asks")
    @POST
    public Response createAsk(@PathParam("uid") String acc_id, @Context UriInfo uri_info, String json) {
        // create raw ask from request body
        Ask raw_ask = gson.fromJson(json, Ask.class);
     
        // create an account and return 201 on success
        try {
        	Ask a = bi.createAsk(acc_id, raw_ask);
            String s = gson.toJson(a);
            // Build the URI for the "Location:" header
            UriBuilder builder = uri_info.getAbsolutePathBuilder();
            builder.path(a.getID());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
        // return 400 if request is not valid
        catch (AssertionError e) {
			return badRequestHandler(e.getMessage(), "/accounts/"+acc_id);
        }
    }
    
    // DEACTIVATE ASK
    @Path("/accounts/{uid}/asks/{aid}/deactivate")
    @GET
    public Response deactivateAsk(@PathParam("uid") String acc_id, @PathParam("aid") String ask_id) {
    	// deactivate ask and return 200 on success
    	try {
    		String s = gson.toJson(bi.deactivateAsk(acc_id, ask_id));
    		return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 404 if the account or ask does not exist
    	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/asks/"+ask_id+"/deactivate");
    	}
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/asks/"+ask_id+"/deactivate");
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
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/asks/"+ask_id);
	    }
	    // return 400 if request is not valid
	    catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/asks/"+ask_id);
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
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/asks/"+ask_id);
        }
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/asks/"+ask_id);
    	}
    }
    

    // VIEW ALL GIVES OR FILTERED SET
    @Path("gives")
	@GET
	public Response viewGives(@DefaultValue("") @QueryParam("key") String key,
							  @DefaultValue("") @QueryParam("v_by") String uid,
							  @DefaultValue("") @QueryParam("is_active") String is_active,
 							  @DefaultValue("01-Jan-1970") @QueryParam("start_date") String start,
 							  @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {
     	try {
			String s;
			// if no keyword provided, display all accounts according to criteria and return 200 on success
			if (key.equals("")) {
				s = gson.toJson(bi.viewGives(uid, is_active));
			}
			// otherwise display all accounts within search criteria and return 200 on success
			else {
				s = gson.toJson(bi.searchGives(key, start, end));
			}
         	return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// return 400 if request is not valid
     	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/gives");
     	}
	}
 
    // VIEW SPECIFIC GIVE
    @Path("gives/{gid}")
    @GET
    public Response viewGive(@PathParam("gid") String giv_id) {
    	// display a single give and return 200 on success
    	try {
            String s = gson.toJson(bi.viewGive(giv_id));
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if give does not exist
    	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/gives/"+giv_id);
        }
    }
    
    // VIEW MY GIVES
    @Path("/accounts/{uid}/gives")
    @GET
    public Response viewMyGives(@PathParam("uid") String acc_id,
    						    @DefaultValue("") @QueryParam("is_active") String is_active) {
    	// collect all gives for a user and return 200 on success
	  	try {
	  		String s = gson.toJson(bi.viewMyGives(acc_id, is_active)); 				
	  		return Response.status(Response.Status.OK).entity(s).build();
	  	}
	  	// return 404 if the account does not exist
	  	catch (NoSuchElementException e) {
			return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/gives");
    	}
	  	// return 400 if request is not valid
	  	catch (AssertionError e) {
			return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/gives");
	  	}
    }
    
    // CREATE NEW GIVE
    @Path("accounts/{uid}/gives")
    @POST
    public Response createGive(@PathParam("uid") String acc_id, @Context UriInfo uri_info, String json) {
        // create raw give from request body
        Give raw_give = gson.fromJson(json, Give.class);
     
        // create an account and return 201 on success
        try {
        	Give g = bi.createGive(acc_id, raw_give);
            String s = gson.toJson(g);
            // Build the URI for the "Location:" header
            UriBuilder builder = uri_info.getAbsolutePathBuilder();
            builder.path(g.getID());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
        // return 400 if request is not valid
        catch (AssertionError e) {
			return badRequestHandler(e.getMessage(), "/accounts/"+acc_id);
        }
    }
    
    // DEACTIVATE GIVE
    @Path("/accounts/{uid}/gives/{gid}/deactivate")
    @GET
    public Response deactivateGive(@PathParam("uid") String acc_id, @PathParam("gid") String giv_id) {
    	// deactivate give and return 200 on success
    	try {
    		String s = gson.toJson(bi.deactivateGive(acc_id, giv_id));
    		return Response.status(Response.Status.OK).entity(s).build();
    	}
    	// return 404 if the account or ask does not exist
    	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/gives/"+giv_id+"/deactivate");
    	}
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/gives/"+giv_id+"/deactivate");
    	}
    }

    // UPDATE GIVE
    @Path("accounts/{uid}/gives/{gid}")
    @PUT
    public Response updateGive(@PathParam("uid") String acc_id, @PathParam("gid") String giv_id, String json) {
    	// create new give from request body
    	Give new_give = gson.fromJson(json, Give.class);
    	
    	// update account and return 204 on success
	    try {
    		bi.updateGive(giv_id, new_give);
    		return Response.status(Response.Status.NO_CONTENT).build();
	    }
	    // return 404 if the account does not exist
	    catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/gives/"+giv_id);
	    }
	    // return 400 if request is not valid
	    catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/gives/"+giv_id);
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
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/gives/"+giv_id);
        }
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/gives/"+giv_id);
    	}
    }


	// VIEW ALL THANKS OR FILTERED SET
    @Path("thanks")
	@GET
	public Response viewThanks(@DefaultValue("") @QueryParam("key") String key,
							   @DefaultValue("") @QueryParam("v_by") String uid,
							   @DefaultValue("") @QueryParam("is_active") String is_active,
							   @DefaultValue("01-Jan-1970") @QueryParam("start_date") String start,
							   @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {
     	try {
			String s;
			// if no keyword provided, display all accounts according to criteria and return 200 on success
			if (key.equals("")) {
				s = gson.toJson(bi.viewThanks(uid, is_active));
			}
			// display all accounts within criteria and return 200 on success
			else {
				s = gson.toJson(bi.searchThanks(key, start, end));
			}
         	return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// return 400 if request is not valid
     	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/thanks");
     	}
	}
 
    // VIEW SPECIFIC THANK
    @Path("thanks/{tid}")
    @GET
    public Response viewThank(@PathParam("tid") String thk_id) {
    	// display a single thank and return 200 on success
    	try {
            String s = gson.toJson(bi.viewThank(thk_id));
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if thank does not exist
    	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/thanks/"+thk_id);
        }
    }
    
    // VIEW MY THANKS
    @Path("/accounts/{uid}/thanks")
    @GET
    public Response viewMyThanks(@PathParam("uid") String acc_id,
    						     @DefaultValue("") @QueryParam("is_active") String is_active) {
    	// collect all thanks for a user and return 200 on success
	  	try {
	  		String s = gson.toJson(bi.viewMyThanks(acc_id, is_active));
	  		return Response.status(Response.Status.OK).entity(s).build();
	  	}
	  	// return 404 if the account does not exist
	  	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/thanks");
	    }
	  	// return 400 if request is not valid
	  	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/thanks");
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
    		return notFoundHandler(e.getMessage(), "/thanks/received/"+acc_id);
		}
		// return 400 if request is not valid
		catch (AssertionError e) {
			return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/thanks");
		}
	}
    
    // CREATE NEW THANK
    @Path("accounts/{uid}/thanks")
    @POST
    public Response createThank(@PathParam("uid") String acc_id, @Context UriInfo uri_info, String json) {
        // create raw thank from request body
        Thank raw_thank = gson.fromJson(json, Thank.class);
     
        // create an account and return 201 on success
        try {
        	Thank t = bi.createThank(acc_id, raw_thank);
            String s = gson.toJson(t);
            // Build the URI for the "Location:" header
            UriBuilder builder = uri_info.getAbsolutePathBuilder();
            builder.path(t.getID());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
        // return 400 if request is not valid
        catch (AssertionError e) {
			return badRequestHandler(e.getMessage(), "/accounts/"+acc_id);
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
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/thanks/"+thk_id);
	    }
	    // return 400 if request is not valid
	    catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/thanks/"+thk_id);
	    }
    }
	

	// VIEW ALL NOTES OR FILTERED SET
    @Path("notes")
	@GET
	public Response viewNotes(@DefaultValue("") @QueryParam("key") String key,
							  @DefaultValue("") @QueryParam("c_by") String created_by_id,
							  @DefaultValue("") @QueryParam("v_by") String uid,
							  @DefaultValue("") @QueryParam("type") String type,
							  @DefaultValue("") @QueryParam("agid") String agid,
							  @DefaultValue("01-Jan-1970") @QueryParam("start_date") String start,
							  @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {
     	try {
			String s;
			// if no keyword provided, display all accounts according to criteria and return 200 on success
			if (key.equals("")) {
				s = bi.viewNotes(created_by_id, uid, type, agid);
			}
			// display all notes within criteria and return 200 on success
			else {
				s = gson.toJson(bi.searchNotes(key, start, end));
			}
         	return Response.status(Response.Status.OK).entity(s).build();
     	}
     	// return 400 if request is not valid
     	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/notes");
     	}
	}
 
    // VIEW SPECIFIC NOTE
    @Path("notes/{nid}")
    @GET
    public Response viewNote(@PathParam("nid") String not_id) {
    	// display a single note and return 200 on success
    	try {
            String s = gson.toJson(bi.viewNote(not_id));
            return Response.status(Response.Status.OK).entity(s).build();
    	} 
    	// return 404 if note does not exist
    	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/notes/"+not_id);
        }
    }
    
    // CREATE NEW NOTE
    @Path("notes")
    @POST
    public Response createNote(@Context UriInfo uri_info, String json) {
        // create raw note from request body
        Note raw_note = gson.fromJson(json, Note.class);
     
        // create an account and return 201 on success
        try {
        	Note n = bi.createNote(raw_note);
            String s = gson.toJson(n);
            // Build the URI for the "Location:" header
            UriBuilder builder = uri_info.getAbsolutePathBuilder();
            builder.path(n.getID());

            // The response includes header and body data
            return Response.created(builder.build()).entity(s).build();
        }
        // return 400 if request is not valid
        catch (AssertionError e) {
			return badRequestHandler(e.getMessage(), "/notes");
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
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/notes/"+not_id);
	    }
	    // return 400 if request is not valid
	    catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/notes/"+not_id);
	    }
    }

    // DELETE NOTE
    @Path("notes/{nid}")
    @DELETE
    public Response deleteNote(@PathParam("uid") String acc_id, @PathParam("nid") String not_id) {
        // delete account and return 204 on success
    	try {
    		bi.deleteNote(acc_id, not_id);
    	    return Response.status(Response.Status.NO_CONTENT).build();
    	} 
    	// return 404 if the account does not exist
    	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/accounts/"+acc_id+"/notes/"+not_id);
        }
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/accounts/"+acc_id+"/asks/"+not_id);
    	}
    }


	// VIEW ALL REPORTS
	@Path("reports")
	@GET
	public Response viewAllReports() {
		String s = gson.toJson(bi.viewAllReports());
		return Response.status(Response.Status.OK).entity(s).build();
	}

	// VIEW FILTERED REPORTS
	@Path("reports/{rid}")
	@GET
	public Response viewReport(@PathParam("rid") String rep_id,
							   @DefaultValue("") @QueryParam("c_by") String created_by_id,
							   @DefaultValue("") @QueryParam("v_by") String viewed_by_id,
							   @DefaultValue("01-Jan-1970") @QueryParam("start_date") String start,
							   @DefaultValue("01-Jan-2100") @QueryParam("end_date") String end) {
		try {
			// display report generated from user criteria
			String s = gson.toJson(bi.generateReport(rep_id, created_by_id, viewed_by_id, start, end));
			return Response.status(Response.Status.OK).entity(s).build();
		}
		// return 404 if the rid, c_by_id, or v_by_id does not exist
    	catch (NoSuchElementException e) {
    		return notFoundHandler(e.getMessage(), "/reports/"+rep_id);
        }
    	// return 400 if request is not valid
    	catch (AssertionError e) {
    		return badRequestHandler(e.getMessage(), "/reports/"+rep_id);
    	}
	}


	// Error handler for NoSuchElementException
	public Response notFoundHandler(String msg, String inst) {
		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
		String err_title = "Your request data didn't pass validation";
		String err_msg = msg;
		String err_inst = inst;
		int err_status = 404;
		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
		String s = gson.toJson(err);
		return Response.status(Response.Status.NOT_FOUND).entity(s).build();
	}

	// Error handler for AssertionError
	public Response badRequestHandler(String msg, String inst) {
		String err_type = "http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/api/problems/data-validation";
		String err_title = "Your request data didn't pass validation";
		String err_msg = msg;
		String err_inst = inst;
		int err_status = 400;
		ErrorResponse err = new ErrorResponse(err_type, err_title, err_msg, err_inst, err_status);
		String s = gson.toJson(err);
		return Response.status(Response.Status.BAD_REQUEST).entity(s).build();
	}
}
