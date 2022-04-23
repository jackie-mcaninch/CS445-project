package edu.iit.cs445.spring2022.buynothing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


public class AccountManager {
	private static List<Account> allAccounts = new ArrayList<Account>();

	public Account createAccount(Account a) {
		checkMissingInfo(a);
		Account newAccount = new Account(a);
		allAccounts.add(newAccount);
		return newAccount;
	}
	
	public void activateAccount(String acc_id) {
		Account a = findByID(acc_id);
		if (a.isNil()) throw new NoSuchElementException();
		checkMissingInfo(a);
		a.activate();
	}
	
    public void updateAccount(String old_id, Account anew) {
    	Account aold = findByID(old_id);
    	if (aold.isNil()) throw new NoSuchElementException();
    	checkMissingInfo(anew);
    	aold.updateName(anew.getName());
    	aold.updateAddress(anew.getStreet(), anew.getZip());
    	aold.updatePhone(anew.getPhone());
    	aold.updatePicture(anew.getPicture());
    }
    
    public void deleteAccount(String acc_id) {
    	Account a = findByID(acc_id);
    	if (a.isNil()) throw new NoSuchElementException();
		a.deactivate();
		allAccounts.remove(a);
    }
    
    public List<Account> viewAllAccounts() {
    	return allAccounts;
    }
    
    public Account viewAccount(String acc_id) {
    	Account a = findByID(acc_id);
    	if (a.isNil()) throw new NoSuchElementException();
    	return a;
    }
    
    public List<Account> searchAccounts(String key, String start_date, String end_date) {
    	if (key.equals(null)) return allAccounts;
    	List<Account> filteredAccounts = new ArrayList<Account>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		if (!start.before(end)) throw new AssertionError();
    		Iterator<Account> acc_iter = allAccounts.listIterator();
        	while (acc_iter.hasNext()) {
        		Account a = acc_iter.next();
        		if (a.checkForKeyword(key)) {
        			Date created = a.getDateCreated();
        			if (!created.after(end) && !created.before(start)) {
        				filteredAccounts.add(a);
        			}
        		}
        	}
        	return filteredAccounts;
    	}
    	catch (Exception e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    public Account findByID(String acc_id) {
    	Iterator<Account> acc_iter = allAccounts.listIterator();
    	while (acc_iter.hasNext()) {
    		Account a = acc_iter.next();
    		if (a.matchesID(acc_id)) return (a);
    	}
    	return (new NullAccount());
    }
    
    public void checkMissingInfo(Account a) {
    	if (a.getName().equals(null) || 
    		a.getStreet().equals(null) ||
    		a.getZip().equals(null) || 
    		a.getPhone().equals(null) || 
    		a.getPicture().equals(null)) {
    			throw new AssertionError();
    		}
    }
    
    public String assessMissingInfo(String acc_id) {
    	Account a = findByID(acc_id);
		if (a.getName().equals(null)) return "Name is missing!";
		if (a.getStreet().equals(null)) return "Street is missing!";
		if (a.getZip().equals(null)) return "Zip code is missing!";
		if (a.getPhone().equals(null)) return "Phone number is missing!";
		if (a.getPicture().equals(null)) return "Picture is missing!";
		return "Something went wrong.";
    }
}