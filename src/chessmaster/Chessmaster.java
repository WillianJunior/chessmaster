/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package chessmaster;

import java.util.ArrayList;
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
        
        ResourceList resources = new ResourceList();
        
        resources.resources.add(new Resource(1, 500));
        resources.resources.add(new Resource(2, 600));
        
        Queue<Task> tasks = new LinkedList<>();
        
        List<Long> costs = new ArrayList<>();
        costs.add(new Long(50));
        costs.add(new Long(200));
        costs.add(new Long(350));
        
        List<Long> costs2 = new ArrayList<>();
        costs2.add(new Long(150));
        costs2.add(new Long(200));
        
        tasks.add(new Task(1, costs));
        tasks.add(new Task(2, costs));
        tasks.add(new Task(3, costs2));
        
        ResourceList out = Chessmaster.minmaxPlayer(resources, tasks, 6);
        
        System.out.println("best waste: " + out.getWastage());
        for (Resource resource : out.resources) {
            System.out.println("R" + resource.id);
            for (AllocatedTask task : resource.getTasks()) {
                System.out.println("| T" + task.taskRef.id);
            }
            System.out.println("");
        }
        
    }
    
    private static ResourceList minmaxPlayer(ResourceList resourceList, Queue<Task> taskList, int depth) {
        
        // create a local best to minimise with maximum wastage
        ResourceList best = new ResourceList();
        best.resources.add(new Resource(0, Long.MAX_VALUE));
        
        // if we can still go deeper
        if (depth != 0) {
            for (Resource resource : resourceList.resources) {
                // create resourceList and taskList copies
                Queue<Task> taskListCopy = new LinkedList<>(taskList);
                ResourceList resourceListCopy = new ResourceList(resourceList);
                
                // run recursive call with current resource
                ResourceList result = minmaxNature(resource, resourceListCopy, taskListCopy, depth-1);
                
                // minimise player's play
                if (result.getWastage() < best.getWastage())
                    best = result;
            }
            return best;
        }
        // if the maximum depth was reached
        else {
            return resourceList;
        }
    }
    
    private static ResourceList minmaxNature(Resource resource, ResourceList resourceList, Queue<Task> taskList, int depth) {
        
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
                    getResource(resource, resourceListCopy.resources).allocateTask(newTask);
                    
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
            
            // penalise negative wastage (underprovisioning)
            for (Resource r : best.resources) {
                if (r.getWastage() < 0) {
                    ResourceList inf = new ResourceList();
                    inf.resources.add(new Resource(0, Long.MAX_VALUE));
                    return inf;
                }
            }
            return best;
        } else {
            // if the max depth was reached or there aren't any more tasks
            return resourceList;
        }
    }
    
    private static Resource getResource(Resource r, List<Resource> rs) {
        return rs.get(rs.indexOf(r));
    }
    
}
