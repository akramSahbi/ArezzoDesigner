package fr.arezzo.designer.palette;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * CategoryChildren helper class is used for filling the category names into the
 * palette
 *
 * @author akram.sahbi@esprit.tn
 */
@SuppressWarnings({"unchecked", "deprecation", "rawtypes"}) 
public class CategoryChildren extends Children.Keys<Category> {

    //name of the categories that will bw in the palette
    private static String[] Categories = new String[]{"Workstations", "Load/Unload Sensors", "Link Nodes", "Sensors", "Stop Sensors",
        "Switch Inputs", "Switch Intermediates", "Switch Outputs"
//       ,"Shuttles"
    };

    /**
     * constructor
     */
    public CategoryChildren() {
    }

    /**
     * this method creates the nodes that will fill each palette category
     *
     * @param key a Category (of widgets inside the palette)
     * @return nodes of the palette category
     */
    @Override
    protected Node[] createNodes(Category key) {
        Category obj =  key;
        return new Node[]{
            new CategoryNode(obj)
        };
    }

    /**
     * this method fills the Categories of the palette using the Categories
     * names (attribute) it also initializes the Category keys that will be used
     * in the method createNodes one by one
     */
    @Override
    protected void addNotify() {
        super.addNotify();
        Category[] objs = new Category[Categories.length];
        for (int i = 0; i < objs.length; i++) {
            Category cat = new Category();
            cat.setName(Categories[i]);
            objs[i] = cat;
        }
        setKeys(objs);
    }
}
