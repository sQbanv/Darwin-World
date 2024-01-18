package agh.ics.oop.model.map;

import agh.ics.oop.SimulationConfigurator;

public class GlobeMap extends AbstractWorldMap {
    public GlobeMap(SimulationConfigurator configurator){
        super(configurator);
        generateTiles();
        generateAnimals(configurator.initialAnimalCount());
        generatePlants(configurator.initialPlantCount());
    }
}
