package edu.iit.cs445.spring2022.buynothing;

public class Address {
	private String street;
	private String zip;
	
	public Address() {}

	public Address(String street, String zip) {
		this.street = street;
		this.zip = zip;
	}
	
	public String getStreet() {
		return this.street;
	}
	
	public String getZip() {
		return this.zip;
	}
	
	public void updateStreet(String new_street) {
		this.street = new_street;
	}
	
	public void updateZip(String new_zip) {
		this.zip = new_zip;
	}
}
