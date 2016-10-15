package fr.arezzo.designer.Dialogs.DialogBoxes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.Border;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.WidgetType;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.Scene.Scene;

/**
 * DialogBoxYesNoButtons is a custom dialog box which contains buttons as input
 * (Yes or No is used in our case)
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class DialogBoxYesNoButtons extends JFrame {

    //Panel that will contain the choices of the user

    JPanel panel = null;
    //label that will contain the title
    JLabel label = null;
    //possible choices for the user
    private List<String> possibilities = new ArrayList<String>();
    //possible answers depending on suggested possible choices
    private List<String> answers = new ArrayList<String>();
    private String answer = "-1";
    //title of the message dialog
    private String title;
    //text inside the message dialog
    private String text;
    //the type of the icon to be displayed
    private WidgetType iconType;
    //the image to be displayed inside the message box
    private ImageIcon messageImage = new ImageIcon();

    /**
     * DialogBoxYesNoButtons is a dialog box where the user can chose from
     * buttons, the constructor initializes all of the fields
     *
     * @param title represents the title of the dialog box
     * @param text represents the description of the dialog box or prompt text
     * @param possibilities possibilities represents a list of multiple choice
     * strings to select from
     * @param answers represents the possible answers that we need to get
     * processed depending on the choice of the user from the possibilities
     * @param iconType represents the type of widget's icon to be used for the
     * dialog
     */
    public DialogBoxYesNoButtons(String title, String text, List<String> possibilities, List<String> answers, WidgetType iconType) {
        try {
            
            //stop linking process first #BuG Fix
            Scene.globalScene.stopLinkingProcess();
            //initialize the type of icon to know what image to display
            this.iconType = iconType;
            //intitialize image for the message
            switch (iconType) {

                case LOAD_UNLOAD_SENSOR:
                    messageImage.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/sensor_load _unload.png"));
                    break;
                case LOAD_UNLOAD_WORKSTATION:
                    messageImage.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/Load Unload workstation.png"));
                    break;
                case WORKSTATION:
                    messageImage.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/workstation.png"));
                    break;
                case STOP_SENSOR:
                    messageImage.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/end_sensor.png"));
                    break;
                case SENSOR:
                    messageImage.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/sensor.png"));
                    break;
                case SHUTTLE:
                    messageImage.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/shuttle.png"));
                    break;
                case SWITCH_INPUT:
                    messageImage.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/switchInput.png"));
                    break;
                case SWITCH_INTERMEDATE:
                    messageImage.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/switch_intermediate.png"));
                    break;
                case SWITCH_OUTPUT:
                    messageImage.setImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/switchOutput.png"));
                    break;
            }

            //fix the size of the window
            setSize(400, 150);
            setPreferredSize(getSize());
            //initiate the needed choices
            this.possibilities = possibilities;
            //initiate the needed answers depending on the choices
            this.answers = answers;
            //the title of the window
            this.title = title;
            //the text that explain the choice of the user
            this.text = text;
            //the panel that contains all of the graphical componenets
            panel = createSimpleDialogBoxButtons();
            //a label that will contain the "text"
            //label = new JLabel(title, JLabel.CENTER);
            label = new JLabel(title, messageImage, JLabel.CENTER);
            //image label to add to panel
            //JLabel imageLabel = new JLabel(messageImage);

            //Lay out the panel's borders
            Border padding = BorderFactory.createEmptyBorder(20, 20, 5, 20);
            panel.setBorder(padding);
            //add the panel to the center of the window
            add(panel, BorderLayout.CENTER);
            //add the label to the top of the window
            add(label, BorderLayout.NORTH);
            //
            //add(imageLabel,BorderLayout.WEST);
            //set the initial location of the window to the center of the screen
            Dimension dimemsion = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation(dimemsion.width / 2 - getSize().width / 2, dimemsion.height / 2 - getSize().height / 2);
            //set the image to be shown in the window (near the title)
            setIconImage(ImageUtilities.loadImage("fr/arezzo/designer/resources/workstation.png"));
            //dispose the window when the user press the X button of the window
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //do not resize
            setResizable(false);
            pack();

             //so that always on top of other windows
            setAlwaysOnTop(true);
            
            this.setVisible(true);
           

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * getResult shows the dialog message and prompt the user to make a choice
     * and correspond the choice of the user with the corresponding answer to be
     * processed
     *
     * @return a string representing the user's answer that we can process
     */
    public String getResult() {
        String response = "0";
        try {
            //stop linking process first #BuG Fix
            Scene.globalScene.stopLinkingProcess();
            //block waiting for the result of the dialog box
            MyWorkstationWidget.semaphore.acquire();
            MyWorkstationWidget.mutex.acquire();
            int indexOfChoice = possibilities.indexOf(answer);
            if (indexOfChoice != -1) {
                response = answers.get(indexOfChoice);
                //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
            //unlock the other wating thread
            MyWorkstationWidget.mutex.release();
            MyWorkstationWidget.semaphore.release();

            return response + "";
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        return response;

    }

    /**
     * createSimpleDialogBoxButtons Creates the panel that will be inside the
     * window that contains buttons as choices
     *
     * @return a JPanel that represents the panel that will be inside the window
     * that contains buttons as choices
     */
    private JPanel createSimpleDialogBoxButtons() {
        //number of buttons is the number of possible choices
        final int numButtons = possibilities.size();
        //the buttons choices
        JButton[] buttons = new JButton[numButtons];
        //the box that will contain the buttons and the description
        JPanel group = new JPanel();
        //set the layout of the box as a side by side layout (Line)
        group.setLayout(new FlowLayout());
        //initializing the buttons using the choices
        for (int i = 0; i < numButtons; i++) {
            //initialize the label of the button which is what the user sees
            buttons[i] = new JButton(possibilities.get(i));
            //initialize the command name of the button which is the data that we need
            buttons[i].setActionCommand((possibilities.get(i)));
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //get the data that has been selected
                    String command = e.getActionCommand();
                    //assign the data to answer
                    answer = command;
                    //prepare the result for the widget call
                    getResult();
                    //close thhe window
                    dispose();
                    return;
                }
            });
        }
        //add all of the buttons to the group
        for (int i = 0; i < numButtons; i++) {
            group.add(buttons[i]);
        }

        //call the create pane function to instanciate the panel ans return it
        return createPane(text,
                group);
    }

    /**
     * createPane is Used by createSimpleDialogBoxButtons to create a panel
     * containing a description, a single panel of side by side buttons
     *
     * @param description is the description to be shown inside the panel
     * @param buttonsPanel a panel that contains a list of buttons
     * @return a JPanel is the panel that contains all of the integrated
     * components
     */
    private JPanel createPane(String description, JPanel buttonsPanel) // JButton[] buttons) 
    {
        //the number of buttons
        //int numChoices = buttons.length;
        //the box that will contain the buttons and the description
        JPanel box = new JPanel();
        //the label that contains the description
        JLabel label = new JLabel(description);
        //set the layout of the box as a vertical layout (column)
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        //add the label to the box panel
        box.add(label);
        //add the panel of buttons to the vertical panel
        box.add(buttonsPanel);
        //create a parent panel that will contain the box panel
        //the parent panel layout is a border layout
        JPanel pane = new JPanel(new BorderLayout());
        //add the box panel at the start of the content of the parent panel
        pane.add(box, BorderLayout.PAGE_START);

        return pane;
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
     * getPanel returns the panel of the dialog box
     *
     * @return JPanel that represents the panel of the dialog
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * setPanel sets the panel of the dialog box
     *
     * @param panel
     */
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    /**
     * getLabel returns the label that contains the description of the dialog
     *
     * @return JLabel is the label that contains the description of the dialog
     */
    public JLabel getLabel() {
        return label;
    }

    /**
     * setLabel sets the label that contains the description of the dialog
     *
     * @param label is the label that contains the description of the dialog
     */
    public void setLabel(JLabel label) {
        this.label = label;
    }

    /**
     * getAnswer returns the answer that is to be processed
     *
     * @return a string that represents the answer that is to be processed
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * setAnswer sets the answer that is to be processed
     *
     * @param answer represents the answer that is to be processed
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * getAnswers returns the answers that are to be processed
     *
     * @return List of the possible processable answers
     */
    public List<String> getAnswers() {
        return answers;
    }

    /**
     * setAnswers sets the answers that are to be processed
     *
     * @param answers List of the possible processable answers
     */
    public void setAnswers(List<String> answers) {
        this.answers = answers;
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
