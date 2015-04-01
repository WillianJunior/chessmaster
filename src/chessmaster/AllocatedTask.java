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
class AllocatedTask {
    private final long cost;
    private final Task taskRef;

    public AllocatedTask(Long cost, Task task) {
        this.cost = cost;
        taskRef = task;
    }
    
    public Long getCost() {
        return cost;
    }
    
}
