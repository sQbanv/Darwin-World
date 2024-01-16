package agh.ics.oop.model.map;

import agh.ics.oop.SimulationConfigurator;
import agh.ics.oop.model.map.AbstractWorldMap;

public class GlobeMap extends AbstractWorldMap {
    public GlobeMap(SimulationConfigurator configurator){
        super(configurator);
        generateTiles();
        generateAnimals(configurator.initialAnimalCount());
        generatePlants(configurator.initialPlantCount());
    }
}
