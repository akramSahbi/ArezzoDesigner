/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.arezzo.designer.EditeurModule.Repositories;

import java.util.List;

/**
 *
 * @author nemesis
 */
public interface IRepository<T> {

    /**
     * add a widget to the repository
     *
     * @param widget the widget to add to the repository
     * @return if the widget has been added
     */
    public boolean add(T widget);

    /**
     * update a widget inside the repository
     *
     * @param widgetToUpdate original widget to update
     * @param widgetUpdated updated widget
     * @return whether the widget has been updated
     */
    public boolean update(T widgetToUpdate, T widgetUpdated);

    /**
     * update a widget inside the repository
     *
     * @param widgetIDToUpdate the number property value of the widget to
     * be updated
     * @param widgetUpdated updated widget
     * @return whether the widget has been updated
     */
    public boolean update(Integer widgetIDToUpdate, T widgetUpdated);

    /**
     * find a widget from the repository
     *
     * @param widgetNumber the number of the widget to find
     * @return the widget that has been found or null
     */
    public T find(Integer widgetID);

    /**
     * remove a widget from the repository
     *
     * @param widgetID the number of the widget to remove
     * @return whether the widget has been removed
     */
    public boolean remove(Integer widgetID);

    /**
     * remove a widget from the repository
     *
     * @param widget the widget to remove
     * @return whether the widget has been removed
     */
    public boolean remove(T widget);

    /**
     * find a widget in a repository
     *
     * @param widgetID the number property value of the widget
     * @return the widget found or null
     */
    public T get(Integer widgetID);

    /**
     * find all widgets in a repository
     *
     * @return the widgets of a repository
     */
    public List<T> getAll();
}
