package agh.ics.oop.model;

import agh.ics.oop.SimulationConfigurator;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap{
    protected final UUID uuid;
    protected final SimulationConfigurator configurator;
    protected final Map<Vector2d, Tile> mapTiles = new HashMap<>();
    protected final List<Animal> animals = new LinkedList<>();
    protected final List<Plant> plants = new LinkedList<>();
    protected final Vector2d lowerLeft = new Vector2d(0,0);
    protected final Vector2d upperRight;
    protected final LinkedList<MapChangeListener> mapChangeListeners = new LinkedList<>();
    protected final Random random = new Random();

    public AbstractWorldMap(SimulationConfigurator configurator){
        uuid = UUID.randomUUID();
        this.configurator = configurator;
        upperRight = new Vector2d(configurator.mapWidth() - 1,configurator.mapHeight() - 1);
        generateTiles();
        generateAnimals(configurator.initialAnimalCount());
        generatePlants(configurator.initialPlantCount());
    }

    protected void generateTiles(){
        for(int x = 0; x <= upperRight.getX(); x++){
            for(int y = 0; y <= upperRight.getY(); y++){
                Vector2d position = new Vector2d(x,y);
                mapTiles.put(position, new RegularTile(position));
            }
        }

        //TODO NeighbourTiles
    }

    protected void generateAnimals(int initialAnimalCount){
        for(int i=0; i<initialAnimalCount; i++){
            Genotype genotype = configurator.mutationType().createGenotype(configurator.genotypeLength());
            Vector2d position = new Vector2d(random.nextInt(configurator.mapWidth()), random.nextInt(configurator.mapHeight()));
            Animal newAnimal = new Animal(position, configurator.initialAnimalEnergy(), genotype, configurator.mutationType());
            this.placeAnimal(newAnimal);
        }
    }

    protected void generatePlants(int initialPlantCount){
        for(int i=0; i<initialPlantCount; i++){
            Vector2d position = PreferredPositionGenerator.generatePosition(configurator.mapWidth(), configurator.mapHeight());
            while(mapTiles.get(position).getPlant().isPresent()){
                position = PreferredPositionGenerator.generatePosition(configurator.mapWidth(), configurator.mapHeight());
            }
            Plant newPlant = new Plant(position);
            this.placePlant(newPlant);
        }
    }

    @Override
    public void placeAnimal(Animal animal){
        animals.add(animal);
        mapTiles.get(animal.getPosition()).addAnimal(animal);
    }

    @Override
    public void placePlant(Plant plant){
        plants.add(plant);
        mapTiles.get(plant.getPosition()).addPlant(plant);
    }

    @Override
    public Optional<MapElement> objectAt(Vector2d position){
        if(mapTiles.get(position).getAnimal().isPresent()){
            return Optional.of(mapTiles.get(position).getAnimal().get());
        } else if(mapTiles.get(position).getPlant().isPresent()){
            return Optional.of(mapTiles.get(position).getPlant().get());
        } else {
            return Optional.empty();
        }
    }

    public List<MapElement> objectsAt(Vector2d position){
        //TODO
        return new LinkedList<>();
    }

    public List<Animal> getAnimals(){
        return new LinkedList<>(animals);
    }

    public List<Plant> getPlants(){
        return new LinkedList<>(plants);
    }

    public List<Tile> getTiles(){
        return new LinkedList<>(mapTiles.values());
    }

    public List<MapElement> getElements(){
        //TODO
        return new LinkedList<>();
    }

    @Override
    public UUID getID() {
        return uuid;
    }

    public Vector2d lowerLeft() {
        return lowerLeft;
    }

    public Vector2d upperRight() {
        return upperRight;
    }

    public void addListener(MapChangeListener listener){
        mapChangeListeners.add(listener);
    }

    public void removeListener(MapChangeListener listener){
        mapChangeListeners.remove(listener);
    }

    public void mapChanged(){
        for (MapChangeListener listener : mapChangeListeners) {
            listener.mapChanged(this);
        }
    }
}
