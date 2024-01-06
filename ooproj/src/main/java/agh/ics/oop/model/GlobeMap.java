package agh.ics.oop.model;

public class GlobeMap extends AbstractWorldMap{
    public GlobeMap(int width, int height) {
        super(width, height);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return false;
    }
}
