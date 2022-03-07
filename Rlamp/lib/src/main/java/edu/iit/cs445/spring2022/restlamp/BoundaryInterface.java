package edu.iit.cs445.spring2022.restlamp;

import java.util.List;
import java.util.UUID;

public interface BoundaryInterface {
    List<Lamp> getAllLamps();
    Lamp createLamp(Lamp il);
    Lamp getLampDetail(UUID lid);
    void turnLampOn(UUID lid);
    void turnLampOff(UUID lid);
    void replaceLamp(UUID lid, Lamp li);
    void deleteLamp(UUID lid);
}
