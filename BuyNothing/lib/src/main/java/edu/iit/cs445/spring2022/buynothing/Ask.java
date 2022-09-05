package edu.iit.cs445.spring2022.buynothing;

import java.util.NoSuchElementException;
import java.util.UUID;

public class Ask extends BuyNothingObj {
	private String uid;
	private String aid;
	private String type;
	private String description;
	private String start_date;
	private String end_date;
	private String[] extra_zip;
		
	public Ask() {
		super();
		this.aid = UUID.randomUUID().toString();
	}
	
	public Ask(Ask a) {
		super();
		this.uid = a.getAccountID();
		this.aid = UUID.randomUUID().toString();
		this.type = a.getType();
		this.description = a.getDescription();
		this.start_date = a.getStartDate();
		this.end_date = a.getEndDate();
		this.extra_zip = a.getExtraZip();
		this.activate();
	}
	
	public void updateAccountID(String new_uid) {
		this.uid = new_uid;
	}
	
	public void updateType(String new_type) {
		this.type = new_type;
	}
	
	public void updateDescription(String new_description) {
		this.description = new_description;
	}
	
	public void updateStartDate(String new_start) {
		this.start_date = new_start;
	}
	
	public void updateEndDate(String new_end) {
		this.end_date = new_end;
	}
	
	public void updateExtraZip(String[] new_zips) {
		this.extra_zip = new_zips;
	}
	
	public String getAccountID() {
		return this.uid;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getStartDate() {
		return this.start_date;
	}
	
	public String getEndDate() {
		return this.end_date;
	}
	
	public String[] getExtraZip() {
		return this.extra_zip;
	}
	
	public String getID() {
		return this.aid;
	}
	
	public boolean matchesID(String id) {
		return this.aid.equals(id);
	}

	public boolean matchesAccountID(String acc_id) {
		return (acc_id.equals(this.uid));
	}
	
	public boolean checkForKeyword(String key) {
		if (key.equalsIgnoreCase(this.type)) return true;
		if (key.equalsIgnoreCase(this.description)) return true;
		for (String zip: this.extra_zip) {
			if (key.equalsIgnoreCase(zip)) return true;
		}
		return false;
	}
	
	public boolean equals(Ask a) {
    	if (a.isNil()) throw new NoSuchElementException();
    	if (a.getType().equals(this.type) &&
    		a.getDescription().equals(this.description) &&
    		a.getStartDate().toString().equals(this.start_date.toString()) &&
    		a.getEndDate().toString().equals(this.end_date.toString()) &&
    		a.getExtraZip().equals(this.extra_zip))
    		return true;
    	else return false;
    }
	
	public boolean isNil() {
		return false;
	}
}
