package agh.ics.oop.model.animal;

import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AnimalReproduceTest {
    AbstractGenotype genotype = new RegularGenome(4);
    Vector2d position = new Vector2d(4,4);
    Animal animal1 = new Animal(position,20,genotype,new RegularGenotypeFactory());
    Animal animal2 = new Animal(position,20,genotype,new RegularGenotypeFactory());
    @Test
    void sumEnergy(){
        int energyBefore = animal1.getEnergy()+animal2.getEnergy();
        Animal child = animal1.reproduce(animal2,0,2,0);
        int energyAfter = animal1.getEnergy()+animal2.getEnergy()+child.getEnergy();
        assertEquals(energyAfter,energyBefore);
    }
}
