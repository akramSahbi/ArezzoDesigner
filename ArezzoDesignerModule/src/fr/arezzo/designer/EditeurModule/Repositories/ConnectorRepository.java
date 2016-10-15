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
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;




/**
 *
 * @author nemesis
 */
public class ConnectorRepository implements IRepository<MyConnectorWidget>
{

    private Map<Integer, MyConnectorWidget> myMyConnectorWidgets = new HashMap<>();
    private static ConnectorRepository instance = null;
    
    
    
    @Override
    public boolean add(MyConnectorWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getMyConnectorInfo().getConnectorProperties().getNumber() != null)
            {
                myMyConnectorWidgets.put(widget.getMyConnectorInfo().getConnectorProperties().getNumber(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MyConnectorWidget widgetToUpdate, MyConnectorWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getMyConnectorInfo().getConnectorProperties().getNumber() != null && find(widgetToUpdate.getMyConnectorInfo().getConnectorProperties().getNumber()) != null)
            {
                myMyConnectorWidgets.remove(widgetToUpdate.getMyConnectorInfo().getConnectorProperties().getNumber());
                if(widgetUpdated.getMyConnectorInfo().getConnectorProperties().getNumber() != null)
                {
                    myMyConnectorWidgets.put(widgetUpdated.getMyConnectorInfo().getConnectorProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MyConnectorWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                myMyConnectorWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getMyConnectorInfo().getConnectorProperties().getNumber() != null)
                {
                    myMyConnectorWidgets.put(widgetUpdated.getMyConnectorInfo().getConnectorProperties().getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MyConnectorWidget find(Integer widgetNumber)
    {
        return myMyConnectorWidgets.get(widgetNumber);
    }

    @Override
    public boolean remove(Integer widgetNumber)
    {
        boolean done = false;
        myMyConnectorWidgets.remove(widgetNumber);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MyConnectorWidget widget)
    {
        return remove(widget.getMyConnectorInfo().getConnectorProperties().getNumber());
    }

    @Override
    public MyConnectorWidget get(Integer widgetNumber)
    {
        return myMyConnectorWidgets.get(widgetNumber);
    }

    @Override
    public List<MyConnectorWidget> getAll()
    {
        List<MyConnectorWidget> widgets = new ArrayList<>();
        widgets.addAll(myMyConnectorWidgets.values());
        
        return widgets;
    }

    public static ConnectorRepository getInstance()
    {
        if(instance == null)
        {
            instance = new ConnectorRepository();
        }
        return instance;
    }
    
    
    
}
