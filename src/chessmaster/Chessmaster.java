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
        
        resources.resources.add(new Resource(1, (float) 2, (float) 0.007));
        resources.resources.add(new Resource(2, (float) 3.4, (float) 0.012));
        
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
        
        ResourceList out = Chessmaster.minmaxPlayer(resources, tasks, (float) 0.3, 10);
        
        System.out.println("best max time: " + out.getMaxTime());
        System.out.println("best avg time: " + out.getAvgTime());
        System.out.println("best cost: " + out.getFullCost());
        for (Resource resource : out.resources) {
            System.out.println("R" + resource.id + " - t: " + resource.getExecTime() + " - c: " + resource.getCost());
            for (AllocatedTask task : resource.getTasks()) {
                System.out.println("| T" + task.taskRef.id + " - c: " + task.cost);
            }
            System.out.println("");
        }
        
    }
    
    private static ResourceList minmaxPlayer(ResourceList resourceList, Queue<Task> taskList, float alpha, int depth) {
        
        if (!taskList.isEmpty()) {
            printTab(depth);
            System.out.println("[" + depth + "] Player - task: " + taskList.peek().id);
        }
        
        // create a local best to minimise with maximum wastage
        ResourceList best = new ResourceList();
//        Resource r = new Resource(0, Float.MAX_VALUE);
//        r.allocateTask(new AllocatedTask((float) 1, null));
//        best.resources.add(r);
        
        List<ResourceList> paretoOptResults = new ArrayList<>();
        
        // create a max cost point (i.e. (0, inf))
        ResourceList maxCost = new ResourceList();
        Resource r1 = new Resource(0, (float) 1, Float.MAX_VALUE);
        r1.allocateTask(new AllocatedTask((float) 1, null));
        maxCost.resources.add(r1);
        paretoOptResults.add(maxCost);
        
        // create a max time point (i.e. (inf, 0))
        ResourceList maxTime = new ResourceList();
        Resource r2 = new Resource(0, (float) 1, (float) 0);
        r2.allocateTask(new AllocatedTask(Float.MAX_VALUE, null));
        maxTime.resources.add(r2);
        paretoOptResults.add(maxTime);
        
//        TODO: f1 = 1 and not 0. need to fix
//        Float f1 = maxCost.getMaxTime();
//        Float f2 = maxCost.getFullCost();
        
        // if we can still go deeper and there is a task to schedule
        if (depth != 0 && !taskList.isEmpty()) {
            for (Resource resource : resourceList.resources) {
                // create resourceList and taskList copies
                Queue<Task> taskListCopy = new LinkedList<>(taskList);
                ResourceList resourceListCopy = new ResourceList(resourceList);
                
                // run recursive call with current resource
                ResourceList result = minmaxNature(resource, resourceListCopy, taskListCopy, alpha, depth-1);
                
                // minimise player's play with a pareto optimal and an alpha selector
                // attempt to add new result to pareto curve
                paretoOptResults.add(result);
                List<ResourceList> newParetoOptResults = Pareto.getParetoCurve(paretoOptResults);
                
//                printTab(depth);
//                System.out.println("    cost=" + result.getFullCost() + ", avgTime=" + result.getAvgTime() + ", maxTime=" + result.getMaxTime());
                
                // if added, attempt to update best result
                if (newParetoOptResults != null) {
                    // update pareto curve
                    paretoOptResults = newParetoOptResults;
                    
                    // get pareto optimal for alpha
                    ResourceList newBest = Pareto.getParetoOptimal(paretoOptResults, alpha);
                    if (newBest != null)
                        best = newBest;
                }
            }
            return best;
        }
        // if the maximum depth was reached or all tasks were scheduled
        else {
            return resourceList;
        }
    }
    
    private static ResourceList minmaxNature(Resource resource, ResourceList resourceList, Queue<Task> taskList, float alpha, int depth) {
        printTab(depth);
        System.out.println("[" + depth + "] Nature - resource: " + resource.id);
        Task task;
        
        // create current best with min cost
        ResourceList best = new ResourceList();
        best.resources.add(new Resource(0, (float)1, Float.MIN_VALUE));
        
        // if we can still go deeper
        task = taskList.poll();
        if (depth != 0) {
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
                ResourceList result = minmaxPlayer(resourceListCopy, taskListCopy, alpha, depth-1);
                
                // penalise negative wastage (underprovisioning)
//                for (Resource r : result.resources) {
//                    if (r.getWastage() < 0) {
//                        ResourceList inf = new ResourceList();
//                        Resource rr = new Resource(0, Float.MAX_VALUE);
//                        rr.allocateTask(new AllocatedTask((float) 1, null));
//                        inf.resources.add(rr);
//                        result = inf;
//                    }
//                }
                printTab(depth);
                System.out.println("    cost=" + result.getFullCost() + ", avgTime=" + result.getAvgTime() + ", maxTime=" + result.getMaxTime());
                // maxmise nature's play
                if (result.getFullCost() > best.getFullCost())
                    best = result;
            }
            return best;
        } else {
            // if the max depth was reached
            return resourceList;
        }
    }
    
    private static Resource getResource(Resource r, List<Resource> rs) {
        return rs.get(rs.indexOf(r));
    }
    
    private static void printTab(int n) {
        for (int i = 0; i < 10-n; i++) {
                System.out.print("   ");
        }
        
    }
    
}
