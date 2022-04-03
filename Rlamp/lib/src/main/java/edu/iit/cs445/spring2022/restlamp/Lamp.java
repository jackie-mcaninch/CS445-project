package edu.iit.cs445.spring2022.restlamp;

import java.util.UUID;

public class Lamp {
    private String id;
    private boolean ison;

    public Lamp() {
        this.id = UUID.randomUUID().toString();
    }
    
    public Lamp(Lamp il) {
        this.id = UUID.randomUUID().toString();
        this.ison = il.ison;
    }

    public boolean isOn() {
        return ison;
    }

    public void turnOn() {
        this.ison = true;
    }

    public void turnOff() {
        this.ison = false;
    }

    public boolean matchesId(String lid) {
        return(lid.equals(this.id));
    }

    public boolean isNil() {
        return false;
    }

    public String getID() {
        return this.id;
    }
    
    protected void updateOnOff(boolean onoff) {
    	this.ison = onoff;
    }
}
