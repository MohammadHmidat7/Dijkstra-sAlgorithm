package com.example.algo3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class flights extends Application {


    @Override
    public void start(Stage primaryStage) {

        capital[] Graph = Read();
        // Root Pane
        Pane root = new Pane();

        // Load the map image
        ImageView mapView = new ImageView(new Image("C:\\Users\\ASUS\\Pictures\\Screenshots\\Screenshot 2024-12-28 210408.png"));
        mapView.setFitWidth(800); // Adjust size to fit the screen
        mapView.setFitHeight(500);
        root.getChildren().add(mapView);

        Label[] names = new Label[Graph.length];
        RadioButton[] buttons = new RadioButton[Graph.length];
//        RadioButton center = new RadioButton();//test center
//        int x_center = 380;
//        int y_center = 330;

//        center.setLayoutX(180 + 200);
//        center.setLayoutY(90 + 240);
//        root.getChildren().add(center);

        double w = ((double) 360 / 795);
        double h = ((double) 180 / 490);

        for (int i = 0; i < Graph.length; i++) {


            buttons[i] = new RadioButton();
            buttons[i].setText(Graph[i].name);


            if (Graph[i].x < 0 && Graph[i].y > 0) {
                buttons[i].setLayoutX(((Graph[i].x + 180) / w) - 15);
                buttons[i].setLayoutY(((90 - Graph[i].y) / h) + 90);
            } else if (Graph[i].x < 0 && Graph[i].y < 0) {
                buttons[i].setLayoutX(((Graph[i].x + 180) / w) - 15);
                buttons[i].setLayoutY(((90 - Graph[i].y) / h) + 75);
            } else if (Graph[i].x > 0 && Graph[i].y > 0) {
                if (Graph[i].y < 52) {
                    buttons[i].setLayoutX(((Graph[i].x + 180) / w) - 20);
                    buttons[i].setLayoutY(((90 - Graph[i].y) / h) + 100);
                } else {
                    buttons[i].setLayoutX(((Graph[i].x + 180) / w) - 17);
                    buttons[i].setLayoutY(((90 - Graph[i].y) / h) + 90);
                }
            } else {
                buttons[i].setLayoutX(((Graph[i].x + 180) / w) - 20);
                buttons[i].setLayoutY(((90 - Graph[i].y) / h) + 75);
            }

            buttons[i].setFont(Font.font(7));
            buttons[i].setTextFill(Color.BLACK);
            buttons[i].setStyle("-fx-font-weight: normal");

            root.getChildren().add(buttons[i]);
        }


        // UI Elements
        Label sourceLabel = new Label("Source:");
        sourceLabel.setFont(Font.font(14));
        sourceLabel.setStyle("-fx-font-weight: bold");
        ComboBox<String> sourceCombo = new ComboBox<>();
        for (capital capital : Graph) {
            sourceCombo.getItems().add(capital.name);
        }

        Label targetLabel = new Label("Target:");
        targetLabel.setFont(Font.font(14));
        targetLabel.setStyle("-fx-font-weight: bold");
        ComboBox<String> targetCombo = new ComboBox<>();
        for (capital capital : Graph) {
            targetCombo.getItems().add(capital.name);
        }


        Button runButton = new Button("Run");
        runButton.setDisable(true);

        Button resetButton = new Button("Reset");
        resetButton.setDisable(true);

        HBox hBox = new HBox(20, runButton, resetButton);
        hBox.setLayoutX(900);
        hBox.setLayoutY(170);

        Label filterLabel = new Label("Filter:");
        filterLabel.setFont(Font.font(14));
        filterLabel.setStyle("-fx-font-weight: bold");
        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("Distance", "Cost", "Time");
        filterCombo.setValue("Distance");


        Label pathLabel = new Label("Path:");
        pathLabel.setFont(Font.font(14));
        pathLabel.setStyle("-fx-font-weight: bold");
        TextArea pathField = new TextArea();
        pathField.setPrefColumnCount(10);
        pathField.setPrefHeight(40);
        pathField.setPrefWidth(100);
        pathField.setEditable(false);

        Label distanceLabel = new Label("Distance:");
        distanceLabel.setFont(Font.font(14));
        distanceLabel.setStyle("-fx-font-weight: bold");
        TextField distanceField = new TextField();
        distanceField.setEditable(false);

        Label costLabel = new Label("Cost:");
        costLabel.setFont(Font.font(14));
        costLabel.setStyle("-fx-font-weight: bold");
        TextField costField = new TextField();
        costField.setEditable(false);

        Label timeLabel = new Label("Time:");
        timeLabel.setFont(Font.font(14));
        timeLabel.setStyle("-fx-font-weight: bold");
        TextField timeField = new TextField();
        timeField.setEditable(false);

        // Layout

        VBox labels = new VBox(17, sourceLabel, targetLabel, filterLabel);
        VBox boxes = new VBox(10, sourceCombo, targetCombo, filterCombo);
        HBox comboBoxes = new HBox(10, labels, boxes);
        comboBoxes.setLayoutX(850);
        comboBoxes.setLayoutY(50);


        VBox labels2 = new VBox(17, distanceLabel, costLabel, timeLabel);
        VBox forArea = new VBox(35, pathLabel, labels2);
        VBox fields = new VBox(10, pathField, distanceField, costField, timeField);
        HBox results = new HBox(10, forArea, fields);
        results.setLayoutX(850);
        results.setLayoutY(300);
//        Line n = new Line();
//        n.setStartX(100);
//        n.setStartY(100);
//        n.setEndX(200);
//        n.setEndY(200);

        AtomicBoolean first = new AtomicBoolean(false);
        AtomicBoolean noSelected = new AtomicBoolean(true);

        mapView.setOnMouseMoved(mouseEvent -> {

            AtomicReference<String> source = new AtomicReference<>("");

            for (int i = 0; i < buttons.length; i++) {
                int finalI = i;
                buttons[i].setOnAction(event -> {
                    if (pathField.getText().isEmpty()) {
                        if (noSelected.get()) {

                            if (buttons[finalI].isSelected()) {
                                source.set(buttons[finalI].getText());
                                sourceCombo.setValue(buttons[finalI].getText());
                                buttons[finalI].setTextFill(Color.GREEN);
                                buttons[finalI].setStyle("-fx-font-weight: bold");
                                noSelected.set(false);
                                resetButton.setDisable(false);
                            }
//                            for (int j = 0; j < buttons.length; j++) {
//                                if (j != finalI) {
//                                    buttons[j].setDisable(true);
//                                }
//                            }
                            for (int j = 0; j < buttons.length; j++) {
                                for (int k = 0; k < targetCombo.getItems().size(); k++) {
                                    if (targetCombo.getItems().get(k).equals(buttons[j].getText())) {
                                        buttons[j].setDisable(false);
                                    }
                                }
                            }
                        } else {
                            for (int j = 0; j < buttons.length; j++) {
                                for (int k = 0; k < targetCombo.getItems().size(); k++) {
                                    if (targetCombo.getItems().get(k).equals(buttons[j].getText())) {
                                        buttons[j].setDisable(false);
                                        buttons[j].setTextFill(Color.BLACK);
                                        buttons[j].setStyle("-fx-font-weight: normal");
                                    }
                                }
                            }
                            if (buttons[finalI].isSelected()) {
                                targetCombo.setValue(buttons[finalI].getText());
                                buttons[finalI].setTextFill(Color.RED);
                                buttons[finalI].setStyle("-fx-font-weight: bold");
                                for (int j = 0; j < buttons.length; j++) {
                                    if (!buttons[j].isSelected()) {
                                        buttons[j].setDisable(true);
                                        noSelected.set(false);
                                    }
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < buttons.length; j++) {
                            if (!buttons[j].isDisable()) {
                                buttons[j].setSelected(true);
                            }
                        }
                    }
                });

            }

            boolean thereIsSelect = false;
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].isSelected()) {
                    thereIsSelect = true;
                }

            }
            if (!thereIsSelect) {
                noSelected.set(true);
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].setDisable(false);
                    buttons[i].setFont(Font.font(7));
                    buttons[i].setTextFill(Color.BLACK);
                    buttons[i].setStyle("-fx-font-weight: normal");
                    sourceCombo.setValue("");
                    targetCombo.setValue("");
                }
            } else if (noSelected.get()) {
                noSelected.set(true);
                targetCombo.setValue("");
                sourceCombo.setValue(source.get());
            }

        });

        sourceCombo.setOnAction(event -> {


//            for (int j = 0; j < buttons.length; j++) {
//                if (!buttons[j].getText().equals(sourceCombo.getValue())) {
//                    buttons[j].setDisable(true);
//                }
//            }


            try {


//                capital source = DijkstraImplement.getCapital(Graph, sourceCombo.getValue());
//                TableEntry[] Table = DijkstraImplement.initializeTable(source, Graph);
//                DijkstraImplement.Dijkstra(Table, filterCombo.getValue());

                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].getText().equals(sourceCombo.getValue())) {
                        buttons[i].setSelected(true);
                        resetButton.setDisable(false);
                        buttons[i].setTextFill(Color.GREEN);
                        buttons[i].setStyle("");
                        buttons[i].setStyle("-fx-font-size: 12px;-fx-font-weight: bold");
                        buttons[i].setManaged(true);
                        noSelected.set(false);
                    } else {
                        buttons[i].setSelected(false);
                        buttons[i].setFont(Font.font(7));
                        buttons[i].setTextFill(Color.BLACK);
                        buttons[i].setStyle("-fx-font-weight: normal");
                    }
                }


