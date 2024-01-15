package agh.ics.oop.model.animal;

import agh.ics.oop.model.animal.Animal;

public interface Reproducible {
    Animal reproduce(Animal animal, int minMutation, int maxMutation, int energyCost);
}
