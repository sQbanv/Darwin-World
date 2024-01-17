package agh.ics.oop.model.animal;

public class RegularGenotypeFactory implements GenotypeFactory {
    @Override
    public Genotype createGenotype(int n){
        return new RegularGenome(n);
    }

    @Override
    public Genotype createChildGenotype(int n, int minMutations, int maxMutations, Animal parentA, Animal parentB){
        return new RegularGenome(n,minMutations,maxMutations,parentA,parentB);
    }
}
