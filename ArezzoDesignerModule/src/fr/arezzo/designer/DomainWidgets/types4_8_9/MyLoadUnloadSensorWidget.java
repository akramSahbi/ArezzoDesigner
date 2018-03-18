package fr.arezzo.designer.DomainWidgets.types4_8_9;

import fr.arezzo.designer.Dialogs.DialogOutputMessages.Alert;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.NodeOperation;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import fr.arezzo.designer.Domain.CoordinatesOfArezzo;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType4_8_9;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;

import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.WidgetType;
import fr.arezzo.designer.DomainWidgets.types3.MyLoadUnloadWorkstationWidget;
import fr.arezzo.designer.EditeurModule.Repositories.ConnectorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.LoadUnloadSensorRepository;
import fr.arezzo.designer.Scene.Scene;
import static fr.arezzo.designer.Scene.Scene.N;
import static fr.arezzo.designer.Scene.Scene.getN;
import fr.arezzo.designer.palette.ShapeNode;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.LabelWidget;

/**
 * MyLoadUnloadSensorWidget represents a load/unload sensor widget node of type
 * 4
 *
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public final class MyLoadUnloadSensorWidget {

    //scene represents the place that will contain this widget
    public static Scene scene;
    //PropertiesOfNodesOfType4_8_9 represents the properties of the load unload widget (nodes of type 4)
    private PropertiesOfNodesOfType4_8_9 loadUnloadSensorProperties;
    //represents the icon that will be placed inside the scene
    private IconNodeWidget widget = null;
    //current instance
    private MyLoadUnloadSensorWidget instance = null;
    //connections (links) widget
    private List<MyConnectorWidget> connections = new ArrayList<>();
    //the widet editor action to edit the name of the widget on the scene
    public WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());

    /**
     * constructor which is initialized using a reference to the scene, and the
     * shape of the widget that is the icon inside the palette the role of the
     * constructor is to create the workstation widget inside the scene and add
     * to it its actions (moving action, hover action, properties menu, delete
     * menu
     *
     * @param scene the scene containing the widgets
     * @param n the shape of the node
     */
    public MyLoadUnloadSensorWidget(Scene scene, ShapeNode n) {
        instance = MyLoadUnloadSensorWidget.this;
        //initialize the domain class which contain the properties of the widget
        loadUnloadSensorProperties = new PropertiesOfNodesOfType4_8_9(scene);
        //initialize the number of the widget (ID)
        loadUnloadSensorProperties.setNumber(WidgetCommonInfo.getNumberOfNextWidget());
        loadUnloadSensorProperties.setID(loadUnloadSensorProperties.getNumber());
        //initialize our instance of the scene for direct access
        MyLoadUnloadSensorWidget.scene = scene;
        //initialize the widget which is a genereic IconWidget that will be added to the scene
        widget = new IconNodeWidget(scene);
        //initialize the widget image using the shape image (from the palette)
        widget.setImage(ImageUtilities.loadImage(n.getShape().getImage()));
        //initialize the label with the number of the widget
        widget.setLabel(loadUnloadSensorProperties.getNumber() + "");
        //add an action that will make the widget linkable (we can link it to another widget or wire)
        widget.getActions().addAction(
                ActionFactory.createExtendedConnectAction(Scene.connectionLayer, new WidgetCommonInfo().new MyConnectProvider()));
        //add an action to make the widget alignable OLD
        //widget.getActions().addAction(
        //        ActionFactory.createAlignWithMoveAction( scene.mainLayer, scene.interactionLayer, ActionFactory.createDefaultAlignWithMoveDecorator())); 
        //allow multiple selection of widgets
        WidgetCommonInfo.MyMoveStrategyProvider myMoveProvider;
        myMoveProvider = new WidgetCommonInfo.MyMoveStrategyProvider(scene);
        widget.getActions().addAction(ActionFactory.createMoveAction(myMoveProvider, myMoveProvider));

        //add an action to make the widget capable of having links from the widget to connect to another widget (line)
        widget.getLabelWidget().getActions().addAction(editorAction);
        //add an action to make the widget label editable after hovering
        widget.getActions().addAction(scene.createObjectHoverAction());
        //initializing a constant variable to be accessed from inner methods
        final Scene sceneConstantRemoteAcess = scene;
        //adding an action for a popup menu to the widget when right cliking the widget
        widget.getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {
            @Override
            public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
                //initializing the x and y coordinates of the widget using the shape coordinates
                N = (ShapeNode) sceneConstantRemoteAcess.findObject(widget);
                //initializing the x and y coordinates of the widget using the shape coordinates
                //converts from widget coordinates system (middle of widget is x:0,y:0)
                //to scene coordinates system that can have negative x or y then to
                //view coordinates system (top left == x:0,y:0)
                localLocation = widget.convertLocalToScene(new Point(0, 0));
//                    localLocation = Scene.globalScene.convertSceneToView(localLocation);
                final float x = localLocation.getLocation().x;
                final float y = localLocation.getLocation().y;
                //adding the properties menu item to the popup menu of the widget
                JMenuItem propsMenu = new JMenuItem("Proprietes");
                //verify the type of the widget 
                if (N.getShape().getType().equals("4")) {
                    propsMenu.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            AbstractNode node = new AbstractNode(Children.LEAF) {

                                //creating the properties sheet which contains all of the informations of the widget
                                //that the user can edit
                                @Override
                                protected Sheet createSheet() {
                                    //initialize the properties editor with the properties of the widget
                                    //initialize the x ad y coordinates of the widget
                                    loadUnloadSensorProperties.setxCoordinate(x);
                                    loadUnloadSensorProperties.setyCoordinate(y);

                                    //update to new Arezzo coordinates system only if changed location
                                    Point arezzoCoordinates = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
                                    if (x != (float) arezzoCoordinates.getX()) {
                                        loadUnloadSensorProperties.setxCoordinateArezzo((float) arezzoCoordinates.getX());
                                    }
                                    if (y != (float) arezzoCoordinates.getY()) {
                                        loadUnloadSensorProperties.setyCoordinateArezzo((float) arezzoCoordinates.getY());
                                    }
                                    //create the properties editor sheet
                                    Sheet sheet = super.createSheet();
                                    //create a set that the properties editor will contain
                                    Sheet.Set set = Sheet.createPropertiesSet();
                                    //we fill the sheet with all of the properties of the domain class of the widget
                                    //since the widget is a type 4 node, its properties will contain all of the
                                    //attributes of the domain class "PropertiesOfNodesOfType4_8_9" properties 
                                    set.put(new PropertiesOfNodesOfType4_8_9.NumberProperty(loadUnloadSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.XCoordinateProperty(loadUnloadSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.YCoordinateProperty(loadUnloadSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.XCoordinateArezzoProperty(loadUnloadSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.YCoordinateArezzoProperty(loadUnloadSensorProperties));

                                    set.put(new PropertiesOfNodesOfType4_8_9.TypeProperty(loadUnloadSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.AipNumberProperty(loadUnloadSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.SpeedsToNextNodesProperty(loadUnloadSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.NumbersOfNextNodesProperty(loadUnloadSensorProperties));
                                    //speed to next nodes one by one to edit
                                    int nextNodeIndex = 0;
                                    if (MyLoadUnloadSensorWidget.this.getLoadUnloadSensorProperties().getNumbersOfNextNodes() != null
                                            && MyLoadUnloadSensorWidget.this.getLoadUnloadSensorProperties().getNumbersOfNextNodes().size() > 0) {
                                        for (Integer nextNodeNumber : MyLoadUnloadSensorWidget.this.getLoadUnloadSensorProperties().getNumbersOfNextNodes()) {
                                            //NextNodeSpeedProperty
                                            set.put(new PropertiesOfNodesOfType4_8_9.NextNodeSpeedProperty(getLoadUnloadSensorProperties(),
                                                    nextNodeNumber, nextNodeIndex));
                                            nextNodeIndex++;
                                        }
                                    }

                                    sheet.put(set);

                                    return sheet;

                                }
                            };
                            //add labels for the properties editor of the widget
                            node.setDisplayName("Properties of the Load/Unload Sensor");
                            node.setShortDescription("Properties of the Load/Unload Sensor");
                            //show the properties editor of the widget when the properties menu item is cliked
                            NodeOperation.getDefault().showProperties(node);
                        }
                    });
                }
                //create the popup menu that will contain all of the menu items of this widget
                JPopupMenu popup = new JPopupMenu();

                //LINK
                JMenu linkMenu = new JMenu("Link");

                //create a menu item to attach the widget using a straight link
                JMenuItem linearLinkMenuItem = new JMenuItem("Linear Link");
                //map the link widget action listener to the Linl popup menu item
                ActionListener linearLinkActionListener = new LinkActionListener(true);
                linearLinkMenuItem.addActionListener(linearLinkActionListener);
                linkMenu.add(linearLinkMenuItem);

                //create a menu item to attach the widget using a complex polygonal link
                JMenuItem polygonalLinkMenuItem = new JMenuItem("Polygonal Link");
                //map the link widget action listener to the Linl popup menu item
                ActionListener polygonalLinkActionListener = new LinkActionListener(false);
                polygonalLinkMenuItem.addActionListener(polygonalLinkActionListener);
                linkMenu.add(polygonalLinkMenuItem);
                popup.add(linkMenu);
                popup.addSeparator();

                //create a delete widget action listener
                ActionListener actionListener = new PopupDeleteActionListener();
                //create a menu item to delete the widget
                JMenuItem deleteMenuItem = new JMenuItem("Delete");
                //map the delete widget action listener to the delete popup menu item
                deleteMenuItem.addActionListener(actionListener);
                //add the delete menu item to the popup menu of the widget
                popup.add(deleteMenuItem);
                //CUT/COPY PASTE
                JMenuItem cutMenuItem = new JMenuItem("Cut");
                cutMenuItem.setActionCommand((String) TransferHandler.getCutAction().
                        getValue(Action.NAME));

                ActionListener cutActionListener = new MyLoadUnloadSensorWidget.PopupCutActionListener(WidgetCommonInfo.WidgetType.LOAD_UNLOAD_SENSOR, instance, N);
                cutMenuItem.addActionListener(cutActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                JMenuItem copyMenuItem = new JMenuItem("Copy");
                copyMenuItem.setActionCommand((String) TransferHandler.getCopyAction().
                        getValue(Action.NAME));

                ActionListener copyActionListener = new MyLoadUnloadSensorWidget.PopupCopyActionListener(WidgetCommonInfo.WidgetType.LOAD_UNLOAD_SENSOR, instance, N);
                copyMenuItem.addActionListener(copyActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                if (N.getShape().getType().equals("4")) {
                    popup.add(propsMenu);
                    popup.addSeparator();
                    popup.add(cutMenuItem);
                    popup.add(copyMenuItem);
                }
                //return the popup menu
                return popup;
            }
        }));
    }

    /**
     * constructor
     *
     * @param scene the scene containing the widgets
     * @param n the shape of the node
     * @param copiedOrCuttedWidget the copied or cut load/unload sensor widget
     */
    public MyLoadUnloadSensorWidget(Scene scene, ShapeNode n, MyLoadUnloadSensorWidget copiedOrCuttedWidget) {
        this(scene, n);
        //copied properties
        PropertiesOfNodesOfType4_8_9 copiedProperties = copiedOrCuttedWidget.getLoadUnloadSensorProperties();
        //copy values
        getLoadUnloadSensorProperties().setType(copiedProperties.getType());
        getLoadUnloadSensorProperties().setAipNumber(copiedProperties.getAipNumber());

    }

    /**
     * class helper for beginning the linking process
     *
     */
    public class LinkActionListener implements ActionListener {

        private boolean linearLink = false;
        Point widgetPointLocation = null;

        /**
         *
         * @param linear true if linear link, false if polygonal link
         */
        public LinkActionListener(boolean linear) {
            MyConnectorWidget.isLinearLink = WidgetCommonInfo.linearLink = this.linearLink = linear;
            widgetPointLocation = MyLoadUnloadSensorWidget.this.getLoadUnloadSensorProperties().getLocation();
        }

        /**
         * the action that is performed when the link pop up menu item is
         * clicked
         *
         * @param actionEvent is the event triggered
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            try {
                MyConnectorWidget.currentlyLinking = true;
                //initiate the type of the link to be used
                WidgetCommonInfo.linearLink = linearLink;
                //simulate actions from the keyboard and the mouse to begin the linking process
                Robot robot = new Robot();
                //simulate the ctrl button hold
                robot.keyPress(KeyEvent.VK_CONTROL);
                //simulate the left mouse button hold
                //robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mousePress(InputEvent.BUTTON1_MASK);

            } catch (AWTException e) {
                //e.printStackTrace();
            }

        }
    }

    /**
     * helper class for managing the copying of a load unload sensor widget
     *
     */
    public class PopupCopyActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MyLoadUnloadSensorWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         *
         * @param type the type of the widget
         * @param myLUWw the load unload sensor widget to copy
         * @param shapeNode the shape of the load unload sensor node
         */
        public PopupCopyActionListener(WidgetType type, MyLoadUnloadSensorWidget myLUWw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = myLUWw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Load Unload Sensor")) {
                shapeNode.getShape().setName("Copy of Load Unload Sensor " + shapeNode.getShape().getName());
            }
            this.shapeNode = shapeNode;
        }

        /**
         * the action that is performed when the copy pop up menu item is
         * clicked
         *
         * @param actionEvent the event triggered
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (widgetType == WidgetType.LOAD_UNLOAD_SENSOR) {
                //copy the widget to the scene
                ccpWidget = MyLoadUnloadSensorWidget.this;
                Scene.ccpWidget = ccpWidget;
                Scene.ccpShapeNode = shapeNode;
                //repaint the scene
                Scene.globalScene.validate();
                Scene.globalScene.repaint();
                Scene.globalScene.setCopiedOrCutted(true);
            }

        }
    }

    /**
     * class helper that manages cutting a load unload sensor widget
     *
     */
    public class PopupCutActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MyLoadUnloadSensorWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         *
         * @param type the type of the node
         * @param myLUw the load unload sensor that has been cut
         * @param shapeNode the shape of the node
         */
        public PopupCutActionListener(WidgetType type, MyLoadUnloadSensorWidget myLUw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = myLUw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Load Unload Sensor")) {
                shapeNode.getShape().setName("Copy of Load Unload Sensor " + shapeNode.getShape().getName());
            }

            this.shapeNode = shapeNode;
        }

        /**
         * the action that is performed when the cut pop up menu item is clicked
         *
         * @param actionEvent the event triggered
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (widgetType == WidgetType.LOAD_UNLOAD_SENSOR) {
                //copy it to the scene
                ccpWidget = MyLoadUnloadSensorWidget.this;
                Scene.ccpWidget = ccpWidget;
                Scene.ccpShapeNode = shapeNode;
                //remove all of the connections related to this widget from the scene first
                for (MyConnectorWidget connection : getConnections()) {
                    connection.deleteNextNodeOfSourceAndLink();
                }
                //then remove the node of the widget from the scene
                scene.removeNode(shapeNode);
                //repaint the scene
                Scene.globalScene.validate();
                Scene.globalScene.repaint();
                Scene.globalScene.setCopiedOrCutted(true);
            }

        }
    }

    /**
     * helper class that manages deleting a load unload sensor widget
     *
     */
    public class PopupDeleteActionListener implements ActionListener {

        /**
         * the action that is performed when the delete pop up menu item is
         * clicked
         *
         * @param actionEvent the event triggered
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            //remove all of the connections related to this widget from the scene first
            for (MyConnectorWidget connection : getConnections()) {
                connection.deleteNextNodeOfSourceAndLink();
                //remove connection from repository
                ConnectorRepository.getInstance().remove(connection.getMyConnectorInfo().getConnectorProperties().getNumber());
            }
            //then remove the node of the widget from the scene
            scene.removeNode(getN());

            //remove from repository
            LoadUnloadSensorRepository.getInstance().remove(getLoadUnloadSensorProperties().getNumber());
        }
    }
    
    
    /**
     * helper class for editing the label of the widgets at their footer on the
     * scene
     */
    private class LabelTextFieldEditor implements TextFieldInplaceEditor {

        /**
         * whether we can modify the label of a widget
         *
         * @param widget the widget that we want to modify its label
         * @return
         */
        @Override
        public boolean isEnabled(Widget widget) {
            return true;
        }

        /**
         * gets the label of the widget
         *
         * @param widget is the widget to get its name from the scene
         * @return the name of the widget in the scene
         */
        @Override
        public String getText(Widget widget) {
            
            return MyLoadUnloadSensorWidget.this.getLoadUnloadSensorProperties().getNumber()+"";
            
        }

        /**
         * set the name of the the widget
         *
         * @param widget the widget that has a label to be edited
         * @param text the new text value that will become its new label
         */
        @Override
        public void setText(Widget widget, String text) {
            
            try
            {
                Integer newNumber = Integer.parseInt(text);
                MyLoadUnloadSensorWidget.this.getLoadUnloadSensorProperties().setNumber(newNumber);
                if(newNumber instanceof Integer)
                {
                    ((LabelWidget) widget).setLabel(newNumber+"");
                    ((LabelWidget) widget).setLabel(newNumber+"");
                    //update next nodes numbers
                    for(MyConnectorWidget conn : MyLoadUnloadSensorWidget.this.getConnections())
                    {
                        //Alert.alert("ok", "number conn: " + conn.getMyConnectorInfo().getConnectorProperties().getNumber(), Alert.AlertType.ERROR_MESSAGE);
                        Integer IdOfTargetWidget = MyLoadUnloadSensorWidget.this.getLoadUnloadSensorProperties().getID();
                        if(conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().contains(IdOfTargetWidget))
                        {
                            //Alert.alert("ok", "number conn: " + conn.getMyConnectorInfo().getConnectorProperties().getNumber(), Alert.AlertType.ERROR_MESSAGE);
                            int i = conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().indexOf(IdOfTargetWidget);
                            conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().remove(i);
                            conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(i, newNumber);
                            ConnectorRepository.getInstance().update(conn.getMyConnectorInfo().getConnectorProperties().getID(), conn);
                        }
                    }
                }
                
            }
            catch(Exception e)
            {
                
            }
            
        }
    }
    

    /**
     *
     * @return the scene container of user widgets
     */
    public static Scene getScene() {
        return scene;
    }

    /**
     *
     * @param scene the scene container of user widgets
     */
    public static void setScene(Scene scene) {
        MyLoadUnloadSensorWidget.scene = scene;
    }

    /**
     *
     * @return the properties of the load/unload sensor node
     */
    public PropertiesOfNodesOfType4_8_9 getLoadUnloadSensorProperties() {
        return loadUnloadSensorProperties;
    }

    /**
     *
     * @param loadUnloadSensorProperties the properties of the load/unload
     * sensor node
     */
    public void setLoadUnloadSensorProperties(PropertiesOfNodesOfType4_8_9 loadUnloadSensorProperties) {
        this.loadUnloadSensorProperties = loadUnloadSensorProperties;
    }

    /**
     *
     * @return the icon of the widget
     */
    public IconNodeWidget getWidget() {
        return widget;
    }

    /**
     *
     * @param widget the icon of the load unload sensor node
     */
    public void setWidget(IconNodeWidget widget) {
        this.widget = widget;
    }

    /**
     *
     * @return the current instance of the load unload sensor node
     */
    public MyLoadUnloadSensorWidget getInstance() {
        return instance;
    }

    /**
     *
     * @param instance the current instance of the load unload sensor node
     */
    public void setInstance(MyLoadUnloadSensorWidget instance) {
        this.instance = instance;
    }

    /**
     *
     * @return a list of the connectors of the load unload sensor widget
     */
    public List<MyConnectorWidget> getConnections() {
        return connections;
    }

    /**
     *
     * @param connections a list of the connectors of the load unload sensor
     * widget
     */
    public void setConnections(List<MyConnectorWidget> connections) {
        this.connections = connections;
    }

}
