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


import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchIntermediateWidget;

/**
 *
 * @author nemesis
 */
public class SwitchIntermediateRepository implements IRepository<MySwitchIntermediateWidget>
{

    private Map<Integer, MySwitchIntermediateWidget> myMySwitchIntermediateWidgets = new HashMap<>();
    private static SwitchIntermediateRepository instance = null;
    
    
    
    @Override
    public boolean add(MySwitchIntermediateWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getSwitchIntermediateNodeProperties().getID() != null)
            {
                myMySwitchIntermediateWidgets.put(widget.getSwitchIntermediateNodeProperties().getID(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MySwitchIntermediateWidget widgetToUpdate, MySwitchIntermediateWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getSwitchIntermediateNodeProperties().getID() != null && find(widgetToUpdate.getSwitchIntermediateNodeProperties().getID()) != null)
            {
                myMySwitchIntermediateWidgets.remove(widgetToUpdate.getSwitchIntermediateNodeProperties().getID());
                if(widgetUpdated.getSwitchIntermediateNodeProperties().getID() != null)
                {
                    myMySwitchIntermediateWidgets.put(widgetUpdated.getSwitchIntermediateNodeProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MySwitchIntermediateWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                myMySwitchIntermediateWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getSwitchIntermediateNodeProperties().getID() != null)
                {
                    myMySwitchIntermediateWidgets.put(widgetUpdated.getSwitchIntermediateNodeProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MySwitchIntermediateWidget find(Integer widgetID)
    {
        return myMySwitchIntermediateWidgets.get(widgetID);
    }

    @Override
    public boolean remove(Integer widgetID)
    {
        boolean done = false;
        myMySwitchIntermediateWidgets.remove(widgetID);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MySwitchIntermediateWidget widget)
    {
        return remove(widget.getSwitchIntermediateNodeProperties().getID());
    }

    @Override
    public MySwitchIntermediateWidget get(Integer widgetID)
    {
        return myMySwitchIntermediateWidgets.get(widgetID);
    }

    @Override
    public List<MySwitchIntermediateWidget> getAll()
    {
        List<MySwitchIntermediateWidget> widgets = new ArrayList<>();
        widgets.addAll(myMySwitchIntermediateWidgets.values());
        
        return widgets;
    }

    public static SwitchIntermediateRepository getInstance()
    {
        if(instance == null)
        {
            instance = new SwitchIntermediateRepository();
        }
        return instance;
    }
    
    
    
}
