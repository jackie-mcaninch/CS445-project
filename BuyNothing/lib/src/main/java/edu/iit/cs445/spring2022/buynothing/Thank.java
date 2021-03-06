package edu.iit.cs445.spring2022.buynothing;

public class Thank extends BuyNothingObj {
	private String uid;
	private String thankTo;
	private String description;
		
	public Thank() {
		super();
	}
	
	public Thank(Thank t) {
		super();
		this.isActive = t.getActiveStatus();
		this.dateCreated = t.getDateCreated();
		this.thankTo = t.getThankTo();
		this.description = t.getDescription();
	}
	
	public void updateAccountID(String new_uid) {
		this.uid = new_uid;
	}
	
	public void updateThankTo(String new_tt) {
		this.thankTo = new_tt;
	}
	
	public void updateDescription(String new_description) {
		this.description = new_description;
	}
	
	public String getAccountID() {
		return this.uid;
	}
	
	public String getThankTo() {
		return this.thankTo;
	}
	
	public String getDescription() {
		return this.description;
	}

	public boolean matchesID(String thankID) {
		return (thankID.equals(this.id));
	}
	
	public boolean checkForKeyword(String key) {
		if (key.equalsIgnoreCase(this.description)) return true;
		return false;
	}
	
	public boolean isNil() {
		return false;
	}
}
