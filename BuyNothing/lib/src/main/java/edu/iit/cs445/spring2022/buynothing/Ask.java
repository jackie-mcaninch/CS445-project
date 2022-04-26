package edu.iit.cs445.spring2022.buynothing;

import java.util.Date;
import java.util.NoSuchElementException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Ask extends BuyNothingObj {
	private String uid;
	private String type;
	private String description;
	private Date startDate;
	private Date endDate;
	private String[] extraZip;
		
	public Ask() {
		super();
	}
	
	public Ask(Ask a) {
		super();
		this.isActive = a.getActiveStatus();
		this.dateCreated = a.getDateCreated();
		this.type = a.getType();
		this.description = a.getDescription();
		this.startDate = a.getStartDate();
		this.endDate = a.getEndDate();
		this.extraZip = a.getExtraZip();	
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
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			this.startDate = fmt.parse(new_start);
		}
		catch (ParseException e) {
			throw new IllegalArgumentException();
		}
	}
	
	public void updateEndDate(String new_end) {
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			this.endDate = fmt.parse(new_end);
		}
		catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	public void updateExtraZip(String[] new_zips) {
		this.extraZip = new_zips;
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
	
	public Date getStartDate() {
		return this.startDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
	}
	
	public String[] getExtraZip() {
		return this.extraZip;
	}

	public boolean matchesID(String aid) {
		return (aid.equals(this.id));
	}
	
	public boolean matchesUserID(String uid) {
		return (uid.equals(this.uid));
	}
	
	public boolean checkForKeyword(String key) {
		if (key.equalsIgnoreCase(this.type)) return true;
		if (key.equalsIgnoreCase(this.description)) return true;
		for (String zip: extraZip) {
			if (key.equalsIgnoreCase(zip)) return true;
		}
		return false;
	}
	
	public boolean equals(Ask a) {
    	if (a.isNil()) throw new NoSuchElementException();
    	if (a.getType().equals(this.type) &&
    		a.getDescription().equals(this.description) &&
    		a.getStartDate().toString().equals(this.startDate.toString()) &&
    		a.getEndDate().toString().equals(this.endDate.toString()) &&
    		a.getExtraZip().equals(this.extraZip))
    		return true;
    	else return false;
    }
	
	public boolean isNil() {
		return false;
	}
}
