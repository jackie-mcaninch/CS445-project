package edu.iit.cs445.spring2022.buynothing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Thank extends BuyNothingObj {
	private String uid;
	private String tid;
	private String thank_to;
	private String description;
	private List<Account> viewed_by = new ArrayList<Account>();
		
	public Thank() {
		super();
		this.tid = UUID.randomUUID().toString();
	}
	
	public Thank(Thank t) {
		super();
		this.uid = t.getAccountID();
		this.tid = UUID.randomUUID().toString();
		this.thank_to = t.getThankTo();
		this.description = t.getDescription();
	}
	
	public void updateAccountID(String new_uid) {
		this.uid = new_uid;
	}
	
	public void updateThankTo(String new_tt) {
		this.thank_to = new_tt;
	}
	
	public void updateDescription(String new_description) {
		this.description = new_description;
	}
	
	public String getAccountID() {
		return this.uid;
	}
	
	public String getThankTo() {
		return this.thank_to;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getID() {
		return this.tid;
	}
	
	public void view(Account a) {
		if (!this.viewed_by.contains(a)) this.viewed_by.add(a);
	}
	
	public boolean viewedBy(String uid) {
		Iterator<Account> acc_iter = this.viewed_by.listIterator();
    	while (acc_iter.hasNext()) {
    		Account a = acc_iter.next();
    		if (a.matchesID(uid)) return true;
    	}
    	return false;
	}
	
	public boolean matchesID(String id) {
		return this.tid.equals(id);
	}

	public boolean matchesAccountID(String acc_id) {
		return (acc_id.equals(this.uid));
	}
	
	public boolean checkForKeyword(String key) {
		if (key.equalsIgnoreCase(this.description)) return true;
		return false;
	}
	
	public boolean isNil() {
		return false;
	}
}
