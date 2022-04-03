package edu.iit.cs445.spring2022.restlamp;

import java.util.List;
import java.util.UUID;

public interface BoundaryInterface {
    List<Lamp> getAllLamps();
    Lamp createLamp(Lamp il);
    Lamp getLampDetail(String lid);
    void turnLampOn(String lid);
    void turnLampOff(String lid);
    void replaceLamp(String lid, Lamp li);
    void deleteLamp(String lid);
}
