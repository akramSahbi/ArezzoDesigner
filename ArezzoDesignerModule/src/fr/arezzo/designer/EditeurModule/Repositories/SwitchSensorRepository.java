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

import fr.arezzo.designer.DomainWidgets.types4_8_9.MySensorWidget;

/**
 *
 * @author nemesis
 */
public class SwitchSensorRepository implements IRepository<MySensorWidget>
{

    private Map<Integer, MySensorWidget> myMySensorWidgets = new HashMap<>();
    private static SwitchSensorRepository instance = null;
    
    
    
    @Override
    public boolean add(MySensorWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getSensorProperties().getID() != null)
            {
                myMySensorWidgets.put(widget.getSensorProperties().getID(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MySensorWidget widgetToUpdate, MySensorWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getSensorProperties().getID() != null && find(widgetToUpdate.getSensorProperties().getID()) != null)
            {
                myMySensorWidgets.remove(widgetToUpdate.getSensorProperties().getID());
                if(widgetUpdated.getSensorProperties().getID() != null)
                {
                    myMySensorWidgets.put(widgetUpdated.getSensorProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MySensorWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                myMySensorWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getSensorProperties().getID() != null)
                {
                    myMySensorWidgets.put(widgetUpdated.getSensorProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MySensorWidget find(Integer widgetID)
    {
        return myMySensorWidgets.get(widgetID);
    }

    @Override
    public boolean remove(Integer widgetID)
    {
        boolean done = false;
        myMySensorWidgets.remove(widgetID);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MySensorWidget widget)
    {
        return remove(widget.getSensorProperties().getID());
    }

    @Override
    public MySensorWidget get(Integer widgetID)
    {
        return myMySensorWidgets.get(widgetID);
    }

    @Override
    public List<MySensorWidget> getAll()
    {
        List<MySensorWidget> widgets = new ArrayList<>();
        widgets.addAll(myMySensorWidgets.values());
        
        return widgets;
    }

    public static SwitchSensorRepository getInstance()
    {
        if(instance == null)
        {
            instance = new SwitchSensorRepository();
        }
        return instance;
    }
    
    
    
}
