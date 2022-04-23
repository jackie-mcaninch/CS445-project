package edu.iit.cs445.spring2022.buynothing;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Ask extends BuyNothingObj {
	private String type;
	private String description;
	private Date startDate;
	private Date endDate;
	private String[] extraZip;
		
	public Ask() {
		super();
		super.activate();
		//TODO: only activate after all info is valid
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

	public boolean matchesID(String askID) {
		return (askID.equals(this.id));
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
