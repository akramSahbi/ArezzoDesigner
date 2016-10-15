package fr.arezzo.designer.palette;

import java.util.ArrayList;
import org.openide.nodes.Index;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;

/**
 * ShapeChildren helper class is a class that represents the nodes for use by
 * the palette, in every category in the palette there are many shapes (shape
 * children)
 *
 * @author akram.sahbi@esprit.tn
 */
@SuppressWarnings({"unchecked", "deprecation", "rawtypes"}) 
public class ShapeChildren extends Index.ArrayChildren {

    //category of the shape
    private Category category;
    //the list of all of the shape informations:
    //index| category_name| location of shape's image| type | label | widgetType
    private String[][] items = new String[][]{
        {"0", "Workstations", "fr/arezzo/designer/resources/workstation.png", "3", "Workstation", WidgetCommonInfo.WidgetType.WORKSTATION.toString()},
        {"1", "Load/Unload Sensors", "fr/arezzo/designer/resources/sensor_load _unload.png", "4", "Load/Unload Sensor", WidgetCommonInfo.WidgetType.LOAD_UNLOAD_SENSOR.toString()},
        {"2", "Sensors", "fr/arezzo/designer/resources/sensor.png", "8", "Sensor", WidgetCommonInfo.WidgetType.SENSOR.toString()},
        //shuttle does not have type here i made a type number 99 for testing only 
        // I WILL REMOVE 99 WHEN I FINISH THE SHUTTLE WIDGET
        //{"3", "Shuttles", "fr/arezzo/designer/resources/shuttle.png","99","Shuttle", WidgetCommonInfo.WidgetType.SHUTTLE.toString()}, 
        {"4", "Switch Inputs", "fr/arezzo/designer/resources/switchInput.png", "10", "Switch Input", WidgetCommonInfo.WidgetType.SWITCH_INPUT.toString()},
        {"5", "Switch Outputs", "fr/arezzo/designer/resources/switchOutput.png", "12", "Switch Output", WidgetCommonInfo.WidgetType.SWITCH_OUTPUT.toString()},
        {"6", "Switch Intermediates", "fr/arezzo/designer/resources/switch_intermediate.png", "11", "Switch Intermediate", WidgetCommonInfo.WidgetType.SWITCH_INTERMEDATE.toString()},
        {"7", "Stop Sensors", "fr/arezzo/designer/resources/end_sensor.png", "9", "Stop Sensor", WidgetCommonInfo.WidgetType.STOP_SENSOR.toString()},
        //load/unload workstations are now workstations with just load or unload operations
        //{"8", "Workstations", "fr/arezzo/designer/resources/Load Unload workstation.png","3","Load Unload workstation",WidgetCommonInfo.WidgetType.LOAD_UNLOAD_WORKSTATION.toString()},
        {"8", "Link Nodes", "fr/arezzo/designer/resources/link adapter.png", "999", "Virtual Adapter", WidgetCommonInfo.WidgetType.LINK_NODE.toString()},};

    /**
     * constructor of Shape children using category of the shape in the palette
     *
     * @param Category the category of the shapes
     */
    public ShapeChildren(Category Category) {
        this.category = Category;
    }

    /**
     * initialization of the shapes of the palette to regroup the shapes in the
     * palette using their category
     *
     * @return a list of shape nodes
     */
    @Override
    protected java.util.List initCollection() {
        //list of shape nodes that we will add to the palette
        ArrayList<ShapeNode> childrenNodes = new ArrayList<ShapeNode>(items.length);
        for (int i = 0; i < items.length; i++) {
            //regroup the shape nodes of the palette using the category of the shapeChildren
            if (category.getName().equals(items[i][1])) {
                //instanciate a new shape
                Shape item = new Shape();
                //set its number
                item.setNumber(new Integer(items[i][0]));
                //set its category
                item.setCategory(items[i][1]);
                //set its image
                item.setImage(items[i][2]);
                //set its type
                item.setType(items[i][3]);
                //set its name
                item.setName(items[i][4]);
                //set its shapeType
                item.setShapeType(convertToShape(items[i][5]));
                childrenNodes.add(new ShapeNode(item));
            }
        }
        return childrenNodes;
    }

    /**
     * convert a shape type to a widget type for future use
     *
     * @param shapeType is the string of the type of a shape node
     * @return is a WidgetType; the type of a widget node
     */
    private WidgetCommonInfo.WidgetType convertToShape(String shapeType) {
        return WidgetCommonInfo.WidgetType.valueOf(shapeType);
    }
}
