package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


public class MainController {
    @FXML
    private Pane mainPane;
    @FXML
    private TextField thicknessText;
    @FXML
    private TextField colorText;
    @FXML
    private TextField chosenFigureMsg;

    public enum Figure {LINE, CIRCLE, RECTANGLE}

    private Figure chosenFigure = Figure.RECTANGLE;
    private Point2D firstClick = null;
    private Point2D secondClick = null;
    private Color color = Color.BLACK;
    private final String chosenFigureMsgTemplate = "Chosen figure: ";

    private double orgSceneX, orgSceneY;
    private double orgScene1X, orgScene1Y;
    private boolean figureIsBeeingDragged = false;

    public void chosePoint(MouseEvent event) {
        if (figureIsBeeingDragged){
            figureIsBeeingDragged = false;
            return;
        }
        if (firstClick == null) {
            firstClick = new Point2D(event.getX(), event.getY());
            mainPane.getChildren().add(new Circle(event.getX(), event.getY(), 0.3, color));
        } else {
            secondClick = new Point2D(event.getX(), event.getY());
            Node node = draw();
            mainPane.getChildren().add(node);
            cleanClicks();
        }
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
        Circle circle = new Circle(firstClick.getX(), firstClick.getY(), radian, color);
        circle.setOnMousePressed((t) -> {
            figureIsBeeingDragged = true;
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });
        circle.setOnMouseDragged((t) -> {
            figureIsBeeingDragged = true;
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            Circle c = (Circle) (t.getSource());

            c.setCenterX(c.getCenterX() + offsetX);
            c.setCenterY(c.getCenterY() + offsetY);

            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

        });
        return circle;
    }


    private Node drawRectangle() {
        double width = secondClick.getX() - firstClick.getX();
        double height = secondClick.getY() - firstClick.getY();
        Rectangle rectangle = new Rectangle(firstClick.getX(), firstClick.getY(), width, height);
        rectangle.setFill(color);

        rectangle.setOnMousePressed((t) -> {
            figureIsBeeingDragged = true;
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });
        rectangle.setOnMouseDragged((t) -> {
            figureIsBeeingDragged = true;
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            Rectangle r = (Rectangle) (t.getSource());

            r.setX(r.getX() + offsetX);
            r.setY(r.getY() + offsetY);

            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

        });
        return rectangle;
    }

    private Node drawLine() {
        Line line = new Line(firstClick.getX(), firstClick.getY(), secondClick.getX(), secondClick.getY());
        line.setStroke(color);

        line.setOnMousePressed((t) -> {
            figureIsBeeingDragged = true;
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });
        line.setOnMouseDragged((t) -> {
            figureIsBeeingDragged = true;
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            Line l = (Line) (t.getSource());

            l.setStartX(l.getStartX() + offsetX);
            l.setStartY(l.getStartY() + offsetY);
            l.setEndX(l.getEndX() + offsetX);
            l.setEndY(l.getEndY() + offsetY);

            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

        });
        return line;
    }

    public void switchToRectangle(ActionEvent actionEvent) {
        chosenFigure = Figure.RECTANGLE;
        chosenFigureMsg.setText(chosenFigureMsgTemplate + "Rectangle");

    }

    public void switchToCircle(ActionEvent actionEvent) {
        chosenFigure = Figure.CIRCLE;
        chosenFigureMsg.setText(chosenFigureMsgTemplate + "Circle");
    }

    public void switchToLine(ActionEvent actionEvent) {
        chosenFigure = Figure.LINE;
        chosenFigureMsg.setText(chosenFigureMsgTemplate + "Line");
    }

    public void editConfig(ActionEvent actionEvent) {
        String chosenColor = colorText.getText();
        if (!chosenColor.matches("0[xX][0-9a-fA-F]{6,8}")) {
            colorText.setText("");
            colorText.setPromptText("Color must be in hexagonal format e.g. 0xFF00FF00");
            colorText.setStyle("-fx-prompt-text-fill: red;");
            return;
        }
        color = Color.web(chosenColor);
        colorText.setText(color.toString());
    }

    private void cleanClicks() {
        secondClick = null;
        firstClick = null;
    }
}
