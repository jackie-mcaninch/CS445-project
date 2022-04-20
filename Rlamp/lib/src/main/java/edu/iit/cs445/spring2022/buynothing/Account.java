package edu.iit.cs445.spring2022.buynothing;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Account extends BuyNothingObj {
	private String name;
	private String street;
	private String zip;
	private String phone;
	private String picture;
	
	public Account() {
		super();
	}
	
	public Account(Account a) {
		super();
		this.isActive = false;
		this.name = a.getName();
		this.street = a.getStreet();
		this.zip = a.getZip();
		this.phone = a.getPhone();
		this.picture = a.getPicture();		
	}
	
	public void updateName(String new_name) {
		this.name = new_name;
	}
	
	public void updateAddress(String new_street, String new_zip) {
		this.street = new_street;
		this.zip = new_zip;
	}
	
	public void updatePhone(String new_phone) {
		this.phone = new_phone;
	}
	
	public void updatePicture(String new_picture) {
		this.picture = new_picture;
	}
	
	public boolean matchesID(String accountID) {
		return (accountID.equals(this.id));
	}
	
	public String getID() {
		return this.id;
	}
	
	public boolean getActiveStatus() {
		return this.isActive;
	}
	
	public String getDateCreated() {
		return this.date_created;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getStreet() {
		return this.street;
	}
	
	public String getZip() {
		return this.zip;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public String getPicture() {
		return this.picture;
	}
	
	public boolean isNil() {
		return false;
	}
}

