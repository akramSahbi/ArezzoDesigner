package fr.arezzo.designer.DomainWidgets;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.NodeOperation;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import fr.arezzo.designer.Dialogs.DialogBoxes.DialogBoxRadioButtons;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfTypeShuttle;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;

import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.WidgetType;
import fr.arezzo.designer.EditeurModule.Repositories.ShuttleRepository;
import fr.arezzo.designer.Scene.Scene;
import static fr.arezzo.designer.Scene.Scene.N;
import static fr.arezzo.designer.Scene.Scene.connectionLayer;
import fr.arezzo.designer.palette.ShapeNode;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.LabelWidget;

/**
 * PropertiesOfNodesOfTypeShuttle represents the shuttle Node in the scene
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class MyShuttleWidget {

    //IPC semaphores
    public static Semaphore semaphore = new Semaphore(1);
    public static Semaphore mutex = new Semaphore(1);

    //scene represents the place that will contain this widget
    public static Scene scene;
    //PropertiesOfNodesOfTypeShuttle represents the properties of the shuttles (nodes of type 99)
    private PropertiesOfNodesOfTypeShuttle shuttleProperties;
    //represents the icon that will be placed inside the scene
    private IconNodeWidget widget = null;
    //current instance
    private MyShuttleWidget instance = null;
    //connections (links) widget
    private List<MyConnectorWidget> connections = new ArrayList<>();
    private ShapeNode shapeNodeShuttle = null;
    //the widet editor action to edit the name of the widget on the scene
    public WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
    
    

    /**
     * MyShuttleWidget constructor which is initialized using a reference to the
     * scene, and the shape of the widget that is the icon inside the palette
     * the role of the constructor is to create the shuttle widget inside the
     * scene and add to it its actions (moving action, hover action, properties
     * menu, delete menu
     *
     * @param scene is the component that contains all of the widgets
     * @param n the shape (image) of the node(shuttle)
     */
    public MyShuttleWidget(Scene scene, ShapeNode n) {
        instance = MyShuttleWidget.this;
        shapeNodeShuttle = n;
        //initialize the domain class which contain the properties of the widget
        shuttleProperties = new PropertiesOfNodesOfTypeShuttle(scene);
        //initialize the number of the widget (ID)
        shuttleProperties.setNumber(WidgetCommonInfo.getNumberOfNextWidget());
        shuttleProperties.setID(shuttleProperties.getNumber());
        //initialize our instance of the scene for direct access
        MyShuttleWidget.scene = scene;
        //initialize the widget which is a genereic IconWidget that will be added to the scene
        widget = new IconNodeWidget(scene);
        //initialize the widget image using the shape image (from the palette)
        widget.setImage(ImageUtilities.loadImage(n.getShape().getImage()));
        //initialize the label of the widget
        //with its number
        widget.setLabel(shuttleProperties.getNumber() + "");
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
                //initialize N using current shapeNode of this instance not using the scene
                //N = (ShapeNode) sceneConstantRemoteAcess.findObject(widget);  
                N = getShapeNodeShuttle();
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
                if (N.getShape().getType().equals("99")) {
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
                                    shuttleProperties.setxCoordinate( x);
                                    shuttleProperties.setyCoordinate( y);

                                    //create the properties editor sheet
                                    Sheet sheet = super.createSheet();

                                    //create a set that the properties editor will contain
                                    Sheet.Set set = Sheet.createPropertiesSet();
                                    //we fill the sheet with all of the properties of the domain class of the widget
                                    //since the widget is a type 3 node, its properties will contain all of the
                                    //attributes of the domain class "PropertiesOfNodesOfTypeShuttle" properties 
                                    set.put(new PropertiesOfNodesOfTypeShuttle.NumberProperty(shuttleProperties));
                                    set.put(new PropertiesOfNodesOfTypeShuttle.XCoordinateProperty(shuttleProperties));
                                    set.put(new PropertiesOfNodesOfTypeShuttle.YCoordinateProperty(shuttleProperties));
                                    set.put(new PropertiesOfNodesOfTypeShuttle.TypeProperty(shuttleProperties));
                                    set.put(new PropertiesOfNodesOfTypeShuttle.SizeInDMProperty(shuttleProperties));
                                    set.put(new PropertiesOfNodesOfTypeShuttle.StartNodeNumberProperty(shuttleProperties));
                                    set.put(new PropertiesOfNodesOfTypeShuttle.EndNodeNumberProperty(shuttleProperties));
                                    set.put(new PropertiesOfNodesOfTypeShuttle.DistanceFromStartingNodeProperty(shuttleProperties));
                                    set.put(new PropertiesOfNodesOfTypeShuttle.SpeedsToNextNodesProperty(shuttleProperties));
                                    set.put(new PropertiesOfNodesOfTypeShuttle.NumbersOfNextNodesProperty(shuttleProperties));
                                    //speed to next nodes one by one to edit
                                    int nextNodeIndex = 0;
                                    if (MyShuttleWidget.this.getParentShuttleProperties().getNumbersOfNextNodes() != null
                                            && MyShuttleWidget.this.getParentShuttleProperties().getNumbersOfNextNodes().size() > 0) {
                                        for (Integer nextNodeNumber : MyShuttleWidget.this.getParentShuttleProperties().getNumbersOfNextNodes()) {
                                            //NextNodeSpeedProperty
                                            set.put(new PropertiesOfNodesOfTypeShuttle.NextNodeSpeedProperty(getParentShuttleProperties(),
                                                    nextNodeNumber, nextNodeIndex));
                                            nextNodeIndex++;
                                        }
                                    }

                                    sheet.put(set);

                                    return sheet;

                                }
                            };
                            //add labels for the properties editor of the widget
                            node.setDisplayName("Properties of the Shuttle");
                            node.setShortDescription("Properties of the Shuttle");

                            //show the properties editor of the widget when the properties menu item is cliked
                            NodeOperation.getDefault().showProperties(node);
                        }
                    });
                }
                //create the popup menu that will contain all of the menu items of this widget
                JPopupMenu popup = new JPopupMenu();
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

                ActionListener cutActionListener = new MyShuttleWidget.PopupCutActionListener(WidgetCommonInfo.WidgetType.SHUTTLE, instance, N);
                cutMenuItem.addActionListener(cutActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                JMenuItem copyMenuItem = new JMenuItem("Copy");
                copyMenuItem.setActionCommand((String) TransferHandler.getCopyAction().
                        getValue(Action.NAME));

                ActionListener copyActionListener = new MyShuttleWidget.PopupCopyActionListener(WidgetCommonInfo.WidgetType.SHUTTLE, instance, N);
                copyMenuItem.addActionListener(copyActionListener);
                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);

                if (N.getShape().getType().equals("99")) {
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
     * PopupCopyActionListener to manage the copy action when clicked with the
     * mouse
     *
     */
    public class PopupCopyActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MyShuttleWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         * PopupCopyActionListener constructor
         *
         * @param type the type of the node
         * @param mySw the shuttle widget to copy
         * @param shapeNode the shape (image) of the node to copy
         */
        public PopupCopyActionListener(WidgetType type, MyShuttleWidget mySw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = mySw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Shuttle")) {
                shapeNode.getShape().setName("Copy of Shuttle " + shapeNode.getShape().getName());
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
            if (widgetType == WidgetType.SHUTTLE) {
                //copy the widget to the scene
                ccpWidget = MyShuttleWidget.this;
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
     * PopupCutActionListener to manage the cut action when clicked with the
     * mouse
     *
     */
    public class PopupCutActionListener implements ActionListener {

        private WidgetType widgetType = null;
        private MyShuttleWidget ccpWidget;
        private ShapeNode shapeNode = null;

        /**
         * PopupCutActionListener constructor
         *
         * @param type the type of the node
         * @param mySw the shuttle widget to cut
         * @param shapeNode the shape (image) of the node to cut
         */
        public PopupCutActionListener(WidgetType type, MyShuttleWidget mySw, ShapeNode shapeNode) {
            widgetType = type;
            ccpWidget = mySw;
            if (!shapeNode.getShape().getName().startsWith("Copy of Shuttle")) {
                shapeNode.getShape().setName("Copy of Shuttle " + shapeNode.getShape().getName());
            }

            this.shapeNode = shapeNode;
        }

        /**
         * the action that is performed when the cut pop up menu item is clicked
         *
         * @param actionEvent the event that is triggered
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (widgetType == WidgetType.SHUTTLE) {
                //copy it to the scene
                ccpWidget = MyShuttleWidget.this;
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
     * MyConnectProvider class provides connectivity for this widget with other
     * widgets
     */
    public class MyConnectProvider implements ConnectProvider {

        /**
         * verify that the source widget can support connectivity with other
         * widgets
         *
         * @param source the source widget during the linking process
         * @return validates whether the source widget can be used during this
         * linking process
         */
        @Override
        public boolean isSourceWidget(Widget source) {
            //verify that the widget is an IconNodeWidget and that it is instanciated
            return ((source != null) && source instanceof IconNodeWidget);
        }

        /**
         * verify that the target widget can support connectivity with other
         * widgets
         *
         * @param src the source widget during the linking process
         * @param trg the target widget during the linking process
         * @return the state of the connector by validating or rejecting the
         * linking
         */
        @Override
        public ConnectorState isTargetWidget(Widget src, Widget trg) {
            //verify that the source widget is different from the target widget and that the target widget is
            // an IconNodeWidget also
            return src != trg && trg instanceof IconNodeWidget ? ConnectorState.ACCEPT : ConnectorState.REJECT;
        }

        /**
         * create the connection between the source widget and the target widget
         *
         * @param source the source widget during the linking process
         * @param target the target widget during the linking process
         */
        @Override
        public void createConnection(Widget source, Widget target) {
            //instanciate our connector widget that will link the source and target widget
            MyConnectorWidget conn = new MyConnectorWidget(scene);
            //set the shape of the connector widget to an arrow
            conn.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
            //set the target anchor of the connector widget to the target widget
            conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(target));
            //initialize the source and target nodes to our connector widget for future use
            ShapeNode sourceNode = (ShapeNode) scene.findObject(source);
            ShapeNode targetNode = (ShapeNode) scene.findObject(target);

            //set the source anchor to our source widget
            conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(source));
            //add the connection widget to the connection layer
            connectionLayer.addChild(conn);
        }

        /**
         * Called to check whether the provider has a custom target widget
         * resolver.
         *
         * @param scene the the scene where the resolver will be called
         * @return if true, then the resolveTargetWidget method is called for
         * resolving the target widget; if false, then the isTargetWidget method
         * is called for resolving the target widget
         */
        @Override
        public boolean hasCustomTargetWidgetResolver(org.netbeans.api.visual.widget.Scene scene) {
            return false;
        }

        /**
         * Called to find the target widget of a possible connection. Called
         * only when a hasCustomTargetWidgetResolver returns true.
         *
         * @param scene the scene component that contains all of the widgets
         * @param point the scene location of the widget
         * @return
         */
        @Override
        public Widget resolveTargetWidget(org.netbeans.api.visual.widget.Scene scene, Point point) {
            return null;
        }

    }

    /**
     * PopupDeleteActionListener is the class that manages the deletion of a
     * shuttle
     *
     */
    public class PopupDeleteActionListener implements ActionListener {

        /**
         * the action that is performed when the delete pop up menu item is
         * clicked from the shuttle menu
         *
         * @param actionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            //remove all of the shuttles related to this connection from the parent connections
            for (MyConnectorWidget connection : getConnections()) {

                connection.removeChild(MyShuttleWidget.this.getWidget());
                //remove from repository
                ShuttleRepository.getInstance().remove(MyShuttleWidget.this.getShuttleProperties().getNumber());

            }

        }

    }

    /**
     *
     * @return the scene component that contains all of the widgets
     */
    public static Scene getScene() {
        return scene;
    }

    /**
     *
     * @param scene the scene component that contains all of the widgets
     */
    public static void setScene(Scene scene) {
        MyShuttleWidget.scene = scene;
    }

    public PropertiesOfNodesOfTypeShuttle getParentShuttleProperties() {
        return shuttleProperties;
    }

    /**
     *
     * @param shuttleProperties the properties of the shuttle
     */
    public void setParentShuttleProperties(PropertiesOfNodesOfTypeShuttle shuttleProperties) {
        this.shuttleProperties = shuttleProperties;
    }

    /**
     *
     * @return the semaphore that is used during inter process communication IPC
     */
    public static Semaphore getSemaphore() {
        return semaphore;
    }

    /**
     *
     * @param semaphore the semaphore that is used during inter process
     * communication IPC
     */
    public static void setSemaphore(Semaphore semaphore) {
        MyShuttleWidget.semaphore = semaphore;
    }

    /**
     *
     * @return the 'Mutex' that is used during inter process communication IPC
     */
    public static Semaphore getMutex() {
        return mutex;
    }

    /**
     *
     * @param mutex the 'Mutex' that is used during inter process communication
     * IPC
     */
    public static void setMutex(Semaphore mutex) {
        MyShuttleWidget.mutex = mutex;
    }

    /**
     *
     * @return the instance of the current shuttle
     */
    public MyShuttleWidget getInstance() {
        return instance;
    }

    /**
     *
     * @param instance the instance of the current shuttle
     */
    public void setInstance(MyShuttleWidget instance) {
        this.instance = instance;
    }

    /**
     * initializeSizeForShuttle is called to get from the message dialog the
     * user's choice of the size of the shuttle
     *
     */
    public void initializeSizeForShuttle() {
        //call another thread to wait for user input while the dialog is shown
        //we need this thread so that the GUI is not blocked
        new AsynchrnousDialogBoxRadioButtonsResultConsumer().start();
    }

    /**
     * AsynchrnousDialogBoxRadioButtonsResultConsumer is a thread that is used
     * to get from the message dialog the user's choice of size of the shuttle
     */
    private class AsynchrnousDialogBoxRadioButtonsResultConsumer extends Thread {

        @Override
        public void run() {
            //initialize dialog that will ask the user to chose from different radio buttons
            DialogBoxRadioButtons dialog;
            //initialize the size of the shuttle to -1 to know if the field has been updated from the dialog or not
            shuttleProperties.setSizeInDM(-1);
            //instanciate our possible choices that the user will chose from
            List<String> possibilities = new ArrayList<>();
            possibilities.add("1");
            possibilities.add("2");
            possibilities.add("3");
            possibilities.add("4");
            possibilities.add("5");
            possibilities.add("6");
            possibilities.add("Cancel");
            //initialize the answers that we want for the preceding choices
            List<String> answers = new ArrayList<>();
            answers.add("1");
            answers.add("2");
            answers.add("3");
            answers.add("4");
            answers.add("5");
            answers.add("6");
            answers.add("0");
            //instanciate the dialog using the title of he window, the description,the choices for the user, the answer related
            //to these choices (the data that we need in return)
            dialog = new DialogBoxRadioButtons("Size of the Shuttle", "Please select the Size of the new Shuttle?",
                    possibilities, answers);

            //while the user did not change the field; he did not make a choice
            while (shuttleProperties.getSizeInDM() == -1) {
                try {
                    //read if there is an answer from the dialog
                    //until the user releases the lock of the semaphore
                    //when that happens, we have the choice data of the user
                    String result = dialog.getAnswer();
                    //block waiting for the user choice
                    MyShuttleWidget.semaphore.acquire();
                    mutex.acquire();
                    //update the field value using the user choice data
                    shuttleProperties.setSizeInDM(Integer.parseInt(result));
                    //release the lock
                    mutex.release();
                    //stop the block waiting
                    MyShuttleWidget.semaphore.release();
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
            
            return MyShuttleWidget.this.getShuttleProperties().getNumber()+"";
            
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
                MyShuttleWidget.this.getShuttleProperties().setNumber(newNumber);
                if(newNumber instanceof Integer)
                {
                    ((LabelWidget) widget).setLabel(newNumber+"");
                }
                
            }
            catch(Exception e)
            {
                
            }
            
        }
    }
    
    /**
     *
     * @return the Icon representing the node
     */
    public IconNodeWidget getWidget() {
        return widget;
    }

    /**
     *
     * @param widget the Icon representing the node
     */
    public void setWidget(IconNodeWidget widget) {
        this.widget = widget;
    }

    /**
     *
     * @return the properties of the shuttle
     */
    public PropertiesOfNodesOfTypeShuttle getShuttleProperties() {
        return shuttleProperties;
    }

    /**
     *
     * @param shuttleProperties the properties of the shuttle
     */
    public void setShuttleProperties(PropertiesOfNodesOfTypeShuttle shuttleProperties) {
        this.shuttleProperties = shuttleProperties;
    }

    /**
     *
     * @return list of connectors of the shuttle
     */
    public List<MyConnectorWidget> getConnections() {
        return connections;
    }

    /**
     *
     * @param connections list of connectors of the shuttle
     */
    public void setConnections(List<MyConnectorWidget> connections) {
        this.connections = connections;
    }

    /**
     *
     * @return the shape(image) of the shuttle
     */
    public ShapeNode getShapeNodeShuttle() {
        return shapeNodeShuttle;
    }

    /**
     *
     * @param shapeNodeShuttle the shape(image) of the shuttle
     */
    public void setShapeNodeShuttle(ShapeNode shapeNodeShuttle) {
        this.shapeNodeShuttle = shapeNodeShuttle;
    }

}
