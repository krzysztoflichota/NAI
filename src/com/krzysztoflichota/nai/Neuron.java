package com.krzysztoflichota.nai;

import com.krzysztoflichota.nai.graphics.CoordinateSystemComponent;
import com.krzysztoflichota.nai.neuron.ActivationFunction;
import com.krzysztoflichota.nai.neuron.FunctionType;
import com.krzysztoflichota.nai.neuron.NeuronModel;
import com.krzysztoflichota.nai.neuron.PerceptronModel;
import com.krzysztoflichota.nai.utilities.ClassifiedPoint;
import com.krzysztoflichota.nai.utilities.CoordinateSystemMouseListener;
import com.krzysztoflichota.nai.utilities.PointType;

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
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                JFrame f = new Neuron();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setTitle("Model Neuronu");
                f.setVisible(true);
            }
        });
    }

    private CoordinateSystemComponent coordinateSystemComponent;
    private JTextField weightX, weightY, teta, pointX, pointY, numberOfSteps, learningFactor;
    private JButton refresh, clear, addPoint, nextLearningStep, startLearning, setRandomWeights;
    private JRadioButton functionUp, functionDown, rectanglesPoints, circlePoints;
    private JLabel cursorPosition;
    private JToggleButton learningMode;

    private JPanel controls, neuronProperties, neuronLearningPanel;

    private PerceptronModel perceptron;

    public static final double initWeightX = 1.00;
    public static final double initWeightY = 1.00;
    public static final double initTeta = 0.00;

    public Neuron() throws HeadlessException {
        initFields();
        initGUI();
        pack();
        setSizes();
    }

    private void setSizes() {
        int controlsMinHeight = controls.getPreferredSize().height + 35;
        int controlsMinWidth = controls.getPreferredSize().width + 15;
        setMinimumSize(new Dimension(controlsMinWidth + controlsMinHeight, controlsMinHeight));
    }

    public PerceptronModel getPerceptron() {
        return perceptron;
    }

    private void initFields(){
        cursorPosition = new JLabel("(0, 0)");

        coordinateSystemComponent = new CoordinateSystemComponent(new NeuronModel(initWeightX, initWeightY, initTeta, new ActivationFunction(FunctionType.STEP_DOT_UP)));
        coordinateSystemComponent.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        CoordinateSystemMouseListener listener = new CoordinateSystemMouseListener(coordinateSystemComponent, cursorPosition, this);
        coordinateSystemComponent.addMouseListener(listener);
        coordinateSystemComponent.addMouseMotionListener(listener);
        coordinateSystemComponent.addMouseWheelListener(listener);

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

        controls = new JPanel();

        learningMode = new JToggleButton("Tryb nauki");
        learningMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLearningMode(learningMode.isSelected());
            }
        });
        nextLearningStep = new JButton("Nast. krok");
        nextLearningStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextLearningStep();
            }
        });
        startLearning = new JButton("Start");
        startLearning.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int numberOfRepeats = Integer.parseInt(numberOfSteps.getText());
                    if(numberOfRepeats < 0) throw new IllegalStateException();
                    repeatLearningStep(numberOfRepeats);
                }
                catch(NumberFormatException | IllegalComponentStateException exc){
                    showError("Zła liczba powtórzeń!");
                }
            }
        });
        setRandomWeights = new JButton("Losowe wagi");
        setRandomWeights.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPerceptronRandomWeights();
            }
        });
        numberOfSteps = new JTextField(10);
        numberOfSteps.setText("1000");
        learningFactor = new JTextField(10);
        learningFactor.setText("0.1");
        rectanglesPoints = new JRadioButton("Kwadraty", true);
        circlePoints = new JRadioButton("Kółka");
        ButtonGroup pointType = new ButtonGroup();
        pointType.add(rectanglesPoints);
        pointType.add(circlePoints);
    }

    private void initGUI(){
        add(coordinateSystemComponent, BorderLayout.CENTER);

        GridBagLayout layout = new GridBagLayout();
        controls.setLayout(layout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        createNeuronPropertiesPanel();
        controls.add(neuronProperties, gridBagConstraints);
        gridBagConstraints.gridy = 1;

        JPanel pointsControls = createPointsControlPanel();
        controls.add(pointsControls, gridBagConstraints);
        gridBagConstraints.gridy = 2;

        createLearningControlPanel();
        controls.add(neuronLearningPanel, gridBagConstraints);

        add(controls, BorderLayout.EAST);

        setLearningMode(false);
    }

    private JPanel createNeuronPropertiesPanel(){
        neuronProperties = new JPanel();
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

        return neuronProperties;
    }

    private JPanel createPointsControlPanel(){
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
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        border = BorderFactory.createTitledBorder(border, "Dodaj punkt");
        pointsControls.setBorder(border);

        return pointsControls;
    }

    private JPanel createLearningControlPanel(){
        neuronLearningPanel = new JPanel();
        neuronLearningPanel.setLayout(new GridLayout(8, 2, 5, 5));
        neuronLearningPanel.add(new JLabel("Włącz:"));
        neuronLearningPanel.add(learningMode);
        neuronLearningPanel.add(new JLabel("Max. liczba kroków:"));
        neuronLearningPanel.add(numberOfSteps);
        neuronLearningPanel.add(new JLabel("Wsp. nauki:"));
        neuronLearningPanel.add(learningFactor);
        neuronLearningPanel.add(new JLabel("Typ punktu:"));
        neuronLearningPanel.add(rectanglesPoints);
        neuronLearningPanel.add(new JLabel());
        neuronLearningPanel.add(circlePoints);
        neuronLearningPanel.add(new JLabel());
        neuronLearningPanel.add(setRandomWeights);
        neuronLearningPanel.add(new JLabel());
        neuronLearningPanel.add(nextLearningStep);
        neuronLearningPanel.add(new JLabel());
        neuronLearningPanel.add(startLearning);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        border = BorderFactory.createTitledBorder(border, "Tryb nauki");
        neuronLearningPanel.setBorder(border);

        return neuronLearningPanel;
    }

    private void refreshNeuron(){
        try {
            coordinateSystemComponent.getNeuron().setWeightX(Double.parseDouble(weightX.getText()));
            coordinateSystemComponent.getNeuron().setWeightY(Double.parseDouble(weightY.getText()));
            coordinateSystemComponent.getNeuron().setTeta(Double.parseDouble(teta.getText()));
            if(functionUp.isSelected()) coordinateSystemComponent.getNeuron().getActivationFunction().setIncludeLine(FunctionType.STEP_DOT_UP);
            else if(functionDown.isSelected()) coordinateSystemComponent.getNeuron().getActivationFunction().setIncludeLine(FunctionType.STEP_DOT_DOWN);
            coordinateSystemComponent.repaint();
        }catch (NumberFormatException e){
                showError("Wprowadzone dane są złe!");
        }
    }

    private void showError(String error){
        JOptionPane.showMessageDialog(this, error, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    private void clearPoints(){
        if(!isLearningMode())coordinateSystemComponent.clearPoints();
        else perceptron.clearLearningSet();
        coordinateSystemComponent.repaint();
    }

    private void addPoint(){
        try {
            double x = Double.parseDouble(pointX.getText());
            double y = -Double.parseDouble(pointY.getText());
            if(!isLearningMode())coordinateSystemComponent.addPoint(new Point2D.Double(x, y));
            else perceptron.addPoint(new ClassifiedPoint(x, y, getSelectedPointType()));
        }catch (NumberFormatException e){
            showError("Wprowadzone dane są złe!");
        }

        coordinateSystemComponent.repaint();
    }

    private void setLearningMode(boolean mode){
        if(mode) {
            NeuronModel neuron = coordinateSystemComponent.getNeuron();
            perceptron = new PerceptronModel(neuron.getWeightX(), neuron.getWeightY(), neuron.getTeta(), neuron.getActivationFunction());
            perceptron.addPoints(coordinateSystemComponent.getPoints());
            coordinateSystemComponent.clearPoints();
            coordinateSystemComponent.setNeuron(perceptron);
            coordinateSystemComponent.setLearningMode(true);
            actualizeNeuronProperties(perceptron);
        }
        else {
            if(perceptron != null) coordinateSystemComponent.setLearningMode(false);
        }

        for(Component comp : neuronLearningPanel.getComponents()){
            if(!comp.equals(learningMode))comp.setEnabled(mode);
        }
        for(Component comp : neuronProperties.getComponents()){
            comp.setEnabled(!mode);
        }

        coordinateSystemComponent.repaint();
    }

    public boolean isLearningMode(){
        return coordinateSystemComponent.isLearningMode();
    }

    private boolean nextLearningStep(){
        if(!isLearningMode()) return false;
        else if(perceptron.isLearningSetProperlyClassified()){
            showError("Wszystkie punkty są zakwalifikowane poprawnie");
            return false;
        }

        try {
            perceptron.setLearnFactor(Double.parseDouble(learningFactor.getText()));
        }
        catch(NumberFormatException | IllegalStateException e){
            showError("Zły współczynnik nauki!");
        }

        perceptron.nextLearningStep();
        actualizeNeuronProperties(perceptron);
        coordinateSystemComponent.repaint();

        return true;
    }

    private void actualizeNeuronProperties(NeuronModel neuron){
        weightX.setText(neuron.getWeightX() + "");
        weightY.setText(neuron.getWeightY() + "");
        teta.setText(neuron.getTeta() + "");
    }

    public PointType getSelectedPointType(){
        if(rectanglesPoints.isSelected()) return PointType.RECTANGLE;
        return PointType.CIRCLE;
    }

    private void repeatLearningStep(int count){
        for(int i = 0; i < count; i++){
            if(!nextLearningStep()) return;
        }

        showError("Nie udało się nauczyć podanego zbioru punktów w podanej liczbie kroków.");
    }

    private void setPerceptronRandomWeights(){
        if(!isLearningMode()) return;
        perceptron.setRandomWeights();
        actualizeNeuronProperties(perceptron);
        coordinateSystemComponent.repaint();
    }
}
