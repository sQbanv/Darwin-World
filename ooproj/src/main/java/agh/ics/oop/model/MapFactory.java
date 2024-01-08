package agh.ics.oop.model;

import agh.ics.oop.SimulationConfigurator;

public interface MapFactory {
    WorldMap createMap(SimulationConfigurator configurator);
}
