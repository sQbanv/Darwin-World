package agh.ics.oop.model;

import agh.ics.oop.SimulationConfigurator;

public class GlobeMapFactory implements MapFactory{
    @Override
    public WorldMap createMap(SimulationConfigurator configurator){
        return new GlobeMap(configurator);
    }
}
