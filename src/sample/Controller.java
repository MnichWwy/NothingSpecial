package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.Point;

import java.io.IOException;
import java.util.List;

import static javafx.scene.paint.Color.GRAY;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

public class Controller extends Application {
    private Pane pane = new Pane();
    private ScrollPane scrollPane = new ScrollPane();
    private static List<Point> pointList = null;
    private static int startIndex = 0;

    private final int factor = 5;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        pane.setOnMouseClicked((EventHandler<MouseEvent>) event -> {
            Circle circle = new Circle(event.getX(), event.getY(), 2);
            pane.getChildren().add(circle);
        });
        scrollPane.setContent(pane);

        primaryStage.setTitle("Projekt1");
        primaryStage.setScene(new Scene(pane, 600, 600));
        primaryStage.show();


    }

    private void drawLines() {
        int index = startIndex;
        for (int i = 0; i < pointList.size() - 1; ++i) {
            Point start = pointList.get(index);
            ++index;
            if (index == pointList.size()) {
                index = 0;
            }
            Point end = pointList.get(index);
            pane.getChildren().add(new Line(start.getX() * factor, start.getY() * factor, end.getX() * factor, end.getY() * factor));
        }
    }


}