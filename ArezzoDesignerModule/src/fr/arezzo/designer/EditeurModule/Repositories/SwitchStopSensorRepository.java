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

import fr.arezzo.designer.DomainWidgets.types4_8_9.MyStopSensorWidget;

/**
 *
 * @author nemesis
 */
public class SwitchStopSensorRepository implements IRepository<MyStopSensorWidget>
{

    private Map<Integer, MyStopSensorWidget> myMyStopSensorWidgets = new HashMap<>();
    private static SwitchStopSensorRepository instance = null;
    
    
    
    @Override
    public boolean add(MyStopSensorWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getStopSensorProperties().getID() != null)
            {
                myMyStopSensorWidgets.put(widget.getStopSensorProperties().getID(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MyStopSensorWidget widgetToUpdate, MyStopSensorWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getStopSensorProperties().getID() != null && find(widgetToUpdate.getStopSensorProperties().getID()) != null)
            {
                myMyStopSensorWidgets.remove(widgetToUpdate.getStopSensorProperties().getID());
                if(widgetUpdated.getStopSensorProperties().getID() != null)
                {
                    myMyStopSensorWidgets.put(widgetUpdated.getStopSensorProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MyStopSensorWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                myMyStopSensorWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getStopSensorProperties().getID() != null)
                {
                    myMyStopSensorWidgets.put(widgetUpdated.getStopSensorProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MyStopSensorWidget find(Integer widgetID)
    {
        return myMyStopSensorWidgets.get(widgetID);
    }

    @Override
    public boolean remove(Integer widgetID)
    {
        boolean done = false;
        myMyStopSensorWidgets.remove(widgetID);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MyStopSensorWidget widget)
    {
        return remove(widget.getStopSensorProperties().getID());
    }

    @Override
    public MyStopSensorWidget get(Integer widgetID)
    {
        return myMyStopSensorWidgets.get(widgetID);
    }

    @Override
    public List<MyStopSensorWidget> getAll()
    {
        List<MyStopSensorWidget> widgets = new ArrayList<>();
        widgets.addAll(myMyStopSensorWidgets.values());
        
        return widgets;
    }

    public static SwitchStopSensorRepository getInstance()
    {
        if(instance == null)
        {
            instance = new SwitchStopSensorRepository();
        }
        return instance;
    }
    
    
    
}
