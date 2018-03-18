package fr.arezzo.designer.Scene;

import java.awt.AWTException;
import fr.arezzo.designer.palette.ShapeNode;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.graph.GraphScene;

import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Exceptions;

import org.openide.util.ImageUtilities;

import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import fr.arezzo.designer.DomainWidgets.types3.MyLoadUnloadWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyLoadUnloadSensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MySensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyStopSensorWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchInputWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyTopologyNodesWidget;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchIntermediateWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchOutputWidget;
import fr.arezzo.designer.Dialogs.DialogOutputMessages.Alert;
import fr.arezzo.designer.DomainWidgets.MyShuttleWidget;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.AskUserToInitializeWidgetPropertiesNow;
import static fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget.operationsAnswerPossibilities;
import static fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget.operationsPossibilities;
import fr.arezzo.designer.EditeurModule.Repositories.LoadUnloadSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.LoadUnloadWorkstationRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchStopSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchInputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchIntermediateRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchOutputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.WorkstationRepository;
import fr.arezzo.designer.palette.PaletteSupport;
import fr.arezzo.designer.palette.Shape;
import java.io.Serializable;

/**
 * Scene represents the scene container where we find all of the widgets of the
 * network topology it is of type GraphScene with Nodes of type ShapeNode
 *
 *
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class Scene extends GraphScene<ShapeNode, String> implements Serializable
{

    //layer of the scene which will contain all of our widgets
    public static transient LayerWidget mainLayer;
    //layer of the scene which will contain all of the widgets connections
    public static transient LayerWidget connectionLayer;
    //layer of the scene which will contain all of the widgets interactions
    public static transient LayerWidget interactionLayer;
    //the widet editor action to edit the name of the widget on the scene
   // public WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
    //variable that will contain currently used Shape Node in the scene
    public static ShapeNode N;
    //variable for the currectly used connector of widgets
    public static MyConnectorWidget C;
    //variable to access the current instance of the scene outside of  this class
    public static Scene globalScene;
    //cut or copy or paste widget
    public static Object ccpWidget = null;
    //cut or copy or paste ShapeNode
    public static ShapeNode ccpShapeNode = null;
    //cut or copy or paste location
    public static Point pastePosition = null;
    //widget location
    public static Point widgetPosition = null;
    //pasted or not boolean variable
    private boolean pasted = false;
    //CopiedOrCutted boolean variable
    private boolean CopiedOrCutted = false;
    private Map<Widget, Object> myWidgetsAdded = new HashMap<>();

    /**
     * Constructor
     *
     */
    public Scene() {

        //the possible choices of operations of the workstation for load/unload initialized for the user
        operationsPossibilities = new ArrayList<>();
        operationsPossibilities.add("Load");
        operationsPossibilities.add("Unload");

//        the result needed depending on the user choice 
//        choice 1 => answer possible 1 anso so on
        operationsAnswerPossibilities = new ArrayList<>();
        operationsAnswerPossibilities.add("100");
        operationsAnswerPossibilities.add("101");

        //initialize the fields of the scene
        globalScene = Scene.this;
        mainLayer = new LayerWidget(this);
        connectionLayer = new LayerWidget(this);
        interactionLayer = new LayerWidget(this);
        //add the layers of the scene
        addChild(mainLayer);
        addChild(connectionLayer);
        addChild(interactionLayer);

        /*
         add widgets to the scene automatically for example after reading a file
         //Widget w1 = addNode(new MyNode(ImageUtilities.loadImage("tn/esprit/editeurtest/resources/workstation.png", true),"A"));
         //w1.setPreferredLocation(new Point(10, 100));
        
         //Widget w2 = addNode(new MyNode(ImageUtilities.loadImage("tn/esprit/editeurtest/resources/switch.png", true),"B"));
         //w2.setPreferredLocation(new Point(100, 250));
         */
        //add the zoom action to the scene which will add a small screen containing a zoom out of the scene
        //at the left bottom side of the screen
        getActions().addAction(ActionFactory.createZoomAction());
        //add zoom scroll
        getActions().addAction(ActionFactory.createPanAction());
        //multi selection action
        getActions().addAction(ActionFactory.createRectangularSelectAction(Scene.this, connectionLayer));

        //add a dragged shape to the scene action 
        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {
            //validate the state
            @Override
            public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
                try {

                    //get the shape that has been dragged to the scene
                    Shape shape = (Shape) transferable.getTransferData(PaletteSupport.ShapeNodeFlavor);
                    //draw the image of the scene
                    Image dragImage = ImageUtilities.loadImage(shape.getImage());
                    //get the view component of the scene
                    JComponent view = getView();
                    //get the graphics of the view of the scene
                    Graphics2D g2 = (Graphics2D) view.getGraphics();
                    //get the visible rectangle of the view of the scene
                    Rectangle visRect = view.getVisibleRect();
                    //paint a filler rectangle to the view of the scene
                    view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
                    //draw using the graphics of the view of the scene a new graphic (our shape)
                    //using as input:
                    //the X coordinates of the point's location of the drop event
                    //the Y coordinates of the point's location of the drop event
                    g2.drawImage(dragImage, AffineTransform.getTranslateInstance(point.getLocation().getX(), point.getLocation().getY()), null);
                    //return that the drop to the scene has been validated
                    return ConnectorState.ACCEPT;
                } catch (UnsupportedFlavorException | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return null;
            }

            //this method is called before a drag and drop is validated
            //inputs are:
            //the widget of the drag and drop
            //the point where the drop took effect on the scene
            //the transferred data while the drag and drop is the shape of the widget (the shapeNode)
            @Override
            public void accept(Widget widget, Point point, Transferable transferable) {
                try {
                    if (!CopiedOrCutted) {
                        //create a new shape that has been transferred using the drag from the palette
                        Shape shape = (Shape) transferable.getTransferData(PaletteSupport.ShapeNodeFlavor);
                        //if there has been a copy/paste process
                        //reinitialize the name of the shape transferred as a new shape from the palette
                        if (shape.getName().startsWith("Copy of ")) {
                            //remove Copy of from the name of the shape
                            shape.setName(shape.getName().replace("Copy of ", ""));
                            //remove the name of the copied shape
                            String[] shapeNameStrings = shape.getName().split(" ");
                            shape.setName(shape.getName().replace(shapeNameStrings[0] + " ", ""));
                        }

                        //create a new generic widget with the new shape
                        //Widget w = Scene.this.addNode(new ShapeNode(shape));
                        Widget w = Scene.this.addNode(new ShapeNode(shape));
                        //set the location on the scene where the widget will be dropped and displayed
                        w.setPreferredLocation(widget.convertLocalToScene(point));
                    } else {
                        Alert.alert("Waring", "you need to paste first", Alert.AlertType.WARNING_MESSAGE);
                    }
                    Scene.globalScene.validate();
                    Scene.globalScene.repaint();

                } catch (UnsupportedFlavorException | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }));

        //getActions().addAction(ActionFactory.createZoomAction());
        //getActions().addAction(ActionFactory.createPanAction());
        //create the popup menu that will contain all of the menu items of this scene
        //cut/copy paste
        getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {
            @Override
            public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
                //initializing the x and y coordinates of the widget using the mouse click coordinates
                pastePosition = localLocation;

                //paste menu item creation
                JMenuItem pasteMenuItem = new JMenuItem("Paste");
                //set the paste command name
                pasteMenuItem.setActionCommand((String) TransferHandler.getPasteAction().
                        getValue(Action.NAME));
                //instanciate the paste action
                ActionListener pasteActionListener = new PopupPasteActionListener();
                //add the paste action to the paste menu item
                pasteMenuItem.addActionListener(pasteActionListener);
                pasteMenuItem.setEnabled(CopiedOrCutted);
                //if there is some shape to paste
                if (pasteMenuItem.isEnabled()) {
                    //get the shape to paste
                    Shape shape = ccpShapeNode.getShape();
                    //assign the image of the shape as the paste menu item icon
                    pasteMenuItem.setIcon(new ImageIcon(ImageUtilities.loadImage(shape.getImage())));
                }

                //cancel linking menu item creation
                JMenuItem cancelLinkMenuItem = new JMenuItem("Cancel Linking");

                //instanciate the paste action
                ActionListener cancelLinkingActionListener = new CancelLinkingActionListener();
                //add the cancel linking action to the cancel linking menu item
                cancelLinkMenuItem.addActionListener(cancelLinkingActionListener);
                cancelLinkMenuItem.setEnabled(MyConnectorWidget.currentlyLinking);

                //menuItem.setAccelerator(
                //KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                //menuItem.setMnemonic(KeyEvent.VK_T);
                //instanciate the scene popup menu
                JPopupMenu scenePopupMenu = new JPopupMenu();
                scenePopupMenu.add(cancelLinkMenuItem);
                //add the paste menu item to the scene popup menu
                scenePopupMenu.add(pasteMenuItem);
                return scenePopupMenu;
            }

        }));
        //TESTING information alert
        //Alert.alert("WECOME", "Welcome to AREZZO !", Alert.AlertType.INFORMATION_MESSAGE);

    }

    @Override
    protected Widget attachEdgeWidget(String e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void attachEdgeSourceAnchor(String e, ShapeNode n, ShapeNode n1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void attachEdgeTargetAnchor(String e, ShapeNode n, ShapeNode n1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    /**
     * method called to cancel the linking process
     */
    public void stopLinkingProcess() {
        if (MyConnectorWidget.currentlyLinking == true) {
            //end of the mouse and keyboard simulation for linking
            Robot robot;
            try {
                robot = new Robot();

                //simulate the ctrl button release
                robot.keyRelease(KeyEvent.VK_CONTROL);
                //simulate the mouse button release
                //robot.mouseRelease(InputEvent.BUTTON1_MASK );

            } catch (AWTException ex) {
                Exceptions.printStackTrace(ex);
            }
            MyConnectorWidget.currentlyLinking = false;
            validate();
            repaint();

        }
        MyConnectorWidget.currentlyLinking = false;

    }
    /**
     * helper class for canceling the linking action
     */
    public class CancelLinkingActionListener implements ActionListener {

        /**
         * the action that is performed when the cancel linking pop up menu item
         * is clicked in an empty location on the scene
         *
         * @param actionEvent the event triggered
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //end of the mouse and keyboard simulation for linking
            stopLinkingProcess();

        }
    }

    

    /**
     * helper class for pasting a widget inside the scene
     *
     */
    public class PopupPasteActionListener implements ActionListener {

        /**
         * the action that is performed when the paste pop up menu item is
         * clicked
         *
         * @param actionEvent the event trigger
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //if there is a copied or cutted widget from the scene
            if (ccpShapeNode != null && ccpWidget != null) {

                //add the new widget to the scene
                Widget w = addNode(new ShapeNode(ccpShapeNode.getShape()));
                if (pasted) {
                    //reinitialize ccpWiget to null to end the process of cut, copy paste
                    ccpWidget = null;
                    ccpShapeNode = null;
                }
                //reinitialize copy paste booleans
                pasted = CopiedOrCutted = false;
//                 N = (ShapeNode) findObject(w);
                //repaint the scene
                validate();
                repaint();
            }

        }

    }

    /**
     * attach a shape Node(icon) to the Scene (as a Widget Node that contains
     * all of the informations of our node)
     *
     * @param n the shape of the node
     * @return the widget added to the scene
     */
    @Override
    public Widget attachNodeWidget(ShapeNode n) {
        //get the type of the shape
        String shapeType = n.getShape().getType();
        //get the name of the shape
        String shapeName = n.getShape().getName();
        //the widget to be attached to the scene
        IconNodeWidget widget = new IconNodeWidget(this);
        N = n;

        /// if not cut/copy pasted widget
        if (ccpWidget == null) {
            //Alert.alert("not copy or paste", "not copy or paste", Alert.AlertType.INFORMATION_MESSAGE);
            //Alert.alert("shape informations", "shape name: " + shapeName + "\n"
            //        +"shape type: " + shapeType + "\n", Alert.AlertType.INFORMATION_MESSAGE);
            //workstation shape
            switch (shapeType) {
            //load/unload sensor shape
                case "3":
            switch (shapeName) {
                case "Workstation":
                    //only if shape is of type 3 and name is Workstation
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MyWorkstationWidget myWw = new MyWorkstationWidget(this, n);
                    //                    //initialize position of widget properties (x and y coordinates) based on current mouse position
//                    Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
//                    myWw.getParentWorkstationProperties().setxCoordinate( (float) mouseLocation.getX() - 20);
//                    myWw.getParentWorkstationProperties().setyCoordinate((float)mouseLocation.getY() - 84);
                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.WORKSTATION, myWw);
                    //update the widget with the user choices of properties of the widget
                    myWw = (MyWorkstationWidget) WidgetCommonInfo.getWidget();
                    widget.setLabel(myWw.getParentWorkstationProperties().getNumber() + "");
                    //assign the generic widget that has to be attached to the scene
                    widget = myWw.getWidget();
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, myWw);
                    //add to repository
                    WorkstationRepository.getInstance().add(myWw);
                    break;
                case "Load Unload workstation":
                    //only if shape is of type 3 and name is a Load Unload workstation
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MyLoadUnloadWorkstationWidget myLoadUnloadWorkstationWidget = new MyLoadUnloadWorkstationWidget(this, n);
                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.LOAD_UNLOAD_WORKSTATION, myLoadUnloadWorkstationWidget);
                    //update the widget with the user choices of properties of the widget
                    myLoadUnloadWorkstationWidget = (MyLoadUnloadWorkstationWidget) WidgetCommonInfo.getWidget();
                    //assign the generic widget that has to be attached to the scene
                    widget = myLoadUnloadWorkstationWidget.getWidget();
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, myLoadUnloadWorkstationWidget);
                    //add to repository
                    LoadUnloadWorkstationRepository.getInstance().add(myLoadUnloadWorkstationWidget);
                    break;
            }
            break;
                    //sensor shape
                case "4":
                    //only one load/unload sensor is of type 4
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MyLoadUnloadSensorWidget myloadUnloadWidget = new MyLoadUnloadSensorWidget(this, n);
                    //assign the generic widget that has to be attached to the scene
                    widget = myloadUnloadWidget.getWidget();
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, myloadUnloadWidget);
                    //add to repository
                    LoadUnloadSensorRepository.getInstance().add(myloadUnloadWidget);
                    break;
                    //stop sensor shape
                case "8":
                    //only  sensor is of type 8
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MySensorWidget sensorWidget = new MySensorWidget(this, n);
                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.SENSOR, sensorWidget);
                    //update the widget with the user choices of properties of the widget
                    sensorWidget = (MySensorWidget) WidgetCommonInfo.getWidget();
                    //assign the generic widget that has to be attached to the scene
                    widget = sensorWidget.getWidget();
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, sensorWidget);
                    //add to repository
                    SwitchSensorRepository.getInstance().add(sensorWidget);
                    break;
                    //topology node shape
                case "9":
                    //only stop sensor is of type 9
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MyStopSensorWidget stopSensorWidget = new MyStopSensorWidget(this, n);
                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.STOP_SENSOR, stopSensorWidget);
                    //update the widget with the user choices of properties of the widget
                    stopSensorWidget = (MyStopSensorWidget) WidgetCommonInfo.getWidget();
                    //assign the generic widget that has to be attached to the scene
                    widget = stopSensorWidget.getWidget();
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, stopSensorWidget);
                    //add to repository
                    SwitchStopSensorRepository.getInstance().add(stopSensorWidget);
                    break;
                case "6":
                {
                    //only topology node widget is of type 6
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MyTopologyNodesWidget topologyNodeWidget = new MyTopologyNodesWidget(this, n);
                        //assign the generic widget that has to be attached to the scene
                    widget = topologyNodeWidget.getWidget();
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, topologyNodeWidget);
                    break;
                } //switch input shape
            //
                case "10":
                    //only MySwitchInputWidget is of type 10
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MySwitchInputWidget switchInputWidget = new MySwitchInputWidget(this, n);
                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.SWITCH_INPUT, switchInputWidget);
                    //update the widget with the user choices of properties of the widget
                    switchInputWidget = (MySwitchInputWidget) WidgetCommonInfo.getWidget();
                    //assign the generic widget that has to be attached to the scene
                widget = switchInputWidget.getWidget();
                //map the widget to myWidget to access its properties from the connector widget using the widget key
                myWidgetsAdded.put(widget, switchInputWidget);
                //add to repository
                SwitchInputRepository.getInstance().add(switchInputWidget);
                break;
                case "11":
                    //only MySwitchIntermediateWidget is of type 11
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MySwitchIntermediateWidget switchIntermediateWidget = new MySwitchIntermediateWidget(this, n);
                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.SWITCH_INTERMEDATE, switchIntermediateWidget);
                    //update the widget with the user choices of properties of the widget
                    switchIntermediateWidget = (MySwitchIntermediateWidget) WidgetCommonInfo.getWidget();
                    //assign the generic widget that has to be attached to the scene
                widget = switchIntermediateWidget.getWidget();
                //map the widget to myWidget to access its properties from the connector widget using the widget key
                myWidgetsAdded.put(widget, switchIntermediateWidget);
                //add to repository
                SwitchIntermediateRepository.getInstance().add(switchIntermediateWidget);
                break;
                case "12":
                    //only MySwitchOutputWidget is of type 12
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MySwitchOutputWidget switchOutputWidget = new MySwitchOutputWidget(this, n);
                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.SWITCH_OUTPUT, switchOutputWidget);
                    //update the widget with the user choices of properties of the widget
                    switchOutputWidget = (MySwitchOutputWidget) WidgetCommonInfo.getWidget();
                    //assign the generic widget that has to be attached to the scene
                widget = switchOutputWidget.getWidget();
                //map the widget to myWidget to access its properties from the connector widget using the widget key
                myWidgetsAdded.put(widget, switchOutputWidget);
                //add to repository
                SwitchOutputRepository.getInstance().add(switchOutputWidget);
                break;
                    //if adapter for connectors
                case "99":
                    //testing all of the alert types
                    //Alert.alert("testing alert error", "Akram did not finished the shuttle widget", Alert.AlertType.ERROR_MESSAGE);
                    //Alert.alert("testing alert information", "Akram did not finished the shuttle widget", Alert.AlertType.INFORMATION_MESSAGE);
                    //Alert.alert("testing alert plain message", "Akram did not finished the shuttle widget", Alert.AlertType.PLAIN_MESSAGE);
                    //Alert.alert("testing alert question message", "Akram did not finished the shuttle widget? ", Alert.AlertType.QUESTION_MESSAGE);
                    //Alert.alert("testing alert WARNING MESSAGE", "Akram did not finished the shuttle widget yet ! ", Alert.AlertType.WARNING_MESSAGE);
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MyShuttleWidget shuttleWidget = new MyShuttleWidget(this, n);
                    //assign the generic widget that has to be attached to the scene
                    widget = shuttleWidget.getWidget();
                    //initialize the widget image using the shape image (from the palette)
                    widget.setImage(ImageUtilities.loadImage(n.getShape().getImage()));
                    //initialize the label of the widget
                    widget.setLabel(n.getShape().getName());
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, shuttleWidget);
                    break;
                case "999":
                {
                    MyTopologyNodesWidget topologyNodeWidget = new MyTopologyNodesWidget(this, n);
                    //assign the generic widget that has to be attached to the scene
                    widget = topologyNodeWidget.getWidget();
                    //ALREADY RESIZED A NEW IMAGE for the adapter
                    //resize the adapter widget
                //widget.setPreferredSize(new Dimension(10, 10));
                    repaint();
                    //widget.setMaximumSize(new Dimension(3, 3));
                //widget.getLabelWidget().removeChildren();
                //map the widget to myWidget to access its properties from the connector widget using the widget key
                myWidgetsAdded.put(widget, topologyNodeWidget);
                        break;
                    }
                default:
                    Alert.alert("shape error", "shape type not recognized!", Alert.AlertType.ERROR_MESSAGE);
                    break;
            }
        } //if cutted or copied widget
        else {
            //Alert.alert("copy or paste", "copy or paste", Alert.AlertType.INFORMATION_MESSAGE);
            //workstation type shape
            if (shapeType.equals("3")) {
                //workstation shape
                if (shapeName.startsWith("Copy of Workstation")) {
                    //only if shape is of type 3 and name is Workstation
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
//                    MyWorkstationWidget myWw = new MyWorkstationWidget(this, n);
//                    // fill the properties of the copied or cutted widget 
//                    myWw.setParentWorkstationProperties(((MyWorkstationWidget) ccpWidget).getParentWorkstationProperties());
//                    //assign new number for the copied widget
//                    myWw.getParentWorkstationProperties().setNumber(WidgetCommonInfo.getNumberOfNextWidget());

                    //BUG fix (do not clone an existing widget because we get 2 widgets that will always have the same values)
                    MyWorkstationWidget myWw = new MyWorkstationWidget(this, n, (MyWorkstationWidget) ccpWidget);

                    //initialize the connections of My new Widget
                    myWw.setConnections(new ArrayList<MyConnectorWidget>());
                    //assign the generic widget that has to be attached to the scene
                    widget = myWw.getWidget();
                    widget.setPreferredLocation(pastePosition);
                    //reinitialize copied or cutted widget
                    //ccpWidget = null;
                    pasted = true;
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, myWw);

                    //add to repository
                    WorkstationRepository.getInstance().add(myWw);
                } //load unload workstation shape
                else if (shapeName.startsWith("Copy of Load Unload Workstation")) {
                    //only if shape is of type 3 and name begins withCopy of  Load Unload Workstation
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
//                    MyLoadUnloadWorkstationWidget myLUWw = new MyLoadUnloadWorkstationWidget(this, n);
//                    // fill the properties of the copied or cutted widget 
//                    myLUWw.setLoadUnloadWorkstationProperties(((MyLoadUnloadWorkstationWidget) ccpWidget).getLoadUnloadWorkstationProperties());
//                    //assign new number for the copied widget
//                    myLUWw.getLoadUnloadWorkstationProperties().setNumber(WidgetCommonInfo.getNumberOfNextWidget());

                    //BUG fix (do not clone an existing widget because we get 2 widgets that will always have the same values)
                    MyLoadUnloadWorkstationWidget myLUWw = new MyLoadUnloadWorkstationWidget(this, n, (MyLoadUnloadWorkstationWidget) ccpWidget);

                    //initialize the connections of My new Widget
                    myLUWw.setConnections(new ArrayList<MyConnectorWidget>());
                    //assign the generic widget that has to be attached to the scene
                    widget = myLUWw.getWidget();

                    widget.setPreferredLocation(pastePosition);
                    //reinitialize copied or cutted widget
                    //ccpWidget = null;
                    pasted = true;
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, myLUWw);

                    //add to repository
                    LoadUnloadWorkstationRepository.getInstance().add(myLUWw);
                } else {
                    pasted = false;
                    Alert.alert("Waring", "you need to paste first", Alert.AlertType.WARNING_MESSAGE);
                }
            } else if (shapeType.equals("4")) {
                //load unload workstation shape
                if (shapeName.startsWith("Copy of Load Unload Sensor")) {
                    //only if shape is of type 4 and the name begins with Copy of Load Unload Sensor
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
//                    MyLoadUnloadSensorWidget myLUWw = new MyLoadUnloadSensorWidget(this, n);
//                    // fill the properties of the copied or cutted widget 
//                    myLUWw.setLoadUnloadSensorProperties(((MyLoadUnloadSensorWidget) ccpWidget).getLoadUnloadSensorProperties());
//                    
                    //BUG fix (do not clone an existing widget because we get 2 widgets that will always have the same values)
                    MyLoadUnloadSensorWidget myLUWw = new MyLoadUnloadSensorWidget(this, n, (MyLoadUnloadSensorWidget) ccpWidget);

                    //initialize the connections of My new Widget
                    myLUWw.setConnections(new ArrayList<MyConnectorWidget>());
//                    //assign new number for the copied widget
//                    myLUWw.getLoadUnloadSensorProperties().setNumber(WidgetCommonInfo.getNumberOfNextWidget());
                    //assign the generic widget that has to be attached to the scene
                    widget = myLUWw.getWidget();

                    widget.setPreferredLocation(pastePosition);
                    //reinitialize copied or cutted widget
                    //ccpWidget = null;
                    pasted = true;
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, myLUWw);

                    //add to repository
                    LoadUnloadSensorRepository.getInstance().add(myLUWw);
                } else {
                    pasted = false;
                    Alert.alert("Waring", "you need to paste first", Alert.AlertType.WARNING_MESSAGE);
                }
            } else if (shapeType.equals("8")) {
                //sensor shape
                if (shapeName.startsWith("Copy of Sensor")) {
                    //only if shape is of type 8
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
//                    MySensorWidget mySw = new MySensorWidget(this, n);
//                    // fill the properties of the copied or cutted widget 
//                    mySw.setSensorProperties(((MySensorWidget) ccpWidget).getSensorProperties());
//                    //assign new number for the copied widget
//                    mySw.getSensorProperties().setNumber(WidgetCommonInfo.getNumberOfNextWidget());

                    //BUG fix (do not clone an existing widget because we get 2 widgets that will always have the same values)
                    MySensorWidget mySw = new MySensorWidget(this, n, (MySensorWidget) ccpWidget);

                    //initialize the connections of My new Widget
                    mySw.setConnections(new ArrayList<MyConnectorWidget>());

                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.SENSOR, mySw);
                    //update the widget with the user choices of properties of the widget
                    mySw = (MySensorWidget) WidgetCommonInfo.getWidget();

                    //assign the generic widget that has to be attached to the scene
                    widget = mySw.getWidget();

                    widget.setPreferredLocation(pastePosition);
                    //reinitialize copied or cutted widget
                    //ccpWidget = null;
                    pasted = true;
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, mySw);

                    //add to repository
                    SwitchSensorRepository.getInstance().add(mySw);
                } else {
                    pasted = false;
                    Alert.alert("Waring", "you need to paste first", Alert.AlertType.WARNING_MESSAGE);
                }
            } else if (shapeType.equals("9")) {
                //stop sensor shape
                if (shapeName.startsWith("Copy of Stop Sensor")) {
                    //only if shape is of type 9
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
//                    MyStopSensorWidget mySsw = new MyStopSensorWidget(this, n);
//                    // fill the properties of the copied or cutted widget 
//                    mySsw.setStopSensorProperties(((MyStopSensorWidget) ccpWidget).getStopSensorProperties());
//                    //assign new number for the copied widget
//                    mySsw.getStopSensorProperties().setNumber(WidgetCommonInfo.getNumberOfNextWidget());

                    //BUG fix (do not clone an existing widget because we get 2 widgets that will always have the same values)
                    MyStopSensorWidget mySsw = new MyStopSensorWidget(this, n, (MyStopSensorWidget) ccpWidget);

                    //initialize the connections of My new Widget
                    mySsw.setConnections(new ArrayList<MyConnectorWidget>());

                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.STOP_SENSOR, mySsw);
                    //update the widget with the user choices of properties of the widget
                    mySsw = (MyStopSensorWidget) WidgetCommonInfo.getWidget();

                    //assign the generic widget that has to be attached to the scene
                    widget = mySsw.getWidget();

                    widget.setPreferredLocation(pastePosition);
                    //reinitialize copied or cutted widget
                    //ccpWidget = null;
                    pasted = true;
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, mySsw);

                    //add to repository
                    SwitchStopSensorRepository.getInstance().add(mySsw);
                } else {
                    pasted = false;
                    Alert.alert("Waring", "you need to paste first", Alert.AlertType.WARNING_MESSAGE);
                }
            } else if (shapeType.equals("10")) {
                //switch input shape
                if (shapeName.startsWith("Copy of Switch Input")) {
                    //only if shape is of type 10 and the name begins with Copy of Switch Input
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
//                    MySwitchInputWidget mySiw = new MySwitchInputWidget(this, n);
//                    // fill the properties of the copied or cutted widget 
//                    mySiw.setSwitchInputNodeProperties(((MySwitchInputWidget) ccpWidget).getSwitchInputNodeProperties());
//                    //assign new number for the copied widget
//                    mySiw.getSwitchInputNodeProperties().setNumber(WidgetCommonInfo.getNumberOfNextWidget());

                    //BUG fix (do not clone an existing widget because we get 2 widgets that will always have the same values)
                    MySwitchInputWidget mySiw = new MySwitchInputWidget(this, n, ((MySwitchInputWidget) ccpWidget));

                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.SWITCH_INPUT, mySiw);
                    //update the widget with the user choices of properties of the widget
                    mySiw = (MySwitchInputWidget) WidgetCommonInfo.getWidget();

                    //assign the generic widget that has to be attached to the scene
                    widget = mySiw.getWidget();

                    //initialize the connections of My new Widget
                    mySiw.setConnections(new ArrayList<MyConnectorWidget>());

                    widget.setPreferredLocation(pastePosition);
                    //reinitialize copied or cutted widget
                    //ccpWidget = null;
                    pasted = true;
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, mySiw);

                    //add to repository
                    SwitchInputRepository.getInstance().add(mySiw);
                } else {
                    pasted = false;
                    Alert.alert("Waring", "you need to paste first", Alert.AlertType.WARNING_MESSAGE);
                }
            } else if (shapeType.equals("11")) {
                //switch intermediate shape
                if (shapeName.startsWith("Copy of Switch Intermediate")) {
                    //only if shape is of type 11 and the name begins with Copy of Switch Intermediate
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    //MySwitchIntermediateWidget mySiw = new MySwitchIntermediateWidget(this, n);

                    // fill the properties of the copied or cutted widget 
                    //mySiw.setSwitchIntermediateNodeProperties(((MySwitchIntermediateWidget) ccpWidget).getSwitchIntermediateNodeProperties());
                    //BUG fix (do not clone an existing widget because we get 2 widgets that will always have the same values)
                    MySwitchIntermediateWidget mySiw = new MySwitchIntermediateWidget(this, n, ((MySwitchIntermediateWidget) ccpWidget));

                    //assign new number for the copied widget
                    mySiw.getSwitchIntermediateNodeProperties().setNumber(WidgetCommonInfo.getNumberOfNextWidget());

                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.SWITCH_INTERMEDATE, mySiw);
                    //update the widget with the user choices of properties of the widget
                    mySiw = (MySwitchIntermediateWidget) WidgetCommonInfo.getWidget();

                    //assign the generic widget that has to be attached to the scene
                    widget = mySiw.getWidget();

                    //initialize the connections of My new Widget
                    mySiw.setConnections(new ArrayList<MyConnectorWidget>());

                    widget.setPreferredLocation(pastePosition);
                    //reinitialize copied or cutted widget
                    //ccpWidget = null;
                    pasted = true;
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, mySiw);

                    //add to repository
                    SwitchIntermediateRepository.getInstance().add(mySiw);
                } else {
                    pasted = false;
                    Alert.alert("Waring", "you need to paste first", Alert.AlertType.WARNING_MESSAGE);
                }
            } else if (shapeType.equals("12")) {
                //switch intermediate shape
                if (shapeName.startsWith("Copy of Switch Output")) {
                    //only if shape is of type 12 and the name begins with Copy of Switch Output
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
//                    MySwitchOutputWidget mySow = new MySwitchOutputWidget(this, n);
//                    // fill the properties of the copied or cutted widget 
//                    mySow.setSwitchOutputNodeProperties(((MySwitchOutputWidget) ccpWidget).getSwitchOutputNodeProperties());
//                    //assign new number for the copied widget
//                    mySow.getSwitchOutputNodeProperties().setNumber(WidgetCommonInfo.getNumberOfNextWidget());
//                    
//                    //initialize the connections of My new Widget
//                    mySow.setConnections(new ArrayList<MyConnectorWidget>());

                    //BUG fix (do not clone an existing widget because we get 2 widgets that will always have the same values)
                    MySwitchOutputWidget mySow = new MySwitchOutputWidget(this, n, ((MySwitchOutputWidget) ccpWidget));
                    //initialize the connections of My new Widget
                    mySow.setConnections(new ArrayList<MyConnectorWidget>());
                    //ask the user if he want to fill the properties of the widget now
                    //inputs are: the type of the widget, the widget
                    new AskUserToInitializeWidgetPropertiesNow(WidgetCommonInfo.WidgetType.SWITCH_OUTPUT, mySow);
                    //update the widget with the user choices of properties of the widget
                    mySow = (MySwitchOutputWidget) WidgetCommonInfo.getWidget();

                    //assign the generic widget that has to be attached to the scene
                    widget = mySow.getWidget();

                    widget.setPreferredLocation(pastePosition);
                    //reinitialize copied or cutted widget
                    //ccpWidget = null;
                    pasted = true;
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, mySow);

                    //add to repository
                    SwitchOutputRepository.getInstance().add(mySow);
                } else {
                    pasted = false;
                    Alert.alert("Waring", "you need to paste first", Alert.AlertType.WARNING_MESSAGE);
                }
            } else if (shapeType.equals("99")) {
                //switch intermediate shape
                if (shapeName.startsWith("Copy of Shuttle")) {
                    //only if shape is of type 11 and the name begins with Copy of Switch Intermediate
                    //create a new widget depending on the typee of the shape from the palette
                    //inputs are the scene, the shape o the widget
                    MyShuttleWidget mySw = new MyShuttleWidget(this, n);
                    // fill the properties of the copied or cutted widget 
                    mySw.setParentShuttleProperties(((MyShuttleWidget) ccpWidget).getParentShuttleProperties());
                    //assign new number for the copied widget
                    mySw.getParentShuttleProperties().setNumber(WidgetCommonInfo.getNumberOfNextWidget());
                    //assign the generic widget that has to be attached to the scene
                    widget = mySw.getWidget();
                    //initialize the connections of My new Widget
                    mySw.setConnections(new ArrayList<MyConnectorWidget>());

                    widget.setPreferredLocation(pastePosition);
                    //reinitialize copied or cutted widget
                    //ccpWidget = null;
                    pasted = true;
                    //map the widget to myWidget to access its properties from the connector widget using the widget key
                    myWidgetsAdded.put(widget, mySw);
                } else {
                    pasted = false;
                    Alert.alert("Waring", "you need to paste first", Alert.AlertType.WARNING_MESSAGE);
                }
            }
        }
        if (widget != null) {

            mainLayer.addChild(widget);
            validate();

        }

        return widget;
    }

