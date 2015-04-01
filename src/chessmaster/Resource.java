/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmaster;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author willian
 */
public class Resource {
    
    public Long value;
    private Long waste;
    private final List<AllocatedTask> allocatedTasks;

    public Resource(Long value) {
        this.value = value;
        waste = value;
        allocatedTasks = new ArrayList<>();
    }
    
    // Copy constructor
    public Resource(Resource resource) {
        this.value = resource.value;
        waste = resource.value;
        allocatedTasks = new ArrayList<>(resource.getTasks());
    }
    
    
    public Long getWastage() {
        return waste;
    }
    
    public void allocateTask(AllocatedTask task) {
        allocatedTasks.add(task);
        waste -= task.getCost();
    }
    
    public List<AllocatedTask> getTasks() {
        return allocatedTasks;
    }
    
}
