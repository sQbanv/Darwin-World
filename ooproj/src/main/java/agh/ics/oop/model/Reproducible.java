package agh.ics.oop.model;

public interface Reproducible {
    Animal reproduce(Animal animal, int minMutation, int maxMutation);
}
