package edu.iit.cs445.spring2022.buynothing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class GiveManager {
	private static final String[] TYPES = {"gift", "service", "lend", "share"};
	private static List<Give> allGives = new ArrayList<Give>();
	
	public Give createGive(Give g) {
		checkMissingInfo(g);
		checkType(g);
		Give newGive = new Give(g);
		allGives.add(newGive);
		g.activate();
		return newGive;
	}
	
	public Give deactivateGive(String gid) {
		Give g = findByID(gid);
		if (g.isNil()) throw new NoSuchElementException();
		g.deactivate();
    	allGives.remove(g);
    	return g;
	}
	
    public void updateGive(String old_id, Give gnew) {
    	Give gold = findByID(old_id);
    	if (gold.isNil() || !gold.getActiveStatus()) throw new NoSuchElementException();
    	checkMissingInfo(gnew);
    	checkType(gnew);
    	gold.updateAccountID(gnew.getAccountID());
    	gold.updateType(gnew.getType());
    	gold.updateDescription(gnew.getDescription());
    	SimpleDateFormat fmt = new SimpleDateFormat("YYYY-MM-DD");
    	gold.updateStartDate(fmt.format(gnew.getStartDate()));
    	gold.updateEndDate(fmt.format(gnew.getEndDate()));
    	gold.updateExtraZip(gnew.getExtraZip());
    }
    
    public void deleteGive(String gid) {
    	Give g = findByID(gid);
    	if (g.isNil()) throw new NoSuchElementException();
    	allGives.remove(g);
    }
    
    public List<String> deleteByUID(String uid) {
    	Iterator<Give> give_iter = allGives.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (give_iter.hasNext()) {
    		Give g = give_iter.next();
    		if (g.getAccountID().equals(uid)) {
    			deleteGive(g.getID());
        		deleted.add(g.getID());
    		}
    	}
    	return deleted;
    }
    
    public List<Give> viewAllGives() {
    	return allGives;
    }
    
    public List<Give> viewMyGives(String uid, boolean isActive) {
    	List<Give> myGives = new ArrayList<Give>();
    	Iterator<Give> give_iter = allGives.listIterator();
    	while (give_iter.hasNext()) {
    		Give g = give_iter.next();
    		if (g.getAccountID().equals(uid) && g.getActiveStatus()==isActive) {
    			myGives.add(g);
    		}
    	}
    	return myGives;
    }
    
    public Give viewGive(String gid) {
    	Give g = findByID(gid);
    	if (g.isNil()) throw new NoSuchElementException();
    	return g;
    }
    
    public List<Give> searchGives(String key, String start_date, String end_date) {
    	if (key.equals(null)) return allGives;
    	List<Give> filteredGives = new ArrayList<Give>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		Iterator<Give> give_iter = allGives.listIterator();
        	while (give_iter.hasNext()) {
        		Give g = give_iter.next();
        		if (g.checkForKeyword(key)) {
        			Date created = g.getDateCreated();
        			if (!created.after(end) && !created.before(start)) {
        				filteredGives.add(g);
        			}
        		}
        	}
        	return filteredGives;
    	}
    	// Date cannot be parsed
    	catch (Exception e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    public Give findByID(String gid) {
    	Iterator<Give> give_iter = allGives.listIterator();
    	while (give_iter.hasNext()) {
    		Give g = give_iter.next();
    		if (g.matchesID(gid)) return g;
    	}
    	return (new NullGive());
    }
    
    public void checkType(Give g) {
    	String myType = g.getType();
    	for (String type : TYPES) {
    		if (myType.equals(type)) return;
    	}
    	// specified type is not a valid identifier
    	throw new IllegalArgumentException();
    }
    
    public void checkMissingInfo(Give g) {
    	if (g.getAccountID().equals(null) || 
    		g.getType().equals(null) ||
    		g.getDescription().equals(null) || 
    		g.getStartDate().equals(null)) {
    			throw new AssertionError();
    	}
    }
    
    public String assessMissingInfo(String gid) {
    	Give g = findByID(gid);
		if (g.getAccountID().equals(null)) return "Account ID is missing!";
		if (g.getType().equals(null)) return "Type is missing!";
		if (g.getDescription().equals(null)) return "Description is missing!";
		if (g.getStartDate().equals(null)) return "Start date is missing!";
		return "Something went wrong.";
    }
}