//                targetCombo.setValue("");
//                while (!targetCombo.getItems().isEmpty()) {
//                    targetCombo.getItems().remove(0);
//                }

//                for (TableEntry entry : Table) {
//
//                    if (entry.dist != Integer.MAX_VALUE && !entry.header.name.equals(source.name)) {
//                        targetCombo.getItems().add(entry.header.name);
//                    }
//                }

                for (int j = 0; j < buttons.length; j++) {
                    for (int k = 0; k < targetCombo.getItems().size(); k++) {
                        if (targetCombo.getItems().get(k).equals(buttons[j].getText())) {
                            buttons[j].setDisable(false);
                        }
                    }
                }

                for (int j = 0; j < buttons.length; j++) {
                    if (buttons[j].getText().equals(sourceCombo.getValue())) {
                        buttons[j].setDisable(false);
                        buttons[j].setTextFill(Color.GREEN);
                    }
                }


//                if (targetCombo.getItems().isEmpty()) {
//                    targetCombo.setValue("There is no target");
//                    runButton.setDisable(true);
//                }

                pathField.setText("");
                distanceField.setText("");
                timeField.setText("");
                costField.setText("");

            } catch (NullPointerException c) {

            }
        });

        targetCombo.setOnAction(event -> {

            if (!targetCombo.getValue().equals("There is no target") && !targetCombo.getValue().equals("")) {
                runButton.setDisable(false);
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].getText().equals(targetCombo.getValue())) {
                        buttons[i].setSelected(true);
                        buttons[i].setDisable(false);
                        buttons[i].setTextFill(Color.RED);
                        buttons[i].setStyle("-fx-font-size: 12px;-fx-font-weight: bold");
                    } else if (!buttons[i].getText().equals(sourceCombo.getValue())) {
                        buttons[i].setSelected(false);
                        buttons[i].setDisable(true);
                        buttons[i].setFont(Font.font(7));
                        buttons[i].setTextFill(Color.BLACK);
                        buttons[i].setStyle("-fx-font-weight: normal");
                    }
                }
            }
            pathField.setText("");
            distanceField.setText("");
            timeField.setText("");
            costField.setText("");
        });

        Line[] line = new Line[15];

        runButton.setOnAction(event -> {
            try {
                sourceCombo.setDisable(true);
                targetCombo.setDisable(true);
//                filterCombo.setDisable(true);
                runButton.setDisable(true);

                capital source = DijkstraImplement.getCapital(Graph, sourceCombo.getValue());
                TableEntry[] Table = DijkstraImplement.initializeTable(source, Graph);
                DijkstraImplement.Dijkstra(Table, filterCombo.getValue());

                TableEntry target = TableEntry.getEntry(Table, DijkstraImplement.getCapital(Graph, targetCombo.getValue()));
                    distanceField.setText(String.format("%.2f", (target.dist)) + " km");
                    costField.setText(String.valueOf(target.price) + " $");
                    timeField.setText(target.time + " min");
                    ArrayList<String> path = new ArrayList<>();
                    ArrayList<Double> x = new ArrayList<>();
                    ArrayList<Double> y = new ArrayList<>();
                    x.add(target.header.x);
                    y.add(target.header.y);
                    while (target.header != source) {
                        x.add(target.path.x);
                        y.add(target.path.y);

                        path.add(target.path.name);
                        target = TableEntry.getEntry(Table, DijkstraImplement.getCapital(Graph, target.path.name));
                    }

                    boolean b = true;
                    for (int i = 0; i < buttons.length; i++) {
                        for (int j = 0; j < path.size(); j++) {
                            if (buttons[i].getText().equals(path.get(j))) {
                                buttons[i].setDisable(false);
                                buttons[i].setSelected(true);
                                b = false;
                            }
                        }
                        if (b && !buttons[i].getText().equals(targetCombo.getValue()) && !buttons[i].getText().equals(sourceCombo.getValue())) {
                            buttons[i].setDisable(true);
                            buttons[i].setSelected(false);
                        }
                        b = true;
                    }
                    pathField.setText("");
                    pathField.appendText("From: ");
                    for (int i = path.size() - 1; i >= 0; i--) {
                        pathField.appendText(path.get(i) + ". ");
                        if (i != 0) {
                            pathField.appendText("To: ");
                        }
                    }
                    pathField.appendText("To: " + targetCombo.getValue() + ".");

                    for (int i = 0; i < x.size() - 1; i++) {

                        line[i] = new Line();

                        if (x.get(i) < 0 && y.get(i) > 0) {
                            line[i].setStartX(((x.get(i) + 180) / w) - 7);
                            line[i].setStartY(((90 - y.get(i)) / h) + 99);
                        } else if (x.get(i) < 0 && y.get(i) < 0) {
                            line[i].setStartX(((x.get(i) + 180) / w) - 7);
                            line[i].setStartY(((90 - y.get(i)) / h) + 83);
                        } else if (x.get(i) > 0 && y.get(i) > 0) {

                            if (y.get(i) < 52) {
                                line[i].setStartX(((x.get(i) + 180) / w) - 12);
                                line[i].setStartY(((90 - y.get(i)) / h) + 109);
                            } else {
                                line[i].setStartX(((x.get(i) + 180) / w) - 10);
                                line[i].setStartY(((90 - y.get(i)) / h) + 99);
                            }

                        } else {
                            line[i].setStartX(((x.get(i) + 180) / w) - 12);
                            line[i].setStartY(((90 - y.get(i)) / h) + 83);
                        }


                        if (x.get(i + 1) < 0 && y.get(i + 1) > 0) {
                            line[i].setEndX(((x.get(i + 1) + 180) / w) - 7);
                            line[i].setEndY(((90 - y.get(i + 1)) / h) + 99);
                        } else if (x.get(i + 1) < 0 && y.get(i + 1) < 0) {
                            line[i].setEndX(((x.get(i + 1) + 180) / w) - 7);
                            line[i].setEndY(((90 - y.get(i + 1)) / h) + 83);
                        } else if (x.get(i + 1) > 0 && y.get(i + 1) > 0) {

                            if (y.get(i + 1) < 52) {
                                line[i].setEndX(((x.get(i + 1) + 180) / w) - 12);
                                line[i].setEndY(((90 - y.get(i + 1)) / h) + 109);
                            } else {
                                line[i].setEndX(((x.get(i + 1) + 180) / w) - 10);
                                line[i].setEndY(((90 - y.get(i + 1)) / h) + 99);
                            }
                        } else {
                            line[i].setEndX(((x.get(i + 1) + 180) / w) - 13);
                            line[i].setEndY(((90 - y.get(i + 1)) / h) + 83);
                        }

                        line[i].setStroke(Color.BLUE);
                        line[i].setStrokeWidth(3);
                        root.getChildren().add(line[i]);


                    }

                } catch(NullPointerException e){
                    resetButton.setDisable(true);

                }

            if (pathField.getText().isEmpty()){
                pathField.setText("No path");
                costField.setText("");
                distanceField.setText("");
                timeField.setText("");
                resetButton.setDisable(false);
            }

        });

        filterCombo.setOnAction(event -> {
            try {


                if (!sourceCombo.getValue().equals("") && !targetCombo.getValue().equals("There is no target") && !targetCombo.getValue().equals("")) {
                    runButton.setDisable(false);
                    int i = 0;
                    while (line[i] != null) {
                        root.getChildren().remove(line[i]);
                        i++;
                    }
                    pathField.setText("");
                    distanceField.setText("");
                    timeField.setText("");
                    costField.setText("");
                }
            } catch (NullPointerException e) {
            }
        });

        resetButton.setOnAction(event -> {

            resetButton.setDisable(true);
            runButton.setDisable(true);
            sourceCombo.setDisable(false);
            targetCombo.setDisable(false);
            filterCombo.setDisable(false);
            noSelected.set(true);

            sourceCombo.setValue("");
            targetCombo.setValue("");


            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setSelected(false);
                buttons[i].setDisable(false);
                buttons[i].setFont(Font.font(7));
                buttons[i].setTextFill(Color.BLACK);
                buttons[i].setStyle("-fx-font-weight: normal");

            }
            int i = 0;
            while (line[i] != null) {
                root.getChildren().remove(line[i]);
                i++;
            }

        });

        root.getChildren().addAll(comboBoxes, hBox, results);

        // Scene and Stage
        Scene scene = new Scene(root, 1100, 500);
        primaryStage.setTitle("Flights");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static capital[] Read() {

        String filePath = "C:\\University\\dataAlgo.csv";
        capital[] Graph;
        try (Scanner input = new Scanner(new File(filePath))) {

            String[] line = input.nextLine().split(",");

            int numOfCapitals = Integer.parseInt(line[0]);
            Graph = new capital[numOfCapitals];
            int numOfEdges = Integer.parseInt(line[1]);



            for (int i = 0; i < Graph.length; i++) {
                try {
                    line = input.nextLine().split(",");
                    Graph[i] = (new capital(line[0], Double.parseDouble(line[1]), Double.parseDouble(line[2])));
                } catch (NullPointerException e) {

                }
            }


            while (numOfEdges != 0 && input.hasNextLine()) {
                numOfEdges--;
                try {
                    line = input.nextLine().split(",");
                    capital source = DijkstraImplement.getCapital(Graph, line[0]);
                    source.add(new Edge(DijkstraImplement.getCapital(Graph, line[1]), Double.parseDouble(line[2]), Integer.parseInt(line[3])));



                } catch (NullPointerException e) {


                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return Graph;
    }
}

