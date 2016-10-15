package fr.arezzo.designer.Dialogs.DialogBoxes;

import fr.arezzo.designer.Scene.Scene;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.openide.util.ImageUtilities;

/**
 * DialogBoxList is a custom dialog box which contains a list as input
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 * @param <T> the type of choices for the user
 */
public class DialogBoxList<T> {

    //possible choices for the user
    private List<String> possibilities = new ArrayList<String>();
    //reply value per choice
    private List<T> ResultOfChoice = new ArrayList<T>();
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
     * DialogBoxList initializes all of the fields, the first value of
     * resultsOfChoice must be related to the first value possibilities and so
     * on
     *
     * @param title represents the title of the dialog
     * @param text represents the description of the dialog
     * @param possibilities represents a list of single choice string to select
     * @param ResultOfChoice represents what to return for processing depending
     * on what the user have chosen from the possibilities
     * @param messageType the type of the message
     */
    public DialogBoxList(String title, String text, List<String> possibilities, List<T> ResultOfChoice, MessageType messageType) {

        //stop linking process first #BuG Fix
        Scene.globalScene.stopLinkingProcess();

        this.possibilities = possibilities;
        this.ResultOfChoice = ResultOfChoice;
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
     * make a choice and correspond the choice of the user with the
     * corresponding resultOfHisChoice
     *
     * @return T is the user choice to be processed
     */
    public T showDialogAndGetResult() {
        try {
            if (possibilities.get(0) != null && ResultOfChoice.get(0) != null) {
                String choice = (String) JOptionPane.showInputDialog(
                        null,
                        text,
                        title,
                        messageTypeIntegerValue,
                        new ImageIcon(ImageUtilities.loadImage("fr/arezzo/designer/resources/workstation.png")),
                        possibilities.toArray(),
                        possibilities.get(0) != null ? possibilities.get(0) : "");
                int indexOfUserChoice = possibilities.indexOf(choice);
                if (ResultOfChoice.get(indexOfUserChoice) != null) {
                    return ResultOfChoice.get(indexOfUserChoice);
                } else {
                    return ResultOfChoice.get(0);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid Choices");
            return null;
        }
        JOptionPane.showMessageDialog(null, "Invalid Choices");
        return null;
    }

    /**
     * getPossibilities return the possibilities that the user can chose from
     *
     * @return List of possibilities the user can chose from
     *
     */
    public List<String> getPossibilities() {
        return possibilities;
    }

    /**
     * setPossibilities sets the possibilities that the user can chose from
     *
     * @param possibilities is List of possibilities the user can chose from
     *
     */
    public void setPossibilities(List<String> possibilities) {
        this.possibilities = possibilities;
    }

    /**
     * getResultOfChoice returns the answers chosen that are to be processed
     *
     * @return List of the possible processable answers
     */
    public List<T> getResultOfChoice() {
        return ResultOfChoice;
    }

    /**
     * setResultOfChoice sets the answers chosen that are to be processed
     *
     * @param ResultOfChoice is a List of the possible processable answers
     */
    public void setResultOfChoice(List<T> ResultOfChoice) {
        this.ResultOfChoice = ResultOfChoice;
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
     * getMessageType returns the type of message for the dialog box
     *
     * @return MessageType is the type of message for the dialog box
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * setMessageType sets the type of message for the dialog box
     *
     * @param messageType is the type of message for the dialog box
     */
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * getMessageTypeIntegerValue returns the type of message for the dialog box
     * as integer value
     *
     * @return integer that represents the type of message for the dialog box
     */
    public int getMessageTypeIntegerValue() {
        return messageTypeIntegerValue;
    }

    /**
     * setMessageTypeIntegerValue sets the type of message for the dialog box as
     * integer value
     *
     * @param messageTypeIntegerValue represents the type of message for the
     * dialog box as an integer
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
