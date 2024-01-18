package agh.ics.oop.model.animal;

public class SlightCorrectionGenotypeFactory implements GenotypeFactory {
    @Override
    public Genotype createGenotype(int n){
        return new SlightCorrectionGenome(n);
    }

    @Override
    public Genotype createChildGenotype(int n, int minMutations, int maxMutations, Animal parentA, Animal parentB){
        return new SlightCorrectionGenome(n,minMutations,maxMutations,parentA,parentB);
    }
}
