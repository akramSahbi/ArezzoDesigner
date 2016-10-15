package fr.arezzo.designer.DomainWidgets.types_6_10_11_12;

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
import fr.arezzo.designer.Domain.CoordinatesOfArezzo;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType6_10_11_12;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;

import fr.arezzo.designer.DomainWidgets.MySwitchWidget;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.WidgetType;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.EditeurModule.Repositories.ConnectorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchOutputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchSensorRepository;
import fr.arezzo.designer.Scene.Scene;
import static fr.arezzo.designer.Scene.Scene.N;
import static fr.arezzo.designer.Scene.Scene.getN;
import fr.arezzo.designer.palette.ShapeNode;

/**
 * MySwitchOutputWidget represents a switch output widget node of type 12
 *
 *
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public final class MySwitchOutputWidget {

    //scene represents the place that will contain this widget
    public static Scene scene;
    //PropertiesOfNodesOfType6_10_11_12 represents the properties of the output switchs (nodes of type 10)
    private PropertiesOfNodesOfType6_10_11_12 switchOutputNodeProperties;
    //represents the icon that will be placed inside the scene
    private IconNodeWidget widget = null;
    //current instance
    private MySwitchOutputWidget instance = null;
    //connections (links) widget
    private List<MyConnectorWidget> connections = new ArrayList<>();
    private MySwitchWidget switchInstance;

    /**
     * constructor which is initialized using a reference to the scene, and the
     * shape of the widget that is the icon inside the palette the role of the
     * constructor is to create the switch output widget inside the scene and
     * add to it its actions (moving action, hover action, properties menu,
     * delete menu
     *
     * @param scene is the scene that is the container of the widgets
     * @param n is the shape of the node
     */
    public MySwitchOutputWidget(Scene scene, ShapeNode n) {
        instance = MySwitchOutputWidget.this;
        //initialize the domain class which contain the properties of the widget
        switchOutputNodeProperties = new PropertiesOfNodesOfType6_10_11_12(scene);
        //initialize the number of the widget (ID)
        switchOutputNodeProperties.setNumber(WidgetCommonInfo.getNumberOfNextWidget());
        //initialize the type of the widget
        switchOutputNodeProperties.setType(Integer.parseInt(n.getShape().getType()));
        //initialize our instance of the scene for direct access
        MySwitchOutputWidget.scene = scene;
        //initialize the widget which is a genereic IconWidget that will be added to the scene
        widget = new IconNodeWidget(scene);
        //initialize the widget image using the shape image (from the palette)
        widget.setImage(ImageUtilities.loadImage(n.getShape().getImage()));
        //initialize the label using the number of the widget
        widget.setLabel(switchOutputNodeProperties.getNumber() + "");
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
        widget.getLabelWidget().getActions().addAction(scene.editorAction);
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
                if (N.getShape().getType().equals("12")) {
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
                                    switchOutputNodeProperties.setxCoordinate(x);
                                    switchOutputNodeProperties.setyCoordinate(y);
                                    //update to new Arezzo coordinates system only if changed location
                                    Point arezzoCoordinates = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
                                    if (x != (float) arezzoCoordinates.getX()) {
                                        switchOutputNodeProperties.setxCoordinateArezzo((float) arezzoCoordinates.getX());
                                    }
                                    if (y != (float) arezzoCoordinates.getY()) {
                                        switchOutputNodeProperties.setyCoordinateArezzo((float) arezzoCoordinates.getY());
                                    }
                                    //create the properties editor sheet
                                    Sheet sheet = super.createSheet();
                                    //create a set that the properties editor will contain
                                    Sheet.Set set = Sheet.createPropertiesSet();
                                    //we fill the sheet with all of the properties of the domain class of the widget
                                    //since the widget is a type 12 node, its properties will contain all of the
                                    //attributes of the domain class "PropertiesOfNodesOfType6_10_11_12" properties 
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.NumberProperty(switchOutputNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.XCoordinateProperty(switchOutputNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.YCoordinateProperty(switchOutputNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.XCoordinateArezzoProperty(switchOutputNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.YCoordinateArezzoProperty(switchOutputNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.TypeProperty(switchOutputNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.SpeedsToNextNodesProperty(switchOutputNodeProperties));
                                    set.put(new PropertiesOfNodesOfType6_10_11_12.NumbersOfNextNodesProperty(switchOutputNodeProperties));
                                    //speed to next nodes one by one to edit
                                    int nextNodeIndex = 0;
                                    if (MySwitchOutputWidget.this.getSwitchOutputNodeProperties().getNumbersOfNextNodes() != null
                                            && MySwitchOutputWidget.this.getSwitchOutputNodeProperties().getNumbersOfNextNodes().size() > 0) {
                                        for (Integer nextNodeNumber : MySwitchOutputWidget.this.getSwitchOutputNodeProperties().getNumbersOfNextNodes()) {
                                            //NextNodeSpeedProperty
                                            set.put(new PropertiesOfNodesOfType6_10_11_12.NextNodeSpeedProperty(getSwitchOutputNodeProperties(),
                                                    nextNodeNumber, nextNodeIndex));
                                            nextNodeIndex++;
                                        }
                                    }
                                    //switch
                                    if (MySwitchOutputWidget.this.getSwitchOutputNodeProperties().getParentSwitch() != null) {
                                        set.put(new PropertiesOfNodesOfType6_10_11_12.SwitchProperty(switchOutputNodeProperties));
                                        //first output?
                                        if (MySwitchOutputWidget.this.getSwitchOutputNodeProperties().getIsFirstOutputNodeInParentSwitch()) {
                                            set.put(new PropertiesOfNodesOfType6_10_11_12.isFirstOutputNodeInParentSwitchProperty(switchOutputNodeProperties));
                                            //show if the first is the one connected
                                            set.put(new PropertiesOfNodesOfType6_10_11_12.isSwitchConnectedToFirstOutputProperty(switchOutputNodeProperties));
                                        } //second output
                                        else {

                                            set.put(new PropertiesOfNodesOfType6_10_11_12.isSecondOutputNodeInParentSwitchProperty(switchOutputNodeProperties));
                                            //show if the first is the one connected
                                            set.put(new PropertiesOfNodesOfType6_10_11_12.isSwitchConnectedToSecondOutputProperty(switchOutputNodeProperties));
                                        }
                                    }

                                    sheet.put(set);
                                    return sheet;
                                }
                            };
                            //add labels for the properties editor of the widget
                            node.setDisplayName("Properties of the Switch Output");
                            node.setShortDescription("Properties of the Switch Output");
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

                ActionListener cutActionListener = new MySwitchOutputWidget.PopupCutActionListener(WidgetCommonInfo.WidgetType.SWITCH_OUTPUT, instance, N);
                cutMenuItem.addActionListener(cutActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                JMenuItem copyMenuItem = new JMenuItem("Copy");
                copyMenuItem.setActionCommand((String) TransferHandler.getCopyAction().
                        getValue(Action.NAME));

                ActionListener copyActionListener = new MySwitchOutputWidget.PopupCopyActionListener(WidgetCommonInfo.WidgetType.SWITCH_OUTPUT, instance, N);
                copyMenuItem.addActionListener(copyActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                if (N.getShape().getType().equals("12")) {
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
                if (MySwitchOutputWidget.this.getSwitchInstance() != null) {
                    MySwitchWidget.hideShowAllChildrensOfSwitch(getSwitchInstance().getNumber());
                }

            }
        }, false));
    }

    /**
     * constructor for copied or cut widget
     *
     * @param scene is the scene that is the container of the widgets
     * @param n is the shape of the node
     * @param copiedOrCuttedWidget is the copied or cut switch output widget
     */
    public MySwitchOutputWidget(Scene scene, ShapeNode n, MySwitchOutputWidget copiedOrCuttedWidget) {
        //for copied or cutted widget => initialize the pasted widget to evade cloning of the same widget
        this(scene, n);
        //copied properties
        PropertiesOfNodesOfType6_10_11_12 copiedProperties = copiedOrCuttedWidget.getSwitchOutputNodeProperties();
        //copy values
        getSwitchOutputNodeProperties().setType(copiedProperties.getType());

    }

    /**
     * helper class for beginning the linking process from a switch intermediate
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
            MyConnectorWidget.isLinearLink = WidgetCommonInfo.linearLink = this.linearLink = linear;
            widgetPointLocation = MySwitchOutputWidget.this.getSwitchOutputNodeProperties().getLocation();
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
                //robot.mousePress(OutputEvent.BUTTON1_MASK);
                robot.mousePress(InputEvent.BUTTON1_MASK);

            } catch (AWTException e) {
                //e.printStackTrace();
            }

        }
    }

    /**
     * helper class for copying a switch output widget
     *
     */
    public class PopupCopyActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MySwitchOutputWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         * constructor
         *
         * @param type is the type of the widget node
         * @param mySow is the switch output widget that has been copied or cut
         * @param shapeNode is the shape of the node
         */
        public PopupCopyActionListener(WidgetType type, MySwitchOutputWidget mySow, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = mySow;
            if (!shapeNode.getShape().getName().startsWith("Copy of Switch Output")) {
                shapeNode.getShape().setName("Copy of Switch Output " + shapeNode.getShape().getName());
            }
            this.shapeNode = shapeNode;
        }

        /**
         * the action that is performed when the copy pop up menu item is
         * clicked
         *
         * @param actionEvent event triggered
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (widgetType == WidgetType.SWITCH_OUTPUT) {
                //copy the widget to the scene
                ccpWidget = MySwitchOutputWidget.this;
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
     * helper class for cutting a switch output widget
     *
     */
    public class PopupCutActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MySwitchOutputWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         * constructor
         *
         * @param type is the type of the widget node
         * @param mySow is the switch output widget that has been copied or cut
         * @param shapeNode is the shape of the node
         */
        public PopupCutActionListener(WidgetType type, MySwitchOutputWidget mySow, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = mySow;
            if (!shapeNode.getShape().getName().startsWith("Copy of Switch Output")) {
                shapeNode.getShape().setName("Copy of Switch Output " + shapeNode.getShape().getName());
            }

            this.shapeNode = shapeNode;
        }

        /**
         * the action that is performed when the copy pop up menu item is
         * clicked
         *
         * @param actionEvent event triggered
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (widgetType == WidgetType.SWITCH_OUTPUT) {
                //copy it to the scene
                ccpWidget = MySwitchOutputWidget.this;
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
     * this method is called to begin the process of the initialization of this
     * switch sub component's parent switch
     *
     */
    public void initializeParentSwitch() {
        //call another thread to wait for user input while the dialog is shown
        //we need this thread so that the GUI is not blocked
        new AsynchrnousDialogBoxListResultConsumer().start();
    }

    /**
     * is a thread that is called to get from the message dialog the user's
     * choice of the switch configuration
     *
     */
    private class AsynchrnousDialogBoxListResultConsumer extends Thread {

        /**
         * start the thread work
         */
        @Override
        public void run() {
            //initialize dialog that will ask the user to chose from different radio buttons
            DialogBoxList<String> dialog;
            //initialize the waiting queue to -1 to know if the field has been updated from the dialog or not
            switchOutputNodeProperties.setParentSwitch(-1);
            //instanciate our possible choices that the user will chose from
            List<String> possibilities = new ArrayList<>();
            possibilities.add("Add New Switch");
            List<MySwitchWidget> allSwitchs = SwitchRepository.getInstance().getAll();
            for (MySwitchWidget mySw : allSwitchs) {
                //show only switch that has an empty slot for a switch input
                if (mySw.getFirstOutputNode() == null || mySw.getSecondOutputNode() == null) {
                    possibilities.add(mySw.getNumber() + "");
                }

            }
            //initialize the answers that we want for the preceding choices
            List<String> answers = new ArrayList<>();
            answers.add("99999");
            for (MySwitchWidget mySw : allSwitchs) {
                //show only switch that has an empty slot for a switch input
                if (mySw.getFirstOutputNode() == null || mySw.getSecondOutputNode() == null) {
                    answers.add(mySw.getNumber() + "");
                }
            }

            //instanciate the dialog using the title of he window, the description,the choices for the user, the answer related
            //to these choices (the data that we need in return)
            dialog = new DialogBoxList<>("Parent Switch Selection", "Please select the switch?",
                    possibilities, answers, DialogBoxList.MessageType.INFORMATION);

            //while the user did not change the field; he did not make a choice
            //while(switchOutputNodeProperties.getParentSwitch() == -1)
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
//                        switchOutputNodeProperties.setParentSwitch(0);
//                    }
                if (result.equals("99999")) {
                    //show which switch it is assigned to
                    //add the switch to this widget
                    switchInstance = new MySwitchWidget();

                    //first or second node dialog
                    DialogBoxList<String> dialogFirstSecond ;

                    List<String> possibilitiesFirstOrSecondNode = new ArrayList<>();
                    possibilitiesFirstOrSecondNode.add("First Output Node in the Switch");
                    possibilitiesFirstOrSecondNode.add("Second Output Node in the Switch");

                    //initialize the answers that we want for the preceding choices
                    List<String> answersFirstOrSecondNode = new ArrayList<>();
                    answersFirstOrSecondNode.add("First");
                    answersFirstOrSecondNode.add("Second");

                    //instanciate the dialog using the title of the window, the description,the choices for the user, the answer related
                    //to these choices (the data that we need in return)
                    dialogFirstSecond = new DialogBoxList<>("Switch Output Node rank in the Switch", "Please select the rank of the Switch Output in the switch?",
                            possibilitiesFirstOrSecondNode, answersFirstOrSecondNode, DialogBoxList.MessageType.QUESTION);

                    String resultFiestOrSecond = "";
                    while (resultFiestOrSecond == null || resultFiestOrSecond.isEmpty()) {
                        resultFiestOrSecond =  dialogFirstSecond.showDialogAndGetResult();
                    }

                    switchOutputNodeProperties.setParentSwitch(switchInstance.getNumber());
                    switch (resultFiestOrSecond) {
                        case "First":
                            MySwitchOutputWidget.this.getSwitchOutputNodeProperties().setIsFirstOutputNodeInParentSwitch(true);
                            MySwitchOutputWidget.this.getSwitchOutputNodeProperties().setIsSecondOutputNodeInParentSwitch(false);
                            //set is connected to switch
                            MySwitchOutputWidget.this.getSwitchOutputNodeProperties().setIsSwitchConnectedToFirstOutput(true);
                            MySwitchOutputWidget.this.getSwitchOutputNodeProperties().setIsSwitchConnectedToSecondOutput(false);
                            //assign the first endstop sensor
                            switchInstance.setFirstOutputNode(MySwitchOutputWidget.this);
                            break;
                        case "Second":
                            MySwitchOutputWidget.this.getSwitchOutputNodeProperties().setIsSecondOutputNodeInParentSwitch(true);
                            MySwitchOutputWidget.this.getSwitchOutputNodeProperties().setIsFirstOutputNodeInParentSwitch(false);
                            //set is connected to switch
                            MySwitchOutputWidget.this.getSwitchOutputNodeProperties().setIsSwitchConnectedToFirstOutput(false);
                            MySwitchOutputWidget.this.getSwitchOutputNodeProperties().setIsSwitchConnectedToSecondOutput(true);
                            //assign the first endstop sensor
                            switchInstance.setSecondOutputNode(MySwitchOutputWidget.this);
                            break;
                    }

                    //show that this stop sensor widget is affected to a switch
                    widget.addChild(switchInstance.getWidget(), switchInstance);

                    Scene.globalScene.validate();
                    Scene.globalScene.repaint();
                    //add the switch to the switch repository
                    SwitchRepository.getInstance().add(switchInstance);

                } else {
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
//                             switchInstance.setSecondOutputNode(MySwitchOutputWidget.this);
//                         }
                    //first or second node dialog
                    DialogBoxList<String> dialogFirstSecond ;
                    //list of what to show
                    List<String> possibilitiesFirstOrSecondNode = new ArrayList<>();
                    //initialize the answers that we want for the preceding choices
                    List<String> answersFirstOrSecondNode = new ArrayList<>();
                    if (switchInstance.getFirstOutputNode() == null && switchInstance.getSecondOutputNode() == null) {
                        possibilitiesFirstOrSecondNode.add("First Output Node in the Switch");
                        possibilitiesFirstOrSecondNode.add("Second Output Node in the Switch");

                        answersFirstOrSecondNode.add("First");
                        answersFirstOrSecondNode.add("Second");
                    } else if (switchInstance.getFirstOutputNode() == null) {
                        possibilitiesFirstOrSecondNode.add("First Output Node in the Switch");
                        answersFirstOrSecondNode.add("First");
                    } else {
                        possibilitiesFirstOrSecondNode.add("Second Output Node in the Switch");
                        answersFirstOrSecondNode.add("Second");
                    }

                    //instanciate the dialog using the title of the window, the description,the choices for the user, the answer related
                    //to these choices (the data that we need in return)
                    dialogFirstSecond = new DialogBoxList<>("Switch Output Node rank in the Switch", "Please select the rank of the Switch Output in the switch?",
                            possibilitiesFirstOrSecondNode, answersFirstOrSecondNode, DialogBoxList.MessageType.QUESTION);

                    String resultFiestOrSecond = "";
                    while (resultFiestOrSecond == null || resultFiestOrSecond.isEmpty()) {
                        resultFiestOrSecond =  dialogFirstSecond.showDialogAndGetResult();
                    }

                    switchOutputNodeProperties.setParentSwitch(switchInstance.getNumber());
                    switch (resultFiestOrSecond) {
                        case "First":
                            MySwitchOutputWidget.this.getSwitchOutputNodeProperties().setIsFirstOutputNodeInParentSwitch(true);
                            //assign the first endstop sensor
                            switchInstance.setFirstOutputNode(MySwitchOutputWidget.this);
                            break;
                        case "Second":
                            MySwitchOutputWidget.this.getSwitchOutputNodeProperties().setIsSecondOutputNodeInParentSwitch(true);
                            //assign the first endstop sensor
                            switchInstance.setSecondOutputNode(MySwitchOutputWidget.this);
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
                    //switchOutputNodeProperties.setParentSwitch(switchInstance.getNumber());

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
            }

        }
        //}

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
            //delete from current repository
            SwitchOutputRepository.getInstance().remove(MySwitchOutputWidget.this.getSwitchOutputNodeProperties().getNumber());
            SwitchSensorRepository.getInstance().remove(MySwitchOutputWidget.this.getSwitchOutputNodeProperties().getNumber());

            //update parent switch and remove from parent switch
            if (switchInstance != null) {
                MySwitchWidget sw = SwitchRepository.getInstance().find(switchInstance.getNumber());
                if (MySwitchOutputWidget.this.getSwitchOutputNodeProperties().getIsFirstOutputNodeInParentSwitch()) {
                    sw.setFirstOutputNode(null);
                    //delete if the switch became empty (zombie switch)
                    SwitchRepository.getInstance().IfUnusedSwitch_DeleteSwitch(switchInstance.getNumber());
                } else {
                    sw.setSecondOutputNode(null);
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
        MySwitchOutputWidget.scene = scene;
    }

    /**
     *
     * @return the properties of the switch output node
     */
    public PropertiesOfNodesOfType6_10_11_12 getSwitchOutputNodeProperties() {
        return switchOutputNodeProperties;
    }

    /**
     *
     * @param switchOutputNodeProperties the properties of the switch output
     * node
     */
    public void setSwitchOutputNodeProperties(PropertiesOfNodesOfType6_10_11_12 switchOutputNodeProperties) {
        this.switchOutputNodeProperties = switchOutputNodeProperties;
    }

    /**
     *
     * @return the current instance of the switch output widget
     */
    public MySwitchOutputWidget getInstance() {
        return instance;
    }

    /**
     *
     * @param instance the current instance of the switch output widget
     */
    public void setInstance(MySwitchOutputWidget instance) {
        this.instance = instance;
    }

    /**
     *
     * @return a list of the connectors of the switch output widget
     */
    public List<MyConnectorWidget> getConnections() {
        return connections;
    }

    /**
     *
     * @param connections a list of the connectors of the switch output widget
     */
    public void setConnections(List<MyConnectorWidget> connections) {
        this.connections = connections;
    }

    /**
     *
     * @return the switch component of this switch output(sub component)
     */
    public MySwitchWidget getSwitchInstance() {
        return switchInstance;
    }

    /**
     *
     * @param switchInstance the switch component of this switch output(sub
     * component)
     */
    public void setSwitchInstance(MySwitchWidget switchInstance) {
        this.switchInstance = switchInstance;
    }

    /**
     *
     * @return the widget icon of the switch output node
     */
    public IconNodeWidget getWidget() {
        return widget;
    }

    /**
     *
     * @param widget the widget icon of the switch output node
     */
    public void setWidget(IconNodeWidget widget) {
        this.widget = widget;
    }
}
