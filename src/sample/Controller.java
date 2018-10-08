package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;

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
    private static String text = "";
    private static List<Point> pointList = null;
    private static int startIndex = 0;

    private final int factor = 5;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        if (pointList == null) {
            pane.getChildren().add(new Text(50, 50, text));
            scrollPane.setContent(pane);
        } else {
            drawPoints();
            drawLines();
        }
        primaryStage.setTitle("Projekt1");
        primaryStage.setScene(new Scene(pane, 600, 600));
        primaryStage.show();
    }

    private void drawPoints() {
        int i = 0;
        for (Point point : pointList) {
            pane.getChildren().add(new Circle(point.getX() * factor, point.getY() * factor, 2));
            Text text = new Text(point.getX() * factor, point.getY() * factor, point.toString());
            if (i == 0) {
                text.setFill(RED);
            } else if (i % 2 == 0) {
                text.setFill(GRAY);
                pane.getChildren().add(text);
            } else {
                text.setFill(GREEN);
                pane.getChildren().add(text);
            }
            ++i;
        }
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

    public static void setPointList(List<Point> list, Point centerPoint) {
        pointList = list;
        pointList.add(centerPoint);
    }

    public static void setPointList(List<Point> list) {
        pointList = list;
    }

    public static void setStartPoint(int start) {
        startIndex = start;
    }

    public static void setText(String message) {
        text = message;
    }
}