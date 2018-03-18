package fr.arezzo.designer.actions.export;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

import fr.arezzo.designer.Dialogs.DialogOutputMessages.Alert;
import static fr.arezzo.designer.Domain.CoordinatesOfArezzo.updateNewOriginOfTheSceneAndArezzoCoordinates;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType3;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType4_8_9;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType6_10_11_12;
import fr.arezzo.designer.DomainWidgets.MyShuttleWidget;
import fr.arezzo.designer.DomainWidgets.MySwitchWidget;
import fr.arezzo.designer.DomainWidgets.types3.MyLoadUnloadWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyLoadUnloadSensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MySensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyStopSensorWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchInputWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchIntermediateWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchOutputWidget;
import fr.arezzo.designer.EditeurModule.Repositories.ConnectorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.LoadUnloadSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.LoadUnloadWorkstationRepository;
import fr.arezzo.designer.EditeurModule.Repositories.ShuttleRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchInputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchIntermediateRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchOutputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchStopSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.WorkstationRepository;
import fr.arezzo.designer.Scene.Scene;
import fr.arezzo.designer.actions.verification.graph.VerifyNetwork;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileAttribute;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * ExportFilesToArezzo action class that is used to export the files for the
 * simulator configuration
 *
 *
 *
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
@ActionID(
        category = "File",
        id = "fr.arezzo.designer.actions.export.ExportFilesToArezzo"
)
@ActionRegistration(
        iconBase = "fr/arezzo/designer/resources/export.png",
        displayName = "#CTL_ExportFilesToArezzo"
)
@ActionReferences(
        {
            @ActionReference(path = "Menu/File/Export", position = 400, separatorBefore = 350, separatorAfter = 450),
            @ActionReference(path = "Toolbars/File", position = 500)
        })
@Messages("CTL_ExportFilesToArezzo=Export As Text Files")
public final class ExportFilesToArezzo implements ActionListener {

    //output tab where we will write the informations about the verifications
    private final InputOutput output = IOProvider.getDefault().getIO("Generation of plan_aip_aiguillage_vit.txt", true);
    //data to be written to file
    private StringBuilder fileOutputData = new StringBuilder("");
    //folder choser
    JFileChooser folderChooser = new JFileChooser(new File("."));
    File folderChosen = null;

