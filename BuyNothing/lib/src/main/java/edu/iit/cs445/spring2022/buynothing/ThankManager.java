package edu.iit.cs445.spring2022.buynothing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ThankManager {
	private static List<Thank> allThanks = new ArrayList<Thank>();
	
	public Thank createThank(Thank t) {
		checkMissingInfo(t);
		Thank newThank = new Thank(t);
		allThanks.add(newThank);
		t.activate();
		return newThank;
	}
	
	public Thank deactivateThank(String tid) {
		Thank t = findByID(tid);
		if (t.isNil()) throw new NoSuchElementException();
		t.deactivate();
    	allThanks.remove(t);
    	return t;
	}
	
    public void updateThank(String old_id, Thank tnew) {
    	Thank told = findByID(old_id);
    	if (told.isNil() || !told.getActiveStatus()) throw new NoSuchElementException();
    	checkMissingInfo(tnew);
    	told.updateAccountID(tnew.getAccountID());
    	told.updateThankTo(tnew.getThankTo());
    	told.updateDescription(tnew.getDescription());
    }
    
    public void deleteThank(String tid) {
    	Thank t = findByID(tid);
    	if (t.isNil()) throw new NoSuchElementException();
    	allThanks.remove(t);
    }
    
    public List<String> deleteByUID(String uid) {
    	Iterator<Thank> thank_iter = allThanks.listIterator();
    	List<String> deleted = new ArrayList<String>();
    	while (thank_iter.hasNext()) {
    		Thank t = thank_iter.next();
    		if (t.getAccountID().equals(uid)) {
    			deleteThank(t.getID());
        		deleted.add(t.getID());
    		}
    	}
    	return deleted;
    }
    
    public List<Thank> viewAllThanks() {
    	return allThanks;
    }
    
    public List<Thank> viewMyThanks(String uid, boolean isActive) {
    	List<Thank> myThanks = new ArrayList<Thank>();
    	Iterator<Thank> thank_iter = allThanks.listIterator();
    	while (thank_iter.hasNext()) {
    		Thank t = thank_iter.next();
    		if (t.getAccountID().equals(uid) && t.getActiveStatus()==isActive) {
    			myThanks.add(t);
    		}
    	}
    	return myThanks;
    }
    
    public Thank viewThank(String tid) {
    	Thank t = findByID(tid);
    	if (t.isNil()) throw new NoSuchElementException();
    	return t;
    }
    
    public List<Thank> searchThanks(String key, String start_date, String end_date) {
    	if (key.equals(null)) return allThanks;
    	List<Thank> filteredThanks = new ArrayList<Thank>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		Iterator<Thank> thank_iter = allThanks.listIterator();
        	while (thank_iter.hasNext()) {
        		Thank t = thank_iter.next();
        		if (t.checkForKeyword(key)) {
        			Date created = t.getDateCreated();
        			if (!created.after(end) && !created.before(start)) {
        				filteredThanks.add(t);
        			}
        		}
        	}
        	return filteredThanks;
    	}
    	// Date cannot be parsed
    	catch (Exception e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    public Thank findByID(String tid) {
    	Iterator<Thank> thank_iter = allThanks.listIterator();
    	while (thank_iter.hasNext()) {
    		Thank t = thank_iter.next();
    		if (t.matchesID(tid)) return (t);
    	}
    	return (new NullThank());
    }
    
    public void checkMissingInfo(Thank t) {
    	if (t.getAccountID().equals(null) || 
    		t.getThankTo().equals(null) ||
    		t.getDescription().equals(null)) {
    			throw new AssertionError();
    	}
    }
    
    public String assessMissingInfo(String tid) {
    	Thank t = findByID(tid);
		if (t.getAccountID().equals(null)) return "Account ID is missing!";
		if (t.getThankTo().equals(null)) return "Thank To field is missing!";
		if (t.getDescription().equals(null)) return "Description is missing!";
		return "Something went wrong.";
    }
}
