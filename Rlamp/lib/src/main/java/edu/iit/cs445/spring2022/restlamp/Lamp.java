package edu.iit.cs445.spring2022.restlamp;

import java.util.UUID;

public class Lamp {
    private UUID ID;
    private boolean ison;

    public Lamp() {
        this.ID = UUID.randomUUID();
    }
    
    public Lamp(Lamp il) {
        this.ID = UUID.randomUUID();
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

    public boolean matchesId(UUID lid) {
        return(lid.equals(this.ID));
    }

    public boolean isNil() {
        return false;
    }

    public UUID getID() {
        return this.ID;
    }
    
    protected void updateOnOff(boolean onoff) {
    	this.ison = onoff;
    }
}
