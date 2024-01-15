package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;

public interface MoveValidator {
    Vector2d canMoveTo(Vector2d position, Vector2d oldPosition);
}
