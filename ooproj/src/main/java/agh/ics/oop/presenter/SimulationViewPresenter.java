package agh.ics.oop.presenter;

import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class SimulationViewPresenter implements MapChangeListener{
    private static final double CELL_WIDTH = 35;
    private static final double CELL_HEIGHT = 35;

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
                drawObject(new Vector2d(x,y));
            }
        }
    }

    private void drawObject(Vector2d currentPosition) {
        Optional<MapElement> worldElement = worldMap.objectAt(currentPosition);
        if(worldElement.isPresent()){
            Label label = new Label(worldElement.get().toString());
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, currentPosition.getX(), currentPosition.getY());
        } else {
            Label label = new Label(" ");
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, currentPosition.getX(), currentPosition.getY());
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
