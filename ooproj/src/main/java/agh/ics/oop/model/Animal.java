package agh.ics.oop.model;

import java.util.*;

public class Animal implements MapElement,Movable,Eatable,Reproducible{
    private final UUID uuid;
    private final GenotypeFactory genotypeFactory;
    private Vector2d position;
    private int energy;
    private final int maxEnergy;
    private MapDirection direction;
    private final Genotype genotype;
    private LinkedList<Animal> childrens = new LinkedList<>();
    private int days = 0;
    private final Random random = new Random();

    public Animal(Vector2d position, int energy, Genotype genotype, GenotypeFactory genotypeFactory){
        this.uuid = UUID.randomUUID();
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
    public void eat(int plantEnergy) {
        energy = energy + plantEnergy;
    }

    public void subtractEnergy(int toSubtract){
        energy = energy - toSubtract;
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }

    public int getDays(){
        return days;
    }

    public int getChildrens() {
        return childrens.size();
    }

    @Override
    public void move(MoveValidator validator) {
        energy = energy - 1;
        days = days + 1;
        int currentGen = genotype.getGenes().get(genotype.getCurrentGen());
        genotype.nextGen();
        MapDirection newDirection = MapDirection.valueOf(currentGen);
        switch (direction){
            case NORTH -> {
                Vector2d newPosition = validator.canMoveTo(position.add(newDirection.toUnitVector()),position);
                if(newPosition != position) {
                    position = newPosition;
                    direction = newDirection;
                }
                else {
                    direction = direction.opposite();
                }
            }
            case NORTHEAST -> {
                Vector2d newPosition = validator.canMoveTo(position.add(newDirection.next().toUnitVector()),position);
                if(newPosition != position) {
                    position = newPosition;
                    direction = newDirection.next();
                }
                else {
                    direction = direction.opposite();
                }
            }
            case EAST -> {
                Vector2d newPosition = validator.canMoveTo(position.add(newDirection.next().next().toUnitVector()),position);
                if(newPosition != position) {
                    position = newPosition;
                    direction = newDirection.next().next();
                }
                else {
                    direction = direction.opposite();
                }
            }
            case SOUTHEAST -> {
                Vector2d newPosition = validator.canMoveTo(position.add(newDirection.next().next().next().toUnitVector()),position);
                if(newPosition != position) {
                    position = newPosition;
                    direction = newDirection.next().next().next();
                }
                else {
                    direction = direction.opposite();
                }
            }
            case SOUTH -> {
                Vector2d newPosition = validator.canMoveTo(position.subtract(newDirection.toUnitVector()),position);
                if(newPosition != position) {
                    position = newPosition;
                    direction = newDirection.next().next().next().next();
                }
                else {
                    direction = direction.opposite();
                }
            }
            case SOUTHWEST -> {
                Vector2d newPosition = validator.canMoveTo(position.add(newDirection.next().next().next().next().next().toUnitVector()),position);
                if(newPosition != position) {
                    position = newPosition;
                    direction = newDirection.next().next().next().next().next();
                }
                else {
                    direction = direction.opposite();
                }
            }
            case WEST -> {
                Vector2d newPosition = validator.canMoveTo(position.add(newDirection.next().next().next().next().next().next().toUnitVector()),position);
                if(newPosition != position) {
                    position = newPosition;
                    direction = newDirection.next().next().next().next().next().next();
                }
                else {
                    direction = direction.opposite();
                }
            }
            case NORTHWEST -> {
                Vector2d newPosition = validator.canMoveTo(position.add(newDirection.next().next().next().next().next().next().next().toUnitVector()),position);
                if(newPosition != position) {
                    position = newPosition;
                    direction = newDirection.next().next().next().next().next().next().next();
                }
                else {
                    direction = direction.opposite();
                }
            }
        }
    }

    @Override
    public Animal reproduce(Animal animal, int minMutation, int maxMutation, int energyCost) {
        Genotype childGenotype = genotypeFactory.createChildGenotype(genotype.getGenes().size(), minMutation, maxMutation, this, animal);
        int childEnergy = 2*energyCost;
        Animal child = new Animal(position, childEnergy, childGenotype, genotypeFactory);
        energy = energy - energyCost;
        animal.subtractEnergy(energyCost);
        childrens.add(child);
        animal.childrens.add(child);
        return child;
    }

    @Override
    public String toString() {
        return direction.toString();
    }

    @Override
    public String getMapRepresentation(){
        double energyPercentage = (double) energy /maxEnergy;
        if(energyPercentage >= 1){
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
