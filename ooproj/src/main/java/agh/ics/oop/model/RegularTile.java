package agh.ics.oop.model;

import java.util.*;

public class RegularTile implements Tile{
    private final Vector2d position;
    private HashMap<Vector2d, Tile> neighbourTiles = new HashMap<>();
    private List<Animal> animals = new LinkedList<>();
    private Optional<Plant> plant = Optional.empty();

    public RegularTile(Vector2d position){
        this.position = position;
    }

    @Override
    public void addNeighbourTile(Tile tile){
        neighbourTiles.put(tile.getPosition(), tile);
    }

    @Override
    public void addAnimal(Animal animal){
        animals.add(animal);
    }

    @Override
    public void addPlant(Plant plant){
        if (this.plant.isEmpty()){
            this.plant = Optional.of(plant);
        }
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public Optional<Animal> getAnimal(){
        if(!animals.isEmpty()){
            return Optional.of(animals.get(0));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Plant> getPlant() {
        return plant;
    }
}