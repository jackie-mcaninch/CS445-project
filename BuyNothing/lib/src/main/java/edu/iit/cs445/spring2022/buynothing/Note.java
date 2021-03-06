package edu.iit.cs445.spring2022.buynothing;

public class Note extends BuyNothingObj {
	private String uid;
	private String toType;
	private String toUserID;
	private String toID;
	private String description;
		
	public Note() {
		super();
	}
	
	public Note(Note n) {
		super();
		this.isActive = n.getActiveStatus();
		this.dateCreated = n.getDateCreated();
		this.toType = n.getToType();
		this.toUserID = n.getToUserID();
		this.toID = n.getToID();
		this.description = n.getDescription();
	}
	
	public void updateAccountID(String new_uid) {
		this.uid = new_uid;
	}
	
	public void updateToType(String new_totype) {
		this.toType = new_totype;
	}
	
	public void updateToUserID(String new_touser) {
		this.toUserID = new_touser;
	}
	
	public void updateToID(String new_toid) {
		this.toID = new_toid;
	}
	
	public void updateDescription(String new_description) {
		this.description = new_description;
	}
	
	public String getAccountID() {
		return this.uid;
	}
	
	public String getToType() {
		return this.toType;
	}
	
	public String getToUserID() {
		return this.toUserID;
	}
	
	public String getToID() {
		return this.toID;
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
