package fr.arezzo.designer.DomainWidgets.types_6_10_11_12;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
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
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType6_10_11_12;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;

import fr.arezzo.designer.Scene.Scene;
import static fr.arezzo.designer.Scene.Scene.N;
import static fr.arezzo.designer.Scene.Scene.getN;
import fr.arezzo.designer.palette.ShapeNode;

/**
 * MyTopologyNodesWidget represents a topology widget node of type 6
 *
 *
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class MyTopologyNodesWidget {

    //scene represents the place that will contain this widget
    public static Scene scene;
    //PropertiesOfNodesOfType6_10_11_12 represents the properties of the topology nodes (nodes of type 6)
    private PropertiesOfNodesOfType6_10_11_12 topologyNodeProperties;
    //represents the icon that will be placed inside the scene
    private IconNodeWidget widget = null;
    private Widget sourceWidget;
    private Widget targetWidget;
    private WidgetCommonInfo.WidgetType sourceWidgetType;
    private WidgetCommonInfo.WidgetType targetWidgetType;
    private Point adapterLocation = null;

    /**
     * constructor which is initialized using a reference to the scene, and the
     * shape of the widget that is the icon inside the palette the role of the
     * constructor is to create the topology node widget inside the scene and
     * add to it its actions (moving action, hover action, properties menu,
     * delete menu
     *
     * @param scene is the scene that is the container of the widgets
     * @param n is the shape of the node
     */
    public MyTopologyNodesWidget(Scene scene, ShapeNode n) {
        //initialize the domain class which contain the properties of the widget
        topologyNodeProperties = new PropertiesOfNodesOfType6_10_11_12(scene);
        //initialize the number of the widget (ID)
        topologyNodeProperties.setNumber(WidgetCommonInfo.getNumberOfNextWidget());
        //initialize the type of the widget
        topologyNodeProperties.setType(Integer.parseInt(n.getShape().getType()));
        //initialize our instance of the scene for direct access
        MyTopologyNodesWidget.scene = scene;
        //initialize the widget which is a genereic IconWidget that will be added to the scene
        widget = new IconNodeWidget(scene);

        //initialize the label of the widget if the shape is not an adapter's shape
        if (!(n.getShape().getType().equals("999"))) {
            widget.setLabel(n.getShape().getName());
        } else {
            n.getShape().setImage("fr/arezzo/designer/resources/link adapter min.png");
        }
        //initialize the widget image using the shape image (from the palette)
        widget.setImage(ImageUtilities.loadImage(n.getShape().getImage()));
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

        //add an action to make the widget capable of having a label
        //if it is not a link adapter, save space by removing it's label
        if (!(n.getShape().getType().equals("999"))) {
            widget.getLabelWidget().getActions().addAction(scene.editorAction);
            //add an action to make the widget label editable after hovering
            widget.getActions().addAction(scene.createObjectHoverAction());
        }

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
                //localLocation = Scene.globalScene.convertSceneToView(localLocation);
                final float x = localLocation.getLocation().x;
                final float y = localLocation.getLocation().y;
                adapterLocation = Scene.globalScene.convertLocalToScene(localLocation);
                //adding the properties menu item to the popup menu of the widget
                JMenuItem propsMenu = new JMenuItem("Proprietes");
                //verify the type of the widget 
                if (N.getShape().getType().equals("6")) {
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
                                    topologyNodeProperties.setxCoordinate(x);
                                    topologyNodeProperties.setyCoordinate(y);
                                    //update to new Arezzo coordinates system only if changed location
                                    Point arezzoCoordinates = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
                                    if (x != (float) arezzoCoordinates.getX()) {
                                        topologyNodeProperties.setxCoordinateArezzo((float) arezzoCoordinates.getX());
                                    }
                                    if (y != (float) arezzoCoordinates.getY()) {
                                        topologyNodeProperties.setyCoordinateArezzo((float) arezzoCoordinates.getY());
                                    }
                                    //create the properties editor sheet
                                    Sheet sheet = super.createSheet();
                                    //create a set that the properties editor will contain
                                    Sheet.Set set = Sheet.createPropertiesSet();
                                    //we fill the sheet with all of the properties of the domain class of the widget
                                    //since the widget is a type 6 node, its properties will contain all of the
                                    //attributes of the domain class "PropertiesOfNodesOfType6_10_11_12" properties 
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.NumberProperty(topologyNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.XCoordinateProperty(topologyNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.YCoordinateProperty(topologyNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.XCoordinateArezzoProperty(topologyNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.YCoordinateArezzoProperty(topologyNodeProperties));

                                    set.put(new PropertiesOfNodesOfType6_10_11_12.TypeProperty(topologyNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.SpeedsToNextNodesProperty(topologyNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.NumbersOfNextNodesProperty(topologyNodeProperties));

                                    sheet.put(set);

                                    return sheet;

                                }
                            };
                            //add labels for the properties editor of the widget
                            node.setDisplayName("Properties of the Topology Node");
                            node.setShortDescription("Properties of the Topology Node");
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
                //if the shape is a topology node (of type 6) add to the popup menu of the widget the properties menu item
                if (N.getShape().getType().equals("6")) {
                    popup.add(propsMenu);
                }

                //return the popup menu
                return popup;
            }

        }));
    }

    /**
     * helper class for beginning the linking process from a topology node
     * widget
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
            WidgetCommonInfo.linearLink = this.linearLink = linear;
            widgetPointLocation = MyTopologyNodesWidget.this.getWidget().getLocation();
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
                //begin linking process, we are following the linking process so we can disable it if the user
                //wants so
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
     * helper class for deleting a switch output widget
     *
     */
    public class PopupDeleteActionListener implements ActionListener {

        /**
         * the action that is performed when the delete pop up menu item is
         * clicked
         *
         * @param actionEvent the event that is triggered
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //remove the node of the widget from the scene
            scene.removeNode(getN());
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
        MyTopologyNodesWidget.scene = scene;
    }

    /**
     *
     * @return the properties of the topology node
     */
    public PropertiesOfNodesOfType6_10_11_12 getTopologyNodeProperties() {
        return topologyNodeProperties;
    }

    /**
     *
     * @param topologyNodeProperties the properties of the topology node
     */
    public void setTopologyNodeProperties(PropertiesOfNodesOfType6_10_11_12 topologyNodeProperties) {
        this.topologyNodeProperties = topologyNodeProperties;
    }

    /**
     *
     * @return the source widget when used for the link
     */
    public Widget getSourceWidget() {
        return sourceWidget;
    }

    /**
     *
     * @param sourceWidget the source widget when used for the link
     */
    public void setSourceWidget(Widget sourceWidget) {
        this.sourceWidget = sourceWidget;
    }

    /**
     *
     * @return the target widget when used for the link
     */
    public Widget getTargetWidget() {
        return targetWidget;
    }

    /**
     *
     * @param targetWidget the target widget when used for the link
     */
    public void setTargetWidget(Widget targetWidget) {
        this.targetWidget = targetWidget;
    }

    /**
     *
     * @return the type of the source node widget that is linked
     */
    public WidgetCommonInfo.WidgetType getSourceWidgetType() {
        return sourceWidgetType;
    }

    /**
     *
     * @param sourceWidgetType the type of the source node widget that is linked
     */
    public void setSourceWidgetType(WidgetCommonInfo.WidgetType sourceWidgetType) {
        this.sourceWidgetType = sourceWidgetType;
    }

    /**
     *
     * @return the type of the target node widget that is linked
     */
    public WidgetCommonInfo.WidgetType getTargetWidgetType() {
        return targetWidgetType;
    }

    /**
     *
     * @param targetWidgetType the type of the target node widget that is linked
     */
    public void setTargetWidgetType(WidgetCommonInfo.WidgetType targetWidgetType) {
        this.targetWidgetType = targetWidgetType;
    }

    /**
     *
     * @return the location of the adapter that connects links
     */
    public Point getAdapterLocation() {
        return adapterLocation;
    }

    /**
     *
     * @param adapterLocation the location of the adapter that connects links
     */
    public void setAdapterLocation(Point adapterLocation) {
        this.adapterLocation = adapterLocation;
    }

    /**
     *
     * @return the icon of the node widget
     */
    public IconNodeWidget getWidget() {
        return widget;
    }

    /**
     *
     * @param widget the icon of the node widget
     */
    public void setWidget(IconNodeWidget widget) {
        this.widget = widget;
    }

}
