package fr.arezzo.designer.DomainWidgets.types_6_10_11_12;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import fr.arezzo.designer.Scene.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.NodeOperation;
import org.openide.nodes.Sheet;
import fr.arezzo.designer.Dialogs.DialogOutputMessages.Alert;
import fr.arezzo.designer.Domain.ConnectorInfo;
import fr.arezzo.designer.Domain.CoordinatesOfArezzo;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType6_10_11_12;
import fr.arezzo.designer.DomainWidgets.MyShuttleWidget;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.WidgetType;
import fr.arezzo.designer.DomainWidgets.types3.MyLoadUnloadWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyLoadUnloadSensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MySensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyStopSensorWidget;
import fr.arezzo.designer.EditeurModule.Repositories.ConnectorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.ShuttleRepository;
import fr.arezzo.designer.actions.verification.graph.VerifyNetwork;
import fr.arezzo.designer.palette.Shape;
import fr.arezzo.designer.palette.ShapeNode;

/**
 * MyConnectorWidget represents a connector widget node of type 6
 *
 *
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class MyConnectorWidget extends ConnectionWidget {

    public static Scene scene;
    private ConnectorInfo myConnectorInfo;

    private Widget sourceWidget;
    private Widget targetWidget;
    private WidgetType sourceWidgetType;
    private WidgetType targetWidgetType;
    private MyConnectorWidget instance = null;

    private AnchorFactory.DirectionalAnchorKind sourceDirection = null;
    private AnchorFactory.DirectionalAnchorKind targetDirection = null;
    //connections (links) widget
    private List<MyConnectorWidget> connections = new ArrayList<>();
    //is this link a link to/from an adapter
    private boolean isAdapterLink = false;
    //is this a linear link?
    public static boolean isLinearLink = false;
    public static boolean currentlyLinking = false;

    /**
     * constructor
     *
     * @param scene is the scene that is the container of the widgets
     */
    public MyConnectorWidget(Scene scene) {

        super(scene);
        instance = MyConnectorWidget.this;
        myConnectorInfo = new ConnectorInfo(scene);
        MyConnectorWidget.scene = scene;

        //removed diirect connection between links must have an intermediate adapter
        //add an action that will make the widget linkable (we can link it to another widget or wire)
        //getActions().addAction( 
        //        ActionFactory.createExtendedConnectAction( scene.connectionLayer, new WidgetCommonInfo().new MyConnectProvider())); 
        getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {
            @Override
            public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
                //initializing the x and y coordinates of the widget using the shape coordinates
                //N = (ShapeNode) Scene.globalScene.findObject(widget);  
                Scene.C = MyConnectorWidget.this;
                    //set the location on the scene where the widget will be dropped and displayed

                //initializing the x and y coordinates of the widget using the shape coordinates
                //converts from widget coordinates system (middle of widget is x:0,y:0)
                //to scene coordinates system that can have negative x or y then to
                //view coordinates system (top left == x:0,y:0)
                Point middleOfConnection = MyConnectorWidget.this.getControlPoints().get(0);
                localLocation = MyConnectorWidget.this.convertLocalToScene(middleOfConnection);
                    //localLocation = Scene.globalScene.convertSceneToView(localLocation);

                //localLocation = widget.convertLocalToScene(new Point(0,0));
                //localLocation = Scene.globalScene.convertSceneToView(localLocation);
                final float x = localLocation.getLocation().x;
                final float y = localLocation.getLocation().y;
                //adding the properties menu item to the popup menu of the widget
                JMenuItem propsMenu = new JMenuItem("Proprietes");

                propsMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AbstractNode node = new AbstractNode(Children.LEAF) {

                            //creating the properties sheet which contains all of the informations of the widget
                            //that the user can edit
                            @Override
                            protected Sheet createSheet() {

                                //Point locationRelativeToScene = new Point((int)x, (int)y);
                                //initialize the properties editor with the properties of the widget
                                //initialize the x ad y coordinates of the widget
                                myConnectorInfo.getConnectorProperties().setxCoordinate( x);
                                myConnectorInfo.getConnectorProperties().setyCoordinate( y);
                                //update to new Arezzo coordinates system only if changed location
                                Point arezzoCoordinates = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
                                if (x != (float) arezzoCoordinates.getX()) {
                                    myConnectorInfo.getConnectorProperties().setxCoordinateArezzo((float) arezzoCoordinates.getX());
                                }
                                if (y != (float) arezzoCoordinates.getY()) {
                                    myConnectorInfo.getConnectorProperties().setyCoordinateArezzo((float) arezzoCoordinates.getY());
                                }
                                //create the properties editor sheet
                                Sheet sheet = super.createSheet();
                                //create a set that the properties editor will contain
                                Sheet.Set set = Sheet.createPropertiesSet();
                                //we fill the sheet with all of the properties of the domain class of the widget
                                //since the widget is a type 6 node, its properties will contain all of the
                                //attributes of the domain class "PropertiesOfNodesOfType6_10_11_12" properties 
                                set.put(new PropertiesOfNodesOfType6_10_11_12.NumberProperty(myConnectorInfo.getConnectorProperties()));
                                set.put(new PropertiesOfNodesOfType6_10_11_12.XCoordinateProperty(myConnectorInfo.getConnectorProperties()));
                                set.put(new PropertiesOfNodesOfType6_10_11_12.YCoordinateProperty(myConnectorInfo.getConnectorProperties()));
                                set.put(new PropertiesOfNodesOfType6_10_11_12.XCoordinateArezzoProperty(myConnectorInfo.getConnectorProperties()));
                                set.put(new PropertiesOfNodesOfType6_10_11_12.YCoordinateArezzoProperty(myConnectorInfo.getConnectorProperties()));
                                set.put(new PropertiesOfNodesOfType6_10_11_12.TypeProperty(myConnectorInfo.getConnectorProperties()));
                                set.put(new PropertiesOfNodesOfType6_10_11_12.SpeedsToNextNodesProperty(myConnectorInfo.getConnectorProperties()));
                                set.put(new PropertiesOfNodesOfType6_10_11_12.NumbersOfNextNodesProperty(myConnectorInfo.getConnectorProperties()));
                                //speed to next nodes one by one to edit
                                int nextNodeIndex = 0;
                                if (MyConnectorWidget.this.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes() != null
                                        && MyConnectorWidget.this.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                                    for (Integer nextNodeNumber : MyConnectorWidget.this.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes()) {
                                        //NextNodeSpeedProperty
                                        set.put(new PropertiesOfNodesOfType6_10_11_12.NextNodeSpeedProperty(myConnectorInfo.getConnectorProperties(),
                                                nextNodeNumber, nextNodeIndex));
                                        nextNodeIndex++;
                                    }
                                }
                                sheet.put(set);
                                return sheet;
                            }
                        };
                        //add labels for the properties editor of the widget
                        node.setDisplayName("Properties of the Link Node");
                        node.setShortDescription("Properties of the Link Node");
                        //show the properties editor of the widget when the properties menu item is cliked
                        NodeOperation.getDefault().showProperties(node);
                    }
                });
                //}
                //do not allow  direct links between connectors
                //create the popup menu that will contain all of the menu items of this widget
                JPopupMenu popup = new JPopupMenu();

                //Attach SHUTTLE
                JMenuItem attachShuttleMenuItem = new JMenuItem("Attach Shuttle");
                //map the link widget action listener to the Linl popup menu item
                ActionListener attachShuttleActionListener = new AttachShuttleActionListener();
                attachShuttleMenuItem.addActionListener(attachShuttleActionListener);
                popup.add(attachShuttleMenuItem);
                popup.addSeparator();

                //create a menu item to delete the widget
                JMenuItem deleteMenuItem = new JMenuItem("Delete");
                //map the delete widget action listener to the delete popup menu item
                deleteMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        //delete from repository
                        ConnectorRepository.getInstance().remove(MyConnectorWidget.this.getMyConnectorInfo().getConnectorProperties().getNumber());

                        //delete the next node of  source => link  and  link => target
                        //and update nextNodes of source
                        deleteNextNodeOfSourceAndLink();

                    }
                ;
                });


                //add the delete menu item to the popup menu of the widget
                //position 2
                popup.add(deleteMenuItem);
                //position 3
                popup.add(propsMenu);
                //position 4
                popup.addSeparator();
                //change the link direction according to the position of the source and target widgets
                //if their positions  have been updated
                //or just reverse the directions of each widget (source and target)
                JMenuItem reverseLink = new JMenuItem("Update Link Direction");
                //set enabled only if this link is not for an adapter
                reverseLink.setEnabled(!isAdapterLink);
                //update link only if it is not a linear link
                //reverseLink.setEnabled(isLinearLink);
                reverseLink.setEnabled(true);
                reverseLink.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateDirection();
                    }
                ;
                });
                //position 5
                popup.add(reverseLink);
                return popup;
            }
        }));
        getActions().addAction(scene.createWidgetHoverAction());

    }

    /**
     * helper class to manage attaching a shuttle on a connector widget
     *
     */
    public class AttachShuttleActionListener implements ActionListener {

        //the action that is performed when the attach popup menu item is clicked
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //verify that the shuttle is indeed in a waiting loop
            if (VerifyNetwork.getInstance().canWeAttachShuttleToLink(myConnectorInfo.getConnectorProperties().getNumber())) {
                Shape shapeShuttle = new Shape(3, "Shuttles", "99", WidgetCommonInfo.WidgetType.SHUTTLE, "Shuttle", "fr/arezzo/designer/resources/shuttle.png");
                ShapeNode shapeNodeShuttle = new ShapeNode(shapeShuttle);

                MyShuttleWidget shuttleWidget = new MyShuttleWidget(Scene.globalScene, shapeNodeShuttle);
                shuttleWidget.getParentShuttleProperties().setNumbersOfNextNodes(MyConnectorWidget.this.getMyConnectorInfo()
                        .getConnectorProperties().getNumbersOfNextNodes());
                shuttleWidget.getShuttleProperties().setCurrentPathLinkNumber(MyConnectorWidget.this.getMyConnectorInfo().getConnectorProperties()
                        .getNumber());
                shuttleWidget.getShuttleProperties().setStartNodeNumber(MyConnectorWidget.this.getMyConnectorInfo().getConnectorProperties()
                        .getNumber());
                shuttleWidget.getShuttleProperties().setEndNodeNumber((MyConnectorWidget.this.getMyConnectorInfo().getConnectorProperties()
                        .getNumbersOfNextNodes().isEmpty() ? 0 : MyConnectorWidget.this.getMyConnectorInfo().getConnectorProperties()
                                .getNumbersOfNextNodes().get(0)));
                shuttleWidget.getShuttleProperties().setDistanceFromStartingNode(0.0);
                //assign the generic widget that has to be attached to the scene
                IconNodeWidget widget = shuttleWidget.getWidget();

                shuttleWidget.getConnections().add(MyConnectorWidget.this);
                MyConnectorWidget.this.addChild(widget, shuttleWidget);
                MyConnectorWidget.this.setConstraint(widget, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER_SOURCE, 0.5f);
                Scene.globalScene.validate();
                //add this widget reference to the scene and map it to its ShapeNode so that we can edit its label
                Scene.globalScene.addObject(shapeNodeShuttle, widget);
                Scene.globalScene.validate();
                Scene.globalScene.repaint();
            //Alert.alert("shape type = "+shapeNodeShuttle.getShape().getType(), "shape type = "+shapeNodeShuttle.getShape().getType(), Alert.AlertType.INFORMATION_MESSAGE);
                //Alert.alert("N  = null ? "+(N == null), "N  = null ? "+(N == null), Alert.AlertType.INFORMATION_MESSAGE);
                //Scene.mainLayer.addChild(widget);

                //add to repository
                ShuttleRepository.getInstance().add(shuttleWidget);
                //add to scene
                Scene.globalScene.getMyWidgetsAdded().put(shuttleWidget.getWidget(), shuttleWidget);
            } else {
                Alert.alert("Warning Cannot Attach shuttle to this link", "This link is not part of a waiting loop!", Alert.AlertType.WARNING_MESSAGE);
            }
        }
    }

    /**
     * change the direction of the connected widgets (source widget and target
     * widget) at the edges of the POLYGONAL connection the direction can be
     * horizontal or vertical in each edge (source widget and destination
     * widget) at the end of the connection h means horizontal direction and v
     * means vertical direction 1 to 4 are all the possibilities of the source
     * widget and destination widget directions 1 ==> source = h & destination =
     * h if 1 ==> change to 2 2 ==> source = h & target = v if 2 ==> change to 3
     * 3 ==> source = v & target = h if 3 ==> change to 4 4 ==> source = v &
     * target = v if 4 ==> change to 1
     */
    public void updateDirection() {
        isLinearLink = getMyConnectorInfo().isIsLinear();
        //Alert.alert("is linear = " + isLinearLink, "is linear = " + isLinearLink, Alert.AlertType.INFORMATION_MESSAGE);
        if (!isLinearLink) {
            if (sourceDirection == AnchorFactory.DirectionalAnchorKind.HORIZONTAL && targetDirection == AnchorFactory.DirectionalAnchorKind.HORIZONTAL) {
                sourceDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
                targetDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
            } else if (sourceDirection == AnchorFactory.DirectionalAnchorKind.HORIZONTAL && targetDirection == AnchorFactory.DirectionalAnchorKind.VERTICAL) {
                sourceDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
                targetDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
            } else if (sourceDirection == AnchorFactory.DirectionalAnchorKind.VERTICAL && targetDirection == AnchorFactory.DirectionalAnchorKind.HORIZONTAL) {
                sourceDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
                targetDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
            } else if (sourceDirection == AnchorFactory.DirectionalAnchorKind.VERTICAL && targetDirection == AnchorFactory.DirectionalAnchorKind.VERTICAL) {
                sourceDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
                targetDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
            }

            (MyConnectorWidget.this).setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceWidget, sourceDirection));
            (MyConnectorWidget.this).setTargetAnchor(AnchorFactory.createDirectionalAnchor(targetWidget, targetDirection));
        }

    }

    /**
     * update the direction of the connector based on current direction and
     * position of source widget and destination widget also based on the widget
     * size so that the connector don't go far away from the current widgets
     *
     * @param widgetSize the size of the side of a widget which is represented
     * as a square
     */
    public void updateDirection(double widgetSize) {

        double sizeOfWidget = widgetSize;

        //bug fix some widgets does not have preferred location so we initialize their preferred location with their current location
        if (targetWidget.getPreferredLocation() == null) {
            targetWidget.setPreferredLocation(targetWidget.getLocation());
        }
        if (sourceWidget.getPreferredLocation() == null) {
            sourceWidget.setPreferredLocation(sourceWidget.getLocation());
        }
        double x_positionOfTarget = targetWidget.getPreferredLocation() == null ? targetWidget.getLocation().getX()
                : targetWidget.getPreferredLocation().getX();
        double y_positionOfTarget = targetWidget.getPreferredLocation() == null ? targetWidget.getLocation().getY()
                : targetWidget.getPreferredLocation().getY();
        //source widget can be a connector that dont have a preferred location
        double x_positionOfSource = (ShapeNode) Scene.globalScene.findObject(sourceWidget) != null ? sourceWidget.getPreferredLocation().getX()
                : sourceWidget.getLocation().getX();
        double y_positionOfSource = (ShapeNode) Scene.globalScene.findObject(sourceWidget) != null ? sourceWidget.getPreferredLocation().getY()
                : sourceWidget.getLocation().getY();
        double diff_XSource_XTarget_abs = Math.abs(x_positionOfTarget - x_positionOfSource);
        double diff_YSource_YTarget_abs = Math.abs(y_positionOfTarget - y_positionOfSource);
        double diff_XSource_XTarget = x_positionOfTarget - x_positionOfSource;
        double diff_YSource_YTarget = y_positionOfTarget - y_positionOfSource;

        //new direction depending on position of both source and target
        AnchorFactory.DirectionalAnchorKind newSourceDirection;
        AnchorFactory.DirectionalAnchorKind newTargetDirection;
        if (diff_XSource_XTarget_abs < sizeOfWidget) {
            newSourceDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
            newTargetDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
            if (newSourceDirection != sourceDirection || newTargetDirection != targetDirection) {
                setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceWidget, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                setSourceDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                setTargetAnchor(AnchorFactory.createDirectionalAnchor(targetWidget, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                setTargetDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                return;
            }

        } else if (diff_YSource_YTarget_abs < sizeOfWidget) {
            newSourceDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
            newTargetDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
            if (newSourceDirection != sourceDirection || newTargetDirection != targetDirection) {
                setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceWidget, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                setSourceDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                setTargetAnchor(AnchorFactory.createDirectionalAnchor(targetWidget, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                setTargetDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                return;
            }
        } else if (diff_XSource_XTarget > 0 && diff_YSource_YTarget > 0) {
            newSourceDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
            newTargetDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
            if (newSourceDirection != sourceDirection || newTargetDirection != targetDirection) {
                setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceWidget, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                setSourceDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                setTargetAnchor(AnchorFactory.createDirectionalAnchor(targetWidget, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                setTargetDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                return;
            }

        } else if (diff_XSource_XTarget > 0 && diff_YSource_YTarget < 0) {
            newSourceDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
            newTargetDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
            if (newSourceDirection != sourceDirection || newTargetDirection != targetDirection) {
                setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceWidget, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                setSourceDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                setTargetAnchor(AnchorFactory.createDirectionalAnchor(targetWidget, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                setTargetDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                return;
            }
        } else if (diff_XSource_XTarget < 0 && diff_YSource_YTarget < 0) {
            newSourceDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
            newTargetDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
            if (newSourceDirection != sourceDirection || newTargetDirection != targetDirection) {
                setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceWidget, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                setSourceDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                setTargetAnchor(AnchorFactory.createDirectionalAnchor(targetWidget, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                setTargetDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                return;
            }

        } else if (diff_XSource_XTarget < 0 && diff_YSource_YTarget > 0) {
            newSourceDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
            newTargetDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
            if (newSourceDirection != sourceDirection || newTargetDirection != targetDirection) {
                setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceWidget, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                setSourceDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                setTargetAnchor(AnchorFactory.createDirectionalAnchor(targetWidget, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                setTargetDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                return;
            }
        }

        if (sourceDirection == AnchorFactory.DirectionalAnchorKind.HORIZONTAL) {
            sourceDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
        } else {
            sourceDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
        }

        if (targetDirection == AnchorFactory.DirectionalAnchorKind.HORIZONTAL) {
            targetDirection = AnchorFactory.DirectionalAnchorKind.VERTICAL;
        } else {
            targetDirection = AnchorFactory.DirectionalAnchorKind.HORIZONTAL;
        }

        (MyConnectorWidget.this).setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceWidget, sourceDirection));
        (MyConnectorWidget.this).setTargetAnchor(AnchorFactory.createDirectionalAnchor(targetWidget, targetDirection));
    }

    /**
     * helper class for beginning the linking process from a connector widget
     *
     */
    public class LinkActionListener implements ActionListener {

        private boolean linearLink = false;

        /**
         *
         * @param linear true if linear link, false if polygonal link
         */
        public LinkActionListener(boolean linear) {
            WidgetCommonInfo.linearLink = this.linearLink = linear;

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
     * method for deleting a link; and when a user deletes a link between two
     * nodes, we must update the next nodes of the source before deleting the
     * link
     */
    public void deleteNextNodeOfSourceAndLink() {
        /*
         //remove all of the connections related to this widget from the scene first
         for(MyConnectorWidget connection : getConnections())
         {
         connection.deleteNextNodeOfSourceAndLink();
         }
         //then remove the node of the widget from the scene
         Scene.globalScene.removeNode(getN());
         */
        //get current source and target of the link
        Object MySourceWidget = getMyConnectorInfo().getMySourceWidget();
        //check if the current connection number is in MyWidget nextNodes
        boolean currentConnectionNumberFound = false;
        //update the nextNodes field of the sourceWidget depending on its type
        if (sourceWidgetType.equals(WidgetCommonInfo.WidgetType.WORKSTATION)) {

            MyWorkstationWidget temp = (MyWorkstationWidget) MySourceWidget;
            int numberOfcurrentConnection = getMyConnectorInfo().getConnectorProperties().getNumber();
            int indexOfNextWidget = 0;
            for (int number : temp.getParentWorkstationProperties().getNumbersOfNextNodes()) {
                if (numberOfcurrentConnection == number) {
                    currentConnectionNumberFound = true;
                    break;
                }
                indexOfNextWidget++;
            }
            if (currentConnectionNumberFound) {
                //remove from next nodes and speeds to next nodes
                temp.getParentWorkstationProperties().getSpeedsToNextNodes().remove(indexOfNextWidget);
                temp.getParentWorkstationProperties().getNumbersOfNextNodes().remove(indexOfNextWidget);

            }

        } else if (sourceWidgetType.equals(WidgetCommonInfo.WidgetType.LOAD_UNLOAD_WORKSTATION)) {

            MyLoadUnloadWorkstationWidget temp = (MyLoadUnloadWorkstationWidget) MySourceWidget;
            int numberOfcurrentConnection = getMyConnectorInfo().getConnectorProperties().getNumber();
            int indexOfNextWidget = 0;
            for (int number : temp.getLoadUnloadWorkstationProperties().getNumbersOfNextNodes()) {
                if (numberOfcurrentConnection == number) {
                    currentConnectionNumberFound = true;
                    break;
                }
                indexOfNextWidget++;
            }
            if (currentConnectionNumberFound) {
                //remove from next nodes and speeds to next nodes
                temp.getLoadUnloadWorkstationProperties().getSpeedsToNextNodes().remove(indexOfNextWidget);
                temp.getLoadUnloadWorkstationProperties().getNumbersOfNextNodes().remove(indexOfNextWidget);
            }
        } else if (sourceWidgetType.equals(WidgetCommonInfo.WidgetType.LOAD_UNLOAD_SENSOR)) {

            MyLoadUnloadSensorWidget temp = (MyLoadUnloadSensorWidget) MySourceWidget;
            int numberOfcurrentConnection = getMyConnectorInfo().getConnectorProperties().getNumber();
            int indexOfNextWidget = 0;
            for (int number : temp.getLoadUnloadSensorProperties().getNumbersOfNextNodes()) {
                if (numberOfcurrentConnection == number) {
                    currentConnectionNumberFound = true;
                    break;
                }
                indexOfNextWidget++;
            }
            if (currentConnectionNumberFound) {
                //remove from next nodes and speeds to next nodes
                temp.getLoadUnloadSensorProperties().getSpeedsToNextNodes().remove(indexOfNextWidget);
                temp.getLoadUnloadSensorProperties().getNumbersOfNextNodes().remove(indexOfNextWidget);
            }
        } else if (sourceWidgetType.equals(WidgetCommonInfo.WidgetType.SENSOR)) {

            MySensorWidget temp = (MySensorWidget) MySourceWidget;
            int numberOfcurrentConnection = getMyConnectorInfo().getConnectorProperties().getNumber();
            int indexOfNextWidget = 0;
            for (int number : temp.getSensorProperties().getNumbersOfNextNodes()) {
                if (numberOfcurrentConnection == number) {
                    currentConnectionNumberFound = true;
                    break;
                }
                indexOfNextWidget++;
            }
            if (currentConnectionNumberFound) {
                //remove from next nodes and speeds to next nodes
                temp.getSensorProperties().getSpeedsToNextNodes().remove(indexOfNextWidget);
                temp.getSensorProperties().getNumbersOfNextNodes().remove(indexOfNextWidget);
            }
        } else if (sourceWidgetType.equals(WidgetCommonInfo.WidgetType.STOP_SENSOR)) {

            MyStopSensorWidget temp = (MyStopSensorWidget) MySourceWidget;
            int numberOfcurrentConnection = getMyConnectorInfo().getConnectorProperties().getNumber();
            int indexOfNextWidget = 0;
            for (int number : temp.getStopSensorProperties().getNumbersOfNextNodes()) {
                if (numberOfcurrentConnection == number) {
                    currentConnectionNumberFound = true;
                    break;
                }
                indexOfNextWidget++;
            }
            if (currentConnectionNumberFound) {
                //remove from next nodes and speeds to next nodes
                temp.getStopSensorProperties().getSpeedsToNextNodes().remove(indexOfNextWidget);
                temp.getStopSensorProperties().getNumbersOfNextNodes().remove(indexOfNextWidget);
            }
        } else if (sourceWidgetType.equals(WidgetCommonInfo.WidgetType.SWITCH_INPUT)) {

            MySwitchInputWidget temp = (MySwitchInputWidget) MySourceWidget;
            int numberOfcurrentConnection = getMyConnectorInfo().getConnectorProperties().getNumber();
            int indexOfNextWidget = 0;
            for (int number : temp.getSwitchInputNodeProperties().getNumbersOfNextNodes()) {
                if (numberOfcurrentConnection == number) {
                    currentConnectionNumberFound = true;
                    break;
                }
                indexOfNextWidget++;
            }
            if (currentConnectionNumberFound) {
                //remove from next nodes and speeds to next nodes
                temp.getSwitchInputNodeProperties().getSpeedsToNextNodes().remove(indexOfNextWidget);
                temp.getSwitchInputNodeProperties().getNumbersOfNextNodes().remove(indexOfNextWidget);
            }
        } else if (sourceWidgetType.equals(WidgetCommonInfo.WidgetType.SWITCH_INTERMEDATE)) {

            MySwitchIntermediateWidget temp = (MySwitchIntermediateWidget) MySourceWidget;
            int numberOfcurrentConnection = getMyConnectorInfo().getConnectorProperties().getNumber();
            int indexOfNextWidget = 0;
            for (int number : temp.getSwitchIntermediateNodeProperties().getNumbersOfNextNodes()) {
                if (numberOfcurrentConnection == number) {
                    currentConnectionNumberFound = true;
                    break;
                }
                indexOfNextWidget++;
            }
            if (currentConnectionNumberFound) {
                //remove from next nodes and speeds to next nodes
                temp.getSwitchIntermediateNodeProperties().getSpeedsToNextNodes().remove(indexOfNextWidget);
                temp.getSwitchIntermediateNodeProperties().getNumbersOfNextNodes().remove(indexOfNextWidget);
            }
        } else if (sourceWidgetType.equals(WidgetCommonInfo.WidgetType.SWITCH_OUTPUT)) {

            MySwitchOutputWidget temp = (MySwitchOutputWidget) MySourceWidget;
            int numberOfcurrentConnection = getMyConnectorInfo().getConnectorProperties().getNumber();
            int indexOfNextWidget = 0;
            for (int number : temp.getSwitchOutputNodeProperties().getNumbersOfNextNodes()) {
                if (numberOfcurrentConnection == number) {
                    currentConnectionNumberFound = true;
                    break;
                }
                indexOfNextWidget++;
            }
            if (currentConnectionNumberFound) {
                //remove from next nodes and speeds to next nodes
                temp.getSwitchOutputNodeProperties().getSpeedsToNextNodes().remove(indexOfNextWidget);
                temp.getSwitchOutputNodeProperties().getNumbersOfNextNodes().remove(indexOfNextWidget);
            }
        } else if (sourceWidgetType.equals(WidgetCommonInfo.WidgetType.SHUTTLE)) {

            MyShuttleWidget temp = (MyShuttleWidget) MySourceWidget;
            int numberOfcurrentConnection = getMyConnectorInfo().getConnectorProperties().getNumber();
            int indexOfNextWidget = 0;
            for (int number : temp.getParentShuttleProperties().getNumbersOfNextNodes()) {
                if (numberOfcurrentConnection == number) {
                    currentConnectionNumberFound = true;
                    break;
                }
                indexOfNextWidget++;
            }
            if (currentConnectionNumberFound) {
                //remove from next nodes and speeds to next nodes
                temp.getParentShuttleProperties().getSpeedsToNextNodes().remove(indexOfNextWidget);
                temp.getParentShuttleProperties().getNumbersOfNextNodes().remove(indexOfNextWidget);
            }
        } else if (sourceWidgetType.equals(WidgetCommonInfo.WidgetType.LINK_NODE)) {

            MyConnectorWidget temp = (MyConnectorWidget) MySourceWidget;
            int numberOfcurrentConnection = getMyConnectorInfo().getConnectorProperties().getNumber();
            int indexOfNextWidget = 0;
            if (temp.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                for (int number : temp.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes()) {
                    if (numberOfcurrentConnection == number) {
                        currentConnectionNumberFound = true;
                        break;
                    }
                    indexOfNextWidget++;
                }
            }

            if (currentConnectionNumberFound) {
                //remove from next nodes and speeds to next nodes
                temp.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes().remove(indexOfNextWidget);
                temp.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().remove(indexOfNextWidget);
            }
        }

        //delete child shuttles
        for (Widget child : this.getChildren()) {
            if (Scene.globalScene.getMyWidgetsAdded().get(child) != null && ((MyShuttleWidget) Scene.globalScene.getMyWidgetsAdded().get(child)) instanceof MyShuttleWidget) {
                //remove from repository
                MyShuttleWidget shuttleW = (MyShuttleWidget) Scene.globalScene.getMyWidgetsAdded().get(child);
                boolean removed = ShuttleRepository.getInstance().remove(shuttleW.getParentShuttleProperties().getNumber());
//                if(removed)
//                {
//                    Alert.alert("removed" +"shuttle"+"from repo", "removed" +"shuttle"+"from repo", Alert.AlertType.INFORMATION_MESSAGE);
//                }

            }
        }

        //delete the connector from the scene
        if (currentConnectionNumberFound) {
            Scene.getConnectionLayer().removeChild(this);
        }

    }

    /**
     *
     * @param scene the scene container of user widgets
     */
    public void setScene(Scene scene) {
        MyConnectorWidget.scene = scene;
    }

    /**
     *
     * @return the source widget relative to the link
     */
    public Widget getSourceWidget() {
        return sourceWidget;
    }

    /**
     *
     * @param sourceWidget the source widget relative to the link
     */
    public void setSourceWidget(Widget sourceWidget) {
        this.sourceWidget = sourceWidget;
    }

    /**
     *
     * @return the target widget relative to the link
     */
    public Widget getTargetWidget() {
        return targetWidget;
    }

    /**
     *
     * @param targetWidget the target widget relative to the link
     */
    public void setTargetWidget(Widget targetWidget) {
        this.targetWidget = targetWidget;
    }

    /**
     *
     * @return the direction of the source anchor of the link which can be
     * Horizontal or Vertical
     */
    public AnchorFactory.DirectionalAnchorKind getSourceDirection() {
        return sourceDirection;
    }

    /**
     *
     * @param sourceDirection the direction of the source anchor of the link
     * which can be Horizontal or Vertical
     */
    public void setSourceDirection(AnchorFactory.DirectionalAnchorKind sourceDirection) {
        this.sourceDirection = sourceDirection;
    }

    /**
     *
     * @return the direction of the target anchor of the link which can be
     * Horizontal or Vertical
     */
    public AnchorFactory.DirectionalAnchorKind getTargetDirection() {
        return targetDirection;
    }

    /**
     *
     * @param targetDirection the direction of the target anchor of the link
     * which can be Horizontal or Vertical
     */
    public void setTargetDirection(AnchorFactory.DirectionalAnchorKind targetDirection) {
        this.targetDirection = targetDirection;
    }

    /**
     *
     * @return the type of the source node widget that is linked
     */
    public WidgetType getSourceWidgetType() {
        return sourceWidgetType;
    }

    /**
     *
     * @param sourceWidgetType the type of the source node widget that is linked
     */
    public void setSourceWidgetType(WidgetType sourceWidgetType) {
        this.sourceWidgetType = sourceWidgetType;
    }

    /**
     *
     * @return the type of the target node widget that is linked
     */
    public WidgetType getTargetWidgetType() {
        return targetWidgetType;
    }

    /**
     *
     * @param targetWidgetType the type of the target node widget that is linked
     */
    public void setTargetWidgetType(WidgetType targetWidgetType) {
        this.targetWidgetType = targetWidgetType;
    }

    /**
     *
     * @return the instance of the current connector
     */
    public MyConnectorWidget getInstance() {
        return instance;
    }

    /**
     *
     * @param instance the instance of the current connector
     */
    public void setInstance(MyConnectorWidget instance) {
        this.instance = instance;
    }

    /**
     *
     * @return a list of the connectors of the sensor widget
     */
    public List<MyConnectorWidget> getConnections() {
        return connections;
    }

    /**
     *
     * @param connections a list of the connectors of the sensor widget
     */
    public void setConnections(List<MyConnectorWidget> connections) {
        this.connections = connections;
    }

    /**
     *
     * @return if true the link is used from an adapter source to link to
     * another link
     */
    public boolean isIsAdapterLink() {
        return isAdapterLink;
    }

    /**
     *
     * @param isAdapterLink if true the link is used from an adapter source to
     * link to another link
     */
    public void setIsAdapterLink(boolean isAdapterLink) {
        this.isAdapterLink = isAdapterLink;
    }

    /**
     *
     * @return the properties of the connector
     */
    public ConnectorInfo getMyConnectorInfo() {
        return myConnectorInfo;
    }

    /**
     *
     * @param myConnectorInfo the properties of the connector
     */
    public void setMyConnectorInfo(ConnectorInfo myConnectorInfo) {
        this.myConnectorInfo = myConnectorInfo;
    }
}
