package edu.iit.cs445.spring2022.buynothing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BuyNothingObj {
	protected String id;
	protected boolean isActive;
	protected String date_created;
	
	public BuyNothingObj() {
		this.id = UUID.randomUUID().toString();
		this.isActive = false;
	}
	
	public void activate() {
		this.isActive = true;
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date curr = new Date();
		this.date_created = f.format(curr);
	}
	
	public void deactivate() {
		this.isActive = false;
	}
}
