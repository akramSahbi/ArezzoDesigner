/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.arezzo.designer.actions.toolbar;

import fr.arezzo.designer.Scene.Scene;
import java.io.Serializable;
import java.util.Objects;
import org.netbeans.api.visual.widget.LayerWidget;

/**
 *
 * @author nemesis
 */

public class ArezzoDesignFile implements Serializable
{
    
    //layer of the scene which will contain all of our widgets
    private transient LayerWidget mainLayer;
    //layer of the scene which will contain all of the widgets connections
    private transient LayerWidget connectionLayer;
    //layer of the scene which will contain all of the widgets interactions
    private  transient LayerWidget interactionLayer;

    public ArezzoDesignFile() {
    }

    public ArezzoDesignFile(LayerWidget mainLayer, LayerWidget connectionLayer, LayerWidget interactionLayer) {
        this.mainLayer = mainLayer;
        this.connectionLayer = connectionLayer;
        this.interactionLayer = interactionLayer;
    }

    public LayerWidget getMainLayer() {
        return mainLayer;
    }

    public void setMainLayer(LayerWidget mainLayer) {
        this.mainLayer = mainLayer;
    }

    public LayerWidget getConnectionLayer() {
        return connectionLayer;
    }

    public void setConnectionLayer(LayerWidget connectionLayer) {
        this.connectionLayer = connectionLayer;
    }

    public LayerWidget getInteractionLayer() {
        return interactionLayer;
    }

    public void setInteractionLayer(LayerWidget interactionLayer) {
        this.interactionLayer = interactionLayer;
    }
    
    
    

    
}
