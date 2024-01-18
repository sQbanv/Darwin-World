package agh.ics.oop.model.animal;

import java.util.List;

public interface Genotype {
    List<Integer> getGenes();

    void nextGen();
    int getCurrentGen();
    void mutate();
}
