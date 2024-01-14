package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final Simulation simulation;
    private Thread simulationThread;

    public SimulationEngine(Simulation simulation){
        this.simulation = simulation;
    }

    public void runAsync(){
        simulationThread = new Thread(simulation);
        simulationThread.start();
    }

    public void pauseSimulation(){
        simulation.pauseSimulation();
    }

    public void resumeSimulation(){
        simulation.resumeSimulation();
    }
}
