package fr.arezzo.designer.DomainWidgets;

import fr.arezzo.designer.DomainWidgets.types4_8_9.MySensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyStopSensorWidget;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.ImageUtilities;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchInputWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchIntermediateWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchOutputWidget;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchRepository;
import fr.arezzo.designer.Scene.Scene;

/**
 * MySwitchWidget represents the parent switch of the switch components
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class MySwitchWidget {

    //switch number in the list of switchs
    public static Integer switchNumberInSwitchList;
    //number of last swicth and the number of switchs
    public static Integer lastSwitchNumber = 0;
    //number of the switch
    private Integer number;
    //first input node of the switch
    private MySwitchInputWidget firstInputNode;
    //secoond input node of the switch
    private MySwitchInputWidget secondInputNode;
    //what is the connected switch input (first = 0 (false) or second = 1 (true)
    private boolean secondInputConnected;
    //first ouput node of the switch 
    private MySwitchOutputWidget firstOutputNode;
    //second output node of the switch
    private MySwitchOutputWidget secondOutputNode;
    //what is the connected switch output (first = 0 (false) or second = 1 (true)
    private boolean secondOutputConnected;
    //first intermediate switch
    private MySwitchIntermediateWidget firstIntermediateNode;
    //second intermediate switch
    private MySwitchIntermediateWidget secondIntermediateNode;
    //first end_stop/sensor node of switch
    private MyStopSensorWidget firstEndStopSensorNode;
    //second end_stop/sensor node of switch
    private MyStopSensorWidget secondEndStopSensorNode;
    //first sensor output node
    private MySensorWidget firstSensorOutputNode;
    //second sensor output node
    private MySensorWidget secondSensorOutputNode;
    //widget of switch
    private IconNodeWidget widget = null;
    //is the children of the swich showed?
    private static Boolean isSwitchChildrenShowed = false;

    /**
     * MySwitchWidget Constructor
     */
    public MySwitchWidget() {
        number = (WidgetCommonInfo.getNumberOfNextWidget());
        switchNumberInSwitchList = ++lastSwitchNumber;
        //initialize the widget which is a genereic IconWidget that will be added to the scene
        widget = new IconNodeWidget(Scene.globalScene);
        //initialize the widget image using the shape image (from the palette)
        widget.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/switch_thumbnail.png"));
        //initialize the label of the widget
        widget.setLabel(number + "");

        widget.getActions().addAction(ActionFactory.createEditAction(new EditProvider() {
            @Override
            public void edit(Widget widget) {
                //if the user want to hide the switch label, he can double-click it
                widget.setVisible(!widget.isVisible());
            }
        }));
    }

    /**
     * MySwitchWidget constructor using the number of the node
     *
     * @param number the number assigned to the switch node
     */
    public MySwitchWidget(Integer number) {
        this.number = number;
        switchNumberInSwitchList = ++lastSwitchNumber;
        //initialize the widget which is a genereic IconWidget that will be added to the scene
        widget = new IconNodeWidget(Scene.globalScene);
        //initialize the widget image using the shape image (from the palette)
        widget.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/switch_thumbnail.png"));
        //initialize the label of the widget
        widget.setLabel(this.number + "");
        widget.getActions().addAction(ActionFactory.createEditAction(new EditProvider() {
            @Override
            public void edit(Widget widget) {
                //if the user want to hide the switch label, he can double-click it
                widget.setVisible(!widget.isVisible());
            }
        }));

    }

    /**
     * this function is called to help the user know all of the children of the
     * same switch by showing an icon of the switch(that also contain the number
     * of the switch) in the bottom of each child component of the switch
     *
     * @param switchNumber is the node number assigned to the switch
     */
    public static void hideShowAllChildrensOfSwitch(Integer switchNumber) {
        //Alert.alert("hide/show children of switch ok1", "hide/show children of switch ok1", Alert.AlertType.INFORMATION_MESSAGE);
        MySwitchWidget currentSwitch = SwitchRepository.getInstance().find(switchNumber);
        if (currentSwitch != null) {
            //show/hide 1st stop sensor switch icon
            if (currentSwitch.getFirstEndStopSensorNode() != null) {
                //Alert.alert("hide/show children of switch ok2", "hide/show children of switch ok2", Alert.AlertType.INFORMATION_MESSAGE);
                if (currentSwitch.getFirstEndStopSensorNode().getWidget().getChildren().size() > 0) {
                    //Alert.alert("hide/show children of switch ok3", "hide/show children of switch ok3", Alert.AlertType.INFORMATION_MESSAGE);
                    IconNodeWidget myParentW = currentSwitch.getFirstEndStopSensorNode().getWidget();
                    int switchLabelIndexToShowOrHide = 2; //myParentW.getChildren().indexOf(currentSwitch.getWidget());
                    //Alert.alert(""+switchLabelIndexToShowOrHide, myParentW.getChildren().size()+" = size", Alert.AlertType.INFORMATION_MESSAGE);
                    if (switchLabelIndexToShowOrHide != -1) {
                        //Alert.alert("hide/show children of switch ok4", "hide/show children of switch ok4", Alert.AlertType.INFORMATION_MESSAGE);
                        currentSwitch.getFirstEndStopSensorNode().getWidget().getChildren().get(switchLabelIndexToShowOrHide).setVisible(
                                !isSwitchChildrenShowed);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();

                    }

                }
            }
            //show/hide 2nd stop sensor switch icon
            if (currentSwitch.getSecondEndStopSensorNode() != null) {
                //Alert.alert("hide/show children of switch ok2", "hide/show children of switch ok2", Alert.AlertType.INFORMATION_MESSAGE);
                if (currentSwitch.getSecondEndStopSensorNode().getWidget().getChildren().size() > 0) {
                    //Alert.alert("hide/show children of switch ok3", "hide/show children of switch ok3", Alert.AlertType.INFORMATION_MESSAGE);
                    IconNodeWidget myParentW = currentSwitch.getSecondEndStopSensorNode().getWidget();
                    int switchLabelIndexToShowOrHide = 2; //myParentW.getChildren().indexOf(currentSwitch.getWidget());
                    //Alert.alert(""+switchLabelIndexToShowOrHide, myParentW.getChildren().size()+" = size", Alert.AlertType.INFORMATION_MESSAGE);
                    if (switchLabelIndexToShowOrHide != -1) {
                        //Alert.alert("hide/show children of switch ok4", "hide/show children of switch ok4", Alert.AlertType.INFORMATION_MESSAGE);
                        currentSwitch.getSecondEndStopSensorNode().getWidget().getChildren().get(switchLabelIndexToShowOrHide).setVisible(
                                !isSwitchChildrenShowed);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();

                    }

                }
            }
            //show/hide 1st input switch icon
            if (currentSwitch.getFirstInputNode() != null) {
                //Alert.alert("hide/show children of switch ok2", "hide/show children of switch ok2", Alert.AlertType.INFORMATION_MESSAGE);
                if (currentSwitch.getFirstInputNode().getWidget().getChildren().size() > 0) {
                    //Alert.alert("hide/show children of switch ok3", "hide/show children of switch ok3", Alert.AlertType.INFORMATION_MESSAGE);
                    IconNodeWidget myParentW = currentSwitch.getFirstInputNode().getWidget();
                    int switchLabelIndexToShowOrHide = 2; //myParentW.getChildren().indexOf(currentSwitch.getWidget());
                    //Alert.alert(""+switchLabelIndexToShowOrHide, myParentW.getChildren().size()+" = size", Alert.AlertType.INFORMATION_MESSAGE);
                    if (switchLabelIndexToShowOrHide != -1) {
                        //Alert.alert("hide/show children of switch ok4", "hide/show children of switch ok4", Alert.AlertType.INFORMATION_MESSAGE);
                        currentSwitch.getFirstInputNode().getWidget().getChildren().get(switchLabelIndexToShowOrHide).setVisible(
                                !isSwitchChildrenShowed);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();

                    }
                }
            }
            //show/hide 2nd input switch icon
            if (currentSwitch.getSecondInputNode() != null) {
                //Alert.alert("hide/show children of switch ok2", "hide/show children of switch ok2", Alert.AlertType.INFORMATION_MESSAGE);
                if (currentSwitch.getSecondInputNode().getWidget().getChildren().size() > 0) {
                    //Alert.alert("hide/show children of switch ok3", "hide/show children of switch ok3", Alert.AlertType.INFORMATION_MESSAGE);
                    IconNodeWidget myParentW = currentSwitch.getSecondInputNode().getWidget();
                    int switchLabelIndexToShowOrHide = 2; //myParentW.getChildren().indexOf(currentSwitch.getWidget());
                    //Alert.alert(""+switchLabelIndexToShowOrHide, myParentW.getChildren().size()+" = size", Alert.AlertType.INFORMATION_MESSAGE);
                    if (switchLabelIndexToShowOrHide != -1) {
                        //Alert.alert("hide/show children of switch ok4", "hide/show children of switch ok4", Alert.AlertType.INFORMATION_MESSAGE);
                        currentSwitch.getSecondInputNode().getWidget().getChildren().get(switchLabelIndexToShowOrHide).setVisible(
                                !isSwitchChildrenShowed);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();
                    }
                }
            }

            //show/hide 1st intermediate switch icon
            if (currentSwitch.getFirstIntermediateNode() != null) {
                //Alert.alert("hide/show children of switch ok2", "hide/show children of switch ok2", Alert.AlertType.INFORMATION_MESSAGE);
                if (currentSwitch.getFirstIntermediateNode().getWidget().getChildren().size() > 0) {
                    //Alert.alert("hide/show children of switch ok3", "hide/show children of switch ok3", Alert.AlertType.INFORMATION_MESSAGE);
                    IconNodeWidget myParentW = currentSwitch.getFirstIntermediateNode().getWidget();
                    int switchLabelIndexToShowOrHide = 2; //myParentW.getChildren().indexOf(currentSwitch.getWidget());
                    //Alert.alert(""+switchLabelIndexToShowOrHide, myParentW.getChildren().size()+" = size", Alert.AlertType.INFORMATION_MESSAGE);
                    if (switchLabelIndexToShowOrHide != -1) {
                        //Alert.alert("hide/show children of switch ok4", "hide/show children of switch ok4", Alert.AlertType.INFORMATION_MESSAGE);
                        currentSwitch.getFirstIntermediateNode().getWidget().getChildren().get(switchLabelIndexToShowOrHide).setVisible(
                                !isSwitchChildrenShowed);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();
                    }
                }
            }

            //show/hide 2nd intermediate switch icon
            if (currentSwitch.getSecondIntermediateNode() != null) {
                //Alert.alert("hide/show children of switch ok2", "hide/show children of switch ok2", Alert.AlertType.INFORMATION_MESSAGE);
                if (currentSwitch.getSecondIntermediateNode().getWidget().getChildren().size() > 0) {
                    //Alert.alert("hide/show children of switch ok3", "hide/show children of switch ok3", Alert.AlertType.INFORMATION_MESSAGE);
                    IconNodeWidget myParentW = currentSwitch.getSecondIntermediateNode().getWidget();
                    int switchLabelIndexToShowOrHide = 2; //myParentW.getChildren().indexOf(currentSwitch.getWidget());
                    //Alert.alert(""+switchLabelIndexToShowOrHide, myParentW.getChildren().size()+" = size", Alert.AlertType.INFORMATION_MESSAGE);
                    if (switchLabelIndexToShowOrHide != -1) {
                        //Alert.alert("hide/show children of switch ok4", "hide/show children of switch ok4", Alert.AlertType.INFORMATION_MESSAGE);
                        currentSwitch.getSecondIntermediateNode().getWidget().getChildren().get(switchLabelIndexToShowOrHide).setVisible(
                                !isSwitchChildrenShowed);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();

                    }

                }
            }

            //show/hide 1st output switch icon
            if (currentSwitch.getFirstOutputNode() != null) {
                //Alert.alert("hide/show children of switch ok2", "hide/show children of switch ok2", Alert.AlertType.INFORMATION_MESSAGE);
                if (currentSwitch.getFirstOutputNode().getWidget().getChildren().size() > 0) {
                    //Alert.alert("hide/show children of switch ok3", "hide/show children of switch ok3", Alert.AlertType.INFORMATION_MESSAGE);
                    IconNodeWidget myParentW = currentSwitch.getFirstOutputNode().getWidget();
                    int switchLabelIndexToShowOrHide = 2; //myParentW.getChildren().indexOf(currentSwitch.getWidget());
                    //Alert.alert(""+switchLabelIndexToShowOrHide, myParentW.getChildren().size()+" = size", Alert.AlertType.INFORMATION_MESSAGE);
                    if (switchLabelIndexToShowOrHide != -1) {
                        //Alert.alert("hide/show children of switch ok4", "hide/show children of switch ok4", Alert.AlertType.INFORMATION_MESSAGE);
                        currentSwitch.getFirstOutputNode().getWidget().getChildren().get(switchLabelIndexToShowOrHide).setVisible(
                                !isSwitchChildrenShowed);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();

                    }
                }
            }
            //show/hide 2nd output switch icon
            if (currentSwitch.getSecondOutputNode() != null) {
                //Alert.alert("hide/show children of switch ok2", "hide/show children of switch ok2", Alert.AlertType.INFORMATION_MESSAGE);
                if (currentSwitch.getSecondOutputNode().getWidget().getChildren().size() > 0) {
                    //Alert.alert("hide/show children of switch ok3", "hide/show children of switch ok3", Alert.AlertType.INFORMATION_MESSAGE);
                    IconNodeWidget myParentW = currentSwitch.getSecondOutputNode().getWidget();
                    int switchLabelIndexToShowOrHide = 2; //myParentW.getChildren().indexOf(currentSwitch.getWidget());
                    //Alert.alert(""+switchLabelIndexToShowOrHide, myParentW.getChildren().size()+" = size", Alert.AlertType.INFORMATION_MESSAGE);
                    if (switchLabelIndexToShowOrHide != -1) {
                        //Alert.alert("hide/show children of switch ok4", "hide/show children of switch ok4", Alert.AlertType.INFORMATION_MESSAGE);
                        currentSwitch.getSecondOutputNode().getWidget().getChildren().get(switchLabelIndexToShowOrHide).setVisible(
                                !isSwitchChildrenShowed);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();
                    }
                }
            }

            //show/hide 1st  sensor switch icon
            if (currentSwitch.getFirstSensorOutputNode() != null) {
                //Alert.alert("hide/show children of switch ok2", "hide/show children of switch ok2", Alert.AlertType.INFORMATION_MESSAGE);
                if (currentSwitch.getFirstSensorOutputNode().getWidget().getChildren().size() > 0) {
                    //Alert.alert("hide/show children of switch ok3", "hide/show children of switch ok3", Alert.AlertType.INFORMATION_MESSAGE);
                    IconNodeWidget myParentW = currentSwitch.getFirstSensorOutputNode().getWidget();
                    int switchLabelIndexToShowOrHide = 2; //myParentW.getChildren().indexOf(currentSwitch.getWidget());
                    //Alert.alert(""+switchLabelIndexToShowOrHide, myParentW.getChildren().size()+" = size", Alert.AlertType.INFORMATION_MESSAGE);
                    if (switchLabelIndexToShowOrHide != -1) {
                        //Alert.alert("hide/show children of switch ok4", "hide/show children of switch ok4", Alert.AlertType.INFORMATION_MESSAGE);
                        currentSwitch.getFirstSensorOutputNode().getWidget().getChildren().get(switchLabelIndexToShowOrHide).setVisible(
                                !isSwitchChildrenShowed);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();

                    }

                }
            }
            //show/hide 2nd  sensor switch icon
            if (currentSwitch.getSecondSensorOutputNode() != null) {
                //Alert.alert("hide/show children of switch ok2", "hide/show children of switch ok2", Alert.AlertType.INFORMATION_MESSAGE);
                if (currentSwitch.getSecondSensorOutputNode().getWidget().getChildren().size() > 0) {
                    //Alert.alert("hide/show children of switch ok3", "hide/show children of switch ok3", Alert.AlertType.INFORMATION_MESSAGE);
                    IconNodeWidget myParentW = currentSwitch.getSecondSensorOutputNode().getWidget();
                    int switchLabelIndexToShowOrHide = 2; //myParentW.getChildren().indexOf(currentSwitch.getWidget());
                    //Alert.alert(""+switchLabelIndexToShowOrHide, myParentW.getChildren().size()+" = size", Alert.AlertType.INFORMATION_MESSAGE);
                    if (switchLabelIndexToShowOrHide != -1) {
                        //Alert.alert("hide/show children of switch ok4", "hide/show children of switch ok4", Alert.AlertType.INFORMATION_MESSAGE);
                        currentSwitch.getSecondSensorOutputNode().getWidget().getChildren().get(switchLabelIndexToShowOrHide).setVisible(
                                !isSwitchChildrenShowed);
                        Scene.globalScene.validate();
                        Scene.globalScene.repaint();

                    }

                }
            }
            isSwitchChildrenShowed = !isSwitchChildrenShowed;

        }

    }

    /**
     *
     * @return the number of the last switch being added
     */
    public static Integer getLastSwitchNumber() {
        return lastSwitchNumber;
    }

    /**
     *
     * @param lastSwitchNumber the number of the last switch being added
     */
    public static void setLastSwitchNumber(Integer lastSwitchNumber) {
        MySwitchWidget.lastSwitchNumber = lastSwitchNumber;
    }

    /**
     *
     * @return the node number of the switch
     */
    public Integer getNumber() {
        return number;
    }

    /**
     *
     * @param number the node number of the switch
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     *
     * @return the first switch input component in the switch
     */
    public MySwitchInputWidget getFirstInputNode() {
        return firstInputNode;
    }

    /**
     *
     * @param firstInputNode the first switch input component in the switch
     */
    public void setFirstInputNode(MySwitchInputWidget firstInputNode) {

        this.firstInputNode = firstInputNode;
    }

    /**
     *
     * @return the second switch input component in the switch
     */
    public MySwitchInputWidget getSecondInputNode() {
        return secondInputNode;
    }

    /**
     *
     * @param secondInputNode the second switch input component in the switch
     */
    public void setSecondInputNode(MySwitchInputWidget secondInputNode) {
        //reinitialize the previous switch input
        this.secondInputNode = secondInputNode;
    }

    /**
     *
     * @return if the second input node is the one connected in the switch
     */
    public boolean isSecondInputConnected() {
        if (secondInputNode != null) {
            if (secondInputNode.getSwitchInputNodeProperties().getIsSwitchConnectedToSecondInput()) {
                return secondInputNode.getSwitchInputNodeProperties().getIsSwitchConnectedToSecondInput();
            }

            if (firstInputNode != null) {
                return firstInputNode.getSwitchInputNodeProperties().getIsSwitchConnectedToSecondInput();
            }
            return false;
        } else {
            return false;
        }

    }

    /**
     *
     * @param secondInputConnected if the second input node is the one connected
     * in the switch
     */
    public void setSecondInputConnected(boolean secondInputConnected) {
        this.secondInputConnected = secondInputConnected;
    }

    /**
     *
     * @return the second switch output component in the switch
     */
    public MySwitchOutputWidget getFirstOutputNode() {
        return firstOutputNode;
    }

    /**
     *
     * @param firstOutputNode the first switch output component in the switch
     */
    public void setFirstOutputNode(MySwitchOutputWidget firstOutputNode) {
        this.firstOutputNode = firstOutputNode;
    }

    /**
     *
     * @return the second switch output component in the switch
     */
    public MySwitchOutputWidget getSecondOutputNode() {
        return secondOutputNode;
    }

    /**
     *
     * @param secondOutputNode the second switch output component in the switch
     */
    public void setSecondOutputNode(MySwitchOutputWidget secondOutputNode) {

        this.secondOutputNode = secondOutputNode;
    }

    public boolean isSecondOutputConnected() {
        if (secondOutputNode != null) {
            if (secondOutputNode.getSwitchOutputNodeProperties().getIsSwitchConnectedToSecondOutput()) {
                return secondOutputNode.getSwitchOutputNodeProperties().getIsSwitchConnectedToSecondOutput();
            }

            if (firstOutputNode != null) {
                return firstOutputNode.getSwitchOutputNodeProperties().getIsSwitchConnectedToSecondOutput();
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     *
     * @param secondOutputConnected if the second output is the one connected to
     * the switch
     */
    public void setSecondOutputConnected(boolean secondOutputConnected) {
        this.secondOutputConnected = secondOutputConnected;
    }

    /**
     *
     * @return the first intermediate component of the switch
     */
    public MySwitchIntermediateWidget getFirstIntermediateNode() {
        return firstIntermediateNode;
    }

    /**
     *
     * @param firstIntermediateNode the first intermediate component of the
     * switch
     */
    public void setFirstIntermediateNode(MySwitchIntermediateWidget firstIntermediateNode) {
        this.firstIntermediateNode = firstIntermediateNode;
    }

    /**
     *
     * @return the second intermediate component of the switch
     */
    public MySwitchIntermediateWidget getSecondIntermediateNode() {
        return secondIntermediateNode;
    }

    /**
     *
     * @param secondIntermediateNode the second intermediate component of the
     * switch
     */
    public void setSecondIntermediateNode(MySwitchIntermediateWidget secondIntermediateNode) {
        this.secondIntermediateNode = secondIntermediateNode;
    }

    /**
     *
     * @return the first stop sensor component of the switch
     */
    public MyStopSensorWidget getFirstEndStopSensorNode() {
        return firstEndStopSensorNode;
    }

    /**
     *
     * @param firstEndStopSensorNode the first stop sensor component of the
     * switch
     */
    public void setFirstEndStopSensorNode(MyStopSensorWidget firstEndStopSensorNode) {

        this.firstEndStopSensorNode = firstEndStopSensorNode;
    }

    /**
     *
     * @return the second stop sensor component of the switch
     */
    public MyStopSensorWidget getSecondEndStopSensorNode() {
        return secondEndStopSensorNode;
    }

    /**
     *
     * @param secondEndStopSensorNode the second stop sensor component of the
     * switch
     */
    public void setSecondEndStopSensorNode(MyStopSensorWidget secondEndStopSensorNode) {

        this.secondEndStopSensorNode = secondEndStopSensorNode;
    }

    /**
     *
     * @return the first sensor output component of the switch
     */
    public MySensorWidget getFirstSensorOutputNode() {
        return firstSensorOutputNode;
    }

    /**
     *
     * @param firstSensorOutputNode the first sensor output component of the
     * switch
     */
    public void setFirstSensorOutputNode(MySensorWidget firstSensorOutputNode) {
        this.firstSensorOutputNode = firstSensorOutputNode;
    }

    /**
     *
     * @return the second sensor output component of the switch
     */
    public MySensorWidget getSecondSensorOutputNode() {
        return secondSensorOutputNode;
    }

    /**
     *
     * @param secondSensorOutputNode the second sensor output component of the
     * switch
     */
    public void setSecondSensorOutputNode(MySensorWidget secondSensorOutputNode) {
        this.secondSensorOutputNode = secondSensorOutputNode;
    }

    /**
     *
     * @return the index in the list of switches
     */
    public static Integer getSwitchNumberInSwitchList() {
        return switchNumberInSwitchList;
    }

    /**
     *
     * @param switchNumberInSwitchList the index in the list of switches
     */
    public static void setSwitchNumberInSwitchList(Integer switchNumberInSwitchList) {
        MySwitchWidget.switchNumberInSwitchList = switchNumberInSwitchList;
    }

    /**
     *
     * @return the icon representing a switch
     */
    public IconNodeWidget getWidget() {
        return widget;
    }

    /**
     *
     * @param widget the icon representing a switch
     */
    public void setWidget(IconNodeWidget widget) {
        this.widget = widget;
    }

    /**
     *
     * @return if the components of the switch are showing at their bottom the
     * switch that they belong to
     */
    public static Boolean getIsSwitchChildrenShowed() {
        return isSwitchChildrenShowed;
    }

    /**
     *
     * @param isSwitchChildrenShowed if the components of the switch are showing
     * at their bottom the switch that they belong to
     */
    public static void setIsSwitchChildrenShowed(Boolean isSwitchChildrenShowed) {
        MySwitchWidget.isSwitchChildrenShowed = isSwitchChildrenShowed;
    }

}
