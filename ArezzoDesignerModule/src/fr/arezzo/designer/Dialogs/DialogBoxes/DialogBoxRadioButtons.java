package fr.arezzo.designer.Dialogs.DialogBoxes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.Scene.Scene;

/**
 * DialogBoxRadioButtons is a custom dialog box which contains radio buttons as
 * input
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class DialogBoxRadioButtons extends JFrame {

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

    /**
     * DialogBoxRadioButtons is a dialog box that contain radio buttons where
     * the user can chose from many possible choices and we get the answer
     * depending of the possibility chosen by the user
     *
     * @param title represents the title of the dialog box
     * @param text represents the description of the dialog box or prompt text
     * @param possibilities represent a list of single choice strings to select
     * from
     * @param answers represents the possible answers that we need to get
     * processed
     */
    public DialogBoxRadioButtons(String title, String text, List<String> possibilities, List<String> answers) {
        try {
            
            //stop linking process first #BuG Fix
            Scene.globalScene.stopLinkingProcess();
            
            //fix the size of the window
            setSize(400, 250);
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
            panel = createSimpleDialogBoxRadioButtons();
            //a label that will contain the "text"
            label = new JLabel(title, JLabel.CENTER);
            //Lay out the panel's borders
            Border padding = BorderFactory.createEmptyBorder(20, 20, 5, 20);
            panel.setBorder(padding);
            //add the panel to the center of the window
            add(panel, BorderLayout.CENTER);
            //add the label to the top of the window
            add(label, BorderLayout.NORTH);
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
     * (from possibilities) and correspond the choice of the user with the
     * corresponding resultOfHisChoice
     *
     * @return a string representing the user choice
     */
    public String getResult() {
        String response = "0";
        try {
            //block waiting for the result of the dialog box
            MyWorkstationWidget.semaphore.acquire();
            MyWorkstationWidget.mutex.acquire();
            int indexOfChoice = possibilities.indexOf(answer);
            if (indexOfChoice != -1) {
                response = answers.get(indexOfChoice);
                //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }

            MyWorkstationWidget.mutex.release();
            MyWorkstationWidget.semaphore.release();

            return response + "";
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        return response;

    }

    /**
     * createSimpleDialogBoxRadioButtons Creates the panel that will be inside
     * the window that contains radio buttons for choices
     *
     * @return a JPanel that contains the radio buttons
     */
    private JPanel createSimpleDialogBoxRadioButtons() {
        //number of radio buttons is the number of possible choices
        final int numButtons = possibilities.size();
        //the radio buttons choices
        JRadioButton[] radioButtons = new JRadioButton[numButtons];
        //the regroupment of the radio buttons, so that only one radio button is selected at any time
        final ButtonGroup group = new ButtonGroup();
        //the button that the user must click to validate his choice
        JButton choseButton = null;
        //initializing the radio buttons using the choices
        for (int i = 0; i < numButtons; i++) {
            //initialize the label of the radio button which is what the user sees
            radioButtons[i] = new JRadioButton(possibilities.get(i));
            //initialize the command name of the radio button which is the data that we need
            radioButtons[i].setActionCommand((possibilities.get(i)));
        }
        //add all of the radio buttons to the group
        for (int i = 0; i < numButtons; i++) {
            group.add(radioButtons[i]);
        }
        //radioButtons[0].setSelected(true);
        //answer = possibilities.get(0);
        //initialize the button with its label
        choseButton = new JButton("Chose");
        //add the action that will happen when the user clicks the button
        choseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //see if the group of radio buttons have a radio button that has been selected
                if (group.getSelection() != null) {
                    //get the data that has been selected
                    String command = group.getSelection().getActionCommand();
                    //assign the data to answer
                    answer = command;

                } else {
                    //reset answer otherwise
                    answer = "0";
                }
                //prepare the result for the widget call
                answer = getResult();
                //close thhe window
                dispose();

                return;
            }
        });
        //call the create pane function to instanciate the panel ans return it
        return createPane(text,
                radioButtons,
                choseButton);
    }

    /**
     * createPane is used by createSimpleDialogBoxRadioButtons to create a pane
     * containing a description, a single column of radio buttons, and the chose
     * button.
     *
     *
     * @param description is the description to be shown inside the panel
     * @param radioButtons is the list of radio buttons to be added to the panel
     * @param showButton is the button to be added to the panel
     * @return the panel created that contains all of the initialized components
     */
    private JPanel createPane(String description, JRadioButton[] radioButtons, JButton showButton) {
        //the number of radio buttons
        int numChoices = radioButtons.length;
        //the box that will contain the radio buttons and the description
        JPanel box = new JPanel();
        //the label that contains the description
        JLabel label = new JLabel(description);
        //set the layout of the box as a vertical layout (column)
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        //add the label to the box panel
        box.add(label);
        //add the radio buttons to the box panel
        for (int i = 0; i < numChoices; i++) {
            box.add(radioButtons[i]);
        }
        //create a parent panel that will contain the box panel
        //the parent panel layout is a border layout
        JPanel pane = new JPanel(new BorderLayout());
        //add the box panel before the start of the content of the parent panel
        pane.add(box, BorderLayout.PAGE_START);
        //add the box panel after the end of the content of the parent panel
        pane.add(showButton, BorderLayout.PAGE_END);
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
     * @return a string of the possible processable answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * setAnswer sets the answer that is to be processed
     *
     * @param answer a string of the possible processable answer
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
