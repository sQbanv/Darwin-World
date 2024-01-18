package agh.ics.oop.model;

public class Plant implements MapElement{
    private final Vector2d position;
    private final int energy;

    public Plant(Vector2d position, int energy){
        this.position = position;
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String getMapRepresentation(){
        return "#448021";
    }
}
