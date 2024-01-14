package agh.ics.oop.model;

import java.util.*;

public class UndergroundTile implements Tile {
    private final Vector2d position;
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private HashMap<Vector2d,Tile> neighbourTiles = new HashMap<>();
    private List<Animal> animals = new LinkedList<>();
    private List<Animal> twoBestAnimals = new ArrayList<>();
    private Optional<Plant> plant = Optional.empty();

    public UndergroundTile(Vector2d position,Vector2d lowerLeft, Vector2d upperRight){
        this.position = position;
        this.lowerLeft=lowerLeft;
        this.upperRight=upperRight;
    }

    @Override
    public void addNeighbourTile(Map<Vector2d, Tile> map){
        System.out.println(position);
        for(MapDirection direction: MapDirection.values()){
            Vector2d neighbor = position.add(direction.toUnitVector());
            if (neighbor.precedes(upperRight) && neighbor.follows(lowerLeft)){
                neighbourTiles.put(neighbor,map.get(neighbor));
            }
        }
    }

    @Override
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

    private boolean conflictSolver(Animal newAnimal, Animal oldAnimal){
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
        else if (newAnimal.getChildrens()<oldAnimal.getChildrens()){
            return false;
        } else if (oldAnimal.getChildrens()<newAnimal.getChildrens()) {
            return true;
        }
        Random random = new Random();
        return random.nextInt(2) == 0;
    }

    private Animal conflictSolverWorst(Animal animalA, Animal animalB){
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
        else if (animalA.getChildrens()<animalB.getChildrens()){
            return animalA;
        } else if (animalB.getChildrens()<animalA.getChildrens()) {
            return animalB;
        }
        Random random = new Random();
        return (random.nextInt(2)==0) ? animalA : animalB;
    }

    @Override
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

    private void findNewTwo(){
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

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public Optional<Animal> getAnimal(){
        if(!animals.isEmpty()){
            return Optional.of(animals.get(0));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Plant> getPlant() {
        return plant;
    }

    private int getPlantEnergy(){
        if(getPlant().isPresent()){
            return getPlant().get().getEnergy();
        }
        return 0;
    }

    @Override
    public Vector2d canMoveTo(Vector2d toCheck, Vector2d oldPosition) {
        if (neighbourTiles.containsKey(toCheck)) {
            return toCheck;
        }
        return oldPosition;
    }
}