    /**
     * exports the 3 necessary text files
     *
     * @param e the event triggered when clicking the ExportFilesToArezzo menu
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        //chose folder where to export files
        folderChooser.setDialogTitle("Chose the folder where do you want to export your files");
        folderChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (folderChooser.showOpenDialog(Scene.globalScene.getView()) == JFileChooser.APPROVE_OPTION) 
        {
            folderChosen = folderChooser.getSelectedFile().getAbsoluteFile();
            Alert.alert("Chosen path", "Your files will be saved at "+folderChosen.getPath(), Alert.AlertType.INFORMATION_MESSAGE);
            if(Files.notExists(Paths.get(folderChosen.getPath()), LinkOption.NOFOLLOW_LINKS)   )
            {
                try 
                {
                    Files.createDirectory(Paths.get(folderChosen.getPath()));
                } catch (IOException ex) 
                {
                    Exceptions.printStackTrace(ex);
                }
            }
        } else {
                
            
            if (folderChosen == null) {
                folderChosen = new File(".");
            }
            Alert.alert("No folder chosen", "Using default folder at " + folderChosen, Alert.AlertType.INFORMATION_MESSAGE);
        }
        //update new coordinates for arezzo
        updateNewOriginOfTheSceneAndArezzoCoordinates();
        //export cycles
        //call networkVerification to verify network and calculate cycles and fill them with their corresponding nodes
        VerifyNetwork networkVerification = new VerifyNetwork();
        networkVerification.actionPerformed(null);
        if (networkVerification.isValidNetwork()) {
            //gets the total number of nodes from each of the repositories
            //(shuttles are not nodes!)
            Integer totalNumberOfNetworkNodes
                    = ConnectorRepository.getInstance().getAll().size()
                    + LoadUnloadSensorRepository.getInstance().getAll().size()
                    + LoadUnloadWorkstationRepository.getInstance().getAll().size()
                    + //StopSensorRepository.getInstance().getAll().size() +
                    SwitchStopSensorRepository.getInstance().getAll().size()
                    + SwitchInputRepository.getInstance().getAll().size()
                    + SwitchIntermediateRepository.getInstance().getAll().size()
                    + SwitchOutputRepository.getInstance().getAll().size()
                    + SwitchSensorRepository.getInstance().getAll().size()
                    + WorkstationRepository.getInstance().getAll().size();
            output.select();
            printTextToOutput("####################Network is valid####################\n");
            printTextToOutput("####################Begining export process at " + (Instant.now()) + "####################\n");
            printTextToOutput("####################Exporting plan_aipaiguillage_vit.txt####################\n");
            //exporting to output first then to file "plan_aip_aiguillage_vit.txt"
            printTextToOutput(totalNumberOfNetworkNodes + "\n\n");
            //writing to cache String that to be exported to file
            fileOutputData.append(totalNumberOfNetworkNodes).append("\n\n");
            //exporting type 3 Workstations (not load unload workstations)
            for (MyWorkstationWidget widget : WorkstationRepository.getInstance().getAll()) {
                //get the properties of widget
                PropertiesOfNodesOfType3 widgetProperties = widget.getParentWorkstationProperties();
                //read properties
                String number = widgetProperties.getNumber() + "";
                String coordinates = "[" + widgetProperties.getxCoordinateArezzo() + " " + widgetProperties.getyCoordinateArezzo() + "]";
                String type = widgetProperties.getType() + "";
                String aipNumber = widgetProperties.getAipNumber() + "";
                StringBuilder listOfNextNodes = new StringBuilder("[");
                for (int nextNode : widgetProperties.getNumbersOfNextNodes()) {
                    listOfNextNodes.append(nextNode).append(" ");
                }
                int lastSpaceIndex = listOfNextNodes.lastIndexOf(" ");
                if (lastSpaceIndex != -1) {
                    listOfNextNodes.replace(lastSpaceIndex, lastSpaceIndex + 1, "");
                }
                listOfNextNodes.append("]");

                StringBuilder listOfSpeedsToNextNodes = new StringBuilder("[");
                for (float speedToNextNode : widgetProperties.getSpeedsToNextNodes()) {
                    listOfSpeedsToNextNodes.append(speedToNextNode).append(" ");
                }
                int lastSpaceIndexInSpeedList = listOfSpeedsToNextNodes.lastIndexOf(" ");
                if (lastSpaceIndexInSpeedList != -1) {
                    listOfSpeedsToNextNodes.replace(lastSpaceIndexInSpeedList, lastSpaceIndexInSpeedList + 1, "");
                }
                listOfSpeedsToNextNodes.append("]");

                String userDefinedNumberOfMachine = widgetProperties.getNumberOfTheMachine() + "";
                StringBuilder listOfOperations = new StringBuilder("[");
                for (int operation : widgetProperties.getOperationsOfThisMachine()) {
                    listOfOperations.append(operation).append(" ");
                }
                int lastSpaceIndexInOperations = listOfOperations.lastIndexOf(" ");
                if (lastSpaceIndexInOperations != -1) {
                    listOfOperations.replace(lastSpaceIndexInOperations, lastSpaceIndexInOperations + 1, "");
                }
                listOfOperations.append("]");

                StringBuilder listOfDurationsOfOperations = new StringBuilder("[");
                for (int duration : widgetProperties.getDurationsOfOperations()) {
                    listOfDurationsOfOperations.append(duration).append(" ");
                }
                int lastSpaceIndexInDurations = listOfDurationsOfOperations.lastIndexOf(" ");
                if (lastSpaceIndexInDurations != -1) {
                    listOfDurationsOfOperations.replace(lastSpaceIndexInDurations, lastSpaceIndexInDurations + 1, "");
                }
                listOfDurationsOfOperations.append("]");
                String QueueSize = widgetProperties.getWaitingQueueMaximumSize() + "";
                //write properties to output and add to cached String (that to be exported)
                printTextToOutput(number + "\n");
                fileOutputData.append(number).append("\n");
                printTextToOutput(coordinates + "\n");
                fileOutputData.append(coordinates).append("\n");
                printTextToOutput(type + "\n");
                fileOutputData.append(type).append("\n");
                printTextToOutput(aipNumber + "\n");
                fileOutputData.append(aipNumber).append("\n");
                printTextToOutput(listOfNextNodes.toString() + "\n");
                fileOutputData.append(listOfNextNodes.toString()).append("\n");
                printTextToOutput(listOfSpeedsToNextNodes.toString() + "\n");
                fileOutputData.append(listOfSpeedsToNextNodes.toString()).append("\n");
                printTextToOutput(userDefinedNumberOfMachine + "\n");
                fileOutputData.append(userDefinedNumberOfMachine).append("\n");
                printTextToOutput(listOfOperations.toString() + "\n");
                fileOutputData.append(listOfOperations.toString()).append("\n");
                printTextToOutput(listOfDurationsOfOperations.toString() + "\n");
                fileOutputData.append(listOfDurationsOfOperations.toString()).append("\n");
                printTextToOutput(QueueSize + "\n\n");
                fileOutputData.append(QueueSize).append("\n\n");

            }

            //exporting type 3 load unload workstations widgets
            for (MyLoadUnloadWorkstationWidget widget : LoadUnloadWorkstationRepository.getInstance().getAll()) {
                //get the properties of widget
                PropertiesOfNodesOfType3 widgetProperties = widget.getLoadUnloadWorkstationProperties();
                //read properties
                String number = widgetProperties.getNumber() + "";
                String coordinates = "[" + widgetProperties.getxCoordinateArezzo() + " " + widgetProperties.getyCoordinateArezzo() + "]";
                String type = widgetProperties.getType() + "";
                String aipNumber = widgetProperties.getAipNumber() + "";
                StringBuilder listOfNextNodes = new StringBuilder("[");
                for (int nextNode : widgetProperties.getNumbersOfNextNodes()) {
                    listOfNextNodes.append(nextNode).append(" ");
                }
                int lastSpaceIndex = listOfNextNodes.lastIndexOf(" ");
                if (lastSpaceIndex != -1) {
                    listOfNextNodes.replace(lastSpaceIndex, lastSpaceIndex + 1, "");
                }
                listOfNextNodes.append("]");

                StringBuilder listOfSpeedsToNextNodes = new StringBuilder("[");
                for (float speedToNextNode : widgetProperties.getSpeedsToNextNodes()) {
                    listOfSpeedsToNextNodes.append(speedToNextNode).append(" ");
                }
                int lastSpaceIndexInSpeedList = listOfSpeedsToNextNodes.lastIndexOf(" ");
                if (lastSpaceIndexInSpeedList != -1) {
                    listOfSpeedsToNextNodes.replace(lastSpaceIndexInSpeedList, lastSpaceIndexInSpeedList + 1, "");
                }
                listOfSpeedsToNextNodes.append("]");

                String userDefinedNumberOfMachine = widgetProperties.getNumberOfTheMachine() + "";
                StringBuilder listOfOperations = new StringBuilder("[");
                for (int operation : widgetProperties.getOperationsOfThisMachine()) {
                    listOfOperations.append(operation).append(" ");
                }
                int lastSpaceIndexInOperations = listOfOperations.lastIndexOf(" ");
                if (lastSpaceIndexInOperations != -1) {
                    listOfOperations.replace(lastSpaceIndexInOperations, lastSpaceIndexInOperations + 1, "");
                }
                listOfOperations.append("]");

                StringBuilder listOfDurationsOfOperations = new StringBuilder("[");
                for (int duration : widgetProperties.getDurationsOfOperations()) {
                    listOfDurationsOfOperations.append(duration).append(" ");
                }
                int lastSpaceIndexInDurations = listOfDurationsOfOperations.lastIndexOf(" ");
                if (lastSpaceIndexInDurations != -1) {
                    listOfDurationsOfOperations.replace(lastSpaceIndexInDurations, lastSpaceIndexInDurations + 1, "");
                }
                listOfDurationsOfOperations.append("]");
                String QueueSize = widgetProperties.getWaitingQueueMaximumSize() + "";
                //write properties to output and add to cached String (that to be exported)
                printTextToOutput(number + "\n");
                fileOutputData.append(number).append("\n");
                printTextToOutput(coordinates + "\n");
                fileOutputData.append(coordinates).append("\n");
                printTextToOutput(type + "\n");
                fileOutputData.append(type).append("\n");
                printTextToOutput(aipNumber + "\n");
                fileOutputData.append(aipNumber).append("\n");
                printTextToOutput(listOfNextNodes.toString() + "\n");
                fileOutputData.append(listOfNextNodes.toString()).append("\n");
                printTextToOutput(listOfSpeedsToNextNodes.toString() + "\n");
                fileOutputData.append(listOfSpeedsToNextNodes.toString()).append("\n");
                printTextToOutput(userDefinedNumberOfMachine + "\n");
                fileOutputData.append(userDefinedNumberOfMachine).append("\n");
                printTextToOutput(listOfOperations.toString() + "\n");
                fileOutputData.append(listOfOperations.toString()).append("\n");
                printTextToOutput(listOfDurationsOfOperations.toString() + "\n");
                fileOutputData.append(listOfDurationsOfOperations.toString()).append("\n");
                printTextToOutput(QueueSize + "\n\n");
                fileOutputData.append(QueueSize).append("\n\n");

            }

            //exporting type 4 load unload sensor widgets
            for (MyLoadUnloadSensorWidget widget : LoadUnloadSensorRepository.getInstance().getAll()) {
                //get the properties of widget
                PropertiesOfNodesOfType4_8_9 widgetProperties = widget.getLoadUnloadSensorProperties();
                //read properties
                String number = widgetProperties.getNumber() + "";
                String coordinates = "[" + widgetProperties.getxCoordinateArezzo() + " " + widgetProperties.getyCoordinateArezzo() + "]";
                String type = widgetProperties.getType() + "";
                String aipNumber = widgetProperties.getAipNumber() + "";
                StringBuilder listOfNextNodes = new StringBuilder("[");
                for (int nextNode : widgetProperties.getNumbersOfNextNodes()) {
                    listOfNextNodes.append(nextNode).append(" ");
                }
                int lastSpaceIndex = listOfNextNodes.lastIndexOf(" ");
                if (lastSpaceIndex != -1) {
                    listOfNextNodes.replace(lastSpaceIndex, lastSpaceIndex + 1, "");
                }
                listOfNextNodes.append("]");

                StringBuilder listOfSpeedsToNextNodes = new StringBuilder("[");
                for (float speedToNextNode : widgetProperties.getSpeedsToNextNodes()) {
                    listOfSpeedsToNextNodes.append(speedToNextNode).append(" ");
                }
                int lastSpaceIndexInSpeedList = listOfSpeedsToNextNodes.lastIndexOf(" ");
                if (lastSpaceIndexInSpeedList != -1) {
                    listOfSpeedsToNextNodes.replace(lastSpaceIndexInSpeedList, lastSpaceIndexInSpeedList + 1, "");
                }
                listOfSpeedsToNextNodes.append("]");

                //write properties to output and add to cached String (that to be exported)
                printTextToOutput(number + "\n");
                fileOutputData.append(number).append("\n");
                printTextToOutput(coordinates + "\n");
                fileOutputData.append(coordinates).append("\n");
                printTextToOutput(type + "\n");
                fileOutputData.append(type).append("\n");
                printTextToOutput(aipNumber + "\n");
                fileOutputData.append(aipNumber).append("\n");
                printTextToOutput(listOfNextNodes.toString() + "\n");
                fileOutputData.append(listOfNextNodes.toString()).append("\n");
                printTextToOutput(listOfSpeedsToNextNodes.toString() + "\n\n");
                fileOutputData.append(listOfSpeedsToNextNodes.toString()).append("\n\n");

            }

            //exporting type 8  sensor widgets
            for (MySensorWidget widget : SwitchSensorRepository.getInstance().getAll()) {
                //get the properties of widget
                PropertiesOfNodesOfType4_8_9 widgetProperties = widget.getSensorProperties();
                //read properties
                String number = widgetProperties.getNumber() + "";
                String coordinates = "[" + widgetProperties.getxCoordinateArezzo() + " " + widgetProperties.getyCoordinateArezzo() + "]";
                String type = widgetProperties.getType() + "";
                String aipNumber = widgetProperties.getAipNumber() + "";
                StringBuilder listOfNextNodes = new StringBuilder("[");
                for (int nextNode : widgetProperties.getNumbersOfNextNodes()) {
                    listOfNextNodes.append(nextNode).append(" ");
                }
                int lastSpaceIndex = listOfNextNodes.lastIndexOf(" ");
                if (lastSpaceIndex != -1) {
                    listOfNextNodes.replace(lastSpaceIndex, lastSpaceIndex + 1, "");
                }
                listOfNextNodes.append("]");

                StringBuilder listOfSpeedsToNextNodes = new StringBuilder("[");
                for (float speedToNextNode : widgetProperties.getSpeedsToNextNodes()) {
                    listOfSpeedsToNextNodes.append(speedToNextNode).append(" ");
                }
                int lastSpaceIndexInSpeedList = listOfSpeedsToNextNodes.lastIndexOf(" ");
                if (lastSpaceIndexInSpeedList != -1) {
                    listOfSpeedsToNextNodes.replace(lastSpaceIndexInSpeedList, lastSpaceIndexInSpeedList + 1, "");
                }
                listOfSpeedsToNextNodes.append("]");

                //write properties to output and add to cached String (that to be exported)
                printTextToOutput(number + "\n");
                fileOutputData.append(number).append("\n");
                printTextToOutput(coordinates + "\n");
                fileOutputData.append(coordinates).append("\n");
                printTextToOutput(type + "\n");
                fileOutputData.append(type).append("\n");
                printTextToOutput(aipNumber + "\n");
                fileOutputData.append(aipNumber).append("\n");
                printTextToOutput(listOfNextNodes.toString() + "\n");
                fileOutputData.append(listOfNextNodes.toString()).append("\n");
                printTextToOutput(listOfSpeedsToNextNodes.toString() + "\n\n");
                fileOutputData.append(listOfSpeedsToNextNodes.toString()).append("\n\n");

            }

            //exporting type 9  sensor widgets
            for (MyStopSensorWidget widget : SwitchStopSensorRepository.getInstance().getAll()) {
                //get the properties of widget
                PropertiesOfNodesOfType4_8_9 widgetProperties = widget.getStopSensorProperties();
                //read properties
                String number = widgetProperties.getNumber() + "";
                String coordinates = "[" + widgetProperties.getxCoordinateArezzo() + " " + widgetProperties.getyCoordinateArezzo() + "]";
                String type = widgetProperties.getType() + "";
                String aipNumber = widgetProperties.getAipNumber() + "";
                StringBuilder listOfNextNodes = new StringBuilder("[");
                for (int nextNode : widgetProperties.getNumbersOfNextNodes()) {
                    listOfNextNodes.append(nextNode).append(" ");
                }
                int lastSpaceIndex = listOfNextNodes.lastIndexOf(" ");
                if (lastSpaceIndex != -1) {
                    listOfNextNodes.replace(lastSpaceIndex, lastSpaceIndex + 1, "");
                }
                listOfNextNodes.append("]");

                StringBuilder listOfSpeedsToNextNodes = new StringBuilder("[");
                for (float speedToNextNode : widgetProperties.getSpeedsToNextNodes()) {
                    listOfSpeedsToNextNodes.append(speedToNextNode).append(" ");
                }
                int lastSpaceIndexInSpeedList = listOfSpeedsToNextNodes.lastIndexOf(" ");
                if (lastSpaceIndexInSpeedList != -1) {
                    listOfSpeedsToNextNodes.replace(lastSpaceIndexInSpeedList, lastSpaceIndexInSpeedList + 1, "");
                }
                listOfSpeedsToNextNodes.append("]");

                //write properties to output and add to cached String (that to be exported)
                printTextToOutput(number + "\n");
                fileOutputData.append(number).append("\n");
                printTextToOutput(coordinates + "\n");
                fileOutputData.append(coordinates).append("\n");
                printTextToOutput(type + "\n");
                fileOutputData.append(type).append("\n");
                printTextToOutput(aipNumber + "\n");
                fileOutputData.append(aipNumber).append("\n");
                printTextToOutput(listOfNextNodes.toString() + "\n");
                fileOutputData.append(listOfNextNodes.toString()).append("\n");
                printTextToOutput(listOfSpeedsToNextNodes.toString() + "\n\n");
                fileOutputData.append(listOfSpeedsToNextNodes.toString()).append("\n\n");

            }

            //exporting type 6  connector widgets
            for (MyConnectorWidget widget : ConnectorRepository.getInstance().getAll()) {
                //get the properties of widget
                PropertiesOfNodesOfType6_10_11_12 widgetProperties = widget.getMyConnectorInfo().getConnectorProperties();
                //read properties
                String number = widgetProperties.getNumber() + "";
                String coordinates = "[" + widgetProperties.getxCoordinateArezzo() + " " + widgetProperties.getyCoordinateArezzo() + "]";
                String type = widgetProperties.getType() + "";
                StringBuilder listOfNextNodes = new StringBuilder("[");
                for (int nextNode : widgetProperties.getNumbersOfNextNodes()) {
                    listOfNextNodes.append(nextNode).append(" ");
                }
                int lastSpaceIndex = listOfNextNodes.lastIndexOf(" ");
                if (lastSpaceIndex != -1) {
                    listOfNextNodes.replace(lastSpaceIndex, lastSpaceIndex + 1, "");
                }
                listOfNextNodes.append("]");

                StringBuilder listOfSpeedsToNextNodes = new StringBuilder("[");
                for (float speedToNextNode : widgetProperties.getSpeedsToNextNodes()) {
                    listOfSpeedsToNextNodes.append(speedToNextNode).append(" ");
                }
                int lastSpaceIndexInSpeedList = listOfSpeedsToNextNodes.lastIndexOf(" ");
                if (lastSpaceIndexInSpeedList != -1) {
                    listOfSpeedsToNextNodes.replace(lastSpaceIndexInSpeedList, lastSpaceIndexInSpeedList + 1, "");
                }
                listOfSpeedsToNextNodes.append("]");

                //write properties to output and add to cached String (that to be exported)
                printTextToOutput(number + "\n");
                fileOutputData.append(number).append("\n");
                printTextToOutput(coordinates + "\n");
                fileOutputData.append(coordinates).append("\n");
                printTextToOutput(type + "\n");
                fileOutputData.append(type).append("\n");
                printTextToOutput(listOfNextNodes.toString() + "\n");
                fileOutputData.append(listOfNextNodes.toString()).append("\n");
                printTextToOutput(listOfSpeedsToNextNodes.toString() + "\n\n");
                fileOutputData.append(listOfSpeedsToNextNodes.toString()).append("\n\n");

            }

            //exporting type 10  switch input widgets
            for (MySwitchInputWidget widget : SwitchInputRepository.getInstance().getAll()) {
                //get the properties of widget
                PropertiesOfNodesOfType6_10_11_12 widgetProperties = widget.getSwitchInputNodeProperties();
                //read properties
                String number = widgetProperties.getNumber() + "";
                String coordinates = "[" + widgetProperties.getxCoordinateArezzo() + " " + widgetProperties.getyCoordinateArezzo() + "]";
                String type = widgetProperties.getType() + "";
                StringBuilder listOfNextNodes = new StringBuilder("[");
                for (int nextNode : widgetProperties.getNumbersOfNextNodes()) {
                    listOfNextNodes.append(nextNode).append(" ");
                }
                int lastSpaceIndex = listOfNextNodes.lastIndexOf(" ");
                if (lastSpaceIndex != -1) {
                    listOfNextNodes.replace(lastSpaceIndex, lastSpaceIndex + 1, "");
                }
                listOfNextNodes.append("]");

                StringBuilder listOfSpeedsToNextNodes = new StringBuilder("[");
                for (float speedToNextNode : widgetProperties.getSpeedsToNextNodes()) {
                    listOfSpeedsToNextNodes.append(speedToNextNode).append(" ");
                }
                int lastSpaceIndexInSpeedList = listOfSpeedsToNextNodes.lastIndexOf(" ");
                if (lastSpaceIndexInSpeedList != -1) {
                    listOfSpeedsToNextNodes.replace(lastSpaceIndexInSpeedList, lastSpaceIndexInSpeedList + 1, "");
                }
                listOfSpeedsToNextNodes.append("]");

                //write properties to output and add to cached String (that to be exported)
                printTextToOutput(number + "\n");
                fileOutputData.append(number).append("\n");
                printTextToOutput(coordinates + "\n");
                fileOutputData.append(coordinates).append("\n");
                printTextToOutput(type + "\n");
                fileOutputData.append(type).append("\n");
                printTextToOutput(listOfNextNodes.toString() + "\n");
                fileOutputData.append(listOfNextNodes.toString()).append("\n");
                printTextToOutput(listOfSpeedsToNextNodes.toString() + "\n\n");
                fileOutputData.append(listOfSpeedsToNextNodes.toString()).append("\n\n");

            }

            //exporting type 11  switch intermediate widgets
            for (MySwitchIntermediateWidget widget : SwitchIntermediateRepository.getInstance().getAll()) {
                //get the properties of widget
                PropertiesOfNodesOfType6_10_11_12 widgetProperties = widget.getSwitchIntermediateNodeProperties();
                //read properties
                String number = widgetProperties.getNumber() + "";
                String coordinates = "[" + widgetProperties.getxCoordinateArezzo() + " " + widgetProperties.getyCoordinateArezzo() + "]";
                String type = widgetProperties.getType() + "";
                StringBuilder listOfNextNodes = new StringBuilder("[");
                for (int nextNode : widgetProperties.getNumbersOfNextNodes()) {
                    listOfNextNodes.append(nextNode).append(" ");
                }
                int lastSpaceIndex = listOfNextNodes.lastIndexOf(" ");
                if (lastSpaceIndex != -1) {
                    listOfNextNodes.replace(lastSpaceIndex, lastSpaceIndex + 1, "");
                }
                listOfNextNodes.append("]");

                StringBuilder listOfSpeedsToNextNodes = new StringBuilder("[");
                for (float speedToNextNode : widgetProperties.getSpeedsToNextNodes()) {
                    listOfSpeedsToNextNodes.append(speedToNextNode).append(" ");
                }
                int lastSpaceIndexInSpeedList = listOfSpeedsToNextNodes.lastIndexOf(" ");
                if (lastSpaceIndexInSpeedList != -1) {
                    listOfSpeedsToNextNodes.replace(lastSpaceIndexInSpeedList, lastSpaceIndexInSpeedList + 1, "");
                }
                listOfSpeedsToNextNodes.append("]");

                //write properties to output and add to cached String (that to be exported)
                printTextToOutput(number + "\n");
                fileOutputData.append(number).append("\n");
                printTextToOutput(coordinates + "\n");
                fileOutputData.append(coordinates).append("\n");
                printTextToOutput(type + "\n");
                fileOutputData.append(type).append("\n");
                printTextToOutput(listOfNextNodes.toString() + "\n");
                fileOutputData.append(listOfNextNodes.toString()).append("\n");
                printTextToOutput(listOfSpeedsToNextNodes.toString() + "\n\n");
                fileOutputData.append(listOfSpeedsToNextNodes.toString()).append("\n\n");

            }

            //exporting type 12  switch output widgets
            for (MySwitchOutputWidget widget : SwitchOutputRepository.getInstance().getAll()) {
                //get the properties of widget
                PropertiesOfNodesOfType6_10_11_12 widgetProperties = widget.getSwitchOutputNodeProperties();
                //read properties
                String number = widgetProperties.getNumber() + "";
                String coordinates = "[" + widgetProperties.getxCoordinateArezzo() + " " + widgetProperties.getyCoordinateArezzo() + "]";
                String type = widgetProperties.getType() + "";
                StringBuilder listOfNextNodes = new StringBuilder("[");
                for (int nextNode : widgetProperties.getNumbersOfNextNodes()) {
                    listOfNextNodes.append(nextNode).append(" ");
                }
                int lastSpaceIndex = listOfNextNodes.lastIndexOf(" ");
                if (lastSpaceIndex != -1) {
                    listOfNextNodes.replace(lastSpaceIndex, lastSpaceIndex + 1, "");
                }
                listOfNextNodes.append("]");

                StringBuilder listOfSpeedsToNextNodes = new StringBuilder("[");
                for (float speedToNextNode : widgetProperties.getSpeedsToNextNodes()) {
                    listOfSpeedsToNextNodes.append(speedToNextNode).append(" ");
                }
                int lastSpaceIndexInSpeedList = listOfSpeedsToNextNodes.lastIndexOf(" ");
                if (lastSpaceIndexInSpeedList != -1) {
                    listOfSpeedsToNextNodes.replace(lastSpaceIndexInSpeedList, lastSpaceIndexInSpeedList + 1, "");
                }
                listOfSpeedsToNextNodes.append("]");

                //write properties to output and add to cached String (that to be exported)
                printTextToOutput(number + "\n");
                fileOutputData.append(number).append("\n");
                printTextToOutput(coordinates + "\n");
                fileOutputData.append(coordinates).append("\n");
                printTextToOutput(type + "\n");
                fileOutputData.append(type).append("\n");
                printTextToOutput(listOfNextNodes.toString() + "\n");
                fileOutputData.append(listOfNextNodes.toString()).append("\n");
                printTextToOutput(listOfSpeedsToNextNodes.toString() + "\n\n");
                fileOutputData.append(listOfSpeedsToNextNodes.toString()).append("\n\n");

            }
            //export cycles
            //        //call networkVerification to verify network and calculate cycles and fill them with their corresponding nodes
            //        VerifyNetwork networkVerification = new VerifyNetwork();
            //        networkVerification.actionPerformed(null);
            //number of cycles found
            String numberOfCycles = networkVerification.getArezzoCyclesWithIntegerNumbers().size() + "\n";
            //write to output and to file cache
            printTextToOutput(numberOfCycles + "\n\n");
            fileOutputData.append(numberOfCycles).append("\n\n");
            //write the list of cycles
            for (int i = 0; i < networkVerification.getArezzoCyclesWithIntegerNumbers().size(); i++) {
                StringBuilder listOfNodesInACycle = new StringBuilder("[");
                for (int nodeInCycle : networkVerification.getArezzoCyclesWithIntegerNumbers().get(i)) {
                    listOfNodesInACycle.append(nodeInCycle).append(" ");

                }
                int lastSpaceIndexInCycleList = listOfNodesInACycle.lastIndexOf(" ");
                if (lastSpaceIndexInCycleList != -1) {
                    listOfNodesInACycle.replace(lastSpaceIndexInCycleList, lastSpaceIndexInCycleList + 1, "");
                }
                listOfNodesInACycle.append("]");
                printTextToOutput(listOfNodesInACycle + "\n");
                fileOutputData.append(listOfNodesInACycle).append("\n");
            }
            printTextToOutput("\n\n");
            fileOutputData.append("\n\n");
            //exporting number of switch
            String numberOfSwitchs = SwitchRepository.getInstance().getAll().size() + "";
            printTextToOutput(numberOfSwitchs + "\n\n");
            fileOutputData.append(numberOfSwitchs).append("\n\n");

            //exporting switchs configuration
            for (MySwitchWidget widget : SwitchRepository.getInstance().getAll()) {

                String switchNumber = widget.getNumber() + "";
                printTextToOutput(switchNumber + "\n");
                fileOutputData.append(switchNumber).append("\n");
                if (widget.getFirstInputNode() != null) {
                    String firstInputNode = widget.getFirstInputNode().getSwitchInputNodeProperties().getNumber() + "";
                    printTextToOutput(firstInputNode + "\n");
                    fileOutputData.append(firstInputNode).append("\n");
                } else {
                    printTextToOutput("\n");
                    fileOutputData.append("\n");
                }
                //already verified that objects are not null 
                String secondInputNode = widget.getSecondInputNode().getSwitchInputNodeProperties().getNumber() + "";
                printTextToOutput(secondInputNode + "\n");
                fileOutputData.append(secondInputNode).append("\n");
                String firstOutputNode = widget.getFirstOutputNode().getSwitchOutputNodeProperties().getNumber() + "";
                printTextToOutput(firstOutputNode + "\n");
                fileOutputData.append(firstOutputNode).append("\n");
                String secondOutputNumber = widget.getSecondOutputNode().getSwitchOutputNodeProperties().getNumber() + "";
                printTextToOutput(secondOutputNumber + "\n");
                fileOutputData.append(secondOutputNumber).append("\n");
                String firstIntermediateNumber = widget.getFirstIntermediateNode().getSwitchIntermediateNodeProperties().getNumber() + "";
                printTextToOutput(firstIntermediateNumber + "\n");
                fileOutputData.append(firstIntermediateNumber).append("\n");
                String secondIntermediateNumber = widget.getSecondIntermediateNode().getSwitchIntermediateNodeProperties().getNumber() + "";
                printTextToOutput(secondIntermediateNumber + "\n");
                fileOutputData.append(secondIntermediateNumber).append("\n");
                String firstEndStopNumber = widget.getFirstEndStopSensorNode().getStopSensorProperties().getNumber() + "";
                printTextToOutput(firstEndStopNumber + "\n");
                fileOutputData.append(firstEndStopNumber).append("\n");
                String secondEndStopNumber = widget.getSecondEndStopSensorNode().getStopSensorProperties().getNumber() + "";
                printTextToOutput(secondEndStopNumber + "\n");
                fileOutputData.append(secondEndStopNumber).append("\n");
                String firstSensorOutput = widget.getFirstSensorOutputNode().getSensorProperties().getNumber() + "";
                printTextToOutput(firstSensorOutput + "\n");
                fileOutputData.append(firstSensorOutput).append("\n");
                String secondSensorOutput = widget.getSecondSensorOutputNode().getSensorProperties().getNumber() + "";
                printTextToOutput(secondSensorOutput + "\n\n\n");
                fileOutputData.append(secondSensorOutput).append("\n\n\n");

            }
        } else {
            printTextToOutput("####################Network is NOT valid####################\n");
            printTextToOutput("####################cancelling export process at " + (Instant.now()) + "####################\n");
            return;
        }

        //write from cache to file plan_aip_aiguillage_vit.txt at chosen folder
        Path pathToFile1 = Paths.get(folderChosen.getAbsolutePath() + "\\plan_aip_aiguillage_vit.txt");
        //Alert.alert(pathToFile1.toString(), pathToFile1.toString(), Alert.AlertType.INFORMATION_MESSAGE);
        List<String> linesToWrite = new ArrayList<>();
        for (String line : fileOutputData.toString().split("\n")) {
            linesToWrite.add(line);
        }
        fileOutputData = new StringBuilder("");
        try {
            Files.write(pathToFile1, linesToWrite, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        printTextToOutput("####################Exporting to init.txt####################\n");
        for (MySwitchWidget widget : SwitchRepository.getInstance().getAll()) {
            String switchNumber = widget.getNumber() + "";
            printTextToOutput(switchNumber + "\n");
            fileOutputData.append(switchNumber).append("\n");
            printTextToOutput("[");
            fileOutputData.append("[");
            String isSecondInputConnected = widget.isSecondInputConnected() + "";
            printTextToOutput((isSecondInputConnected.equals("false") ? "0" : "1") + " ");
            fileOutputData.append(isSecondInputConnected.equals("false") ? "0" : "1").append(" ");
            String isSecondOutputConnected = widget.isSecondOutputConnected() + "";
            printTextToOutput((isSecondOutputConnected.equals("false") ? "0" : "1") + "]\n\n");
            fileOutputData.append(isSecondOutputConnected.equals("false") ? "0" : "1").append("]\n\n");

        }
        //write from cache to file init.txt at chosen folder
        Path pathToFile2 = Paths.get(folderChosen.getAbsolutePath() + "\\init.txt");
        //Alert.alert(pathToFile1.toString(), pathToFile1.toString(), Alert.AlertType.INFORMATION_MESSAGE);
        linesToWrite = new ArrayList<>();
        linesToWrite.clear();
        for (String line : fileOutputData.toString().split("\n")) {
            linesToWrite.add(line);
        }
        fileOutputData = new StringBuilder("");
        try {
            Files.write(pathToFile2, linesToWrite, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        printTextToOutput("####################Exporting to shuttles.txt####################\n");
        //exporting shuttles configuration
        //exporting number of shuttles
        String numberOfShuttles = ShuttleRepository.getInstance().getAll().size() + "";
        printTextToOutput(numberOfShuttles + "\n\n");
        fileOutputData.append(numberOfShuttles).append("\n\n");
        for (MyShuttleWidget widget : ShuttleRepository.getInstance().getAll()) {
            String shuttleNumber = widget.getParentShuttleProperties().getNumber() + "";
            printTextToOutput(shuttleNumber + "\n");
            fileOutputData.append(shuttleNumber).append("\n");
            String shuttleSize = widget.getParentShuttleProperties().getSizeInDM() + "";
            printTextToOutput(shuttleSize + "\n");
            fileOutputData.append(shuttleSize).append("\n");
            String shuttleStartNode = widget.getParentShuttleProperties().getStartNodeNumber() + "";
            printTextToOutput(shuttleStartNode + "\n");
            fileOutputData.append(shuttleStartNode).append("\n");
            String shuttleEndNode = widget.getShuttleProperties().getEndNodeNumber() + "";
            printTextToOutput(shuttleEndNode + "\n");
            fileOutputData.append(shuttleEndNode).append("\n");
            String shuttleDistanceFromStartNode = widget.getShuttleProperties().getDistanceFromStartingNode() + "";
            printTextToOutput(shuttleDistanceFromStartNode + "\n\n");
            fileOutputData.append(shuttleDistanceFromStartNode).append("\n\n");

        }
        //write from cache to file init.txt at chosen folder
        Path pathToFile3 = Paths.get(folderChosen.getAbsolutePath() + "\\shuttles.txt");
        //Alert.alert(pathToFile1.toString(), pathToFile1.toString(), Alert.AlertType.INFORMATION_MESSAGE);
        linesToWrite = new ArrayList<>();
        linesToWrite.clear();
        for (String line : fileOutputData.toString().split("\n")) {
            linesToWrite.add(line);
        }
        fileOutputData = new StringBuilder("");
        try {
            Files.write(pathToFile3, linesToWrite, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        printTextToOutput("####################Exporting files terminated####################\n");
    }

    /**
     * method that is called to print the text to the output window
     *
     * @param text the text to be printed to the output window
     */
    public void printTextToOutput(String text) {
        output.getOut().print(text);
    }

    /**
     * method that is called to print the error text to the output window
     *
     * @param text the error text to be printed to the output window
     */
    public void printErrorToOutput(String text) {
        output.getErr().print(text);
    }
}
