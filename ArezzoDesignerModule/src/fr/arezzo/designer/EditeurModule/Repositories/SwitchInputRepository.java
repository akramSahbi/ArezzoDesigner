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
public class SwitchInputRepository implements IRepository<MySwitchInputWidget>
{

    private Map<Integer, MySwitchInputWidget> myMySwitchInputWidgets = new HashMap<>();
    private static SwitchInputRepository instance = null;
    
    
    
    @Override
    public boolean add(MySwitchInputWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getSwitchInputNodeProperties().getNumber() != null)
            {
                myMySwitchInputWidgets.put(widget.getSwitchInputNodeProperties().getNumber(),widget);
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
            if(widgetToUpdate.getSwitchInputNodeProperties().getNumber() != null && find(widgetToUpdate.getSwitchInputNodeProperties().getNumber()) != null)
            {
                myMySwitchInputWidgets.remove(widgetToUpdate.getSwitchInputNodeProperties().getNumber());
                if(widgetUpdated.getSwitchInputNodeProperties().getNumber() != null)
                {
                    myMySwitchInputWidgets.put(widgetUpdated.getSwitchInputNodeProperties().getNumber(),widgetUpdated);
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
                if(widgetUpdated.getSwitchInputNodeProperties().getNumber() != null)
                {
                    myMySwitchInputWidgets.put(widgetUpdated.getSwitchInputNodeProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MySwitchInputWidget find(Integer widgetNumber)
    {
        return myMySwitchInputWidgets.get(widgetNumber);
    }

    @Override
    public boolean remove(Integer widgetNumber)
    {
        boolean done = false;
        myMySwitchInputWidgets.remove(widgetNumber);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MySwitchInputWidget widget)
    {
        return remove(widget.getSwitchInputNodeProperties().getNumber());
    }

    @Override
    public MySwitchInputWidget get(Integer widgetNumber)
    {
        return myMySwitchInputWidgets.get(widgetNumber);
    }

    @Override
    public List<MySwitchInputWidget> getAll()
    {
        List<MySwitchInputWidget> widgets = new ArrayList<>();
        widgets.addAll(myMySwitchInputWidgets.values());
        
        return widgets;
    }

    public static SwitchInputRepository getInstance()
    {
        if(instance == null)
        {
            instance = new SwitchInputRepository();
        }
        return instance;
    }
    
    
    
}
