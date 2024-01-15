package agh.ics.oop.model.statistics;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.map.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticsToCSV implements MapChangeListener {
    private final Statistics statistics;
    private final CSVWriter csvWriter;
    private int day = 1;

    public StatisticsToCSV(Statistics statistics, String filePath){
        this.statistics = statistics;
        csvWriter = new CSVWriter(filePath);

        List<String> data = new ArrayList<>(List.of("Day", "AnimalCount", "PlantCount", "FreeTilesCount",
                "AverageEnergy", "AverageLifeSpanOfDeadAnimals", "AverageChildren", "Genotype1", "Genotype2",
                "Genotype3", "Genotype4", "Genotype5", "Genotype6", "Genotype7", "Genotype8", "Genotype9", "Genotype10"));
        csvWriter.write(data);
    }

    @Override
    public void mapChanged(WorldMap worldMap) {
        List<Map.Entry<String, Long>> popularGenotypes = statistics.getMostPopularGenotype();

        List<String> row = new ArrayList<>(List.of(String.valueOf(day),
                String.valueOf(statistics.getAnimalCount()),
                String.valueOf(statistics.getPlantCount()),
                String.valueOf(statistics.getFreeTilesCount()),
                String.valueOf(statistics.getAverageEnergyLevel()),
                String.valueOf(statistics.getAverageLifeSpanOfDeadAnimals()),
                String.valueOf(statistics.getAverageChildrenCount())));

        for (int i = 0; i < Math.min(popularGenotypes.size(), 10); i++) {
            Map.Entry<String, Long> entry = popularGenotypes.get(i);
            row.add(entry.getKey() + " : " + entry.getValue());
        }
        csvWriter.write(row);
        day++;
    }
}
