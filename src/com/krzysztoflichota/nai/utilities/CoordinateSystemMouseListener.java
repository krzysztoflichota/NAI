package com.krzysztoflichota.nai.utilities;

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

    public static final double ZOOM = 0.2;
    public static final double MIN_ZOOM = 3.0;
    public static final double MAX_ZOOM = 4900000.0;

    public CoordinateSystemMouseListener(CoordinateSystemComponent coordinateSystemComponent, JLabel cursorPosition) {
        this.coordinateSystemComponent = coordinateSystemComponent;
        this.cursorPosition = cursorPosition;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        coordinateSystemComponent.addPoint(new Point2D.Double(coordinateSystemComponent.getPixelsInX(e.getX()), -coordinateSystemComponent.getPixelsInY(e.getY())));
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
        BigDecimal x = new BigDecimal(coordinateSystemComponent.getPixelsInX(e.getX()));
        BigDecimal y = new BigDecimal(coordinateSystemComponent.getPixelsInY(e.getY()));

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
            coordinateSystemComponent.GAP *= 1 + ZOOM;
        }
        else if(turns < 0 && coordinateSystemComponent.GAP > MIN_ZOOM){
            coordinateSystemComponent.GAP *= 1 - ZOOM;
        }

        coordinateSystemComponent.repaint();
    }
}
