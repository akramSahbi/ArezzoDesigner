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
import java.util.concurrent.Semaphore;
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
import fr.arezzo.designer.Dialogs.DialogBoxes.DialogBoxCheckBoxes;
import fr.arezzo.designer.Dialogs.DialogBoxes.DialogBoxRadioButtons;
import fr.arezzo.designer.Domain.CoordinatesOfArezzo;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType3;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;

import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.MyConnectProvider;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.MyMoveStrategyProvider;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.WidgetType;
import fr.arezzo.designer.EditeurModule.Repositories.ConnectorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.WorkstationRepository;
import fr.arezzo.designer.Scene.Scene;
import static fr.arezzo.designer.Scene.Scene.N;
import static fr.arezzo.designer.Scene.Scene.getN;

import fr.arezzo.designer.palette.ShapeNode;

/**
 * MyLoadUnloadWorkstationWidget represents a workstation widget node of type 3
 *
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public final class MyWorkstationWidget //extends Widget
{

    //IPC semaphores
    public static Semaphore semaphore = new Semaphore(1);
    public static Semaphore mutex = new Semaphore(1);

    //scene represents the place that will contain this widget
    public static Scene scene;
    //PropertiesOfNodesOfType3 represents the properties of the worksations (nodes of type 3)
    private PropertiesOfNodesOfType3 parentWorkstationProperties;
    //represents the icon that will be placed inside the scene
    private IconNodeWidget widget = null;
    //current instance
    private MyWorkstationWidget instance = null;
    //connections (links) widget
    private List<MyConnectorWidget> connections = new ArrayList<>();
    //the possible choices of operations for the user
    public static List<String> operationsPossibilities;
    //the possible answers by the user related to possible operations
    public static List<String> operationsAnswerPossibilities;

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
    public MyWorkstationWidget(Scene scene, ShapeNode n) {
        //super(scene);
        instance = MyWorkstationWidget.this;
        //initialize the domain class which contain the properties of the widget
        parentWorkstationProperties = new PropertiesOfNodesOfType3(scene);
        //initialize the number of the widget (ID)
        parentWorkstationProperties.setNumber(WidgetCommonInfo.getNumberOfNextWidget());
        //initialize our instance of the scene for direct access
        MyWorkstationWidget.scene = scene;
        //initialize the widget which is a genereic IconWidget that will be added to the scene
        widget = new IconNodeWidget(scene);
        //initialize the widget image using the shape image (from the palette)
        widget.setImage(ImageUtilities.loadImage(n.getShape().getImage()));
        //initialize the label with the number of the widget
        widget.setLabel(parentWorkstationProperties.getNumber() + "");

        //add an action that will make the widget linkable (we can link it to another widget or wire)
        widget.getActions().addAction(
                ActionFactory.createExtendedConnectAction(Scene.connectionLayer, new WidgetCommonInfo().new MyConnectProvider()));
        //add an action to make the widget alignable OLD
        //widget.getActions().addAction(
        //        ActionFactory.createAlignWithMoveAction( scene.mainLayer, scene.interactionLayer, ActionFactory.createDefaultAlignWithMoveDecorator())); 
        //allow multiple selection of widgets
        MyMoveStrategyProvider myMoveProvider;
        myMoveProvider = new MyMoveStrategyProvider(scene);
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
                //converts from widget coordinates system (middle of widget is x:0,y:0)
                //to scene coordinates system that can have negative x or y then to
                //view coordinates system (top left == x:0,y:0)
                Point sceneLocation;
                sceneLocation = widget.convertLocalToScene(new Point(0, 0));
                //Alert.alert("local => scene", sceneLocation.toString(), Alert.AlertType.INFORMATION_MESSAGE);
                // localLocation = sceneLocation;
                sceneLocation = Scene.globalScene.convertSceneToView(sceneLocation);
                localLocation = sceneLocation;
                //Alert.alert("scene => view", sceneLocation.toString(), Alert.AlertType.INFORMATION_MESSAGE);
                //localLocation = Scene.globalScene.convertViewToScene(sceneLocation);
                N = (ShapeNode) sceneConstantRemoteAcess.findObject(widget);
                final float x = (float) localLocation.getLocation().getX();
                final float y = (float) localLocation.getLocation().getY();

                //adding the properties menu item to the popup menu of the widget
                JMenuItem propsMenu = new JMenuItem("Proprietes");
                //verify the type of the widget 
                if (N.getShape().getType().equals("3")) {
                    propsMenu.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            AbstractNode node;
                            node = new AbstractNode(Children.LEAF) {
                                
                                //creating the properties sheet which contains all of the informations of the widget
                                //that the user can edit
                                @Override
                                protected Sheet createSheet() {
                                    //initialize the properties editor with the properties of the widget
                                    //initialize the x ad y coordinates of the widget
                                    parentWorkstationProperties.setxCoordinate( x);
                                    parentWorkstationProperties.setyCoordinate( y);
                                    //update to new Arezzo coordinates system only if changed location
                                    Point arezzoCoordinates = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
                                    if (x != (float) arezzoCoordinates.getX()) {
                                        parentWorkstationProperties.setxCoordinateArezzo((float) arezzoCoordinates.getX());
                                    }
                                    if (y != (float) arezzoCoordinates.getY()) {
                                        parentWorkstationProperties.setyCoordinateArezzo((float) arezzoCoordinates.getY());
                                    }

                                    //create the properties editor sheet
                                    Sheet sheet = super.createSheet();
                                    //create a set that the properties editor will contain
                                    Sheet.Set set = Sheet.createPropertiesSet();
                                    //we fill the sheet with all of the properties of the domain class of the widget
                                    //since the widget is a type 3 node, its properties will contain all of the
                                    //attributes of the domain class "PropertiesOfNodesOfType3" properties 
                                    set.put(new PropertiesOfNodesOfType3.NumberProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.XCoordinateProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.YCoordinateProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.XCoordinateArezzoProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.YCoordinateArezzoProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.TypeProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.AipNumberProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.NumberOfTheMachineProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.WaitingQueueMaximumSizeProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.OperationsOfThisMachineProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.DurationsOfOperationsProperty(parentWorkstationProperties));
                                    //duration of the operations added one by one to edit in properties
                                    int operationIndex = 0;
                                    if (MyWorkstationWidget.this.getParentWorkstationProperties().getOperationsOfThisMachine() != null
                                            && MyWorkstationWidget.this.getParentWorkstationProperties().getOperationsOfThisMachine().size() > 0) {
                                        for (Integer operationNumber : MyWorkstationWidget.this.getParentWorkstationProperties().getOperationsOfThisMachine()) {
                                            int indexOfOperationName = MyWorkstationWidget.operationsAnswerPossibilities.indexOf(operationNumber + "");
                                            String operationName = MyWorkstationWidget.operationsPossibilities.get(indexOfOperationName);
                                            //duration of operation with "operationNumber"
                                            set.put(new PropertiesOfNodesOfType3.OperationDurationProperty(getParentWorkstationProperties(),
                                                    operationNumber, operationIndex, operationName));
                                            operationIndex++;
                                        }
                                    }

                                    set.put(new PropertiesOfNodesOfType3.SpeedsToNextNodesProperty(parentWorkstationProperties));
                                    set.put(new PropertiesOfNodesOfType3.NumbersOfNextNodesProperty(parentWorkstationProperties));

                                    //speed to next nodes one by one to edit
                                    int nextNodeIndex = 0;
                                    if (MyWorkstationWidget.this.getParentWorkstationProperties().getNumbersOfNextNodes() != null
                                            && MyWorkstationWidget.this.getParentWorkstationProperties().getNumbersOfNextNodes().size() > 0) {
                                        for (Integer nextNodeNumber : MyWorkstationWidget.this.getParentWorkstationProperties().getNumbersOfNextNodes()) {
                                            //NextNodeSpeedProperty
                                            set.put(new PropertiesOfNodesOfType3.NextNodeSpeedProperty(getParentWorkstationProperties(),
                                                    nextNodeNumber, nextNodeIndex));
                                            nextNodeIndex++;
                                        }
                                    }

                                    sheet.put(set);

                                    return sheet;

                                }
                            };
                            //add labels for the properties editor of the widget
                            node.setDisplayName("Properties of the Workstation");
                            node.setShortDescription("Properties of the Workstation");
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
                //if the shape is a workstation (of type 3) add to the popup menu of the widget the properties menu item

                //CUT/COPY PASTE
                JMenuItem cutMenuItem = new JMenuItem("Cut");
                cutMenuItem.setActionCommand((String) TransferHandler.getCutAction().
                        getValue(Action.NAME));

                ActionListener cutActionListener = new PopupCutActionListener(WidgetType.WORKSTATION, instance, N);
                cutMenuItem.addActionListener(cutActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                JMenuItem copyMenuItem = new JMenuItem("Copy");
                copyMenuItem.setActionCommand((String) TransferHandler.getCopyAction().
                        getValue(Action.NAME));

                ActionListener copyActionListener = new PopupCopyActionListener(WidgetType.WORKSTATION, instance, N);
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

    /**
     * constructor
     *
     * @param scene the scene containing the widgets
     * @param n the shape of the node
     * @param copiedOrCuttedWidget the widget of a workstation
     */
    public MyWorkstationWidget(Scene scene, ShapeNode n, MyWorkstationWidget copiedOrCuttedWidget) {
        this(scene, n);
        //copied properties
        PropertiesOfNodesOfType3 copiedProperties = copiedOrCuttedWidget.getParentWorkstationProperties();
        //copy values
        getParentWorkstationProperties().setType(copiedProperties.getType());
        getParentWorkstationProperties().setAipNumber(copiedProperties.getAipNumber());
        //getParentWorkstationProperties().setDurationsOfOperations(copiedProperties.getDurationsOfOperations());
        //getParentWorkstationProperties().setOperationsOfThisMachine(copiedProperties.getOperationsOfThisMachine());
        getParentWorkstationProperties().setNumberOfTheMachine(copiedProperties.getNumberOfTheMachine());
        getParentWorkstationProperties().getDurationsOfOperations()
                .addAll(copiedProperties.getDurationsOfOperations());
        getParentWorkstationProperties().getOperationsOfThisMachine()
                .addAll(copiedProperties.getOperationsOfThisMachine());
        getParentWorkstationProperties().setWaitingQueueMaximumSize(copiedProperties.getWaitingQueueMaximumSize());

    }

    /**
     * helper class to begin the linking process of the workstation widget to
     * other widgets
     *
     */
    public class LinkActionListener implements ActionListener {

        private boolean linearLink = false;
        Point widgetPointLocation = null;

        /**
         * constructor
         *
         * @param linear true if the link is linear, false if the link is
         * polygonal
         */
        public LinkActionListener(boolean linear) {
            MyConnectorWidget.isLinearLink = WidgetCommonInfo.linearLink = this.linearLink = linear;
            widgetPointLocation = MyWorkstationWidget.this.getParentWorkstationProperties().getLocation();
        }

        /**
         * the action that is performed when the link pop up menu item is
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
     * class helper that manages copying a workstation widget
     *
     */
    public class PopupCopyActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MyWorkstationWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         * constructor
         *
         * @param type the type of the widget
         * @param myWw the workstation widget to copy
         * @param shapeNode the shape of the workstation node
         */
        public PopupCopyActionListener(WidgetType type, MyWorkstationWidget myWw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = myWw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Workstation")) {
                shapeNode.getShape().setName("Copy of Workstation " + shapeNode.getShape().getName());
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
            if (widgetType == WidgetType.WORKSTATION) {
                //copy the widget to the scene
                ccpWidget = MyWorkstationWidget.this;
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
     * class helper that manages cutting a workstation widget
     *
     */
    public class PopupCutActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MyWorkstationWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         *
         * @param type the type of the node
         * @param myWw the widget that has been cut
         * @param shapeNode the shape of the node
         */
        public PopupCutActionListener(WidgetType type, MyWorkstationWidget myWw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = myWw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Workstation")) {
                shapeNode.getShape().setName("Copy of Workstation " + shapeNode.getShape().getName());
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
            if (widgetType == WidgetType.WORKSTATION) {
                //copy it to the scene
                ccpWidget = MyWorkstationWidget.this;
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
     * helper class that manages deleting a workstation widget
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
            for (MyConnectorWidget connection : getConnections()) 
            {
                connection.deleteNextNodeOfSourceAndLink();
                //remove connection from repository
                ConnectorRepository.getInstance().remove(connection.getMyConnectorInfo().getConnectorProperties().getNumber());
            }
            //then remove the node of the widget from the scene
            scene.removeNode(getN());

            //remove from repository
            WorkstationRepository.getInstance().remove(getParentWorkstationProperties().getNumber());
        }
    }

    /**
     *
     * @param scene the scene container of user widgets
     */
    public static void setScene(Scene scene) {
        MyWorkstationWidget.scene = scene;
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
     * @return the properties of the workstation
     */
    public PropertiesOfNodesOfType3 getParentWorkstationProperties() {
        return parentWorkstationProperties;
    }

    /**
     *
     * @param parentWorkstationProperties the properties of the workstation
     */
    public void setParentWorkstationProperties(PropertiesOfNodesOfType3 parentWorkstationProperties) {
        this.parentWorkstationProperties = parentWorkstationProperties;
    }

    /**
     *
     * @return a semaphore for managing inter process communication a.k.a IPC
     */
    public static Semaphore getSemaphore() {
        return semaphore;
    }

    /**
     *
     * @param semaphore a semaphore for managing inter process communication
     * a.k.a IPC
     */
    public static void setSemaphore(Semaphore semaphore) {
        MyWorkstationWidget.semaphore = semaphore;
    }

    /**
     *
     * @return a 'Mutex' for managing inter process communication a.k.a IPC
     */
    public static Semaphore getMutex() {
        return mutex;
    }

    /**
     *
     * @param mutex a 'Mutex' for managing inter process communication a.k.a IPC
     */
    public static void setMutex(Semaphore mutex) {
        MyWorkstationWidget.mutex = mutex;
    }

    /**
     *
     * @return the current instance of the workstation widget
     */
    public MyWorkstationWidget getInstance() {
        return instance;
    }

    /**
     *
     * @param instance the current instance of the workstation widget
     */
    public void setInstance(MyWorkstationWidget instance) {
        this.instance = instance;
    }

    /**
     * get from the radio button dialog box the user's choice of the waiting
     * queue size of the workstation
     *
     */
    public void initializeWaitingQueueForWorkstation() {
        //call another thread to wait for user input while the dialog is shown
        //we need this thread so that the GUI is not blocked
        new AsynchrnousDialogBoxRadioButtonsResultConsumer().start();
    }

    /**
     * get from the radio button dialog box the user's choice of the waiting
     * queue size of the workstation
     *
     */
    public void initializeOperationsForWorkstation() {
        //call another thread to wait for user input while the dialog is shown
        //we need this thread so that the GUI is not blocked
        new AsynchrnousDialogBoxCheckBoxResultsConsumer().start();
    }

    /**
     * a thread that is called outside of the GUI thread for the initialization
     * of the waiting queue size of the workstation
     *
     */
    private class AsynchrnousDialogBoxRadioButtonsResultConsumer extends Thread {

        /**
         * called when the thread is started
         *
         */
        @Override
        public void run() {
            //initialize dialog that will ask the user to chose from different radio buttons
            DialogBoxRadioButtons dialog;
            //initialize the waiting queue to -1 to know if the field has been updated from the dialog or not
            parentWorkstationProperties.setWaitingQueueMaximumSize(-1);
            //instanciate our possible choices that the user will chose from
            List<String> possibilities = new ArrayList<>();
            possibilities.add("0");
            possibilities.add("1");
            possibilities.add("2");
            possibilities.add("3");
            //initialize the answers that we want for the preceding choices
            List<String> answers = new ArrayList<>();
            answers.add("0");
            answers.add("1");
            answers.add("2");
            answers.add("3");
            //instanciate the dialog using the title of he window, the description,the choices for the user, the answer related
            //to these choices (the data that we need in return)
            dialog = new DialogBoxRadioButtons("Size of the workstation Queue", "Please select the Size of the workstation's Queue?",
                    possibilities, answers);

            //while the user did not change the field; he did not make a choice
            while (parentWorkstationProperties.getWaitingQueueMaximumSize() == -1) {
                try {
                    //read if there is an answer from the dialog
                    //until the user releases the lock of the semaphore
                    //when that happens, we have the choice data of the user
                    String result = dialog.getAnswer();
                    //block waiting for the user choice
                    MyWorkstationWidget.semaphore.acquire();
                    mutex.acquire();
                    //update the field value using the user choice data
                    parentWorkstationProperties.setWaitingQueueMaximumSize(Integer.parseInt(result));
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
     * a thread that is called outside of the GUI thread for the initialization
     * of the operations of the workstation
     *
     */
    private class AsynchrnousDialogBoxCheckBoxResultsConsumer extends Thread {

        @Override
        public void run() {
            //initialize dialog that will ask the user to chose from different radio buttons
            DialogBoxCheckBoxes dialog;
            //operations for the workstations is now configured during each add of a workstation

            //instanciate the dialog using the title of he window, the description,the choices for the user, the answer related
            //to these choices (the data that we need in return)
            dialog = new DialogBoxCheckBoxes("Operations of this Workstation", "Please select Operations for this Workstation?",
                    operationsPossibilities, operationsAnswerPossibilities);
            List<String> results;
            //while the user did not change the field; he did not make a choice
            while (parentWorkstationProperties.getOperationsOfThisMachine().isEmpty()) {
                try {
                    //read if there is an answer from the dialog
                    //until the user releases the lock of the semaphore
                    //when that happens, we have the choices data of the user
                    results = dialog.getResults();
                    //block waiting for the user choice
                    semaphore.acquire();
                    mutex.acquire();
                    //convert to integer values
                    List<Integer> operations = new ArrayList<>();
                    List<Integer> durations = new ArrayList<>();
                    for (String result : results) {
                        //add operation
                        operations.add(Integer.parseInt(result));
                        //add duration
                        durations.add(0);

                    }
                    //update the field value using the user choices data
                    parentWorkstationProperties.setOperationsOfThisMachine(operations);
                    parentWorkstationProperties.setDurationsOfOperations(durations);
                    //release the lock
                    mutex.release();
                    //stop the block waiting
                    semaphore.release();
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
     *
     * @return a list of the connectors of the workstation
     */
    public List<MyConnectorWidget> getConnections() {
        return connections;
    }

    /**
     *
     * @param connections a list of the connectors of the workstation
     */
    public void setConnections(List<MyConnectorWidget> connections) {
        this.connections = connections;
    }

}
