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
public class ResourceList {
    
    public final List<Resource> resources;

    public ResourceList() {
        this.resources = new ArrayList<>();
    }

    // Copy constructor
    ResourceList(ResourceList resourceList) {
        
        resources = new ArrayList<>();
        
        for (Resource resource : resourceList.resources) {
            if (resource != null)
                resources.add(new Resource(resource));
        }
    }
    
    public Float getFullCost() {
        
        float cost = 0;
        
        for (Resource resource : resources)
            cost += resource.getCost();
        
        return cost;
    }
    
    public Float getAvgTime() {
        
        float time = 0;
        
        for (Resource resource : resources)
            time += resource.getExecTime();
        
        return time/resources.size();
    }
    
    public Float getMaxTime() {
        
        float maxTime = 0;
        
        for (Resource resource : resources)
            if (resource.getExecTime() > maxTime)
                maxTime = resource.getExecTime();
        
        return maxTime;
    }
    
}
