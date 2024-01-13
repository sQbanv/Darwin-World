package agh.ics.oop.model;

import java.util.Random;

public class PreferredPositionGenerator {
    private static final double PREFERRED_AREA_PERCENTAGE = 0.8;
    private static final Random random = new Random();

    public static Vector2d generatePosition(int mapWidth, int mapHeight){
        if(random.nextDouble() < PREFERRED_AREA_PERCENTAGE){
            int equatorY = mapHeight / 2;

            int preferredX = random.nextInt(mapWidth / 5) + mapWidth / 5;
            int preferredY = random.nextInt(mapHeight / 5) + equatorY - mapHeight / 10;

            return new Vector2d(preferredX, preferredY);
        } else {
            int otherX = random.nextInt(mapWidth);
            int otherY = random.nextInt(mapHeight);

            return new Vector2d(otherX, otherY);
        }
    }
}
