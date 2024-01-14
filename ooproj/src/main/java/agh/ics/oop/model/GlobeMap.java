package agh.ics.oop.model;

import agh.ics.oop.SimulationConfigurator;

public class GlobeMap extends AbstractWorldMap{
    public GlobeMap(SimulationConfigurator configurator){
        super(configurator);
        generateTiles();
        generateAnimals(configurator.initialAnimalCount());
        generatePlants(configurator.initialPlantCount());
    }

    @Override
    public void special() {

    }
}
