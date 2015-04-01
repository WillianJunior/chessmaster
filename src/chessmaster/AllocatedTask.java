/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmaster;

/**
 *
 * @author willian
 */
public class AllocatedTask {
    public final long cost;
    public final Task taskRef;

    public AllocatedTask(Long cost, Task task) {
        this.cost = cost;
        taskRef = task;
    }
    
}
