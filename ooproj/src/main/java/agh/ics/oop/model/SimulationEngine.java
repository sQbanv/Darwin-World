package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> threads = new LinkedList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> simulations){
        this.simulations = simulations;
    }

    public void runSync(){
        for(Simulation simulation : simulations){
            simulation.run();
        }
    }

    public void runAsync(){
        for(Simulation simulation : simulations){
            Thread engineThread = new Thread(simulation);
            threads.add(engineThread);
            engineThread.start();
        }
    }

    public void runAsyncInThreadPool(){
        for(Simulation simulation : simulations){
            executorService.submit(simulation);
        }
        executorService.shutdown();
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        for(Thread thread : threads){
            thread.join();
        }

        executorService.shutdown();
        if(!executorService.awaitTermination(10, TimeUnit.SECONDS)){
            executorService.shutdownNow();
        }
    }
}
