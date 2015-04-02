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
    public final Long value;
    private Long waste;
    private final List<AllocatedTask> allocatedTasks;

    public Resource(int id, long value) {
        this.id = id;
        this.value = value;
        waste = value;
        allocatedTasks = new ArrayList<>();
    }
    
    // Copy constructor
    public Resource(Resource resource) {
        this.id = resource.id;
        this.value = resource.value;
        long sum = 0;
        for (AllocatedTask task : resource.allocatedTasks) {
            sum += task.cost;
        }
        waste = resource.value - sum;
        allocatedTasks = new ArrayList<>(resource.getTasks());
    }
    
    public Long getWastage() {
        return waste;
    }
    
    public void allocateTask(AllocatedTask task) {
        allocatedTasks.add(task);
        waste -= task.cost;
    }
    
    public List<AllocatedTask> getTasks() {
        return allocatedTasks;
    }

    @Override
    public boolean equals(Object obj) {
        Resource r = (Resource) obj;
        return id == r.id;
    }
    
}
