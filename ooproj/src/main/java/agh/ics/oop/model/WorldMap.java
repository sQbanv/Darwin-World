package agh.ics.oop.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorldMap{
    UUID getID();
    void placeAnimal(Animal animal);
    void placePlant(Plant plant);
    Optional<MapElement> objectAt(Vector2d position);
    Vector2d lowerLeft();
    Vector2d upperRight();
    void addListener(MapChangeListener listener);
    void mapChanged();
    void generatePlants(int plantsNumber);
    void removeDead();
    void move();
    void eat();
    void reproduce();
    void special();
    List<Animal> getAnimals();
    List<Plant> getPlants();
    int getNumberOfFreeTiles();
    int getPlantCount();
    double getAverageLifeSpanOfDeadAnimals();
}