//    /**
//     * helper class for editing the label of the widgets at their footer on the
//     * scene
//     */
//    private class LabelTextFieldEditor implements TextFieldInplaceEditor, Serializable {
//
//        /**
//         * whether we can modify the label of a widget
//         *
//         * @param widget the widget that we want to modify its label
//         * @return
//         */
//        @Override
//        public boolean isEnabled(Widget widget) {
//            return true;
//        }
//
//        /**
//         * gets the label of the widget
//         *
//         * @param widget is the widget to get its name from the scene
//         * @return the name of the widget in the scene
//         */
//        @Override
//        public String getText(Widget widget) {
//            //Access shapeNode to reach its Shape that contains the type of the widget so that we can get its number and
//            //view it as the label
//            //transform the widget as a label widget
//            LabelWidget labelWidget = ((LabelWidget) widget);
//            labelWidget.setLabel("");
//            //find the ShapeNode relative to the widget
//            N = (ShapeNode) findObject(widget);
//            
//            Shape shape = N.getShape();
//            
//            
//            return labelWidget.getLabel();
//            //get the displayed name of the widget (as a LabelWidget) from its label
//            //return ((LabelWidget) widget).getLabel();
//            
//        }
//
//        /**
//         * set the name of the the widget
//         *
//         * @param widget the widget that has a label to be edited
//         * @param text the new text value that will become its new label
//         */
//        @Override
//        public void setText(Widget widget, String text) {
//            
//            //find the ShapeNode relative to the widget
//            N = (ShapeNode) findObject(widget);
//            //change the displayed name of the shapeeNode to the value of text
//            N.setDisplayName(text);
//            //change the name of the shapeNode to the value of text
//            N.getShape().setName(text);
//            //change the displayed name of the widget (as a LabelWidget) to the value of text
//            LabelWidget labelWidget = ((LabelWidget) widget);
//            
//            //find the ShapeNode relative to the widget
//            N = (ShapeNode) findObject(widget);
//            
//            Shape shape = N.getShape();
//            
//            
//            
//            WidgetCommonInfo.WidgetType type = shape.getShapeType();
//            //map the widget to myWidget to access its properties using the widget key
//            Object myWidget = myWidgetsAdded.get(widget);
//            
//            
//            Alert.alert(myWidget+"", myWidget+"", Alert.AlertType.INFORMATION_MESSAGE);
//            if(type == WidgetCommonInfo.WidgetType.WORKSTATION)
//            {
//                MyWorkstationWidget myWorkstation = (MyWorkstationWidget) myWidget;
//                //return myWorkstation.getParentWorkstationProperties().getNumber()+"";
//                myWorkstation.getParentWorkstationProperties().setNumber(Integer.parseInt(text));
//                labelWidget.setLabel(myWorkstation.getParentWorkstationProperties().getNumber()+"");
//            }
//            else if(type == WidgetCommonInfo.WidgetType.LINK_NODE)
//            {
//                MyConnectorWidget x  = (MyConnectorWidget) myWidget;
//                //return x.getMyConnectorInfo().getConnectorProperties().getNumber()+"";
//                x.getMyConnectorInfo().getConnectorProperties().setNumber(Integer.parseInt(text));
//                labelWidget.setLabel(x.getMyConnectorInfo().getConnectorProperties().getNumber()+"");
//            }
//            else if(type == WidgetCommonInfo.WidgetType.LOAD_UNLOAD_SENSOR)
//            {
//                MyLoadUnloadSensorWidget x  = (MyLoadUnloadSensorWidget) myWidget;
//                //return x.getLoadUnloadSensorProperties().getNumber()+"";
//                x.getLoadUnloadSensorProperties().setNumber(Integer.parseInt(text));
//                labelWidget.setLabel(x.getLoadUnloadSensorProperties().getNumber()+"");
//            }
//            else if(type == WidgetCommonInfo.WidgetType.SENSOR)
//            {
//                MySensorWidget x  = (MySensorWidget) myWidget;
//                //return x.getSensorProperties().getNumber()+"";
//                x.getSensorProperties().setNumber(Integer.parseInt(text));
//                labelWidget.setLabel(x.getSensorProperties().getNumber()+"");
//            }
//            else if(type == WidgetCommonInfo.WidgetType.SHUTTLE)
//            {
//                MyShuttleWidget x  = (MyShuttleWidget) myWidget;
//                //return x.getParentShuttleProperties().getNumber()+"";
//                x.getParentShuttleProperties().setNumber(Integer.parseInt(text));
//                labelWidget.setLabel(x.getParentShuttleProperties().getNumber()+"");
//                
//            }
//            else if(type == WidgetCommonInfo.WidgetType.STOP_SENSOR)
//            {
//                MyStopSensorWidget x  = (MyStopSensorWidget) myWidget;
//                //return x.getStopSensorProperties().getNumber()+"";
//                x.getStopSensorProperties().setNumber(Integer.parseInt(text));
//                labelWidget.setLabel(x.getStopSensorProperties().getNumber()+"");
//            }
//            else if(type == WidgetCommonInfo.WidgetType.SWITCH_INPUT)
//            {
//                MySwitchInputWidget x  = (MySwitchInputWidget) myWidget;
//                //return x.getSwitchInputNodeProperties().getNumber()+"";
//                x.getSwitchInputNodeProperties().setNumber(Integer.parseInt(text));
//                labelWidget.setLabel(x.getSwitchInputNodeProperties().getNumber()+"");
//            }
//            else if(type == WidgetCommonInfo.WidgetType.SWITCH_INTERMEDATE)
//            {
//                MySwitchIntermediateWidget x  = (MySwitchIntermediateWidget) myWidget;
//                //return x.getSwitchIntermediateNodeProperties().getNumber()+"";
//                x.getSwitchIntermediateNodeProperties().setNumber(Integer.parseInt(text));
//                labelWidget.setLabel( x.getSwitchIntermediateNodeProperties().getNumber()+"");
//            }
//            else if(type == WidgetCommonInfo.WidgetType.SWITCH_OUTPUT)
//            {
//                MySwitchOutputWidget x  = (MySwitchOutputWidget) myWidget;
//                x.getSwitchOutputNodeProperties().setNumber(Integer.parseInt(text));
//                //return x.getSwitchOutputNodeProperties().getNumber()+"";
//                labelWidget.setLabel( x.getSwitchOutputNodeProperties().getNumber()+"");
//            }
//            
//        }
//    }

    /**
     *
     * @return the main layer that contains the widgets of the scene
     */
    public LayerWidget getMainLayer() {
        return mainLayer;
    }

    /**
     *
     * @param mainLayer the main layer that contains the widgets of the scene
     */
    public void setMainLayer(LayerWidget mainLayer) {
        Scene.mainLayer = mainLayer;
    }

    /**
     *
     * @return the connection layer that contains the connectors of the layer
     */
    public static LayerWidget getConnectionLayer() {
        return connectionLayer;
    }

    /**
     *
     * @param connectionLayer the connection layer that contains the connectors
     * of the layer
     */
    public void setConnectionLayer(LayerWidget connectionLayer) {
        Scene.connectionLayer = connectionLayer;
    }

    /**
     *
     * @return the interaction layer that may contain interaction widgets (not
     * used yet)
     */
    public LayerWidget getInteractionLayer() {
        return interactionLayer;
    }

    /**
     *
     * @param interactionLayer the interaction layer that may contain
     * interaction widgets (not used yet)
     */
    public void setInteractionLayer(LayerWidget interactionLayer) {
        Scene.interactionLayer = interactionLayer;
    }

