package edu.iit.cs445.spring2022.buynothing;

import java.util.Date;
import java.util.NoSuchElementException;

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
	
	public String getID() {
		return this.id;
	}
	
	public boolean getActiveStatus() {
		return this.isActive;
	}
	
	public Date getDateCreated() {
		return this.dateCreated;
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
	
	public boolean matchesID(String accountID) {
		return (accountID.equals(this.id));
	}
	
	public boolean checkForKeyword(String key) {
		String[] split_name = this.name.split(" ");
		String first_name = split_name[0];
		String last_name = split_name[1];
		String address = String.format("%s %s", this.street, this.zip);
		if (key.equalsIgnoreCase(first_name)) return true;
		if (key.equalsIgnoreCase(last_name)) return true;
		if (key.equalsIgnoreCase(address)) return true;
		if (key.equalsIgnoreCase(phone)) return true;
		return false;
		}
    
    public boolean equals(Account a) {
    	if (a.isNil()) throw new NoSuchElementException();
    	if (a.getName().equals(this.name) &&
    		a.getStreet().equals(this.street) &&
    		a.getZip().equals(this.zip) &&
    		a.getPhone().equals(this.phone) &&
    		a.getPicture().equals(this.picture))
    		return true;
    	else return false;
    }
	
	public boolean isNil() {
		return false;
	}
}

