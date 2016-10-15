package fr.arezzo.designer.actions.toolbar;

import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import fr.arezzo.designer.Scene.Scene;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 * LinkAction menu is used to linking/unlinking; if the user began the linking
 * process but changed his mind to do something else we unlink, if the user is
 * not already linking we begin the linking process
 *
 * @author akram.sahbi@esprit.tn
 */
@ActionID(
        category = "Edit",
        id = "fr.arezzo.designer.actions.toolbar.LinkAction"
)
@ActionRegistration(
        iconBase = "fr/arezzo/designer/resources/linking1616.png",
        displayName = "#CTL_LinkAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1125), //position = 1150), //separatorBefore = 1125), //separatorAfter = 1175),
    @ActionReference(path = "Toolbars/Undo", position = 0)
})
@Messages("CTL_LinkAction=Cancel Linking")
public final class LinkAction implements ActionListener {

    /**
     * the action that is performed when the cancel linking menu is clicked
     *
     * @param actionEvent the event triggered
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        //if already linking, cancel the linking process
        if (MyConnectorWidget.currentlyLinking) {
            //end of the mouse and keyboard for linking simulation
            Robot robot;
            try {
                robot = new Robot();

                //simulate the ctrl button release
                robot.keyRelease(KeyEvent.VK_CONTROL);
                //simulate the mouse button release
                //robot.mouseRelease(InputEvent.BUTTON1_MASK );

            } catch (AWTException ex) {
                Exceptions.printStackTrace(ex);
            }
            MyConnectorWidget.currentlyLinking = false;
            Scene.globalScene.validate();
            Scene.globalScene.repaint();
        } 
        //else begin the linking process
        else    
        {
            try {
                MyConnectorWidget.currentlyLinking = true;
                //initiate the type of the link to be used
                WidgetCommonInfo.linearLink = false;
                //simulate actions from the keyboard and the mouse to begin the linking process
                Robot robot = new Robot();
                //simulate the ctrl button hold
                robot.keyPress(KeyEvent.VK_CONTROL);
                //simulate the left mouse button hold
                //robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mousePress(InputEvent.BUTTON1_MASK);

            } catch (AWTException e) {
                //e.printStackTrace();
            }

        }

    }
}
