package agh.ics.oop.model.map;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Plant;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animal.Animal;

import java.util.*;

public class RegularTile extends AbstractTile {

    public RegularTile(Vector2d position,Vector2d lowerLeft, Vector2d upperRight){
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
        if(position.getX()==lowerLeft.getX()){
            Vector2d positionA = new Vector2d(upperRight.getX(),position.getY());
            neighbourTiles.put(positionA,map.get(positionA));
            if(position.getY()>lowerLeft.getY()){
                Vector2d positionB = new Vector2d(upperRight.getX(), position.getY()-1);
                neighbourTiles.put(positionB,map.get(positionB));
            }
            if(position.getY()<upperRight.getY()){
                Vector2d positionC = new Vector2d(upperRight.getX(), position.getY()+1);
                neighbourTiles.put(positionC,map.get(positionC));
            }
        }
        if(position.getX()==upperRight.getX()){
            Vector2d positionA = new Vector2d(lowerLeft.getX(),position.getY());
            neighbourTiles.put(positionA,map.get(position));
            if(position.getY()<upperRight.getY()){
                Vector2d positionB = new Vector2d(lowerLeft.getX(), position.getY()+1);
                neighbourTiles.put(positionB,map.get(positionB));
            }
            if(position.getY()>lowerLeft.getY()){
                Vector2d positionC = new Vector2d(lowerLeft.getX(), position.getY()-1);
                neighbourTiles.put(positionC,map.get(positionC));
            }
        }
    }

    @Override
    public Vector2d canMoveTo(Vector2d toCheck, Vector2d oldPosition) {
        if(toCheck.getX()==-1 && lowerLeft.getY()<=toCheck.getY() && upperRight.getY()>= toCheck.getY()){
            Vector2d newPosition = new Vector2d(upperRight.getX(),toCheck.getY());
            if(neighbourTiles.containsKey(newPosition)){
                return newPosition;
            }
        }
        if(toCheck.getX()==upperRight.getX()+1 && lowerLeft.getY()<=toCheck.getY() && upperRight.getY()>= toCheck.getY()){
            Vector2d newPosition = new Vector2d(lowerLeft.getX(),toCheck.getY());
            if(neighbourTiles.containsKey(newPosition)){
                return newPosition;
            }
        } else if (neighbourTiles.containsKey(toCheck)) {
            return toCheck;
        }
        return oldPosition;
    }
}