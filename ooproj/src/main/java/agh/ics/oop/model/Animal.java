package agh.ics.oop.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Animal implements MapElement,Movable,Eatable,Reproducible{
    private final GenotypeFactory genotypeFactory;
    private Vector2d position;
    private int energy;
    private int maxEnergy;
    private MapDirection direction;
    private final Genotype genotype;
    private LinkedList<Animal> children = new LinkedList<>();
    private int days = 0;
    private final Random random = new Random();

    public Animal(Vector2d position, int energy, Genotype genotype, GenotypeFactory genotypeFactory){
        this.position = position;
        this.energy = energy;
        this.maxEnergy = energy;
        this.genotype = genotype;
        this.genotypeFactory = genotypeFactory;
        direction = randomDirection();
    }

    private MapDirection randomDirection(){
        List<MapDirection> directions = List.of(MapDirection.values());
        return directions.get(random.nextInt(directions.size()));
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

    public int getDays(){
        return days;
    }

    @Override
    public void move() {
        //TODO
    }

    @Override
    public Animal reproduce(Animal animal, int minMutation, int maxMutation) {
        Genotype childGenotype = genotypeFactory.createChildGenotype(genotype.getGenes().size(), minMutation, maxMutation, this, animal);
        int childEnergy = 100; //TODO
        Animal child = new Animal(position, childEnergy, childGenotype, genotypeFactory);

        children.add(child);
        animal.children.add(child);

        return child;
    }

    @Override
    public String toString() {
        return direction.toString();
    }

    @Override
    public String getMapRepresentation(){
        double energyPercentage = (double) energy /maxEnergy;
        if(energyPercentage == 1){
            return "#573011";
        } else if(energyPercentage >= 0.75){
            return "#754a28";
        } else if(energyPercentage >= 0.5){
            return "#966b48";
        } else if(energyPercentage >= 0.25){
            return "#b58f70";
        } else {
            return "#d4b8a1";
        }
    }
}
