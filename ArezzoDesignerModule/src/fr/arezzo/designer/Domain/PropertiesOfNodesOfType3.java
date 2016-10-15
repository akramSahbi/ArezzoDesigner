package fr.arezzo.designer.Domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.nodes.PropertySupport;
import fr.arezzo.designer.Scene.Scene;

/**
 * PropertiesOfNodesOfType3 represents the properties informations of type 3
 * nodes (workstation)
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class PropertiesOfNodesOfType3 extends IconNodeWidget {

    protected Integer number;
    protected Float xCoordinate;
    protected Float yCoordinate;
    //new coordinates sytem for the simulator where point (0,0) represents the center of the network
    protected Float xCoordinateArezzo;
    protected Float yCoordinateArezzo;

    protected Integer Type;
    protected Integer aipNumber;
    protected Integer numberOfTheMachine; //user defined
    protected Integer waitingQueueMaximumSize;

    protected List<Integer> operationsOfThisMachine;
    protected List<Integer> durationsOfOperations;
    protected List<Float> speedsToNextNodes;
    protected List<Integer> numbersOfNextNodes;

    /**
     * PropertiesOfNodesOfType3 constructor using the scene that contains all of
     * the widgets
     *
     * @param scene is the component that contains all of the widgets
     */
    public PropertiesOfNodesOfType3(org.netbeans.api.visual.widget.Scene scene) {
        super(scene);
        operationsOfThisMachine = new ArrayList<Integer>();
        durationsOfOperations = new ArrayList<Integer>();
        speedsToNextNodes = new ArrayList<Float>();
        numbersOfNextNodes = new ArrayList<Integer>();
        Type = 3;
        number = aipNumber = numberOfTheMachine = waitingQueueMaximumSize = 0;
        xCoordinate = yCoordinate = 0.0f;
    }

    /**
     * PropertiesOfNodesOfType3 constructor
     *
     * @param scene is the component that contains all of the widgets
     * @param number is the number of the node
     * @param xCoordinate is the x coordinate inside the scene
     * @param yCoordinate is the y coordinate inside the scene
     * @param Type is the type of the node
     * @param aipNumber is the AIP number of the node
     * @param numbersOfNextNodes is a list of the numbers (or id) of the nodes
     * linked to this node
     * @param speedsToNextNodes is the list of speeds to the next nodes
     * @param numberOfTheMachine is the number of the machine
     * @param operationsOfThisMachine is a list of operations for this machine
     * @param durationsOfOperations is a list of durations of the operations for
     * this machine
     * @param waitingQueueMaximumSize is the size of the queue of this machine
     */
    public PropertiesOfNodesOfType3(Scene scene, Integer number, Float xCoordinate, Float yCoordinate, Integer Type, Integer aipNumber, List<Integer> numbersOfNextNodes, List<Float> speedsToNextNodes, Integer numberOfTheMachine, List<Integer> operationsOfThisMachine, List<Integer> durationsOfOperations, Integer waitingQueueMaximumSize) {
        super(scene);
        this.number = number;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.Type = Type;
        this.aipNumber = aipNumber;
        this.numbersOfNextNodes = numbersOfNextNodes;
        this.speedsToNextNodes = speedsToNextNodes;
        this.numberOfTheMachine = numberOfTheMachine;
        this.operationsOfThisMachine = operationsOfThisMachine;
        this.durationsOfOperations = durationsOfOperations;
        this.waitingQueueMaximumSize = waitingQueueMaximumSize;
    }

    /**
     *
     * @return the number of the node (id)
     */
    public Integer getNumber() {
        return number;
    }

    /**
     *
     * @param number is the number of the node (id)
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     *
     * @return the x coordinate of the node on the scene
     */
    public Float getxCoordinate() {
        return xCoordinate;
    }

    /**
     *
     * @param xCoordinate the x coordinate of the node on the scene
     */
    public void setxCoordinate(Float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    /**
     *
     * @return the y coordinate of the node on the scene
     */
    public Float getyCoordinate() {
        return yCoordinate;
    }

    /**
     *
     * @param yCoordinate the y coordinate of the node on the scene
     */
    public void setyCoordinate(Float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    /**
     *
     * @return the type of the node
     */
    public Integer getType() {
        return Type;
    }

    /**
     *
     * @param Type the type of the node
     */
    public void setType(Integer Type) {
        this.Type = Type;
    }

    /**
     *
     * @return the AIP number of the node
     */
    public Integer getAipNumber() {
        return aipNumber;
    }

    /**
     *
     * @param aipNumber the AIP number of the node
     */
    public void setAipNumber(Integer aipNumber) {
        this.aipNumber = aipNumber;
    }

    /**
     *
     * @return a list of the numbers of the next nodes (linked to this node)
     */
    public List<Integer> getNumbersOfNextNodes() {
        return numbersOfNextNodes;
    }

    /**
     *
     * @param numbersOfNextNodes a list of the numbers of the next nodes (linked
     * to this node)
     */
    public void setNumbersOfNextNodes(List<Integer> numbersOfNextNodes) {
        this.numbersOfNextNodes = numbersOfNextNodes;
    }

    /**
     *
     * @return a list of the speeds to the next nodes (linked to this node)
     */
    public List<Float> getSpeedsToNextNodes() {
        return speedsToNextNodes;
    }

    /**
     *
     * @param speedsToNextNodes a list of the speeds to the next nodes (linked
     * to this node)
     */
    public void setSpeedsToNextNodes(List<Float> speedsToNextNodes) {
        this.speedsToNextNodes = speedsToNextNodes;
    }

    /**
     *
     * @return the number of the machine
     */
    public Integer getNumberOfTheMachine() {
        return numberOfTheMachine;
    }

    /**
     *
     * @param numberOfTheMachine the number of the machine
     */
    public void setNumberOfTheMachine(Integer numberOfTheMachine) {
        this.numberOfTheMachine = numberOfTheMachine;
    }

    /**
     *
     * @return a list of the operations of this machine
     */
    public List<Integer> getOperationsOfThisMachine() {
        return operationsOfThisMachine;
    }

    /**
     *
     * @param operationsOfThisMachine a list of the operations of this machine
     */
    public void setOperationsOfThisMachine(List<Integer> operationsOfThisMachine) {
        this.operationsOfThisMachine = operationsOfThisMachine;
    }

    /**
     *
     * @return a list of durations of the operations of this machine
     */
    public List<Integer> getDurationsOfOperations() {
        return durationsOfOperations;
    }

    /**
     *
     * @param durationsOfOperations a list of durations of the operations of
     * this machine
     */
    public void setDurationsOfOperations(List<Integer> durationsOfOperations) {
        this.durationsOfOperations = durationsOfOperations;
    }

    /**
     *
     * @return the size of the queue of this machine
     */
    public Integer getWaitingQueueMaximumSize() {
        return waitingQueueMaximumSize;
    }

    /**
     *
     * @param waitingQueueMaximumSize the size of the queue of this machine
     */
    public void setWaitingQueueMaximumSize(Integer waitingQueueMaximumSize) {
        this.waitingQueueMaximumSize = waitingQueueMaximumSize;
    }

    /**
     *
     * @return the x coordinate in arezzo coordination system
     */
    public Float getxCoordinateArezzo() {
        return xCoordinateArezzo;
    }

    /**
     *
     * @param xCoordinateArezzo is the x coordinate in arezzo coordination
     * system
     */
    public void setxCoordinateArezzo(Float xCoordinateArezzo) {
        this.xCoordinateArezzo = xCoordinateArezzo;
    }

    /**
     *
     * @return the y coordinate in arezzo coordination system
     */
    public Float getyCoordinateArezzo() {
        return yCoordinateArezzo;
    }

    /**
     *
     * @param yCoordinateArezzo the y coordinate in arezzo coordination system
     */
    public void setyCoordinateArezzo(Float yCoordinateArezzo) {
        this.yCoordinateArezzo = yCoordinateArezzo;
    }

    /**
     * NumberProperty represents the "number" property for this node
     *
     */
    public static class NumberProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject the properties of this node
         */
        public NumberProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("Number", String.class, "Number", "Edit the Number");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "number" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return workstationObject.getNumber() + "";
        }

        /**
         *
         * @param t id the value of the "number" property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            workstationObject.setNumber(Integer.parseInt(t.toString()));
        }

    }

    /**
     * NextNodeSpeedProperty represents a property for the speed of a next node
     */
    public static class NextNodeSpeedProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfType3 PropertiesOfNodeObject;
        private Integer nextNodeNumber;
        private Integer nextNodeIndex;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         * @param nextNodeNumber the number of the next node
         * @param nextNodeIndex the index of the next node in the list of next
         * nodes
         */
        public NextNodeSpeedProperty(PropertiesOfNodesOfType3 PropertiesOfNodeObject, Integer nextNodeNumber, Integer nextNodeIndex) {
            super("Speed to next node " + nextNodeNumber, String.class, "Speed to next node " + nextNodeNumber,
                    "Edit Speed to next node " + nextNodeNumber);
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
            this.nextNodeNumber = nextNodeNumber;
            this.nextNodeIndex = nextNodeIndex;
        }

        /**
         *
         * @return the value of the speed to the next node
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            try {
                int currentIndexInSpeeds = nextNodeIndex;
                nextNodeIndex = PropertiesOfNodeObject.getNumbersOfNextNodes().indexOf(nextNodeNumber);
                //check if next nodes has changed

                if (nextNodeIndex != -1) {
                    if (PropertiesOfNodeObject.getSpeedsToNextNodes().get(nextNodeIndex) != null) {
                        return PropertiesOfNodeObject.getSpeedsToNextNodes().get(nextNodeIndex) + "";
                    } else {
                        return "";
                    }
                } else {
                    if (PropertiesOfNodeObject.getSpeedsToNextNodes().get(currentIndexInSpeeds) != null) {
                        PropertiesOfNodeObject.getSpeedsToNextNodes().remove(currentIndexInSpeeds);

                    }
                    return "";
                }
            } catch (IndexOutOfBoundsException ex) {
                //if there is a difference between the size of the numberOfNextNodes list and the speedForNextNodes list
                //we must fill
                int sizeDifference_NextNodes_speedsToNextNodes = PropertiesOfNodeObject.getNumbersOfNextNodes().size() - PropertiesOfNodeObject.getSpeedsToNextNodes().size();
                if (sizeDifference_NextNodes_speedsToNextNodes > 0) {
                    for (int i = 0; i < sizeDifference_NextNodes_speedsToNextNodes; i++) {
                        PropertiesOfNodeObject.getSpeedsToNextNodes().add(0.0f);
                    }
                    return PropertiesOfNodeObject.getSpeedsToNextNodes().get(nextNodeIndex) + "";
                }
                return "";
            }

        }

        /**
         *
         * @param t the value of the speed to the next node
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            nextNodeIndex = PropertiesOfNodeObject.getNumbersOfNextNodes().indexOf(nextNodeNumber);
            PropertiesOfNodeObject.getSpeedsToNextNodes().set(nextNodeIndex, Float.parseFloat(t.toString()));
        }

    }

    /**
     * OperationDurationProperty represents a property for the duration of an
     * operation for the machine
     */
    public static class OperationDurationProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfType3 PropertiesOfNodeObject;
        private Integer operationNumber;
        private String operationName = "";
        private Integer operationIndex;

        /**
         *
         * @param PropertiesOfNodeObject is the properties of the node
         * @param operationNumber is the number of the operation
         * @param operationIndex is the index of the operation in the list of
         * operations of this node
         * @param operationName is the name of the operation
         */
        public OperationDurationProperty(PropertiesOfNodesOfType3 PropertiesOfNodeObject, Integer operationNumber, Integer operationIndex, String operationName) {
            super("Duration of operation " + operationName, String.class, "duration of operation " + operationName,
                    "Edit Duration of operation " + operationName);
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
            this.operationNumber = operationNumber;
            this.operationIndex = operationIndex;
            this.operationName = operationName;

        }

        /**
         *
         * @return the duration of the operation
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            try {
                int currentIndexInSpeeds = operationIndex;
                operationIndex = PropertiesOfNodeObject.getOperationsOfThisMachine().indexOf(operationNumber);

                if (operationIndex != -1) {
                    if (PropertiesOfNodeObject.getDurationsOfOperations().get(operationIndex) != null) {
                        return PropertiesOfNodeObject.getDurationsOfOperations().get(operationIndex) + "";
                    } else {
                        return "";
                    }
                } else {
                    if (PropertiesOfNodeObject.getDurationsOfOperations().get(currentIndexInSpeeds) != null) {
                        PropertiesOfNodeObject.getDurationsOfOperations().remove(currentIndexInSpeeds);

                    }
                    return "";
                }
            } catch (IndexOutOfBoundsException ex) {
                //if there is a difference between the size of the numberOfNextNodes list and the speedForNextNodes list
                //we must fill
                int sizeDifference_NextNodes_speedsToNextNodes = PropertiesOfNodeObject.getOperationsOfThisMachine().size() - PropertiesOfNodeObject.getDurationsOfOperations().size();
                if (sizeDifference_NextNodes_speedsToNextNodes > 0) {
                    for (int i = 0; i < sizeDifference_NextNodes_speedsToNextNodes; i++) {
                        PropertiesOfNodeObject.getDurationsOfOperations().add(0);
                    }
                    return PropertiesOfNodeObject.getDurationsOfOperations().get(operationIndex) + "";
                }
                return "";
            }

        }

        /**
         *
         * @param t the duration of the operation
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            operationIndex = PropertiesOfNodeObject.getOperationsOfThisMachine().indexOf(operationNumber);
            PropertiesOfNodeObject.getDurationsOfOperations().set(operationIndex, Integer.parseInt(t.toString()));
        }

    }

    /**
     * XCoordinateProperty represents the "x coordinate" property of the node
     *
     */
    public static class XCoordinateProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject the properties of the node
         */
        public XCoordinateProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("XCoordinate", String.class, "XCoordinate", "Edit the X Coordinate");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the x coordinate of the node in the scene
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return workstationObject.getxCoordinate() + "";
        }

        /**
         *
         * @param t the value of the x coordinate of the node in the scene
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            workstationObject.setxCoordinate(Float.parseFloat(t.toString()));
        }

    }

    /**
     * YCoordinateProperty represents the "y coordinate" property
     *
     */
    public static class YCoordinateProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject the properties of the node
         */
        public YCoordinateProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("YCoordinate", String.class, "YCoordinate", "Edit the Y Coordinate");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the y coordinate property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return workstationObject.getyCoordinate() + "";
        }

        /**
         *
         * @param t the value of the y coordinate property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            workstationObject.setyCoordinate(Float.parseFloat(t.toString()));
        }

    }

    /**
     * XCoordinateArezzoProperty represents the "x coordinate in arezzo
     * coordinates system" property
     *
     */
    public static class XCoordinateArezzoProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject the properties of the node
         */
        public XCoordinateArezzoProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("XCoordinate In Arezzo", String.class, "XCoordinate In Arezzo", "Edit the X Coordinate INn Arezzo");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "x coordinate in arezzo coordinates system"
         * property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return workstationObject.getxCoordinateArezzo() + "";
        }

        /**
         *
         * @param t the value of the "x coordinate in arezzo coordinates system"
         * property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            workstationObject.setyCoordinateArezzo(Float.parseFloat(t.toString()));
        }

    }

    /**
     * YCoordinateArezzoProperty represents the "y coordinate in arezzo
     * coordinates system" property
     *
     */
    public static class YCoordinateArezzoProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject the properties of the node
         */
        public YCoordinateArezzoProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("Y Coordinate In Arezzo", String.class, "Y Coordinate In Arezzo", "Edit the Y Coordinate In Arezzo");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "y coordinate in arezzo coordinates system"
         * property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return workstationObject.getyCoordinateArezzo() + "";
        }

        /**
         *
         * @param t the value of the "y coordinate in arezzo coordinates system"
         * property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            workstationObject.setyCoordinateArezzo(Float.parseFloat(t.toString()));
        }

    }

    /**
     * TypeProperty represents the "type of node" property
     *
     */
    public static class TypeProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject is the properties of the node
         */
        public TypeProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("Type", String.class, "Type", "Edit the Type");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "type of node" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return workstationObject.getType() + "";
        }

        /**
         *
         * @param t the value of the "type of node" property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            workstationObject.setType(Integer.parseInt(t.toString()));
        }

    }

    /**
     * AipNumberProperty represents the "AIP number" property
     *
     */
    public static class AipNumberProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject is the properties of the node
         */
        public AipNumberProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("AIPnumber", String.class, "AIPnumber", "Edit the AIP number");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "AIP number" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return workstationObject.getAipNumber() + "";
        }

        /**
         *
         * @param t the value of the "AIP number" property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            workstationObject.setAipNumber(Integer.parseInt(t.toString()));
        }

    }

    /**
     * NumberOfTheMachineProperty represents the "number of the machine"
     * property
     *
     */
    public static class NumberOfTheMachineProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject is the properties of the node
         */
        public NumberOfTheMachineProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("NumberOfTheMachine", String.class, "NumberOfTheMachine", "Edit the Number Of The Machine");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "number of the machine" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return workstationObject.getNumberOfTheMachine() + "";
        }

        /**
         *
         * @param t the value of the "number of the machine" property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            workstationObject.setNumberOfTheMachine(Integer.parseInt(t.toString()));
        }

    }

    /**
     * WaitingQueueMaximumSizeProperty represents the "size of queue of the
     * machine" property
     *
     */
    public static class WaitingQueueMaximumSizeProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject the properties of the node
         */
        public WaitingQueueMaximumSizeProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("WaitingQueueMaximumSize", String.class, "WaitingQueueMaximumSize", "Edit the Waiting Queue Maximum Size Of The Machine");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "size of queue of the machine" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return workstationObject.getWaitingQueueMaximumSize() + "";
        }

        /**
         *
         * @param t the value of the "size of queue of the machine" property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            workstationObject.setWaitingQueueMaximumSize(Integer.parseInt(t.toString()));
        }

    }

    /**
     * OperationsOfThisMachineProperty represents the "list of the operations of
     * the machine" property
     *
     */
    public static class OperationsOfThisMachineProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject the properties of the node
         */
        public OperationsOfThisMachineProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("OperationsOfThisMachine", String.class, "OperationsOfThisMachine", "Edit the Operations Of The Machine, using numbers seperated by spaces like: 1 2 3 4 for the operations 1 and 2 and 3 and 4");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "list of the operations of the machine"
         * property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            String result = "";
            if (workstationObject.getOperationsOfThisMachine().size() >= 1) {
                for (Integer s : workstationObject.getOperationsOfThisMachine()) {
                    result += s + " ";
                }
            }

            return result;
        }

        /**
         *
         * @param t the value of the "list of the operations of the machine"
         * property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            List<Integer> result = new ArrayList<Integer>();
            String[] values = t.toString().split(" ");
            for (String value : values) {
                Integer intValue = Integer.parseInt(value);
                result.add(intValue);
            }
            workstationObject.setOperationsOfThisMachine(result);
        }

    }

    /**
     * OperationsOfThisMachineOfLoadUnloadWorkstationProperty represents the
     * "list of the operations of the load/unload machine" property
     *
     */
    public static class OperationsOfThisMachineOfLoadUnloadWorkstationProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject the properties of the node
         */
        public OperationsOfThisMachineOfLoadUnloadWorkstationProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("OperationsOfThisMachine", String.class, "OperationsOfThisMachine", "Click On Enter or click the right button then OK to Edit the Operations Of The Machine by chosing the type of its"
                    + "operations Loading or Unloading or both");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "list of the operations of the load/unload
         * machine" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            String result1 = "";
            for (Integer s : workstationObject.getOperationsOfThisMachine()) {
                result1 += s + " ";
            }

            return result1;
        }

        /**
         *
         * @param t the value of the "list of the operations of the load/unload
         * machine" property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            String[] values = t.toString().split(" ");
            List<Integer> result = new ArrayList<Integer>();
            for (String value : values) {
                Integer intValue = Integer.parseInt(value);
                result.add(intValue);
            }
            workstationObject.setOperationsOfThisMachine(result);
        }

    }

    /**
     * DurationsOfOperationsProperty represents the "list of the durations of
     * the operations of the machine" property
     *
     */
    public static class DurationsOfOperationsProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject represents the properties of the node
         */
        public DurationsOfOperationsProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("DurationsOfOperations", String.class, "DurationsOfOperations", "Edit the Durations in seconds Of The Operations Of The Machine, using numbers seperated by spaces like: 10 22 33 44 "
                    + "for the operations  taking respectively 11 seconds and 22 seconds and 33 seconds and 44 seconds");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "list of the durations of the operations of
         * the machine" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            String result = "";
            for (Integer s : workstationObject.getDurationsOfOperations()) {
                result += s + " ";
            }
            return result;
        }

        /**
         *
         * @param t is the value of the "list of the durations of the operations
         * of the machine" property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            List<Integer> result = new ArrayList<Integer>();
            String[] values = t.toString().split(" ");
            for (String value : values) {
                Integer intValue = Integer.parseInt(value);
                result.add(intValue);
            }
            workstationObject.setDurationsOfOperations(result);
        }

    }

    /**
     * SpeedsToNextNodesProperty represents the "list of the speeds to the next
     * nodes" property
     *
     */
    public static class SpeedsToNextNodesProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         *
         * @param workstationObject is the properties of the node
         */
        public SpeedsToNextNodesProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("SpeedsToNextNodes", String.class, "SpeedsToNextNodes", "Edit the Speeds To the Next Nodes From The Machine, using numbers seperated by spaces like: 10 22 ");

            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of "list of the speeds to the next nodes" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            String result = "";
            for (Float s : workstationObject.getSpeedsToNextNodes()) {
                result += s + " ";
            }
            return result;
        }

        /**
         *
         * @param t is the value of "list of the speeds to the next nodes"
         * property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            List<Float> result = new ArrayList<Float>();
            String[] values = t.toString().split(" ");
            for (String value : values) {
                Float floatValue = Float.parseFloat(value);
                result.add(floatValue);
            }
            workstationObject.setSpeedsToNextNodes(result);
        }

    }

    /**
     * NumbersOfNextNodesProperty represents the "list of the numbers to the
     * next nodes" property
     *
     */
    public static class NumbersOfNextNodesProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType3 workstationObject;

        /**
         * 
         * @param workstationObject is the properties of the node
         */
        public NumbersOfNextNodesProperty(PropertiesOfNodesOfType3 workstationObject) {
            super("NumbersOfNextNodes", String.class, "NumbersOfNextNodes", "Edit the numbers of the next nodes in the network, using numbers seperated by spaces like: 10 22 when the next nodes are nodes number 10 and 22 "
                    + "for the operations  taking respectively 11 seconds and 22 seconds and 33 seconds and 44 seconds");
            this.workstationObject = workstationObject;
        }

        /**
         *
         * @return the value of the "list of the numbers to the next nodes"
         * property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            String result = "";
            for (Integer s : workstationObject.getNumbersOfNextNodes()) {
                result += s + " ";
            }
            return result;
        }

        /**
         *
         * @param t is the value of the "list of the numbers to the next nodes"
         * property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            List<Integer> result = new ArrayList<Integer>();
            String[] values = t.toString().split(" ");
            for (String value : values) {
                Integer intValue = Integer.parseInt(value);
                result.add(intValue);
            }
            workstationObject.setNumbersOfNextNodes(result);
        }

    }
}
