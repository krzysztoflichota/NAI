package com.krzysztoflichota.nai;

import com.krzysztoflichota.nai.graphics.CoordinateSystemComponent;
import com.krzysztoflichota.nai.neuron.ActivationFunction;
import com.krzysztoflichota.nai.neuron.NeuronModel;
import com.krzysztoflichota.nai.utilities.CoordinateSystemMouseListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

/**
 * Created by Krzysztof Lichota on 2015-03-21.
 * krzysztoflichota.com
 */
public class Neuron extends JFrame{
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame f = new Neuron();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setResizable(false);
                f.setTitle("Model Neuronu");
                f.setVisible(true);
            }
        });
    }

    private CoordinateSystemComponent coordinateSystemComponent;
    private JTextField weightX, weightY, teta, pointX, pointY;
    private JButton refresh, clear, addPoint;
    private JRadioButton functionUp, functionDown;
    private JLabel cursorPosition;

    public static final double initWeightX = 1.00;
    public static final double initWeightY = 1.00;
    public static final double initTeta = 0.00;

    public Neuron() throws HeadlessException {
        initFields();
        initGUI();
        pack();
    }

    private void initFields(){
        cursorPosition = new JLabel("(0, 0)");

        coordinateSystemComponent = new CoordinateSystemComponent(new NeuronModel(initWeightX, initWeightY, initTeta, new ActivationFunction(true)));
        coordinateSystemComponent.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        CoordinateSystemMouseListener listener = new CoordinateSystemMouseListener(coordinateSystemComponent, cursorPosition);
        coordinateSystemComponent.addMouseListener(listener);
        coordinateSystemComponent.addMouseMotionListener(listener);

        weightX = new JTextField(10);
        weightX.setText(initWeightX + "");
        weightY = new JTextField(10);
        weightY.setText(initWeightY + "");
        teta = new JTextField(10);
        teta.setText(initTeta + "");
        pointX = new JTextField(10);
        pointY = new JTextField(10);

        refresh = new JButton("Aktualizuj Neuron");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshNeuron();
            }
        });
        clear = new JButton("Usuń Punkty");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPoints();
            }
        });
        addPoint = new JButton("Dodaj");
        addPoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPoint();
            }
        });

        functionUp = new JRadioButton("Na górze", true);
        functionDown = new JRadioButton("Na dole");
        ButtonGroup functionType = new ButtonGroup();
        functionType.add(functionUp);
        functionType.add(functionDown);
    }

    private void initGUI(){
        JPanel coordinateComponent = new JPanel();
        coordinateComponent.add(coordinateSystemComponent);
        add(coordinateComponent, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        controls.setLayout(layout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        JPanel neuronProperties = new JPanel();
        neuronProperties.setLayout(new GridLayout(6, 2, 5, 5));
        neuronProperties.add(new JLabel("Waga X:"));
        neuronProperties.add(weightX);
        neuronProperties.add(new JLabel("Waga Y:"));
        neuronProperties.add(weightY);
        neuronProperties.add(new JLabel("Teta:"));
        neuronProperties.add(teta);
        neuronProperties.add(new JLabel("Kropka na funkcji akt.:"));
        neuronProperties.add(functionUp);
        neuronProperties.add(new JLabel());
        neuronProperties.add(functionDown);
        neuronProperties.add(new JLabel());
        neuronProperties.add(refresh);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        border = BorderFactory.createTitledBorder(border, "Właściwości neuronu");
        neuronProperties.setBorder(border);

        controls.add(neuronProperties, gridBagConstraints);
        gridBagConstraints.gridy = 1;

        JPanel pointsControls = new JPanel();
        pointsControls.setLayout(new GridLayout(4, 2, 5, 5));
        pointsControls.add(new JLabel("X:"));
        pointsControls.add(pointX);
        pointsControls.add(new JLabel("Y:"));
        pointsControls.add(pointY);
        pointsControls.add(new JLabel());
        pointsControls.add(addPoint);
        pointsControls.add(cursorPosition);
        pointsControls.add(clear);
        border = BorderFactory.createLineBorder(Color.BLACK);
        border = BorderFactory.createTitledBorder(border, "Dodaj punkt");
        pointsControls.setBorder(border);

        controls.add(pointsControls, gridBagConstraints);

        add(controls, BorderLayout.EAST);
    }

    private void refreshNeuron(){
        try {
            coordinateSystemComponent.getNeuron().setWeightX(Double.parseDouble(weightX.getText()));
            coordinateSystemComponent.getNeuron().setWeightY(Double.parseDouble(weightY.getText()));
            coordinateSystemComponent.getNeuron().setTeta(Double.parseDouble(teta.getText()));
            coordinateSystemComponent.getNeuron().getActivationFunction().setIncludeLine(functionUp.isSelected());
            coordinateSystemComponent.repaint();
        }catch (NumberFormatException e){
                showError("Wprowadzone dane są złe!");
        }
    }

    private void showError(String error){
        JOptionPane.showMessageDialog(this, error, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    private void clearPoints(){
        coordinateSystemComponent.clearPoints();
        coordinateSystemComponent.repaint();
    }

    private void addPoint(){
        try {
            Point2D point = new Point2D.Double(Double.parseDouble(pointX.getText()), -Double.parseDouble(pointY.getText()));
            coordinateSystemComponent.addPoint(point);
            coordinateSystemComponent.repaint();
        }catch (NumberFormatException e){
            showError("Wprowadzone dane są złe!");
        }
    }
}
