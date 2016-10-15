package fr.arezzo.designer.Dialogs.DialogBoxes;

import fr.arezzo.designer.Scene.Scene;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.openide.util.ImageUtilities;

/**
 * DialogBoxText is a custom dialog box which contains a text as input
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class DialogBoxText {

    //title of the message dialog
    private String title;
    //text inside the message dialog
    private String text;
    //type of the message (MessageType.ERROR,MessageType.INFORMATION....)
    private MessageType messageType;
    //mapping of the messageType to its integer value
    private int messageTypeIntegerValue;
    /**
     * Used for error messages.
     */
    public static final int ERROR = 0;
    /**
     * Used for information messages.
     */
    public static final int INFORMATION = 1;
    /**
     * Used for warning messages.
     */
    public static final int WARNING = 2;
    /**
     * Used for questions.
     */
    public static final int QUESTION = 3;
    /**
     * No icon is used.
     */
    public static final int PLAIN = -1;

    /**
     * DialogBoxText constructor initialize all of the fields
     *
     * @param title represents the title of the dialog box
     * @param text represents the description of the dialog box or prompt text
     * @param messageType represents the type of the message
     */
    public DialogBoxText(String title, String text, MessageType messageType) {

        //stop linking process first #BuG Fix
        Scene.globalScene.stopLinkingProcess();

        this.title = title;
        this.text = text;
        this.messageType = messageType;
        //switch the type of the MessageType
        switch (this.messageType) {
            //if messageType is MessageType.ERROR
            case ERROR:
                //assign the integer constant ERROR = 0
                messageTypeIntegerValue = ERROR;
                break;
            //if messageType is MessageType.INFORMATION
            case INFORMATION:
                //assign the integer constant INFORMATION = 1
                messageTypeIntegerValue = INFORMATION;
                break;
            //if messageType is MessageType.WARNING
            case WARNING:
                //assign the integer constant WARNING = 2
                messageTypeIntegerValue = WARNING;
                break;
            //if messageType is MessageType.QUESTION
            case QUESTION:
                //assign the integer constant QUESTION = 3
                messageTypeIntegerValue = QUESTION;
                break;
            //if messageType is MessageType.PLAIN
            case PLAIN:
                //assign the integer constant PLAIN = -1
                messageTypeIntegerValue = PLAIN;
                break;
            default:
                messageTypeIntegerValue = INFORMATION;

        }
    }

    /**
     * showDialogAndGetResult shows the dialog message and prompt the user to
     * input a choice
     *
     * @return the choice of the user
     */
    public String showDialogAndGetResult() {
        try {
            
            //stop linking process first #BuG Fix
            Scene.globalScene.stopLinkingProcess();

            String answer = (String) JOptionPane.showInputDialog(
                    null,
                    text,
                    title,
                    messageTypeIntegerValue,
                    new ImageIcon(ImageUtilities.loadImage("fr/arezzo/designer/resources/workstation.png")),
                    null,
                    "");
            return answer;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid Answer");
            return "";
        }
    }

    /**
     * getTitle returns the title of the dialog box
     *
     * @return the title of the dialog box
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     * setTitle sets the title of the dialog box
     *
     * @param title is the title of the dialog box
     *
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getText gets the title of the dialog box
     *
     * @return String the title of the dialog box
     *
     */
    public String getText() {
        return text;
    }

    /**
     * setText sets the text for the dialog box
     *
     * @param text the description of the dialog box
     *
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * getMessageType gets the type of the message
     *
     * @return a MessageType that represents the type of the message of the
     * dialog
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * setMessageType sets the type of the message
     *
     * @param messageType represents the type of the message of the dialog
     */
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * getMessageTypeIntegerValue gets the type of the message of the dialog as
     * an integer
     *
     * @return an integer that represents the type of the message of the dialog
     */
    public int getMessageTypeIntegerValue() {
        return messageTypeIntegerValue;
    }

    /**
     * setMessageTypeIntegerValue sets the type of the message of the dialog as
     * an integer
     *
     * @param messageTypeIntegerValue is an integer that represents the type of
     * the message of the dialog
     */
    public void setMessageTypeIntegerValue(int messageTypeIntegerValue) {
        this.messageTypeIntegerValue = messageTypeIntegerValue;
    }

    /**
     * MessageType represents the type of the message dialog
     *
     *
     */
    public enum MessageType {

        ERROR, INFORMATION, WARNING, QUESTION, PLAIN

    }
}
