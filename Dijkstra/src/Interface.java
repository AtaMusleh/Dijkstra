import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Interface extends Application {
	private static final double MAP_WIDTH = 600;
	private static final double MAP_HEIGHT = 742;
//1	private static final double MyMin = 31.60290;
//	private static final double MyMax = 31.23000;
//	private static final double MxMin = 34.20316;
//	private static final double MxMax = 34.56786;
//2	private static final double MyMin = 31.61728;
//	private static final double MyMax = 31.21815;
//	private static final double MxMin = 34.18358;
//	private static final double MxMax = 34.58256;
//Most Correct

	private static final double MyMin = 31.60156;
	private static final double MyMax = 31.21419;
	private static final double MxMin = 34.19591;
	private static final double MxMax = 34.56944;
	private static Pane mapPane;
	private static ComboBox<String> source;
	private static ComboBox<String> target;
	private Graph graph;
	private Circle selectedSourceCircle = null;
	private Circle selectedTargetCircle = null;
	private Vertex selectedSource = null;
	private Vertex selectedTarget = null;
	List<Circle> circles = new ArrayList<>();

	@Override
	public void start(Stage stage) throws Exception {
		Dijkstra dijkstra = new Dijkstra();
		String filePath = "vertexes.txt";
		mapPane = new Pane();
		Image backgroundImage = new Image("Map1.png");
		ImageView backgroundImageView = new ImageView(backgroundImage);
		mapPane.getChildren().add(backgroundImageView);

		Label sourceL = new Label("Source:");
		Label targetL = new Label("Target:");
		sourceL.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		targetL.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		source = new ComboBox<String>();
		target = new ComboBox<String>();
		source.setValue(null);
		target.setValue(null);
		source.setPrefWidth(120);
		target.setPrefWidth(120);
		source.setStyle("-fx-background-radius: 15; " + "-fx-border-radius: 15; " + "-fx-border-width: 2;");
		target.setStyle("-fx-background-radius: 15; " + "-fx-border-radius: 15; " + "-fx-border-width: 2;");
		HBox sou = new HBox(10, sourceL, source);
		sou.setPadding(new Insets(20, 0, 0, 0));
		HBox tar = new HBox(15, targetL, target);
		Button run = new Button("Run");
		run.setPrefWidth(90);
		run.setPrefHeight(40);
		run.setStyle("-fx-background-radius: 20; " + "-fx-border-radius: 15; ");
		run.setFont(new Font(16));
		Button clear = new Button("Clear");
		clear.setStyle("-fx-background-radius: 20; " + "-fx-border-radius: 15; ");
		clear.setFont(new Font(16));
		clear.setPrefWidth(90);
		clear.setPrefHeight(40);
		clear.setDisable(true);
		VBox v1 = new VBox(20, sou, tar, run);
		v1.setAlignment(Pos.CENTER);
		Label pathL = new Label("Path:");
		pathL.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		TextArea pathTa = new TextArea();
		pathTa.setEditable(false);
		pathTa.setPrefSize(200, 200);
		VBox v2 = new VBox(10, pathL, pathTa);
		Label dis = new Label("Distance:");
		dis.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		TextField disT = new TextField();
		disT.setEditable(false);
		VBox v3 = new VBox(10, dis, disT);
		VBox v4 = new VBox(clear);
		v4.setAlignment(Pos.CENTER);
		VBox v = new VBox(30, v1, v2, v3, v4);
		HBox h = new HBox(10, mapPane, v);

		graph = readGraphFromFile("vertexes.txt");

		mapPane.getChildren().forEach(node -> {
			if (node instanceof Circle) {
				Circle circle = (Circle) node;
				circles.add(circle);

				circle.setOnMouseClicked(event -> {
					if (event.getButton() == MouseButton.PRIMARY) {
						handlePrimaryClick(circle);
					} else if (event.getButton() == MouseButton.SECONDARY) {
						handleSecondaryClick(circle);
					}
				});
			}
		});

		source.setOnAction(event -> {
			String selectedCity = source.getValue();
			updateSource(selectedCity);
		});

		target.setOnAction(event -> {
			String selectedCity = target.getValue();
			updateTarget(selectedCity);
		});

		run.setOnAction(e -> {
			if ((source.getValue() != null && target.getValue() != null) && (source.getValue() != target.getValue())) {
				run.setDisable(true);
				clear.setDisable(false);
				Vertex sourceV = graph.getVertexByName(source.getValue());
				Vertex destinationV = graph.getVertexByName(target.getValue());
				List<Vertex> path = dijkstra.shortestPath(sourceV, destinationV, graph);
				double totalDistance = 0.0;

				if (path != null) {
					for (int i = 0; i < path.size() - 1; i++) {
						Vertex currentVertex = path.get(i);
						Vertex nextVertex = path.get(i + 1);

						double startX = (((MAP_WIDTH - 0) * (currentVertex.getLongitude() - MxMin)) / (MxMax - MxMin));
						double startY = (((MAP_HEIGHT - 0) * (currentVertex.getLatitdue() - MyMin)) / (MyMax - MyMin));

						double endX = (((MAP_WIDTH - 0) * (nextVertex.getLongitude() - MxMin)) / (MxMax - MxMin));
						double endY = (((MAP_HEIGHT - 0) * (nextVertex.getLatitdue() - MyMin)) / (MyMax - MyMin));

						Line pathLine = new Line(startX, startY, endX, endY);
						pathLine.setStroke(Color.BLACK);
						pathLine.setStrokeWidth(5.0);
						mapPane.getChildren().add(pathLine);
						pathTa.appendText("Step " + (i + 1) + " -> " + currentVertex.getName() + " -> "
								+ nextVertex.getName() + "\n");
						double distanceBetweenBuildings = calculateDistances(currentVertex, nextVertex);
						totalDistance += distanceBetweenBuildings;

					}
					// must edit ---------------------
					disT.setText(String.format("%.3f", totalDistance) + " km");
				} else if (path == null) {
					pathTa.setText("There is no path for thic city!");

				}
			} else if (source.getValue() == null || target.getValue() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText("Null source or Null target");
				alert.setContentText("Choose a source and a target");

				// Show the alert
				alert.showAndWait();
			} else if (source.getValue() == target.getValue()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText("The source and the target must be differnt");
				alert.setContentText("Change the source or the target");

				alert.showAndWait();
			}

		});
		clear.setOnAction(e -> {
			clear.setDisable(true);
			run.setDisable(false);
			pathTa.clear();
			disT.clear();
			mapPane.getChildren().clear();
			mapPane.getChildren().add(backgroundImageView);
			graph = readGraphFromFile(filePath);
			source.setValue(null);
			target.setValue(null);
			circles.clear();
			mapPane.getChildren().forEach(node -> {
				if (node instanceof Circle) {
					Circle circle = (Circle) node;
					circles.add(circle);
					circle.setOnMouseClicked(event -> {
						if (event.getButton() == MouseButton.PRIMARY) {
							handlePrimaryClick(circle);
						} else if (event.getButton() == MouseButton.SECONDARY) {
							handleSecondaryClick(circle);
						}
					});
				}
			});

		});
		Scene scene = new Scene(h, backgroundImage.getWidth() + 230, backgroundImage.getHeight());
		stage.setScene(scene);
		stage.show();
	}

	private Vertex getVertexFromCircle(Circle circle) {
		for (Vertex vertex : graph.getVertices()) {
			double x = (((MAP_WIDTH - 0) * (vertex.getLongitude() - MxMin)) / (MxMax - MxMin));
			double y = (((MAP_HEIGHT - 0) * (vertex.getLatitdue() - MyMin)) / (MyMax - MyMin));

			if (circle.getCenterX() == x && circle.getCenterY() == y) {
				return vertex;
			}
		}
		return null;
	}

	private void handlePrimaryClick(Circle circle) {
		if (circle.getFill() == Color.RED) {
			if (selectedSourceCircle == null) {
				selectedSourceCircle = circle;
				selectedSource = getVertexFromCircle(circle);
				circle.setFill(Color.DARKRED);
				source.setValue(selectedSource.getName());
			} else if (selectedTargetCircle == null) {
				selectedTargetCircle = circle;
				selectedTarget = getVertexFromCircle(circle);
				circle.setFill(Color.DARKRED);
				target.setValue(selectedTarget.getName());
			}
		} else if (circle.getFill() == Color.DARKRED) {
			circle.setFill(Color.RED);
			if (circle == selectedSourceCircle) {
				selectedSourceCircle = null;
				selectedSource = null;
				source.setValue(null);
			} else if (circle == selectedTargetCircle) {
				selectedTargetCircle = null;
				selectedTarget = null;
				target.setValue(null);
			}
		}
	}

	private void handleSecondaryClick(Circle circle) {
		if (circle.getFill() == Color.GREEN) {
			circle.setFill(Color.RED);
			if (circle == selectedSourceCircle) {
				selectedSourceCircle = null;
				selectedSource = null;
				source.setValue(null);
			} else if (circle == selectedTargetCircle) {
				selectedTargetCircle = null;
				selectedTarget = null;
				target.setValue(null);
			}
		}
	}

	private void updateSource(String newSourceCity) {
		if (selectedSourceCircle != null) {
			selectedSourceCircle.setFill(Color.RED);
		}

		selectedSource = graph.getVertexByName(newSourceCity);
		selectedSourceCircle = getCircleFromVertex(selectedSource);

		if (selectedSourceCircle != null) {
			selectedSourceCircle.setFill(Color.DARKRED);
		}
	}

	private void updateTarget(String newTargetCity) {
		if (selectedTargetCircle != null) {
			selectedTargetCircle.setFill(Color.RED);
		}

		selectedTarget = graph.getVertexByName(newTargetCity);
		selectedTargetCircle = getCircleFromVertex(selectedTarget);

		if (selectedTargetCircle != null) {
			selectedTargetCircle.setFill(Color.GREEN);
		}
	}

	private Circle getCircleFromVertex(Vertex vertex) {
		for (Circle circle : circles) {
			Vertex circleVertex = getVertexFromCircle(circle);
			if (circleVertex != null && circleVertex.equals(vertex)) {
				return circle;
			}
		}
		return null;
	}

	public static Graph readGraphFromFile(String filePath) {

		Graph graph = new Graph();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String firstLine = reader.readLine();
			String[] num = firstLine.split(",");

			// Read Cities
			for (int i = 1; i < Integer.parseInt(num[0]); i++) {
				String line = reader.readLine();
				if (line.equals(firstLine)) {
					i--;
					continue;
				}

				String[] locationInfo = line.split(",");
				String cityName = locationInfo[0];
				double latitude = Double.parseDouble(locationInfo[1]);
				double longitude = Double.parseDouble(locationInfo[2]);
				String isStreet = locationInfo[3];
				double x = (((MAP_WIDTH - 0) * (longitude - MxMin)) / (MxMax - MxMin));
				double y = (((MAP_HEIGHT - 0) * (latitude - MyMin)) / (MyMax - MyMin));
				if (isStreet.equals("0")) {
					source.getItems().add(cityName);
					target.getItems().add(cityName);
					Circle point = new Circle(x, y, 5, Color.RED);
					mapPane.getChildren().add(point);
					Label label = new Label(cityName);
					label.setLayoutX(x - 25);
					label.setLayoutY(y - 20);
					mapPane.getChildren().add(label);
				}
				Vertex vertex = new Vertex(cityName, latitude, longitude);
				graph.addVertex(vertex);
			}

			for (int i = 0; i < Integer.parseInt(num[1]); i++) {
				String line = reader.readLine();
				if (!line.isEmpty()) {
					String[] adjacencyInfo = line.split(",");
					if (adjacencyInfo.length >= 2) {
						String sourceCityName = adjacencyInfo[0];
						String adjacentCityName = adjacencyInfo[1];

						Vertex sourceVertex = graph.getVertexByName(sourceCityName);
						Vertex adjacentVertex = graph.getVertexByName(adjacentCityName);

						// double cost = calculateDistance(sourceVertex, adjacentVertex);
						double cost = calculateDistances(sourceVertex, adjacentVertex);

						graph.addEdge(sourceVertex, adjacentVertex, cost);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return graph;
	}

	private static double calculateDistances(Vertex v1, Vertex v2) {
		double earthRadiusKm = 6371.0; // Approximate Earth radius in kilometers

		double lat1 = Math.toRadians(v1.getLatitdue());
		double lon1 = Math.toRadians(v1.getLongitude());
		double lat2 = Math.toRadians(v2.getLatitdue());
		double lon2 = Math.toRadians(v2.getLongitude());

		double dLat = lat2 - lat1;
		double dLon = lon2 - lon1;

		double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);

		double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

		double distance = earthRadiusKm * c;

		return distance;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
