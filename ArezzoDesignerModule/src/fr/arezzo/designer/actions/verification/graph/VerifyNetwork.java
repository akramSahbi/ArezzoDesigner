package fr.arezzo.designer.actions.verification.graph;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

import fr.arezzo.designer.DomainWidgets.MySwitchWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;
import fr.arezzo.designer.DomainWidgets.types3.MyLoadUnloadWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyLoadUnloadSensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MySensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyStopSensorWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchInputWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchIntermediateWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchOutputWidget;
import fr.arezzo.designer.EditeurModule.Repositories.ConnectorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.LoadUnloadSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.LoadUnloadWorkstationRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchInputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchIntermediateRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchOutputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchStopSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.WorkstationRepository;
import fr.arezzo.designer.Scene.Scene;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;


/**
 * VerifyNetwork represents an action menu class to verify the validity of the
 * network before exporting its nodes configuration
 *
 *
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
@ActionID(
        category = "Bugtracking",
        id = "fr.arezzo.designer.actions.verification.graph.VerifyNetwork"
)
@ActionRegistration(
        iconBase = "fr/arezzo/designer/resources/verify_network.png",
        displayName = "#CTL_VerifyNetwork"
)
@ActionReferences(
        {
            @ActionReference(path = "Menu/RunProject", position = 100),
            @ActionReference(path = "Toolbars/UndoRedo", position = 300),
            @ActionReference(path = "Shortcuts", name = "D-V")
        })
@Messages("CTL_VerifyNetwork=Verify Network")
@SuppressWarnings({"unchecked", "deprecation", "rawtypes"}) 
public final class VerifyNetwork implements ActionListener {

    private Integer sizeOfNetwork = 0;
    private String[] nodes;
    private boolean[][] adjNodes;
    //to know which element numbers are in the same cycle
    private boolean[][] cyclesMatrix;
    //output tab where we will write the informations about the verifications
    private InputOutput output = IOProvider.getDefault().getIO("Network Verification", true);
    //the cycles that we are going to use for arezzo (containning just numbers of each node)
    private Map<Integer, List<Integer>> cyclesWithIntegerNumbers;
    private Map<Integer, List<Integer>> arezzoCyclesWithIntegerNumbers;
    //the cycles that we are going to use for arezzo (containning strings of each node)
    private Map<Integer, List<String>> arezzoCycles;
    //constraints for the cycle
    private List<String> arezzoCycleConstraints;
    //the number of cycles
    private int numberOfCycles;
    //cycle to add or not?
    private boolean arezzoCycleToAdd;
    private boolean validNetwork = true;
    //singleton design pattern
    //only one instance is present in memory
    private static VerifyNetwork instance = null;

    /**
     * verifying the network for closed network and loops and more
     *
     * @param e event triggered
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        validNetwork = true;
        output.select();
        //Alert.alert("Starting the Verification Process", "Starting the Verification Process", Alert.AlertType.INFORMATION_MESSAGE);
        printTextToOutput("Begining the Verification Process at " + (Instant.now()) + "\n");
//        Alert.alert("number of widgets is " + WidgetCommonInfo.getTotalNumberOfWidgets() , "number of widgets is " + WidgetCommonInfo.getTotalNumberOfWidgets(), Alert.AlertType.INFORMATION_MESSAGE);

        setSizeOfNetwork(WidgetCommonInfo.getTotalNumberOfWidgets() + 1);

        nodes = new String[sizeOfNetwork];
        adjNodes = new boolean[sizeOfNetwork][sizeOfNetwork];
        //to know which elements are in the same cycle
        cyclesWithIntegerNumbers = new HashMap<>();
        //to know which elements are in the same arezzo cycle
        arezzoCyclesWithIntegerNumbers = new HashMap<>();
        //to know ehich element are in the same cycle (each String will contain the node name that is its type and number)
        arezzoCycles = new HashMap<>();
        //initialization of the cycle's constraints (there must not be a WORKSTATION or a LOAD_UNLOAD_WORKSTATION in the cycle)
        arezzoCycleConstraints = new ArrayList<>();
        arezzoCycleConstraints.add(WidgetCommonInfo.WidgetType.WORKSTATION + "");
        arezzoCycleConstraints.add(WidgetCommonInfo.WidgetType.LOAD_UNLOAD_WORKSTATION + "");
        //initialize the number of cycles
        numberOfCycles = 0;
        //reinitialize the colors of the links in the scene
        UncolorLinks();
        //initialize our graph
        initializeAdjNodesMatrix();

        //verify closed network
        printTextToOutput("####################Begining closed network verification process at " + (Instant.now()) + ":####################\n");
        boolean closedNetwork = true;
        //the two dimension array is a square matrix which contains for each node number of a row (Xi) the possible
        //other linked nodes Yi if the node is linked to another which is a column Yi only if (adjNodes[Xi][Yi] == true)
        for (int i = 0; i < sizeOfNetwork; i++) {
            if (nodes[i] != null) {
                //verify if node i is linked to any other node
                boolean linked = false;
                linked = false;
                for (int j = 0; j < sizeOfNetwork; j++) {
                    if (adjNodes[i][j] == true) {
                        //i node is linked to j node
                        printTextToOutput(nodes[i] + " is linked to the network\n");
                        linked = true;
                        break;
                    }
                }
                if (linked == false) {
                    //i node not linked to j
                    printErrorToOutput(nodes[i] + " is not linked to the network!\n");
                    closedNetwork = false;
                }
            }
        }
        if (closedNetwork) {
            printTextToOutput("The network is closed !\n");
        } else {
            validNetwork = false;
            printErrorToOutput("The network is open !\n");
            return;
        }

        //verify cycles 
        //verify closed network
        printTextToOutput("####################Begining cycles verification process:####################\n");
        ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(adjNodes, nodes);
        //all johnson mathematical graph cycles found (unfiltered by constraints)
        List cycles = ecs.getElementaryCycles();
        cyclesMatrix = new boolean[cycles.size()][sizeOfNetwork];

        //for each mathematical cycle we need to get the arezzo cycles
        for (int i = 0; i < cycles.size(); i++, numberOfCycles++) {
            //no constraints found related to this cycle (it is an arezzo cycle until we find a constraint)
            arezzoCycleToAdd = true;

            //get the johnson mathematical graph cycle (without any constraints)
            List cycle = (List) cycles.get(i);
            //initialize an arezzo cycle
            List arezzoCycle = new ArrayList<>();
            arezzoCycle.clear();

            //initialize thisCycle which contains only node numbers of this arezzzo cycle
            List<Integer> thisCycle = new ArrayList<>();
            List<Integer> mathematicalCycle = new ArrayList<>();

            for (int j = 0; j < cycle.size(); j++) {
                //get the node type + number
                String node = (String) cycle.get(j);
                //extract the node's number
                String nodeNumber = node.substring(node.lastIndexOf(" "), node.length());
                //extract the node's type
                String nodeType = node.substring(0, node.trim().lastIndexOf(" "));
                mathematicalCycle.add(Integer.parseInt(nodeNumber.trim()));
                //update cyclesMatrix
                cyclesMatrix[i][Integer.parseInt(nodeNumber.trim())] = true;
                //if meets constraints do not take into consideration this cycle
                if (arezzoCycleConstraints.contains(nodeType) && arezzoCycleToAdd) {
                    numberOfCycles--;
                    arezzoCycleToAdd = false;
                    //break;
                }
                if (arezzoCycleToAdd) {
                    //add integer number of node to thisCycle
                    thisCycle.add(Integer.parseInt(nodeNumber.trim()));
                    //add node type + number to arezzoCycle
                    arezzoCycle.add( node);

                }

            }
            //accepted as an arezzo cycle
            if (arezzoCycleToAdd) {
                //make sure that in the arezzo cycle, the first node is the last node (link included)
                //by refining the numbers only cycle
                Integer beginingNodeLink = thisCycle.get(thisCycle.size() - 1);
                Integer beginingNode = thisCycle.get(thisCycle.size() - 2);
                List<Integer> begining = new ArrayList<>();
                begining.add(beginingNode);
                begining.add(beginingNodeLink);
                thisCycle.remove(beginingNode);
                thisCycle.remove(beginingNodeLink);
                thisCycle.addAll(0, begining);
                thisCycle.add(thisCycle.size(), thisCycle.get(0));
                //and by refining the arezzo cycle that contains the type of node + number
                String beginingNodeLinkExplicit = (String) arezzoCycle.get(arezzoCycle.size() - 1);
                String beginingNodeExplicit = (String) arezzoCycle.get(arezzoCycle.size() - 2);
                List<String> beginingExplicit = new ArrayList<>();
                beginingExplicit.add(beginingNodeExplicit);
                beginingExplicit.add(beginingNodeLinkExplicit);
                arezzoCycle.remove(beginingNodeExplicit);
                arezzoCycle.remove(beginingNodeLinkExplicit);
                arezzoCycle.addAll(0, beginingExplicit);
                arezzoCycle.add(arezzoCycle.size(), arezzoCycle.get(0));

                arezzoCyclesWithIntegerNumbers.put(numberOfCycles, thisCycle);
                arezzoCycles.put(numberOfCycles, arezzoCycle);

            }

            cyclesWithIntegerNumbers.put(i, mathematicalCycle);
            //johnson mathematical graph cycles
            //colorCycle(mathematicalCycle);

        }
        //print number of cycles if found
        if (numberOfCycles > 0) {
            printTextToOutput("Found " + numberOfCycles + " cycle(s):\n");
        } else {
            printTextToOutput("Did not Find any cycle(s)!\n");
        }
        //print cycles to output and color them randomly
        for (int i = 0; i < numberOfCycles; i++) {
            printCycleToVerificationOutput(arezzoCycles.get(i), i);
            colorCycle(arezzoCyclesWithIntegerNumbers.get(i));
        }
        printTextToOutput("####################Begining switchs verification process:####################\n");
        for (MySwitchWidget switchWidget : SwitchRepository.getInstance().getAll()) {
            printTextToOutput("===>  Verifiying switch " + switchWidget.getNumber() + "  <===\n");

            //is first end stop sensor connected to first input in switch?
            if (switchWidget.getFirstEndStopSensorNode() != null) {
                if (switchWidget.getFirstInputNode() != null) {
                    int mustLinkToSource = switchWidget.getFirstEndStopSensorNode().getStopSensorProperties().getNumber();
                    int mustLinkToTarget = switchWidget.getFirstInputNode().getSwitchInputNodeProperties().getNumber();
                    boolean isLinked = false;

                    //search recursively in the next nodes links until we find the target node
                    for (int i = 0; i < switchWidget.getFirstEndStopSensorNode().getStopSensorProperties().getNumbersOfNextNodes().size()
                            && !isLinked; i++) {

                        int nextNode = switchWidget.getFirstEndStopSensorNode().getStopSensorProperties().getNumbersOfNextNodes().get(i);

                        while (ConnectorRepository.getInstance().find(nextNode) != null) {
                            MyConnectorWidget currentConnector = ConnectorRepository.getInstance().find(nextNode);
                            if (currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                                nextNode = currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().get(0);
                            }
                        }
                        //is next node is the target?
                        if (mustLinkToTarget == SwitchInputRepository.getInstance().find(nextNode).getSwitchInputNodeProperties().getNumber()) {
                            isLinked = true;
                            break;
                        }

                    }

                    if (isLinked) {
                        printTextToOutput("first end stop sensor [" + mustLinkToSource + "] is  linked  to first input [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    } else {
                        printErrorToOutput("Warning: first end stop sensor [" + mustLinkToSource + "] is not linked to first input [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    }
                } else {
                    validNetwork = false;
                    printErrorToOutput("Warning first switch input not found in switch " + switchWidget.getNumber() + "\n");
                }

            } else {
                validNetwork = false;
                printErrorToOutput("Warning first end stop sensor not found in switch " + switchWidget.getNumber() + "\n");
            }

            //is second end stop sensor connected to second input in switch?
            if (switchWidget.getSecondEndStopSensorNode() != null) {
                if (switchWidget.getSecondInputNode() != null) {
                    int mustLinkToSource = switchWidget.getSecondEndStopSensorNode().getStopSensorProperties().getNumber();
                    int mustLinkToTarget = switchWidget.getSecondInputNode().getSwitchInputNodeProperties().getNumber();
                    boolean isLinked = false;
                    //search recursively in the next nodes links until we find the target node
                    for (int i = 0; i < switchWidget.getSecondEndStopSensorNode().getStopSensorProperties().getNumbersOfNextNodes().size()
                            && !isLinked; i++) {

                        int nextNode = switchWidget.getSecondEndStopSensorNode().getStopSensorProperties().getNumbersOfNextNodes().get(i);

                        while (ConnectorRepository.getInstance().find(nextNode) != null) {
                            MyConnectorWidget currentConnector = ConnectorRepository.getInstance().find(nextNode);
                            if (currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                                nextNode = currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().get(0);
                            }
                        }
                        //is next node is the target?
                        if (mustLinkToTarget == SwitchInputRepository.getInstance().find(nextNode).getSwitchInputNodeProperties().getNumber()) {
                            isLinked = true;
                            break;
                        }

                    }
                    if (isLinked) {
                        printTextToOutput("second end stop sensor [" + mustLinkToSource + "] is  linked  to second input [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    } else {
                        printErrorToOutput("Warning: second end stop sensor [" + mustLinkToSource + "] is not linked to second input [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    }
                } else {
                    validNetwork = false;
                    printErrorToOutput("Warning second switch input not found in switch " + switchWidget.getNumber() + "\n");
                }

            } else {
                validNetwork = false;
                printErrorToOutput("Warning second end stop sensor not found in switch " + switchWidget.getNumber() + "\n");
            }

            //is first switch input connected to first intermediate in switch?
            if (switchWidget.getFirstInputNode() != null) {
                if (switchWidget.getFirstIntermediateNode() != null) {
                    int mustLinkToSource = switchWidget.getFirstInputNode().getSwitchInputNodeProperties().getNumber();
                    int mustLinkToTarget = switchWidget.getFirstIntermediateNode().getSwitchIntermediateNodeProperties().getNumber();
                    boolean isLinked = false;
                    //search recursively in the next nodes links until we find the target node
                    for (int i = 0; i < switchWidget.getFirstInputNode().getSwitchInputNodeProperties().getNumbersOfNextNodes().size()
                            && !isLinked; i++) {

                        int nextNode = switchWidget.getFirstInputNode().getSwitchInputNodeProperties().getNumbersOfNextNodes().get(i);

                        while (ConnectorRepository.getInstance().find(nextNode) != null) {
                            MyConnectorWidget currentConnector = ConnectorRepository.getInstance().find(nextNode);
                            if (currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                                nextNode = currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().get(0);
                            }
                        }
                        //is next node is the target?
                        if (mustLinkToTarget == SwitchIntermediateRepository.getInstance().find(nextNode).getSwitchIntermediateNodeProperties().getNumber()) {
                            isLinked = true;
                            break;
                        }

                    }
                    if (isLinked) {

                        printTextToOutput("first switch input [" + mustLinkToSource + "] is  linked  to first intermediate [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    } else {
                        printErrorToOutput("Warning: first switch input [" + mustLinkToSource + "] is not linked to first intermediate [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    }
                } else {
                    validNetwork = false;
                    printErrorToOutput("Warning first switch intermediate not found in switch " + switchWidget.getNumber() + "\n");
                }

            } else {
                validNetwork = false;
                printErrorToOutput("Warning first switch input not found in switch " + switchWidget.getNumber() + "\n");
            }

            //is second switch input connected to first intermediate in switch?
            if (switchWidget.getSecondInputNode() != null) {
                if (switchWidget.getFirstIntermediateNode() != null) {
                    int mustLinkToSource = switchWidget.getSecondInputNode().getSwitchInputNodeProperties().getNumber();
                    int mustLinkToTarget = switchWidget.getFirstIntermediateNode().getSwitchIntermediateNodeProperties().getNumber();
                    boolean isLinked = false;
                    //search recursively in the next nodes links until we find the target node
                    for (int i = 0; i < switchWidget.getSecondInputNode().getSwitchInputNodeProperties().getNumbersOfNextNodes().size()
                            && !isLinked; i++) {

                        int nextNode = switchWidget.getSecondInputNode().getSwitchInputNodeProperties().getNumbersOfNextNodes().get(i);

                        while (ConnectorRepository.getInstance().find(nextNode) != null) {
                            MyConnectorWidget currentConnector = ConnectorRepository.getInstance().find(nextNode);
                            if (currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                                nextNode = currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().get(0);
                            }
                        }
                        //is next node is the target?
                        if (mustLinkToTarget == SwitchIntermediateRepository.getInstance().find(nextNode).getSwitchIntermediateNodeProperties().getNumber()) {
                            isLinked = true;
                            break;
                        }

                    }
                    if (isLinked) {
                        printTextToOutput("second switch input [" + mustLinkToSource + "] is  linked  to first intermediate [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    } else {
                        printErrorToOutput("Warning: second switch input [" + mustLinkToSource + "] is not linked to first intermediate [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    }
                } else {
                    validNetwork = false;
                    printErrorToOutput("Warning first switch intermediate not found in switch " + switchWidget.getNumber() + "\n");
                }

            } else {
                validNetwork = false;
                printErrorToOutput("Warning second switch input not found in switch " + switchWidget.getNumber() + "\n");
            }

            //is first switch intermediate connected to second intermediate in switch?
            if (switchWidget.getFirstIntermediateNode() != null) {
                if (switchWidget.getSecondIntermediateNode() != null) {
                    int mustLinkToSource = switchWidget.getFirstIntermediateNode().getSwitchIntermediateNodeProperties().getNumber();
                    int mustLinkToTarget = switchWidget.getSecondIntermediateNode().getSwitchIntermediateNodeProperties().getNumber();
                    boolean isLinked = false;
                    //search recursively in the next nodes links until we find the target node
                    for (int i = 0; i < switchWidget.getFirstIntermediateNode().getSwitchIntermediateNodeProperties().getNumbersOfNextNodes().size()
                            && !isLinked; i++) {

                        int nextNode = switchWidget.getFirstIntermediateNode().getSwitchIntermediateNodeProperties().getNumbersOfNextNodes().get(i);

                        while (ConnectorRepository.getInstance().find(nextNode) != null) {
                            MyConnectorWidget currentConnector = ConnectorRepository.getInstance().find(nextNode);
                            if (currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                                nextNode = currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().get(0);
                            }
                        }
                        //is next node is the target?
                        if (mustLinkToTarget == SwitchIntermediateRepository.getInstance().find(nextNode).getSwitchIntermediateNodeProperties().getNumber()) {
                            isLinked = true;
                            break;
                        }

                    }
                    if (isLinked) {

                        printTextToOutput("first switch intermediate [" + mustLinkToSource + "] is  linked  to second intermediate [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");

                    } else {
                        printErrorToOutput("Warning: first switch intermediate [" + mustLinkToSource + "] is not linked to second intermediate [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    }
                } else {
                    validNetwork = false;
                    printErrorToOutput("Warning second switch intermediate not found in switch " + switchWidget.getNumber() + "\n");
                }

            } else {
                validNetwork = false;
                printErrorToOutput("Warning first switch intermediate not found in switch " + switchWidget.getNumber() + "\n");
            }

            //is second switch intermediate connected to first output  in switch?
            if (switchWidget.getSecondIntermediateNode() != null) {
                if (switchWidget.getFirstOutputNode() != null) {
                    int mustLinkToSource = switchWidget.getSecondIntermediateNode().getSwitchIntermediateNodeProperties().getNumber();
                    int mustLinkToTarget = switchWidget.getFirstOutputNode().getSwitchOutputNodeProperties().getNumber();
                    boolean isLinked = false;
                    //search recursively in the next nodes links until we find the target node
                    for (int i = 0; i < switchWidget.getSecondIntermediateNode().getSwitchIntermediateNodeProperties().getNumbersOfNextNodes().size()
                            && !isLinked; i++) {

                        int nextNode = switchWidget.getSecondIntermediateNode().getSwitchIntermediateNodeProperties().getNumbersOfNextNodes().get(i);

                        while (ConnectorRepository.getInstance().find(nextNode) != null) {
                            MyConnectorWidget currentConnector = ConnectorRepository.getInstance().find(nextNode);
                            if (currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                                nextNode = currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().get(0);
                            }
                        }
                        //is next node is the target?
                        if (mustLinkToTarget == SwitchOutputRepository.getInstance().find(nextNode).getSwitchOutputNodeProperties().getNumber()) {
                            isLinked = true;
                            break;
                        }

                    }
                    if (isLinked) {
                        printTextToOutput("second switch intermediate [" + mustLinkToSource + "] is  linked  to first  output [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    } else {
                        printErrorToOutput("Warning: second switch intermediate [" + mustLinkToSource + "] is not linked to first output [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    }
                } else {
                    validNetwork = false;
                    printErrorToOutput("Warning first switch output not found in switch " + switchWidget.getNumber() + "\n");
                }

            } else {
                validNetwork = false;
                printErrorToOutput("Warning second switch intermediate not found in switch " + switchWidget.getNumber() + "\n");
            }

            //is second switch intermediate connected to second output  in switch?
            if (switchWidget.getSecondIntermediateNode() != null) {
                if (switchWidget.getSecondOutputNode() != null) {
                    int mustLinkToSource = switchWidget.getSecondIntermediateNode().getSwitchIntermediateNodeProperties().getNumber();
                    int mustLinkToTarget = switchWidget.getSecondOutputNode().getSwitchOutputNodeProperties().getNumber();
                    boolean isLinked = false;
                    //search recursively in the next nodes links until we find the target node
                    for (int i = 0; i < switchWidget.getSecondIntermediateNode().getSwitchIntermediateNodeProperties().getNumbersOfNextNodes().size()
                            && !isLinked; i++) {

                        int nextNode = switchWidget.getSecondIntermediateNode().getSwitchIntermediateNodeProperties().getNumbersOfNextNodes().get(i);

                        while (ConnectorRepository.getInstance().find(nextNode) != null) {
                            MyConnectorWidget currentConnector = ConnectorRepository.getInstance().find(nextNode);
                            if (currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                                nextNode = currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().get(0);
                            }
                        }
                        //is next node is the target?
                        if (mustLinkToTarget == SwitchOutputRepository.getInstance().find(nextNode).getSwitchOutputNodeProperties().getNumber()) {
                            isLinked = true;
                            break;
                        }

                    }
                    if (isLinked) {

                        printTextToOutput("second switch intermediate [" + mustLinkToSource + "] is  linked  to second  output [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    } else {
                        printErrorToOutput("Warning: second switch intermediate [" + mustLinkToSource + "] is not linked to second output [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    }
                } else {
                    validNetwork = false;
                    printErrorToOutput("Warning second switch output not found in switch " + switchWidget.getNumber() + "\n");
                }

            } else {
                validNetwork = false;
                printErrorToOutput("Warning second switch intermediate not found in switch " + switchWidget.getNumber() + "\n");
            }

            //is first switch output connected to first sensor output in switch?
            if (switchWidget.getFirstOutputNode() != null) {
                if (switchWidget.getFirstSensorOutputNode() != null) {
                    int mustLinkToSource = switchWidget.getFirstOutputNode().getSwitchOutputNodeProperties().getNumber();
                    int mustLinkToTarget = switchWidget.getFirstSensorOutputNode().getSensorProperties().getNumber();
                    boolean isLinked = false;
                    //search recursively in the next nodes links until we find the target node
                    for (int i = 0; i < switchWidget.getFirstOutputNode().getSwitchOutputNodeProperties().getNumbersOfNextNodes().size()
                            && !isLinked; i++) {

                        int nextNode = switchWidget.getFirstOutputNode().getSwitchOutputNodeProperties().getNumbersOfNextNodes().get(i);

                        while (ConnectorRepository.getInstance().find(nextNode) != null) {
                            MyConnectorWidget currentConnector = ConnectorRepository.getInstance().find(nextNode);
                            if (currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                                nextNode = currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().get(0);
                            }
                        }
                        //is next node is the target?
                        if (mustLinkToTarget == SwitchSensorRepository.getInstance().find(nextNode).getSensorProperties().getNumber()) {
                            isLinked = true;
                            break;
                        }

                    }
                    if (isLinked) {
                        printTextToOutput("first switch output [" + mustLinkToSource + "] is  linked  to first sensor output [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    } else {
                        printErrorToOutput("Warning: first switch output [" + mustLinkToSource + "] is not linked to first sensor output [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    }
                } else {
                    validNetwork = false;
                    printErrorToOutput("Warning first sensor output not found in switch " + switchWidget.getNumber() + "\n");
                }

            } else {
                validNetwork = false;
                printErrorToOutput("Warning first switch output not found in switch " + switchWidget.getNumber() + "\n");
            }

            //is second switch output connected to second sensor output in switch?
            if (switchWidget.getSecondOutputNode() != null) {
                if (switchWidget.getSecondSensorOutputNode() != null) {
                    int mustLinkToSource = switchWidget.getSecondOutputNode().getSwitchOutputNodeProperties().getNumber();
                    int mustLinkToTarget = switchWidget.getSecondSensorOutputNode().getSensorProperties().getNumber();
                    boolean isLinked = false;
                    //search recursively in the next nodes links until we find the target node
                    for (int i = 0; i < switchWidget.getSecondOutputNode().getSwitchOutputNodeProperties().getNumbersOfNextNodes().size()
                            && !isLinked; i++) {

                        int nextNode = switchWidget.getSecondOutputNode().getSwitchOutputNodeProperties().getNumbersOfNextNodes().get(i);

                        while (ConnectorRepository.getInstance().find(nextNode) != null) {
                            MyConnectorWidget currentConnector = ConnectorRepository.getInstance().find(nextNode);
                            if (currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().size() > 0) {
                                nextNode = currentConnector.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes().get(0);
                            }
                        }
                        //is next node is the target?
                        if (mustLinkToTarget == SwitchSensorRepository.getInstance().find(nextNode).getSensorProperties().getNumber()) {
                            isLinked = true;
                            break;
                        }

                    }
                    if (isLinked) {
                        printTextToOutput("second switch output [" + mustLinkToSource + "] is  linked  to second sensor output [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    } else {
                        printErrorToOutput("Warning: second switch output [" + mustLinkToSource + "] is not linked to second sensor output [" + mustLinkToTarget + "] in switch " + switchWidget.getNumber() + "\n");
                    }
                } else {
                    validNetwork = false;
                    printErrorToOutput("Warning second sensor output not found in switch " + switchWidget.getNumber() + "\n");
                }

            } else {
                validNetwork = false;
                printErrorToOutput("Warning second switch output not found in switch " + switchWidget.getNumber() + "\n");
            }
        }
    }

    /**
     * before attaching a shuttle to a connector, we need to test if we can
     * attach the shuttle to the connector link; only if the connector link is
     * part of a loop
     *
     * @param linkNodeNumber
     * @return
     */
    public boolean canWeAttachShuttleToLink(int linkNodeNumber) {
        boolean weCanAdd = false;
        if (arezzoCyclesWithIntegerNumbers == null || arezzoCyclesWithIntegerNumbers.isEmpty()) {
            actionPerformed(null);
            getOutput().closeInputOutput();
        }
        for (int i = 0; i < arezzoCyclesWithIntegerNumbers.size(); i++) {
            for (int j = 0; j < arezzoCyclesWithIntegerNumbers.get(i).size(); j++) {
                if (linkNodeNumber == arezzoCyclesWithIntegerNumbers.get(i).get(j)) {
                    weCanAdd = true;
                    break;
                }
            }
            if (weCanAdd) {
                break;
            }
        }
        return weCanAdd;

    }

    /**
     * initialize initializeAdjNodesMatrix by filling the adjacent nodes to
     * create a searchable graph if node 2 has as next node node 3 then
     * adjNodes[2][3] = true => they are adjacent name the nodes using "type of
     * node" + "number of node"
     */
    public void initializeAdjNodesMatrix() {
        //fill connector nodes
        for (MyConnectorWidget widget : ConnectorRepository.getInstance().getAll()) {
            int number = widget.getMyConnectorInfo().getConnectorProperties().getNumber();
            String label = "";
            String name = WidgetCommonInfo.WidgetType.LINK_NODE.toString() + " " + number;
            nodes[number] = name;
            for (int nextNodeNumber : widget.getMyConnectorInfo().getConnectorProperties().getNumbersOfNextNodes()) {
                adjNodes[number][nextNodeNumber] = true;
            }
        }
        //fill shuttle nodes
        //fill switchs
        //fill load/unload workstations
        for (MyLoadUnloadWorkstationWidget widget : LoadUnloadWorkstationRepository.getInstance().getAll()) {
            int number = widget.getLoadUnloadWorkstationProperties().getNumber();
            String label = "";
            String name = WidgetCommonInfo.WidgetType.LOAD_UNLOAD_WORKSTATION.toString() + " " + number;
            nodes[number] = name;

            for (int nextNodeNumber : widget.getLoadUnloadWorkstationProperties().getNumbersOfNextNodes()) {
                adjNodes[number][nextNodeNumber] = true;
            }
        }
        //fill workstations
        for (MyWorkstationWidget widget : WorkstationRepository.getInstance().getAll()) {
            int number = widget.getParentWorkstationProperties().getNumber();
            String name = WidgetCommonInfo.WidgetType.WORKSTATION.toString() + " " + number;
            nodes[number] = name;
            for (int nextNodeNumber : widget.getParentWorkstationProperties().getNumbersOfNextNodes()) {
                adjNodes[number][nextNodeNumber] = true;
            }
        }
        //fill load/unload sensors
        for (MyLoadUnloadSensorWidget widget : LoadUnloadSensorRepository.getInstance().getAll()) {
            int number = widget.getLoadUnloadSensorProperties().getNumber();
            String name = WidgetCommonInfo.WidgetType.LOAD_UNLOAD_SENSOR.toString() + " " + number;
            nodes[number] = name;
            for (int nextNodeNumber : widget.getLoadUnloadSensorProperties().getNumbersOfNextNodes()) {
                adjNodes[number][nextNodeNumber] = true;
            }
        }
        //fill sensors
        for (MySensorWidget widget : SwitchSensorRepository.getInstance().getAll()) {
            int number = widget.getSensorProperties().getNumber();
            String name = WidgetCommonInfo.WidgetType.SENSOR.toString() + " " + number;
            nodes[number] = name;
            for (int nextNodeNumber : widget.getSensorProperties().getNumbersOfNextNodes()) {
                adjNodes[number][nextNodeNumber] = true;
            }
        }
        //fill stop sensors
        for (MyStopSensorWidget widget : SwitchStopSensorRepository.getInstance().getAll()) {
            int number = widget.getStopSensorProperties().getNumber();
            String name = WidgetCommonInfo.WidgetType.STOP_SENSOR.toString() + " " + number;
            nodes[number] = name;
            for (int nextNodeNumber : widget.getStopSensorProperties().getNumbersOfNextNodes()) {
                adjNodes[number][nextNodeNumber] = true;
            }
        }
        //fill switch inputs
        for (MySwitchInputWidget widget : SwitchInputRepository.getInstance().getAll()) {
            int number = widget.getSwitchInputNodeProperties().getNumber();
            String name = WidgetCommonInfo.WidgetType.SWITCH_INPUT.toString() + " " + number;
            nodes[number] = name;
            for (int nextNodeNumber : widget.getSwitchInputNodeProperties().getNumbersOfNextNodes()) {
                adjNodes[number][nextNodeNumber] = true;
            }
        }
        //fill switch outputs
        for (MySwitchOutputWidget widget : SwitchOutputRepository.getInstance().getAll()) {
            int number = widget.getSwitchOutputNodeProperties().getNumber();
            String name = WidgetCommonInfo.WidgetType.SWITCH_OUTPUT.toString() + " " + number;
            nodes[number] = name;
            for (int nextNodeNumber : widget.getSwitchOutputNodeProperties().getNumbersOfNextNodes()) {
                adjNodes[number][nextNodeNumber] = true;
            }
        }
        //fill switch intermediates
        for (MySwitchIntermediateWidget widget : SwitchIntermediateRepository.getInstance().getAll()) {
            int number = widget.getSwitchIntermediateNodeProperties().getNumber();
            String name = WidgetCommonInfo.WidgetType.SWITCH_INTERMEDATE.toString() + " " + number;
            nodes[number] = name;
            for (int nextNodeNumber : widget.getSwitchIntermediateNodeProperties().getNumbersOfNextNodes()) {
                adjNodes[number][nextNodeNumber] = true;
            }
        }

    }

    /**
     * method that is called to print the nodes of each cycle
     *
     * @param cycle the cycle that contains nodes
     * @param i the index of the cycle in the list of the cycles
     */
    private void printCycleToVerificationOutput(List cycle, int i) {
        printTextToOutput("Cycle " + (i + 1) + ":\n");
        for (int j = 0; j < cycle.size(); j++) {
            String node = (String) cycle.get(j);
            if (j < cycle.size() - 1) {
                //System.out.print(node + " -> ");
                printTextToOutput(node + " -> ");
            } else {
                printTextToOutput(node);
            }
        }
        printTextToOutput("\n");

    }

    /**
     * print the text to the output window
     *
     * @param text the text to be printed to output
     */
    public void printTextToOutput(String text) {
        output.getOut().print(text);
    }

    /**
     * print the error text to the output window
     *
     * @param text the error text to be printed to output
     */
    public void printErrorToOutput(String text) {
        output.getErr().print(text);
    }

    /**
     *
     * @return the number of nodes in the network
     */
    public Integer getSizeOfNetwork() {
        return sizeOfNetwork;
    }

    /**
     *
     * @param sizeOfNetwork the number of nodes in the network
     */
    public void setSizeOfNetwork(Integer sizeOfNetwork) {
        this.sizeOfNetwork = sizeOfNetwork;
    }

    /**
     *
     * @return a list of the nodes informations
     */
    public String[] getNodes() {
        return nodes;
    }

    /**
     *
     * @param nodes a list of the nodes informations
     */
    public void setNodes(String[] nodes) {
        this.nodes = nodes;
    }

    /**
     *
     * @return the adjacent nodes matrix
     */
    public boolean[][] getAdjNodes() {
        return adjNodes;
    }

    /**
     *
     * @param adjNodes the adjacent nodes matrix
     */
    public void setAdjNodes(boolean[][] adjNodes) {
        this.adjNodes = adjNodes;
    }

    /**
     * this method is called to color the nodes of an arezzo cycle using a
     * random color
     *
     * @param arezzoCycleNodeNumbers a list of nodes that are considered part of
     * a cycle(a.k.a waiting loop)
     */
    private void colorCycle(List arezzoCycleNodeNumbers) {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        //RGB random color
        Color color = new Color(red, green, blue);
        for (Object o : Scene.connectionLayer.getChildren()) {

            MyConnectorWidget conn = (MyConnectorWidget) o;
            if (arezzoCycleNodeNumbers.contains(conn.getMyConnectorInfo().getConnectorProperties().getNumber())) {
                conn.setLineColor(color);
                Scene.globalScene.validate();
                Scene.globalScene.repaint();
            }

        }

    }

    /**
     * this method is called to remove the colors of the links
     */
    private void UncolorLinks() {

        //black color
        Color color = Color.BLACK;
        for (Object o : Scene.connectionLayer.getChildren()) {
            MyConnectorWidget conn = (MyConnectorWidget) o;
            conn.setLineColor(color);
            Scene.globalScene.validate();
            Scene.globalScene.repaint();
        }
    }

    /**
     * [i][j] matrix where i represents the index of the cycle and j represents
     * the number(ID) of the node
     *
     * @return a matrix that contains the representation of the nodes in each
     * cycle
     */
    public boolean[][] getCyclesMatrix() {
        return cyclesMatrix;
    }

    /**
     * [i][j] matrix where i represents the index of the cycle and j represents
     * the number(ID) of the node
     *
     * @param cyclesMatrix matrix that contains the representation of the nodes
     * in each cycle
     */
    public void setCyclesMatrix(boolean[][] cyclesMatrix) {
        this.cyclesMatrix = cyclesMatrix;
    }

    /**
     *
     * @return the output window
     */
    public InputOutput getOutput() {
        return output;
    }

    /**
     *
     * @param output the output window
     */
    public void setOutput(InputOutput output) {
        this.output = output;
    }

    /**
     *
     * @return a map of cycle number => a list of node's integer numbers
     */
    public Map<Integer, List<Integer>> getCyclesWithIntegerNumbers() {
        return cyclesWithIntegerNumbers;
    }

    /**
     *
     * @param cyclesWithIntegerNumbers a map of cycle number => a list of node's
     * integer numbers
     */
    public void setCyclesWithIntegerNumbers(Map<Integer, List<Integer>> cyclesWithIntegerNumbers) {
        this.cyclesWithIntegerNumbers = cyclesWithIntegerNumbers;
    }

    /**
     *
     * @return a map of arezzo simulator cycle number => a list of node's
     * integer numbers
     */
    public Map<Integer, List<Integer>> getArezzoCyclesWithIntegerNumbers() {
        return arezzoCyclesWithIntegerNumbers;
    }

    /**
     *
     * @param arezzoCyclesWithIntegerNumbers a map of arezzo simulator cycle
     * number => a list of node's integer numbers
     */
    public void setArezzoCyclesWithIntegerNumbers(Map<Integer, List<Integer>> arezzoCyclesWithIntegerNumbers) {
        this.arezzoCyclesWithIntegerNumbers = arezzoCyclesWithIntegerNumbers;
    }

    /**
     *
     * @return a map of arezzo simulator cycle number => a list of node's string
     * representation
     */
    public Map<Integer, List<String>> getArezzoCycles() {
        return arezzoCycles;
    }

    /**
     *
     * @param arezzoCycles a map of arezzo simulator cycle number => a list of
     * node's string representation
     */
    public void setArezzoCycles(Map<Integer, List<String>> arezzoCycles) {
        this.arezzoCycles = arezzoCycles;
    }

    /**
     *
     * @return a map of arezzo simulator cycle number => a list of node's string
     * representation
     */
    public List<String> getArezzoCycleConstraints() {
        return arezzoCycleConstraints;
    }

    /**
     *
     * @param arezzoCycleConstraints the constraints that filters all cycles
     */
    public void setArezzoCycleConstraints(List<String> arezzoCycleConstraints) {
        this.arezzoCycleConstraints = arezzoCycleConstraints;
    }

    /**
     *
     * @return the number of cycles (a.k.a waiting loops)
     */
    public int getNumberOfCycles() {
        return numberOfCycles;
    }

    /**
     *
     * @param numberOfCycles the number of cycles (a.k.a waiting loops)
     */
    public void setNumberOfCycles(int numberOfCycles) {
        this.numberOfCycles = numberOfCycles;
    }

    /**
     *
     * @return true if the cycle is to be added as an arezzo cycle (cycle to be
     * added to the simulator)
     */
    public boolean isArezzoCycleToAdd() {
        return arezzoCycleToAdd;
    }

    /**
     *
     * @param arezzoCycleToAdd true if the cycle is to be added as an arezzo
     * cycle (cycle to be added to the simulator)
     */
    public void setArezzoCycleToAdd(boolean arezzoCycleToAdd) {
        this.arezzoCycleToAdd = arezzoCycleToAdd;
    }

    /**
     *
     * @return true if the network is valid;network closed, if contains switch
     * must have all the components of a switch...
     */
    public boolean isValidNetwork() {
        return validNetwork;
    }

    /**
     *
     * @param validNetwork true if the network is valid;network closed, if
     * contains switch must have all the components of a switch...
     */
    public void setValidNetwork(boolean validNetwork) {
        this.validNetwork = validNetwork;
    }

    /**
     *singleton used to have only one instance in memory of this class and to be accessed from outside
     * @return the only instance of the verify network class
     */
    public static VerifyNetwork getInstance() {
        if (instance == null) {
            instance = new VerifyNetwork();
        }
        return instance;
    }

    /**
     * singleton used to have only one instance in memory of this class and to be accessed from outside
     * @param instance the only instance of the verify network class
     */
    public static void setInstance(VerifyNetwork instance) {
        VerifyNetwork.instance = instance;
    }

}
