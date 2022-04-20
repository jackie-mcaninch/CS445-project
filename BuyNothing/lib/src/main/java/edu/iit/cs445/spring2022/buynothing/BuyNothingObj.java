package edu.iit.cs445.spring2022.buynothing;

import java.util.Date;
import java.util.UUID;

public class BuyNothingObj {
	protected String id;
	protected boolean isActive;
	protected Date date_created;
	
	public BuyNothingObj() {
		this.id = UUID.randomUUID().toString();
		this.isActive = false;
	}
	
	public void activate() {
		this.isActive = true;
		this.date_created = new Date();
	}
	
	public void deactivate() {
		this.isActive = false;
	}
}
