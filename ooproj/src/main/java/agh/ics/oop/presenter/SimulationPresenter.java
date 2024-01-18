package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationConfigurator;
import agh.ics.oop.model.*;
import agh.ics.oop.model.animal.GenotypeFactory;
import agh.ics.oop.model.animal.RegularGenotypeFactory;
import agh.ics.oop.model.animal.SlightCorrectionGenotypeFactory;
import agh.ics.oop.model.map.GlobeMapFactory;
import agh.ics.oop.model.map.MapFactory;
import agh.ics.oop.model.map.UndergroundTunnelsFactory;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.statistics.Statistics;
import agh.ics.oop.model.statistics.StatisticsToCSV;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
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
    private Spinner<Integer> initialTunnelCountField;
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
    private CheckBox saveToCSV;

    private final Properties properties = new Properties();
    private final String propertiesPath = "src/main/resources/config/config.properties";
    private final SimulationEngine simulationEngine = new SimulationEngine();

    @FXML
    public void initialize(){
        loadConfig();
        configureMapBoundariesListener();
        configureUndergroundTunnelsListener();
        configureEnergyListener();
        configureSpinnerListener();
    }

    private void configureMapBoundariesListener(){
        mapHeightField.valueProperty().addListener(((observable, oldValue, newValue) -> setSpinnerWithMapBoundaries(newValue * mapWidthField.getValue())));
        mapWidthField.valueProperty().addListener(((observable, oldValue, newValue) -> setSpinnerWithMapBoundaries(newValue * mapHeightField.getValue())));

        initialPlantCountField.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue > mapWidthField.getValue() * mapHeightField.getValue()){
                initialPlantCountField.getValueFactory().setValue(oldValue);
            }
        }));
        initialAnimalCountField.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue > mapWidthField.getValue() * mapHeightField.getValue()){
                initialAnimalCountField.getValueFactory().setValue(oldValue);
            }
        }));
        numberOfPlantsGrowingPerDayField.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue > mapWidthField.getValue() * mapHeightField.getValue()){
                numberOfPlantsGrowingPerDayField.getValueFactory().setValue(oldValue);
            }
        }));
        initialTunnelCountField.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue > mapWidthField.getValue() * mapHeightField.getValue() / 3){
                initialTunnelCountField.getValueFactory().setValue(oldValue);
            }
        }));
    }

    private void setSpinnerWithMapBoundaries(int tiles){
        if (initialPlantCountField.getValue() > tiles){
            initialPlantCountField.getValueFactory().setValue(tiles);
        }
        if (initialAnimalCountField.getValue() > tiles){
            initialAnimalCountField.getValueFactory().setValue(tiles);
        }
        if (numberOfPlantsGrowingPerDayField.getValue() > tiles){
            numberOfPlantsGrowingPerDayField.getValueFactory().setValue(tiles);
        }
        if (initialTunnelCountField.getValue() > tiles / 3){
            initialTunnelCountField.getValueFactory().setValue(tiles/3);
        }
    }

    private void configureUndergroundTunnelsListener(){
        if (!"UndergroundTunnels".equals(mapTypeField.getValue())){
            initialTunnelCountField.setDisable(true);
        }
        mapTypeField.setOnAction(event -> {
            String selectedMapType = mapTypeField.getValue();
            initialTunnelCountField.setDisable(!"UndergroundTunnels".equals(selectedMapType));
        });
    }

    private void configureEnergyListener(){
        initialAnimalEnergyField.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue <= readyToReproduceEnergyField.getValue()){
                readyToReproduceEnergyField.getValueFactory().setValue(newValue - 1);
                if(readyToReproduceEnergyField.getValue() < reproduceEnergyLossField.getValue()){
                    reproduceEnergyLossField.getValueFactory().setValue(readyToReproduceEnergyField.getValue());
                }
            }
        }));

        readyToReproduceEnergyField.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue > initialAnimalEnergyField.getValue() - 1){
                readyToReproduceEnergyField.getValueFactory().setValue(oldValue);
            }
            if(newValue < reproduceEnergyLossField.getValue()){
                reproduceEnergyLossField.getValueFactory().setValue(newValue);
            }
        }));

        reproduceEnergyLossField.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue > readyToReproduceEnergyField.getValue()){
                reproduceEnergyLossField.getValueFactory().setValue(oldValue);
            }
        }));
    }

    private void configureSpinnerListener(){
        minMutationCountField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue > maxMutationCountField.getValue()){
                minMutationCountField.getValueFactory().setValue(oldValue);
            }
        });
        maxMutationCountField.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue < minMutationCountField.getValue()){
                maxMutationCountField.getValueFactory().setValue(oldValue);
            }
        }));
    }

    private void loadConfig(){
        try(FileInputStream fileInputStream = new FileInputStream(propertiesPath)){
            properties.load(fileInputStream);

            mapHeightField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("mapHeightField")));
            mapWidthField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("mapWidthField")));
            mapTypeField.setValue(properties.getProperty("mapTypeField"));
            initialTunnelCountField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("initialTunnelCountField")));
            initialPlantCountField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("initialPlantCountField")));
            plantEnergyField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("plantEnergyField")));
            numberOfPlantsGrowingPerDayField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("numberOfPlantsGrowingPerDayField")));
            initialAnimalCountField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("initialAnimalCountField")));
            initialAnimalEnergyField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("initialAnimalEnergyField")));
            readyToReproduceEnergyField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("readyToReproduceEnergyField")));
            reproduceEnergyLossField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("reproduceEnergyLossField")));
            minMutationCountField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("minMutationCountField")));
            maxMutationCountField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("maxMutationCountField")));
            mutationTypeField.setValue(properties.getProperty("mutationTypeField"));
            genotypeLengthField.getValueFactory().setValue(Integer.valueOf(properties.getProperty("genotypeLengthField")));
            saveToCSV.setSelected(Boolean.parseBoolean(properties.getProperty("saveToCSV")));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void saveConfig(){
        properties.setProperty("mapHeightField", String.valueOf(mapHeightField.getValue()));
        properties.setProperty("mapWidthField", String.valueOf(mapWidthField.getValue()));
        properties.setProperty("mapTypeField", mapTypeField.getValue());
        properties.setProperty("initialTunnelCountField", String.valueOf(initialTunnelCountField.getValue()));
        properties.setProperty("initialPlantCountField", String.valueOf(initialPlantCountField.getValue()));
        properties.setProperty("plantEnergyField", String.valueOf(plantEnergyField.getValue()));
        properties.setProperty("numberOfPlantsGrowingPerDayField", String.valueOf(numberOfPlantsGrowingPerDayField.getValue()));
        properties.setProperty("initialAnimalCountField", String.valueOf(initialAnimalCountField.getValue()));
        properties.setProperty("initialAnimalEnergyField", String.valueOf(initialAnimalEnergyField.getValue()));
        properties.setProperty("readyToReproduceEnergyField", String.valueOf(readyToReproduceEnergyField.getValue()));
        properties.setProperty("reproduceEnergyLossField", String.valueOf(reproduceEnergyLossField.getValue()));
        properties.setProperty("minMutationCountField", String.valueOf(minMutationCountField.getValue()));
        properties.setProperty("maxMutationCountField", String.valueOf(maxMutationCountField.getValue()));
        properties.setProperty("mutationTypeField", mutationTypeField.getValue());
        properties.setProperty("genotypeLengthField", String.valueOf(genotypeLengthField.getValue()));
        properties.setProperty("saveToCSV", String.valueOf(saveToCSV.isSelected()));

        try(FileOutputStream fileOutputStream = new FileOutputStream(propertiesPath)){
            properties.store(fileOutputStream, null);
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @FXML
    public void onSimulationStartClicked() throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationView.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationViewPresenter presenter = loader.getController();

        presenter.setStage(primaryStage);

        SimulationConfigurator configurator = createConfigurator();
        WorldMap map = configurator.mapType().createMap(configurator);

        Statistics statistics = new Statistics();

        map.addListener(statistics);
        map.addListener(presenter);
        presenter.setWorldMap(map);

        Simulation simulation = new Simulation(configurator, map, statistics);

        if(saveToCSV.isSelected()){
            map.addListener(new StatisticsToCSV(statistics, map.getID() + ".csv"));
        }

        simulationEngine.addSimulation(simulation);
        presenter.setSimulation(simulation);

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
        int initialTunnelCount = initialTunnelCountField.getValue();
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

        return new SimulationConfigurator(mapHeight,mapWidth,mapType,initialTunnelCount,
                initialPlantCount, plantEnergy, numberOfPlantsGrowingPerDay, initialAnimalCount,
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
            return new UndergroundTunnelsFactory();
        }
    }

    public void setStage(Stage primaryStage){
        primaryStage.setOnCloseRequest(event -> simulationEngine.stopSimulations());
    }
}
