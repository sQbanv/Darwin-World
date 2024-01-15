package agh.ics.oop.model.animal;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.Genotype;

public interface GenotypeFactory {
    Genotype createGenotype(int n);
    Genotype createChildGenotype(int n, int minMutations, int maxMutations, Animal parentA, Animal parentB);
}
