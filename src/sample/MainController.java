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

import java.util.HashMap;


public class MainController {
    @FXML
    private Pane mainPane;
    @FXML
    private TextField resizeText;
    @FXML
    private TextField colorText;
    @FXML
    private TextField figureToDrawMsg;

    public enum Figure {LINE, CIRCLE, RECTANGLE}

    private Figure figureToDraw = Figure.RECTANGLE;
    private Point2D firstClick = null;
    private Point2D secondClick = null;
    private Color color = Color.BLACK;
    private final String figureToDrawMsgTemplate = "Chosen figure: ";
    private Figure chosenFigureType = null;
    private Node chosenFigure = null;

    private double orgSceneX, orgSceneY;
    private boolean figureIsBeingDragged = false;
    private double offsetX;
    private double offsetY;

    private HashMap<Node, Circle> characteristicspointList = new HashMap<>();

    public void chosePoint(MouseEvent event) {
        if (figureIsBeingDragged) {
            figureIsBeingDragged = false;
            return;
        }
        if (firstClick == null) {
            firstClick = new Point2D(event.getX(), event.getY());
            mainPane.getChildren().add(new Circle(event.getX(), event.getY(), 0.3, color));
        } else {
            secondClick = new Point2D(event.getX(), event.getY());
            draw();
            cleanClicks();
        }
    }

    private void draw() {
        switch (figureToDraw) {
            case LINE:
                drawLine();
                return;
            case RECTANGLE:
                drawRectangle();
                return;
            case CIRCLE:
                drawCircle();
                return;
        }
    }

    private void drawCircle() {
        double radian = firstClick.distance(secondClick);
        Circle circle = new Circle(firstClick.getX(), firstClick.getY(), radian, color);
        Circle characteristicPoint = new Circle(circle.getCenterX(), circle.getCenterY() - circle.getRadius(), 3, color);
        circle.setOnMousePressed((t) -> {
            chosenFigure = circle;
            chosenFigureType = Figure.CIRCLE;
            setFirstChosenFigurePoint(t);
        });
        circle.setOnMouseDragged((t) -> {
            dragCircle(t, circle);
            dragCharacteristics(characteristicPoint);
        });

        characteristicPoint.setOnMousePressed((t) -> setFirstChosenFigurePoint(t));
        characteristicPoint.setOnMouseDragged((t) -> {
                    dragCircleInVerticalOnly(t, characteristicPoint);
                    stretchCircle(circle);
                }
        );
        mainPane.getChildren().add(circle);
        mainPane.getChildren().add(characteristicPoint);
        chosenFigure = circle;
        chosenFigureType = Figure.CIRCLE;
        characteristicspointList.put(circle, characteristicPoint);
    }


    private void drawRectangle() {
        double width = secondClick.getX() - firstClick.getX();
        double height = secondClick.getY() - firstClick.getY();
        Rectangle rectangle = new Rectangle(firstClick.getX(), firstClick.getY(), width, height);
        rectangle.setFill(color);
        Circle characteristicPoint = new Circle(rectangle.getX(), rectangle.getY(), 3, color);
        rectangle.setOnMousePressed((t) -> {
            chosenFigure = rectangle;
            chosenFigureType = Figure.RECTANGLE;
            setFirstChosenFigurePoint(t);
        });
        rectangle.setOnMouseDragged((t) -> {
            dragRectangle(t, rectangle);
            dragCharacteristics(characteristicPoint);

        });

        characteristicPoint.setOnMousePressed((t) -> setFirstChosenFigurePoint(t));
        characteristicPoint.setOnMouseDragged((t) -> {
                    dragCircle(t, characteristicPoint);
                    stretchRectangle(rectangle);
                }
        );
        mainPane.getChildren().add(rectangle);
        mainPane.getChildren().add(characteristicPoint);
        chosenFigure = rectangle;
        chosenFigureType = Figure.RECTANGLE;
        characteristicspointList.put(rectangle, characteristicPoint);
    }

