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
class ResourceList {
    
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
    
    public Float getWastage() {
        Float waste = (float) 0;
        for (Resource resource : resources) {
            waste += resource.getWastage();
        }
        
        if (waste < 0)
            return Float.MAX_VALUE;
        
        return waste;
    }
    
}
