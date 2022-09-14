package edu.iit.cs445.spring2022.buynothing;

import java.util.UUID;

public class Report extends BuyNothingObj {
    private String rid;
    private String name;

    public Report() {
        super();
        this.rid = UUID.randomUUID().toString();
    }

    public Report(Report r) {
        super();
        this.rid = UUID.randomUUID().toString();
        this.name = r.getName();
    }

    public void updateName(String new_name) {
        this.name = new_name;
    }

    public String getID() {
        return this.rid;
    }
    
    public String getName() {
        return this.name;
    }

    public boolean matchesID(String id) {
		return this.rid.equals(id);
	}

    public boolean isNil() {
        return false;
    }
}
