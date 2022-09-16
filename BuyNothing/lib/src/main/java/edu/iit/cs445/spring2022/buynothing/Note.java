package edu.iit.cs445.spring2022.buynothing;

import java.util.NoSuchElementException;
import java.util.UUID;

public class Note extends BuyNothingObj {
	private String uid;
	private String nid;
	private String to_type;
	private String to_user_id;
	private String to_id;
	private String description;
		
	public Note() {
		super();
		this.nid = UUID.randomUUID().toString();
	}
	
	public Note(Note n) {
		super();
		this.uid = n.getAccountID();
		this.nid = UUID.randomUUID().toString();
		this.to_type = n.getToType();
		this.to_user_id = n.getToUserID();
		this.to_id = n.getToID();
		this.description = n.getDescription();
		this.activate();
	}
	
	public void updateAccountID(String new_uid) {
		this.uid = new_uid;
	}
	
	public void updateToType(String new_to_type) {
		this.to_type = new_to_type;
	}
	
	public void updateToUserID(String new_to_user) {
		this.to_user_id = new_to_user;
	}
	
	public void updateToID(String new_to_id) {
		this.to_id = new_to_id;
	}
	
	public void updateDescription(String new_description) {
		this.description = new_description;
	}
	
	public String getAccountID() {
		return this.uid;
	}
	
	public String getToType() {
		return this.to_type;
	}
	
	public String getToUserID() {
		return this.to_user_id;
	}
	
	public String getToID() {
		return this.to_id;
	}
	
	public String getID() {
		return this.nid;
	}
	
	public boolean matchesID(String id) {
		return this.nid.equals(id);
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public boolean checkForKeyword(String key) {
		if (key.equalsIgnoreCase(this.description)) return true;
		return false;
	}
	
	public boolean equals(Note n) {
		if (n.isNil()) throw new NoSuchElementException();
		if (n.getToType().equals(this.to_type) &&
			n.getToUserID().equals(this.to_user_id) &&
			n.getToID().equals(this.to_id))
			return true;
		else return false;
	}
	
	public boolean isNil() {
		return false;
	}
}
