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
import fr.arezzo.designer.DomainWidgets.MySwitchWidget;

/**
 *
 * @author nemesis
 */
public class SwitchRepository implements IRepository<MySwitchWidget>
{

    private Map<Integer, MySwitchWidget> mySwitchWidgets = new HashMap<>();
    private static SwitchRepository instance = null;
    
    
    
    @Override
    public boolean add(MySwitchWidget widget)
    {
        boolean done = false;
        if(widget != null)
        {
            if(widget.getNumber() != null)
            {
                mySwitchWidgets.put(widget.getNumber(),widget);
                done = true;
            }
            
        }
        return done;
        
    }

    @Override
    public boolean update(MySwitchWidget widgetToUpdate, MySwitchWidget widgetUpdated)
    {
        boolean done = false;
        if(widgetToUpdate != null && widgetUpdated != null )
        {
            if(widgetToUpdate.getNumber() != null && find(widgetToUpdate.getNumber()) != null)
            {
                mySwitchWidgets.remove(widgetToUpdate.getNumber());
                if(widgetUpdated.getNumber() != null)
                {
                    mySwitchWidgets.put(widgetUpdated.getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public boolean update(Integer numberOfWidgetToUpdate, MySwitchWidget widgetUpdated)
    {
        boolean done = false;
        if(numberOfWidgetToUpdate != null && widgetUpdated != null )
        {
            if(numberOfWidgetToUpdate != null && find(numberOfWidgetToUpdate) != null)
            {
                mySwitchWidgets.remove(numberOfWidgetToUpdate);
                if(widgetUpdated.getNumber() != null)
                {
                    mySwitchWidgets.put(widgetUpdated.getNumber(),widgetUpdated);
                    done = true;
                }
            }
            
        }
        return done;
    }

    @Override
    public MySwitchWidget find(Integer widgetNumber)
    {
        return mySwitchWidgets.get(widgetNumber);
    }

    @Override
    public boolean remove(Integer widgetNumber)
    {
        boolean done = false;
        mySwitchWidgets.remove(widgetNumber);
        done = true;
        return done;
    }

    @Override
    public boolean remove(MySwitchWidget widget)
    {
        return remove(widget.getNumber());
    }

    @Override
    public MySwitchWidget get(Integer widgetNumber)
    {
        return mySwitchWidgets.get(widgetNumber);
    }

    @Override
    public List<MySwitchWidget> getAll()
    {
        List<MySwitchWidget> widgets = new ArrayList<>();
        widgets.addAll(mySwitchWidgets.values());
        
        return widgets;
    }

    public static SwitchRepository getInstance()
    {
        if(instance == null)
        {
            instance = new SwitchRepository();
        }
        return instance;
    }
    
    //test if switch is empty then delete it from repository so that we dont have zombie switchs
    public boolean IfUnusedSwitch_DeleteSwitch(Integer switchNumberToverify)
    {
        MySwitchWidget switchToverify = find(switchNumberToverify);
        if(switchToverify != null)
        {
            //if zombie switch => delete it
            if(switchToverify.getFirstEndStopSensorNode() == null &&
                    switchToverify.getSecondEndStopSensorNode() == null &&
                    switchToverify.getFirstInputNode() == null &&
                    switchToverify.getSecondInputNode() == null &&
                    switchToverify.getFirstIntermediateNode() == null &&
                    switchToverify.getSecondIntermediateNode() == null &&
                    switchToverify.getFirstOutputNode() == null &&
                    switchToverify.getSecondOutputNode() == null &&
                    switchToverify.getFirstSensorOutputNode() == null &&
                    switchToverify.getSecondSensorOutputNode() == null
            )
            {
                remove(switchNumberToverify);
                return true;
                        
                
            }
            return false;
        }
        else
        {
            return false;
        }
    }
    
    
    
}
