package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationConfigurator;
import agh.ics.oop.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class SimulationPresenter{
    @FXML
    private Spinner<Integer> mapHeightField;
    @FXML
    private Spinner<Integer> mapWidthField;
    @FXML
    private ChoiceBox<String> mapTypeField;
    @FXML
    private Spinner<Integer> initialPlantCountField;
    @FXML
    private Spinner<Integer> plantEnergyField;
    @FXML
    private Spinner<Integer> numberOfPlantsGrowingPerDayField;
    @FXML
    private Spinner<Integer> initialAnimalCountField;
    @FXML
    private Spinner<Integer> initialAnimalEnergyField;
    @FXML
    private Spinner<Integer> readyToReproduceEnergyField;
    @FXML
    private Spinner<Integer> reproduceEnergyLossField;
    @FXML
    private Spinner<Integer> minMutationCountField;
    @FXML
    private Spinner<Integer> maxMutationCountField;
    @FXML
    private ChoiceBox<String> mutationTypeField;
    @FXML
    private Spinner<Integer> genotypeLengthField;

    @FXML
    public void onSimulationStartClicked() throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationView.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationViewPresenter presenter = loader.getController();

        SimulationConfigurator configurator = createConfigurator();
        WorldMap map = configurator.mapType().createMap(configurator);

        map.addListener(presenter);
        presenter.setWorldMap(map);

        SimulationEngine simulationEngine = new SimulationEngine(List.of(new Simulation(configurator, map, new Statistics())));
        simulationEngine.runAsync();

        configureStage(primaryStage, viewRoot, map.getID());
        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot, UUID worldMapUUID){
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app " + worldMapUUID);
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    private SimulationConfigurator createConfigurator(){
        int mapHeight = mapHeightField.getValue();
        int mapWidth = mapWidthField.getValue();
        MapFactory mapType = createMapFactory(mapTypeField.getValue());
        int initialPlantCount = initialPlantCountField.getValue();
        int plantEnergy = plantEnergyField.getValue();
        int numberOfPlantsGrowingPerDay = numberOfPlantsGrowingPerDayField.getValue();
        int initialAnimalCount = initialAnimalCountField.getValue();
        int initialAnimalEnergy = initialAnimalEnergyField.getValue();
        int readyToReproduceEnergy = readyToReproduceEnergyField.getValue();
        int reproduceEnergyLoss = reproduceEnergyLossField.getValue();
        int minMutationCount = minMutationCountField.getValue();
        int maxMutationCount = maxMutationCountField.getValue();
        GenotypeFactory mutationType = createGenotypeFactory(mutationTypeField.getValue());
        int genotypeLength = genotypeLengthField.getValue();

        //TODO exceptions

        return new SimulationConfigurator(mapHeight,mapWidth,mapType,initialPlantCount,
                plantEnergy, numberOfPlantsGrowingPerDay,initialAnimalCount,
                initialAnimalEnergy, readyToReproduceEnergy,reproduceEnergyLoss,
                minMutationCount, maxMutationCount,mutationType,genotypeLength);
    }

    private GenotypeFactory createGenotypeFactory(String mutationType){
        if(Objects.equals(mutationType, "RegularGenotype")){
            return new RegularGenotypeFactory();
        }else {
            return new SlightCorrectionGenotypeFactory();
        }
    }

    private MapFactory createMapFactory(String mapType){
        if(Objects.equals(mapType, "GlobeMap")){
            return new GlobeMapFactory();
        } else {
            //TODO UnderGroudtunnels
            return null;
        }
    }
}
