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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

import javax.swing.border.Border;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.Scene.Scene;

/**
 * DialogBoxCheckBoxes is a custom dialog box which contains check boxes as
 * input
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class DialogBoxCheckBoxes extends JFrame {

    //Panel that will contain the choices of the user
    JPanel panel = null;
    //label that will contain the title
    JLabel label = null;
    //possible choices for the user
    private List<String> possibilities = new ArrayList<String>();
    //possible answers depending on suggested possible choices
    private List<String> answers = new ArrayList<String>();
    //list of answers from the user's choices
    private List<String> answerListOfUser = new ArrayList<String>();
    //title of the message dialog
    private String title;
    //text inside the message dialog
    private String text;
    //the checkboxes that will contain the choices of the user
    private JCheckBox[] checkBoxes;

    
    /**
     * DialogBoxCheckBoxes is a dialog box that contain check boxes where the
     * user can chose from many possible choices and we get the answers
     * depending of the possibilities chosen by the user
     *
     * @param title represents the title of the dialog box
     * @param text represents the description of the dialog box or prompt text
     * @param possibilities possibilities represents a list of multiple choice
     * strings to select from
     * @param answers represents the possible answers that we need to get processed
     * depending on the choice of the user from the possibilities
     */
    public DialogBoxCheckBoxes(String title, String text, List<String> possibilities, List<String> answers) {
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
            panel = createSimpleDialogBoxCheckBoxes();
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
     * getResults shows the dialog message and prompt the user to make a choice
     * and correspond the choice of the user with the corresponding
     * resultOfHisChoice
     *
     * @return the list of choices from the user
     */
    public List<String> getResults() {
        List<String> responses = null;
        try {
            //block waiting for the result of the dialog box
            MyWorkstationWidget.semaphore.acquire();
            MyWorkstationWidget.mutex.acquire();
            responses = answerListOfUser;
            MyWorkstationWidget.mutex.release();
            MyWorkstationWidget.semaphore.release();

            return responses;
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        return responses;

    }

    /**
     * createSimpleDialogBoxCheckBoxes Creates the panel that will be inside the
     * window that contains check boxes for choices
     *
     * @return JPanel the panel created
     */
    private JPanel createSimpleDialogBoxCheckBoxes() {
        //number of check boxes is the number of possible choices
        final int numButtons = possibilities.size();
        //the check boxes choices
        checkBoxes = new JCheckBox[numButtons];
        //the regroupment of the check boxes, so that only one check box is selected at any time
        //final ButtonGroup group = new ButtonGroup();
        //the button that the user must click to validate his choice
        JButton choseButton = null;
        //initializing the check boxes using the choices
        for (int i = 0; i < numButtons; i++) {
            //initialize the label of the check box which is what the user sees
            checkBoxes[i] = new JCheckBox(possibilities.get(i));
            //initialize the command name of the check box which is the data that we need
            checkBoxes[i].setActionCommand(answers.get(i));

        }

        //initialize the button with its label
        choseButton = new JButton("Chose");
        //add the action that will happen when the user clicks the button
        choseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = 0;
                for (JCheckBox cb : checkBoxes) {
                    if (cb.isSelected()) {
                        //answerListOfUser.add(cb.getText());
                        answerListOfUser.add(answers.get(i));
                    }
                    i++;
                }
                if (answerListOfUser.isEmpty()) {
                    answerListOfUser.add(answers.get(0));
                }
                //close thhe window
                dispose();
                return;
            }
        });
        //call the create pane function to instanciate the panel ans return it
        return createPane(text,
                choseButton);
    }

    /**
     * createPane Used by createSimpleDialogBoxRadioButtons to create a pane
     * containing a description, a single column of check boxes, and the chose
     * button.
     *
     * @param description is the description to be shown inside the panel, the
     * list of check boxes to be added to the panel,
     * @param showButton is the button to be added to the panel
     * @return JPanel is the panel created
     */
    private JPanel createPane(String description, JButton showButton) {
        //the number of check boxes
        int numChoices = checkBoxes.length;
        //the box that will contain the check boxes and the description
        JPanel box = new JPanel();
        //the label that contains the description
        JLabel label = new JLabel(description);
        //set the layout of the box as a vertical layout (column)
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        //add the label to the box panel
        box.add(label);
        //add the check boxes to the box panel
        for (int i = 0; i < numChoices; i++) {
            box.add(checkBoxes[i]);
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
     * @return JPanel that represents the panel of the dialog
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * setPanel sets the panel of the dialog box
     * @param panel 
     */
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    /**
     * getLabel returns the label that contains the description of the dialog
     * @return JLabel is the label that contains the description of the dialog
     */
    public JLabel getLabel() {
        return label;
    }

    /**
     * setLabel sets the label that contains the description of the dialog
     * @param label is the label that contains the description of the dialog
     */
    public void setLabel(JLabel label) {
        this.label = label;
    }

    /**
     * getAnswers returns the answers that are to be processed
     * @return List of the possible processable answers
     */
    public List<String> getAnswers() {
        return answers;
    }

    /**
     * setAnswers sets the answers that are to be processed
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
