package agh.ics.oop.model.map;

import agh.ics.oop.model.Plant;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animal.Animal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Tile extends MoveValidator {
    void addNeighbourTile(Map<Vector2d, Tile> map);
    void addAnimal(Animal animal);
    void addPlant(Plant plant);
    Vector2d getPosition();
    Optional<Plant> getPlant();
    Optional<Animal> getAnimal();
    void eat();
    Animal reproduce(int readyToReproduce, int reproduceLoss, int minMutationCount, int maxMutationCount);
    void removeAnimal(Animal animal);
    List<Animal> getAnimals();
    boolean removeAllAnimals();
    Map<Vector2d,Tile> getNeighbourTile();
}
