package agh.ics.oop.model.map;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;

import java.util.*;

public class UndergroundTile extends AbstractTile {

    public UndergroundTile(Vector2d position,Vector2d lowerLeft, Vector2d upperRight){
        super(position,lowerLeft,upperRight);
    }

    @Override
    public void addNeighbourTile(Map<Vector2d, Tile> map){
        for(MapDirection direction: MapDirection.values()){
            Vector2d neighbor = position.add(direction.toUnitVector());
            if (neighbor.precedes(upperRight) && neighbor.follows(lowerLeft)){
                neighbourTiles.put(neighbor,map.get(neighbor));
            }
        }
    }


    @Override
    public Vector2d canMoveTo(Vector2d toCheck, Vector2d oldPosition) {
        if (neighbourTiles.containsKey(toCheck)) {
            return toCheck;
        }
        return oldPosition;
    }
}
