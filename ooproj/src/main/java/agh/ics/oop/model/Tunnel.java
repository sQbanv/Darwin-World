package agh.ics.oop.model;

public class Tunnel implements MapElement{
    private final Vector2d position;

    public Tunnel(Vector2d position){
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String getMapRepresentation() {
        return "#000000";
    }
}
