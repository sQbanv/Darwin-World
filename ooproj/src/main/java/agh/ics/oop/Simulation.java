package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.Random;

public class Simulation implements Runnable{
    private final WorldMap map;
    private final SimulationConfigurator configurator;
    private final Statistics statistics;
    private final Random random = new Random();

    public Simulation(SimulationConfigurator configurator, WorldMap map, Statistics statistics){
        this.configurator = configurator;
        this.map = map;
        this.statistics = statistics;
        map.mapChanged();
    }

    @Override
    public void run() {
        for(int i=0;i<20;i++){
            map.removeDead();
            map.move();
            map.eat();
            map.reproduce();
            map.generatePlants(configurator.numberOfPlantsGrowingPerDay());
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
            map.mapChanged();
        }
    }
}
