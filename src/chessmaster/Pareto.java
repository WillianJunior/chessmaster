/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package chessmaster;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.atan;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author willian
 */
public class Pareto {
    
    // get the pareto curve of a list of points
    public static List<ResourceList> getParetoCurve(List<ResourceList> resources) {
        
        List<ResourceList> paretoCurve = new ArrayList<>();
        
        // set null element as the current limit
        Float limitCost = (float) 0;
        Float limitTime = Float.MAX_VALUE;
        Float newAngle;
        Float oldAngle = (float) 0;
        
        // for all remaining pairs
        for (ResourceList resource : resources) {
            Float cost = resource.getFullCost();
            Float execTime = resource.getMaxTime();
            newAngle = new Float(atan((execTime-limitCost)/(limitTime-cost)));
//            System.out.println("(c=" + cost + ", t=" + execTime + ")");
//            System.out.println("angle = " + newAngle);
//            System.out.println("");
            
            // if second argument is less than or equal to the limit and
            // the angle is greater than the last angle
            if (execTime <= limitCost && oldAngle <= newAngle) {
                // add to the curve and set it as the new limit
                paretoCurve.add(resource);
                limitCost = cost;
                limitTime = execTime;
            }
        }
        
        return paretoCurve;
    }
    
    // get the point from the ordered list closest to the intersection between
    // the pareto curve and the vector given by the angle alpha
//    public static Entry<Float, Float> getParetoOptimal(TreeMap<Float, Float> paretoCurve, Float alpha) {
    public static ResourceList getParetoOptimal(List<ResourceList> paretoCurve, Float alpha) {        
        
        // set closest point as inf
        ResourceList closestPoint = null;
        Float minDistance = Float.MAX_VALUE;
        
        // for each pareto curve point
        for (ResourceList resource : paretoCurve) {
            // update closest point
            Float cost = resource.getFullCost();
            Float execTime = resource.getMaxTime();
            Float distance = getDistanceFromCurve(alpha, cost, execTime);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = resource;
            }
        }
        
        return closestPoint;
    }
    
    public static Float getDistanceFromCurve(Float alpha, Float x0, Float y0) {
        
        // create the vector of alpha
        Float x1, y1, x2, y2;
        x1 = new Float(0);
        y1 = new Float(0);
        x2 = new Float(cos(alpha));
        y2 = new Float(sin(alpha));
        
        // calculate distance
        Float distance = new Float(abs((y2-y1)*x0-(x2-x1)*y0 + x2*y1 - y2*x1)/
                sqrt(pow(y2-y1, 2) + pow(x2-x1, 2)));
        
        return distance;
    }
    
}
