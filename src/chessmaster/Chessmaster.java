/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package chessmaster;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author willian
 */
public class Chessmaster {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    }
    
    private ResourceList minmaxPlayer(ResourceList resourceList, Queue<Task> taskList, int depth) {
        for (Resource resource : resourceList) {
            minmaxNature(resourceList, taskList, depth-1);
        }
    }
    
    private ResourceList minmaxNature(Resource resource, ResourceList resourceList, Queue<Task> taskList, int depth) {
        
        Task task;
        ResourceList best = new ResourceList();
        
        // if we can still go deeper and we have a task to schedule
        if (depth != 0 && (task = taskList.poll()) != null) {
            // for each aproximation of a task's cost
            for (Long cost : task.costs) {     
                // attempt to use the new resource to allocate the new task
                if (resource != null) {
                    // create resourceList and taskList copies
                    Queue<Task> taskListCopy = new LinkedList<>(taskList);
                    ResourceList resourceListCopy = new ResourceList(resourceList);
                    
                    // Allocate the newTask to the new resource
                    AllocatedTask newTask = new AllocatedTask(cost, task);
                    resource.allocateTask(newTask);
                    
                    // add the new resource to the resourceListCopy
                    resourceListCopy.resources.add(resource);
                    
                    // recursive call
                    best = minmaxPlayer(resourceListCopy, taskListCopy, depth-1);
                }
                // if there isn't a new resource, attempt to allocate to an old one
                else {
                    // allocate the task to every possible resource available
                    for (Resource res : resourceList.resources) {
                        // create resourceList and taskList copies
                        Queue<Task> taskListCopy = new LinkedList<>(taskList);
                        ResourceList resourceListCopy = new ResourceList(resourceList);
                        
                        // Allocate the newTask to resource
                        AllocatedTask newTask = new AllocatedTask(cost, task);
                        getResource(res, resourceListCopy.resources).allocateTask(newTask);
                        
                        // recursive call with one possible resource
                        ResourceList result = minmaxPlayer(resourceListCopy, taskListCopy, depth-1);
                        
                        // maxmise nature's play
                        if (result.getWastage() > best.getWastage())
                            best = result;
                    }
                }
            }
        } else {
            // if the max depth was reached or there aren't any more tasks
            best = resourceList;
        }
        
        return best;
    }
    
    private Resource getResource(Resource r, List<Resource> rs) {
        return rs.get(rs.indexOf(r));
    }
    
}
