package agh.ics.oop.model;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap{
    protected final UUID uuid;
    protected final Map<Vector2d, Tile> mapTiles = new HashMap<>();
    protected final List<Animal> animals = new LinkedList<>();
    protected final List<Plant> plants = new LinkedList<>();
    protected final Vector2d lowerLeft = new Vector2d(0,0);
    protected final Vector2d upperRight;
    public AbstractWorldMap(int width, int height){
        uuid = UUID.randomUUID();
        upperRight = new Vector2d(width,height);
        generateTiles();
    }

    protected void generateTiles(){
        //TODO
    }
    public void place(MapElement mapElement){
        //TODO
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
}
