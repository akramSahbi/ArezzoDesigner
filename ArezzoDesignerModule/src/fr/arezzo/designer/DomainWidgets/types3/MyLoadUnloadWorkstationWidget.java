package fr.arezzo.designer.DomainWidgets.types3;

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
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.NodeOperation;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType3;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;

import fr.arezzo.designer.Dialogs.DialogBoxes.DialogBoxList;
import fr.arezzo.designer.Dialogs.DialogBoxes.DialogBoxRadioButtons;
import fr.arezzo.designer.Dialogs.DialogOutputMessages.Alert;
import fr.arezzo.designer.Domain.CoordinatesOfArezzo;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.WidgetType;
import static fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget.mutex;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyLoadUnloadSensorWidget;
import fr.arezzo.designer.EditeurModule.Repositories.ConnectorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.LoadUnloadWorkstationRepository;
import fr.arezzo.designer.Scene.Scene;
import static fr.arezzo.designer.Scene.Scene.N;
import static fr.arezzo.designer.Scene.Scene.getN;
import fr.arezzo.designer.palette.ShapeNode;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.LabelWidget;

/**
 * MyLoadUnloadWorkstationWidget represents a load/unload workstation node
 * widget
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public final class MyLoadUnloadWorkstationWidget {

    //scene represents the place that will contain this widget
    public static Scene scene;
    //PropertiesOfNodesOfType3 represents the properties of the load unload worksations (nodes of type 3)
    private PropertiesOfNodesOfType3 loadUnloadWorkstationProperties;
    //represents the icon that will be placed inside the scene
    private IconNodeWidget widget = null;
    //current instance
    private MyLoadUnloadWorkstationWidget instance = null;
    //connections (links) widget
    private List<MyConnectorWidget> connections = new ArrayList<>();

    //did the user make the choice during the dialog box prompt(-1 => the user did not make a choice so we dont have a result yet)
    private static String result = "-1";
    //the widet editor action to edit the name of the widget on the scene
    public WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
    
    

    /**
     * constructor which is initialized using a reference to the scene, and the
     * shape of the widget that is the icon inside the palette the role of the
     * constructor is to create the workstation widget inside the scene and add
     * to it its actions (moving action, hover action, properties menu, delete
     * menu
     *
     * @param scene the scene that contains the widgets
     * @param n the shape of the widget node
     */
    public MyLoadUnloadWorkstationWidget(Scene scene, ShapeNode n) {

        instance = MyLoadUnloadWorkstationWidget.this;
        //initialize the domain class which contain the properties of the widget
        loadUnloadWorkstationProperties = new PropertiesOfNodesOfType3(scene);

        //initialize the number of the widget (ID)
        loadUnloadWorkstationProperties.setNumber(WidgetCommonInfo.getNumberOfNextWidget());
        loadUnloadWorkstationProperties.setID(loadUnloadWorkstationProperties.getNumber());
        //initialize our instance of the scene for direct access
        MyLoadUnloadWorkstationWidget.scene = scene;
        //initialize the widget which is a genereic IconWidget that will be added to the scene
        widget = new IconNodeWidget(scene);
        //initialize the widget image using the shape image (from the palette)
        widget.setImage(ImageUtilities.loadImage(n.getShape().getImage()));
        //initialize the label with the number of the widget
        widget.setLabel(loadUnloadWorkstationProperties.getNumber() + "");
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
                if (N.getShape().getType().equals("3")) {
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
                                    loadUnloadWorkstationProperties.setxCoordinate(x);
                                    loadUnloadWorkstationProperties.setyCoordinate(y);
                                    //update to new Arezzo coordinates system only if changed location
                                    Point arezzoCoordinates = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
                                    if (x != (float) arezzoCoordinates.getX()) {
                                        loadUnloadWorkstationProperties.setxCoordinateArezzo((float) arezzoCoordinates.getX());
                                    }
                                    if (y != (float) arezzoCoordinates.getY()) {
                                        loadUnloadWorkstationProperties.setyCoordinateArezzo((float) arezzoCoordinates.getY());
                                    }
                                    //create the properties editor sheet
                                    Sheet sheet = super.createSheet();

                                    //create a set that the properties editor will contain
                                    //Sheet.Set set = sheet.createPropertiesSet();
                                    Sheet.Set set = Sheet.createPropertiesSet();
                                    //we fill the sheet with all of the properties of the domain class of the widget
                                    //since the widget is a type 3 node, its properties will contain all of the
                                    //attributes of the domain class "PropertiesOfNodesOfType3" properties 
                                    set.put(new PropertiesOfNodesOfType3.NumberProperty(loadUnloadWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.XCoordinateProperty(loadUnloadWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.YCoordinateProperty(loadUnloadWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.XCoordinateArezzoProperty(loadUnloadWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.YCoordinateArezzoProperty(loadUnloadWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.TypeProperty(loadUnloadWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.AipNumberProperty(loadUnloadWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.NumberOfTheMachineProperty(loadUnloadWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.WaitingQueueMaximumSizeProperty(loadUnloadWorkstationProperties));
                                    //PropertiesOfNodesOfType3.OperationsOfThisMachineOfLoadUnloadWorkstationProperty oprationsProperty =
                                    //       new PropertiesOfNodesOfType3.OperationsOfThisMachineOfLoadUnloadWorkstationProperty(loadUnloadWorkstationProperties);

                                    //set.put(oprationsProperty);
                                    set.put(new PropertiesOfNodesOfType3.OperationsOfThisMachineProperty(loadUnloadWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.DurationsOfOperationsProperty(loadUnloadWorkstationProperties));
                                    //duration of the operations added one by one to edit in properties
                                    int operationIndex = 0;
                                    if (MyLoadUnloadWorkstationWidget.this.getLoadUnloadWorkstationProperties().getOperationsOfThisMachine() != null
                                            && MyLoadUnloadWorkstationWidget.this.getLoadUnloadWorkstationProperties().getOperationsOfThisMachine().size() > 0) {
                                        for (Integer operationNumber : MyLoadUnloadWorkstationWidget.this.getLoadUnloadWorkstationProperties().getOperationsOfThisMachine()) {
                                            //duration of operation with "operationNumber"
                                            set.put(new PropertiesOfNodesOfType3.OperationDurationProperty(getLoadUnloadWorkstationProperties(),
                                                    operationNumber, operationIndex, operationNumber + ""));
                                            operationIndex++;
                                        }
                                    }

                                    set.put(new PropertiesOfNodesOfType3.SpeedsToNextNodesProperty(loadUnloadWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.NumbersOfNextNodesProperty(loadUnloadWorkstationProperties));

                                    //speed to next nodes one by one to edit
                                    int nextNodeIndex = 0;
                                    if (MyLoadUnloadWorkstationWidget.this.getLoadUnloadWorkstationProperties().getNumbersOfNextNodes() != null
                                            && MyLoadUnloadWorkstationWidget.this.getLoadUnloadWorkstationProperties().getNumbersOfNextNodes().size() > 0) {
                                        for (Integer nextNodeNumber : MyLoadUnloadWorkstationWidget.this.getLoadUnloadWorkstationProperties().getNumbersOfNextNodes()) {
                                            //NextNodeSpeedProperty
                                            set.put(new PropertiesOfNodesOfType3.NextNodeSpeedProperty(getLoadUnloadWorkstationProperties(),
                                                    nextNodeNumber, nextNodeIndex));
                                            nextNodeIndex++;
                                        }
                                    }

                                    //put all of the properties in the sheet
                                    sheet.put(set);
                                    return sheet;

                                }
                            };
                            //add labels for the properties editor of the widget
                            node.setDisplayName("Properties of the Load/Unload Workstation");
                            node.setShortDescription("Properties of the Load/Unload  Workstation");
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

                ActionListener cutActionListener = new MyLoadUnloadWorkstationWidget.PopupCutActionListener(WidgetCommonInfo.WidgetType.LOAD_UNLOAD_WORKSTATION, instance, N);
                cutMenuItem.addActionListener(cutActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                JMenuItem copyMenuItem = new JMenuItem("Copy");
                copyMenuItem.setActionCommand((String) TransferHandler.getCopyAction().
                        getValue(Action.NAME));

                ActionListener copyActionListener = new MyLoadUnloadWorkstationWidget.PopupCopyActionListener(WidgetCommonInfo.WidgetType.LOAD_UNLOAD_WORKSTATION, instance, N);
                copyMenuItem.addActionListener(copyActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                if (N.getShape().getType().equals("3")) {
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

    public MyLoadUnloadWorkstationWidget(Scene scene, ShapeNode n, MyLoadUnloadWorkstationWidget copiedOrCuttedWidget) {
        this(scene, n);
        //copied properties
        PropertiesOfNodesOfType3 copiedProperties = copiedOrCuttedWidget.getLoadUnloadWorkstationProperties();
        //copy values
        getLoadUnloadWorkstationProperties().setType(copiedProperties.getType());
        getLoadUnloadWorkstationProperties().setAipNumber(copiedProperties.getAipNumber());
        getLoadUnloadWorkstationProperties().setDurationsOfOperations(copiedProperties.getDurationsOfOperations());
        getLoadUnloadWorkstationProperties().setOperationsOfThisMachine(copiedProperties.getOperationsOfThisMachine());
        getLoadUnloadWorkstationProperties().setNumberOfTheMachine(copiedProperties.getNumberOfTheMachine());
        getLoadUnloadWorkstationProperties().setWaitingQueueMaximumSize(copiedProperties.getWaitingQueueMaximumSize());
    }

    /**
     * link action when clicked with the mouse
     *
     */
    public class LinkActionListener implements ActionListener {

        private boolean linearLink = false;
        Point widgetPointLocation = null;

        /**
         *
         * @param linear if true the link is linear else the link is polygonal
         */
        public LinkActionListener(boolean linear) {
            MyConnectorWidget.isLinearLink = WidgetCommonInfo.linearLink = this.linearLink = linear;
            widgetPointLocation = MyLoadUnloadWorkstationWidget.this.getLoadUnloadWorkstationProperties().getLocation();
        }

        /**
         * the action that is performed when the copy pop up menu item is
         * clicked
         *
         * @param actionEvent the event triggered
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
     * class for the copy pop up menu item
     *
     */
    public class PopupCopyActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MyLoadUnloadWorkstationWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         * constructor
         *
         * @param type the type of the widget
         * @param myLUWw the load/unload widget
         * @param shapeNode the shape of the node
         */
        public PopupCopyActionListener(WidgetType type, MyLoadUnloadWorkstationWidget myLUWw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = myLUWw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Load Unload Workstation")) {
                shapeNode.getShape().setName("Copy of Load Unload Workstation " + shapeNode.getShape().getName());
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
            if (widgetType == WidgetType.LOAD_UNLOAD_WORKSTATION) {
                //copy the widget to the scene
                ccpWidget = MyLoadUnloadWorkstationWidget.this;
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
     * helper class for the cut pop up menu item
     *
     */
    public class PopupCutActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MyLoadUnloadWorkstationWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         *
         * @param type the type of the widget
         * @param myLUWw the load/unload widget
         * @param shapeNode the shape of the node
         */
        public PopupCutActionListener(WidgetType type, MyLoadUnloadWorkstationWidget myLUWw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = myLUWw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Load Unload Workstation")) {
                shapeNode.getShape().setName("Copy of Load Unload Workstation " + shapeNode.getShape().getName());
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
            if (widgetType == WidgetType.LOAD_UNLOAD_WORKSTATION) {
                //copy it to the scene
                ccpWidget = MyLoadUnloadWorkstationWidget.this;
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
     * class helper of the delete pop up menu item
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
            boolean removed = LoadUnloadWorkstationRepository.getInstance().remove(getLoadUnloadWorkstationProperties().getNumber());

        }
    }

    /**
     *
     * @return the scene containing the widgets
     */
    public static Scene getScene() {
        return scene;
    }

    /**
     *
     * @param scene the scene containing the widgets
     */
    public static void setScene(Scene scene) {
        MyLoadUnloadWorkstationWidget.scene = scene;
    }

    /**
     *
     * @return the properties of the load/unload widget
     */
    public PropertiesOfNodesOfType3 getLoadUnloadWorkstationProperties() {
        return loadUnloadWorkstationProperties;
    }

    /**
     *
     * @param loadUnloadWorkstationProperties the properties of the load/unload
     * widget
     */
    public void setLoadUnloadWorkstationProperties(PropertiesOfNodesOfType3 loadUnloadWorkstationProperties) {
        this.loadUnloadWorkstationProperties = loadUnloadWorkstationProperties;
    }

    /**
     * show message dialog to chose the operations of the widget
     *
     */
    public void initializeOperations() {

        //the possible choices of operations for the user
        List<String> operationsPossibilities = new ArrayList<>();
        operationsPossibilities.add("Load");
        operationsPossibilities.add("Unload");
        //operationsPossibilities.add("Load And Unload");
        operationsPossibilities.add("Cancel");
//        the result needed depending on the user choice 
//        choice 1 => answer possible 1 anso so on
        List<String> operationsAnswerPossibilities = new ArrayList<>();
        operationsAnswerPossibilities.add("100");
        operationsAnswerPossibilities.add("101");
        //operationsAnswerPossibilities.add("100 101");
        operationsAnswerPossibilities.add("");
        String userChoicevalue = "";
        //we need explicitly the user choice of the operation
        do {
            //initialize the dialog
            DialogBoxList<String> dialog = new DialogBoxList<>("Operations of the Loading/Unloading workstation", "Please chose the Operations of the load"
                    + "/unload workstation?",
                    operationsPossibilities, operationsAnswerPossibilities, DialogBoxList.MessageType.QUESTION);
            //get the user's choice
            userChoicevalue = dialog.showDialogAndGetResult();
        } while (userChoicevalue.equals(""));

        if (userChoicevalue != null) {
            //split the answer of the user in the format "100 101" 
            String[] values = userChoicevalue.split(" ");
            //initialize the operations of the loading/unloading workstation
            List<Integer> OperationsValuesChosed = new ArrayList<>();
            List<Integer> operationDurations = new ArrayList<>();
            for (String value : values) {
                Integer intValue = Integer.parseInt(value);
                OperationsValuesChosed.add(intValue);
                //add a duration
                operationDurations.add(0);
            }
            //set operations and their respective duration
            loadUnloadWorkstationProperties.setOperationsOfThisMachine(OperationsValuesChosed);
            loadUnloadWorkstationProperties.setDurationsOfOperations(operationDurations);
        } else {
            userChoicevalue = "0 ";
            //split the answer of the user in the format "100 101" 
            String[] values = userChoicevalue.split(" ");
            //initialize the operations of the loading/unloading workstation
            List<Integer> OperationsValuesChosed = new ArrayList<>();
            List<Integer> operationDurations = new ArrayList<>();
            for (String value : values) {
                Integer intValue = Integer.parseInt(value);
                OperationsValuesChosed.add(intValue);
                //add a duration
                operationDurations.add(0);
            }
            //set operations and their respective duration
            loadUnloadWorkstationProperties.setOperationsOfThisMachine(OperationsValuesChosed);
            loadUnloadWorkstationProperties.setDurationsOfOperations(operationDurations);

        }

    }

    /**
     * get from the message dialog the user's choice of the waiting queue size
     * of the load/unload workstation
     *
     */
    public void initializeWaitingQueueForLoadUnloadWorkstation() {
        //call another thread to wait for user input while the dialog is shown
        //we need this thread so that the GUI is not blocked
        new AsynchrnousDialogBoxRadioButtonsResultConsumer().start();
    }

    /**
     * thread to show a dialog box to select the size of the queue of the
     * machine
     *
     */
    private class AsynchrnousDialogBoxRadioButtonsResultConsumer extends Thread {

        @Override
        public void run() {
            //initialize dialog that will ask the user to chose from different radio buttons
            DialogBoxRadioButtons dialog = null;
            //initialize the waiting queue to -1 to know if the field has been updated from the dialog or not
            loadUnloadWorkstationProperties.setWaitingQueueMaximumSize(-1);
            //instanciate our possible choices that the user will chose from
            List<String> possibilities = new ArrayList<>();
            possibilities.add("100");
            possibilities.add("200");
            possibilities.add("300");
            possibilities.add("Cancel");
            //initialize the answers that we want for the preceding choices
            List<String> answers = new ArrayList<>();
            answers.add("100");
            answers.add("200");
            answers.add("300");
            answers.add("0");
            //instanciate the dialog using the title of he window, the description,the choices for the user, the answer related
            //to these choices (the data that we need in return)
            dialog = new DialogBoxRadioButtons("Size of the workstation Queue", "Please select the Size of the workstation's Queue?",
                    possibilities, answers);

            //while the user did not change the field; he did not make a choice
            while (loadUnloadWorkstationProperties.getWaitingQueueMaximumSize() == -1) {
                try {
                    //read if there is an answer from the dialog
                    //until the user releases the lock of the semaphore
                    //when that happens, we have the choice data of the user
                    String result = dialog.getAnswer();
                    //block waiting for the user choice
                    MyWorkstationWidget.semaphore.acquire();
                    mutex.acquire();
                    //update the field value using the user choice data
                    loadUnloadWorkstationProperties.setWaitingQueueMaximumSize(Integer.parseInt(result));
                    //release the lock
                    mutex.release();
                    //stop the block waiting
                    MyWorkstationWidget.semaphore.release();
                    try {
                        //sleep this thread so that we dnot waste too much CPU processing in this loop 
                        sleep(50);
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                } catch (InterruptedException | NumberFormatException ex) {
                    Exceptions.printStackTrace(ex);
                }

            }
            //stop this thread and quit
            //stop();
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
            
            return MyLoadUnloadWorkstationWidget.this.getLoadUnloadWorkstationProperties().getNumber()+"";
            
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
                MyLoadUnloadWorkstationWidget.this.getLoadUnloadWorkstationProperties().setNumber(newNumber);
                
                if(newNumber instanceof Integer)
                {
                    ((LabelWidget) widget).setLabel(newNumber+"");
                    //update next nodes numbers
                    for(MyConnectorWidget conn : MyLoadUnloadWorkstationWidget.this.getConnections())
                    {
                        Alert.alert("ok", "number conn: " + conn.getMyConnectorInfo().getConnectorProperties().getNumber(), Alert.AlertType.ERROR_MESSAGE);
                        Integer IdOfTargetWidget = MyLoadUnloadWorkstationWidget.this.getLoadUnloadWorkstationProperties().getID();
                        if(conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().contains(IdOfTargetWidget))
                        {
                            Alert.alert("ok", "number conn: " + conn.getMyConnectorInfo().getConnectorProperties().getNumber(), Alert.AlertType.ERROR_MESSAGE);
                            int i = conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().indexOf(IdOfTargetWidget);
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
     * @return the instance of the current load/unload widget
     */
    public MyLoadUnloadWorkstationWidget getInstance() {
        return instance;
    }

    /**
     *
     * @param instance the instance of the current load/unload widget
     */
    public void setInstance(MyLoadUnloadWorkstationWidget instance) {
        this.instance = instance;
    }

    /**
     *
     * @return a list of the connections linked to the widget node
     */
    public List<MyConnectorWidget> getConnections() {
        return connections;
    }

    /**
     *
     * @param connections a list of the connections linked to the widget node
     */
    public void setConnections(List<MyConnectorWidget> connections) {
        this.connections = connections;
    }

    /**
     *
     * @return the widget icon of the load/unload widget
     */
    public IconNodeWidget getWidget() {
        return widget;
    }

    /**
     *
     * @param widget the widget icon of the load/unload widget
     */
    public void setWidget(IconNodeWidget widget) {
        this.widget = widget;
    }
}
