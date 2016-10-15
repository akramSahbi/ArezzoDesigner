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
            if(widget.getSwitchIntermediateNodeProperties().getNumber() != null)
            {
                myMySwitchIntermediateWidgets.put(widget.getSwitchIntermediateNodeProperties().getNumber(),widget);
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
            if(widgetToUpdate.getSwitchIntermediateNodeProperties().getNumber() != null && find(widgetToUpdate.getSwitchIntermediateNodeProperties().getNumber()) != null)
            {
                myMySwitchIntermediateWidgets.remove(widgetToUpdate.getSwitchIntermediateNodeProperties().getNumber());
                if(widgetUpdated.getSwitchIntermediateNodeProperties().getNumber() != null)
                {
                    myMySwitchIntermediateWidgets.put(widgetUpdated.getSwitchIntermediateNodeProperties().getNumber(),widgetUpdated);
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
                if(widgetUpdated.getSwitchIntermediateNodeProperties().getNumber() != null)
                {
                    myMySwitchIntermediateWidgets.put(widgetUpdated.getSwitchIntermediateNodeProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MySwitchIntermediateWidget find(Integer widgetNumber)
    {
        return myMySwitchIntermediateWidgets.get(widgetNumber);
    }

    @Override
    public boolean remove(Integer widgetNumber)
    {
        boolean done = false;
        myMySwitchIntermediateWidgets.remove(widgetNumber);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MySwitchIntermediateWidget widget)
    {
        return remove(widget.getSwitchIntermediateNodeProperties().getNumber());
    }

    @Override
    public MySwitchIntermediateWidget get(Integer widgetNumber)
    {
        return myMySwitchIntermediateWidgets.get(widgetNumber);
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
