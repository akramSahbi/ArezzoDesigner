package fr.arezzo.designer.palette;

import org.openide.nodes.AbstractNode;
import org.openide.util.lookup.Lookups;

/**
 * CategoryNode helper class is a class that represents a category node inside
 * the palette which contains all of the children shape nodes of the category
 * and a label (Display name) to differentiate between different categories of
 * nodes
 *
 * @author akram.sahbi@esprit.tn
 */
public class CategoryNode extends AbstractNode {

    /**
     * Creates a new instance of CategoryNode, namely a category inside the
     * palette
     *
     * @param category the name of a certain category of widgets inside the
     * palette
     */
    public CategoryNode(Category category) {
        //initialze the category node with its shape children nodes (workstation shapes or sensor shapes or...)
        super(new ShapeChildren(category), Lookups.singleton(category));
        //set the name of the category inside the palette
        setDisplayName(category.getName());
    }

}
