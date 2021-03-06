package com.krzysztoflichota.nai.neuron;

import com.krzysztoflichota.nai.Neuron;
import com.krzysztoflichota.nai.utilities.ClassifiedPoint;
import com.krzysztoflichota.nai.utilities.PointType;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krzysztof Lichota on 2015-03-29.
 * krzysztoflichota.com
 */
public class PerceptronModel extends NeuronModel{

    private double learnFactor;
    private List<ClassifiedPoint> learningSet;
    private Iterator<ClassifiedPoint> iterator;

    public PerceptronModel(Neuron neuron){
        super();
        learningSet = new LinkedList<>();
        learnFactor = 0.1;
    }

    public PerceptronModel(double weightX, double weightY, double teta, ActivationFunction activationFunction) {
        super(weightX, weightY, teta, activationFunction);
        learningSet = new LinkedList<>();
    }

    public void nextLearningStep(){
        for(ClassifiedPoint point : learningSet){
            switch(getOutput(point)){
                case 0:
                    if(point.getType() == PointType.RECTANGLE) modifyWeights(point);
                    break;
                case 1:
                    if(point.getType() == PointType.CIRCLE) modifyWeights(point);
                    break;
            }
        }
    }

    private void modifyWeights(ClassifiedPoint point){
        int expectedAnswer = (point.getType() == PointType.RECTANGLE ? 1 : 0);
        int actualAnswer = (expectedAnswer == 1 ? 0 : 1);

        double xWeight = learnFactor * (expectedAnswer - actualAnswer) * point.getX();
        double yWeight = learnFactor * (expectedAnswer - actualAnswer) * -point.getY();
        double tetaWeight = learnFactor * (expectedAnswer - actualAnswer);

        setWeightX(getWeightX() + xWeight);
        setWeightY(getWeightY() + yWeight);
        setTeta(getTeta() + tetaWeight);
    }

    public double getLearnFactor() {
        return learnFactor;
    }

    public void setLearnFactor(double learnFactor) {
        if(learnFactor <= 0) throw new IllegalStateException();
        this.learnFactor = learnFactor;
    }

    public void addPoint(ClassifiedPoint point){
        learningSet.add(point);
    }

    public boolean isLearningSetProperlyClassified(){
        for(ClassifiedPoint point : learningSet){
            switch(getOutput(point)){
                case 0:
                    if(point.getType() != PointType.CIRCLE) return false;
                    break;
                case 1:
                    if(point.getType() != PointType.RECTANGLE) return false;
                    break;
            }
        }

        return true;
    }

    public List<ClassifiedPoint> getLearningSet(){
        return learningSet;
    }

    public static PerceptronModel createPerceptronWithRandomWeigths(){
        double xWeight = Math.random()*10;
        double yWeight = Math.random()*10;
        double teta = Math.random()*25;

        return new PerceptronModel(xWeight, yWeight, teta, new ActivationFunction(FunctionType.STEP_DOT_UP));
    }

    public void clearLearningSet(){
        learningSet.clear();
    }

    public void removePoint(ClassifiedPoint point){
        learningSet.remove(point);
    }

    public void addPoints(List<? extends Point2D> points){
        for(Point2D point : points){
            if(getOutput(point) == 1){
                learningSet.add(new ClassifiedPoint(point.getX(), point.getY(), PointType.RECTANGLE));
            }
            else if(getOutput(point) == 0){
                learningSet.add(new ClassifiedPoint(point.getX(), point.getY(), PointType.CIRCLE));
            }
        }
    }

    public void setRandomWeights(){
        setWeightX(Math.random()*10);
        setWeightY(Math.random()*10);
        setTeta(Math.random()*25);
    }
}
