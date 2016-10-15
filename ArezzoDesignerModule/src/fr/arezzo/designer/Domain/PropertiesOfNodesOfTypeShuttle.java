package fr.arezzo.designer.Domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.nodes.PropertySupport;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;
import fr.arezzo.designer.Scene.Scene;

/**
 * PropertiesOfNodesOfTypeShuttle represents the properties informations shuttle
 * nodes
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class PropertiesOfNodesOfTypeShuttle extends IconNodeWidget {

    protected Integer number;
    protected Float xCoordinate;
    protected Float yCoordinate;
    protected Integer Type;

    protected List<Float> speedsToNextNodes;
    protected List<Integer> numbersOfNextNodes;
    //the shuttle size in decimeter
    protected Integer sizeInDM = 3;
    //the current link number where the shuttle is
    protected Integer currentPathLinkNumber;
    //the current link  where the shuttle is
    protected Object currentPathLink;
    //this value represents the Node Number of the starting side of the link where the shuttle is  
    protected Integer startNodeNumber;
    //this value represents the Node Number of the ending side of the link where the shuttle is  
    protected Integer endNodeNumber;
    //the distance from  starting node starting to its current position
    protected Double distanceFromStartingNode;
    //this value represents the Node of the starting side of the link where the shuttle is  
    protected Object startNode;
    //this value represents the Node of the ending side of the link where the shuttle is  
    protected Object endNode;
    //list of next nodes in the path of the shuttle
    protected List<Object> nextNodes = new ArrayList<>();
    protected List<WidgetCommonInfo.WidgetType> nextNodestypes = new ArrayList<>();

    //new coordinates sytem for the simulator where point (0,0) represents the center of the network
    protected Float xCoordinateArezzo;
    protected Float yCoordinateArezzo;

    /**
     * PropertiesOfNodesOfTypeShuttle constructor
     *
     * @param scene is the component that contains all of the widgets
     */
    public PropertiesOfNodesOfTypeShuttle(org.netbeans.api.visual.widget.Scene scene) {
        super(scene);
        this.number = 0;
        speedsToNextNodes = new ArrayList<>();
        numbersOfNextNodes = new ArrayList<>();
        Type = 99;
        xCoordinate = yCoordinate = 0.0f;
        this.sizeInDM = 3;
        this.currentPathLinkNumber = 0;
        this.startNodeNumber = 0;
        this.endNodeNumber = 0;
        this.distanceFromStartingNode = 0.0;
        this.startNode = null;
        this.endNode = null;
        this.currentPathLink = null;
        this.nextNodes = new ArrayList<>();
        nextNodestypes = new ArrayList<>();
    }

    /**
     * PropertiesOfNodesOfTypeShuttle constructor
     *
     * @param scene is the component that contains all of the widgets
     * @param number is the number of the node(or id)
     * @param xCoordinate is the x coordinate inside the scene
     * @param yCoordinate is the y coordinate inside the scene
     * @param Type is the type of the node
     * @param speedsToNextNodes is the list of speeds to the next nodes
     * @param numbersOfNextNodes is a list of the numbers (or id) of the nodes
     * linked to this node
     * @param sizeInDM is the size of the shuttle in DM
     * @param currentPathLinkNumber is the current link number where the shuttle
     * is
     * @param startNodeNumber this value represents the Node Number of the
     * starting side of the link where the shuttle is
     * @param endNodeNumber this value represents the Node Number of the ending
     * side of the link where the shuttle is
     * @param distanceFromStartingNode is the distance from starting node
     * starting to its current position
     * @param startNode this value represents the Node of the starting side of
     * the link where the shuttle is
     * @param endNode this value represents the Node of the ending side of the
     * link where the shuttle is
     * @param currentPathLink is the current link where the shuttle is
     * @param nextNodes is a list of the nodes which are linked to this node
     * @param nextNodestypes is a list of the types of the nodes which are
     * linked to this node
     */
    public PropertiesOfNodesOfTypeShuttle(Scene scene, Integer number, Float xCoordinate, Float yCoordinate, Integer Type, List<Float> speedsToNextNodes, List<Integer> numbersOfNextNodes,
            Integer sizeInDM, Integer currentPathLinkNumber, Integer startNodeNumber, Integer endNodeNumber, Double distanceFromStartingNode, Object startNode, Object endNode,
            Object currentPathLink, List<Object> nextNodes, List<WidgetCommonInfo.WidgetType> nextNodestypes) {
        super(scene);
        this.number = number;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.Type = Type;
        this.numbersOfNextNodes = numbersOfNextNodes;
        this.speedsToNextNodes = speedsToNextNodes;
        this.sizeInDM = sizeInDM;
        this.currentPathLinkNumber = currentPathLinkNumber;
        this.startNodeNumber = startNodeNumber;
        this.endNodeNumber = endNodeNumber;
        this.distanceFromStartingNode = distanceFromStartingNode;
        this.startNode = startNode;
        this.endNode = endNode;
        this.currentPathLink = currentPathLink;
        this.nextNodes = nextNodes;
        this.nextNodestypes = nextNodestypes;
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
     * @return the size of the shuttle in DM
     */
    public Integer getSizeInDM() {
        return sizeInDM;
    }

    /**
     *
     * @param sizeInDM is the size of the shuttle in DM
     */
    public void setSizeInDM(Integer sizeInDM) {
        this.sizeInDM = sizeInDM;
    }

    /**
     *
     * @return the current link number where the shuttle is
     */
    public Integer getCurrentPathLinkNumber() {
        return currentPathLinkNumber;
    }

    /**
     *
     * @param currentPathLinkNumber is the current link number where the shuttle
     * is
     */
    public void setCurrentPathLinkNumber(Integer currentPathLinkNumber) {
        this.currentPathLinkNumber = currentPathLinkNumber;
    }

    /**
     *
     * @return the current link where the shuttle is
     */
    public Object getCurrentPathLink() {
        return currentPathLink;
    }

    /**
     *
     * @param currentPathLink is the current link where the shuttle is
     */
    public void setCurrentPathLink(Object currentPathLink) {
        this.currentPathLink = currentPathLink;
    }

    /**
     *
     * @return the Node Number of the starting side of the link where the
     * shuttle is
     */
    public Integer getStartNodeNumber() {
        return startNodeNumber;
    }

    /**
     *
     * @param startNodeNumber is the Node Number of the starting side of the
     * link where the shuttle is
     */
    public void setStartNodeNumber(Integer startNodeNumber) {
        this.startNodeNumber = startNodeNumber;
    }

    /**
     *
     * @return the Node Number of the ending side of the link where the shuttle
     * is
     */
    public Integer getEndNodeNumber() {
        return endNodeNumber;
    }

    /**
     *
     * @param endNodeNumber is the Node Number of the ending side of the link
     * where the shuttle is
     */
    public void setEndNodeNumber(Integer endNodeNumber) {
        this.endNodeNumber = endNodeNumber;
    }

    /**
     *
     * @return the distance from starting node starting to its current position
     */
    public Double getDistanceFromStartingNode() {
        return distanceFromStartingNode;
    }

    /**
     *
     * @param distanceFromStartingNode is the distance from starting node
     * starting to its current position
     */
    public void setDistanceFromStartingNode(Double distanceFromStartingNode) {
        this.distanceFromStartingNode = distanceFromStartingNode;
    }

    /**
     *
     * @return a value that represents the Node of the starting side of the link
     * where the shuttle is
     */
    public Object getStartNode() {
        return startNode;
    }

    /**
     *
     * @param startNode this value represents the Node of the starting side of
     * the link where the shuttle is
     */
    public void setStartNode(Object startNode) {
        this.startNode = startNode;
    }

    /**
     *
     * @return a value that represents the Node of the ending side of the link
     * where the shuttle is
     */
    public Object getEndNode() {
        return endNode;
    }

    /**
     *
     * @param endNode this value represents the Node of the ending side of the
     * link where the shuttle is
     */
    public void setEndNode(Object endNode) {
        this.endNode = endNode;
    }

    /**
     *
     * @return a list of the nodes which are linked to this node
     */
    public List<Object> getNextNodes() {
        return nextNodes;
    }

    /**
     *
     * @param nextNodes is a list of the nodes which are linked to this node
     */
    public void setNextNodes(List<Object> nextNodes) {
        this.nextNodes = nextNodes;
    }

    /**
     *
     * @return a list of the types of nodes which are linked to this node
     */
    public List<WidgetCommonInfo.WidgetType> getNextNodestypes() {
        return nextNodestypes;
    }

    /**
     *
     * @param nextNodestypes is a list of the types of nodes which are linked to
     * this node
     */
    public void setNextNodestypes(List<WidgetCommonInfo.WidgetType> nextNodestypes) {
        this.nextNodestypes = nextNodestypes;
    }

    /**
     * NumberProperty represents the "number" property for this node
     *
     */
    public static class NumberProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public NumberProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
            super("Number", String.class, "Number", "Edit the Number");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return the value of the number field for the shuttle
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getNumber() + "";
        }

        /**
         *
         * @param t is the value of the number field for the shuttle
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
     * StartNodeNumberProperty represents the property of the number of the link
     * where the shuttle is attached
     *
     */
    public static class StartNodeNumberProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public StartNodeNumberProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
            super("Start Node Number", String.class, "Start Node Number", "Starting Node Number of the link that contains the shuttle");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return the value of the number of the link where the shuttle is
         * attached
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getStartNodeNumber() + "";
        }

        /**
         *
         * @param t the number of the link where the shuttle is attached
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setStartNodeNumber(Integer.parseInt(t.toString()));
        }

    }

    /**
     * EndNodeNumberProperty is the property that represents the value of the
     * number of the node at the end of the link where the shuttle is attached
     */
    public static class EndNodeNumberProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public EndNodeNumberProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
            super("End Node Number", String.class, "End Node Number", "Ending Node Number of the node at the end of the link where the shuttle is attached");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return the value of the number of the link where the shuttle is
         * attached
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getEndNodeNumber() + "";
        }

        /**
         *
         * @param t the value of the number of the node at the end of the link
         * where the shuttle is attached
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setEndNodeNumber(Integer.parseInt(t.toString()));
        }

    }

    /**
     * SizeInDMProperty represents the sizeInDM property in the properties panel
     *
     */
    public static class SizeInDMProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public SizeInDMProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
            super("SizeInDM", String.class, "Size In DM", "Edit the Size of the Shuttle in DM");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return the value of the SizeInDM field for the shuttle
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getSizeInDM() + "";
        }

        /**
         *
         * @param t the value of the SizeInDM field for the shuttle
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setSizeInDM(Integer.parseInt(t.toString()));
        }

    }

    /**
     * NextNodeSpeedProperty represents a property for the speed of a next node
     */
    public static class NextNodeSpeedProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;
        private Integer nextNodeNumber;
        private Integer nextNodeIndex;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         * @param nextNodeNumber
         * @param nextNodeIndex
         */
        public NextNodeSpeedProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject, Integer nextNodeNumber, Integer nextNodeIndex) {
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
     * XCoordinateProperty represents the "x coordinate" property of the node
     *
     */
    public static class XCoordinateProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public XCoordinateProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
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

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public YCoordinateProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
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
     * DistanceFromStartingNodeProperty is a property that represents the
     * distance from starting node starting to its current position
     *
     */
    public static class DistanceFromStartingNodeProperty extends PropertySupport.ReadWrite<String> {

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public DistanceFromStartingNodeProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
            super("DistanceFromStartingNode", String.class, "Distance From Starting Node", "Edit the Distance From Starting Node");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         *
         * @return the distance from starting node starting to its current
         * position
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return PropertiesOfNodeObject.getDistanceFromStartingNode() + "";
        }

        /**
         *
         * @param t the distance from starting node starting to its current
         * position
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PropertiesOfNodeObject.setDistanceFromStartingNode(Double.parseDouble(t));
        }

    }

    /**
     * XCoordinateArezzoProperty represents the "x coordinate in arezzo
     * coordinates system" property
     *
     */
    public static class XCoordinateArezzoProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public XCoordinateArezzoProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
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
         * @param t is the value of the "x coordinate in arezzo coordinates
         * system"
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

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public YCoordinateArezzoProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
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
         * @param t is the value of the "y coordinate in arezzo coordinates
         * system"
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
     * TypeProperty represents the "type of node" property
     *
     */
    public static class TypeProperty extends PropertySupport.ReadOnly<String> {

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public TypeProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
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

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public SpeedsToNextNodesProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
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
         * @param t is the value of "list of the speeds to the next nodes"
         * property
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

        private final PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject;

        /**
         *
         * @param PropertiesOfNodeObject the properties of the shuttle
         */
        public NumbersOfNextNodesProperty(PropertiesOfNodesOfTypeShuttle PropertiesOfNodeObject) {
            super("NumbersOfNextNodes", String.class, "NumbersOfNextNodes", "Edit the numbers of the next nodes in the network, using numbers seperated by spaces like: 10 22 when the next nodes are nodes number 10 and 22 "
                    + "for the operations  taking respectively 11 seconds and 22 seconds and 33 seconds and 44 seconds");
            this.PropertiesOfNodeObject = PropertiesOfNodeObject;
        }

        /**
         * 
         * @return the value of the "list of the numbers to the next nodes"
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
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException 
         */
        @Override
        public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            List<Integer> result = new ArrayList<>();
            String[] values = t.toString().split(" ");
            for (String value : values) 
            {
                Integer intValue = Integer.parseInt(value);
                result.add(intValue);
            }
            PropertiesOfNodeObject.setNumbersOfNextNodes(result);
        }

    }
}
