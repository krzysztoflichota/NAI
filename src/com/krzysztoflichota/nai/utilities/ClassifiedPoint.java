package com.krzysztoflichota.nai.utilities;

import java.awt.geom.Point2D;

/**
 * Created by Krzysztof Lichota on 2015-03-29.
 * krzysztoflichota.com
 */
public class ClassifiedPoint extends Point2D.Double {
    private PointType type;

    public ClassifiedPoint(double x, double y, PointType type) {
        super(x, y);
        this.type = type;
    }

    public PointType getType() {
        return type;
    }

    public void setType(PointType type) {
        this.type = type;
    }
}
