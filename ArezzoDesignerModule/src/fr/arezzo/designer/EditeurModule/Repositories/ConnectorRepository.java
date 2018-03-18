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
            if(widget.getMyConnectorInfo().getConnectorProperties().getID() != null)
            {
                myMyConnectorWidgets.put(widget.getMyConnectorInfo().getConnectorProperties().getID(),widget);
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
            if(widgetToUpdate.getMyConnectorInfo().getConnectorProperties().getID() != null && find(widgetToUpdate.getMyConnectorInfo().getConnectorProperties().getID()) != null)
            {
                myMyConnectorWidgets.remove(widgetToUpdate.getMyConnectorInfo().getConnectorProperties().getID());
                if(widgetUpdated.getMyConnectorInfo().getConnectorProperties().getID() != null)
                {
                    myMyConnectorWidgets.put(widgetUpdated.getMyConnectorInfo().getConnectorProperties().getID(),widgetUpdated);
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
                if(widgetUpdated.getMyConnectorInfo().getConnectorProperties().getID() != null)
                {
                    myMyConnectorWidgets.put(widgetUpdated.getMyConnectorInfo().getConnectorProperties().getID(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MyConnectorWidget find(Integer widgetID)
    {
        return myMyConnectorWidgets.get(widgetID);
    }

    @Override
    public boolean remove(Integer widgetID)
    {
        boolean done = false;
        myMyConnectorWidgets.remove(widgetID);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MyConnectorWidget widget)
    {
        return remove(widget.getMyConnectorInfo().getConnectorProperties().getID());
    }

    @Override
    public MyConnectorWidget get(Integer widgetID)
    {
        return myMyConnectorWidgets.get(widgetID);
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
