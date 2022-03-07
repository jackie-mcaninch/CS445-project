package edu.iit.cs445.spring2022.restlamp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.NoSuchElementException;

public class LampManager implements BoundaryInterface {
    private static List<Lamp> Lamps = new ArrayList<Lamp>();

    public List<Lamp> getAllLamps() {
        return(Lamps);
    }

    public Lamp createLamp(Lamp il) {
        Lamp l= new Lamp(il);
        Lamps.add(l);
        return(l);
    }

    public Lamp getLampDetail(UUID lid) {
        return(findById(lid));
    }

    public void turnLampOn(UUID lid) {
        Lamp l = findById(lid);
        l.turnOn();
    }

    public void turnLampOff(UUID lid) {
        Lamp l = findById(lid);
        l.turnOff();
    }
    
    // Will be used by PUT; keep the ID of the resource, update "ison" from
    // the payload submitted by PUT.
    
    public void replaceLamp(UUID lid, Lamp il) {
    	Lamp l = findById(lid);
    	l.updateOnOff(il.isOn());
    }
    
    public void deleteLamp(UUID lid) throws NoSuchElementException {
    	Lamp l = findById(lid);
    	if (l.isNil()) {
    		throw new NoSuchElementException();
    	} else {
    		Lamps.remove(l);
    	}
    }

    private Lamp findById(UUID lid) {
    	System.out.println(lid);
        Iterator<Lamp> li = Lamps.listIterator();
        while(li.hasNext()) {
            Lamp l = li.next();
            if(l.matchesId(lid)) return(l);
        }
        return(new NullLamp());
    }
}


