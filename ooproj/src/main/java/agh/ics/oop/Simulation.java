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

    }
}
