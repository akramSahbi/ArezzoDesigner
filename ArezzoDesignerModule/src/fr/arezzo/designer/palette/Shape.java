package fr.arezzo.designer.palette;

import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;

/**
 * Shape helper class is a class that represents the shape of a node for use by
 * the palette
 *
 * @author akram.sahbi@esprit.tn
 */
public class Shape {

    //the ID of the shape
    private Integer number;
    //the category name
    private String category;
    //the type of the shape to differentiate between shapes when creating a widget (type 3 for workstation...)
    private String type;
    //the type of the shape to differentiate between shapes when creating a widget WORKSTATION for workstation
    private WidgetCommonInfo.WidgetType shapeType;
     //
    //private String subCategory;

    //the name of the shape
    private String name;

    /**
     * constructor added to know the type of the shape
     *
     * @param category
     */
    public Shape(String category) {
        this.category = category;
    }

    /**
     * constructor
     *
     * @param number is the number of the node
     * @param category is the category name of the node
     * @param type is the type of the node
     * @param shapeType is the type of the shape
     * @param name is the name of the shape
     * @param image is the path to the image of the shape
     */
    public Shape(Integer number, String category, String type, WidgetCommonInfo.WidgetType shapeType, String name, String image) {
        this.number = number;
        this.category = category;
        this.type = type;
        this.shapeType = shapeType;
        this.name = name;
        this.image = image;
    }

    /**
     * constructor
     *
     * @param number is the number of the node
     * @param category is the category name of the node
     * @param title is the title of the shape
     * @param image is the path to the image of the shape
     */
    public Shape(Integer number, String category, String title, String image) {
        this.number = number;
        this.category = category;
        this.type = title;
        this.image = image;
    }

    /**
     * constructor
     */
    public Shape() {
    }

    /**
     *
     * @return the id of the shape (the number of the node)
     */
    public Integer getNumber() {
        return number;
    }

    /**
     *
     * @param number the id of the shape (the number of the node)
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     *
     * @return the category of the node's shape
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category the category of the node's shape
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return the path to the image of the shape
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image the path to the image of the shape
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @return the name of the shape
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name the name of the shape
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the type of the widget
     */
    public WidgetCommonInfo.WidgetType getShapeType() {
        return shapeType;
    }

    /**
     *
     * @param shapeType the type of the widget
     */
    public void setShapeType(WidgetCommonInfo.WidgetType shapeType) {
        this.shapeType = shapeType;
    }

    /**
     *
     * @return the type of the node (3...12)
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type the type of the node
     */
    public void setType(String type) {
        this.type = type;
    }
    private String image;

}
