/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmaster;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author willian
 */
public class Test {
    
    public static void main(String[] args) {
        
        TreeMap<Float, Float> list1 = new TreeMap<>();
        TreeMap<Float, Float> pareto1;
        Float alpha = new Float(0.8);
        
        list1.put(new Float(3), new Float(20));
        list1.put(new Float(4), new Float(12));
        list1.put(new Float(6), new Float(7));
        list1.put(new Float(7), new Float(15));
        list1.put(new Float(8), new Float(6));
        list1.put(new Float(12), new Float(11));
        list1.put(new Float(18), new Float(5));
        
        pareto1 = Pareto.getParetoCurve(list1);
        
        printMap(list1);
        System.out.println("");
        printMap(pareto1);
        Map.Entry<Float, Float> opt = Pareto.getParetoOptimal(pareto1, alpha);
        System.out.println("optimal: (" + opt.getKey() + ", " + opt.getValue() + ")");
        
    }
    
    public static void printMap (TreeMap<Float, Float> pareto) {
        for (Map.Entry<Float, Float> point : pareto.entrySet()) {
            System.out.println("(" + point.getKey() + ", " + point.getValue() + ")");
        }
    }
            
}
