/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.arezzo.designer.Domain;

import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;
import fr.arezzo.designer.Scene.Scene;

/**
 * ConnectorInfo is composed of informations related to a connector
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class ConnectorInfo {

    private WidgetCommonInfo.WidgetType sourceWidgetType;
    private WidgetCommonInfo.WidgetType targetWidgetType;
    private Object MySourceWidget;
    private Object MyTrgetWidget;
    private PropertiesOfNodesOfType6_10_11_12 connectorProperties;
    private boolean isLinear = false;

    /**
     * ConnectorInfo constructor using the scene that contains all of the
     * widgets
     *
     * @param scene is the component that contains all of the widgets
     */
    public ConnectorInfo(Scene scene) {
        connectorProperties = new PropertiesOfNodesOfType6_10_11_12(scene);
        connectorProperties.Type = 6;
    }

    /**
     * getMySourceWidget returns the source widget that is linked using the
     * connector
     *
     * @return an Object that is the source widget that is linked using the
     * connector
     */
    public Object getMySourceWidget() {
        return MySourceWidget;
    }

    /**
     * setMySourceWidget sets the source widget that is linked using the
     * connector
     *
     * @param MySourceWidget is the source widget that is linked using the
     * connector
     */
    public void setMySourceWidget(Object MySourceWidget) {
        this.MySourceWidget = MySourceWidget;
    }

    /**
     * getMyTrgetWidget returns the target widget that is linked using the
     * connector
     *
     * @return an Object that is the target widget that is linked using the
     * connector
     */
    public Object getMyTrgetWidget() {
        return MyTrgetWidget;
    }

    /**
     * setMyTrgetWidget sets the target widget that is linked using the
     * connector
     *
     * @param MyTrgetWidget is an object that is the target widget that is
     * linked using the connector
     */
    public void setMyTrgetWidget(Object MyTrgetWidget) {
        this.MyTrgetWidget = MyTrgetWidget;
    }

    /**
     * getSourceWidgetType returns the type of the source widget
     *
     * @return a WidgetType that represents the type of the source widget
     */
    public WidgetCommonInfo.WidgetType getSourceWidgetType() {
        return sourceWidgetType;
    }

    /**
     * setSourceWidgetType sets the type of the source widget
     *
     * @param sourceWidgetType is a WidgetType that represents the type of the
     * source widget
     */
    public void setSourceWidgetType(WidgetCommonInfo.WidgetType sourceWidgetType) {
        this.sourceWidgetType = sourceWidgetType;
    }

    /**
     * getTargetWidgetType returns the type of the target widget
     *
     * @return a WidgetType that represents the type of the target widget
     */
    public WidgetCommonInfo.WidgetType getTargetWidgetType() {
        return targetWidgetType;
    }

    /**
     * setTargetWidgetType sets the type of the target widget
     *
     * @param targetWidgetType represents the type of the target widget
     */
    public void setTargetWidgetType(WidgetCommonInfo.WidgetType targetWidgetType) {
        this.targetWidgetType = targetWidgetType;
    }

    /**
     * getConnectorProperties returns the properties of the connector
     *
     * @return PropertiesOfNodesOfType6_10_11_12 represents the properties of
     * the connector
     */
    public PropertiesOfNodesOfType6_10_11_12 getConnectorProperties() {
        return connectorProperties;
    }

    /**
     * setConnectorProperties sets the properties of the connector
     *
     * @param connectorProperties represents the properties of the connector
     */
    public void setConnectorProperties(PropertiesOfNodesOfType6_10_11_12 connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    /**
     * isIsLinear returns if the connector link is linear (true) or polygonal
     * (false)
     *
     * @return a boolean that represents if the connector link is linear (true)
     * or polygonal (false)
     */
    public boolean isIsLinear() {
        return isLinear;
    }

    /**
     * setIsLinear sets the connector link to linear (true) or polygonal (false)
     *
     * @param isLinear is a boolean that represents if the connector link is
     * linear (true) or polygonal (false)
     */
    public void setIsLinear(boolean isLinear) {
        this.isLinear = isLinear;
    }

}
