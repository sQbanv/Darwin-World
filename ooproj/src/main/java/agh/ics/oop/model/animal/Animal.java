package agh.ics.oop.model.animal;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.MoveValidator;

import java.util.*;

public class Animal implements MapElement, Movable, Eatable, Reproducible {
    private final UUID uuid;
    private final GenotypeFactory genotypeFactory;
    private Vector2d position;
    private int energy;
    private int maxEnergy;
    private MapDirection direction;
    private final Genotype genotype;
    private final LinkedList<Animal> children = new LinkedList<>();
    private int days = 0;
    private int deathDay = 0;
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
        energy = Math.min(energy + plantEnergy,Integer.MAX_VALUE);
        maxEnergy = Math.max(energy,maxEnergy);
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

    public int getChildren() {
        return children.size();
    }

    public int getDescendants() {
        Set<Animal> visited = new HashSet<>(); // Przechowuje odwiedzone zwierzęta
        return getDescendants(this, visited);
    }

    private int getDescendants(Animal animal, Set<Animal> visited) {
        if (visited.contains(animal)) {
            return 0; // Zwierzę było już odwiedzone, więc nie dodajemy potomków
        }

        visited.add(animal);

        int descendants = children.size();
        for (Animal child : children) {
            descendants += getDescendants(child, visited);
        }

        return descendants;
    }

    public int getDeathDay() {
        return deathDay;
    }

    public void setDeathDay(int deathDay){
        this.deathDay = deathDay;
    }

    @Override
    public void move(MoveValidator validator) {
        energy = energy - 1;
        days = days + 1;
        int currentGen = genotype.getGenes().get(genotype.getCurrentGen());
        genotype.nextGen();
        MapDirection newDirection = MapDirection.valueOf(currentGen);
        Vector2d newPosition =  validator.canMoveTo(position.add(direction.rotateToVector(newDirection)),position);
        if(newPosition != position) {
            position = newPosition;
            direction = direction.rotate(newDirection);
        }
        else {
            direction = direction.opposite();
        }
    }

    @Override
    public Animal reproduce(Animal animal, int minMutation, int maxMutation, int energyCost) {
        Genotype childGenotype = genotypeFactory.createChildGenotype(genotype.getGenes().size(), minMutation, maxMutation, this, animal);
        int childEnergy = 2*energyCost;
        Animal child = new Animal(position, childEnergy, childGenotype, genotypeFactory);
        energy = energy - energyCost;
        animal.subtractEnergy(energyCost);
        children.add(child);
        animal.children.add(child);
        return child;
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    public void setPosition(Vector2d position){
        this.position = position;
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
