package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.model.Point;

import java.io.IOException;
import java.util.List;

public class Controller extends Application {
    private Pane pane = new Pane();
    private ScrollPane scrollPane = new ScrollPane();
    private static List<Point> pointList = null;

    private enum Figure {LINE, CIRCLE, RECTANGLE}

    private Figure chosenFigure = Figure.CIRCLE;
    private Point firstClick = null;
    private Point secondClick = null;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        pane.setOnMouseClicked( event -> {
            if (firstClick == null) {
                firstClick = new Point(event.getX(), event.getY());
                pane.getChildren().add(new Circle(event.getX(), event.getY(), 0.3));
            } else {
                secondClick = new Point(event.getX(), event.getY());
                Node node = draw();
                pane.getChildren().add(node);
                cleanClicks();
            }
        });
        scrollPane.setContent(pane);

        primaryStage.setTitle("Projekt1");
        primaryStage.setScene(new Scene(pane, 600, 600));
        primaryStage.show();


    }

    private Node draw() {
        switch (chosenFigure) {
            case LINE:
                return drawLine();
            case RECTANGLE:
                return drawRectangle();
            case CIRCLE:
                return drawCircle();
        }
        return null;
    }

    private Node drawCircle() {
        double radian = firstClick.distance(secondClick);
        return new Circle(firstClick.getX(), firstClick.getY(), radian);
    }

    private Node drawRectangle() {
        double width = secondClick.getX() - firstClick.getX();
        double height = secondClick.getY() - firstClick.getY();
        return new Rectangle(firstClick.getX(), firstClick.getY(), width, height);
    }

    private Node drawLine() {
        return new Line(firstClick.getX(), firstClick.getY(), secondClick.getX(), secondClick.getY());
    }


    private void cleanClicks() {
        secondClick = null;
        firstClick = null;
    }
}
