package agh.ics.oop.model.statistics;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.map.WorldMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Statistics implements MapChangeListener {
    private int animalCount;
    private int plantCount;
    private int freeTilesCount;
    private double averageLifeSpanOfDeadAnimals;
    private double averageEnergyLevel;
    private double averageChildrenCount;
    private List<Map.Entry<String, Long>> sortedGenotypes;

    @Override
    public void mapChanged(WorldMap worldMap) {
        List<Animal> animals = worldMap.getAnimals();

        animalCount = animals.size();
        plantCount = worldMap.getPlantCount();
        freeTilesCount = worldMap.getNumberOfFreeTiles();
        averageEnergyLevel = animals.stream()
                .mapToInt(Animal::getEnergy)
                .average()
                .orElse(0.0);
        averageChildrenCount = animals.stream()
                .mapToInt(Animal::getChildren)
                .average()
                .orElse(0.0);
        averageLifeSpanOfDeadAnimals = worldMap.getAverageLifeSpanOfDeadAnimals();

        Map<String, Long> genotypeRegistry = animals.stream()
                .collect(Collectors.groupingBy(animal -> animal.getGenotype().getGenes().toString(), Collectors.counting()));

        sortedGenotypes = genotypeRegistry.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .toList();
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

    public double getAverageLifeSpanOfDeadAnimals() {
        return averageLifeSpanOfDeadAnimals;
    }

    public double getAverageEnergyLevel() {
        return averageEnergyLevel;
    }

    public double getAverageChildrenCount() {
        return averageChildrenCount;
    }

    public List<Map.Entry<String, Long>> getMostPopularGenotype() {
        return sortedGenotypes;
    }
}
