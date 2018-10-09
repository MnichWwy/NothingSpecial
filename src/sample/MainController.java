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
import sample.model.Point;



public class MainController {
    @FXML
    private Pane mainPane;
    @FXML
    private TextField thicknessText;
    @FXML
    private TextField colorText;
    @FXML
    private TextField chosenFigureMsg;

    private enum Figure {LINE, CIRCLE, RECTANGLE}

    private Figure chosenFigure = Figure.RECTANGLE;
    private Point firstClick = null;
    private Point secondClick = null;
    private Color color = Color.BLACK;
    private final String chosenFigureMsgTemplate = "Chosen figure: ";

    public void chosePoint(MouseEvent event) {
        if (firstClick == null) {
            firstClick = new Point(event.getX(), event.getY());
            mainPane.getChildren().add(new Circle(event.getX(), event.getY(), 0.3, color));
        } else {
            secondClick = new Point(event.getX(), event.getY());
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
        return new Circle(firstClick.getX(), firstClick.getY(), radian, color);
    }

    private Node drawRectangle() {
        double width = secondClick.getX() - firstClick.getX();
        double height = secondClick.getY() - firstClick.getY();
        Rectangle rectangle= new Rectangle(firstClick.getX(), firstClick.getY(), width, height);
        rectangle.setFill(color);
        return rectangle;
    }

    private Node drawLine() {
        Line line = new Line(firstClick.getX(), firstClick.getY(), secondClick.getX(), secondClick.getY());
        line.setStroke(color);
        return line;
    }

    public void switchToRectangle(ActionEvent actionEvent) {
        chosenFigure=Figure.RECTANGLE;
        chosenFigureMsg.setText(chosenFigureMsgTemplate + "Rectangle");

    }

    public void switchToCircle(ActionEvent actionEvent) {
        chosenFigure=Figure.CIRCLE;
        chosenFigureMsg.setText(chosenFigureMsgTemplate + "Circle");
    }

    public void switchToLine(ActionEvent actionEvent) {
        chosenFigure=Figure.LINE;
        chosenFigureMsg.setText(chosenFigureMsgTemplate + "Line");
    }

    public void editConfig(ActionEvent actionEvent) {
        String chosenColor = colorText.getText();
        if (! chosenColor.matches("0[xX][0-9a-fA-F]{6,8}")){
            colorText.setText("");
            colorText.setPromptText("Color must be in hexagonal format e.g. 0xFF00FF00");
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