    private void drawLine() {
        Line line = new Line(firstClick.getX(), firstClick.getY(), secondClick.getX(), secondClick.getY());
        line.setStroke(color);
        Circle characteristicPoint = new Circle(line.getStartX(), line.getStartY(), 3, color);

        line.setOnMousePressed((t) -> {
            chosenFigure = line;
            chosenFigureType = Figure.LINE;
            setFirstChosenFigurePoint(t);
        });
        line.setOnMouseDragged((t) -> {
            figureIsBeingDragged = true;
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
        characteristicPoint.setOnMousePressed((t) -> setFirstChosenFigurePoint(t));
        characteristicPoint.setOnMouseDragged((t) -> {
                    dragCircle(t, characteristicPoint);
                    stretchLine(line);
                }
        );
        mainPane.getChildren().add(line);
        mainPane.getChildren().add(characteristicPoint);
        chosenFigure = line;
        chosenFigureType = Figure.LINE;
        characteristicspointList.put(line, characteristicPoint);
    }

    private void stretchRectangle(Rectangle rectangle) {
        figureIsBeingDragged = true;

        rectangle.setX(rectangle.getX() + offsetX);
        rectangle.setY(rectangle.getY() + offsetY);
        rectangle.setWidth(rectangle.getWidth() - offsetX);
        rectangle.setHeight(rectangle.getHeight() - offsetY);
    }

    private void stretchCircle(Circle circle) {
        figureIsBeingDragged = true;

        circle.setRadius(circle.getRadius() - offsetY);
    }

    private void stretchLine(Line line) {
        figureIsBeingDragged = true;

        line.setStartX(line.getStartX() + offsetX);
        line.setStartY(line.getStartY() + offsetY);
    }


    public void switchToRectangle(ActionEvent actionEvent) {
        figureToDraw = Figure.RECTANGLE;
        figureToDrawMsg.setText(figureToDrawMsgTemplate + "Rectangle");
    }

    public void switchToCircle(ActionEvent actionEvent) {
        figureToDraw = Figure.CIRCLE;
        figureToDrawMsg.setText(figureToDrawMsgTemplate + "Circle");
    }

    public void switchToLine(ActionEvent actionEvent) {
        figureToDraw = Figure.LINE;
        figureToDrawMsg.setText(figureToDrawMsgTemplate + "Line");
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


    private void setFirstChosenFigurePoint(MouseEvent click) {
        figureIsBeingDragged = true;
        orgSceneX = click.getSceneX();
        orgSceneY = click.getSceneY();
    }

    public void modifyFigure(ActionEvent actionEvent) {
        double factor = Double.parseDouble(resizeText.getText());
        switch (chosenFigureType) {
            case RECTANGLE:
                resizeRectangle(factor);
                return;
            case LINE:
                resizeLine(factor);
                return;
            case CIRCLE:
                resizeCircle(factor);
                return;
        }

    }

    private void resizeRectangle(double factor) {
        Rectangle rectangle = (Rectangle) chosenFigure;

        rectangle.setHeight(rectangle.getHeight() * factor);
        rectangle.setWidth(rectangle.getWidth() * factor);

    }

    private void resizeLine(double factor) {
        Line line = (Line) chosenFigure;

        line.setEndX(line.getEndX() * factor);

        Circle point = characteristicspointList.get(line);
    }

    private void resizeCircle(double factor) {
        Circle circle = (Circle) chosenFigure;

        double prevRadius = circle.getRadius();

        circle.setRadius(circle.getRadius() * factor);
        Circle point = characteristicspointList.get(circle);
        point.setCenterY(point.getCenterY() + (prevRadius - circle.getRadius()));
    }

    private void dragRectangle(MouseEvent t, Rectangle r) {
        figureIsBeingDragged = true;
        offsetX = t.getSceneX() - orgSceneX;
        offsetY = t.getSceneY() - orgSceneY;
        r.setX(r.getX() + offsetX);
        r.setY(r.getY() + offsetY);

        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();

    }

    private void dragCircle(MouseEvent t, Circle c) {
        figureIsBeingDragged = true;
        offsetX = t.getSceneX() - orgSceneX;
        offsetY = t.getSceneY() - orgSceneY;

        c.setCenterX(c.getCenterX() + offsetX);
        c.setCenterY(c.getCenterY() + offsetY);

        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();
    }

    private void dragCircleInVerticalOnly(MouseEvent t, Circle c) {
        figureIsBeingDragged = true;
        offsetY = t.getSceneY() - orgSceneY;

        c.setCenterY(c.getCenterY() + offsetY);

        orgSceneY = t.getSceneY();
    }

    private void dragCharacteristics(Circle c) {
        figureIsBeingDragged = true;
        c.setCenterX(c.getCenterX() + offsetX);
        c.setCenterY(c.getCenterY() + offsetY);
    }
}


