/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package chessmaster;

import static java.lang.StrictMath.abs;
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
    
    // get the point from the ordered list closest to the intersection between
    // the pareto curve and the vector given by the angle alpha
    public static Entry<Float, Float> getParetoOptimal(TreeMap<Float, Float> points, Float alpha) {
        
        TreeMap<Float, Float> paretoCurve = new TreeMap<>();
        
        // set null element as the current limit
        Entry<Float, Float> limit = new AbstractMap.SimpleEntry<>(Float.MAX_VALUE,Float.MAX_VALUE);
        
        // for all remaining pairs
        for (Entry<Float, Float> point : points.entrySet()) {
            // if second argument is less than or equal to the limit
            if (point.getValue() <= limit.getValue()) {
                // add to the curve and set it as the new limit
                paretoCurve.put(point.getKey(), point.getValue());
                limit = point;
            }
        }
        
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
    
    
}
