package agh.ics.oop.model.animal;

import agh.ics.oop.model.map.MoveValidator;

public interface Movable {
    void move(MoveValidator validator);
}
