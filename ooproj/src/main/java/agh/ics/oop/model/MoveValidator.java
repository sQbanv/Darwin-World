package agh.ics.oop.model;

public interface MoveValidator {
    Vector2d canMoveTo(Vector2d position,Vector2d oldPosition);
}
