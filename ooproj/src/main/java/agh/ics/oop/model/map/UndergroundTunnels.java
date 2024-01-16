package agh.ics.oop.model.map;

import agh.ics.oop.SimulationConfigurator;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animal.Animal;

import java.util.*;

public class UndergroundTunnels extends AbstractWorldMap {
    private final HashMap<Vector2d,Vector2d> tilesWithTunnel = new HashMap<>();
    private final List<Vector2d> tilesWithoutTunnel = new LinkedList<>();

    private Set<Vector2d> wasTunnelUsed = new HashSet<>();
    public UndergroundTunnels(SimulationConfigurator configurator) {
        super(configurator);
        generateTiles();
        generateAnimals(configurator.initialAnimalCount());
        generatePlants(configurator.initialPlantCount());
        generateTunnels(configurator.initialTunnelCount());
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
                tilesWithoutTunnel.add(position);
            }
        }
        addNeighbors();
    }
    public void generateTunnels(int tunnelsNum){
        for(int i = 0 ;i<tunnelsNum; i++){
            int idx1 = random.nextInt(tilesWithoutTunnel.size());
            Vector2d position1 = tilesWithoutTunnel.remove(idx1);
            idx1 = random.nextInt(tilesWithoutTunnel.size());
            Vector2d position2 = tilesWithoutTunnel.remove(idx1);
            tilesWithTunnel.put(position1,position2);
//            System.out.println(position2 + " " + position1);
        }
    }

    @Override
    public void move() {
        super.move();
        List<Vector2d> toRemove = new LinkedList<>();
        List<Vector2d> toAdd = new LinkedList<>();
        for(Vector2d position : tilesWithAnimals) {
            if (!wasTunnelUsed.contains(position) && (tilesWithTunnel.containsKey(position)||tilesWithTunnel.containsValue(position))) {
                Vector2d position2 = position;
                if(tilesWithTunnel.containsKey(position)) {
                    position2 = tilesWithTunnel.get(position);
                }
                else {
                    for(Map.Entry<Vector2d, Vector2d> entry : tilesWithTunnel.entrySet()){
                        if(entry.getValue().equals(position)){
                            position2 = entry.getKey();
                            break;
                        }
                    }
                }
                Tile tile1 = mapTiles.get(position);
                Tile tile2 = mapTiles.get(position2);
                List<Animal> animals1 = new ArrayList<>(tile1.getAnimals());
                List<Animal> animals2 = new ArrayList<>(tile2.getAnimals());
//                System.out.println(tile1.getAnimals() + " " + tile2.getAnimals());
//                System.out.println(tile1.getPosition() + " " + tile2.getPosition());
                if (!animals1.isEmpty() && !animals2.isEmpty()) {
                    if(tile2.removeAllAnimals()&&tile1.removeAllAnimals()) {
                        animals1.forEach(tile2::addAnimal);
                        animals2.forEach(tile1::addAnimal);
                    }
                }
                if (!animals1.isEmpty() && animals2.isEmpty()) {
                    tile1.removeAllAnimals();
                    animals1.forEach(tile2::addAnimal);
                    toRemove.add(position);
                    toAdd.add(position2);
                }
                if (animals1.isEmpty() && !animals2.isEmpty()) {
                    animals2.forEach(tile1::addAnimal);
                    tile2.removeAllAnimals();
                    toRemove.add(position2);
                    toAdd.add(position);
                }
//                System.out.println(tile1.getAnimals() + " " + tile2.getAnimals());
                wasTunnelUsed.add(position);
                wasTunnelUsed.add(position2);
            }
        }
        for(Vector2d position : toRemove){
            tilesWithAnimals.remove(position);
        }
        tilesWithAnimals.addAll(toAdd);
        wasTunnelUsed.clear();
    }
}
