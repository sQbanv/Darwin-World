package agh.ics.oop.model.map;

import agh.ics.oop.SimulationConfigurator;
import agh.ics.oop.model.*;
import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.Genotype;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final UUID uuid;
    protected final SimulationConfigurator configurator;
    protected final Map<Vector2d, Tile> mapTiles = new HashMap<>();
    protected final List<Animal> animals = new LinkedList<>();
    protected final List<Plant> plants = new LinkedList<>();
    protected final Vector2d lowerLeft = new Vector2d(0,0);
    protected final Vector2d upperRight;
    protected double averageLifeSpanOfDeadAnimals = 0.0;
    protected int deadAnimalsCount = 0;
    protected int day = 0;
    protected final LinkedList<MapChangeListener> mapChangeListeners = new LinkedList<>();
    protected final Random random = new Random();
    protected Set<Vector2d> tilesWithAnimals = new HashSet<>();
    protected List<Vector2d> tilesWithoutPlantEquator = new LinkedList<>();
    protected List<Vector2d> tilesWithoutPlantStandard = new LinkedList<>();
    protected List<Vector2d> tilesWithPlantEquator = new LinkedList<>();
    protected List<Vector2d> tilesWithPlantStandard = new LinkedList<>();
    public AbstractWorldMap(SimulationConfigurator configurator){
        uuid = UUID.randomUUID();
        this.configurator = configurator;
        upperRight = new Vector2d(configurator.mapWidth() - 1,configurator.mapHeight() - 1);
    }

    protected void generateTiles(){
        int equatorStart = configurator.mapHeight()*2/5;
        int equatorEnd = configurator.mapHeight()-equatorStart-1;
        for(int x = 0; x <= upperRight.getX(); x++){
            for(int y = 0; y <= upperRight.getY(); y++){
                Vector2d position = new Vector2d(x,y);
                RegularTile tile = new RegularTile(position,lowerLeft,upperRight);
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

    protected void addNeighbors(){
        for (Vector2d key : mapTiles.keySet()) {
            mapTiles.get(key).addNeighbourTile(mapTiles);
        }
    }

    protected void generateAnimals(int initialAnimalCount){
        for(int i=0; i<initialAnimalCount; i++){
            Genotype genotype = configurator.mutationType().createGenotype(configurator.genotypeLength());
            Vector2d position = new Vector2d(random.nextInt(configurator.mapWidth()), random.nextInt(configurator.mapHeight()));
            Animal newAnimal = new Animal(position, configurator.initialAnimalEnergy(), genotype, configurator.mutationType());
            this.placeAnimal(newAnimal);
            tilesWithAnimals.add(position);
        }
    }

    public void generatePlants(int initialPlantCount){
        for(int i=0; i<initialPlantCount && (!tilesWithoutPlantEquator.isEmpty() || !tilesWithoutPlantStandard.isEmpty());){
            if(random.nextInt(100)<80 && !tilesWithoutPlantEquator.isEmpty()){
                int idx = random.nextInt(tilesWithoutPlantEquator.size());
                Vector2d position = tilesWithoutPlantEquator.get(idx);
                Plant plant = new Plant(position, configurator.plantEnergy());
                mapTiles.get(position).addPlant(plant);
                tilesWithPlantEquator.add(position);
                tilesWithoutPlantEquator.remove(idx);
                i++;
            } else if (!tilesWithoutPlantStandard.isEmpty()) {
                int idx = random.nextInt(tilesWithoutPlantStandard.size());
                Vector2d position = tilesWithoutPlantStandard.get(idx);
                Plant plant = new Plant(position, configurator.plantEnergy());
                mapTiles.get(position).addPlant(plant);
                tilesWithPlantStandard.add(position);
                tilesWithoutPlantStandard.remove(idx);
                i++;
            }
            else break;
        }
    }

    public void removeDead() {
        day++; //start of new day
        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()){
            Animal animal = iterator.next();
            if(animal.getEnergy()<=0){
                mapTiles.get(animal.getPosition()).removeAnimal(animal);
                averageLifeSpanOfDeadAnimals = (deadAnimalsCount * averageLifeSpanOfDeadAnimals + animal.getDays())/(deadAnimalsCount + 1);
                deadAnimalsCount++;
                animal.setDeathDay(day);
                iterator.remove();
            }
        }
    }

    public void eat() {
        for(Vector2d position : tilesWithAnimals){
            if(tilesWithPlantStandard.contains(position)){
                mapTiles.get(position).eat();
                tilesWithPlantStandard.remove(position);
                tilesWithoutPlantStandard.add(position);
            }
            if(tilesWithPlantEquator.contains(position)){
                mapTiles.get(position).eat();
                tilesWithPlantEquator.remove(position);
                tilesWithoutPlantEquator.add(position);
            }
        }
    }

    public void reproduce() {
        for(Vector2d position : tilesWithAnimals){
            Animal child = mapTiles.get(position).reproduce(configurator.readyToReproduceEnergy(),configurator.reproduceEnergyLoss(),configurator.minMutationCount(),configurator.maxMutationCount());
            if(child!=null){
                animals.add(child);
            }
        }
    }

    public void move() {
        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()){
            Animal animal = iterator.next();
            Vector2d oldPosition = animal.getPosition();
            animal.move(mapTiles.get(oldPosition));
            if(animal.getPosition()!=oldPosition){
                mapTiles.get(oldPosition).removeAnimal(animal);
                mapTiles.get(animal.getPosition()).addAnimal(animal);
                if(mapTiles.get(oldPosition).getAnimal().isEmpty()){
                    tilesWithAnimals.remove(oldPosition);
                }
                tilesWithAnimals.add(animal.getPosition());
            }
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

    public List<Animal> getAnimals(){
        return new LinkedList<>(animals);
    }

    public List<Plant> getPlants(){
        return new LinkedList<>(plants);
    }

    @Override
    public int getNumberOfFreeTiles() {
        int freeTiles = 0;
        for(Tile tile : mapTiles.values()){
            if(tile.getAnimal().isEmpty() && tile.getPlant().isEmpty()){
                freeTiles++;
            }
        }
        return freeTiles;
    }

    @Override
    public int getPlantCount() {
        return tilesWithPlantEquator.size() + tilesWithPlantStandard.size();
    }

    @Override
    public double getAverageLifeSpanOfDeadAnimals() {
        return averageLifeSpanOfDeadAnimals;
    }

    @Override
    public List<Vector2d> getPreferredTilesPositions() {
        List<Vector2d> preferredTilesPositions = new LinkedList<>();
        preferredTilesPositions.addAll(tilesWithoutPlantEquator);
        preferredTilesPositions.addAll(tilesWithPlantEquator);
        return preferredTilesPositions;
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
