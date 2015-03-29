package com.krzysztoflichota.nai.neuron;

import java.awt.geom.Point2D;

/**
 * Created by Krzysztof Lichota on 2015-03-21.
 * krzysztoflichota.com
 */
public class NeuronModel {

    private double weightX;
    private double weightY;
    private double teta;

    private ActivationFunction activationFunction;

    public NeuronModel(){
        weightX = 1.00;
        weightY = 1.00;
        teta = 0.00;
        activationFunction = new ActivationFunction(FunctionType.STEP_DOT_UP);
    }

    public NeuronModel(double weightX, double weightY, double teta, ActivationFunction activationFunction) {
        this.weightX = weightX;
        this.weightY = weightY;
        this.teta = teta;
        this.activationFunction = activationFunction;
    }

    public int getOutput(Point2D point){
        double NET;
        if(weightY != 0) NET = point.getX()*weightX - point.getY()*weightY + teta;
        else NET = point.getX()*weightX - point.getY()*weightY - teta;

        return activationFunction.getOutput(NET);
    }

    public double getWeightX() {
        return weightX;
    }

    public void setWeightX(double weightX) {
        this.weightX = weightX;
    }

    public double getWeightY() {
        return weightY;
    }

    public void setWeightY(double weightY) {
        this.weightY = weightY;
    }

    public double getTeta() {
        return teta;
    }

    public void setTeta(double teta) {
        this.teta = teta;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }
}
