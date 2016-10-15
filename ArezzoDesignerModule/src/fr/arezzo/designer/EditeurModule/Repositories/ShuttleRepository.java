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
import fr.arezzo.designer.DomainWidgets.MyShuttleWidget;




/**
 *
 * @author nemesis
 */
public class ShuttleRepository implements IRepository<MyShuttleWidget>
{

    private Map<Integer, MyShuttleWidget> myMyShuttleWidgets = new HashMap<>();
    private static ShuttleRepository instance = null;
    
    
    
    @Override
    public boolean add(MyShuttleWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getParentShuttleProperties().getNumber() != null)
            {
                myMyShuttleWidgets.put(widget.getParentShuttleProperties().getNumber(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MyShuttleWidget widgetToUpdate, MyShuttleWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getParentShuttleProperties().getNumber() != null && find(widgetToUpdate.getParentShuttleProperties().getNumber()) != null)
            {
                myMyShuttleWidgets.remove(widgetToUpdate.getParentShuttleProperties().getNumber());
                if(widgetUpdated.getParentShuttleProperties().getNumber() != null)
                {
                    myMyShuttleWidgets.put(widgetUpdated.getParentShuttleProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MyShuttleWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                myMyShuttleWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getParentShuttleProperties().getNumber() != null)
                {
                    myMyShuttleWidgets.put(widgetUpdated.getParentShuttleProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MyShuttleWidget find(Integer widgetNumber)
    {
        return myMyShuttleWidgets.get(widgetNumber);
    }

    @Override
    public boolean remove(Integer widgetNumber)
    {
        boolean done = false;
        myMyShuttleWidgets.remove(widgetNumber);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MyShuttleWidget widget)
    {
        return remove(widget.getParentShuttleProperties().getNumber());
    }

    @Override
    public MyShuttleWidget get(Integer widgetNumber)
    {
        return myMyShuttleWidgets.get(widgetNumber);
    }

    @Override
    public List<MyShuttleWidget> getAll()
    {
        List<MyShuttleWidget> widgets = new ArrayList<>();
        widgets.addAll(myMyShuttleWidgets.values());
        
        return widgets;
    }

    public static ShuttleRepository getInstance()
    {
        if(instance == null)
        {
            instance = new ShuttleRepository();
        }
        return instance;
    }
    
    
    
}
