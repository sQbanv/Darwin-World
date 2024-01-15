package agh.ics.oop.model.map;

import agh.ics.oop.SimulationConfigurator;
import agh.ics.oop.model.Vector2d;

public class UndergroundTunnels extends AbstractWorldMap {

    public UndergroundTunnels(SimulationConfigurator configurator) {
        super(configurator);
        generateTiles();
        generateAnimals(configurator.initialAnimalCount());
        generatePlants(configurator.initialPlantCount());
    }
    @Override
    public void generateTiles(){
        int equatorStart = configurator.mapHeight()*2/5;
        int equatorEnd = configurator.mapHeight()-equatorStart-1;
        for(int x = 0; x <= upperRight.getX(); x++){
            for(int y = 0; y <= upperRight.getY(); y++){
                Vector2d position = new Vector2d(x,y);
                UndergroundTile tile = new UndergroundTile(position,lowerLeft,upperRight);
                mapTiles.put(position, tile);
                if(position.getY()>=equatorStart && position.getY()<=equatorEnd){
                    tilesWithoutPlantEquator.add(position);
                }
                else{
                    tilesWithoutPlantStandard.add(position);
                }
            }
        }
        addNeighbors();

    }

    @Override
    public void special() {

    }
}
