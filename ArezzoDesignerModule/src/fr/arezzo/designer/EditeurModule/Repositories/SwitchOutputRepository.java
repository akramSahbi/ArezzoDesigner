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


import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchOutputWidget;

/**
 *
 * @author nemesis
 */
public class SwitchOutputRepository implements IRepository<MySwitchOutputWidget>
{

    private Map<Integer, MySwitchOutputWidget> myMySwitchOutputWidgets = new HashMap<>();
    private static SwitchOutputRepository instance = null;
    
    
    
    @Override
    public boolean add(MySwitchOutputWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getSwitchOutputNodeProperties().getNumber() != null)
            {
                myMySwitchOutputWidgets.put(widget.getSwitchOutputNodeProperties().getNumber(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MySwitchOutputWidget widgetToUpdate, MySwitchOutputWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getSwitchOutputNodeProperties().getNumber() != null && find(widgetToUpdate.getSwitchOutputNodeProperties().getNumber()) != null)
            {
                myMySwitchOutputWidgets.remove(widgetToUpdate.getSwitchOutputNodeProperties().getNumber());
                if(widgetUpdated.getSwitchOutputNodeProperties().getNumber() != null)
                {
                    myMySwitchOutputWidgets.put(widgetUpdated.getSwitchOutputNodeProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MySwitchOutputWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                myMySwitchOutputWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getSwitchOutputNodeProperties().getNumber() != null)
                {
                    myMySwitchOutputWidgets.put(widgetUpdated.getSwitchOutputNodeProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MySwitchOutputWidget find(Integer widgetNumber)
    {
        return myMySwitchOutputWidgets.get(widgetNumber);
    }

    @Override
    public boolean remove(Integer widgetNumber)
    {
        boolean done = false;
        myMySwitchOutputWidgets.remove(widgetNumber);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MySwitchOutputWidget widget)
    {
        return remove(widget.getSwitchOutputNodeProperties().getNumber());
    }

    @Override
    public MySwitchOutputWidget get(Integer widgetNumber)
    {
        return myMySwitchOutputWidgets.get(widgetNumber);
    }

    @Override
    public List<MySwitchOutputWidget> getAll()
    {
        List<MySwitchOutputWidget> widgets = new ArrayList<>();
        widgets.addAll(myMySwitchOutputWidgets.values());
        
        return widgets;
    }

    public static SwitchOutputRepository getInstance()
    {
        if(instance == null)
        {
            instance = new SwitchOutputRepository();
        }
        return instance;
    }
    
    
    
}
