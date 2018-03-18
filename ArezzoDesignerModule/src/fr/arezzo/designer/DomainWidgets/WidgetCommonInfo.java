package fr.arezzo.designer.DomainWidgets;

import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Exceptions;
import fr.arezzo.designer.Dialogs.DialogBoxes.DialogBoxText;
import fr.arezzo.designer.Dialogs.DialogBoxes.DialogBoxYesNoButtons;
import fr.arezzo.designer.Dialogs.DialogOutputMessages.Alert;
import fr.arezzo.designer.Domain.CoordinatesOfArezzo;
import fr.arezzo.designer.DomainWidgets.types3.MyLoadUnloadWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyLoadUnloadSensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MySensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyStopSensorWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchInputWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchIntermediateWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchOutputWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyTopologyNodesWidget;
import fr.arezzo.designer.EditeurModule.Repositories.ConnectorRepository;
import fr.arezzo.designer.Scene.Scene;
import static fr.arezzo.designer.Scene.Scene.connectionLayer;
import static fr.arezzo.designer.Scene.Scene.getN;
import fr.arezzo.designer.palette.ShapeNode;
import org.netbeans.api.visual.layout.LayoutFactory;

/**
 * WidgetCommonInfo represents the common information needed between all of the
 * widgets
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class WidgetCommonInfo {

    //represents the Number to be assigned to a widget
    private static Integer NumberOfNextWidget = 1;
    private static Integer TotalNumberOfWidgets = 0;
    public static Integer biggestNumberAssignedToANode = 0;
    private static String result = "-1";
    //IPC semaphores
    public static Semaphore semaphore = new Semaphore(1);
    public static Semaphore mutex = new Semaphore(1);
    //another way to make other threads wait certain operations is the countdownlatch
    //latchYesNo is a variable to make the GUI thread wait for the yes/no dialog box (of new widget initialization) response  
    public static CountDownLatch latchYesNo = new CountDownLatch(1);
    //latchInitialization is a variable to make the GUI thread and other threads wait 
    //for the initialization of properties  (of new widget) after that a user validated the need of initialization
    private static CountDownLatch latchInitialization = new CountDownLatch(1);
    ////globalInitializationLatch is a variable to make the GUI thread wait for all of the initialization operations
    private static CountDownLatch globalInitializationLatch = new CountDownLatch(1);
    //represents the widget to be initialized from the scene
    private static Object widget = null;
    //represents the type of widget to be initialized from the scene
    private static WidgetType typeOfWidget;
    //represents all of the supported widget types to be initialized

    public static enum WidgetType {

        WORKSTATION, LOAD_UNLOAD_WORKSTATION, STOP_SENSOR, SENSOR, SHUTTLE, SWITCH_INPUT, SWITCH_INTERMEDATE,
        SWITCH_OUTPUT, LOAD_UNLOAD_SENSOR, LINK_NODE
    }
    //cut/copy/paste widget
    public static Object ccpWidget = null;
    //link type
    public static boolean linearLink = false;
    //to know whether the user configured the new widget
    private static boolean Configured = false;

    /**
     * AskUserToInitializeWidgetPropertiesNow represents a parallel thread to
     * begin the initialization process of a new Widget from the scene
     *
     */
    public static class AskUserToInitializeWidgetPropertiesNow extends Thread {

        /**
         *
         * @param typeOfWidgetArg the type of widget to be added
         * @param widgetArg the widget to have its properties initialized using
         * dialog boxes
         */
        public AskUserToInitializeWidgetPropertiesNow(WidgetType typeOfWidgetArg, Object widgetArg) {
            //initialize fields
            typeOfWidget = typeOfWidgetArg;
            widget = widgetArg;
            //reset the latch
            globalInitializationLatch = new CountDownLatch(1);
            //start this thread
            this.start();
        }

        /**
         * invoked when thread.start is called operations to be executed by this
         * global thread is to call the beginning of the operations to fill the
         * properties asynchronously by another thread and wait for him to
         * terminate
         */
        @Override
        public synchronized void run() {
            //calling this thread to begin the procedure of initialization of properties
            new AsynchrnousWaitNoBlockGUIFillProperties(typeOfWidget, widget).start();
            try {
                //wait for the other thread to finish before quitting
                //so that the scene can get the initialized widget
                globalInitializationLatch.await();
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    /**
     * this thread is to run asynchronously to not block the GUI thread its
     * purpose is to get the yes/no user choice to initialize the operations of
     * the workstation
     */
    private static class AsynchrnousDialogBoxYesNoForOperationsResultConsumer extends Thread {

        //this method represents the operations of the thread when started
        @Override
        public synchronized void run() {

            //initialize dialog that will ask the user to chose from different radio buttons
            DialogBoxYesNoButtons dialog;
            //initialize the result queue to -1 to know if the field has been updated from the dialog or not
            result = "-1";
            //instanciate our possible choices that the user will chose from
            List<String> possibilities = new ArrayList<>();
            possibilities.add("Yes");
            possibilities.add("No");
            //initialize the answers that we want for the preceding choices
            List<String> answers = new ArrayList<>();
            answers.add("1");
            answers.add("0");
            //instanciate the dialog using the title of he window, the description,the choices for the user, the answer related
            //to these choices (the data that we need in return)
            dialog = new DialogBoxYesNoButtons("All Workstations new Operations Configuration",
                    "do you want to add new Operations for all Workstations now?", possibilities, answers, WidgetType.LOAD_UNLOAD_WORKSTATION);

            //while the user did not change the field; he did not make a choice
            while (result.equals("-1")) {
                try {
                    //read if there is an answer from the dialog
                    //until the user releases the lock of the semaphore
                    //when that happens, we have the choice data of the user
                    String result = dialog.getAnswer();
                    //block waiting for the user choice
                    semaphore.acquire();
                    mutex.acquire();
                    //update the field value using the user choice data
                    WidgetCommonInfo.result = result;
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

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }

            }
            //end the waiting of the wating threads
            WidgetCommonInfo.latchYesNo.countDown();
            //stop this thread and quit
            //stop();

        }
    }

    /**
     * this asynchronous thread contains the filling properties procedure which
     * will call the dialogs threads and wait for them firstly it will call the
     * first choice dialog thread to see if the user really wants to initialize
     * the new widget if the user wants to initialize the new widget initialize
     * operations using another thread that runs a dialog box in another thread
     * without blocking the GUI thread
     *
     */
    public static class AsynchrnousWaitNoBlockGUIFillAllWorkstationsOperations extends Thread {

        /**
         * constructor that resets the latch
         *
         */
        public AsynchrnousWaitNoBlockGUIFillAllWorkstationsOperations() {
            //reset the latch
            latchYesNo = new CountDownLatch(1);
            
            
        }

        /**
         * invoked when thread.start is called operations to be executed by this
         * global thread is to call the beginning of the operations to fill the
         * properties asynchronously by another thread and wait for him to
         * terminate
         *
         */
        @Override
        public synchronized void run() {
            //calling this thread to begin the procedure of input of user choice
            //to initialize or not the new widget
            new AsynchrnousDialogBoxYesNoForOperationsResultConsumer().start();
            try {
                //wait for the user to make a choice
                latchYesNo.await();
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
            //if the choice of the user is yes
            if (result.equals("Yes")) {
                //reset the latch 
                latchYesNo = new CountDownLatch(1);

                //initialize the operations of the workstation
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //begin the initilization of the widgets operations process
                        String nameOfOperationToBeAdded = "operation";
                        do {
                            DialogBoxText nameOfOperationDialog = new DialogBoxText("New Named Operation for Workstations", "What is the Name of the New Operation?", DialogBoxText.MessageType.QUESTION);
                            nameOfOperationToBeAdded = nameOfOperationDialog.showDialogAndGetResult();
                            if (nameOfOperationToBeAdded != null && !nameOfOperationToBeAdded.isEmpty()) {
                                String codeOfOperationToBeAdded = "1";
                                Integer codeOfOperation = 0;
                                do {
                                    try {
                                        nameOfOperationDialog = new DialogBoxText("Code representing Operation\"" + nameOfOperationToBeAdded + "\"", "What is the Code representing Operation \"" + nameOfOperationToBeAdded + "\"?", DialogBoxText.MessageType.QUESTION);
                                        codeOfOperationToBeAdded = nameOfOperationDialog.showDialogAndGetResult();
                                        codeOfOperation = Integer.parseInt(codeOfOperationToBeAdded);
                                    } catch (Exception Ex) {
                                        Alert.alert("Wrong input", "Operation Code must be an integer!!", Alert.AlertType.ERROR_MESSAGE);
                                        codeOfOperation = 0;
                                    }
                                } while (codeOfOperation == 0);
                                //add to the workstation operations list
                                //but check first that this operation is really new into the list of operations
                                if (!MyWorkstationWidget.operationsPossibilities.contains(nameOfOperationToBeAdded)) {
                                    MyWorkstationWidget.operationsPossibilities.add(nameOfOperationToBeAdded);
                                    MyWorkstationWidget.operationsAnswerPossibilities.add(codeOfOperationToBeAdded);
                                }
                            }
                        } while (nameOfOperationToBeAdded != null && !nameOfOperationToBeAdded.isEmpty());
                        //end the waiting of the wating threads
                        WidgetCommonInfo.latchYesNo.countDown();
                    }
                };
                runnable.run();
                try {
                    //wait for the user to make a choice
                    latchYesNo.await();
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }

//                try
//                {
//                    //wait for the process to end before quitting this thread
//                    latchInitialization.await();
//                } catch (InterruptedException ex)
//                {
//                    Exceptions.printStackTrace(ex);
//                }
            }
            //reset the latch
            WidgetCommonInfo.latchInitialization.countDown();
            //stop the global thread from waiting
            globalInitializationLatch.countDown();
        }
    }

    /**
     * this asynchronous thread contains the filling properties procedure which
     * will call the dialogs threads and wait for them firstly it will call the
     * first choice dialog thread to see if the user really wants to initialize
     * the new widget if the user wants to initialize the new widget it sees the
     * type of the widget and initialize it accordingly using another thread
     * that runs a dialog box in another thread without blocking the GUI thread
     *
     */
    private static class AsynchrnousWaitNoBlockGUIFillProperties extends Thread {

        /**
         *
         * @param typeOfWidgetArg the type of widget to be added
         * @param widgetArg the widget to have its properties initialized using
         * dialog boxes
         */
        public AsynchrnousWaitNoBlockGUIFillProperties(WidgetType typeOfWidgetArg, Object widgetArg) {
            //initialize fields
            typeOfWidget = typeOfWidgetArg;
            widget = widgetArg;
            //reset the latch
            latchYesNo = new CountDownLatch(1);
        }

        /**
         * invoked when thread.start is called operations to be executed by this
         * global thread is to call the beginning of the operations to fill the
         * properties asynchronously by another thread and wait for him to
         * terminate
         */
        @Override
        public synchronized void run() {
            //calling this thread to begin the procedure of input of user choice
            //to initialize or not the new widget
            new AsynchrnousDialogBoxYesNoResultConsumer().start();
            try {
                //wait for the user to make a choice
                latchYesNo.await();
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
            //if the choice of the user is yes
            if (result.equals("Yes")) {
                //reset the latch 
                latchYesNo = new CountDownLatch(1);
                //initialize the properties depending on the type of the widget
                if (typeOfWidget == WidgetType.WORKSTATION) {
                    //reset the latch
                    latchInitialization = new CountDownLatch(1);
                    //REMOVED AND ADDED A MENU TO ADD NEW OPERATIONS INSTEAD
                    
//                    //begin the initialization of the operations of the workstation
//                    new AsynchrnousWaitNoBlockGUIFillAllWorkstationsOperations().start();
//                    try {
//                        //wait for the process to end before quitting this thread
//                        latchYesNo.await();
//                    } catch (InterruptedException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//
//                    //reset the latch 
//                    //latchYesNo = new CountDownLatch(1);
//                    try {
//                        //wait for the process to end before quitting this thread
//                        latchInitialization.await();
//                    } catch (InterruptedException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
                    
                    //begin the initilization of the widget properties process
                    new initiaLizeWorkstationProperties().start();
                    try {
                        //wait for the process to end before quitting this thread
                        latchInitialization.await();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    //reset the latch
                    latchInitialization = new CountDownLatch(1);
                } //initialize the properties depending on the type of the widget
                else if (typeOfWidget == WidgetType.LOAD_UNLOAD_WORKSTATION) {
                    //begin the initilization of the widget properties process
                    new initiaLizeLoadUnloadWorkstationProperties().start();
                    try {
                        //wait for the process to end before quitting this thread
                        latchInitialization.await();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    //reset the latch
                    latchInitialization = new CountDownLatch(1);
                } //initialize the properties depending on the type of the widget
                else if (typeOfWidget == WidgetType.SENSOR) {
                    //begin the initilization of the widget properties process
                    new initiaLizeSensorProperties().start();
                    try {
                        //wait for the process to end before quitting this thread
                        latchInitialization.await();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    //reset the latch
                    latchInitialization = new CountDownLatch(1);
                } //initialize the properties depending on the type of the widget
                else if (typeOfWidget == WidgetType.STOP_SENSOR) {
                    //begin the initilization of the widget properties process
                    new initiaLizeStopSensorProperties().start();
                    try {
                        //wait for the process to end before quitting this thread
                        latchInitialization.await();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    //reset the latch
                    latchInitialization = new CountDownLatch(1);
                } //initialize the properties depending on the type of the widget
                else if (typeOfWidget == WidgetType.SWITCH_INPUT) {
                    //begin the initilization of the widget properties process
                    new initiaLizeSwichInputProperties().start();
                    try {
                        //wait for the process to end before quitting this thread
                        latchInitialization.await();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    //reset the latch
                    latchInitialization = new CountDownLatch(1);
                } //initialize the properties depending on the type of the widget
                else if (typeOfWidget == WidgetType.SWITCH_OUTPUT) {
                    //begin the initilization of the widget properties process
                    new initiaLizeSwichOutputProperties().start();
                    try {
                        //wait for the process to end before quitting this thread
                        latchInitialization.await();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    //reset the latch
                    latchInitialization = new CountDownLatch(1);
                } //initialize the properties depending on the type of the widget
                else if (typeOfWidget == WidgetType.SWITCH_INTERMEDATE) {
                    //begin the initilization of the widget properties process
                    new initiaLizeSwichIntermediateProperties().start();
                    try {
                        //wait for the process to end before quitting this thread
                        latchInitialization.await();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    //reset the latch
                    latchInitialization = new CountDownLatch(1);
                } //initialize the properties depending on the type of the widget
                else if (typeOfWidget == WidgetType.SHUTTLE) {
                    //begin the initilization of the widget properties process
                    new initiaLizeShuttleProperties().start();
                    try {
                        //wait for the process to end before quitting this thread
                        latchInitialization.await();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    //reset the latch
                    latchInitialization = new CountDownLatch(1);
                }
            } //if user do not want to configure the node, remove it from the scene
            else {
                Alert.alert("Configuration Aborted by user", "Configuration cancelled", Alert.AlertType.INFORMATION_MESSAGE);
                Scene.globalScene.removeNode(getN());
                Scene.globalScene.repaint();
            }

            //stop the global thread from waiting
            globalInitializationLatch.countDown();
        }
    }

    /**
     * thread to initialize the properties of the widget in the scene
     *
     */
    public static class initiaLizeSwichIntermediateProperties extends Thread {

        /**
         * operations of this thread when started
         *
         */
        @Override
        public synchronized void run() {
            //get the switch intermediate
            MySwitchIntermediateWidget mysiW = (MySwitchIntermediateWidget) widget;

            //initialize the switch of the stop sensor
            mysiW.initializeParentSwitch();

            //update our widget field
            widget = mysiW;
        }

    }

    /**
     * thread called to initialize the properties of the new shuttle widget in
     * the scene
     *
     */
    public static class initiaLizeShuttleProperties extends Thread {

        /**
         * operations of this thread when started
         *
         */
        @Override
        public synchronized void run() {
            //get the shuttle widget
            MyShuttleWidget mySw = (MyShuttleWidget) widget;
            //ask the user for the number of the shuttle 
            DialogBoxText questionMachineNumberDialog = new DialogBoxText("Naming the new Shuttle", "What is the number of the Shuttle?", DialogBoxText.MessageType.QUESTION);
            //get the user choice for the number of the shuttle
            String shuttleNumber = questionMachineNumberDialog.showDialogAndGetResult();
            //intermediate variable to store the user number
            Integer machineNumber = 0;
            try {
                //store the integer value of the user choice of the number of the machine
                machineNumber = Integer.parseInt(shuttleNumber);
            } catch (Exception e) {
                //verify that there is not a null value to assign otherwise assign 0
                machineNumber = (machineNumber != null ? machineNumber : 0);
            }
            //assign the user value to the attribute of the domain od the widget
            mySw.getParentShuttleProperties().setNumber(machineNumber);
            //initialize the size in DM of the shuttle
            mySw.initializeSizeForShuttle();
            //update our widget field
            widget = mySw;
        }

    }

    /**
     * thread to initialize the properties of the stop sensor widget in the
     * scene
     *
     */
    public static class initiaLizeStopSensorProperties extends Thread {

        /**
         * operations of this thread when started
         *
         */
        @Override
        public synchronized void run() {
            //get the stop sensor widget
            MyStopSensorWidget myssW = (MyStopSensorWidget) widget;
            //ask the user for the number of the stop sensor
            DialogBoxText questionMachineNumberDialog = new DialogBoxText("Naming the new Stop Sensor", "What is the AIP number of the Stop Sensor?", DialogBoxText.MessageType.QUESTION);
            //get the user choice for the aip number of the stop sensor
            String stopSensorAipNumber = questionMachineNumberDialog.showDialogAndGetResult();
            //intermediate variable to store the user number
            Integer aipNumber = 0;
            try {
                //store the integer value of the user choice of the number of the machine
                aipNumber = Integer.parseInt(stopSensorAipNumber);
            } catch (Exception e) {
                //verify that there is not a null value to assign otherwise assign 0
                aipNumber = (aipNumber != null ? aipNumber : 0);
            }
            //assign the user value to the attribute of the domain of the widget
            myssW.getStopSensorProperties().setAipNumber(aipNumber);

            //initialize the switch of the stop sensor
            myssW.initializeParentSwitch();

            //update our widget field
            widget = myssW;
        }

    }

    /**
     * thread to initialize the properties of the stop sensor widget in the
     * scene
     *
     */
    public static class initiaLizeSensorProperties extends Thread {

        //operations of this thread when started
        @Override
        public synchronized void run() {
            //get the  sensor widget
            MySensorWidget mysW = (MySensorWidget) widget;
            //ask the user for the number of the  sensor
            DialogBoxText questionMachineNumberDialog = new DialogBoxText("Naming the new Sensor", "What is the AIP number of the Sensor?", DialogBoxText.MessageType.QUESTION);
            //get the user choice for the aip number of the sensor
            String sensorAipNumber = questionMachineNumberDialog.showDialogAndGetResult();
            //intermediate variable to store the user number
            Integer aipNumber = 0;
            try {
                //store the integer value of the user choice of the number of the machine
                aipNumber = Integer.parseInt(sensorAipNumber);
            } catch (Exception e) {
                //verify that there is not a null value to assign otherwise assign 0
                aipNumber = (aipNumber != null ? aipNumber : 0);
            }
            //assign the user value to the attribute of the domain of the widget
            mysW.getSensorProperties().setAipNumber(aipNumber);

            //initialize the switch of the stop sensor
            mysW.initializeParentSwitch();

            //update our widget field
            widget = mysW;
        }

    }

    /**
     * thread to initialize the properties of the switch input widget in the
     * scene
     *
     */
    public static class initiaLizeSwichInputProperties extends Thread {

        /**
         * operations of this thread when started
         *
         */
        @Override
        public synchronized void run() {
            //get the switch input
            MySwitchInputWidget mysiW = (MySwitchInputWidget) widget;

            //initialize the switch of the switch input
            mysiW.initializeParentSwitch();

            //update our widget field
            widget = mysiW;
        }

    }

    /**
     * thread to initialize the properties of switch output widget in the scene
     *
     */
    public static class initiaLizeSwichOutputProperties extends Thread {

        /**
         * operations of this thread when started
         */
        @Override
        public synchronized void run() {
            //get the switch output
            MySwitchOutputWidget mysoW = (MySwitchOutputWidget) widget;

            //initialize the switch of the switch output
            mysoW.initializeParentSwitch();

            //update our widget field
            widget = mysoW;
        }

    }

    /**
     * thread to initialize the properties of the new workstation widget in the
     * scene
     *
     */
    public static class initiaLizeLoadUnloadWorkstationProperties extends Thread {

        /**
         * operations of this thread when started
         */
        @Override
        public synchronized void run() {
            //get the load/unload workstation widget
            MyLoadUnloadWorkstationWidget myWw = (MyLoadUnloadWorkstationWidget) widget;
            //ask the user for the number of the load/unload workstation machine
            DialogBoxText questionMachineNumberDialog = new DialogBoxText("Naming the new Load/Unload Workstation Machine Number", "What is the number of the Machine?", DialogBoxText.MessageType.QUESTION);
            //get the user choice for the number of the machine
            String workstationMachineNumber = questionMachineNumberDialog.showDialogAndGetResult();
            //intermediate variable to store the user number
            Integer machineNumber = 0;
            try {
                //store the integer value of the user choice of the number of the machine
                machineNumber = Integer.parseInt(workstationMachineNumber);
            } catch (Exception e) {
                //verify that there is not a null value to assign otherwise assign 0
                machineNumber = (machineNumber != null ? machineNumber : 0);
            }
            //assign the user value to the attribute of the domain od the widget
            myWw.getLoadUnloadWorkstationProperties().setNumberOfTheMachine(machineNumber);

            //ask the user for the AIP number of the workstation machine
            DialogBoxText questionMachineAIPNumberDialog = new DialogBoxText("Naming the new Workstation AIP Number", "What is the AIP Number of the Machine?", DialogBoxText.MessageType.QUESTION);
            //get the user choice for the AIP number of the machine
            String workstationAIPMachineNumber = questionMachineAIPNumberDialog.showDialogAndGetResult();
            //intermediate variable to store the user AIP number
            Integer AIPNumber = 0;
            try {
                //store the integer value of the user choice of the number of the machine
                AIPNumber = Integer.parseInt(workstationAIPMachineNumber);
            } catch (Exception e) {
                //verify that there is not a null value to assign otherwise assign 0
                AIPNumber = (AIPNumber != null ? AIPNumber : 0);
            }
            //assign the user value to the attribute of the domain of the widget
            myWw.getLoadUnloadWorkstationProperties().setAipNumber(AIPNumber);

            myWw.initializeOperations();
            //initialize waiting queue of the load/unload workstation
            myWw.initializeWaitingQueueForLoadUnloadWorkstation();
            //initialize operations of the workstation
            //myWw.initializeOperationsForLoadUnloadWorkstation();
            //update our widget field
            widget = myWw;
        }

    }

    /**
     * thread to initialize the properties of the new workstation widget in the
     * scene
     */
    public static class initiaLizeWorkstationProperties extends Thread {

        /**
         * operations of this thread when started
         *
         */
        @Override
        public synchronized void run() {
            //get the workstation widget
            MyWorkstationWidget myWw = (MyWorkstationWidget) widget;
            //ask the user for the number of the workstation machine
            DialogBoxText questionMachineNumberDialog = new DialogBoxText("Naming the new Workstation Machine Number", "What is the number of the Machine?", DialogBoxText.MessageType.QUESTION);
            //get the user choice for the number of the machine
            String workstationMachineNumber = questionMachineNumberDialog.showDialogAndGetResult();
            //intermediate variable to store the user number
            Integer machineNumber = 0;
            try {
                //store the integer value of the user choice of the number of the machine
                machineNumber = Integer.parseInt(workstationMachineNumber);
            } catch (Exception e) {
                //verify that there is not a null value to assign otherwise assign 0
                machineNumber = (machineNumber != null ? machineNumber : 0);
            }
            //assign the user value to the attribute of the domain od the widget
            myWw.getParentWorkstationProperties().setNumberOfTheMachine(machineNumber);

            //ask the user for the AIP number of the workstation machine
            DialogBoxText questionMachineAIPNumberDialog = new DialogBoxText("Naming the new Workstation AIP Number", "What is the AIP Number of the Machine?", DialogBoxText.MessageType.QUESTION);
            //get the user choice for the AIP number of the machine
            String workstationAIPMachineNumber = questionMachineAIPNumberDialog.showDialogAndGetResult();
            //intermediate variable to store the user AIP number
            Integer AIPNumber = 0;
            try {
                //store the integer value of the user choice of the number of the machine
                AIPNumber = Integer.parseInt(workstationAIPMachineNumber);
            } catch (Exception e) {
                //verify that there is not a null value to assign otherwise assign 0
                AIPNumber = (AIPNumber != null ? AIPNumber : 0);
            }
            //assign the user value to the attribute of the domain of the widget
            myWw.getParentWorkstationProperties().setAipNumber(AIPNumber);

            //initialize waiting queue of the workstation
            myWw.initializeWaitingQueueForWorkstation();
            //initialize operations of the workstation
            myWw.initializeOperationsForWorkstation();
            //update our widget field
            widget = myWw;
        }

    }

    /**
     * this thread is to run asynchronously to not block the GUI thread its
     * purpose is to get the yes/no user choice to initialize a new widget
     *
     */
    private static class AsynchrnousDialogBoxYesNoResultConsumer extends Thread {

        /**
         * this method represents the operations of the thread when started
         *
         */
        @Override
        public synchronized void run() {

            //initialize dialog that will ask the user to chose from different radio buttons
            DialogBoxYesNoButtons dialog;
            //initialize the result queue to -1 to know if the field has been updated from the dialog or not
            result = "-1";
            //instanciate our possible choices that the user will chose from
            List<String> possibilities = new ArrayList<>();
            possibilities.add("Yes");
            possibilities.add("No");
            //initialize the answers that we want for the preceding choices
            List<String> answers = new ArrayList<>();
            answers.add("1");
            answers.add("0");
            //instanciate the dialog using the title of he window, the description,the choices for the user, the answer related
            //to these choices (the data that we need in return)
            dialog = new DialogBoxYesNoButtons(typeOfWidget.toString().toLowerCase().replace("_", " ") + " Configuration",
                    "You need to configure this " + typeOfWidget.toString().toLowerCase().replace("_", " ") + " now?", possibilities, answers, typeOfWidget);

            //while the user did not change the field; he did not make a choice
            while (result.equals("-1")) {
                try {
                    //read if there is an answer from the dialog
                    //until the user releases the lock of the semaphore
                    //when that happens, we have the choice data of the user
                    String result = dialog.getAnswer();
                    //block waiting for the user choice
                    semaphore.acquire();
                    mutex.acquire();
                    //update the field value using the user choice data
                    WidgetCommonInfo.result = result;
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

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }

            }
            //end the waiting of the wating threads
            latchYesNo.countDown();
            //stop this thread and quit
            //stop();

        }
    }

    /**
     *
     * @return the result of a dialog box prompt
     */
    public static String getResult() {
        return result;
    }

    /**
     *
     * @param result the result of a dialog box prompt
     */
    public static void setResult(String result) {
        WidgetCommonInfo.result = result;
    }

    /**
     *
     * @return a semaphore used for inter process communication a.k.a IPC
     */
    public static Semaphore getSemaphore() {
        return semaphore;
    }

    /**
     *
     * @param semaphore a semaphore used for inter process communication a.k.a
     * IPC
     */
    public static void setSemaphore(Semaphore semaphore) {
        WidgetCommonInfo.semaphore = semaphore;
    }

    /**
     *
     * @return a 'mutex' used for inter process communication a.k.a IPC
     */
    public static Semaphore getMutex() {
        return mutex;
    }

    /**
     *
     * @param mutex a 'mutex' used for inter process communication a.k.a IPC
     */
    public static void setMutex(Semaphore mutex) {
        WidgetCommonInfo.mutex = mutex;
    }

    /**
     *
     * @return a latch used for thread synchronization between a thread and the
     * main GUI thread
     */
    public static CountDownLatch getLatchYesNo() {
        return latchYesNo;
    }

    /**
     *
     * @param latchYesNo a latch used for thread synchronization between a
     * thread and the main GUI thread
     */
    public static void setLatchYesNo(CountDownLatch latchYesNo) {
        WidgetCommonInfo.latchYesNo = latchYesNo;
    }

    /**
     *
     * @return a latch used for thread synchronization between a thread and the
     * main GUI other threads
     */
    public static CountDownLatch getLatchInitialization() {
        return latchInitialization;
    }

    /**
     *
     * @param latchInitialization a latch used for thread synchronization
     * between a thread and the main GUI other threads
     */
    public static void setLatchInitialization(CountDownLatch latchInitialization) {
        WidgetCommonInfo.latchInitialization = latchInitialization;
    }

    /**
     *
     * @return a latch used for thread synchronization between a thread and the
     * main GUI other threads
     */
    public static CountDownLatch getGlobalInitializationLatch() {
        return globalInitializationLatch;
    }

    /**
     *
     * @param globalInitializationLatch a latch used for thread synchronization
     * between a thread and the main GUI other threads
     */
    public static void setGlobalInitializationLatch(CountDownLatch globalInitializationLatch) {
        WidgetCommonInfo.globalInitializationLatch = globalInitializationLatch;
    }

    /**
     *
     * @return a widget representing the node graphics
     */
    public static Object getWidget() {
        return widget;
    }

    /**
     *
     * @param widget a widget representing the node graphics
     */
    public static void setWidget(Object widget) {
        WidgetCommonInfo.widget = widget;
    }

    /**
     *
     * @return the type of the widget node
     */
    public static WidgetType getTypeOfWidget() {
        return typeOfWidget;
    }

    /**
     *
     * @param typeOfWidget the type of the widget node
     */
    public static void setTypeOfWidget(WidgetType typeOfWidget) {
        WidgetCommonInfo.typeOfWidget = typeOfWidget;
    }

    /**
     *
     * @return the count of the widgets
     */
    public static Integer getTotalNumberOfWidgets() {
        return NumberOfNextWidget - 1;
    }

    /**
     *
     * @param TotalNumberOfWidgets the count of the widgets
     */
    public static void setTotalNumberOfWidgets(Integer TotalNumberOfWidgets) {
        WidgetCommonInfo.TotalNumberOfWidgets = TotalNumberOfWidgets;
    }

    /**
     *
     * @return the index of the next widget
     */
    public static Integer getNumberOfNextWidget() {
        return NumberOfNextWidget++;
    }

    /**
     *
     * @param NumberOfNextWidget the index of the next widget
     */
    public static void setNumberOfNextWidget(Integer NumberOfNextWidget) {
        WidgetCommonInfo.NumberOfNextWidget = NumberOfNextWidget;
    }

    /**
     * helper class for moving widgets
     */
    public final static class MyMoveStrategyProvider implements MoveStrategy, MoveProvider {

        private HashMap<Widget, Point> originalLocations = new HashMap<>();
        private final Scene scene;

        /**
         *
         * @param scene the current scene containing the widgets
         */
        public MyMoveStrategyProvider(Scene scene) {
            this.scene = scene;
        }

        /**
         *
         * @param widget the widget to be moved
         * @param originalLocation the previous location of the widget
         * @param suggestedLocation the new suggested location of the widget
         * @return
         */
        @Override
        public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation) {
            //Alert.alert("locationSuggested!", "locationSuggested!", Alert.AlertType.INFORMATION_MESSAGE);
            return suggestedLocation;
        }

        /**
         *
         * @param widget the widget to be moved
         */
        @Override
        public void movementStarted(Widget widget) {
            //               Alert.alert("move started!", "move started!", Alert.AlertType.INFORMATION_MESSAGE);
        }

        /**
         *
         * @param widget the widget that has been moved
         */
        @Override
        public void movementFinished(Widget widget) {
            originalLocations.clear();
            //               Alert.alert("move finished!", "move finished!", Alert.AlertType.INFORMATION_MESSAGE);
        }

        /**
         *
         * @param widget the widget to be moved
         * @return the previous(original) location of the widget
         */
        @Override
        public Point getOriginalLocation(Widget widget) {
            //add an action to make the widget alignable OLD
//                Alert.alert("get original location!", "get original location!!", Alert.AlertType.INFORMATION_MESSAGE);
            for (Object o : scene.getSelectedObjects()) {
                Widget w = scene.findWidget(o);
                originalLocations.put(w, ActionFactory.createDefaultMoveProvider().getOriginalLocation(w));
            }
            return ActionFactory.createDefaultMoveProvider().getOriginalLocation(widget);
        }

        /**
         *
         * @param widget the widget to be moved
         * @param location the location of the widget
         */
        @Override
        public void setNewLocation(Widget widget, Point location) {
            //Alert.alert("set new location!", "set new location!", Alert.AlertType.INFORMATION_MESSAGE);
            ActionFactory.createDefaultMoveProvider().setNewLocation(widget, location);
            //update widget coordinates properties(x and y coordinates) based on new location issued by moving the widget
            updateMyWidgetLocation(widget, location);
            Point originalLocation = originalLocations.get(widget);
            if (originalLocation == null) {
                return;
            }
            int dx = location.x - originalLocation.x;
            int dy = location.y - originalLocation.y;
            for (Map.Entry<Widget, Point> entry : originalLocations.entrySet()) {
                Widget w = entry.getKey();
                if (w == widget) {
                    continue;
                }
                Point l = new Point(entry.getValue());
                l.translate(dx, dy);
                ActionFactory.createDefaultMoveProvider().setNewLocation(w, l);
                updateMyWidgetLocation(w, l);
            }
        }
    }

    /**
     * update the widget location and update its arezzo coordinates location
     *
     * @param widget is the widget to be moved
     * @param location the location of the widget
     */
    private static void updateMyWidgetLocation(Widget widget, Point location) {
        //location  = widget.convertLocalToScene(new Point(0,0));
        Object myWidgetObject = Scene.globalScene.getMyWidgetsAdded().get(widget);
        if (myWidgetObject != null) {
            Point arezzoLocation = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(location.x, location.y, true);
            if (myWidgetObject instanceof MyWorkstationWidget) {
                MyWorkstationWidget myWw = (MyWorkstationWidget) myWidgetObject;
                myWw.getParentWorkstationProperties().setxCoordinate((float) location.getX());
                myWw.getParentWorkstationProperties().setyCoordinate((float) location.getY());
                //update new coorinates only if location of this widget has changed
                if (location.getX() != arezzoLocation.getX()) {
                    myWw.getParentWorkstationProperties().setxCoordinateArezzo((float) arezzoLocation.getX());
                }
                if (location.getY() != arezzoLocation.getY()) {
                    myWw.getParentWorkstationProperties().setyCoordinateArezzo((float) arezzoLocation.getY());
                }

            } else if (myWidgetObject instanceof MyLoadUnloadWorkstationWidget) {
                MyLoadUnloadWorkstationWidget w = (MyLoadUnloadWorkstationWidget) myWidgetObject;
                w.getLoadUnloadWorkstationProperties().setxCoordinate((float) location.getX());
                w.getLoadUnloadWorkstationProperties().setyCoordinate((float) location.getY());
            } else if (myWidgetObject instanceof MyLoadUnloadSensorWidget) {
                MyLoadUnloadSensorWidget w = (MyLoadUnloadSensorWidget) myWidgetObject;
                w.getLoadUnloadSensorProperties().setxCoordinate((float) location.getX());
                w.getLoadUnloadSensorProperties().setyCoordinate((float) location.getY());
            } else if (myWidgetObject instanceof MySensorWidget) {
                MySensorWidget w = (MySensorWidget) myWidgetObject;
                w.getSensorProperties().setxCoordinate((float) location.getX());
                w.getSensorProperties().setyCoordinate((float) location.getY());
            } else if (myWidgetObject instanceof MyStopSensorWidget) {
                MyStopSensorWidget w = (MyStopSensorWidget) myWidgetObject;
                w.getStopSensorProperties().setxCoordinate((float) location.getX());
                w.getStopSensorProperties().setyCoordinate((float) location.getY());
            } else if (myWidgetObject instanceof MyConnectorWidget) {
                MyConnectorWidget w = (MyConnectorWidget) myWidgetObject;
                w.getMyConnectorInfo().getConnectorProperties().setxCoordinate((float) location.getX());
                w.getMyConnectorInfo().getConnectorProperties().setyCoordinate((float) location.getY());
            } else if (myWidgetObject instanceof MySwitchInputWidget) {
                MySwitchInputWidget w = (MySwitchInputWidget) myWidgetObject;
                w.getSwitchInputNodeProperties().setxCoordinate((float) location.getX());
                w.getSwitchInputNodeProperties().setyCoordinate((float) location.getY());
            } else if (myWidgetObject instanceof MySwitchIntermediateWidget) {
                MySwitchIntermediateWidget w = (MySwitchIntermediateWidget) myWidgetObject;
                w.getSwitchIntermediateNodeProperties().setxCoordinate((float) location.getX());
                w.getSwitchIntermediateNodeProperties().setyCoordinate((float) location.getY());
            } else if (myWidgetObject instanceof MySwitchOutputWidget) {
                MySwitchOutputWidget w = (MySwitchOutputWidget) myWidgetObject;
                w.getSwitchOutputNodeProperties().setxCoordinate((float) location.getX());
                w.getSwitchOutputNodeProperties().setyCoordinate((float) location.getY());
            } else if (myWidgetObject instanceof MyShuttleWidget) {
                MyShuttleWidget w = (MyShuttleWidget) myWidgetObject;
                w.getShuttleProperties().setxCoordinate((float) location.getX());
                w.getShuttleProperties().setyCoordinate((float) location.getY());
            }
        }
    }

    /**
     * this class provides connectivity for a widget with other widgets
     *
     */
    public class MyConnectProvider implements ConnectProvider {

        /**
         * verify that the source widget can support connectivity with other
         * widgets
         *
         * @param source the source widget to be linked
         * @return if the source widget can support connectivity with other
         * widgets
         */
        @Override
        public boolean isSourceWidget(Widget source) {
            //verify that the widget is an IconNodeWidget and that it is instanciated
            return (source != null) && (source instanceof IconNodeWidget || source instanceof MyConnectorWidget);
        }

        /**
         * verify that the target widget can support connectivity with other
         * widgets
         *
         * @param src the source widget during the linking process
         * @param trg the target widget during the linking process
         * @return a ConnectorState that contains whether to accept or reject
         * the linking process
         */
        @Override
        public ConnectorState isTargetWidget(Widget src, Widget trg) {
            //verify that the source widget is different from the target widget and that the target widget is
            // an IconNodeWidget or MyConnectorWidget
            return ((src != trg && trg != null) && (trg instanceof IconNodeWidget || trg instanceof MyConnectorWidget)) ? ConnectorState.ACCEPT : ConnectorState.REJECT;

        }

        /**
         * create the connection between the source widget and the arget widget
         *
         * @param source the source widget during the linking process
         * @param target the target widget during the linking process
         */
        @Override
        public void createConnection(Widget source, Widget target) {
            Point adapterPreferredLocation = null;
            MyConnectorWidget.currentlyLinking = true;
            MyConnectorWidget myFinalSConnectorLink1;
            //is the link made using an adapter?
            boolean isAdapterLink = false;
            //instanciate our connector widget that will link the source and target widget
            MyConnectorWidget conn = new MyConnectorWidget(Scene.globalScene);
            //initialize the source and target nodes to our connector widget for future use            
            conn.setSourceWidget(source);
            conn.setTargetWidget(target);
            conn.getMyConnectorInfo().getConnectorProperties().setNumber(WidgetCommonInfo.getNumberOfNextWidget());
            conn.getMyConnectorInfo().getConnectorProperties().setID(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
            //check that the source is a widget and not a connector
            if ((ShapeNode) Scene.globalScene.findObject(source) != null) {

                //get the shape from the widget
                ShapeNode sourceShapeNode = (ShapeNode) Scene.globalScene.findObject(source);
                //get the type of widget from its shape
                WidgetType sourceWidgetType = sourceShapeNode.getShape().getShapeType();
                if (sourceWidgetType.equals(WidgetType.WORKSTATION)) {
                    MyWorkstationWidget mywwSource = (MyWorkstationWidget) Scene.globalScene.getMyWidgetsAdded().get(source);

                    //update next nodes for source
                    mywwSource.getParentWorkstationProperties().getNumbersOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
                    //update IDs
                    mywwSource.getParentWorkstationProperties().getIdsOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getID());
                    //update speed of next nodes for source
                    mywwSource.getParentWorkstationProperties().getSpeedsToNextNodes().add(0.0f);
                    
                    

                    conn.getMyConnectorInfo().setMySourceWidget(mywwSource);
                    conn.getMyConnectorInfo().setSourceWidgetType(sourceWidgetType);

                    //add current link to My widget source
                    mywwSource.getConnections().add(conn);
                } else if (sourceWidgetType.equals(WidgetType.LOAD_UNLOAD_WORKSTATION)) {
                    MyLoadUnloadWorkstationWidget mylUw = (MyLoadUnloadWorkstationWidget) Scene.globalScene.getMyWidgetsAdded().get(source);
                    //update next nodes for source
                    mylUw.getLoadUnloadWorkstationProperties().getNumbersOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
                    //update IDs
                    mylUw.getLoadUnloadWorkstationProperties().getIdsOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getID());
                    //update speed of next nodes for source
                    mylUw.getLoadUnloadWorkstationProperties().getSpeedsToNextNodes().add(0.0f);

                    conn.getMyConnectorInfo().setMySourceWidget(mylUw);
                    conn.getMyConnectorInfo().setSourceWidgetType(sourceWidgetType);

                    //add current link to My widget source
                    mylUw.getConnections().add(conn);
                } else if (sourceWidgetType.equals(WidgetType.LOAD_UNLOAD_SENSOR)) {
                    MyLoadUnloadSensorWidget mylUw = (MyLoadUnloadSensorWidget) Scene.globalScene.getMyWidgetsAdded().get(source);
                    //update next nodes for source
                    mylUw.getLoadUnloadSensorProperties().getNumbersOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
                    //update IDs
                    mylUw.getLoadUnloadSensorProperties().getIdsOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getID());
                    //update speed of next nodes for source
                    mylUw.getLoadUnloadSensorProperties().getSpeedsToNextNodes().add(0.0f);

                    conn.getMyConnectorInfo().setMySourceWidget(mylUw);
                    conn.getMyConnectorInfo().setSourceWidgetType(sourceWidgetType);

                    //add current link to My widget source
                    mylUw.getConnections().add(conn);
                } else if (sourceWidgetType.equals(WidgetType.SENSOR)) {
                    MySensorWidget mySw = (MySensorWidget) Scene.globalScene.getMyWidgetsAdded().get(source);
                    //update next nodes for source
                    mySw.getSensorProperties().getNumbersOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
                    //update IDs
                    mySw.getSensorProperties().getIdsOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getID());
                    //update speed of next nodes for source
                    mySw.getSensorProperties().getSpeedsToNextNodes().add(0.0f);

                    conn.getMyConnectorInfo().setMySourceWidget(mySw);
                    conn.getMyConnectorInfo().setSourceWidgetType(sourceWidgetType);

                    //add current link to My widget source
                    mySw.getConnections().add(conn);
                } else if (sourceWidgetType.equals(WidgetType.STOP_SENSOR)) {
                    MyStopSensorWidget mySsw = (MyStopSensorWidget) Scene.globalScene.getMyWidgetsAdded().get(source);
                    //update next nodes for source
                    mySsw.getStopSensorProperties().getNumbersOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
                    //update IDs
                    mySsw.getStopSensorProperties().getIdsOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getID());
                    //update speed of next nodes for source
                    mySsw.getStopSensorProperties().getSpeedsToNextNodes().add(0.0f);

                    conn.getMyConnectorInfo().setMySourceWidget(mySsw);
                    conn.getMyConnectorInfo().setSourceWidgetType(sourceWidgetType);

                    //add current link to My widget source
                    mySsw.getConnections().add(conn);
                } else if (sourceWidgetType.equals(WidgetType.SWITCH_INPUT)) {
                    MySwitchInputWidget mySiW = (MySwitchInputWidget) Scene.globalScene.getMyWidgetsAdded().get(source);
                    //update next nodes for source
                    mySiW.getSwitchInputNodeProperties().getNumbersOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
                    //update IDs
                    mySiW.getSwitchInputNodeProperties().getIdsOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getID());
                    //update speed of next nodes for source
                    mySiW.getSwitchInputNodeProperties().getSpeedsToNextNodes().add(0.0f);

                    conn.getMyConnectorInfo().setMySourceWidget(mySiW);
                    conn.getMyConnectorInfo().setSourceWidgetType(sourceWidgetType);

                    //add current link to My widget source
                    mySiW.getConnections().add(conn);
                } else if (sourceWidgetType.equals(WidgetType.SWITCH_INTERMEDATE)) {
                    MySwitchIntermediateWidget mySiW = (MySwitchIntermediateWidget) Scene.globalScene.getMyWidgetsAdded().get(source);
                    //update next nodes for source
                    mySiW.getSwitchIntermediateNodeProperties().getNumbersOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
                    //update IDs
                    mySiW.getSwitchIntermediateNodeProperties().getIdsOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getID());
                    //update speed of next nodes for source
                    mySiW.getSwitchIntermediateNodeProperties().getSpeedsToNextNodes().add(0.0f);

                    conn.getMyConnectorInfo().setMySourceWidget(mySiW);
                    conn.getMyConnectorInfo().setSourceWidgetType(sourceWidgetType);

                    //add current link to My widget source
                    mySiW.getConnections().add(conn);
                } else if (sourceWidgetType.equals(WidgetType.SWITCH_OUTPUT)) {
                    MySwitchOutputWidget mySoW = (MySwitchOutputWidget) Scene.globalScene.getMyWidgetsAdded().get(source);
                    //update next nodes for source
                    mySoW.getSwitchOutputNodeProperties().getNumbersOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
                    //update IDs
                    mySoW.getSwitchOutputNodeProperties().getIdsOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getID());
                    //update speed of next nodes for source
                    mySoW.getSwitchOutputNodeProperties().getSpeedsToNextNodes().add(0.0f);

                    conn.getMyConnectorInfo().setMySourceWidget(mySoW);
                    conn.getMyConnectorInfo().setSourceWidgetType(sourceWidgetType);

                    //add current link to My widget source
                    mySoW.getConnections().add(conn);
                } else if (sourceWidgetType.equals(WidgetType.SHUTTLE)) {
                    MyShuttleWidget mySw = (MyShuttleWidget) Scene.globalScene.getMyWidgetsAdded().get(source);
                    //update next nodes for source
                    mySw.getParentShuttleProperties().getNumbersOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
                    //update IDs
                    mySw.getParentShuttleProperties().getIdsOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getID());
                    //update speed of next nodes for source
                    mySw.getParentShuttleProperties().getSpeedsToNextNodes().add(0.0f);

                    conn.getMyConnectorInfo().setMySourceWidget(mySw);
                    conn.getMyConnectorInfo().setSourceWidgetType(sourceWidgetType);

                    //add current link to My widget source
                    mySw.getConnections().add(conn);

                } //translate link from link1 => adapter => target
                //to w1=> link1 =>  conn => target
                else if (sourceWidgetType.equals(WidgetType.LINK_NODE)) {
                    if (sourceShapeNode.getShape().getType().equals("999")) {
                        //TO DO LINKS MUST INTERSECT IN A POINT ( NO FREE SPACE)
                        isAdapterLink = true;
                        //Alert.alert("Adapter linking process", "Begining the Adapter linking process", Alert.AlertType.WARNING_MESSAGE);
                        Widget tempSourceWidget = source;
                        MyTopologyNodesWidget mySwAdapter = (MyTopologyNodesWidget) Scene.globalScene.getMyWidgetsAdded().get(source);
                        source = mySwAdapter.getSourceWidget();
                        adapterPreferredLocation = mySwAdapter.getAdapterLocation();
                        //update next nodes of the adapter to be used for every link having this adapter as target
                        mySwAdapter.getTopologyNodeProperties().getNumbersOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getNumber());
                        //update IDs
                        mySwAdapter.getTopologyNodeProperties().getIdsOfNextNodes().add(conn.getMyConnectorInfo().getConnectorProperties().getID());
                        //update speed of next nodes for source
                        mySwAdapter.getTopologyNodeProperties().getSpeedsToNextNodes().add(0.0f);

                        //source.setPreferredLocation(adapterPreferredLocation);
                        int link1Index = Scene.connectionLayer.getChildren().indexOf(source);
                        if (link1Index != -1) {
                            myFinalSConnectorLink1 = (MyConnectorWidget) Scene.connectionLayer.getChildren().get(link1Index);

                            conn.setSourceAnchor(myFinalSConnectorLink1.getTargetAnchor());

                            //update next nodes for source
                            conn.getMyConnectorInfo().setMySourceWidget(myFinalSConnectorLink1);
                            conn.getMyConnectorInfo().setSourceWidgetType(WidgetType.LINK_NODE);
                            //update link1 to be directly connected to conn connector by removing the numberofNextNode of the adapter
                            myFinalSConnectorLink1.setTargetWidget(conn);
                            List<Integer> numberOfNextNodesOfLink1 = myFinalSConnectorLink1.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes();
                            int indexOfAdapterNumber = numberOfNextNodesOfLink1.indexOf(mySwAdapter.getTopologyNodeProperties().getNumber());
                            numberOfNextNodesOfLink1.remove(mySwAdapter.getTopologyNodeProperties().getNumber());
                            //ID'S?
                            //update speeds
                            List<Float> speedToNextNodesOfLink1 = myFinalSConnectorLink1.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes();
//                            speedToNextNodesOfLink1.remove(indexOfAdapterNumber);
                            myFinalSConnectorLink1.getMyConnectorInfo().getConnectorProperties().setNumbersOfNextNodes(numberOfNextNodesOfLink1);
                            myFinalSConnectorLink1.getMyConnectorInfo().getConnectorProperties().setSpeedsToNextNodes(speedToNextNodesOfLink1);

                        }

                        //remove the adapter
                        //Scene.globalScene.getMyWidgetsAdded().remove(tempSourceWidget);
                        //Scene.mainLayer.removeChild(tempSourceWidget);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();

                        //add current link to My widget source
                        //mySw.getConnections().add(conn);
                    } else {

                    }

                }

                //get the shape from the widget
                ShapeNode targetShapeNode = (ShapeNode) Scene.globalScene.findObject(target);
                //Alert.alert("targetShapeNode == null? = " + targetShapeNode, "targetShapeNode == null? = " + targetShapeNode, Alert.AlertType.ERROR_MESSAGE);

                //get the type of widget from its shape
                WidgetType targetWidgetType = targetShapeNode.getShape().getShapeType();

                if (targetWidgetType.equals(WidgetType.WORKSTATION)) {
                    MyWorkstationWidget mywwTarget = (MyWorkstationWidget) Scene.globalScene.getMyWidgetsAdded().get(target);
                    //update next nodes for link 
                    int numberOfNextNode = mywwTarget.getParentWorkstationProperties().getNumber();
                    conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(numberOfNextNode);
                    //id
                    conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().add(mywwTarget.getParentWorkstationProperties().getID());
                    //update speed of next nodes for source
                    conn.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes().add(0.0f);

                    //add current link to My widget source
                    mywwTarget.getConnections().add(conn);

                    //add both source and target widgets to MYConnector
                    conn.getMyConnectorInfo().setMyTrgetWidget(mywwTarget);
                    conn.getMyConnectorInfo().setTargetWidgetType(targetWidgetType);

                    //add current link to My widget source
                    mywwTarget.getConnections().add(conn);

                } else if (targetWidgetType.equals(WidgetType.LOAD_UNLOAD_WORKSTATION)) {
                    MyLoadUnloadWorkstationWidget mylUw = (MyLoadUnloadWorkstationWidget) Scene.globalScene.getMyWidgetsAdded().get(target);

                    //update next nodes for link 
                    int numberOfNextNode = mylUw.getLoadUnloadWorkstationProperties().getNumber();
                    conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(numberOfNextNode);
                    conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().add(mylUw.getLoadUnloadWorkstationProperties().getID());
                    //update speed of next nodes for source
                    conn.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes().add(0.0f);

                    //update connector with the linked MyWidget target  and type of the linked MyWidget
                    conn.getMyConnectorInfo().setMyTrgetWidget(mylUw);
                    conn.getMyConnectorInfo().setTargetWidgetType(targetWidgetType);

                    //add current link to My widget source
                    mylUw.getConnections().add(conn);
                } else if (targetWidgetType.equals(WidgetType.LOAD_UNLOAD_SENSOR)) {
                    MyLoadUnloadSensorWidget mylUw = (MyLoadUnloadSensorWidget) Scene.globalScene.getMyWidgetsAdded().get(target);

                    //update next nodes for link 
                    int numberOfNextNode = mylUw.getLoadUnloadSensorProperties().getNumber();
                    conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(numberOfNextNode);
                    conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().add(mylUw.getLoadUnloadSensorProperties().getID());
                    //update speed of next nodes for source
                    conn.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes().add(0.0f);
                    //update connector with the linked MyWidget target  and type of the linked MyWidget

                    conn.getMyConnectorInfo().setMyTrgetWidget(mylUw);
                    conn.getMyConnectorInfo().setTargetWidgetType(targetWidgetType);

                    //add current link to My widget source
                    mylUw.getConnections().add(conn);
                } else if (targetWidgetType.equals(WidgetType.SENSOR)) {
                    MySensorWidget mySw = (MySensorWidget) Scene.globalScene.getMyWidgetsAdded().get(target);

                    //we linked to the child switch widget so we need to link to the parent widget
                    if (mySw == null) {
                        target = target.getParentWidget();
                        mySw = (MySensorWidget) Scene.globalScene.getMyWidgetsAdded().get(target);
                    }

                    //update next nodes for link 
                    int numberOfNextNode = mySw.getSensorProperties().getNumber();
                    conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(numberOfNextNode);
                    conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().add(mySw.getSensorProperties().getID());
                    //update speed of next nodes for source
                    conn.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes().add(0.0f);
                    //update connector with the linked MyWidget target  and type of the linked MyWidget

                    conn.getMyConnectorInfo().setMyTrgetWidget(mySw);
                    conn.getMyConnectorInfo().setTargetWidgetType(targetWidgetType);

                    //add current link to My widget source
                    mySw.getConnections().add(conn);
                } else if (targetWidgetType.equals(WidgetType.STOP_SENSOR)) {
                    MyStopSensorWidget mySsw = (MyStopSensorWidget) Scene.globalScene.getMyWidgetsAdded().get(target);
                    //we linked to the child switch widget so we need to link to the parent widget
                    if (mySsw == null) {
                        target = target.getParentWidget();
                        mySsw = (MyStopSensorWidget) Scene.globalScene.getMyWidgetsAdded().get(target);
                    }

                    //update next nodes for link 
                    int numberOfNextNode = mySsw.getStopSensorProperties().getNumber();
                    conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(numberOfNextNode);
                    conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().add(mySsw.getStopSensorProperties().getID());
                    //update speed of next nodes for source
                    conn.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes().add(0.0f);
                    //update connector with the linked MyWidget target  and type of the linked MyWidget

                    conn.getMyConnectorInfo().setMyTrgetWidget(mySsw);
                    conn.getMyConnectorInfo().setTargetWidgetType(targetWidgetType);

                    //add current link to My widget source
                    mySsw.getConnections().add(conn);
                } else if (targetWidgetType.equals(WidgetType.SWITCH_INPUT)) {

                    MySwitchInputWidget mySiW = (MySwitchInputWidget) Scene.globalScene.getMyWidgetsAdded().get(target);

                    if (mySiW == null) {
                        target = target.getParentWidget();
                        mySiW = (MySwitchInputWidget) Scene.globalScene.getMyWidgetsAdded().get(target);

                    }

                    //update next nodes for link 
                    int numberOfNextNode = mySiW.getSwitchInputNodeProperties().getNumber();
                    conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(numberOfNextNode);
                    conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().add(mySiW.getSwitchInputNodeProperties().getID());
                    //update speed of next nodes for source
                    conn.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes().add(0.0f);

                    //update connector with the linked MyWidget target  and type of the linked MyWidget
                    conn.getMyConnectorInfo().setMyTrgetWidget(mySiW);
                    conn.getMyConnectorInfo().setTargetWidgetType(targetWidgetType);

                    //add current link to My widget source
                    mySiW.getConnections().add(conn);
                } else if (targetWidgetType.equals(WidgetType.SWITCH_INTERMEDATE)) {
                    MySwitchIntermediateWidget mySiW = (MySwitchIntermediateWidget) Scene.globalScene.getMyWidgetsAdded().get(target);

                    //we linked to the child switch widget so we need to link to the parent widget
                    if (mySiW == null) {
                        target = target.getParentWidget();
                        mySiW = (MySwitchIntermediateWidget) Scene.globalScene.getMyWidgetsAdded().get(target);
                    }

                    //update next nodes for link 
                    int numberOfNextNode = mySiW.getSwitchIntermediateNodeProperties().getNumber();
                    conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(numberOfNextNode);
                    conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().add(mySiW.getSwitchIntermediateNodeProperties().getID());
                    //update speed of next nodes for source
                    conn.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes().add(0.0f);
                    //update connector with the linked MyWidget target  and type of the linked MyWidget

                    conn.getMyConnectorInfo().setMyTrgetWidget(mySiW);
                    conn.getMyConnectorInfo().setTargetWidgetType(targetWidgetType);

                    //add current link to My widget source
                    mySiW.getConnections().add(conn);
                } else if (targetWidgetType.equals(WidgetType.SWITCH_OUTPUT)) {
                    MySwitchOutputWidget mySoW = (MySwitchOutputWidget) Scene.globalScene.getMyWidgetsAdded().get(target);

                    //we linked to the child switch widget so we need to link to the parent widget
                    if (mySoW == null) {
                        target = target.getParentWidget();
                        mySoW = (MySwitchOutputWidget) Scene.globalScene.getMyWidgetsAdded().get(target);
                    }

                    //update next nodes for link 
                    int numberOfNextNode = mySoW.getSwitchOutputNodeProperties().getNumber();
                    conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(numberOfNextNode);
                    conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().add(mySoW.getSwitchOutputNodeProperties().getID());
                    //update speed of next nodes for source
                    conn.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes().add(0.0f);
                    //update connector with the linked MyWidget target  and type of the linked MyWidget

                    conn.getMyConnectorInfo().setMyTrgetWidget(mySoW);
                    conn.getMyConnectorInfo().setTargetWidgetType(targetWidgetType);

                    //add current link to My widget source
                    mySoW.getConnections().add(conn);
                } else if (targetWidgetType.equals(WidgetType.SHUTTLE)) {
                    MyShuttleWidget mySw = (MyShuttleWidget) Scene.globalScene.getMyWidgetsAdded().get(target);

                    //update next nodes for link 
                    int numberOfNextNode = mySw.getParentShuttleProperties().getNumber();
                    conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(numberOfNextNode);
                    conn.getMyConnectorInfo().getConnectorProperties().getIdsOfNextNodes().add(mySw.getParentShuttleProperties().getID());

                    //update speed of next nodes for source
                    conn.getMyConnectorInfo().getConnectorProperties().getSpeedsToNextNodes().add(0.0f);
                    //update connector with the linked MyWidget target  and type of the linked MyWidget

                    conn.getMyConnectorInfo().setMyTrgetWidget(mySw);
                    conn.getMyConnectorInfo().setTargetWidgetType(targetWidgetType);

                    //add current link to My widget source
                    mySw.getConnections().add(conn);
                } else if (targetWidgetType.equals(WidgetType.LINK_NODE)) {
                    if (targetShapeNode.getShape().getType().equals("999")) {

                        MyTopologyNodesWidget myTw = (MyTopologyNodesWidget) Scene.globalScene.getMyWidgetsAdded().get(target);

                        //update next nodes for link 
                        //int numberOfNextNode = myTw.getTopologyNodeProperties().getNumber();
                        //conn.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().add(numberOfNextNode);
                        //update from all of the next nodes from the adapter
                        List<Integer> numberOfNextNodes = myTw.getTopologyNodeProperties().getNumbersOfNextNodes();
                        conn.getMyConnectorInfo().getConnectorProperties().setNumbersOfNextNodes(numberOfNextNodes);
                        //update id's
                        List<Integer> IDsOfNextNodes = myTw.getTopologyNodeProperties().getIdsOfNextNodes();
                        conn.getMyConnectorInfo().getConnectorProperties().setIdsOfNextNodes(IDsOfNextNodes);
                        //update speeds
                        List<Float> speedsTONextNodes = myTw.getTopologyNodeProperties().getSpeedsToNextNodes();
                        conn.getMyConnectorInfo().getConnectorProperties().setSpeedsToNextNodes(speedsTONextNodes);

                        //update connector with the linked MyWidget target  and type of the linked MyWidget
                        // prepare to map the adapter to a new target
                        myTw.setSourceWidget(conn);

                        conn.getMyConnectorInfo().setMyTrgetWidget(myTw);
                        conn.getMyConnectorInfo().setTargetWidgetType(targetWidgetType);

                        //add current link to My widget source
                        //myTw.getConnections().add(conn);
                    } else {
                        Alert.alert("Connection Error", "You cannot connect two Links directly! you must use an adapter!", Alert.AlertType.ERROR_MESSAGE);
                    }

                }

                //set the type of source and target widget to know what type of widgets we are connecting together
                conn.setSourceWidgetType(sourceWidgetType);
                conn.setTargetWidgetType(targetWidgetType);
                /* old connection 1.0
                 //set the shape of the connector widget to an arrow
                 conn.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
                 //set the target anchor of the connector widget to the target widget
                 conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(target));
                 //initialize the source and target nodes to our connector widget for future use
                 ShapeNode sourceNode = (ShapeNode) scene.findObject(source);
                 ShapeNode targetNode = (ShapeNode) scene.findObject(target);
                 conn.getMyConnectorInfo().setSourceNode(sourceNode);
                 conn.getMyConnectorInfo().setTargetNode(targetNode);
                 //set the source anchor to our source widget
                 conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(source)); 
                 */
                //if not linking to a widget

                double sizeOfWidget = 0;
                double x_positionOfTarget = 0;
                double y_positionOfTarget = 0;
                double x_positionOfSource = 0;
                double y_positionOfSource = 0;
                double diff_XSource_XTarget_abs = 0;
                double diff_YSource_YTarget_abs = 0;
                double diff_XSource_XTarget = 0;
                double diff_YSource_YTarget = 0;
                //if it is a link using an adapter, do not update positions
                //bug fix polygonal link not working for adapter link
                //if(!isAdapterLink)
                {
                    sizeOfWidget = 32.0f;
                    if (isAdapterLink) {
                        sizeOfWidget = 16.0f;
                    }
                    //bug fix polygonal link not working for adapter link ( adapter does not have preferred location)
                    if (source != null) {
                        if (source.getPreferredLocation() == null) {
                            x_positionOfSource = source.getLocation().getX();
                            y_positionOfSource = source.getLocation().getY();
                        } else {
                            x_positionOfSource = source.getPreferredLocation().getX();
                            y_positionOfSource = source.getPreferredLocation().getY();
                        }

                        x_positionOfTarget = target.getPreferredLocation().getX();
                        y_positionOfTarget = target.getPreferredLocation().getY();

                        diff_XSource_XTarget_abs = Math.abs(x_positionOfTarget - x_positionOfSource);
                        diff_YSource_YTarget_abs = Math.abs(y_positionOfTarget - y_positionOfSource);
                        diff_XSource_XTarget = x_positionOfTarget - x_positionOfSource;
                        diff_YSource_YTarget = y_positionOfTarget - y_positionOfSource;
                    }

                }
                //if the link is non-linear
                if (!linearLink) {
                    //bug fix polygonal link not working for adapter link
                    //if(!isAdapterLink)
                    {
                        if (diff_XSource_XTarget_abs < sizeOfWidget) {

                            conn.setSourceAnchor(AnchorFactory.createDirectionalAnchor(source, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                            conn.setSourceDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                            conn.setTargetAnchor(AnchorFactory.createDirectionalAnchor(target, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                            conn.setTargetDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                        } else if (diff_YSource_YTarget_abs < sizeOfWidget) {
                            conn.setSourceAnchor(AnchorFactory.createDirectionalAnchor(source, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                            conn.setSourceDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                            conn.setTargetAnchor(AnchorFactory.createDirectionalAnchor(target, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                            conn.setTargetDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                        } else if (diff_XSource_XTarget > 0 && diff_YSource_YTarget > 0) {
                            conn.setSourceAnchor(AnchorFactory.createDirectionalAnchor(source, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                            conn.setSourceDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                            conn.setTargetAnchor(AnchorFactory.createDirectionalAnchor(target, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                            conn.setTargetDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);

                        } else if (diff_XSource_XTarget > 0 && diff_YSource_YTarget < 0) {
                            conn.setSourceAnchor(AnchorFactory.createDirectionalAnchor(source, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                            conn.setSourceDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                            conn.setTargetAnchor(AnchorFactory.createDirectionalAnchor(target, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                            conn.setTargetDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                        } else if (diff_XSource_XTarget < 0 && diff_YSource_YTarget < 0) {
                            conn.setSourceAnchor(AnchorFactory.createDirectionalAnchor(source, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                            conn.setSourceDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                            conn.setTargetAnchor(AnchorFactory.createDirectionalAnchor(target, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                            conn.setTargetDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);

                        } else if (diff_XSource_XTarget < 0 && diff_YSource_YTarget > 0) {
                            conn.setSourceAnchor(AnchorFactory.createDirectionalAnchor(source, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
                            conn.setSourceDirection(AnchorFactory.DirectionalAnchorKind.HORIZONTAL);
                            conn.setTargetAnchor(AnchorFactory.createDirectionalAnchor(target, AnchorFactory.DirectionalAnchorKind.VERTICAL));
                            conn.setTargetDirection(AnchorFactory.DirectionalAnchorKind.VERTICAL);
                        } else {
                            Alert.alert("Widget connection error", "try another position!", Alert.AlertType.ERROR_MESSAGE);
                        }
                    }
                } //if linear link
                else {
                    //set the shape of the connector widget to an arrow
                    conn.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
                    //set the target anchor of the connector widget to the target widget
                    conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(target));
                    //if it is an adapter link, we laready have initialized the source 
                    if (!isAdapterLink) {
                        //set the source anchor to our source widget
                        conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(source));
                    }

                }

                if (!linearLink) {
                    conn.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
                    conn.setPaintControlPoints(true);
                    conn.setControlPointShape(PointShape.SQUARE_FILLED_BIG);

                    conn.setRouter(RouterFactory.createOrthogonalSearchRouter(Scene.mainLayer));
                    conn.getActions().addAction(ActionFactory.createAddRemoveControlPointAction());
                    conn.getActions().addAction(ActionFactory.createFreeMoveControlPointAction());

                } //if linear
                else {
                    conn.setRouter(RouterFactory.createFreeRouter());
                }
                Scene.globalScene.validate();
                Scene.globalScene.repaint();

                //update next nodes for both link => target, and source => link
                //add the connection widget to the connection layer
                Dimension connDim = conn.getPreferredSize();

                conn.getMyConnectorInfo().setIsLinear(linearLink);
                IconNodeWidget widget = new IconNodeWidget(Scene.globalScene);
                //initialize the label of the widget
                //widget.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/switch_thumbnail.png"));
                
                
                //removed old widget
                //replaced it with a widget label
                
                widget.setLabel(conn.getMyConnectorInfo().getConnectorProperties().getNumber() + "");
                widget.setVisible(true);
                //add an action to make the widget capable of having links from the widget to connect to another widget (line)
                widget.getActions().addAction(conn.getEditorAction());
                conn.addChild(widget, null);
                conn.setLabelWidget(widget);
                

                conn.setConstraint(widget, LayoutFactory.ConnectionWidgetLayoutAlignment.BOTTOM_CENTER, 0.5f);
                connectionLayer.addChild(conn);

                //add to repository
                ConnectorRepository.getInstance().add(conn);

                //bug fix reposition
                if (isAdapterLink) {
                    conn.updateDirection(16.0f);
                }
                Scene.globalScene.validate();
                Scene.globalScene.repaint();
                //fill the coordinate properties of the connector
                //find added connector
                Point positionOfConnection = conn.getLocation();

                Point middleOfConnection = conn.getControlPoints().get(0);
                positionOfConnection = conn.convertLocalToScene(middleOfConnection);
                positionOfConnection = Scene.globalScene.convertSceneToView(positionOfConnection);
                conn.getMyConnectorInfo().getConnectorProperties().setxCoordinate((float) positionOfConnection.getX());
                conn.getMyConnectorInfo().getConnectorProperties().setyCoordinate((float) positionOfConnection.getY());

                if (!linearLink) {
                    //bug fix to update end points of links while moving one of the end points
                    //if(!isAdapterLink)
                    {
                        conn.setRoutingPolicy(ConnectionWidget.RoutingPolicy.UPDATE_END_POINTS_ONLY);
                        Scene.globalScene.validate();
                    }

                }
                //end of the mouse and keyboard simulation
                Robot robot;
                try {
                    MyConnectorWidget.currentlyLinking = false;
                    robot = new Robot();

                    //simulate the ctrl button release
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    //simulate the mouse button release
                    //robot.mouseRelease(InputEvent.BUTTON1_MASK );

                } catch (AWTException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

        }

        /**
         * Called to check whether the provider has a custom target widget
         * resolver.
         *
         * @param scene the scene containing the widgets
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
         * @param scene the scene containing all the widgets
         * @param point the scene location
         * @return the target widget; null if no target widget found
         */
        @Override
        public Widget resolveTargetWidget(org.netbeans.api.visual.widget.Scene scene, Point point) {
            return null;
        }
    }
    
    
}
