package agh.ics.oop;

import agh.ics.oop.model.animal.GenotypeFactory;
import agh.ics.oop.model.map.MapFactory;

public record SimulationConfigurator(int mapHeight, int mapWidth, MapFactory mapType, int initialTunnelCount,
                                     int initialPlantCount, int plantEnergy, int numberOfPlantsGrowingPerDay,
                                     int initialAnimalCount, int initialAnimalEnergy, int readyToReproduceEnergy,
                                     int reproduceEnergyLoss, int minMutationCount, int maxMutationCount,
                                     GenotypeFactory mutationType, int genotypeLength) {
}
