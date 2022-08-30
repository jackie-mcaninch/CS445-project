package edu.iit.cs445.spring2022.buynothing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class BuyNothingObj {
	protected String uid;
	protected boolean is_active;
	protected String date_created;
	
	public BuyNothingObj() {
		this.uid = UUID.randomUUID().toString();
		this.is_active = false;
		this.date_created = "";
	}
	
	public void create() {
		DateFormat format = new SimpleDateFormat("dd-MMM-YYYY");
		this.date_created = format.format(new Date());
	}
	
	public void activate() {
		this.is_active = true;
	}
	
	public void forceActivate() {
		// used for setting is_active to true without creating date
		this.is_active = true;
	}
	
	public void deactivate() {
		this.is_active = false;
	}
	
	public String getID() {
		return this.uid;
	}
	
	public boolean getActiveStatus() {
		return this.is_active;
	}
	
	public String getDateCreated() {
		return this.date_created;
	}
}
