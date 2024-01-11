package agh.ics.oop.presenter;

import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class SimulationViewPresenter implements MapChangeListener{
    private static final int CELL_WIDTH = 20;
    private static final int CELL_HEIGHT = 20;

    @FXML
    private GridPane mapGrid;

    private WorldMap worldMap;

    public void drawMap(){
        clearGrid();

        int numRows = worldMap.upperRight().getY();
        int numCols = worldMap.upperRight().getX();

        for (int row = 0; row <= numRows; row++){
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }

        for (int col = 0; col <= numCols; col++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }

        for (int x = 0; x <= numCols; x++){
            for (int y = 0; y <= numRows; y++){
                Rectangle rectangle = new Rectangle(CELL_WIDTH, CELL_HEIGHT);
                rectangle.setFill(Paint.valueOf("#81f041"));
                mapGrid.add(rectangle, x, y);
                drawObject(new Vector2d(x,y));
            }
        }
    }

    private void drawObject(Vector2d currentPosition) {
        Optional<MapElement> worldElement = worldMap.objectAt(currentPosition);
        if(worldElement.isPresent()){
                Circle circle = new Circle((double) CELL_WIDTH /2);
                circle.setFill(Paint.valueOf(worldElement.get().getMapRepresentation()));
                mapGrid.add(circle, currentPosition.getX(), currentPosition.getY());
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(WorldMap map){
        Platform.runLater(() -> {
            drawMap();
        });
    }

    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }
}
