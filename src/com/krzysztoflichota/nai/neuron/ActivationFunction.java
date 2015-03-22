package com.krzysztoflichota.nai.neuron;

/**
 * Created by Krzysztof Lichota on 2015-03-21.
 * krzysztoflichota.com
 */
public class ActivationFunction {

    private boolean includeLine;

    public ActivationFunction(boolean includeLine) {
        this.includeLine = includeLine;
    }

    public int getOutput(double input){
        if(includeLine){
            if(input >=0) return 1;
            else return 0;
        }
        else{
            if(input > 0) return 1;
            else return 0;
        }
    }

    public boolean isIncludeLine() {
        return includeLine;
    }

    public void setIncludeLine(boolean includeLine) {
        this.includeLine = includeLine;
    }
}
