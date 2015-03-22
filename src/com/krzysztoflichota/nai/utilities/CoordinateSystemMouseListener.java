package com.krzysztoflichota.nai.utilities;

import com.krzysztoflichota.nai.graphics.CoordinateSystemComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * Created by Krzysztof Lichota on 2015-03-21.
 * krzysztoflichota.com
 */
public class CoordinateSystemMouseListener extends MouseAdapter {
    private CoordinateSystemComponent coordinateSystemComponent;
    private JLabel cursorPosition;

    public CoordinateSystemMouseListener(CoordinateSystemComponent coordinateSystemComponent, JLabel cursorPosition) {
        this.coordinateSystemComponent = coordinateSystemComponent;
        this.cursorPosition = cursorPosition;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        coordinateSystemComponent.addPoint(new Point2D.Double(CoordinateSystemComponent.getPixelsInX(e.getX()), -CoordinateSystemComponent.getPixelsInY(e.getY())));
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
        double x = CoordinateSystemComponent.getPixelsInX(e.getX());
        double y = CoordinateSystemComponent.getPixelsInY(e.getY());

        cursorPosition.setText("(" + x + ", " + y + ")");

        boolean isCursorAboveNeuron = (x*coordinateSystemComponent.getNeuron().getWeightX() + y*coordinateSystemComponent.getNeuron().getWeightY() + coordinateSystemComponent.getNeuron().getTeta()) == 0;

        if(isCursorAboveNeuron) cursorPosition.setForeground(Color.GREEN);
        else cursorPosition.setForeground(UIManager.getDefaults().getColor("JLabel.foreground"));

        cursorPosition.repaint();
    }
}
