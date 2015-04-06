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
    
    public final int id;
    public final Float value;
    private Float waste;
    private final List<AllocatedTask> allocatedTasks;

    public Resource(int id, Float value) {
        this.id = id;
        this.value = value;
        waste = null;
        allocatedTasks = new ArrayList<>();
    }
    
    // Copy constructor
    public Resource(Resource resource) {
        this.id = resource.id;
        this.value = resource.value;
        Float sum = (float) 0;
        for (AllocatedTask task : resource.allocatedTasks) {
            sum += task.cost;
        }
        if (sum == 0)
            waste = null;
        else
            waste = resource.value - sum;
        allocatedTasks = new ArrayList<>(resource.getTasks());
    }
    
    public Float getWastage() {
        if (waste == null)
            return (float) 0;
        else
            return waste;
    }
    
    public void allocateTask(AllocatedTask task) {
        if (waste == null)
            waste = value;
        
        allocatedTasks.add(task);
        waste -= task.cost;
    }
    
    public List<AllocatedTask> getTasks() {
        return allocatedTasks;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Resource) {
            Resource r = (Resource) obj;
            return id == r.id;
        } else
            return false;
        
    }
    
}
