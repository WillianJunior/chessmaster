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
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author willian
 */
public class Pareto {
    
    // get the pareto curve of a list of points
    public static TreeMap<Float, Float> getParetoCurve(TreeMap<Float, Float> points) {
        
        TreeMap<Float, Float> paretoCurve = new TreeMap<>();
        
        // set null element as the current limit
        Entry<Float, Float> limit = new AbstractMap.SimpleEntry<>(new Float(0),Float.MAX_VALUE);
        Float newAngle;
        Float oldAngle = new Float(0);
        
        // for all remaining pairs
        for (Entry<Float, Float> point : points.entrySet()) {
            newAngle = new Float(atan((point.getValue()-limit.getValue())/(limit.getKey()-point.getKey())));
            System.out.println("(" + point.getKey() + ", " + point.getValue() + ")");
            System.out.println("angle = " + newAngle);
            System.out.println("");
            
            // if second argument is less than or equal to the limit and
            // the angle is greater than the last angle
            if (point.getValue() <= limit.getValue() && oldAngle <= newAngle) {
                // add to the curve and set it as the new limit
                paretoCurve.put(point.getKey(), point.getValue());
                limit = point;
            }
        }
        
        return paretoCurve;
    }
    
    // get the point from the ordered list closest to the intersection between
    // the pareto curve and the vector given by the angle alpha
    public static Entry<Float, Float> getParetoOptimal(TreeMap<Float, Float> paretoCurve, Float alpha) {
        
        // create the vector of alpha
        Float x1, y1, x2, y2;
        x1 = new Float(0);
        y1 = new Float(0);
        x2 = new Float(cos(alpha));
        y2 = new Float(sin(alpha));
        
        // set closest point as inf
        Entry<Float, Float> closestPoint = null;
        Float minDistance = Float.MAX_VALUE;
        
        // for each pareto curve point
        for (Entry<Float, Float> point : paretoCurve.entrySet()) {
            // update closest point
            Float distance = new Float(abs((y2-y1)*point.getKey()-(x2-x1)*point.getValue() + x2*y1 - y2*x1)/
                    sqrt(pow(y2-y1, 2) + pow(x2-x1, 2)));
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = new AbstractMap.SimpleEntry<>(point.getKey(), point.getValue());
            }
        }
        
        return closestPoint;
    }
    
    // attempt to add a new point to the given pareto curve
    // returns null if the point isn't pareto optimal
    public static TreeMap<Float, Float> updateParetoCurve(TreeMap<Float, Float> paretoCurve, Entry<Float, Float> newPoint) {
        
        TreeMap<Float, Float> newParetoCurve = null;
        
        if (paretoCurve.lastEntry().getValue() >= newPoint.getValue()) {
            newParetoCurve = (TreeMap<Float, Float>) paretoCurve.clone();
            newParetoCurve.put(newPoint.getKey(), newPoint.getValue());
        }
        
        return newParetoCurve;
    }
    
    public static Float getDistanceFromCurve(Float alpha, Entry<Float, Float> point) {
        
        // create the vector of alpha
        Float x1, y1, x2, y2;
        x1 = new Float(0);
        y1 = new Float(0);
        x2 = new Float(cos(alpha));
        y2 = new Float(sin(alpha));
        
        // calculate distance
        Float distance = new Float(abs((y2-y1)*point.getKey()-(x2-x1)*point.getValue() + x2*y1 - y2*x1)/
                sqrt(pow(y2-y1, 2) + pow(x2-x1, 2)));
        
        return distance;
    }
    
}
