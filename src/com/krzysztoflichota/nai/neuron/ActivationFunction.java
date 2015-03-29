package com.krzysztoflichota.nai.neuron;

/**
 * Created by Krzysztof Lichota on 2015-03-21.
 * krzysztoflichota.com
 */
public class ActivationFunction {

    private FunctionType type;

    public ActivationFunction(FunctionType type) {
        this.type = type;
    }

    public int getOutput(double input) {

        switch (type) {
            case STEP_DOT_UP:
                if (input >= 0) return 1;
                else return 0;
            case STEP_DOT_DOWN:
                if (input > 0) return 1;
                else return 0;
        }

        return 0;
    }


    public FunctionType isIncludeLine() {
        return type;
    }

    public void setIncludeLine(FunctionType type) {
        this.type = type;
    }
}
