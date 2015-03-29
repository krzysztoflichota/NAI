package com.krzysztoflichota.nai.utilities;

import com.krzysztoflichota.nai.Neuron;
import com.krzysztoflichota.nai.graphics.CoordinateSystemComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.math.BigDecimal;

/**
 * Created by Krzysztof Lichota on 2015-03-21.
 * krzysztoflichota.com
 */
public class CoordinateSystemMouseListener extends MouseAdapter {

    private CoordinateSystemComponent coordinateSystemComponent;
    private JLabel cursorPosition;
    private Neuron controller;

    private Point current;
    private int lastXOffset = 0;
    private int lastYOffset = 0;

    public static final double ZOOM = 0.2;
    public static final double MIN_ZOOM = 3.0;
    public static final double MAX_ZOOM = 4900000.0;

    public CoordinateSystemMouseListener(CoordinateSystemComponent coordinateSystemComponent, JLabel cursorPosition, Neuron controller) {
        this.coordinateSystemComponent = coordinateSystemComponent;
        this.cursorPosition = cursorPosition;
        this.controller = controller;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() - coordinateSystemComponent.OFFSET_X;
        int y = e.getY() - coordinateSystemComponent.OFFSET_Y;

        if(e.getButton() == MouseEvent.BUTTON3) {
            current = e.getPoint();
            lastXOffset = coordinateSystemComponent.OFFSET_X;
            lastYOffset = coordinateSystemComponent.OFFSET_Y;
        } else if(e.getButton() == MouseEvent.BUTTON2) {
            coordinateSystemComponent.getPoints().remove(coordinateSystemComponent.getPoint(x, y));
            current = null;
        }
        else{
            if(controller.isLearningMode()) controller.getPerceptron().addPoint(new ClassifiedPoint(coordinateSystemComponent.getPixelsInX(x), -coordinateSystemComponent.getPixelsInY(y), controller.getSelectedPointType()));
            else coordinateSystemComponent.addPoint(new Point2D.Double(coordinateSystemComponent.getPixelsInX(x), -coordinateSystemComponent.getPixelsInY(y)));
            current = null;
        }

        coordinateSystemComponent.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        coordinateSystemComponent.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        coordinateSystemComponent.setCursor(Cursor.getDefaultCursor());
    }

    public void mouseMoved(MouseEvent e) {
        BigDecimal x = new BigDecimal(coordinateSystemComponent.getPixelsInX(e.getX() - coordinateSystemComponent.OFFSET_X));
        BigDecimal y = new BigDecimal(coordinateSystemComponent.getPixelsInY(e.getY() - coordinateSystemComponent.OFFSET_Y));

        cursorPosition.setText("(" + x.setScale(5, BigDecimal.ROUND_HALF_UP) + ", " + y.setScale(5, BigDecimal.ROUND_HALF_UP) + ")");

        boolean isCursorAboveNeuron = (x.doubleValue()*coordinateSystemComponent.getNeuron().getWeightX() + y.doubleValue()*coordinateSystemComponent.getNeuron().getWeightY() + coordinateSystemComponent.getNeuron().getTeta()) == 0;

        if(isCursorAboveNeuron) cursorPosition.setForeground(Color.GREEN);
        else cursorPosition.setForeground(UIManager.getDefaults().getColor("JLabel.foreground"));

        cursorPosition.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int turns = e.getWheelRotation();

        if(turns > 0 && coordinateSystemComponent.GAP < MAX_ZOOM){
            coordinateSystemComponent.zoomIn(ZOOM);
        }
        else if(turns < 0 && coordinateSystemComponent.GAP > MIN_ZOOM){
            coordinateSystemComponent.zoomOut(ZOOM);
        }

        coordinateSystemComponent.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(current != null) {
            int xOffset = (int) (e.getX() - current.getX());
            int yOffset = (int) (e.getY() - current.getY());
            coordinateSystemComponent.OFFSET_X = lastXOffset + xOffset;
            coordinateSystemComponent.OFFSET_Y = lastYOffset + yOffset;
            coordinateSystemComponent.repaint();
        }
    }
}
