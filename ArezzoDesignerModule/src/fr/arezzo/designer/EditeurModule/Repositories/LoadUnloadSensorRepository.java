/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.arezzo.designer.EditeurModule.Repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.arezzo.designer.DomainWidgets.types4_8_9.MyLoadUnloadSensorWidget;




/**
 *
 * @author nemesis
 */
public class LoadUnloadSensorRepository implements IRepository<MyLoadUnloadSensorWidget>
{

    private Map<Integer, MyLoadUnloadSensorWidget> myMyLoadUnloadSensorWidgets = new HashMap<>();
    private static LoadUnloadSensorRepository instance = null;
    
    
    
    @Override
    public boolean add(MyLoadUnloadSensorWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getLoadUnloadSensorProperties().getNumber() != null)
            {
                myMyLoadUnloadSensorWidgets.put(widget.getLoadUnloadSensorProperties().getNumber(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MyLoadUnloadSensorWidget widgetToUpdate, MyLoadUnloadSensorWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getLoadUnloadSensorProperties().getNumber() != null && find(widgetToUpdate.getLoadUnloadSensorProperties().getNumber()) != null)
            {
                myMyLoadUnloadSensorWidgets.remove(widgetToUpdate.getLoadUnloadSensorProperties().getNumber());
                if(widgetUpdated.getLoadUnloadSensorProperties().getNumber() != null)
                {
                    myMyLoadUnloadSensorWidgets.put(widgetUpdated.getLoadUnloadSensorProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MyLoadUnloadSensorWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                myMyLoadUnloadSensorWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getLoadUnloadSensorProperties().getNumber() != null)
                {
                    myMyLoadUnloadSensorWidgets.put(widgetUpdated.getLoadUnloadSensorProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MyLoadUnloadSensorWidget find(Integer widgetNumber)
    {
        return myMyLoadUnloadSensorWidgets.get(widgetNumber);
    }

    @Override
    public boolean remove(Integer widgetNumber)
    {
        boolean done = false;
        myMyLoadUnloadSensorWidgets.remove(widgetNumber);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MyLoadUnloadSensorWidget widget)
    {
        return remove(widget.getLoadUnloadSensorProperties().getNumber());
    }

    @Override
    public MyLoadUnloadSensorWidget get(Integer widgetNumber)
    {
        return myMyLoadUnloadSensorWidgets.get(widgetNumber);
    }

    @Override
    public List<MyLoadUnloadSensorWidget> getAll()
    {
        List<MyLoadUnloadSensorWidget> widgets = new ArrayList<>();
        widgets.addAll(myMyLoadUnloadSensorWidgets.values());
        
        return widgets;
    }

    public static LoadUnloadSensorRepository getInstance()
    {
        if(instance == null)
        {
            instance = new LoadUnloadSensorRepository();
        }
        return instance;
    }
    
    
    
}
