package edu.iit.cs445.spring2022.buynothing;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Give extends BuyNothingObj {
	private String aid;
	private String type;
	private String description;
	private Date startDate;
	private Date endDate;
	private String[] extraZip;
		
	public Give() {
		super();
	}
	
	public Give(Give g) {
		super();
		this.is_active = g.getActiveStatus();
		this.date_created = g.getdate_created();
		this.type = g.getType();
		this.description = g.getDescription();
		this.startDate = g.getStartDate();
		this.endDate = g.getEndDate();
		this.extraZip = g.getExtraZip();	
	}
	
	public void updateAccountID(String new_aid) {
		this.aid = new_aid;
	}
	
	public void updateType(String new_type) {
		this.type = new_type;
	}
	
	public void updateDescription(String new_description) {
		this.description = new_description;
	}
	
	public void updateStartDate(String new_start) {
		try {
			this.startDate = new SimpleDateFormat("YYYY-MM-DD").parse(new_start);
		}
		catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	public void updateEndDate(String new_end) {
		try {
			this.startDate = new SimpleDateFormat("YYYY-MM-DD").parse(new_end);
		}
		catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	public void updateExtraZip(String[] new_zips) {
		this.extraZip = new_zips;
	}
	
	public String getAccountID() {
		return this.aid;
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

	public boolean matchesID(String giveID) {
		return (giveID.equals(this.uid));
	}
	
	public boolean checkForKeyword(String key) {
		if (key.equalsIgnoreCase(this.type)) return true;
		if (key.equalsIgnoreCase(this.description)) return true;
		for (String zip: extraZip) {
			if (key.equalsIgnoreCase(zip)) return true;
		}
		return false;
	}
	
	public boolean isNil() {
		return false;
	}
}
