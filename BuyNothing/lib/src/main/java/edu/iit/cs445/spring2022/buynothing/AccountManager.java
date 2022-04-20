package edu.iit.cs445.spring2022.buynothing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


public class AccountManager {
	private static List<Account> allAccounts = new ArrayList<Account>();
	
	public List<Account> getAllAccounts() {
		return allAccounts;
	}
	
	public Account createAccount(Account a) {
		Account newAccount = new Account(a);
		allAccounts.add(newAccount);
		return newAccount;
	}
	
	public int activateAccount(String acc_id) {
		Account a = findByID(acc_id);
		int err_code = checkData(a);
		if (err_code != 0) return err_code;
		a.activate();
		return 0;
	}
	
    public int replaceAccount(String old_id, Account anew) {
    	Account aold = findByID(old_id);
    	int err_code = checkData(anew);
    	if (err_code != 0) return err_code;
    	aold.updateName(anew.getName());
    	aold.updateAddress(anew.getStreet(), anew.getZip());
    	aold.updatePhone(anew.getPhone());
    	aold.updatePicture(anew.getPicture());
    	return 0;
    }
    
    public void deleteAccount(String acc_id) {
    	Account a = findByID(acc_id);
    	if (a.isNil()) {
    		throw new NoSuchElementException();
    	}
    	else {
    		allAccounts.remove(a);
    	}
    }
    
    public List<Account> viewAllAccounts() {
    	return allAccounts;
    }
    
    public Account viewAccount(String acc_id) {
    	Account a = findByID(acc_id);
    	if (a.isNil()) {
    		throw new NoSuchElementException();
    	}
    	return a;
    }
    
    public List<Account> searchAccounts(String key, String start_date, String end_date) {
    	if (key.equals(null)) return allAccounts;
    	List<Account> filteredAccounts = new ArrayList<Account>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
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
    
    public int checkData(Account a) {
    	if (a.isNil()) {
			throw new NoSuchElementException();
		}
		if (a.getName().equals(null)) return -1;
		if (a.getStreet().equals(null)) return -2;
		if (a.getZip().equals(null)) return -3;
		if (a.getPhone().equals(null)) return -4;
		if (a.getPicture().equals(null)) return -5;
		return 0;
    }
}