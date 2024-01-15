package agh.ics.oop.model.animal;

import java.util.List;

public interface Genotype {
    List<Integer> getGenes();

    public void nextGen();
    public int getCurrentGen();
    void mutate();
}
