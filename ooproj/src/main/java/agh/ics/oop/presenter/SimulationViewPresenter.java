package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.statistics.Statistics;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimulationViewPresenter implements MapChangeListener{
    private double CELL_WIDTH = 12;
    private double CELL_HEIGHT = 12;
    private static final int MIN_POPULAR_GENOTYPE = 10;
    private boolean isRunning = true;

    @FXML
    private GridPane mapGrid;
    @FXML
    private Button pauseResume;
    @FXML
    private Label animalCountLabel;
    @FXML
    private Label plantCountLabel;
    @FXML
    private Label freeTilesCountLabel;
    @FXML
    private Label averageEnergyLabel;
    @FXML
    private Label averageLifeSpanOfDeadAnimalsLabel;
    @FXML
    private Label averageChildrenCountLabel;
    @FXML
    private VBox popularGenotypesVBox;
    @FXML
    private VBox clickedAnimalInfo;
    @FXML
    private HBox pauseButtons;
    @FXML
    private Button clearButton;

    private WorldMap worldMap;
    private Simulation simulation;
    private Optional<Animal> clickedAnimal = Optional.empty();

    private void handleWindowClose(){
        simulation.stopSimulation();
    }

    public void drawMap(){
        clearGrid();

        mapGrid.setStyle("-fx-grid-lines-color: transparent");
        mapGrid.setGridLinesVisible(false);

        int numRows = worldMap.upperRight().getY();
        int numCols = worldMap.upperRight().getX();

        for (int row = 0; row <= numRows; row++){
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }

        for (int col = 0; col <= numCols; col++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }

        for (int y = numRows; y >= 0; y--){
            for (int x = 0; x <= numCols; x++){
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
            Circle circle = new Circle(CELL_WIDTH /2);
            circle.setFill(Paint.valueOf(worldElement.get().getMapRepresentation()));

            circle.setOnMouseClicked(event -> handleCellClick(worldElement.get()));

            mapGrid.add(circle, currentPosition.getX(), currentPosition.getY());
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    protected void handleCellClick(MapElement mapElement){
        if(!isRunning && mapElement instanceof Animal){
            clickedAnimal = Optional.of((Animal) mapElement);
            updateClickedAnimal();
        }
    }

    public void updateStatistics(){
        Statistics statistics = simulation.getStatistics();
        animalCountLabel.setText(String.valueOf(statistics.getAnimalCount()));
        plantCountLabel.setText(String.valueOf(statistics.getPlantCount()));
        freeTilesCountLabel.setText(String.valueOf(statistics.getFreeTilesCount()));
        averageEnergyLabel.setText(String.format("%.2f", statistics.getAverageEnergyLevel()));
        averageLifeSpanOfDeadAnimalsLabel.setText(String.format("%.2f",statistics.getAverageLifeSpanOfDeadAnimals()));
        averageChildrenCountLabel.setText(String.format("%.2f",statistics.getAverageChildrenCount()));

        popularGenotypesVBox.getChildren().clear();
        popularGenotypesVBox.getChildren().add(new Label("Najpopularniejsze genotypy:"));

        List<Map.Entry<String, Long>> sortedGenotypes = statistics.getMostPopularGenotype();
        for(int i=0; i<Math.min(MIN_POPULAR_GENOTYPE,sortedGenotypes.size()); i++){
            Map.Entry<String, Long> entry = sortedGenotypes.get(i);
            Label label = new Label((i+1) + ". " + entry.getKey() + " : " + entry.getValue());
            popularGenotypesVBox.getChildren().add(label);
        }
    }

    private void updateClickedAnimal(){
        if(clickedAnimal.isPresent()){
            clickedAnimalInfo.getChildren().clear();
            clickedAnimalInfo.getChildren().add(new Label("Clicked animal:"));
            clickedAnimalInfo.getChildren().add(new Label("Pozycja: " + clickedAnimal.get().getPosition()));
            clickedAnimalInfo.getChildren().add(new Label("Genotyp: " + clickedAnimal.get().getGenotype().toString()));
            clickedAnimalInfo.getChildren().add(new Label("Energia: " + clickedAnimal.get().getEnergy()));
            clickedAnimalInfo.getChildren().add(new Label("Dzieci: " + clickedAnimal.get().getChildren()));
            clickedAnimalInfo.getChildren().add(new Label("Potomkowie: " + clickedAnimal.get().getDescendants()));
            if(clickedAnimal.get().getDeathDay() == 0){
                clickedAnimalInfo.getChildren().add(new Label("Dni: " + clickedAnimal.get().getDays()));
                markTile(clickedAnimal.get().getPosition(), new Color(1,0,0,0.5));
            } else {
                clickedAnimalInfo.getChildren().add(new Label("Dzien smierci: " + clickedAnimal.get().getDeathDay()));
            }
            clearButton.setVisible(true);
        }
    }

    public void clearClickedAnimal(){
        clickedAnimalInfo.getChildren().clear();
        clickedAnimal = Optional.empty();
        clearButton.setVisible(false);
    }

    public void setPauseButtons(){
        Button preferredTiles = new Button("Show preferred tiles");
        Button popularGenotype = new Button("Show animals with the most popular genotype");

        preferredTiles.setMinWidth(200);
        popularGenotype.setMinWidth(500);

        preferredTiles.setOnAction(event -> {
            List<Vector2d> preferredTilesPositions = worldMap.getPreferredTilesPositions();
            for(Vector2d position : preferredTilesPositions){
                markTile(position, new Color(0, 0.25, 0, 0.5));
            }
        });

        popularGenotype.setOnAction(event -> {
            List<Animal> animals = worldMap.getAnimals();
            String mostPopularGenotype = simulation.getStatistics().getMostPopularGenotype().get(0).getKey();
            for(Animal animal : animals){
                if(animal.getGenotype().getGenes().toString().equals(mostPopularGenotype)){
                    markTile(animal.getPosition(), new Color(1,0,1,0.5));
                }
            }
        });

        pauseButtons.getChildren().add(preferredTiles);
        pauseButtons.getChildren().add(popularGenotype);
    }

    public void removePauseButtons(){
        pauseButtons.getChildren().clear();
    }

    protected void markTile(Vector2d position, Color color){
        Rectangle rectangle = new Rectangle(CELL_WIDTH, CELL_HEIGHT, color);
        if(worldMap.objectAt(position).isPresent()){
            rectangle.setOnMouseClicked(event -> handleCellClick(worldMap.objectAt(position).get()));
        }
        mapGrid.add(rectangle, position.getX(), position.getY());
    }

    public void pauseResumeSimulation(){
        if(isRunning){
            pauseResume.setText("Resume");
            setPauseButtons();
            simulation.pauseSimulation();
            isRunning = false;
        } else {
            pauseResume.setText("Pause");
            removePauseButtons();
            simulation.resumeSimulation();
            isRunning = true;
        }
    }

    @Override
    public void mapChanged(WorldMap map){
        Platform.runLater(() -> {
            drawMap();
            updateStatistics();
            updateClickedAnimal();
        });
    }

    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() - 300;
        CELL_HEIGHT = screenHeight / Math.max(worldMap.upperRight().getY(),worldMap.upperRight().getX());
        CELL_WIDTH = screenHeight / Math.max(worldMap.upperRight().getY(),worldMap.upperRight().getX());
    }

    public void setSimulation(Simulation simulation){
        this.simulation = simulation;
    }

    public void setStage(Stage stage){
        stage.setOnCloseRequest(event -> handleWindowClose());
    }
}
