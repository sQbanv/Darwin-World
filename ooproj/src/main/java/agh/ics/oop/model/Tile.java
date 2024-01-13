package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

public interface Tile extends MoveValidator{
    void addNeighbourTile(Map<Vector2d, Tile> map, Vector2d lowerLeft, Vector2d upperRight);
    void addAnimal(Animal animal);
    void addPlant(Plant plant);
    Vector2d getPosition();
    Optional<Plant> getPlant();
    Optional<Animal> getAnimal();
    void eat();
    Animal reproduce(int readyToReproduce, int reproduceLoss, int minMutationCount, int maxMutationCount);
    void removeAnimal(Animal animal);
}
