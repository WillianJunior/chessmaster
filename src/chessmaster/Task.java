/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmaster;

import java.util.List;

/**
 *
 * @author willian
 */
public class Task {
    
    public final List<Float> costs;
    public final int id;

    public Task(int id, List<Float> costs) {
        this.id = id;
        this.costs = costs;
    }
    
    
    
}
