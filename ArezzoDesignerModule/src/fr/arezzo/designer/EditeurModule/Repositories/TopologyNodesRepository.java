/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.arezzo.designer.EditeurModule.Repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchInputWidget;

/**
 *
 * @author nemesis
 */
public class TopologyNodesRepository implements IRepository<MySwitchInputWidget>
{

    private Map<Integer, MySwitchInputWidget> myMySwitchInputWidgets = new HashMap<>();
    private static TopologyNodesRepository instance = null;
    
    
    
    @Override
    public boolean add(MySwitchInputWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getSwitchInputNodeProperties().getID() != null)
            {
                myMySwitchInputWidgets.put(widget.getSwitchInputNodeProperties().getID(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MySwitchInputWidget widgetToUpdate, MySwitchInputWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getSwitchInputNodeProperties().getID() != null && find(widgetToUpdate.getSwitchInputNodeProperties().getID()) != null)
            {
                myMySwitchInputWidgets.remove(widgetToUpdate.getSwitchInputNodeProperties().getID());
                if(widgetUpdated.getSwitchInputNodeProperties().getID() != null)
                {
                    myMySwitchInputWidgets.put(widgetUpdated.getSwitchInputNodeProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MySwitchInputWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                myMySwitchInputWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getSwitchInputNodeProperties().getID() != null)
                {
                    myMySwitchInputWidgets.put(widgetUpdated.getSwitchInputNodeProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MySwitchInputWidget find(Integer widgetID)
    {
        return myMySwitchInputWidgets.get(widgetID);
    }

    @Override
    public boolean remove(Integer widgetID)
    {
        boolean done = false;
        myMySwitchInputWidgets.remove(widgetID);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MySwitchInputWidget widget)
    {
        return remove(widget.getSwitchInputNodeProperties().getID());
    }

    @Override
    public MySwitchInputWidget get(Integer widgetID)
    {
        return myMySwitchInputWidgets.get(widgetID);
    }

    @Override
    public List<MySwitchInputWidget> getAll()
    {
        List<MySwitchInputWidget> widgets = new ArrayList<>();
        widgets.addAll(myMySwitchInputWidgets.values());
        
        return widgets;
    }

    public static TopologyNodesRepository getInstance()
    {
        if(instance == null)
        {
            instance = new TopologyNodesRepository();
        }
        return instance;
    }
    
    
    
}
