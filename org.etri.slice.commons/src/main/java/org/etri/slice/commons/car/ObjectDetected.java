package org.etri.slice.commons.car;

import java.io.Serializable;

import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
public class ObjectDetected implements Serializable {

    private static final long serialVersionUID = 1L;
    private Double distance;

    public ObjectDetected() {
    }
    
    public ObjectDetected(Double distance) {
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}

