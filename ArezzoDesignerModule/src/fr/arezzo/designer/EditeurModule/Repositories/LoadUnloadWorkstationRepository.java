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

import fr.arezzo.designer.DomainWidgets.types3.MyLoadUnloadWorkstationWidget;




/**
 *
 * @author nemesis
 */
public class LoadUnloadWorkstationRepository implements IRepository<MyLoadUnloadWorkstationWidget>
{

    private Map<Integer, MyLoadUnloadWorkstationWidget> myMyLoadUnloadWorkstationWidgets = new HashMap<>();
    private static LoadUnloadWorkstationRepository instance = null;
    
    
    
    @Override
    public boolean add(MyLoadUnloadWorkstationWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getLoadUnloadWorkstationProperties().getID() != null)
            {
                myMyLoadUnloadWorkstationWidgets.put(widget.getLoadUnloadWorkstationProperties().getID(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MyLoadUnloadWorkstationWidget widgetToUpdate, MyLoadUnloadWorkstationWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getLoadUnloadWorkstationProperties().getID() != null && find(widgetToUpdate.getLoadUnloadWorkstationProperties().getID()) != null)
            {
                myMyLoadUnloadWorkstationWidgets.remove(widgetToUpdate.getLoadUnloadWorkstationProperties().getID());
                if(widgetUpdated.getLoadUnloadWorkstationProperties().getID() != null)
                {
                    myMyLoadUnloadWorkstationWidgets.put(widgetUpdated.getLoadUnloadWorkstationProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MyLoadUnloadWorkstationWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                myMyLoadUnloadWorkstationWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getLoadUnloadWorkstationProperties().getID() != null)
                {
                    myMyLoadUnloadWorkstationWidgets.put(widgetUpdated.getLoadUnloadWorkstationProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MyLoadUnloadWorkstationWidget find(Integer widgetID)
    {
        return myMyLoadUnloadWorkstationWidgets.get(widgetID);
    }

    @Override
    public boolean remove(Integer widgetID)
    {
        boolean done = false;
        myMyLoadUnloadWorkstationWidgets.remove(widgetID);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MyLoadUnloadWorkstationWidget widget)
    {
        return remove(widget.getLoadUnloadWorkstationProperties().getID());
    }

    @Override
    public MyLoadUnloadWorkstationWidget get(Integer widgetID)
    {
        return myMyLoadUnloadWorkstationWidgets.get(widgetID);
    }

    @Override
    public List<MyLoadUnloadWorkstationWidget> getAll()
    {
        List<MyLoadUnloadWorkstationWidget> widgets = new ArrayList<>();
        widgets.addAll(myMyLoadUnloadWorkstationWidgets.values());
        
        return widgets;
    }

    public static LoadUnloadWorkstationRepository getInstance()
    {
        if(instance == null)
        {
            instance = new LoadUnloadWorkstationRepository();
        }
        return instance;
    }
    
    
    
}
