package agh.ics.oop.model;

public class Plant implements MapElement{
    private final Vector2d position;

    public Plant(Vector2d position){
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }
}
