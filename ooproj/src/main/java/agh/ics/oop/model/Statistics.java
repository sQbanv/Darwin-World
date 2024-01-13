package agh.ics.oop.model;

import java.util.List;

public class Statistics {
    private int animalCount;
    private int plantCount;
    private int freeTilesCount;
    private double averageLifeSpan;
    private double averageEnergyLevel;
    private double averageChildrenCount;
    private Genotype mostPopularGenotype;

    public Statistics(){

    }

    public int getAnimalCount() {
        return animalCount;
    }

    public int getPlantCount() {
        return plantCount;
    }

    public int getFreeTilesCount() {
        return freeTilesCount;
    }

    public void updateStatistics(List<Animal> animals, List<Plant> plants){

    }
}
