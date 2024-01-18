package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final ExecutorService threadPool;

    public SimulationEngine(){
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void addSimulation(Simulation simulation){
        threadPool.submit(simulation);
    }

    public void stopSimulations() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                System.err.println("Some simulations did not terminate in time.");
            }
        } catch (InterruptedException e) {
            System.err.println("Error waiting for termination: " + e.getMessage());
        }
    }
}
