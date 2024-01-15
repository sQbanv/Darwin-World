package agh.ics.oop.model.map;

import agh.ics.oop.SimulationConfigurator;

public class UndergroundTunnelsFactory implements MapFactory {
    @Override
    public WorldMap createMap(SimulationConfigurator configurator){
        return new UndergroundTunnels(configurator);
    }
}
