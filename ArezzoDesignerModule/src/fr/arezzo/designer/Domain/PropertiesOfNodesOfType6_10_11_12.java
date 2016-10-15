package fr.arezzo.designer.Domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.nodes.PropertySupport;
import fr.arezzo.designer.DomainWidgets.MySwitchWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchInputWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchIntermediateWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchOutputWidget;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchInputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchIntermediateRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchOutputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchRepository;
import fr.arezzo.designer.Scene.Scene;
import java.util.Objects;

/**
 * PropertiesOfNodesOfType6_10_11_12 represents the properties informations of
 * type 6, 10, 11 and 12 nodes (topology nodes, switch input, switch
 * intermediate, switch output)
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class PropertiesOfNodesOfType6_10_11_12 extends IconNodeWidget {

    protected Integer number;
    protected Float xCoordinate;
    protected Float yCoordinate;
    protected Integer Type;

    protected List<Float> speedsToNextNodes;
    protected List<Integer> numbersOfNextNodes;

    protected Integer parentSwitch;
    //if the switch is connected to the first switch input
    protected Boolean isSwitchConnectedToFirstInput = false;
    //if the switch is connected to the second switch input
    protected Boolean isSwitchConnectedToSecondInput = false;
    //if the switch is connected to the first switch output
    protected Boolean isSwitchConnectedToFirstOutput = false;
    //if the switch is connected to the second switch output
    protected Boolean isSwitchConnectedToSecondOutput = false;

    //if the input node is the first in the switch 
    protected Boolean isFirstInputNodeInParentSwitch = false;
    //if the input node is the second in the switch 
    protected Boolean isSecondInputNodeInParentSwitch = false;

    //if the intermediate switch node is the first in the switch 
    protected Boolean isFirstIntermediateNodeInParentSwitch = false;
    //if the intermediate switch node is the second in the switch 
    protected Boolean isSecondIntermediateNodeInParentSwitch = false;

    //if the Output node is the first in the switch 
    protected Boolean isFirstOutputNodeInParentSwitch = false;
    //if the Output node is the second in the switch 
    protected Boolean isSecondOutputNodeInParentSwitch = false;
    //new coordinates sytem for the simulator where point (0,0) represents the center of the network
    protected Float xCoordinateArezzo;
    protected Float yCoordinateArezzo;

    /**
     * PropertiesOfNodesOfType6_10_11_12 constructor using the scene that
     * contains all of the widgets
     *
     * @param scene is the component that contains all of the widgets
     */
    public PropertiesOfNodesOfType6_10_11_12(org.netbeans.api.visual.widget.Scene scene) {
        super(scene);
        speedsToNextNodes = new ArrayList<>();
        numbersOfNextNodes = new ArrayList<>();
        Type = 99;
        xCoordinate = yCoordinate = 0.0f;
    }

    /**
     * PropertiesOfNodesOfType6_10_11_12 constructor
     *
     * @param scene is the component that contains all of the widgets
     * @param number is the number of the node(or id)
     * @param xCoordinate is the x coordinate inside the scene
     * @param yCoordinate is the y coordinate inside the scene
     * @param Type is the type of the node
     * @param numbersOfNextNodes is a list of the numbers (or id) of the nodes
     * linked to this node
     * @param speedsToNextNodes is the list of speeds to the next nodes
     */
    public PropertiesOfNodesOfType6_10_11_12(Scene scene, Integer number, Float xCoordinate, Float yCoordinate, Integer Type, List<Integer> numbersOfNextNodes, List<Float> speedsToNextNodes) {
        super(scene);
        this.number = number;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.Type = Type;

        this.numbersOfNextNodes = numbersOfNextNodes;
        this.speedsToNextNodes = speedsToNextNodes;

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
     * @return a boolean about whether the first switch input component in the
     * switch is connected (true)
     */
    public Boolean getIsSwitchConnectedToFirstInput() {
        return isSwitchConnectedToFirstInput;
    }

    /**
     *
     * @param isSwitchConnectedToFirstInput is a boolean about whether the first
     * switch input component in the switch is connected (true)
     */
    public void setIsSwitchConnectedToFirstInput(Boolean isSwitchConnectedToFirstInput) {
        //first and second input are mutually exclusive
        this.isSwitchConnectedToFirstInput = isSwitchConnectedToFirstInput;
        this.isSwitchConnectedToSecondInput = !isSwitchConnectedToFirstInput;
    }

    /**
     *
     * @return a boolean about whether the second switch input component in the
     * switch is connected (true)
     */
    public Boolean getIsSwitchConnectedToSecondInput() {
        return isSwitchConnectedToSecondInput;
    }

    /**
     *
     * @param isSwitchConnectedToSecondInput is a boolean about whether the
     * second switch input component in the switch is connected (true)
     */
    public void setIsSwitchConnectedToSecondInput(Boolean isSwitchConnectedToSecondInput) {
        //first and second input are mutually exclusive
        this.isSwitchConnectedToSecondInput = isSwitchConnectedToSecondInput;
        this.isSwitchConnectedToFirstInput = !isSwitchConnectedToSecondInput;
    }

    /**
     *
     * @return a boolean about whether the first switch output component in the
     * switch is connected (true)
     */
    public Boolean getIsSwitchConnectedToFirstOutput() {
        return isSwitchConnectedToFirstOutput;
    }

    /**
     *
     * @param isSwitchConnectedToFirstOutput is is a boolean about whether the
     * first switch output component in the switch is connected (true)
     */
    public void setIsSwitchConnectedToFirstOutput(Boolean isSwitchConnectedToFirstOutput) {
        this.isSwitchConnectedToFirstOutput = isSwitchConnectedToFirstOutput;
    }

    /**
     *
     * @return a boolean about whether the second switch output component in the
     * switch is connected (true)
     */
    public Boolean getIsSwitchConnectedToSecondOutput() {
        return isSwitchConnectedToSecondOutput;
    }

    /**
     *
     * @param isSwitchConnectedToSecondOutput is a boolean about whether the
     * second switch output component in the switch is connected (true)
     */
    public void setIsSwitchConnectedToSecondOutput(Boolean isSwitchConnectedToSecondOutput) {
        this.isSwitchConnectedToSecondOutput = isSwitchConnectedToSecondOutput;
    }

    /**
     *
     * @return a boolean about whether the switch input component is the first
     * one of its kind in the switch
     */
    public Boolean getIsFirstInputNodeInParentSwitch() {
        return isFirstInputNodeInParentSwitch;
    }

    /**
     *
     * @param isFirstInputNodeInParentSwitch is a boolean about whether the
     * switch input component is the first one of its kind in the switch
     */
    public void setIsFirstInputNodeInParentSwitch(Boolean isFirstInputNodeInParentSwitch) {
        //the same switch component can be first or second in the switch (mutually exclusive)
        this.isFirstInputNodeInParentSwitch = isFirstInputNodeInParentSwitch;
        this.isSecondInputNodeInParentSwitch = !isFirstInputNodeInParentSwitch;
    }

    /**
     *
     * @return a boolean about whether the switch input component is the second
     * one of its kind in the switch
     */
    public Boolean getIsSecondInputNodeInParentSwitch() {
        return isSecondInputNodeInParentSwitch;
    }

    /**
     *
     * @param isSecondInputNodeInParentSwitch is a boolean about whether the
     * switch input component is the second one of its kind in the switch
     */
    public void setIsSecondInputNodeInParentSwitch(Boolean isSecondInputNodeInParentSwitch) {
        //the same switch component can be first or second in the switch (mutually exclusive)
        this.isSecondInputNodeInParentSwitch = isSecondInputNodeInParentSwitch;
        this.isFirstInputNodeInParentSwitch = !isSecondInputNodeInParentSwitch;
    }

    /**
     *
     * @return a boolean about whether the switch intermediate component is the
     * first one of its kind in the switch
     */
    public Boolean getIsFirstIntermediateNodeInParentSwitch() {
        return isFirstIntermediateNodeInParentSwitch;
    }

    /**
     *
     * @param isFirstIntermediateNodeInParentSwitch is a boolean about whether
     * the switch intermediate component is the first one of its kind in the
     * switch
     */
    public void setIsFirstIntermediateNodeInParentSwitch(Boolean isFirstIntermediateNodeInParentSwitch) {
        //the same switch component can be first or second in the switch (mutually exclusive)
        this.isFirstIntermediateNodeInParentSwitch = isFirstIntermediateNodeInParentSwitch;
        this.isSecondIntermediateNodeInParentSwitch = !isFirstIntermediateNodeInParentSwitch;
    }

    /**
     *
     * @return a boolean about whether the switch intermediate component is the
     * second one of its kind in the switch
     */
    public Boolean getIsSecondIntermediateNodeInParentSwitch() {
        return isSecondIntermediateNodeInParentSwitch;
    }

    /**
     *
     * @param isSecondIntermediateNodeInParentSwitch is a boolean about whether
     * the switch intermediate component is the second one of its kind in the
     * switch
     */
    public void setIsSecondIntermediateNodeInParentSwitch(Boolean isSecondIntermediateNodeInParentSwitch) {
        //the same switch component can be first or second in the switch (mutually exclusive)
        this.isSecondIntermediateNodeInParentSwitch = isSecondIntermediateNodeInParentSwitch;
        this.isFirstIntermediateNodeInParentSwitch = !isSecondIntermediateNodeInParentSwitch;
    }

    /**
     *
     * @return a boolean about whether the switch output component is the first
     * one of its kind in the switch
     */
    public Boolean getIsFirstOutputNodeInParentSwitch() {
        return isFirstOutputNodeInParentSwitch;
    }

    /**
     *
     * @param isFirstOutputNodeInParentSwitch is a boolean about whether the
     * switch output component is the first one of its kind in the switch
     */
    public void setIsFirstOutputNodeInParentSwitch(Boolean isFirstOutputNodeInParentSwitch) {
        //the same switch component can be first or second in the switch (mutually exclusive)
        this.isFirstOutputNodeInParentSwitch = isFirstOutputNodeInParentSwitch;
        this.isSecondOutputNodeInParentSwitch = !isFirstOutputNodeInParentSwitch;
    }

    /**
     *
     * @return a boolean about whether the switch output component is the second
     * one of its kind in the switch
     */
    public Boolean getIsSecondOutputNodeInParentSwitch() {
        return isSecondOutputNodeInParentSwitch;
    }

    /**
     *
     * @param isSecondOutputNodeInParentSwitch is a boolean about whether the
     * switch output component is the second one of its kind in the switch
     */
    public void setIsSecondOutputNodeInParentSwitch(Boolean isSecondOutputNodeInParentSwitch) {
        //the same switch component can be first or second in the switch (mutually exclusive)
        this.isSecondOutputNodeInParentSwitch = isSecondOutputNodeInParentSwitch;
        this.isFirstOutputNodeInParentSwitch = !isSecondOutputNodeInParentSwitch;
    }

    /**
     * NextNodeSpeedProperty represents a property for the speed of a next node
     */
    public static class NextNodeSpeedProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;
        private Integer nextNodeNumber;
        private Integer nextNodeIndex;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         * @param nextNodeNumber the number of the next node
         * @param nextNodeIndex the index of the next node in the list of next
         * nodes
         */
        public NextNodeSpeedProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject, Integer nextNodeNumber, Integer nextNodeIndex) {
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

    /**
     * NumberProperty represents the "number" property for this node
     *
     */
    public static class NumberProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of this node
         */
        public NumberProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
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
         * @param t is the value of the "number" property
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
     * SwitchProperty is a property referencing the "parent switch of the switch
     * component"
     *
     */
    public static class SwitchProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public SwitchProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
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
         * @param t is the value that represents the number of the parent switch
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
     * isFirstInputNodeInParentSwitchProperty is a property referencing whether
     * "the input node of the switch is the first one"
     *
     */
    public static class isFirstInputNodeInParentSwitchProperty extends PropertySupport.ReadOnly<Boolean> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isFirstInputNodeInParentSwitchProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("Is First Input Node", Boolean.class, "Is First Input Node", "Edit the First Input Node In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return value that represents whether the switch input node is the
         * first one of its kind in the switch
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsFirstInputNodeInParentSwitch();
        }

        /**
         *
         * @param t is a value that represents whether the switch input node is
         * the first one of its kind in the switch
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsFirstInputNodeInParentSwitch(Boolean.parseBoolean(t.toString()));

            //reverse first node and second node if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                //switch between first and second inputs of this switch if they are already initialized
                //if the second becomes the first => the first becomes the second (permutation)
                //else initialize only the first
                MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
                MySwitchInputWidget currentFirstInputForSwitch = switchW.getFirstInputNode();
                MySwitchInputWidget currentSecondInputForSwitch = switchW.getSecondInputNode();
                MySwitchInputWidget currentTempInputForSwitch ; //temp for permutation
                if (currentSecondInputForSwitch != null && Objects.equals(currentSecondInputForSwitch.getSwitchInputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentFirstInputForSwitch != null) {
                    currentTempInputForSwitch = currentFirstInputForSwitch;
                    currentFirstInputForSwitch = currentSecondInputForSwitch;
                    currentSecondInputForSwitch = currentTempInputForSwitch;
                    PropertiesOfNodeObject.setIsSecondInputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    currentFirstInputForSwitch.getSwitchInputNodeProperties().setIsFirstInputNodeInParentSwitch(true);
                    currentFirstInputForSwitch.getSwitchInputNodeProperties().setIsSecondInputNodeInParentSwitch(false);

                    PropertiesOfNodeObject.setIsFirstInputNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsSecondInputNodeInParentSwitch(false);

                    currentSecondInputForSwitch.getSwitchInputNodeProperties().setIsSecondInputNodeInParentSwitch(true);
                    currentSecondInputForSwitch.getSwitchInputNodeProperties().setIsFirstInputNodeInParentSwitch(false);

                    switchW.setFirstInputNode(currentFirstInputForSwitch);
                    switchW.setSecondInputNode(currentSecondInputForSwitch);
                    //update switch
                    currentFirstInputForSwitch.getSwitchInputNodeProperties().setParentSwitch(switchW.getNumber());
                    currentSecondInputForSwitch.getSwitchInputNodeProperties().setParentSwitch(switchW.getNumber());

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    //PropertiesOfNodeObject.setIsSecondInputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));

                } else {
                    if (currentFirstInputForSwitch != null) {
                        currentFirstInputForSwitch.getSwitchInputNodeProperties().setIsFirstInputNodeInParentSwitch(false);
                    }

                    MySwitchInputWidget currentinputSwitch = SwitchInputRepository.getInstance().find(PropertiesOfNodeObject.getNumber());
                    currentinputSwitch.getSwitchInputNodeProperties().setIsFirstInputNodeInParentSwitch(true);

                    PropertiesOfNodeObject.setIsFirstInputNodeInParentSwitch(false);
                    PropertiesOfNodeObject.setIsSecondInputNodeInParentSwitch(true);
                    switchW.setFirstInputNode(currentinputSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }
            }

        }

    }

    /**
     * isSecondInputNodeInParentSwitchProperty is a property referencing whether
     * "the input node of the switch is the second one of its kind in the
     * switch"
     *
     */
    public static class isSecondInputNodeInParentSwitchProperty extends PropertySupport.ReadOnly<Boolean> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isSecondInputNodeInParentSwitchProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("Is Second Input Node", Boolean.class, "Is Second Input Node", "Edit the Second Input Node In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return whether the input node of the switch is the second one of its
         * kind in the switch
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsSecondInputNodeInParentSwitch();
        }

        /**
         *
         * @param t whether the input node of the switch is the second one of
         * its kind in the switch
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsSecondInputNodeInParentSwitch(Boolean.parseBoolean(t.toString()));
            //reverse first node and second node if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                //switch between first and second stop sensors of this switch if they are already initialized
                //if the second becomes the first => the first becomes the second (permutation)
                //else initialize only the first
                MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
                MySwitchInputWidget currentFirstInputForSwitch = switchW.getFirstInputNode();
                MySwitchInputWidget currentSecondInputForSwitch = switchW.getSecondInputNode();
                MySwitchInputWidget currentTempInputForSwitch; //temp for permutation
                if (currentFirstInputForSwitch != null && Objects.equals(currentFirstInputForSwitch.getSwitchInputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentSecondInputForSwitch != null) {
                    currentTempInputForSwitch = currentFirstInputForSwitch;
                    currentFirstInputForSwitch = currentSecondInputForSwitch;
                    currentSecondInputForSwitch = currentTempInputForSwitch;
                    PropertiesOfNodeObject.setIsSecondInputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    currentFirstInputForSwitch.getSwitchInputNodeProperties().setIsFirstInputNodeInParentSwitch(false);
                    currentFirstInputForSwitch.getSwitchInputNodeProperties().setIsSecondInputNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsFirstInputNodeInParentSwitch(false);
                    PropertiesOfNodeObject.setIsSecondInputNodeInParentSwitch(true);

                    currentSecondInputForSwitch.getSwitchInputNodeProperties().setIsSecondInputNodeInParentSwitch(false);
                    currentSecondInputForSwitch.getSwitchInputNodeProperties().setIsFirstInputNodeInParentSwitch(true);

                    switchW.setFirstInputNode(currentFirstInputForSwitch);
                    switchW.setSecondInputNode(currentSecondInputForSwitch);
                    //update switch
                    currentFirstInputForSwitch.getSwitchInputNodeProperties().setParentSwitch(switchW.getNumber());
                    currentSecondInputForSwitch.getSwitchInputNodeProperties().setParentSwitch(switchW.getNumber());

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    //PropertiesOfNodeObject.setIsFirstInputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));

                } else {
                    if (currentSecondInputForSwitch != null) {
                        currentSecondInputForSwitch.getSwitchInputNodeProperties().setIsSecondInputNodeInParentSwitch(false);
                    }

                    MySwitchInputWidget currentinputSwitch = SwitchInputRepository.getInstance().find(PropertiesOfNodeObject.getNumber());
                    currentinputSwitch.getSwitchInputNodeProperties().setIsSecondInputNodeInParentSwitch(true);

                    switchW.setSecondInputNode(currentinputSwitch);

                    PropertiesOfNodeObject.setIsFirstInputNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsSecondInputNodeInParentSwitch(false);
                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }
            }
        }

    }

    /**
     * isSwitchConnectedToFirstInputProperty is a property referencing whether
     * "the first input node of the switch is the one connected to the switch"
     *
     */
    public static class isSwitchConnectedToFirstInputProperty extends PropertySupport.ReadWrite<Boolean> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isSwitchConnectedToFirstInputProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("Is First Input Node Connected", Boolean.class, "Is First Input Node Connected", "Edit the First Input Node Connected In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return whether the first input is the one connected to the switch
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsSwitchConnectedToFirstInput();
        }

        /**
         *
         * @param t whether the first input is the one connected to the switch
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsSwitchConnectedToFirstInput(Boolean.parseBoolean(t.toString()));

            //switch between first and second inputs connected values
            MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
            MySwitchInputWidget currentFirstInputForSwitch = switchW.getFirstInputNode();
            MySwitchInputWidget currentSecondInputForSwitch = switchW.getSecondInputNode();

            //set first input connected in switch
            switchW.setSecondInputConnected(!Boolean.parseBoolean(t.toString()));
            //if second input in scene, update it
            if (currentSecondInputForSwitch != null) {
                //update second switch input which is mutually exclusive (input1 connected => input2 not connected and vice verca)
                MySwitchInputWidget switchInput2InScene = (MySwitchInputWidget) Scene.globalScene.getMyWidgetsAdded().get(currentSecondInputForSwitch.getWidget());
                switchInput2InScene.getSwitchInputNodeProperties().setIsSwitchConnectedToFirstInput(Boolean.parseBoolean(t.toString()));
                switchInput2InScene.getSwitchInputNodeProperties().setIsSwitchConnectedToSecondInput(!Boolean.parseBoolean(t.toString()));
            }

            //reverse first node and second node connected values if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                if (currentSecondInputForSwitch != null && Objects.equals(currentSecondInputForSwitch.getSwitchInputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentFirstInputForSwitch != null) {

                    //deconnect second switch input 
                    currentSecondInputForSwitch.getSwitchInputNodeProperties().setIsSwitchConnectedToSecondInput(false);

                    //update repository
                    switchW.setFirstInputNode(currentFirstInputForSwitch);
                    switchW.setSecondInputNode(currentSecondInputForSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    //PropertiesOfNodeObject.setIsSecondInputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));

                }

            } //if false
            else {

                if (currentSecondInputForSwitch != null && Objects.equals(currentSecondInputForSwitch.getSwitchInputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentFirstInputForSwitch != null) {

                    //connect second switch input 
                    currentSecondInputForSwitch.getSwitchInputNodeProperties().setIsSwitchConnectedToSecondInput(true);

                    switchW.setFirstInputNode(currentFirstInputForSwitch);
                    switchW.setSecondInputNode(currentSecondInputForSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }
            }
        }

    }

    /**
     * isSwitchConnectedToSecondInputProperty is a property referencing whether
     * "the second input node of the switch is the one connected to the switch"
     *
     */
    public static class isSwitchConnectedToSecondInputProperty extends PropertySupport.ReadWrite<Boolean> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isSwitchConnectedToSecondInputProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("Is Second Input Node Connected", Boolean.class, "Is Second Input Node Connected", "Edit the Second Input Node Connected In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return whether the first input is the one connected to the switch
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsSwitchConnectedToSecondInput();
        }

        /**
         *
         * @param t whether the first input is the one connected to the switch
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsSwitchConnectedToSecondInput(Boolean.parseBoolean(t.toString()));
            //switch between first and second inputs connected values
            MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
            MySwitchInputWidget currentFirstInputForSwitch = switchW.getFirstInputNode();
            MySwitchInputWidget currentSecondInputForSwitch = switchW.getSecondInputNode();

            //set first input connected in switch
            switchW.setSecondInputConnected(Boolean.parseBoolean(t.toString()));
            //if second input in scene, update it
            if (currentFirstInputForSwitch != null) {
                //update second switch input which is mutually exclusive (input1 connected => input2 not connected and vice verca)
                MySwitchInputWidget switchInput1InScene = (MySwitchInputWidget) Scene.globalScene.getMyWidgetsAdded().get(currentFirstInputForSwitch.getWidget());
                switchInput1InScene.getSwitchInputNodeProperties().setIsSwitchConnectedToFirstInput(!Boolean.parseBoolean(t.toString()));
                switchInput1InScene.getSwitchInputNodeProperties().setIsSwitchConnectedToSecondInput(Boolean.parseBoolean(t.toString()));
            }

            //reverse first node and second node connected values if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {
                if (currentFirstInputForSwitch != null && Objects.equals(currentFirstInputForSwitch.getSwitchInputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentSecondInputForSwitch != null) {

                    //deconnect first switch input 
                    currentFirstInputForSwitch.getSwitchInputNodeProperties().setIsSwitchConnectedToFirstInput(false);

                    //update repository
                    switchW.setFirstInputNode(currentFirstInputForSwitch);
                    switchW.setSecondInputNode(currentSecondInputForSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    //PropertiesOfNodeObject.setIsSecondInputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));

                }

            } //if false
            else {

                if (currentFirstInputForSwitch != null && Objects.equals(currentFirstInputForSwitch.getSwitchInputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentSecondInputForSwitch != null) {

                    //connect first switch input 
                    currentFirstInputForSwitch.getSwitchInputNodeProperties().setIsSwitchConnectedToFirstInput(true);

                    switchW.setFirstInputNode(currentFirstInputForSwitch);
                    switchW.setSecondInputNode(currentSecondInputForSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }
            }
        }

    }

    /**
     * isFirstOutputNodeInParentSwitchProperty is a property to know if the
     * output node of the switch is the first one of its kind in the switch
     *
     */
    public static class isFirstOutputNodeInParentSwitchProperty extends PropertySupport.ReadOnly<Boolean> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isFirstOutputNodeInParentSwitchProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("Is First Output Node", Boolean.class, "Is First Output Node", "Edit the First Output Node In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return a boolean containing whether the switch output is the first
         * one
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsFirstOutputNodeInParentSwitch();
        }

        /**
         *
         * @param t is a boolean containing whether the switch output is the
         * first one
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsFirstOutputNodeInParentSwitch(Boolean.parseBoolean(t.toString()));
            //reverse first node and second node if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                //switch between first and second inputs of this switch if they are already initialized
                //if the second becomes the first => the first becomes the second (permutation)
                //else initialize only the first
                MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
                MySwitchOutputWidget currentFirstOutputForSwitch = switchW.getFirstOutputNode();
                MySwitchOutputWidget currentSecondOutputForSwitch = switchW.getSecondOutputNode();
                MySwitchOutputWidget currentTempOutputForSwitch ; //temp for permutation
                if (currentSecondOutputForSwitch != null && Objects.equals(currentSecondOutputForSwitch.getSwitchOutputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentFirstOutputForSwitch != null) {
                    currentTempOutputForSwitch = currentFirstOutputForSwitch;
                    currentFirstOutputForSwitch = currentSecondOutputForSwitch;
                    currentSecondOutputForSwitch = currentTempOutputForSwitch;
                    PropertiesOfNodeObject.setIsSecondOutputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    currentFirstOutputForSwitch.getSwitchOutputNodeProperties().setIsFirstOutputNodeInParentSwitch(true);
                    currentFirstOutputForSwitch.getSwitchOutputNodeProperties().setIsSecondOutputNodeInParentSwitch(false);

                    PropertiesOfNodeObject.setIsFirstOutputNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsSecondOutputNodeInParentSwitch(false);

                    currentSecondOutputForSwitch.getSwitchOutputNodeProperties().setIsSecondOutputNodeInParentSwitch(true);
                    currentSecondOutputForSwitch.getSwitchOutputNodeProperties().setIsFirstOutputNodeInParentSwitch(false);

                    switchW.setFirstOutputNode(currentFirstOutputForSwitch);
                    switchW.setSecondOutputNode(currentSecondOutputForSwitch);
                    //update switch
                    currentFirstOutputForSwitch.getSwitchOutputNodeProperties().setParentSwitch(switchW.getNumber());
                    currentSecondOutputForSwitch.getSwitchOutputNodeProperties().setParentSwitch(switchW.getNumber());

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    //PropertiesOfNodeObject.setIsSecondOutputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));

                } else {
                    if (currentFirstOutputForSwitch != null) {
                        currentFirstOutputForSwitch.getSwitchOutputNodeProperties().setIsFirstOutputNodeInParentSwitch(false);
                    }

                    MySwitchOutputWidget currentinputSwitch = SwitchOutputRepository.getInstance().find(PropertiesOfNodeObject.getNumber());
                    currentinputSwitch.getSwitchOutputNodeProperties().setIsFirstOutputNodeInParentSwitch(true);

                    PropertiesOfNodeObject.setIsFirstOutputNodeInParentSwitch(false);
                    PropertiesOfNodeObject.setIsSecondOutputNodeInParentSwitch(true);
                    switchW.setFirstOutputNode(currentinputSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);

                    //PropertiesOfNodeObject.setIsSecondOutputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));
                }

            }
        }

    }

    /**
     * isSecondOutputNodeInParentSwitchProperty is a property to know if the
     * output node of the switch is the second one of its kind in the switch
     *
     */
    public static class isSecondOutputNodeInParentSwitchProperty extends PropertySupport.ReadOnly<Boolean> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isSecondOutputNodeInParentSwitchProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("Is Second Output Node", Boolean.class, "Is Second Output Node", "Edit the Second Output Node In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return a boolean that represents whether it is the second output
         * node of the switch
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsSecondOutputNodeInParentSwitch();
        }

        /**
         *
         * @param t is a boolean that represents whether it is the second output
         * node of the switch
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsSecondOutputNodeInParentSwitch(Boolean.parseBoolean(t.toString()));
            //reverse first node and second node if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                //switch between first and second stop sensors of this switch if they are already initialized
                //if the second becomes the first => the first becomes the second (permutation)
                //else initialize only the first
                MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
                MySwitchOutputWidget currentFirstOutputForSwitch = switchW.getFirstOutputNode();
                MySwitchOutputWidget currentSecondOutputForSwitch = switchW.getSecondOutputNode();
                MySwitchOutputWidget currentTempOutputForSwitch ; //temp for permutation
                if (currentFirstOutputForSwitch != null && Objects.equals(currentFirstOutputForSwitch.getSwitchOutputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentSecondOutputForSwitch != null) {
                    currentTempOutputForSwitch = currentFirstOutputForSwitch;
                    currentFirstOutputForSwitch = currentSecondOutputForSwitch;
                    currentSecondOutputForSwitch = currentTempOutputForSwitch;
                    PropertiesOfNodeObject.setIsSecondOutputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    currentFirstOutputForSwitch.getSwitchOutputNodeProperties().setIsFirstOutputNodeInParentSwitch(false);
                    currentFirstOutputForSwitch.getSwitchOutputNodeProperties().setIsSecondOutputNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsFirstOutputNodeInParentSwitch(false);
                    PropertiesOfNodeObject.setIsSecondOutputNodeInParentSwitch(true);

                    currentSecondOutputForSwitch.getSwitchOutputNodeProperties().setIsSecondOutputNodeInParentSwitch(false);
                    currentSecondOutputForSwitch.getSwitchOutputNodeProperties().setIsFirstOutputNodeInParentSwitch(true);

                    switchW.setFirstOutputNode(currentFirstOutputForSwitch);
                    switchW.setSecondOutputNode(currentSecondOutputForSwitch);
                    //update switch
                    currentFirstOutputForSwitch.getSwitchOutputNodeProperties().setParentSwitch(switchW.getNumber());
                    currentSecondOutputForSwitch.getSwitchOutputNodeProperties().setParentSwitch(switchW.getNumber());

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    //PropertiesOfNodeObject.setIsFirstOutputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));

                } else {
                    if (currentSecondOutputForSwitch != null) {
                        currentSecondOutputForSwitch.getSwitchOutputNodeProperties().setIsSecondOutputNodeInParentSwitch(false);
                    }

                    MySwitchOutputWidget currentinputSwitch = SwitchOutputRepository.getInstance().find(PropertiesOfNodeObject.getNumber());
                    currentinputSwitch.getSwitchOutputNodeProperties().setIsSecondOutputNodeInParentSwitch(true);

                    switchW.setSecondOutputNode(currentinputSwitch);

                    PropertiesOfNodeObject.setIsFirstOutputNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsSecondOutputNodeInParentSwitch(false);
                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }
            }
        }

    }

    /**
     * isSwitchConnectedToFirstOutputProperty is a property referencing whether
     * "the first output node of the switch is the one connected to the switch"
     *
     */
    public static class isSwitchConnectedToFirstOutputProperty extends PropertySupport.ReadWrite<Boolean> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject is the properties of the node
         */
        public isSwitchConnectedToFirstOutputProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("Is First Output Node Connected", Boolean.class, "Is First Output Node Connected", "Edit the First Output Node Connected In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return a boolean that represents whether the first output node of
         * the switch is the one connected to the switch
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsSwitchConnectedToFirstOutput();
        }

        /**
         *
         * @param t is a boolean that represents whether the first output node
         * of the switch is the one connected to the switch
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsSwitchConnectedToFirstOutput(Boolean.parseBoolean(t.toString()));
            //switch between first and second outputs connected values
            MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
            MySwitchOutputWidget currentFirstOutputForSwitch = switchW.getFirstOutputNode();
            MySwitchOutputWidget currentSecondOutputForSwitch = switchW.getSecondOutputNode();

            //set first output connected in switch
            switchW.setSecondOutputConnected(!Boolean.parseBoolean(t.toString()));
            //if second output in scene, update it
            if (currentSecondOutputForSwitch != null) {
                //update second switch output which is mutually exclusive (output1 connected => output2 not connected and vice verca)
                MySwitchOutputWidget switchOutputput2InScene = (MySwitchOutputWidget) Scene.globalScene.getMyWidgetsAdded().get(currentSecondOutputForSwitch.getWidget());
                switchOutputput2InScene.getSwitchOutputNodeProperties().setIsSwitchConnectedToFirstOutput(Boolean.parseBoolean(t.toString()));
                switchOutputput2InScene.getSwitchOutputNodeProperties().setIsSwitchConnectedToSecondOutput(!Boolean.parseBoolean(t.toString()));
            }

            //reverse first node and second node connected values if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                if (currentSecondOutputForSwitch != null && Objects.equals(currentSecondOutputForSwitch.getSwitchOutputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentFirstOutputForSwitch != null) {

                    //deconnect second switch output 
                    currentSecondOutputForSwitch.getSwitchOutputNodeProperties().setIsSwitchConnectedToSecondOutput(false);

                    //update repository
                    switchW.setFirstOutputNode(currentFirstOutputForSwitch);
                    switchW.setSecondOutputNode(currentSecondOutputForSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }

            } //if false
            else {

                if (currentSecondOutputForSwitch != null && Objects.equals(currentSecondOutputForSwitch.getSwitchOutputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentFirstOutputForSwitch != null) {

                    //connect second switch output 
                    currentSecondOutputForSwitch.getSwitchOutputNodeProperties().setIsSwitchConnectedToSecondOutput(true);

                    switchW.setFirstOutputNode(currentFirstOutputForSwitch);
                    switchW.setSecondOutputNode(currentSecondOutputForSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }
            }
        }

    }

    /**
     * isSwitchConnectedToSecondOutputProperty is a property referencing whether
     * "the second output node of the switch is the one connected to the switch"
     *
     *
     */
    public static class isSwitchConnectedToSecondOutputProperty extends PropertySupport.ReadWrite<Boolean> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isSwitchConnectedToSecondOutputProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("Is Second Output Node Connected", Boolean.class, "Is Second Output Node Connected", "Edit the Second Output Node Connected In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return a boolean that represents whether the second output node of
         * the switch is the one connected to the switch
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsSwitchConnectedToSecondOutput();
        }

        /**
         *
         * @param t is a boolean that represents whether the second output node
         * of the switch is the one connected to the switch
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsSwitchConnectedToSecondOutput(Boolean.parseBoolean(t.toString()));
            //switch between first and second outputs connected values
            MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
            MySwitchOutputWidget currentFirstOutputForSwitch = switchW.getFirstOutputNode();
            MySwitchOutputWidget currentSecondOutputForSwitch = switchW.getSecondOutputNode();

            //set second output connected in switch
            switchW.setSecondOutputConnected(!Boolean.parseBoolean(t.toString()));
            //if first output in scene, update it
            if (currentFirstOutputForSwitch != null) {
                //update second switch output which is mutually exclusive (output2 connected => output1 not connected and vice verca)
                MySwitchOutputWidget switchOutputput1InScene = (MySwitchOutputWidget) Scene.globalScene.getMyWidgetsAdded().get(currentFirstOutputForSwitch.getWidget());
                switchOutputput1InScene.getSwitchOutputNodeProperties().setIsSwitchConnectedToSecondOutput(Boolean.parseBoolean(t.toString()));
                switchOutputput1InScene.getSwitchOutputNodeProperties().setIsSwitchConnectedToFirstOutput(!Boolean.parseBoolean(t.toString()));
            }

            //reverse first node and second node connected values if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {
                if (currentFirstOutputForSwitch != null && Objects.equals(currentFirstOutputForSwitch.getSwitchOutputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentSecondOutputForSwitch != null) {

                    //deconnect first switch output 
                    currentFirstOutputForSwitch.getSwitchOutputNodeProperties().setIsSwitchConnectedToFirstOutput(false);

                    //update repository
                    switchW.setFirstOutputNode(currentFirstOutputForSwitch);
                    switchW.setSecondOutputNode(currentSecondOutputForSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    //PropertiesOfNodeObject.setIsSecondOutputNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));

                }

            } //if false
            else {

                if (currentFirstOutputForSwitch != null && Objects.equals(currentFirstOutputForSwitch.getSwitchOutputNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentSecondOutputForSwitch != null) {

                    //connect first switch output 
                    currentFirstOutputForSwitch.getSwitchOutputNodeProperties().setIsSwitchConnectedToFirstOutput(true);

                    switchW.setFirstOutputNode(currentFirstOutputForSwitch);
                    switchW.setSecondOutputNode(currentSecondOutputForSwitch);

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }
            }
        }

    }

    /**
     * isFirstIntermediateNodeInParentSwitchProperty is a property to know if
     * the intermediate node of the switch is the first one of its kind in the
     * switch
     *
     */
    public static class isFirstIntermediateNodeInParentSwitchProperty extends PropertySupport.ReadOnly<Boolean> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isFirstIntermediateNodeInParentSwitchProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("Is First Intermediate Node", Boolean.class, "Is First Intermediate Node", "Edit the First Intermediate Node In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return a boolean that represents whether the switch intermediate is
         * the first one in the switch
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsFirstIntermediateNodeInParentSwitch();
        }

        /**
         *
         * @param t is a boolean that represents whether the switch intermediate
         * is the first one in the switch
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(Boolean.parseBoolean(t.toString()));
            //reverse first node and second node if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                //switch between first and second intermediatess of this switch if they are already initialized
                //if the second becomes the first => the first becomes the second (permutation)
                //else initialize only the first
                MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
                MySwitchIntermediateWidget currentFirstIntermediateForSwitch = switchW.getFirstIntermediateNode();
                MySwitchIntermediateWidget currentSecondIntermediateForSwitch = switchW.getSecondIntermediateNode();
                MySwitchIntermediateWidget currentTempIntermediateForSwitch; //temp for permutation
                if (currentSecondIntermediateForSwitch != null && Objects.equals(currentSecondIntermediateForSwitch.getSwitchIntermediateNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentFirstIntermediateForSwitch != null) {
                    currentTempIntermediateForSwitch = currentFirstIntermediateForSwitch;
                    currentFirstIntermediateForSwitch = currentSecondIntermediateForSwitch;
                    currentSecondIntermediateForSwitch = currentTempIntermediateForSwitch;
                    PropertiesOfNodeObject.setIsSecondIntermediateNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    currentFirstIntermediateForSwitch.getSwitchIntermediateNodeProperties().setIsFirstIntermediateNodeInParentSwitch(true);
                    currentFirstIntermediateForSwitch.getSwitchIntermediateNodeProperties().setIsSecondIntermediateNodeInParentSwitch(false);

                    PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(false);

                    currentSecondIntermediateForSwitch.getSwitchIntermediateNodeProperties().setIsSecondIntermediateNodeInParentSwitch(true);
                    currentSecondIntermediateForSwitch.getSwitchIntermediateNodeProperties().setIsFirstIntermediateNodeInParentSwitch(false);

                    switchW.setFirstIntermediateNode(currentFirstIntermediateForSwitch);
                    switchW.setSecondIntermediateNode(currentSecondIntermediateForSwitch);
                    //update switch
                    currentFirstIntermediateForSwitch.getSwitchIntermediateNodeProperties().setParentSwitch(switchW.getNumber());
                    currentSecondIntermediateForSwitch.getSwitchIntermediateNodeProperties().setParentSwitch(switchW.getNumber());

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    //PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));

                } else {
                    if (currentFirstIntermediateForSwitch != null) {
                        currentFirstIntermediateForSwitch.getSwitchIntermediateNodeProperties().setIsFirstIntermediateNodeInParentSwitch(false);
                    }

                    MySwitchIntermediateWidget currentinputSwitch = SwitchIntermediateRepository.getInstance().find(PropertiesOfNodeObject.getNumber());
                    currentinputSwitch.getSwitchIntermediateNodeProperties().setIsFirstIntermediateNodeInParentSwitch(true);

                    switchW.setFirstIntermediateNode(currentinputSwitch);
                    PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(false);
                    PropertiesOfNodeObject.setIsSecondIntermediateNodeInParentSwitch(true);
                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }

            }
        }
    }

    /**
     * isSecondIntermediateNodeInParentSwitchProperty is a property to know if
     * the intermediate node of the switch is the second one of its kind in the
     * switch
     *
     */
    public static class isSecondIntermediateNodeInParentSwitchProperty extends PropertySupport.ReadOnly<Boolean> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public isSecondIntermediateNodeInParentSwitchProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("Is Second Intermediate Node", Boolean.class, "Is Second Intermediate Node", "Edit the Second Intermediate Node In The Switch");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return a boolean that represents whether the intermediate node is
         * the second one in the switch
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getIsSecondIntermediateNodeInParentSwitch();
        }

        /**
         *
         * @param t is a boolean that represents whether the intermediate node
         * is the second one in the switch
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(Boolean t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setIsSecondIntermediateNodeInParentSwitch(Boolean.parseBoolean(t.toString()));
            //reverse first node and second node if they are already initialized
            if (Boolean.parseBoolean(t.toString()) == true) {

                //switch between first and second stop sensors of this switch if they are already initialized
                //if the second becomes the first => the first becomes the second (permutation)
                //else initialize only the first
                MySwitchWidget switchW = SwitchRepository.getInstance().find(PropertiesOfNodeObject.getParentSwitch());
                MySwitchIntermediateWidget currentFirstIntermediateForSwitch = switchW.getFirstIntermediateNode();
                MySwitchIntermediateWidget currentSecondIntermediateForSwitch = switchW.getSecondIntermediateNode();
                MySwitchIntermediateWidget currentTempIntermediateForSwitch; //temp for permutation
                if (currentFirstIntermediateForSwitch != null && Objects.equals(currentFirstIntermediateForSwitch.getSwitchIntermediateNodeProperties().getNumber(), PropertiesOfNodeObject.getNumber())
                        && currentSecondIntermediateForSwitch != null) {
                    currentTempIntermediateForSwitch = currentFirstIntermediateForSwitch;
                    currentFirstIntermediateForSwitch = currentSecondIntermediateForSwitch;
                    currentSecondIntermediateForSwitch = currentTempIntermediateForSwitch;
                    PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));
                    currentFirstIntermediateForSwitch.getSwitchIntermediateNodeProperties().setIsFirstIntermediateNodeInParentSwitch(false);
                    currentFirstIntermediateForSwitch.getSwitchIntermediateNodeProperties().setIsSecondIntermediateNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(false);
                    PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(true);

                    currentSecondIntermediateForSwitch.getSwitchIntermediateNodeProperties().setIsSecondIntermediateNodeInParentSwitch(false);
                    currentSecondIntermediateForSwitch.getSwitchIntermediateNodeProperties().setIsFirstIntermediateNodeInParentSwitch(true);

                    switchW.setFirstIntermediateNode(currentFirstIntermediateForSwitch);
                    switchW.setSecondIntermediateNode(currentSecondIntermediateForSwitch);
                    //update switch
                    currentFirstIntermediateForSwitch.getSwitchIntermediateNodeProperties().setParentSwitch(switchW.getNumber());
                    currentSecondIntermediateForSwitch.getSwitchIntermediateNodeProperties().setParentSwitch(switchW.getNumber());

                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                    //PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(!Boolean.parseBoolean(t.toString()));

                } else {
                    if (currentSecondIntermediateForSwitch != null) {
                        currentSecondIntermediateForSwitch.getSwitchIntermediateNodeProperties().setIsFirstIntermediateNodeInParentSwitch(false);
                    }

                    MySwitchIntermediateWidget currentinputSwitch = SwitchIntermediateRepository.getInstance().find(PropertiesOfNodeObject.getNumber());
                    currentinputSwitch.getSwitchIntermediateNodeProperties().setIsFirstIntermediateNodeInParentSwitch(true);

                    switchW.setSecondIntermediateNode(currentinputSwitch);
                    PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(true);
                    PropertiesOfNodeObject.setIsFirstIntermediateNodeInParentSwitch(false);
                    SwitchRepository.getInstance().update(switchW.getNumber(), switchW);
                }
            }
        }

    }

    /**
     * XCoordinateArezzoProperty represents the "x coordinate in arezzo
     * coordinates system" property
     *
     */
    public static class XCoordinateArezzoProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public XCoordinateArezzoProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
            super("XCoordinate In Arezzo", String.class, "XCoordinate In Arezzo", "Edit the X Coordinate INn Arezzo");
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

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public YCoordinateArezzoProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
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

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the node
         */
        public XCoordinateProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
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
         * @param t is the value of the x coordinate of the node in the scene
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

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject represents the properties of the node
         */
        public YCoordinateProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
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

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject is the properties of the node
         */
        public TypeProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
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
     * SpeedsToNextNodesProperty represents the "list of the speeds to the next
     * nodes" property
     *
     */
    public static class SpeedsToNextNodesProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject is the properties of the node
         */
        public SpeedsToNextNodesProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
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
            List<Float> result = new ArrayList<>();
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

        private final PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject is the properties of the node
         */
        public NumbersOfNextNodesProperty(PropertiesOfNodesOfType6_10_11_12 PropertiesOfNodeObject) {
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
            List<Integer> result = new ArrayList<>();
            String[] values = t.toString().split(" ");
            for (String value : values) {
                Integer intValue = Integer.parseInt(value);
                result.add(intValue);
            }
            PropertiesOfNodeObject.setNumbersOfNextNodes(result);
        }
    }

}
