package fr.arezzo.designer.Domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.nodes.PropertySupport;
import fr.arezzo.designer.DomainWidgets.MySwitchWidget;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MySensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyStopSensorWidget;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchStopSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchSensorRepository;
import fr.arezzo.designer.Scene.Scene;

/**
 * PropertiesOfNodesOfType4_8_9 represents the properties informations of type
 * 4, 8 and 9 nodes (load/unload sensor, sensor, stop sensor)
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class PropertiesOfNodesOfType4_8_9 extends IconNodeWidget {

    protected Integer number;
    protected Integer ID;
    protected Float xCoordinate;
    protected Float yCoordinate;
    protected Integer Type;
    protected Integer aipNumber;
    protected List<Float> speedsToNextNodes;
    protected List<Integer> numbersOfNextNodes;
    protected Integer parentSwitch;
    //if the node is the first in the switch (for instance first stop sensor)
    protected Boolean isFirstInParentSwitch = false;
    //if the node is the second in the switch (for instance first stop sensor)
    protected Boolean isSecondInParentSwitch = false;
    //if the intermediate switch node is the first in the switch 
    protected Boolean isFirstSensorNodeInParentSwitch = false;
    //if the intermediate switch node is the second in the switch 
    protected Boolean isSecondSensorNodeInParentSwitch = false;
    //new coordinates sytem for the simulator where point (0,0) represents the center of the network
    protected Float xCoordinateArezzo;
    protected Float yCoordinateArezzo;
    protected List<Integer> idsOfNextNodes = new ArrayList<>();
    

    /**
     * PropertiesOfNodesOfType4_8_9 constructor using the scene that contains
     * all of the widgets
     *
     * @param scene is the component that contains all of the widgets
     */
    public PropertiesOfNodesOfType4_8_9(org.netbeans.api.visual.widget.Scene scene) {
        super(scene);
        aipNumber = 0;
        speedsToNextNodes = new ArrayList<Float>();
        numbersOfNextNodes = new ArrayList<Integer>();
        Type = 4;

        xCoordinate = yCoordinate = 0.0f;
    }

    /**
     * PropertiesOfNodesOfType4_8_9 constructor
     *
     * @param scene is the component that contains all of the widgets
     * @param number is the number of the node(or id)
     * @param xCoordinate is the x coordinate inside the scene
     * @param yCoordinate is the y coordinate inside the scene
     * @param Type is the type of the node
     * @param aipNumber is the AIP number of the node
     * @param numbersOfNextNodes is a list of the numbers (or id) of the nodes
     * linked to this node
     * @param speedsToNextNodes is the list of speeds to the next nodes
     */
    public PropertiesOfNodesOfType4_8_9(Scene scene, Integer number, Float xCoordinate, Float yCoordinate, Integer Type, Integer aipNumber, List<Integer> numbersOfNextNodes, List<Float> speedsToNextNodes) {
        super(scene);
        this.number = number;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.Type = Type;
        this.aipNumber = aipNumber;
        this.numbersOfNextNodes = numbersOfNextNodes;
        this.speedsToNextNodes = speedsToNextNodes;

    }

    public List<Integer> getIdsOfNextNodes() {
        return idsOfNextNodes;
    }

    public void setIdsOfNextNodes(List<Integer> idsOfNextNodes) {
        this.idsOfNextNodes = idsOfNextNodes;
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
        if(number > WidgetCommonInfo.biggestNumberAssignedToANode)
        {
            WidgetCommonInfo.biggestNumberAssignedToANode = number;
        }
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
     * @return the number of the parent switch of the switch component (switch
     * input, switch output, switch intermediate...)
     */
    public Integer getParentSwitch() {
        return parentSwitch;
    }

    /**
     *
     * @param parentSwitch is the number of the parent switch of the switch
     * component (switch input, switch output, switch intermediate...)
     */
    public void setParentSwitch(Integer parentSwitch) {
        this.parentSwitch = parentSwitch;
    }

    /**
     *
     * @return whether the switch component is the first one of its kind in the
     * switch
     */
    public Boolean getIsFirstInParentSwitch() {
        return isFirstInParentSwitch;
    }

    /**
     *
     * @param isFirstInParentSwitch is whether the switch component is the first
     * one of its kind in the switch
     */
    public void setIsFirstInParentSwitch(Boolean isFirstInParentSwitch) {
        this.isFirstInParentSwitch = isFirstInParentSwitch;
    }

    /**
     *
     * @return whether the switch component is the second one of its kind in the
     * switch
     */
    public Boolean getIsSecondInParentSwitch() {
        return isSecondInParentSwitch;
    }

    /**
     *
     * @param isSecondInParentSwitch is whether the switch component is the
     * second one of its kind in the switch
     */
    public void setIsSecondInParentSwitch(Boolean isSecondInParentSwitch) {
        this.isSecondInParentSwitch = isSecondInParentSwitch;
    }

    /**
     *
     * @return whether the switch stop sensor component is the first one of its
     * kind in the switch
     */
    public Boolean getIsFirstSensorNodeInParentSwitch() {
        return isFirstSensorNodeInParentSwitch;
    }

    /**
     *
     * @param isFirstSensorNodeInParentSwitch is whether the switch stop sensor
     * component is the first one of its kind in the switch
     */
    public void setIsFirstSensorNodeInParentSwitch(Boolean isFirstSensorNodeInParentSwitch) {
        this.isFirstSensorNodeInParentSwitch = isFirstSensorNodeInParentSwitch;
    }

    /**
     *
     * @return whether the sensor component is the second one of its kind in the
     * switch
     */
    public Boolean getIsSecondSensorNodeInParentSwitch() {
        return isSecondSensorNodeInParentSwitch;
    }

    /**
     *
     * @param isSecondSensorNodeInParentSwitch is whether the sensor component
     * is the second one of its kind in the switch
     */
    public void setIsSecondSensorNodeInParentSwitch(Boolean isSecondSensorNodeInParentSwitch) {
        this.isSecondSensorNodeInParentSwitch = isSecondSensorNodeInParentSwitch;
    }

    /**
     * NumberProperty represents the "number" property for this node
     *
     */
    public static class NumberProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of this node
         */
        public NumberProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) {
            super("Number", String.class, "Number", "Edit the Number");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return the value of the "number" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            
            return PropertiesOfNodeObject.getNumber() + "";
        }

        /**
         *
         * @param t the value of the "number" property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setNumber(Integer.parseInt(t.toString()));
        }

    }

    /**
     * NextNodeSpeedProperty represents a property for the speed of a next node
     */
    public static class NextNodeSpeedProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;
        private Integer nextNodeNumber;
        private Integer nextNodeIndex;

        /**
         * 
         * @param PropertiesOfNodeObject the properties of the node
         * @param nextNodeNumber the number of the next node
         * @param nextNodeIndex the index of the next node in the list of next nodes
         */
        public NextNodeSpeedProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject, Integer nextNodeNumber, Integer nextNodeIndex) {
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
         * @param t is the value of the speed to the next node
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

    
    /**isFirstSensorNodeInParentSwitchProperty is a property to know if  the switch component sensor node of the switch is the first one
     * 
     */
    public static class isFirstSensorNodeInParentSwitchProperty extends PropertySupport.ReadOnly<Boolean> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isFirstSensorNodeInParentSwitchProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) {
            super("Is First Sensor Node", Boolean.class, "Is First Sensor Node", "Edit the First Sensor Node In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return the value of this property represents whether the switch component sensor node of the switch is the first one
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsFirstSensorNodeInParentSwitch();
        }

        /**
         * 
         * @param t the value of this property represents whether the switch component sensor node of the switch is the first one
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException 
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsFirstSensorNodeInParentSwitch(Boolean.parseBoolean(t.toString()));
            //reverse first node and second node if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                //switch between first and second inputs of this switch if they are already initialized
                //if the second becomes the first => the first becomes the second (permutation)
                //else initialize only the first
                MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
                MySensorWidget currentFirstSensorForSwitch = switchW.getFirstSensorOutputNode();
                MySensorWidget currentSecondSensorForSwitch = switchW.getSecondSensorOutputNode();
                MySensorWidget currentTempSensorForSwitch = null; //temp for permutation
                if (currentSecondSensorForSwitch != null && currentSecondSensorForSwitch.getSensorProperties().getNumber() == PropertiesOfNodeObject.getNumber()
                        && currentFirstSensorForSwitch != null) {
                    currentTempSensorForSwitch = currentFirstSensorForSwitch;
                    currentFirstSensorForSwitch = currentSecondSensorForSwitch;
                    currentSecondSensorForSwitch = currentTempSensorForSwitch;
                    PropertiesOfNodeObject.setIsSecondSensorNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    currentFirstSensorForSwitch.getSensorProperties().setIsFirstSensorNodeInParentSwitch(true);
                    currentFirstSensorForSwitch.getSensorProperties().setIsSecondSensorNodeInParentSwitch(false);

                    PropertiesOfNodeObject.setIsFirstSensorNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsSecondSensorNodeInParentSwitch(false);

                    currentSecondSensorForSwitch.getSensorProperties().setIsSecondSensorNodeInParentSwitch(true);
                    currentSecondSensorForSwitch.getSensorProperties().setIsFirstSensorNodeInParentSwitch(false);

                    switchW.setFirstSensorOutputNode(currentFirstSensorForSwitch);
                    switchW.setSecondSensorOutputNode(currentSecondSensorForSwitch);
                    //update switch
                    currentFirstSensorForSwitch.getSensorProperties().setParentSwitch(switchW.getNumber());
                    currentSecondSensorForSwitch.getSensorProperties().setParentSwitch(switchW.getNumber());

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    

                } else {
                    if (currentFirstSensorForSwitch != null) {
                        currentFirstSensorForSwitch.getSensorProperties().setIsFirstSensorNodeInParentSwitch(false);
                    }

                    MySensorWidget currentinputSwitch = SwitchSensorRepository.getInstance().find(PropertiesOfNodeObject.getNumber());
                    currentinputSwitch.getSensorProperties().setIsFirstSensorNodeInParentSwitch(true);

                    PropertiesOfNodeObject.setIsFirstSensorNodeInParentSwitch(false);
                    PropertiesOfNodeObject.setIsSecondSensorNodeInParentSwitch(true);
                    switchW.setFirstSensorOutputNode(currentinputSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);

                    
                }

            }
        }

    }

    
    /**isSecondSensorNodeInParentSwitchProperty is a property to know if  the switch component sensor node of the switch is the second one
     * 
     */
    public static class isSecondSensorNodeInParentSwitchProperty extends PropertySupport.ReadOnly<Boolean> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isSecondSensorNodeInParentSwitchProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) {
            super("Is Second Sensor Node", Boolean.class, "Is Second Sensor Node", "Edit the Second Sensor Node In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return the value of this property represents whether the switch component sensor node of the switch is the second one
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsSecondSensorNodeInParentSwitch();
        }

        /**
         * 
         * @param t the value of this property represents whether the switch component sensor node of the switch is the second one
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException 
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsSecondSensorNodeInParentSwitch(Boolean.parseBoolean(t.toString()));
            //reverse first node and second node if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                //switch between first and second stop sensors of this switch if they are already initialized
                //if the second becomes the first => the first becomes the second (permutation)
                //else initialize only the first
                MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
                MySensorWidget currentFirstSensorForSwitch = switchW.getFirstSensorOutputNode();
                MySensorWidget currentSecondSensorForSwitch = switchW.getSecondSensorOutputNode();
                MySensorWidget currentTempSensorForSwitch = null; //temp for permutation
                if (currentFirstSensorForSwitch != null && currentFirstSensorForSwitch.getSensorProperties().getNumber() == PropertiesOfNodeObject.getNumber()
                        && currentSecondSensorForSwitch != null) {
                    currentTempSensorForSwitch = currentFirstSensorForSwitch;
                    currentFirstSensorForSwitch = currentSecondSensorForSwitch;
                    currentSecondSensorForSwitch = currentTempSensorForSwitch;
                    PropertiesOfNodeObject.setIsSecondSensorNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    currentFirstSensorForSwitch.getSensorProperties().setIsFirstSensorNodeInParentSwitch(false);
                    currentFirstSensorForSwitch.getSensorProperties().setIsSecondSensorNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsFirstSensorNodeInParentSwitch(false);
                    PropertiesOfNodeObject.setIsSecondSensorNodeInParentSwitch(true);

                    currentSecondSensorForSwitch.getSensorProperties().setIsSecondSensorNodeInParentSwitch(false);
                    currentSecondSensorForSwitch.getSensorProperties().setIsFirstSensorNodeInParentSwitch(true);

                    switchW.setFirstSensorOutputNode(currentFirstSensorForSwitch);
                    switchW.setSecondSensorOutputNode(currentSecondSensorForSwitch);
                    //update switch
                    currentFirstSensorForSwitch.getSensorProperties().setParentSwitch(switchW.getNumber());
                    currentSecondSensorForSwitch.getSensorProperties().setParentSwitch(switchW.getNumber());

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    

                } else {
                    if (currentSecondSensorForSwitch != null) {
                        currentSecondSensorForSwitch.getSensorProperties().setIsSecondSensorNodeInParentSwitch(false);
                    }

                    MySensorWidget currentinputSwitch = SwitchSensorRepository.getInstance().find(PropertiesOfNodeObject.getNumber());
                    currentinputSwitch.getSensorProperties().setIsSecondSensorNodeInParentSwitch(true);

                    switchW.setSecondSensorOutputNode(currentinputSwitch);

                    PropertiesOfNodeObject.setIsFirstSensorNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsSecondSensorNodeInParentSwitch(false);
                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }
            }
        }

    }

    
    /**isFirstStopSensorInSwitchProperty is a property to know if  the stop sensor node of the switch is the first one
     * 
     */
    public static class isFirstStopSensorInSwitchProperty extends PropertySupport.ReadOnly<Boolean> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isFirstStopSensorInSwitchProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) {
            super("Is First Node", Boolean.class, "Is First Node", "Edit the First Node In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return whether the stop sensor node of the switch is the first one
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsFirstInParentSwitch();
        }

        /**
         * 
         * @param t whether the stop sensor node of the switch is the first one
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException 
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsFirstInParentSwitch(Boolean.parseBoolean(t.toString()));
            //reverse first node and second node if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                //switch between first and second stop sensors of this switch if they are already initialized
                //if the second becomes the first => the first becomes the second (permutation)
                //else initialize only the first
                MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
                MyStopSensorWidget currentFirstStopSensorForSwitch = switchW.getFirstEndStopSensorNode();
                MyStopSensorWidget currentSecondStopSensorForSwitch = switchW.getSecondEndStopSensorNode();
                MyStopSensorWidget currentTempStopSensorForSwitch = null; //temp for permutation
                if (currentSecondStopSensorForSwitch != null && currentSecondStopSensorForSwitch.getStopSensorProperties().getNumber() == PropertiesOfNodeObject.getNumber()
                        && currentFirstStopSensorForSwitch != null) 
                {
                    currentTempStopSensorForSwitch = currentFirstStopSensorForSwitch;
                    currentFirstStopSensorForSwitch = currentSecondStopSensorForSwitch;
                    currentSecondStopSensorForSwitch = currentTempStopSensorForSwitch;
                    PropertiesOfNodeObject.setIsSecondInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    currentFirstStopSensorForSwitch.getStopSensorProperties().setIsFirstInParentSwitch(true);
                    currentFirstStopSensorForSwitch.getStopSensorProperties().setIsSecondInParentSwitch(false);
                    PropertiesOfNodeObject.setIsFirstInParentSwitch(true);
                    PropertiesOfNodeObject.setIsSecondInParentSwitch(false);

                    currentSecondStopSensorForSwitch.getStopSensorProperties().setIsSecondInParentSwitch(true);
                    currentSecondStopSensorForSwitch.getStopSensorProperties().setIsFirstInParentSwitch(false);

                    switchW.setFirstEndStopSensorNode(currentFirstStopSensorForSwitch);
                    switchW.setSecondEndStopSensorNode(currentSecondStopSensorForSwitch);
                    //update switch
                    currentFirstStopSensorForSwitch.getStopSensorProperties().setParentSwitch(switchW.getNumber());
                    currentSecondStopSensorForSwitch.getStopSensorProperties().setParentSwitch(switchW.getNumber());

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    PropertiesOfNodeObject.setIsSecondInParentSwitch(!Boolean.parseBoolean(t.toString()));

                } else {
                    if (currentFirstStopSensorForSwitch != null) {
                        currentFirstStopSensorForSwitch.getStopSensorProperties().setIsFirstInParentSwitch(false);
                    }

                    MyStopSensorWidget currentStopSensor = SwitchStopSensorRepository.getInstance().find(PropertiesOfNodeObject.getNumber());
                    currentStopSensor.getStopSensorProperties().setIsFirstInParentSwitch(true);

                    switchW.setFirstEndStopSensorNode(currentStopSensor);
                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);

                    //PropertiesOfNodeObject.setIsSecondInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    PropertiesOfNodeObject.setIsFirstInParentSwitch(false);
                    PropertiesOfNodeObject.setIsSecondInParentSwitch(true);
                }

                //reverse second input property
                //new isSecondStopSensorInSwitchProperty(PropertiesOfNodeObject).setValue(!Boolean.parseBoolean(t.toString()));
            }
        }

    }

    /**isSecondStopSensorInSwitchProperty is a property to know if  the stop sensor node of the switch is the second one
     * 
     */
    public static class isSecondStopSensorInSwitchProperty extends PropertySupport.ReadOnly<Boolean> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;
        
        /**
         * 
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isSecondStopSensorInSwitchProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) {
            super("Is Second Node", Boolean.class, "Is Second Node", "Edit the Second Node In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return a boolean about whether the stop sensor node of the switch is the second one
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsSecondInParentSwitch();
        }

        /**
         * 
         * @param t is a boolean about whether the stop sensor node of the switch is the second one
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException 
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsSecondInParentSwitch(Boolean.parseBoolean(t.toString()));
            //reverse first node and second node if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                //switch between first and second stop sensors of this switch if they are already initialized
                //if the second becomes the first => the first becomes the second (permutation)
                //else initialize only the first
                MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
                MyStopSensorWidget currentFirstStopSensorForSwitch = switchW.getFirstEndStopSensorNode();
                MyStopSensorWidget currentSecondStopSensorForSwitch = switchW.getSecondEndStopSensorNode();
                MyStopSensorWidget currentTempStopSensorForSwitch = null; //temp for permutation
                if (currentFirstStopSensorForSwitch != null && currentFirstStopSensorForSwitch.getStopSensorProperties().getNumber() == PropertiesOfNodeObject.getNumber()
                        && currentSecondStopSensorForSwitch != null) {
                    currentTempStopSensorForSwitch = currentFirstStopSensorForSwitch;
                    currentFirstStopSensorForSwitch = currentSecondStopSensorForSwitch;
                    currentSecondStopSensorForSwitch = currentTempStopSensorForSwitch;
                    PropertiesOfNodeObject.setIsSecondInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    currentFirstStopSensorForSwitch.getStopSensorProperties().setIsFirstInParentSwitch(false);
                    PropertiesOfNodeObject.setIsFirstInParentSwitch(false);

                    currentFirstStopSensorForSwitch.getStopSensorProperties().setIsSecondInParentSwitch(true);
                    PropertiesOfNodeObject.setIsSecondInParentSwitch(true);

                    currentSecondStopSensorForSwitch.getStopSensorProperties().setIsSecondInParentSwitch(false);

                    currentSecondStopSensorForSwitch.getStopSensorProperties().setIsFirstInParentSwitch(true);

                    switchW.setFirstEndStopSensorNode(currentFirstStopSensorForSwitch);
                    switchW.setSecondEndStopSensorNode(currentSecondStopSensorForSwitch);
                    //update switch
                    currentFirstStopSensorForSwitch.getStopSensorProperties().setParentSwitch(switchW.getNumber());
                    currentSecondStopSensorForSwitch.getStopSensorProperties().setParentSwitch(switchW.getNumber());

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);

                    //PropertiesOfNodeObject.setIsFirstInParentSwitch(!Boolean.parseBoolean(t.toString()));
                } else {
                    if (currentSecondStopSensorForSwitch != null) {
                        currentSecondStopSensorForSwitch.getStopSensorProperties().setIsSecondInParentSwitch(false);
                    }

                    MyStopSensorWidget currentStopSensor = SwitchStopSensorRepository.getInstance().find(PropertiesOfNodeObject.getNumber());
                    currentStopSensor.getStopSensorProperties().setIsSecondInParentSwitch(true);

                    switchW.setSecondEndStopSensorNode(currentStopSensor);
                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);

                    //PropertiesOfNodeObject.setIsFirstInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    PropertiesOfNodeObject.setIsFirstInParentSwitch(true);
                    PropertiesOfNodeObject.setIsSecondInParentSwitch(false);

                }
            }
            //reverse first input property
            //new isFirstStopSensorInSwitchProperty(PropertiesOfNodeObject).setValue(!Boolean.parseBoolean(t.toString()));
        }

    }

    /**SwitchProperty is a property referencing the "parent switch of the switch component"
     * 
     */
    public static class SwitchProperty extends PropertySupport.ReadOnly<String> 
    {

        
        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject the properties of the node
         */
        public SwitchProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) {
            super("Switch", String.class, "Switch", "Edit the Number of the Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return the value that represents the number of the parent switch
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getParentSwitch() + "";
        }

        /**
         * 
         * @param t the value that represents the number of the parent switch
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException 
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setParentSwitch(Integer.parseInt(t.toString()));
        }

    }

    /**
     * XCoordinateArezzoProperty represents the "x coordinate in arezzo
     * coordinates system" property
     *
     */
    public static class XCoordinateArezzoProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject the properties of the node
         */
        public XCoordinateArezzoProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) {
            super("XCoordinate In Arezzo", String.class, "XCoordinate In Arezzo", "Edit the X Coordinate IN Arezzo");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return the value of the "x coordinate in arezzo coordinates system"
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getxCoordinateArezzo() + "";
        }

        /**
         * 
         * @param t the value of the "x coordinate in arezzo coordinates system"
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException 
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setyCoordinateArezzo(Float.parseFloat(t.toString()));
        }

    }

    /**
     * YCoordinateArezzoProperty represents the "y coordinate in arezzo
     * coordinates system" property
     *
     */
    public static class YCoordinateArezzoProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject the properties of the node
         */
        public YCoordinateArezzoProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) 
        {
            super("Y Coordinate In Arezzo", String.class, "Y Coordinate In Arezzo", "Edit the Y Coordinate In Arezzo");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return the value of the "y coordinate in arezzo coordinates system"
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getyCoordinateArezzo() + "";
        }

        /**
         * 
         * @param t the value of the "y coordinate in arezzo coordinates system"
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException 
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setyCoordinateArezzo(Float.parseFloat(t.toString()));
        }

    }

    /**
     * XCoordinateProperty represents the "x coordinate" property of the node
     *
     */
    public static class XCoordinateProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject the properties of the node
         */
        public XCoordinateProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) {
            super("XCoordinate", String.class, "XCoordinate", "Edit the X Coordinate");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return the value of the x coordinate of the node in the scene
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getxCoordinate() + "";
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
            PropertiesOfNodeObject.setxCoordinate(Float.parseFloat(t.toString()));
        }

    }

    /**
     * YCoordinateProperty represents the "y coordinate" property
     *
     */
    public static class YCoordinateProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject the properties of the node
         */
        public YCoordinateProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) {
            super("YCoordinate", String.class, "YCoordinate", "Edit the Y Coordinate");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return the value of the y coordinate property
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getyCoordinate() + "";
        }

        /**
         * 
         * @param t is the value of the y coordinate property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException 
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setyCoordinate(Float.parseFloat(t.toString()));
        }

    }

    /**
     * TypeProperty represents the "type of node" property
     *
     */
    public static class TypeProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject is the properties of the node
         */
        public TypeProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) {
            super("Type", String.class, "Type", "Edit the Type");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return the value of the "type of node" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getType() + "";
        }

        /**
         * 
         * @param t is the value of the "type of node" property
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException 
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setType(Integer.parseInt(t.toString()));
        }

    }

    /**
     * AipNumberProperty represents the "AIP number" property
     *
     */
    public static class AipNumberProperty extends PropertySupport.ReadWrite<String> 
    {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject is the properties of the node
         */
        public AipNumberProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) 
        {
            super("AIPnumber", String.class, "AIPnumber", "Edit the AIP number");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return the value of the "AIP number" property
         * @throws IllegalAccessException
         * @throws InvocationTargetException 
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getAipNumber() + "";
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
            PropertiesOfNodeObject.setAipNumber(Integer.parseInt(t.toString()));
        }

    }

    /**
     * SpeedsToNextNodesProperty represents the "list of the speeds to the next
     * nodes" property
     *
     */
    public static class SpeedsToNextNodesProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject is the properties of the node
         */
        public SpeedsToNextNodesProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) 
        {
            super("SpeedsToNextNodes", String.class, "SpeedsToNextNodes", "Edit the Speeds To the Next Nodes From This Node, using numbers seperated by spaces like: 10 22 ");

            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
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
            for (Float s : PropertiesOfNodeObject.getSpeedsToNextNodes()) {
                result += s + " ";
            }
            return result;
        }

        /**
         * 
         * @param t the value of "list of the speeds to the next nodes" property
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
            PropertiesOfNodeObject.setSpeedsToNextNodes(result);
        }

    }

    /**
     * NumbersOfNextNodesProperty represents the "list of the numbers to the
     * next nodes" property
     *
     */
    
    public static class NumbersOfNextNodesProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject;

        /**
         * 
         * @param PropertiesOfNodeObject is the properties of the node
         */
        public NumbersOfNextNodesProperty(PropertiesOfNodesOfType4_8_9 PropertiesOfNodeObject) 
        {
            super("NumbersOfNextNodes", String.class, "NumbersOfNextNodes", "Edit the numbers of the next nodes in the network, using numbers seperated by spaces like: 10 22 when the next nodes are nodes number 10 and 22 "
                    + "for the operations  taking respectively 11 seconds and 22 seconds and 33 seconds and 44 seconds");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
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
            for (Integer s : PropertiesOfNodeObject.getNumbersOfNextNodes()) {
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
            PropertiesOfNodeObject.setNumbersOfNextNodes(result);
        }

    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
    
    

}
