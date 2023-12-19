package agh.ics.oop.model;

public class Animal implements MapElement,Movable,Eatable,Reproducible{
    private Vector2d position;
    private int energy;
    private MapDirection direction;
    private final Genotype genotype;

    public Animal(Vector2d position, int energy, Genotype genotype){
        this.position = position;
        this.energy = energy;
        this.genotype = genotype;
    }

    public MapDirection getDirection() {
        return direction;
    }

    public int getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    @Override
    public void eat(Plant plant) {
        //TODO
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public void move() {
        //TODO
    }

    @Override
    public Animal reproduce(Animal animal) {
        //TODO
        return null;
    }
}
