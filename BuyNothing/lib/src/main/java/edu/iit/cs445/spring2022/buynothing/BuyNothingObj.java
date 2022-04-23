package edu.iit.cs445.spring2022.buynothing;

import java.util.Date;
import java.util.UUID;

public abstract class BuyNothingObj {
	protected String id;
	protected boolean isActive;
	protected Date dateCreated;
	
	public BuyNothingObj() {
		this.id = UUID.randomUUID().toString();
		this.isActive = false;
	}
	
	public void activate() {
		this.isActive = true;
		this.dateCreated = new Date();
	}
	
	public void deactivate() {
		this.isActive = false;
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
}
