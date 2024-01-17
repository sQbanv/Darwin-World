package agh.ics.oop.model.map;

import agh.ics.oop.SimulationConfigurator;
import agh.ics.oop.model.MapElement;
import agh.ics.oop.model.Tunnel;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animal.Animal;

import java.util.*;

public class UndergroundTunnels extends AbstractWorldMap {
    private final HashMap<Vector2d,Vector2d> tilesWithTunnelFirst = new HashMap<>();
    private final HashMap<Vector2d,Vector2d> tilesWithTunnelSecond = new HashMap<>();
    private final List<Vector2d> tilesWithoutTunnel = new LinkedList<>();
    private final Set<Vector2d> wasTunnelUsed = new HashSet<>();
    private final HashMap<Vector2d, Tunnel> tunnels = new HashMap<>();
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
            tilesWithTunnelFirst.put(position1,position2);
            tilesWithTunnelSecond.put(position2,position1);
            tunnels.put(position1, new Tunnel(position1));
            tunnels.put(position2, new Tunnel(position2));
        }
    }

    @Override
    public void move() {
        super.move();
        List<Vector2d> toRemove = new LinkedList<>();
        List<Vector2d> toAdd = new LinkedList<>();
        for(Vector2d position : tilesWithAnimals) {
            if (!wasTunnelUsed.contains(position) && (tilesWithTunnelFirst.containsKey(position)||tilesWithTunnelSecond.containsKey(position))) {
                Vector2d position2 = position;
                if(tilesWithTunnelFirst.containsKey(position)) {
                    position2 = tilesWithTunnelFirst.get(position);
                } else if (tilesWithTunnelSecond.containsKey(position)) {
                    position2 = tilesWithTunnelSecond.get(position);
                }
                Tile tile1 = mapTiles.get(position);
                Tile tile2 = mapTiles.get(position2);
                List<Animal> animals1 = new ArrayList<>(tile1.getAnimals());
                List<Animal> animals2 = new ArrayList<>(tile2.getAnimals());
                for(Animal animal : animals1){
                    animal.setPosition(position2);
                }
                for(Animal animal : animals2){
                    animal.setPosition(position);
                }
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

    @Override
    public Optional<MapElement> objectAt(Vector2d position){
        if(mapTiles.get(position).getAnimal().isPresent()) {
            return Optional.of(mapTiles.get(position).getAnimal().get());
        } else if(tunnels.containsKey(position)){
            return Optional.of(tunnels.get(position));
        } else if(mapTiles.get(position).getPlant().isPresent()){
            return Optional.of(mapTiles.get(position).getPlant().get());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int getNumberOfFreeTiles() {
        int freeTiles = 0;
        for(Tile tile : mapTiles.values()){
            if(tile.getAnimal().isEmpty() && tile.getPlant().isEmpty() && !tunnels.containsKey(tile.getPosition())){
                freeTiles++;
            }
        }
        return freeTiles;
    }
}
