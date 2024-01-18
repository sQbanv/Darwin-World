package agh.ics.oop.model.map;

import agh.ics.oop.SimulationConfigurator;

public interface MapFactory {
    WorldMap createMap(SimulationConfigurator configurator);
}
