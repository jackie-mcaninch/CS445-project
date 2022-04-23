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
		Ask newAsk = new Ask(a);
		allAsks.add(newAsk);
		return newAsk;
	}
	
	public int activateAsk(String acc_id) {
		Ask a = findByID(acc_id);
		int err_code = checkData(a);
		if (err_code != 0) return err_code;
		a.activate();
		return 0;
	}
	
    public int replaceAsk(String old_id, Ask anew) {
    	Ask aold = findByID(old_id);
    	int err_code = checkData(anew);
    	if (err_code != 0) return err_code;
//    	aold.updateName(anew.getName());
//    	aold.updateAddress(anew.getStreet(), anew.getZip());
//    	aold.updatePhone(anew.getPhone());
//    	aold.updatePicture(anew.getPicture());
    	return 0;
    }
    
    public void deleteAsk(String acc_id) {
    	Ask a = findByID(acc_id);
    	if (a.isNil()) {
    		throw new NoSuchElementException();
    	}
    	else {
    		allAsks.remove(a);
    	}
    }
    
    public void deleteByID(String acc_id) {
    	
    }
    
    public List<Ask> viewAllAsks() {
    	return allAsks;
    }
    
    public Ask viewAsk(String acc_id) {
    	Ask a = findByID(acc_id);
    	if (a.isNil()) {
    		throw new NoSuchElementException();
    	}
    	return a;
    }
    
    public List<Ask> searchAsks(String key, String start_date, String end_date) {
    	if (key.equals(null)) return allAsks;
    	List<Ask> filteredAsks = new ArrayList<Ask>();
    	try {
    		Date start = new SimpleDateFormat("DD-MM-YYYY").parse(start_date);
    		Date end = new SimpleDateFormat("DD-MM-YYYY").parse(end_date);
    		Iterator<Ask> acc_iter = allAsks.listIterator();
        	while (acc_iter.hasNext()) {
        		Ask a = acc_iter.next();
        		if (a.checkForKeyword(key)) {
        			Date created = a.getDateCreated();
        			if (!created.after(end) && !created.before(start)) {
        				filteredAsks.add(a);
        			}
        		}
        	}
        	return filteredAsks;
    	}
    	catch (Exception e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    public Ask findByID(String acc_id) {
    	Iterator<Ask> acc_iter = allAsks.listIterator();
    	while (acc_iter.hasNext()) {
    		Ask a = acc_iter.next();
    		if (a.matchesID(acc_id)) return (a);
    	}
    	return (new NullAsk());
    }
    
    public int checkData(Ask a) {
    	if (a.isNil()) {
			throw new NoSuchElementException();
		}
//		if (a.getName().equals(null)) return -1;
//		if (a.getStreet().equals(null)) return -2;
//		if (a.getZip().equals(null)) return -3;
//		if (a.getPhone().equals(null)) return -4;
//		if (a.getPicture().equals(null)) return -5;
		return 0;
    }

}
