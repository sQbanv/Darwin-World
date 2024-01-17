package agh.ics.oop.model.animal;

public interface GenotypeFactory {
    Genotype createGenotype(int n);
    Genotype createChildGenotype(int n, int minMutations, int maxMutations, Animal parentA, Animal parentB);
}
