
package fr.arezzo.designer.Dialogs.DialogOutputMessages;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.openide.util.ImageUtilities;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.WidgetType;

/**
 * Alert is a custom alert message input
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class Alert {

    private static String title;
    private static String description;
    private static ImageIcon messageImage = new ImageIcon();
    private static AlertType alertType;
    private static WidgetType widgetType;

    /**
     * alert shows an alert message on the screen
     *
     * @param titleArg is the title of the alert dialog
     * @param descriptionArg is the description of the alert dialog
     * @param alertTypeArg is the type of the alert
     */
    public static void alert(String titleArg, String descriptionArg, AlertType alertTypeArg) {
        //initialize fields
        title = titleArg;
        description = descriptionArg;
        alertType = alertTypeArg;
        /* if we want to use our own icons for the message alert
         //widgetType = widgetTypeArg;
         //createMessageImageIcon();
         */
        JOptionPane.showMessageDialog(null, description, title, alertTypeArg.value, null);
    }

    /**
     * createMessageImageIcon creates an icon for the alert depending on the
     * Widget Type
     *
     */
    public static void createMessageImageIcon() {
        //instanciate new image icon
        messageImage = new ImageIcon();
        switch (widgetType) {
            case LOAD_UNLOAD_WORKSTATION:
                //set the image of the image icon depending on the type of the widget
                messageImage.setImage(ImageUtilities.loadImage("tn/esprit/editeurtest/resources/sensor_load _unload.png"));
                break;
            case WORKSTATION:
                //set the image of the image icon depending on the type of the widget
                messageImage.setImage(ImageUtilities.loadImage("tn/esprit/editeurtest/resources/workstation.png"));
                break;
            case STOP_SENSOR:
                //set the image of the image icon depending on the type of the widget
                messageImage.setImage(ImageUtilities.loadImage("tn/esprit/editeurtest/resources/end_sensor.png"));
                break;
            case SENSOR:
                //set the image of the image icon depending on the type of the widget
                messageImage.setImage(ImageUtilities.loadImage("tn/esprit/editeurtest/resources/sensor.png"));
                break;
            case SHUTTLE:
                //set the image of the image icon depending on the type of the widget
                messageImage.setImage(ImageUtilities.loadImage("tn/esprit/editeurtest/resources/shuttle.png"));
                break;
            case SWITCH_INPUT:
                //set the image of the image icon depending on the type of the widget
                messageImage.setImage(ImageUtilities.loadImage("tn/esprit/editeurtest/resources/switchInput.png"));
                break;
            case SWITCH_INTERMEDATE:
                //set the image of the image icon depending on the type of the widget
                messageImage.setImage(ImageUtilities.loadImage("tn/esprit/editeurtest/resources/switch_intermediate.png"));
                break;
            case SWITCH_OUTPUT:
                //set the image of the image icon depending on the type of the widget
                messageImage.setImage(ImageUtilities.loadImage("tn/esprit/editeurtest/resources/switchOutput.png"));
                break;
        }
    }

    /**
     * AlertType represents the type of the alert message
     *
     */
    public enum AlertType {

        ERROR_MESSAGE(JOptionPane.ERROR_MESSAGE),
        INFORMATION_MESSAGE(JOptionPane.INFORMATION_MESSAGE),
        WARNING_MESSAGE(JOptionPane.WARNING_MESSAGE),
        QUESTION_MESSAGE(JOptionPane.QUESTION_MESSAGE),
        PLAIN_MESSAGE(JOptionPane.PLAIN_MESSAGE);

        AlertType(int number) {
            value = number;
        }
        public final int value;
    }
}
