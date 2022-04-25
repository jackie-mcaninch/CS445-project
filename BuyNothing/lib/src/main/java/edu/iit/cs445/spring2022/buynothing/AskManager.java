package edu.iit.cs445.spring2022.buynothing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class AskManager {
	private static final String[] TYPES = {"gift", "borrow", "help"};
	private static List<Ask> allAsks = new ArrayList<Ask>();
	
	public Ask createAsk(Ask a) {
		checkMissingInfo(a);
		checkType(a);
		Ask newAsk = new Ask(a);
		allAsks.add(newAsk);
		a.activate();
		return newAsk;
	}
	
	public Ask deactivateAsk(String aid) {
		Ask a = findByID(aid);
		if (a.isNil()) throw new NoSuchElementException();
		a.deactivate();
    	allAsks.remove(a);
    	return a;
	}
	
    public void updateAsk(String old_id, Ask anew) {
    	Ask aold = findByID(old_id);
    	if (aold.isNil() || !aold.getActiveStatus()) throw new NoSuchElementException();
    	checkMissingInfo(anew);
    	checkType(anew);
    	aold.updateAccountID(anew.getAccountID());
    	aold.updateType(anew.getType());
    	aold.updateDescription(anew.getDescription());
    	SimpleDateFormat fmt = new SimpleDateFormat("YYYY-MM-DD");
    	aold.updateStartDate(fmt.format(anew.getStartDate()));
    	aold.updateEndDate(fmt.format(anew.getEndDate()));
    	aold.updateExtraZip(anew.getExtraZip());
    }
    
    public void deleteAsk(String aid) {
    	Ask a = findByID(aid);
    	if (a.isNil()) throw new NoSuchElementException();
    	allAsks.remove(a);
    }
    
    public List<String> deleteByUID(String uid) {
    	Iterator<Ask> ask_iter = allAsks.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (ask_iter.hasNext()) {
    		Ask a = ask_iter.next();
    		if (a.getAccountID().equals(uid)) {
    			deleteAsk(a.getID());
        		deleted.add(a.getID());
    		}
    	}
    	return deleted;
    }
    
    public List<Ask> viewAllAsks() {
    	return allAsks;
    }
    
    public List<Ask> viewMyAsks(String uid, boolean isActive) {
    	List<Ask> myAsks = new ArrayList<Ask>();
    	Iterator<Ask> ask_iter = allAsks.listIterator();
    	while (ask_iter.hasNext()) {
    		Ask a = ask_iter.next();
    		if (a.getAccountID().equals(uid) && a.getActiveStatus()==isActive) {
    			myAsks.add(a);
    		}
    	}
    	return myAsks;
    }
    
    public Ask viewAsk(String aid) {
    	Ask a = findByID(aid);
    	if (a.isNil()) throw new NoSuchElementException();
    	return a;
    }
    
    public List<Ask> searchAsks(String key, String start_date, String end_date) {
    	if (key.equals(null)) return allAsks;
    	List<Ask> filteredAsks = new ArrayList<Ask>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		Iterator<Ask> ask_iter = allAsks.listIterator();
        	while (ask_iter.hasNext()) {
        		Ask a = ask_iter.next();
        		if (a.checkForKeyword(key)) {
        			Date created = a.getDateCreated();
        			if (!created.after(end) && !created.before(start)) {
        				filteredAsks.add(a);
        			}
        		}
        	}
        	return filteredAsks;
    	}
    	// Date cannot be parsed
    	catch (Exception e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    public Ask findByID(String aid) {
    	Iterator<Ask> ask_iter = allAsks.listIterator();
    	while (ask_iter.hasNext()) {
    		Ask a = ask_iter.next();
    		if (a.matchesID(aid)) return (a);
    	}
    	return (new NullAsk());
    }
    
    public void checkType(Ask a) {
    	String myType = a.getType();
    	for (String type : TYPES) {
    		if (myType.equals(type)) return;
    	}
    	// specified type is not a valid identifier
    	throw new IllegalArgumentException();
    }
    
    public void checkMissingInfo(Ask a) {
    	if (a.getAccountID().equals(null) || 
    		a.getType().equals(null) ||
    		a.getDescription().equals(null) || 
    		a.getStartDate().equals(null)) {
    			throw new AssertionError();
    	}
    }
    
    public String assessMissingInfo(Ask a) {
		if (a.getAccountID().equals(null)) return "Account ID is missing!";
		if (a.getType().equals(null)) return "Type is missing!";
		if (a.getDescription().equals(null)) return "Description is missing!";
		if (a.getStartDate().equals(null)) return "Start date is missing!";
		return "Something went wrong.";
    }
}
