package agh.ics.oop;

import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.statistics.Statistics;

public class Simulation implements Runnable{
    private final WorldMap map;
    private final SimulationConfigurator configurator;
    private final Statistics statistics;
    private boolean isPaused = false;

    public Simulation(SimulationConfigurator configurator, WorldMap map, Statistics statistics){
        this.configurator = configurator;
        this.map = map;
        this.statistics = statistics;
        map.mapChanged();
    }

    @Override
    public void run() {
        while (!map.getAnimals().isEmpty()){
            if(!isPaused){
                map.removeDead();
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
                map.move();
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
                map.eat();
                map.reproduce();
                map.generatePlants(configurator.numberOfPlantsGrowingPerDay());
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
                map.mapChanged();
            } else {
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void pauseSimulation(){
        isPaused = true;
    }

    public void resumeSimulation(){
        isPaused = false;
    }

    public Statistics getStatistics(){
        return statistics;
    }
}
