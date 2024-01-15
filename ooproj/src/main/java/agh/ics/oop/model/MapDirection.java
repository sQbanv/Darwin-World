package agh.ics.oop.model;

public enum MapDirection {
    NORTH(0),
    NORTHEAST(1),
    EAST(2),
    SOUTHEAST(3),
    SOUTH(4),
    SOUTHWEST(5),
    WEST(6),
    NORTHWEST(7);

    private final int value;

    MapDirection(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return switch (this){
            case NORTH -> "N";
            case NORTHEAST -> "NE";
            case EAST -> "E";
            case SOUTHEAST -> "SE";
            case SOUTH -> "S";
            case SOUTHWEST -> "SW";
            case WEST -> "W";
            case NORTHWEST -> "NW";
        };
    }
    public Vector2d toUnitVector(){
        return switch(this){

            case NORTH -> new Vector2d(0,1);
            case NORTHEAST -> new Vector2d(1,1);
            case EAST -> new Vector2d(1,0);
            case SOUTHEAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0,-1);
            case SOUTHWEST -> new Vector2d(-1,-1);
            case WEST -> new Vector2d(-1,0);
            case NORTHWEST -> new Vector2d(-1,1);
        };
    }

    public MapDirection next(){
        return values()[(this.ordinal()+1)%8];
    }

    public static MapDirection valueOf(int value) {
        for (MapDirection direction : values()) {
            if (direction.value == value) {
                return direction;
            }
        }
        throw new IllegalArgumentException("wrong value: "+value);
    }

    public MapDirection opposite(){
        return switch (this){
            case NORTH -> SOUTH;
            case NORTHEAST -> SOUTHWEST;
            case EAST -> WEST;
            case SOUTHEAST -> NORTHWEST;
            case SOUTH -> NORTH;
            case SOUTHWEST -> NORTHEAST;
            case WEST -> EAST;
            case NORTHWEST -> SOUTHEAST;
        };
    }

    public Vector2d rotate(MapDirection newDirection) {
        //TO CHANGE
        return switch (this) {
            case NORTH -> newDirection.toUnitVector();
            case NORTHEAST -> newDirection.next().toUnitVector();
            case EAST -> newDirection.next().next().toUnitVector();
            case SOUTHEAST -> newDirection.next().next().next().toUnitVector();
            case SOUTH -> newDirection.toUnitVector().opposite();
            case SOUTHWEST -> newDirection.next().next().next().next().next().toUnitVector();
            case WEST -> newDirection.next().next().next().next().next().next().toUnitVector();
            case NORTHWEST -> newDirection.next().next().next().next().next().next().next().toUnitVector();
        };
    }
}
