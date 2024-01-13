package agh.ics.oop.model;

public class RegularGenotypeFactory implements GenotypeFactory{
    @Override
    public Genotype createGenotype(int n){
        return new RegularGenom(n);
    }

    @Override
    public Genotype createChildGenotype(int n, int minMutations, int maxMutations, Animal parentA, Animal parentB){
        return new RegularGenom(n,minMutations,maxMutations,parentA,parentB);
    }
}
