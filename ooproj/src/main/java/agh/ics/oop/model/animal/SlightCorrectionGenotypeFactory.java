package agh.ics.oop.model.animal;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.Genotype;
import agh.ics.oop.model.animal.GenotypeFactory;
import agh.ics.oop.model.animal.SlightCorrectionGenom;

public class SlightCorrectionGenotypeFactory implements GenotypeFactory {
    @Override
    public Genotype createGenotype(int n){
        return new SlightCorrectionGenom(n);
    }

    @Override
    public Genotype createChildGenotype(int n, int minMutations, int maxMutations, Animal parentA, Animal parentB){
        return new SlightCorrectionGenom(n,minMutations,maxMutations,parentA,parentB);
    }
}
