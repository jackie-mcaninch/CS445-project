package edu.iit.cs445.spring2022.buynothing;

import java.util.NoSuchElementException;
import java.util.UUID;

public class Account extends BuyNothingObj {
	private String uid;
	private String name;
	private Address address = new Address();
	private String phone;
	private String picture;
	private boolean is_privileged = false;
	
	public Account() {
		super();
		this.uid = UUID.randomUUID().toString();
	}
	
	public Account(Account a) {
		super();
		this.uid = UUID.randomUUID().toString();
		this.name = a.getName();
		this.address = new Address(a.getStreet(), a.getZip());
		this.phone = a.getPhone();
		this.picture = a.getPicture();
		this.is_privileged = a.isPrivileged();
	}

	public void elevatePrivileges() {
		this.is_privileged = true;
	}
	
	public void updateName(String new_name) {
		this.name = new_name;
	}
	
	public void updateAddress(String new_street, String new_zip) {
		this.address.updateStreet(new_street);
		this.address.updateZip(new_zip);
	}
	
	public void updatePhone(String new_phone) {
		this.phone = new_phone;
	}
	
	public void updatePicture(String new_picture) {
		this.picture = new_picture;
	}
	
	public boolean getActiveStatus() {
		return this.is_active;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getStreet() {
		return this.address.getStreet();
	}
	
	public String getZip() {
		return this.address.getZip();
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public String getPicture() {
		return this.picture;
	}
	
	public String getID() {
		return this.uid;
	}
	
	public boolean matchesID(String id) {
		return this.uid.equals(id);
	}
	
	public boolean checkForKeyword(String key) {
		key = key.toLowerCase();
		String name 	= this.getName().toLowerCase();
		String street 	= this.getStreet().toLowerCase();
		String zip 		= this.getZip().toLowerCase();
		String phone 	= this.getPhone().toLowerCase();
		if (name.contains(key) ||
			street.contains(key) ||
			zip.contains(key) ||
			phone.contains(key)) 
			return true;
		else return false;
		}
    
    public boolean equals(Account a) {
    	if (a.isNil()) throw new NoSuchElementException();
    	if (a.getName().equals(this.getName()) &&
    		a.getStreet().equals(this.getStreet()) &&
    		a.getZip().equals(this.getZip()) &&
    		a.getPhone().equals(this.getPhone()) &&
    		a.getPicture().equals(this.getPicture()))
    		return true;
    	else return false;
    }
    
	public boolean isPrivileged() {
		return this.is_privileged;
	}

    public boolean isNil() {
    	return false;
    }
}

