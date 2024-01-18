package agh.ics.oop.model.animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractGenotype implements Genotype{
    protected static final int GENOTYPE_NUMBER = 8;
    private List<Integer> genotype;
    private int currentGen;
    protected final Random random = new Random();

    public AbstractGenotype(int n){
        genotype = generateRandomIntList(n);
        currentGen = random.nextInt(n);
    }

    public AbstractGenotype(int n, int minMutations, int maxMutations, Animal parentA, Animal parentB){
        generateGenotype(parentA, parentB, minMutations, maxMutations);
        currentGen = random.nextInt(n);
    }

    private List<Integer> generateRandomIntList(int n){
        List<Integer> randomIntegers = new ArrayList<>();

        for(int i=0; i<n; i++){
            randomIntegers.add(random.nextInt(GENOTYPE_NUMBER));
        }

        return randomIntegers;
    }

    private void generateGenotype(Animal parentA, Animal parentB, int minMutations, int maxMutations){
        int side = random.nextInt(2);
        double aPercent = (double)parentA.getEnergy()/(parentA.getEnergy() + parentB.getEnergy());
        double bPercent = (double)parentB.getEnergy()/(parentA.getEnergy() + parentB.getEnergy());
        int length = parentA.getGenotype().getGenes().size();

        if((aPercent > bPercent && side == 0) || (aPercent < bPercent && side == 1)){
            int position = (int)Math.round(length*aPercent);
            genotype = mergeGenotypes(position, length, parentA.getGenotype().getGenes(), parentB.getGenotype().getGenes());
        } else {
            int position = (int)Math.round(length*bPercent);
            genotype = mergeGenotypes(position, length, parentB.getGenotype().getGenes(), parentA.getGenotype().getGenes());
        }

        int mutations = random.nextInt(maxMutations - minMutations + 1) + minMutations;

        for(int i=0; i<mutations; i++){
            mutate();
        }
    }

    private List<Integer> mergeGenotypes(int position, int length, List<Integer> genotypeA, List<Integer> genotypeB){
        List<Integer> mergedGenotype = new ArrayList<>();

        mergedGenotype.addAll(genotypeA.subList(0,position));
        mergedGenotype.addAll(genotypeB.subList(position,length));

        return mergedGenotype;
    }

    @Override
    public void nextGen(){
        currentGen = (currentGen + 1) % genotype.size();
    }

    @Override
    public List<Integer> getGenes(){
        return genotype;
    }

    @Override
    public int getCurrentGen(){
        return currentGen;
    }

    @Override
    public String toString() {
        return genotype.toString() + " -> " + currentGen;
    }
}
