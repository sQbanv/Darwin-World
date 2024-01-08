package agh.ics.oop.model;

import java.util.Optional;

public interface Tile {
    void addNeighbourTile(Tile tile);
    void addAnimal(Animal animal);
    void addPlant(Plant plant);
    Vector2d getPosition();
    Optional<Plant> getPlant();
    Optional<Animal> getAnimal();
}
