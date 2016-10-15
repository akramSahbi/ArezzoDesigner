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
            if(widget.getStopSensorProperties().getNumber() != null)
            {
                myMyStopSensorWidgets.put(widget.getStopSensorProperties().getNumber(),widget);
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
            if(widgetToUpdate.getStopSensorProperties().getNumber() != null && find(widgetToUpdate.getStopSensorProperties().getNumber()) != null)
            {
                myMyStopSensorWidgets.remove(widgetToUpdate.getStopSensorProperties().getNumber());
                if(widgetUpdated.getStopSensorProperties().getNumber() != null)
                {
                    myMyStopSensorWidgets.put(widgetUpdated.getStopSensorProperties().getNumber(),widgetUpdated);
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
                if(widgetUpdated.getStopSensorProperties().getNumber() != null)
                {
                    myMyStopSensorWidgets.put(widgetUpdated.getStopSensorProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MyStopSensorWidget find(Integer widgetNumber)
    {
        return myMyStopSensorWidgets.get(widgetNumber);
    }

    @Override
    public boolean remove(Integer widgetNumber)
    {
        boolean done = false;
        myMyStopSensorWidgets.remove(widgetNumber);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MyStopSensorWidget widget)
    {
        return remove(widget.getStopSensorProperties().getNumber());
    }

    @Override
    public MyStopSensorWidget get(Integer widgetNumber)
    {
        return myMyStopSensorWidgets.get(widgetNumber);
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
