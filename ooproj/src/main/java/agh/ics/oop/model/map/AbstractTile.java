package agh.ics.oop.model.map;

import agh.ics.oop.model.Plant;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animal.Animal;

import java.util.*;

public abstract class AbstractTile implements Tile{
    protected final Vector2d position;
    protected final Vector2d lowerLeft;
    protected final Vector2d upperRight;
    protected HashMap<Vector2d,Tile> neighbourTiles = new HashMap<>();
    protected List<Animal> animals = new LinkedList<>();
    protected List<Animal> twoBestAnimals = new ArrayList<>();
    protected Optional<Plant> plant = Optional.empty();

    public AbstractTile(Vector2d position, Vector2d lowerLeft, Vector2d upperRight) {
        this.position = position;
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }
    public void addAnimal(Animal animal){
        animals.add(animal);
        if(twoBestAnimals.size()<2){
            twoBestAnimals.add(animal);
        }
        else{
            Animal toCompare = conflictSolverWorst(twoBestAnimals.get(0),twoBestAnimals.get(1));
            if(conflictSolver(animal,toCompare)){
                twoBestAnimals.remove(toCompare);
                twoBestAnimals.add(animal);
            }
        }
    }
    public boolean conflictSolver(Animal newAnimal, Animal oldAnimal){
        if(newAnimal.getEnergy()<oldAnimal.getEnergy()){
            return false;
        } else if (oldAnimal.getEnergy()<newAnimal.getEnergy()) {
            return true;
        }
        else if (newAnimal.getDays()<oldAnimal.getDays()){
            return false;
        } else if (oldAnimal.getDays()<newAnimal.getDays()) {
            return true;
        }
        else if (newAnimal.getChildren()<oldAnimal.getChildren()){
            return false;
        } else if (oldAnimal.getChildren()<newAnimal.getChildren()) {
            return true;
        }
        Random random = new Random();
        return random.nextInt(2) == 0;
    }
    public void addPlant(Plant plant){
        if (this.plant.isEmpty()){
            this.plant = Optional.of(plant);
        }
    }
    public void eat(){
        if(twoBestAnimals.size()==1){
            twoBestAnimals.get(0).eat(getPlantEnergy());
            plant=Optional.empty();
        } else if (twoBestAnimals.size()==2) {
            Animal animalA = twoBestAnimals.get(0);
            Animal animalB = twoBestAnimals.get(1);
            if(conflictSolver(animalA,animalB)) {
                animalA.eat(getPlantEnergy());
                plant = Optional.empty();
            }
            else{
                animalB.eat(getPlantEnergy());
                plant = Optional.empty();
            }
        }
    }
    public Animal reproduce(int readyToReproduce,int reproduceLoss, int minMutationCount,int maxMutationCount){
        if(twoBestAnimals.size()==2){
            Animal animalA = twoBestAnimals.get(0);
            Animal animalB = twoBestAnimals.get(1);
            if(animalA.getEnergy()>=readyToReproduce && animalB.getEnergy()>=readyToReproduce){
                Animal child = animalA.reproduce(animalB,minMutationCount,maxMutationCount,reproduceLoss);
                addAnimal(child);
                return child;
            }
        }
        return null;
    }
    public void removeAnimal(Animal animal){
        animals.remove(animal);
        if(twoBestAnimals.remove(animal)){
            findNewTwo();
        }
    }
    public void findNewTwo(){
        if(animals.size()>1){
            Animal oneBest = twoBestAnimals.get(0);
            Animal possibleAnimal = animals.get(0);
            for(Animal animal : animals){
                if(possibleAnimal.equals(oneBest)){
                    possibleAnimal = animal;
                }
            }
            for(Animal animal : animals){
                if(!animal.equals(oneBest)&&conflictSolver(animal,possibleAnimal)){
                    possibleAnimal=animal;
                }
            }
            twoBestAnimals.add(possibleAnimal);
        }
    }
    public boolean removeAllAnimals() {
        animals.clear();
        twoBestAnimals.clear();
        return true;
    }

    public Vector2d getPosition() {
        return position;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public Optional<Animal> getAnimal(){
        if(!twoBestAnimals.isEmpty()){
            if(twoBestAnimals.size()==1) {
                return Optional.of(twoBestAnimals.get(0));
            } else if (conflictSolver(twoBestAnimals.get(0),twoBestAnimals.get(1))) {
                return Optional.of(twoBestAnimals.get(0));
            }
            else{
                return Optional.of(twoBestAnimals.get(1));
            }
        } else {
            return Optional.empty();
        }
    }
    public int getPlantEnergy(){
        if(getPlant().isPresent()){
            return getPlant().get().getEnergy();
        }
        return 0;
    }
    public Optional<Plant> getPlant() {
        return plant;
    }
    public Animal conflictSolverWorst(Animal animalA, Animal animalB){
        if(animalA.getEnergy()<animalB.getEnergy()){
            return animalA;
        } else if (animalB.getEnergy()<animalA.getEnergy()) {
            return animalB;
        }
        else if (animalA.getDays()<animalB.getDays()){
            return animalA;
        } else if (animalB.getDays()<animalA.getDays()) {
            return animalB;
        }
        else if (animalA.getChildren()<animalB.getChildren()){
            return animalA;
        } else if (animalB.getChildren()<animalA.getChildren()) {
            return animalB;
        }
        Random random = new Random();
        return (random.nextInt(2)==0) ? animalA : animalB;
    }
    public Map<Vector2d,Tile> getNeighbourTile(){
        return neighbourTiles;
    }
}

