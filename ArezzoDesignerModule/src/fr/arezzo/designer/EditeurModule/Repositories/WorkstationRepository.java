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


import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;




/**
 *
 * @author nemesis
 */
public class WorkstationRepository implements IRepository<MyWorkstationWidget>
{

    private Map<Integer, MyWorkstationWidget> myMyWorkstationWidgets = new HashMap<>();
    private static WorkstationRepository instance = null;
    
    
    
    @Override
    public boolean add(MyWorkstationWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getParentWorkstationProperties().getID() != null)
            {
                myMyWorkstationWidgets.put(widget.getParentWorkstationProperties().getID(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MyWorkstationWidget widgetToUpdate, MyWorkstationWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getParentWorkstationProperties().getID() != null && find(widgetToUpdate.getParentWorkstationProperties().getID()) != null)
            {
                myMyWorkstationWidgets.remove(widgetToUpdate.getParentWorkstationProperties().getID());
                if(widgetUpdated.getParentWorkstationProperties().getID() != null)
                {
                    myMyWorkstationWidgets.put(widgetUpdated.getParentWorkstationProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MyWorkstationWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                myMyWorkstationWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getParentWorkstationProperties().getID() != null)
                {
                    myMyWorkstationWidgets.put(widgetUpdated.getParentWorkstationProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MyWorkstationWidget find(Integer widgetID)
    {
        return myMyWorkstationWidgets.get(widgetID);
    }

    @Override
    public boolean remove(Integer widgetID)
    {
        boolean done = false;
        myMyWorkstationWidgets.remove(widgetID);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MyWorkstationWidget widget)
    {
        return remove(widget.getParentWorkstationProperties().getID());
    }

    @Override
    public MyWorkstationWidget get(Integer widgetID)
    {
        return myMyWorkstationWidgets.get(widgetID);
    }

    @Override
    public List<MyWorkstationWidget> getAll()
    {
        List<MyWorkstationWidget> widgets = new ArrayList<>();
        widgets.addAll(myMyWorkstationWidgets.values());
        
        return widgets;
    }

    public static WorkstationRepository getInstance()
    {
        if(instance == null)
        {
            instance = new WorkstationRepository();
        }
        return instance;
    }
    
    
    
}
