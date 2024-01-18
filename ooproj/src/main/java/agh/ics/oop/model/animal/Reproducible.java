package agh.ics.oop.model.animal;

public interface Reproducible {
    Animal reproduce(Animal animal, int minMutation, int maxMutation, int energyCost);
}
