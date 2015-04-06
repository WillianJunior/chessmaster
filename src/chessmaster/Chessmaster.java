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
 * reason 2745649 to hate java:
 * no type classes -> no existing functionality to use a Number class
 * that actually has the 4 basic operations and the assignment operation
 * in order to abstract the number precision for a function.
 */
public class Chessmaster {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ResourceList resources = new ResourceList();
        
        resources.resources.add(new Resource(1, (float) 500));
        resources.resources.add(new Resource(2, (float) 600));
        
        Queue<Task> tasks = new LinkedList<>();
        
        List<Float> costs = new ArrayList<>();
        costs.add((float) 50);
        costs.add((float) 200);
        costs.add((float) 350);
        
        List<Float> costs2 = new ArrayList<>();
        costs2.add((float) 150);
        costs2.add((float) 200);
        
        tasks.add(new Task(1, costs));
        tasks.add(new Task(2, costs));
        tasks.add(new Task(3, costs2));
        
        ResourceList out = Chessmaster.minmaxPlayer(resources, tasks, 10);
        
        System.out.println("best waste: " + out.getWastage());
        for (Resource resource : out.resources) {
            System.out.println("R" + resource.id + " - w: " + resource.getWastage());
            for (AllocatedTask task : resource.getTasks()) {
                System.out.println("| T" + task.taskRef.id + " - c: " + task.cost);
            }
            System.out.println("");
        }
        
    }
    
    private static ResourceList minmaxPlayer(ResourceList resourceList, Queue<Task> taskList, int depth) {
        
        // create a local best to minimise with maximum wastage
        ResourceList best = new ResourceList();
        Resource r = new Resource(0, Float.MAX_VALUE);
        r.allocateTask(new AllocatedTask((float) 1, null));
        best.resources.add(r);
        
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
        best.resources.add(new Resource(0, Float.MIN_VALUE));
        
        // if we can still go deeper and we have a task to schedule
        if (depth != 0 && (task = taskList.poll()) != null) {
            // for each aproximation of a task's cost
            for (Float cost : task.costs) {
                // attempt to use the new resource to allocate the new task
                // create resourceList and taskList copies
                Queue<Task> taskListCopy = new LinkedList<>(taskList);
                ResourceList resourceListCopy = new ResourceList(resourceList);
                
                // Allocate the newTask to the new resource
                AllocatedTask newTask = new AllocatedTask(cost, task);
                getResource(resource, resourceListCopy.resources).allocateTask(newTask);
                
                // recursive call
                ResourceList result = minmaxPlayer(resourceListCopy, taskListCopy, depth-1);
                
                // penalise negative wastage (underprovisioning)
                for (Resource r : result.resources) {
                    if (r.getWastage() < 0) {
                        ResourceList inf = new ResourceList();
                        Resource rr = new Resource(0, Float.MAX_VALUE);
                        rr.allocateTask(new AllocatedTask((float) 1, null));
                        inf.resources.add(rr);
                        result = inf;
                    }
                }
                // maxmise nature's play
                if (result.getWastage() > best.getWastage())
                    best = result;
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
