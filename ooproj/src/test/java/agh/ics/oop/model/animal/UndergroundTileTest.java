package agh.ics.oop.model.animal;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.map.RegularTile;
import agh.ics.oop.model.map.Tile;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UndergroundTileTest {
    @Test
    void addNeighbours(){
        Map<Vector2d, Tile> tiles = new HashMap<>();
        Vector2d lowerLeft = new Vector2d(0,0);
        Vector2d upperRight = new Vector2d(2,2);
        for(int i =0 ;i<3;i++){
            for(int j = 0;j<3;j++){
                Vector2d position = new Vector2d(i,j);
                tiles.put(position,new RegularTile(position,lowerLeft,upperRight));
            }
        }
        for (Vector2d key : tiles.keySet()) {
            tiles.get(key).addNeighbourTile(tiles);
        }
        Tile tile1 = tiles.get(lowerLeft);
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(0,1)).getPosition(),new Vector2d(0,1));
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(1,1)).getPosition(),new Vector2d(1,1));
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(1,0)).getPosition(),new Vector2d(1,0));

        tile1 = tiles.get(lowerLeft.add(new Vector2d(0,1)));
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(1,1)).getPosition(),new Vector2d(1,1));
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(1,0)).getPosition(),new Vector2d(1,0));
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(0,0)).getPosition(),new Vector2d(0,0));
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(1,2)).getPosition(),new Vector2d(1,2));
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(0,2)).getPosition(),new Vector2d(0,2));

        tile1 = tiles.get(lowerLeft.add(new Vector2d(0,2)));
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(1,1)).getPosition(),new Vector2d(1,1));
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(0,1)).getPosition(),new Vector2d(0,1));
        assertEquals(tile1.getNeighbourTile().get(new Vector2d(1,2)).getPosition(),new Vector2d(1,2));
    }
}
