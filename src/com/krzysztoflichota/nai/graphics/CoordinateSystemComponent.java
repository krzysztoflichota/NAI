package com.krzysztoflichota.nai.graphics;

import com.krzysztoflichota.nai.neuron.NeuronModel;
import com.krzysztoflichota.nai.neuron.PerceptronModel;
import com.krzysztoflichota.nai.utilities.ClassifiedPoint;
import com.krzysztoflichota.nai.utilities.PointType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;


/**
 * Created by Krzysztof Lichota on 2015-03-20.
 * krzysztoflichota.com
 */
public class CoordinateSystemComponent extends JComponent {

    public int WIDTH = 800;
    public int HEIGHT = 600;
    public double GAP = 20;
    public int OFFSET_X = 0;
    public int OFFSET_Y = 0;
    public static final int POINT_SIZE = 10;

    private java.util.List<Point2D> points = new LinkedList<>();
    private NeuronModel neuron;
    private boolean learningMode;

    public CoordinateSystemComponent(NeuronModel neuron) {
        this.neuron = neuron;
        setSize(WIDTH, HEIGHT);
        learningMode = false;
    }

    public NeuronModel getNeuron() {
        return neuron;
    }

    public void setNeuron(NeuronModel neuron) {
        this.neuron = neuron;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public void addPoint(Point2D point){
        points.add(point);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;

        fixSize();
        paintBackground(graphics2D);
        drawNet(graphics2D);
        drawNeuron(graphics2D);
        drawPoints(graphics2D);
    }

    private void drawNeuron(Graphics2D graphics2D) {
        Stroke presentStroke = graphics2D.getStroke();
        Color presentColor = graphics2D.getColor();

        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.setColor(Color.GREEN);

        double x1 = (-(WIDTH/2)) - OFFSET_X*10;
        double x2 = (WIDTH/2) + OFFSET_X*10;
        double y1;
        double y2;

        if(neuron.getWeightY() != 0) {
            y1 = ((neuron.getWeightX() * x1 + neuron.getTeta()) / neuron.getWeightY());
            y2 = ((neuron.getWeightX() * x2 + neuron.getTeta()) / neuron.getWeightY());
        }
        else{
            x1 = (neuron.getTeta()/neuron.getWeightX());
            x2 = (neuron.getTeta()/neuron.getWeightX());

            y1 = -HEIGHT/2;
            y2 = HEIGHT/2;
        }

        graphics2D.drawLine((int)getXInPixels(x1) + OFFSET_X, (int)getYInPixels(y1) + OFFSET_Y, (int)getXInPixels(x2) + OFFSET_X, (int)getYInPixels(y2) + OFFSET_Y);

        graphics2D.setStroke(presentStroke);
        graphics2D.setColor(presentColor);
    }

    private void drawPoints(Graphics2D graphics2D) {
        if(!learningMode) {
            for (Point2D point : points) {
                if (neuron.getOutput(point) == 1) drawRectanglePoint(graphics2D, point);
                else drawCirclePoint(graphics2D, point);
            }
        }
        else{
            PerceptronModel perceptron = (PerceptronModel) neuron;
            for (ClassifiedPoint point : perceptron.getLearningSet()) {
                if (point.getType() == PointType.RECTANGLE) drawRectanglePoint(graphics2D, point);
                else drawCirclePoint(graphics2D, point);
            }
        }
    }

    private void drawRectanglePoint(Graphics2D graphics2D, Point2D point) {
        Color presentColor = graphics2D.getColor();
        graphics2D.setColor(Color.BLUE);
        graphics2D.fillRect((int)(getXInPixels(point.getX()) - POINT_SIZE/2) + OFFSET_X, (int)(getYInPixels(point.getY()) - POINT_SIZE/2) + OFFSET_Y, POINT_SIZE, POINT_SIZE);
        graphics2D.setColor(presentColor);
    }

    private void drawCirclePoint(Graphics2D graphics2D, Point2D point) {
        Color presentColor = graphics2D.getColor();
        graphics2D.setColor(Color.RED);
        graphics2D.fillOval((int) (getXInPixels(point.getX()) - POINT_SIZE / 2) + OFFSET_X, (int) (getYInPixels(point.getY()) - POINT_SIZE / 2) + OFFSET_Y, POINT_SIZE, POINT_SIZE);
        graphics2D.setColor(presentColor);
    }

    private void paintBackground(Graphics2D g){
        Color presentColor = g.getColor();
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(presentColor);
    }

    private void drawNet(Graphics2D g){
        Color presentColor = g.getColor();
        g.setColor(Color.BLACK);

        //Od środka w lewo pionowe
        for(int i = 0; getCenterWidth() - i >= 0; i += GAP){
            g.drawLine(getCenterWidth() - i, 0, getCenterWidth() - i, HEIGHT);
        }

        //Od środka w prawo pionowe
        for(int i = 0; getCenterWidth() + i <= WIDTH; i += GAP){
            g.drawLine(getCenterWidth() + i, 0, getCenterWidth() + i, HEIGHT);
        }

        //Od środka w górę poziome
        for(int i = 0; getCenterHeight() - i >= 0; i += GAP){
            g.drawLine(0, getCenterHeight() - i, WIDTH, getCenterHeight() - i);
        }

        //Od środka w dół poziome
        for(int i = 0; getCenterHeight() + i <= HEIGHT; i += GAP){
            g.drawLine(0, getCenterHeight() + i, WIDTH, getCenterHeight() + i);
        }

        Stroke presentStroke = g.getStroke();
        g.setStroke(new BasicStroke(3));
        g.drawLine(getCenterWidth(), 0, getCenterWidth(), HEIGHT);
        g.drawLine(0, getCenterHeight(), WIDTH, getCenterHeight());

        g.setStroke(presentStroke);
        g.setColor(presentColor);
    }

    private int getCenterWidth(){
        return WIDTH/2 + OFFSET_X;
    }

    private int getCenterHeight(){
        return HEIGHT/2 + OFFSET_Y;
    }

    public void clearPoints(){
        points.clear();
    }

    public double getXInPixels(double x){
        return x*GAP + WIDTH / 2;
    }

    public double getYInPixels(double y){
        return y*GAP + HEIGHT / 2;
    }

    public double getPixelsInX(double x){
        return (x - WIDTH / 2)/GAP;
    }

    public double getPixelsInY(double y){
        return -(y - HEIGHT / 2)/GAP;
    }

    private void fixSize(){
        WIDTH = getWidth();
        HEIGHT = getHeight();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    public void zoomIn(double zoom){
        GAP *= 1 + zoom;
        OFFSET_X *= 1 + zoom;
        OFFSET_Y *= 1 + zoom;
    }

    public void zoomOut(double zoom){
        GAP *= 1 - zoom;
        OFFSET_X *= 1 - zoom;
        OFFSET_Y *= 1 - zoom;
    }

    public Point2D getPoint(int xPixels, int yPixels){
        double offset = POINT_SIZE / 2;
        for(Point2D point : points){
            double x = getXInPixels(point.getX());
            double y = getYInPixels(point.getY());

            if(xPixels >= x - offset && xPixels <= x + offset && yPixels >= y - offset && yPixels <= y + offset) return point;
        }

        return null;
    }

    public Point2D getPoint(int xPixels, int yPixels, List<? extends Point2D> points){
        double offset = POINT_SIZE / 2;
        for(Point2D point : points){
            double x = getXInPixels(point.getX());
            double y = getYInPixels(point.getY());

            if(xPixels >= x - offset && xPixels <= x + offset && yPixels >= y - offset && yPixels <= y + offset) return point;
        }

        return null;
    }

    public void setLearningMode(boolean learningMode) {
        this.learningMode = learningMode;
        if(!learningMode){
            PerceptronModel perceptron = (PerceptronModel) neuron;
            for(Point2D.Double point : perceptron.getLearningSet()) points.add(point);
        }
    }
}
