package agh.ics.oop.model;

import agh.ics.oop.SimulationConfigurator;

public class GlobeMap extends AbstractWorldMap{
    public GlobeMap(SimulationConfigurator configurator){
        super(configurator);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return false;
    }
}
