package fr.arezzo.designer.DomainWidgets.types4_8_9;

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
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.NodeOperation;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import fr.arezzo.designer.Dialogs.DialogBoxes.DialogBoxList;
import fr.arezzo.designer.Dialogs.DialogOutputMessages.Alert;
import fr.arezzo.designer.Domain.CoordinatesOfArezzo;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType4_8_9;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;

import fr.arezzo.designer.DomainWidgets.MySwitchWidget;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.WidgetType;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.EditeurModule.Repositories.ConnectorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchStopSensorRepository;
import fr.arezzo.designer.Scene.Scene;
import static fr.arezzo.designer.Scene.Scene.N;
import static fr.arezzo.designer.Scene.Scene.getN;
import fr.arezzo.designer.palette.ShapeNode;
import java.util.Arrays;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.LabelWidget;

/**
 * MyStopSensorWidget represents a stop sensor widget node of type 9
 *
 *
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public final class MyStopSensorWidget {

    //scene represents the place that will contain this widget
    public static Scene scene;

    //PropertiesOfNodesOfType4_8_9 represents the properties of the stop sensor (nodes of type 9)
    private PropertiesOfNodesOfType4_8_9 stopSensorProperties;
    //represents the icon that will be placed inside the scene
    private IconNodeWidget widget = null;
    //current instance
    private MyStopSensorWidget instance = null;
    //connections (links) widget
    private List<MyConnectorWidget> connections = new ArrayList<>();
    //parent switch
    private MySwitchWidget switchInstance = null;
    //the widet editor action to edit the name of the widget on the scene
    public WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
    

    /**
     * constructor which is initialized using a reference to the scene, and the
     * shape of the widget that is the icon inside the palette the role of the
     * constructor is to create the stop sensor widget inside the scene and add
     * to it its actions (moving action, hover action, properties menu, delete
     * menu
     *
     * @param scene the scene that will contain the widget
     * @param n the shape of the node
     */
    public MyStopSensorWidget(Scene scene, ShapeNode n) {
        instance = MyStopSensorWidget.this;
        //initialize the domain class which contain the properties of the widget
        stopSensorProperties = new PropertiesOfNodesOfType4_8_9(scene);
        //initialize the number of the widget (ID)
        stopSensorProperties.setNumber(WidgetCommonInfo.getNumberOfNextWidget());
        stopSensorProperties.setID(stopSensorProperties.getNumber());
        //initialize the type of the widget
        stopSensorProperties.setType(Integer.parseInt(n.getShape().getType()));
        //initialize our instance of the scene for direct access
        MyStopSensorWidget.scene = scene;
        //initialize the widget which is a genereic IconWidget that will be added to the scene
        widget = new IconNodeWidget(scene);
        //initialize the widget image using the shape image (from the palette)
        widget.setImage(ImageUtilities.loadImage(n.getShape().getImage()));
        //initialize the label with the number of the widget
        widget.setLabel(stopSensorProperties.getNumber() + "");
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
                //localLocation = Scene.globalScene.convertSceneToView(localLocation);
                final float x = localLocation.getLocation().x;
                final float y = localLocation.getLocation().y;
                //adding the properties menu item to the popup menu of the widget
                JMenuItem propsMenu = new JMenuItem("Proprietes");
                //verify the type of the widget 
                if (N.getShape().getType().equals("9")) {
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
                                    stopSensorProperties.setxCoordinate( x);
                                    stopSensorProperties.setyCoordinate( y);

                                    //update to new Arezzo coordinates system only if changed location
                                    Point arezzoCoordinates = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
                                    if (x != (float) arezzoCoordinates.getX()) {
                                        stopSensorProperties.setxCoordinateArezzo((float) arezzoCoordinates.getX());
                                    }
                                    if (y != (float) arezzoCoordinates.getY()) {
                                        stopSensorProperties.setyCoordinateArezzo((float) arezzoCoordinates.getY());
                                    }
                                    //create the properties editor sheet
                                    Sheet sheet = super.createSheet();
                                    //create a set that the properties editor will contain
                                    Sheet.Set set = Sheet.createPropertiesSet();
                                    //we fill the sheet with all of the properties of the domain class of the widget
                                    //since the widget is a type 9 node, its properties will contain all of the
                                    //attributes of the domain class "PropertiesOfNodesOfType4_8_9" properties 
                                    set.put(new PropertiesOfNodesOfType4_8_9.NumberProperty(stopSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.XCoordinateProperty(stopSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.YCoordinateProperty(stopSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.XCoordinateArezzoProperty(stopSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.YCoordinateArezzoProperty(stopSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.TypeProperty(stopSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.AipNumberProperty(stopSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.SpeedsToNextNodesProperty(stopSensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.NumbersOfNextNodesProperty(stopSensorProperties));

                                    //speed to next nodes one by one to edit
                                    int nextNodeIndex = 0;
                                    if (MyStopSensorWidget.this.getStopSensorProperties().getNumbersOfNextNodes() != null
                                            && MyStopSensorWidget.this.getStopSensorProperties().getNumbersOfNextNodes().size() > 0) {
                                        for (Integer nextNodeNumber : MyStopSensorWidget.this.getStopSensorProperties().getNumbersOfNextNodes()) {
                                            //NextNodeSpeedProperty
                                            set.put(new PropertiesOfNodesOfType4_8_9.NextNodeSpeedProperty(getStopSensorProperties(),
                                                    nextNodeNumber, nextNodeIndex));
                                            nextNodeIndex++;
                                        }
                                    }

                                    //switch
                                    if (MyStopSensorWidget.this.getStopSensorProperties().getParentSwitch() != null) {
                                        set.put(new PropertiesOfNodesOfType4_8_9.SwitchProperty(stopSensorProperties));
                                        //first stop sensor in switch?
                                        if (MyStopSensorWidget.this.getStopSensorProperties().getIsFirstInParentSwitch()) {
                                            set.put(new PropertiesOfNodesOfType4_8_9.isFirstStopSensorInSwitchProperty(stopSensorProperties));
                                        } //second stop sensor
                                        else {
                                            set.put(new PropertiesOfNodesOfType4_8_9.isSecondStopSensorInSwitchProperty(stopSensorProperties));
                                        }
                                    }

                                    sheet.put(set);

                                    return sheet;

                                }
                            };
                            //add labels for the properties editor of the widget
                            node.setDisplayName("Properties of the Stop Sensor");
                            node.setShortDescription("Properties of the Stop Sensor");
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

                ActionListener cutActionListener = new MyStopSensorWidget.PopupCutActionListener(WidgetCommonInfo.WidgetType.STOP_SENSOR, instance, N);
                cutMenuItem.addActionListener(cutActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                JMenuItem copyMenuItem = new JMenuItem("Copy");
                copyMenuItem.setActionCommand((String) TransferHandler.getCopyAction().
                        getValue(Action.NAME));

                ActionListener copyActionListener = new MyStopSensorWidget.PopupCopyActionListener(WidgetCommonInfo.WidgetType.STOP_SENSOR, instance, N);
                copyMenuItem.addActionListener(copyActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                if (N.getShape().getType().equals("9")) {
                    popup.add(propsMenu);
                    popup.addSeparator();
                    popup.add(cutMenuItem);
                    popup.add(copyMenuItem);
                }
                //return the popup menu
                return popup;
            }
        }));

        //show/hide parent switch for user
        widget.getActions().addAction(ActionFactory.createSelectAction(new SelectProvider() {

            @Override
            public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
                return true;
            }

            @Override
            public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
                return true;
            }

            @Override
            public void select(Widget widget, Point point, boolean bln) {
                //if we have a parent switch => can show or hide childrens of the switch
                if (MyStopSensorWidget.this.getSwitchInstance() != null) {
                    MySwitchWidget.hideShowAllChildrensOfSwitch(getSwitchInstance().getNumber());
                }
            }
        }, false));
        //show which switch it is assigned to
        //switchInstance = new MySwitchWidget();
        //widget.addChild(switchInstance.getWidget(),switchInstance );

    }

    /**
     *
     * @param scene the scene containing the widget
     * @param n the shape of the node
     * @param copiedOrCuttedWidget the copied or cut stop sensor widget
     */
    public MyStopSensorWidget(Scene scene, ShapeNode n, MyStopSensorWidget copiedOrCuttedWidget) {
        this(scene, n);
        //copied properties
        PropertiesOfNodesOfType4_8_9 copiedProperties = copiedOrCuttedWidget.getStopSensorProperties();
        //copy values
        getStopSensorProperties().setType(copiedProperties.getType());
    }

    /**
     * helper class for beginning the linking process from a stop sensor widget
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
            widgetPointLocation = MyStopSensorWidget.this.getStopSensorProperties().getLocation();
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
        private MyStopSensorWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         *
         * @param type the type of the widget
         * @param mySsw the stop sensor widget to copy
         * @param shapeNode the shape of the stop sensor node
         */
        public PopupCopyActionListener(WidgetType type, MyStopSensorWidget mySsw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = mySsw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Stop Sensor")) {
                shapeNode.getShape().setName("Copy of Stop Sensor " + shapeNode.getShape().getName());
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
            if (widgetType == WidgetType.STOP_SENSOR) {
                //copy the widget to the scene
                ccpWidget = MyStopSensorWidget.this;
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
     * helper class that manages cutting a stop sensor widget
     *
     */
    public class PopupCutActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MyStopSensorWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         *
         * @param type the type of the node
         * @param mySsw the stop sensor that has been cut
         * @param shapeNode the shape of the node
         */
        public PopupCutActionListener(WidgetType type, MyStopSensorWidget mySsw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = mySsw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Stop Sensor")) {
                shapeNode.getShape().setName("Copy of Stop Sensor " + shapeNode.getShape().getName());
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
            if (widgetType == WidgetType.STOP_SENSOR) {
                //copy it to the scene
                ccpWidget = MyStopSensorWidget.this;
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
     * helper class that manages deleting a stop sensor widget
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
            //delete from current repository
            SwitchStopSensorRepository.getInstance().remove(MyStopSensorWidget.this.getStopSensorProperties().getNumber());
            //update parent switch and remove from parent switch
            if (switchInstance != null) {
                MySwitchWidget sw = SwitchRepository.getInstance().find(switchInstance.getNumber());
                if (MyStopSensorWidget.this.getStopSensorProperties().getIsFirstInParentSwitch()) {
                    sw.setFirstEndStopSensorNode(null);
                    //delete if the switch became empty (zombie switch)
                    SwitchRepository.getInstance().IfUnusedSwitch_DeleteSwitch(switchInstance.getNumber());
                } else {
                    sw.setSecondEndStopSensorNode(null);
                    //delete if the switch became empty (zombie switch)
                    SwitchRepository.getInstance().IfUnusedSwitch_DeleteSwitch(switchInstance.getNumber());
                }
            }

            //remove all of the connections related to this widget from the scene first
            for (MyConnectorWidget connection : getConnections()) {
                connection.deleteNextNodeOfSourceAndLink();
                //remove connection from repository
                ConnectorRepository.getInstance().remove(connection.getMyConnectorInfo().getConnectorProperties().getNumber());
            }
            //then remove the node of the widget from the scene
            scene.removeNode(getN());
        }
    }

    /**
     * begin to configure the switch of this switch sub component
     *
     */
    public void initializeParentSwitch() {
        //call another thread to wait for user input while the dialog is shown
        //we need this thread so that the GUI is not blocked
        new AsynchrnousDialogBoxListResultConsumer().start();
    }

    /**
     * a thread used to configure the new switch of the output sensor
     *
     */
    private class AsynchrnousDialogBoxListResultConsumer extends Thread {

        /**
         * this method is called when the thread starts
         *
         */
        @Override
        public void run() {
            //initialize dialog that will ask the user to chose from different radio buttons
            DialogBoxList<String> dialog ;
            //initialize the waiting queue to -1 to know if the field has been updated from the dialog or not
            stopSensorProperties.setParentSwitch(-1);
            //instanciate our possible choices that the user will chose from
            List<String> possibilities = new ArrayList<>();
            possibilities.add("Add New Switch");
            List<MySwitchWidget> allSwitchs = SwitchRepository.getInstance().getAll();
            for (MySwitchWidget mySw : allSwitchs) {
                //show only switch that has an empty slot for a stop sensor
                if (mySw.getFirstEndStopSensorNode() == null || mySw.getSecondEndStopSensorNode() == null) {
                    possibilities.add(mySw.getNumber() + "");
                }
            }
            //initialize the answers that we want for the preceding choices
            List<String> answers = new ArrayList<>();
            answers.add("99999");
            for (MySwitchWidget mySw : allSwitchs) {
                //show only switch that has an empty slot for a stop sensor
                if (mySw.getFirstEndStopSensorNode() == null || mySw.getSecondEndStopSensorNode() == null) {
                    answers.add(mySw.getNumber() + "");
                }

            }

            //instanciate the dialog using the title of he window, the description,the choices for the user, the answer related
            //to these choices (the data that we need in return)
            dialog = new DialogBoxList<>("Parent Switch Selection", "Please select the switch?",
                    possibilities, answers, DialogBoxList.MessageType.INFORMATION);

            //while the user did not change the field; he did not make a choice
            //while(stopSensorProperties.getParentSwitch() == -1)
            //{
            try {

                //read if there is an answer from the dialog
                //until the user releases the lock of the semaphore
                //when that happens, we have the choice data of the user
                String result = "";
                while (result == null || result.isEmpty()) {

                    result =  dialog.showDialogAndGetResult();
                }

                //block waiting for the user choice
                MyWorkstationWidget.semaphore.acquire();
                MyWorkstationWidget.mutex.acquire();
//                    if(result.equals("0"))
//                    {
//                        stopSensorProperties.setParentSwitch(0);
//                    }
                //add new switch
                if (result.equals("99999")) {
                    //show which switch it is assigned to
                    //add the switch to this widget
                    switchInstance = new MySwitchWidget();

                    //first or second node dialog
                    DialogBoxList<String> dialogFirstSecond ;

                    List<String> possibilitiesFirstOrSecondNode = new ArrayList<>();
                    possibilitiesFirstOrSecondNode.add("First End Stop Sensor in the Switch");
                    possibilitiesFirstOrSecondNode.add("Second End Stop Sensor in the Switch");

                    //initialize the answers that we want for the preceding choices
                    List<String> answersFirstOrSecondNode = new ArrayList<>();
                    answersFirstOrSecondNode.add("First");
                    answersFirstOrSecondNode.add("Second");

                    //instanciate the dialog using the title of the window, the description,the choices for the user, the answer related
                    //to these choices (the data that we need in return)
                    dialogFirstSecond = new DialogBoxList<>("Stop Sensor rank in the Switch", "Please select the rank of the Stop Sensor in the switch?",
                            possibilitiesFirstOrSecondNode, answersFirstOrSecondNode, DialogBoxList.MessageType.QUESTION);

                    String resultFiestOrSecond = "";
                    while (resultFiestOrSecond == null || resultFiestOrSecond.isEmpty()) {
                        resultFiestOrSecond =  dialogFirstSecond.showDialogAndGetResult();
                    }

                    stopSensorProperties.setParentSwitch(switchInstance.getNumber());
                    switch (resultFiestOrSecond) {
                        case "First":
                            MyStopSensorWidget.this.getStopSensorProperties().setIsFirstInParentSwitch(true);
                            //assign the first endstop sensor
                            switchInstance.setFirstEndStopSensorNode(MyStopSensorWidget.this);
                            break;
                        case "Second":
                            MyStopSensorWidget.this.getStopSensorProperties().setIsSecondInParentSwitch(true);
                            //assign the first endstop sensor
                            switchInstance.setSecondEndStopSensorNode(MyStopSensorWidget.this);
                            break;
                    }

                    //show that this stop sensor widget is affected to a switch
                    widget.addChild(switchInstance.getWidget(), switchInstance);

                    Scene.globalScene.validate();
                    Scene.globalScene.repaint();
                    //add the switch to the switch repository
                    SwitchRepository.getInstance().add(switchInstance);

                } //select empty slot from existing switch
                else {
                    //show which switch it is assigned to
                    //add the switch to this widget
                    Integer numberOfTheselectedSwitch = Integer.parseInt(result);
                        //switchInstance = new MySwitchWidget(numberOfTheselectedSwitch);
                    //copy properties of selected switch

                    //get the selected switch
                    switchInstance = SwitchRepository.getInstance().find(numberOfTheselectedSwitch);
                    //update the second end stop sensor
//                         if(switchInstance.getSecondEndStopSensorNode() == null && switchInstance.getFirstEndStopSensorNode() != null)
//                         {
//                             //if 1st end sensor of this switch is initialized, initialize the second one
//                             switchInstance.setSecondEndStopSensorNode(MyStopSensorWidget.this);
//                         }
                    //first or second node dialog
                    DialogBoxList<String> dialogFirstSecond ;

                    List<String> possibilitiesFirstOrSecondNode = new ArrayList<>();
                    //initialize the answers that we want for the preceding choices
                    List<String> answersFirstOrSecondNode = new ArrayList<>();
                    if (switchInstance.getFirstEndStopSensorNode() == null && switchInstance.getSecondEndStopSensorNode() == null) {
                        //what to show
                        possibilitiesFirstOrSecondNode.add("First End Stop Sensor in the Switch");
                        possibilitiesFirstOrSecondNode.add("Second End Stop Sensor in the Switch");
                        //what to return to the program
                        answersFirstOrSecondNode.add("First");
                        answersFirstOrSecondNode.add("Second");
                    } else if (switchInstance.getFirstEndStopSensorNode() == null) {
                        possibilitiesFirstOrSecondNode.add("First End Stop Sensor in the Switch");
                        answersFirstOrSecondNode.add("First");
                    } else {
                        possibilitiesFirstOrSecondNode.add("Second End Stop Sensor in the Switch");
                        answersFirstOrSecondNode.add("Second");
                    }

                    //instanciate the dialog using the title of the window, the description,the choices for the user, the answer related
                    //to these choices (the data that we need in return)
                    dialogFirstSecond = new DialogBoxList<>("Stop Sensor rank in the Switch", "Please select the rank of the Stop Sensor in the switch?",
                            possibilitiesFirstOrSecondNode, answersFirstOrSecondNode, DialogBoxList.MessageType.QUESTION);

                    String resultFiestOrSecond = "";
                    while (resultFiestOrSecond == null || resultFiestOrSecond.isEmpty()) {
                        resultFiestOrSecond =  dialogFirstSecond.showDialogAndGetResult();
                    }

                    stopSensorProperties.setParentSwitch(switchInstance.getNumber());
                    switch (resultFiestOrSecond) {
                        case "First":
                            MyStopSensorWidget.this.getStopSensorProperties().setIsFirstInParentSwitch(true);
                            //assign the first endstop sensor
                            switchInstance.setFirstEndStopSensorNode(MyStopSensorWidget.this);
                            break;
                        case "Second":
                            MyStopSensorWidget.this.getStopSensorProperties().setIsSecondInParentSwitch(true);
                            //assign the first endstop sensor
                            switchInstance.setSecondEndStopSensorNode(MyStopSensorWidget.this);
                            break;
                    }

//                         
                    IconNodeWidget newWidget;
                    //initialize the NEW widget which is a genereic IconWidget that will be added to the scene
                    newWidget = new IconNodeWidget(Scene.globalScene);
                    //initialize the widget image using the shape image (from the palette)
                    newWidget.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/switch_thumbnail.png"));
                    //initialize the label of the widget
                    newWidget.setLabel(numberOfTheselectedSwitch + "");
                    switchInstance.setWidget(newWidget);
                    widget.addChild(newWidget, switchInstance);
                         //setConstraint (widget, 

                    //widget.setLayout(LayoutFactory.createCardLayout(widget));
                    Scene.globalScene.validate();
                    Scene.globalScene.repaint();
                    //add the switch to the switch repository
                    SwitchRepository.getInstance().update(switchInstance.getNumber(), switchInstance);
                    //stopSensorProperties.setParentSwitch(switchInstance.getNumber());

                }
                //release the lock
                MyWorkstationWidget.mutex.release();
                //stop the block waiting
                MyWorkstationWidget.semaphore.release();
                try {
                    //sleep this thread so that we dnot waste too much CPU processing in this loop 
                    sleep(50);
                } catch (InterruptedException ex) {

                }

                //stop this thread and quit
                //stop();
            } catch (InterruptedException | NumberFormatException ex) {
                Alert.alert(ex.getMessage(), ex.getCause() + "" + "\n" + Arrays.toString(ex.getStackTrace()), Alert.AlertType.ERROR_MESSAGE);
            }

        }
        //}

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
            
            return MyStopSensorWidget.this.getStopSensorProperties().getNumber()+"";
            
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
                MyStopSensorWidget.this.getStopSensorProperties().setNumber(newNumber);
                if(newNumber instanceof Integer)
                {
                    ((LabelWidget) widget).setLabel(newNumber+"");
                    for(MyConnectorWidget conn : MyStopSensorWidget.this.getConnections())
                    {
                        //Alert.alert("ok", "number conn: " + conn.getMyConnectorInfo().getConnectorProperties().getNumber(), Alert.AlertType.ERROR_MESSAGE);
                        Integer IdOfTargetWidget = MyStopSensorWidget.this.getStopSensorProperties().getID();
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
        MyStopSensorWidget.scene = scene;
    }

    /**
     *
     * @return the properties of the stop sensor node
     */
    public PropertiesOfNodesOfType4_8_9 getStopSensorProperties() {
        return stopSensorProperties;
    }

    /**
     *
     * @param stopSensorProperties the properties of the stop sensor node
     */
    public void setStopSensorProperties(PropertiesOfNodesOfType4_8_9 stopSensorProperties) {
        this.stopSensorProperties = stopSensorProperties;
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
     * @param widget the icon of the widget
     */
    public void setWidget(IconNodeWidget widget) {
        this.widget = widget;
    }

    /**
     *
     * @return the current instance of the stop sensor node
     */
    public MyStopSensorWidget getInstance() {
        return instance;
    }

    /**
     *
     * @param instance the current instance of the stop sensor node
     */
    public void setInstance(MyStopSensorWidget instance) {
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
     * @return the parent switch widget of this switch sub component
     */
    public MySwitchWidget getSwitchInstance() {
        return switchInstance;
    }

    /**
     *
     * @param switchInstance the parent switch widget of this switch sub
     * component
     */
    public void setSwitchInstance(MySwitchWidget switchInstance) {
        this.switchInstance = switchInstance;
    }

}
