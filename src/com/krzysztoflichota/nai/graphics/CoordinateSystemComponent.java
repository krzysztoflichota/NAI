package com.krzysztoflichota.nai.graphics;

import com.krzysztoflichota.nai.neuron.NeuronModel;

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
    public static final int POINT_SIZE = 10;

    private java.util.List<Point2D> points = new LinkedList<>();
    private NeuronModel neuron;

    public CoordinateSystemComponent(NeuronModel neuron) {
        this.neuron = neuron;
        setSize(WIDTH, HEIGHT);
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

        double x1 = (-(WIDTH/2));
        double x2 = (WIDTH/2);
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

        graphics2D.drawLine((int)getXInPixels(x1), (int)getYInPixels(y1), (int)getXInPixels(x2), (int)getYInPixels(y2));

        graphics2D.setStroke(presentStroke);
        graphics2D.setColor(presentColor);
    }

    private void drawPoints(Graphics2D graphics2D) {
        for(Point2D point : points){
            if(neuron.getOutput(point) == 1) drawRectanglePoint(graphics2D, point);
            else drawCirclePoint(graphics2D, point);
        }
    }

    private void drawRectanglePoint(Graphics2D graphics2D, Point2D point) {
        Color presentColor = graphics2D.getColor();
        graphics2D.setColor(Color.BLUE);
        graphics2D.fillRect((int)(getXInPixels(point.getX()) - POINT_SIZE/2), (int)(getYInPixels(point.getY()) - POINT_SIZE/2), POINT_SIZE, POINT_SIZE);
        graphics2D.setColor(presentColor);
    }

    private void drawCirclePoint(Graphics2D graphics2D, Point2D point) {
        Color presentColor = graphics2D.getColor();
        graphics2D.setColor(Color.RED);
        graphics2D.fillOval((int) (getXInPixels(point.getX()) - POINT_SIZE / 2), (int) (getYInPixels(point.getY()) - POINT_SIZE / 2), POINT_SIZE, POINT_SIZE);
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

        for(int i = 0; i <= WIDTH/2; i += GAP){
            g.drawLine(WIDTH/2 + i, 0, WIDTH/2 + i, HEIGHT);
            g.drawLine(WIDTH/2 - i, 0, WIDTH/2 - i, HEIGHT);
        }

        for(int i = 0; i <= HEIGHT/2; i += GAP){
            g.drawLine(0, HEIGHT/2 + i, WIDTH, HEIGHT/2 + i);
            g.drawLine(0, HEIGHT/2 - i, WIDTH, HEIGHT/2 - i);
        }

        Stroke presentStroke = g.getStroke();
        g.setStroke(new BasicStroke(3));
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        g.drawLine(0, HEIGHT/2, WIDTH, HEIGHT/2);

        g.setStroke(presentStroke);
        g.setColor(presentColor);
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
}
