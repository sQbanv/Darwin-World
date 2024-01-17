package agh.ics.oop.model.animal;

import agh.ics.oop.model.Plant;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalEatTest {
    Genotype genotype = new RegularGenome(4);
    Vector2d position = new Vector2d(4,4);
    Animal animal1 = new Animal(position,20,genotype,new RegularGenotypeFactory());

    @Test
    void eatTest(){
        Plant plant = new Plant(position,10);
        int energyBefore = animal1.getEnergy() + plant.getEnergy();
        animal1.eat(plant.getEnergy());
        int energyAfter = animal1.getEnergy();
        assertEquals(energyAfter,energyBefore);
    }
}
