package fr.arezzo.designer.palette;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;
import fr.arezzo.designer.palette.Shape;

/**
 * ShapeNode helper class is a class that represents a shape node that will be
 * in the palette, ready for drag and drop
 *
 * @author akram.sahbi@esprit.tn
 */
public class ShapeNode extends AbstractNode implements Transferable {

    //the content is a shape
    private Shape shape;

    //the type of transfer in the drag and drop must be of type Shape and its name is AREZZO Shape Object
    public static final DataFlavor ShapeNodeFlavor = new DataFlavor(Shape.class, "AREZZO Shape OBJECT");

    /**
     *
     * @return the supported drag and drop data flavors (types)
     */
    public static DataFlavor[] getSupportedFlavors() {
        return supportedFlavors;
    }

    /**
     *
     * @param supportedFlavors the supported drag and drop data flavors (types)
     */
    public static void setSupportedFlavors(DataFlavor[] supportedFlavors) {
        ShapeNode.supportedFlavors = supportedFlavors;
    }

    /**
     *
     * @return the shape that is used
     */
    public Shape getShape() {
        return shape;
    }

    /**
     *
     * @param shape the shape that is used
     */
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * initializes the shape Node using a shape object
     *
     * @param key the shape to be used
     */
    public ShapeNode(Shape key) {
        super(Children.LEAF, Lookups.fixed(new Object[]{key}));
        this.shape = key;
        setDisplayName(shape.getName());
        setIconBaseWithExtension(key.getImage());

    }

    //supported flavors to our drag and drop action handler
    protected static DataFlavor[] supportedFlavors = {
        ShapeNodeFlavor, // Transfer as a Shape object,
    };

    /**
     *
     * @return the supported flavors to our drag and drop action handler
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors;
    }

    /**
     * verify that the object to be used in the drag and drop have a supported
     * type
     *
     * @param flavor flavor of the object to be used in the drag and drop from
     * the palette
     * @return true if the object is of supported type (must be a Shape) if not
     * supported object returns false
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavor.equals(ShapeNodeFlavor)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param flavor flavor of the object to be used in the drag and drop from
     * the palette
     * @return the shape that has been dragged and dropped
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        //verify that the object to be transferred is a shape
        if (flavor.equals(ShapeNodeFlavor)) {
            return shape;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

}
