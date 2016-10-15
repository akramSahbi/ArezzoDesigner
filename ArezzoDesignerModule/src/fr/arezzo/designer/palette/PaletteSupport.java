package fr.arezzo.designer.palette;

import fr.arezzo.designer.Scene.Scene;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JOptionPane;

import org.netbeans.spi.palette.DragAndDropHandler;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;

/**
 * PaletteSupport helper class is used to create the palette and its possible
 * functionalities like drag and drop
 *
 * @author akram.sahbi@esprit.tn
 */

@SuppressWarnings({"unchecked", "deprecation", "rawtypes"}) 
public class PaletteSupport {

    //public static final DataFlavor MyShapeNodeFlavor = new DataFlavor( Shape.class, "MyDND" ); 
    //object that will be transferred using the drag and drop
    public static final DataFlavor ShapeNodeFlavor = new DataFlavor(Shape.class, "AREZZO Shape OBJECT");

    /**
     * method that creates the palette
     *
     * @return the palette controller
     */
    public static PaletteController createPalette() {
        //initialize the palette using the categories and the shapes for each category 
        AbstractNode paletteRoot = new AbstractNode(new CategoryChildren());
        //name the palette
        paletteRoot.setName("Arezzo Palette");
//        paletteRoot.setShortDescription(MyConnectorWidget.currentlyLinking == true? "you are currently linking widgets, please cancel"
//                + "the linking process by pressing the unlink menu":"");
        return PaletteFactory.createPalette
                (paletteRoot, new MyActions(), null, new MyDnDHandler());
    }

    /**
     * MyActions represents the custom actions that can be added to the palette
     */
    private static class MyActions extends PaletteActions {

        @Override
        public Action[] getImportActions() {
            return null;
        }

        @Override
        public Action[] getCustomPaletteActions() {
            return null;
        }

        @Override
        public Action[] getCustomCategoryActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action[] getCustomItemActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action getPreferredAction(Lookup lookup) {
            return null;
        }
    }

    /**
     * customized drag and drop class handler to transfer an object of type
     * Shape ,which contains all of the needed informations (to create after that
     * future widgets using informations about the type of the shape), to the scene
     */
    private static class MyDnDHandler extends DragAndDropHandler {

        @Override
        public void customize(ExTransferable exTransferable, Lookup lookup) {
            //stop linking process first #BuG Fix
            Scene.globalScene.stopLinkingProcess();
            
            //shape object that will be transfered using drag and drop
            final Shape item = lookup.lookup(Shape.class);

            
            
            
            if (null != item) {

                //return the object transfered as a shape
                //shape Node flavor represents our custom supported type verification
                //so that the object that will be dragged and dropped must be a single "Shape"
                exTransferable.put(new ExTransferable.Single(PaletteSupport.ShapeNodeFlavor) {
                    @Override
                    protected Object getData() throws IOException, UnsupportedFlavorException {
                        if (item.getType().equals("999")) {
                            item.setName("");
                        }
                        return item;

                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "shape node is null");
            }
        }

    }
}