//    /**
//     *
//     * @return the editor of a widget label
//     */
//    public WidgetAction getEditorAction() {
//        return editorAction;
//    }
//
//    /**
//     *
//     * @param editorAction the editor of a widget label
//     */
//    public void setEditorAction(WidgetAction editorAction) {
//        this.editorAction = editorAction;
//    }

    /**
     *
     * @return the current connector widget in use inside the scene
     */
    public static MyConnectorWidget getC() {
        return C;
    }

    /**
     *
     * @param C the current connector widget in use
     */
    public static void setC(MyConnectorWidget C) {
        Scene.C = C;
    }

    /**
     *
     * @return the current shape of a widget that is in use inside the scene
     */
    public static ShapeNode getN() {
        return N;
    }

    /**
     *
     * @param N the current shape of a widget that is in use inside the scene
     */
    public static void setN(ShapeNode N) {
        Scene.N = N;
    }

    /**
     *
     * @return the scene container that is used
     */
    public static Scene getGlobalScene() {
        return globalScene;
    }

    /**
     *
     * @param globalScene the scene container that is used
     */
    public static void setGlobalScene(Scene globalScene) {
        Scene.globalScene = globalScene;
    }

    /**
     *
     * @return the copied or cut widget
     */
    public static Object getCcpWidget() {
        return ccpWidget;
    }

    /**
     *
     * @param ccpWidget the copied or cut widget
     */
    public static void setCcpWidget(Object ccpWidget) {
        Scene.ccpWidget = ccpWidget;
    }

    /**
     *
     * @return the copied or cut shape of the widget node
     */
    public static ShapeNode getCcpShapeNode() {
        return ccpShapeNode;
    }

    /**
     *
     * @param ccpShapeNode the copied or cut shape of the widget node
     */
    public static void setCcpShapeNode(ShapeNode ccpShapeNode) {
        Scene.ccpShapeNode = ccpShapeNode;
    }

    /**
     *
     * @return the position where to paste the copied or cut widget
     */
    public static Point getPastePosition() {
        return pastePosition;
    }

    /**
     *
     * @param pastePosition the position where to paste the copied or cut widget
     */
    public static void setPastePosition(Point pastePosition) {
        Scene.pastePosition = pastePosition;
    }

    /**
     *
     * @return true if the copied or cut widget has been pasted
     */
    public boolean isPasted() {
        return pasted;
    }

    /**
     *
     * @param pasted true if the copied or cut widget has been pasted
     */
    public void setPasted(boolean pasted) {
        this.pasted = pasted;
    }

    /**
     *
     * @return true if the widget is a copied or cut widget
     */
    public boolean isCopiedOrCutted() {
        return CopiedOrCutted;
    }

    /**
     *
     * @param CopiedOrCutted true if the widget is a copied or cut widget
     */
    public void setCopiedOrCutted(boolean CopiedOrCutted) {
        this.CopiedOrCutted = CopiedOrCutted;
    }

    /**
     *
     * @return a map of all of the widgets added to the scene and their values
     */
    public Map<Widget, Object> getMyWidgetsAdded() {
        return myWidgetsAdded;
    }

    /**
     *
     * @param myWidgetsAdded a map of all of the widgets added to the scene and
     * their values
     */
    public void setMyWidgetsAdded(Map<Widget, Object> myWidgetsAdded) {
        this.myWidgetsAdded = myWidgetsAdded;
    }

}
