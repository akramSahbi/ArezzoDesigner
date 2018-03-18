package fr.arezzo.designer.DomainWidgets.types4_8_9;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import static java.lang.Thread.sleep;
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
import fr.arezzo.designer.EditeurModule.Repositories.SwitchSensorRepository;
import fr.arezzo.designer.Scene.Scene;
import static fr.arezzo.designer.Scene.Scene.N;
import static fr.arezzo.designer.Scene.Scene.getN;
import fr.arezzo.designer.palette.ShapeNode;
import java.util.Arrays;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.LabelWidget;

/**
 * MySensorWidget represents a sensor widget node of type 8
 *
 *
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public final class MySensorWidget {

    //scene represents the place that will contain this widget
    public static Scene scene;

    //PropertiesOfNodesOfType4_8_9 represents the properties of the sensor (nodes of type 8)
    private PropertiesOfNodesOfType4_8_9 sensorProperties;
    //represents the icon that will be placed inside the scene
    private IconNodeWidget widget = null;
    //current instance
    private MySensorWidget instance = null;
    //connections (links) widget
    private List<MyConnectorWidget> connections = new ArrayList<>();
    //parent switch
    private MySwitchWidget switchInstance = null;
    //the widet editor action to edit the name of the widget on the scene
    public WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
    

    /**
     * constructor which is initialized using a reference to the scene, and the
     * shape of the widget that is the icon inside the palette the role of the
     * constructor is to create the sensor widget inside the scene and add to it
     * its actions (moving action, hover action, properties menu, delete menu
     *
     * @param scene the scene that will contain the widget
     * @param n the shape of the node
     */
    public MySensorWidget(Scene scene, ShapeNode n) {
        instance = MySensorWidget.this;
        //initialize the domain class which contain the properties of the widget
        sensorProperties = new PropertiesOfNodesOfType4_8_9(scene);
        //initialize the number of the widget (ID)
        sensorProperties.setNumber(WidgetCommonInfo.getNumberOfNextWidget());
        sensorProperties.setID(sensorProperties.getNumber());
        //initialize the type of the widget
        sensorProperties.setType(Integer.parseInt(n.getShape().getType()));
        //initialize our instance of the scene for direct access
        MySensorWidget.scene = scene;
        //initialize the widget which is a genereic IconWidget that will be added to the scene
        widget = new IconNodeWidget(scene);
        //initialize the widget image using the shape image (from the palette)
        widget.setImage(ImageUtilities.loadImage(n.getShape().getImage()));
        //initialize the label with the number of the widget
        widget.setLabel(sensorProperties.getNumber() + "");
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
                if (N.getShape().getType().equals("8")) {
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
                                    sensorProperties.setxCoordinate(x);
                                    sensorProperties.setyCoordinate(y);
                                    //update to new Arezzo coordinates system only if changed location
                                    Point arezzoCoordinates = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
                                    if (x != (float) arezzoCoordinates.getX()) {
                                        sensorProperties.setxCoordinateArezzo((float) arezzoCoordinates.getX());
                                    }
                                    if (y != (float) arezzoCoordinates.getY()) {
                                        sensorProperties.setyCoordinateArezzo((float) arezzoCoordinates.getY());
                                    }

                                    //create the properties editor sheet
                                    Sheet sheet = super.createSheet();
                                    //create a set that the properties editor will contain
                                    Sheet.Set set = Sheet.createPropertiesSet();
                                    //we fill the sheet with all of the properties of the domain class of the widget
                                    //since the widget is a type 8 node, its properties will contain all of the
                                    //attributes of the domain class "PropertiesOfNodesOfType4_8_9" properties 
                                    set.put(new PropertiesOfNodesOfType4_8_9.NumberProperty(sensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.XCoordinateProperty(sensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.YCoordinateProperty(sensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.XCoordinateArezzoProperty(sensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.YCoordinateArezzoProperty(sensorProperties));

                                    set.put(new PropertiesOfNodesOfType4_8_9.TypeProperty(sensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.AipNumberProperty(sensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.SpeedsToNextNodesProperty(sensorProperties));
                                    set.put(new PropertiesOfNodesOfType4_8_9.NumbersOfNextNodesProperty(sensorProperties));
                                    //speed to next nodes one by one to edit
                                    int nextNodeIndex = 0;
                                    if (MySensorWidget.this.getSensorProperties().getNumbersOfNextNodes() != null
                                            && MySensorWidget.this.getSensorProperties().getNumbersOfNextNodes().size() > 0) {
                                        for (Integer nextNodeNumber : MySensorWidget.this.getSensorProperties().getNumbersOfNextNodes()) {
                                            //NextNodeSpeedProperty
                                            set.put(new PropertiesOfNodesOfType4_8_9.NextNodeSpeedProperty(getSensorProperties(),
                                                    nextNodeNumber, nextNodeIndex));
                                            nextNodeIndex++;
                                        }
                                    }

                                    //for switch properties
                                    if (MySensorWidget.this.getSwitchInstance() != null) {
                                        set.put(new PropertiesOfNodesOfType4_8_9.SwitchProperty(sensorProperties));
                                        //first node of type output sensor in switch?
                                        if (MySensorWidget.this.getSensorProperties().getIsFirstSensorNodeInParentSwitch()) {
                                            //first node of type output sensor in switch?
                                            set.put(new PropertiesOfNodesOfType4_8_9.isFirstSensorNodeInParentSwitchProperty(sensorProperties));
                                        } //second node of type output sensor in switch?
                                        else {
                                            //second node of type output sensor in switch?
                                            set.put(new PropertiesOfNodesOfType4_8_9.isSecondSensorNodeInParentSwitchProperty(sensorProperties));
                                        }

                                    }

                                    sheet.put(set);

                                    return sheet;

                                }
                            };
                            //add labels for the properties editor of the widget
                            node.setDisplayName("Properties of the Sensor");
                            node.setShortDescription("Properties of Sensor");
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

                ActionListener cutActionListener = new MySensorWidget.PopupCutActionListener(WidgetCommonInfo.WidgetType.SENSOR, instance, N);
                cutMenuItem.addActionListener(cutActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                JMenuItem copyMenuItem = new JMenuItem("Copy");
                copyMenuItem.setActionCommand((String) TransferHandler.getCopyAction().
                        getValue(Action.NAME));

                ActionListener copyActionListener = new MySensorWidget.PopupCopyActionListener(WidgetCommonInfo.WidgetType.SENSOR, instance, N);
                copyMenuItem.addActionListener(copyActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                if (N.getShape().getType().equals("8")) {
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
                //if has a parent switch
                if (MySensorWidget.this.getSwitchInstance() != null) {
                    MySwitchWidget.hideShowAllChildrensOfSwitch(getSwitchInstance().getNumber());
                }

            }
        }, false));

    }

    /**
     * constructor
     *
     * @param scene the scene containing the widget
     * @param n the shape of the node
     * @param copiedOrCuttedWidget the copied or cut sensor widget
     */
    public MySensorWidget(Scene scene, ShapeNode n, MySensorWidget copiedOrCuttedWidget) {
        this(scene, n);
        //copied properties
        PropertiesOfNodesOfType4_8_9 copiedProperties = copiedOrCuttedWidget.getSensorProperties();
        //copy values
        getSensorProperties().setType(copiedProperties.getType());
    }

    /**
     * helper class for beginning the linking process
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
            widgetPointLocation = MySensorWidget.this.getSensorProperties().getLocation();
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
     * helper class for managing the copying of a sensor widget
     *
     */
    public class PopupCopyActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MySensorWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         *
         * @param type the type of the widget
         * @param mySw the sensor widget to copy
         * @param shapeNode the shape of the sensor node
         */
        public PopupCopyActionListener(WidgetType type, MySensorWidget mySw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = mySw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Sensor")) {
                shapeNode.getShape().setName("Copy of Sensor " + shapeNode.getShape().getName());
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
            if (widgetType == WidgetType.SENSOR) {
                //copy the widget to the scene
                ccpWidget = MySensorWidget.this;
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
     * helper class that manages cutting a sensor widget
     *
     */
    public class PopupCutActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MySensorWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         *
         * @param type the type of the node
         * @param mySw the sensor that has been cut
         * @param shapeNode the shape of the node
         */
        public PopupCutActionListener(WidgetType type, MySensorWidget mySw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = mySw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Sensor")) {
                shapeNode.getShape().setName("Copy of Sensor " + shapeNode.getShape().getName());
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
            if (widgetType == WidgetType.SENSOR) {
                //copy it to the scene
                ccpWidget = MySensorWidget.this;
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
            DialogBoxList<String> dialog;
            //initialize the waiting queue to -1 to know if the field has been updated from the dialog or not
            sensorProperties.setParentSwitch(-1);
            //instanciate our possible choices that the user will chose from
            List<String> possibilities = new ArrayList<>();
            possibilities.add("Add New Switch");
            List<MySwitchWidget> allSwitchs = SwitchRepository.getInstance().getAll();
            for (MySwitchWidget mySw : allSwitchs) {
                //show only switch that has an empty slot for a stop sensor
                if (mySw.getFirstSensorOutputNode() == null || mySw.getSecondSensorOutputNode() == null) {
                    possibilities.add(mySw.getNumber() + "");
                }
            }
            //initialize the answers that we want for the preceding choices
            List<String> answers = new ArrayList<>();
            answers.add("99999");
            for (MySwitchWidget mySw : allSwitchs) {
                //show only switch that has an empty slot for a stop sensor
                if (mySw.getFirstSensorOutputNode() == null || mySw.getSecondSensorOutputNode() == null) {
                    answers.add(mySw.getNumber() + "");
                }

            }

            //instanciate the dialog using the title of he window, the description,the choices for the user, the answer related
            //to these choices (the data that we need in return)
            dialog = new DialogBoxList<>("Parent Switch Selection", "Please select the switch?",
                    possibilities, answers, DialogBoxList.MessageType.INFORMATION);

            //while the user did not change the field; he did not make a choice
            //while(sensorProperties.getParentSwitch() == -1)
            //{
            try {

                //read if there is an answer from the dialog
                //until the user releases the lock of the semaphore
                //when that happens, we have the choice data of the user
                String result = "";
                while (result == null || result.isEmpty()) {

                    result = dialog.showDialogAndGetResult();
                }

                //block waiting for the user choice
                MyWorkstationWidget.semaphore.acquire();
                MyWorkstationWidget.mutex.acquire();
//                    if(result.equals("0"))
//                    {
//                        sensorProperties.setParentSwitch(0);
//                    }
                //add new switch
                if (result.equals("99999")) {
                    //show which switch it is assigned to
                    //add the switch to this widget
                    switchInstance = new MySwitchWidget();

                    //first or second node dialog
                    DialogBoxList<String> dialogFirstSecond;

                    List<String> possibilitiesFirstOrSecondNode = new ArrayList<>();
                    possibilitiesFirstOrSecondNode.add("First Output Sensor in the Switch");
                    possibilitiesFirstOrSecondNode.add("Second Output Sensor in the Switch");

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

                    sensorProperties.setParentSwitch(switchInstance.getNumber());
                    switch (resultFiestOrSecond) {
                        case "First":
                            MySensorWidget.this.getSensorProperties().setIsFirstSensorNodeInParentSwitch(true);
                            //assign the first output sensor
                            switchInstance.setFirstSensorOutputNode(MySensorWidget.this);
                            break;
                        case "Second":
                            MySensorWidget.this.getSensorProperties().setIsSecondSensorNodeInParentSwitch(true);
                            //assign the first output sensor
                            switchInstance.setSecondSensorOutputNode(MySensorWidget.this);
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
//                         if(switchInstance.getSecondSensorOutputNode() == null && switchInstance.getFirstSensorOutputNode() != null)
//                         {
//                             //if 1st end sensor of this switch is initialized, initialize the second one
//                             switchInstance.setSecondSensorOutputNode(MySensorWidget.this);
//                         }
                    //first or second node dialog
                    DialogBoxList<String> dialogFirstSecond ;

                    List<String> possibilitiesFirstOrSecondNode = new ArrayList<>();
                    //initialize the answers that we want for the preceding choices
                    List<String> answersFirstOrSecondNode = new ArrayList<>();
                    if (switchInstance.getFirstSensorOutputNode() == null && switchInstance.getSecondSensorOutputNode() == null) {
                        //what to show
                        possibilitiesFirstOrSecondNode.add("First Output Sensor in the Switch");
                        possibilitiesFirstOrSecondNode.add("Second Output Sensor in the Switch");
                        //what to return to the program
                        answersFirstOrSecondNode.add("First");
                        answersFirstOrSecondNode.add("Second");
                    } else if (switchInstance.getFirstSensorOutputNode() == null) {
                        possibilitiesFirstOrSecondNode.add("First Output Sensor in the Switch");
                        answersFirstOrSecondNode.add("First");
                    } else {
                        possibilitiesFirstOrSecondNode.add("Second Output Sensor in the Switch");
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

                    sensorProperties.setParentSwitch(switchInstance.getNumber());
                    switch (resultFiestOrSecond) {
                        case "First":
                            MySensorWidget.this.getSensorProperties().setIsFirstSensorNodeInParentSwitch(true);
                            //assign the first output sensor
                            switchInstance.setFirstSensorOutputNode(MySensorWidget.this);
                            break;
                        case "Second":
                            MySensorWidget.this.getSensorProperties().setIsSecondSensorNodeInParentSwitch(true);
                            //assign the first output sensor
                            switchInstance.setSecondSensorOutputNode(MySensorWidget.this);
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
                    //sensorProperties.setParentSwitch(switchInstance.getNumber());

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
     * helper class that manages deleting a sensor widget
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
            SwitchSensorRepository.getInstance().remove(MySensorWidget.this.getSensorProperties().getNumber());
            //update parent switch and remove from parent switch
            if (switchInstance != null) {
                MySwitchWidget sw = SwitchRepository.getInstance().find(switchInstance.getNumber());
                if (MySensorWidget.this.getSensorProperties().getIsFirstSensorNodeInParentSwitch()) {
                    sw.setFirstSensorOutputNode(null);
                    //delete if the switch became empty (zombie switch)
                    SwitchRepository.getInstance().IfUnusedSwitch_DeleteSwitch(switchInstance.getNumber());
                } else {
                    sw.setSecondSensorOutputNode(null);
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
            
            return MySensorWidget.this.getSensorProperties().getNumber()+"";
            
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
                MySensorWidget.this.getSensorProperties().setNumber(newNumber);
                if(newNumber instanceof Integer)
                {
                    ((LabelWidget) widget).setLabel(newNumber+"");
                    for(MyConnectorWidget conn : MySensorWidget.this.getConnections())
                    {
                        //Alert.alert("ok", "number conn: " + conn.getMyConnectorInfo().getConnectorProperties().getNumber(), Alert.AlertType.ERROR_MESSAGE);
                        Integer IdOfTargetWidget = MySensorWidget.this.getSensorProperties().getID();
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
        MySensorWidget.scene = scene;
    }

    /**
     *
     * @return the properties of the sensor node
     */
    public PropertiesOfNodesOfType4_8_9 getSensorProperties() {
        return sensorProperties;
    }

    /**
     *
     * @param sensorProperties the properties of the sensor node
     */
    public void setSensorProperties(PropertiesOfNodesOfType4_8_9 sensorProperties) {
        this.sensorProperties = sensorProperties;
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
     * @return the current instance of the sensor node
     */
    public MySensorWidget getInstance() {
        return instance;
    }

    /**
     *
     * @param instance the current instance of the sensor node
     */
    public void setInstance(MySensorWidget instance) {
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
     * @return the parent switch widget
     */
    public MySwitchWidget getSwitchInstance() {
        return switchInstance;
    }

    /**
     *
     * @param switchInstance the parent switch widget
     */
    public void setSwitchInstance(MySwitchWidget switchInstance) {
        this.switchInstance = switchInstance;
    }
}
