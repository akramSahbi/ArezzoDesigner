/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.arezzo.designer.actions.toolbar;

import fr.arezzo.designer.Dialogs.DialogOutputMessages.Alert;
import fr.arezzo.designer.Scene.Scene;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "fr.arezzo.designer.actions.toolbar.SaveDesignAction"
)
@ActionRegistration(
        iconBase = "fr/arezzo/designer/actions/toolbar/file_save.png",
        displayName = "#CTL_SaveDesignAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1550),
    @ActionReference(path = "Toolbars/File", position = 0),
    @ActionReference(path = "Shortcuts", name = "D-S")
})
@Messages("CTL_SaveDesignAction=Save Design")
public final class SaveDesignAction implements ActionListener {

    ArezzoDesignFile myDesign;
    //folder choser
    JFileChooser folderChooser = new JFileChooser(new File("."));
    File fileChosen = null;
    @Override
    public void actionPerformed(ActionEvent e) {
        myDesign = new ArezzoDesignFile(Scene.mainLayer, Scene.connectionLayer,Scene.interactionLayer );
        
        //chose folder where to export files
        folderChooser.setDialogTitle("Chose the filer where do you want to export your design");
        folderChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        
        
        
        folderChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (folderChooser.showSaveDialog(Scene.globalScene.getView()) == JFileChooser.APPROVE_OPTION) 
        {
            fileChosen = folderChooser.getSelectedFile().getAbsoluteFile();
            Path pathToFile ;
            if(!folderChooser.getSelectedFile().getAbsoluteFile().toString().endsWith(".are"))
            {
                pathToFile = Paths.get(fileChosen.getAbsolutePath() + ".are");
            }
            else
            {
                pathToFile = Paths.get(fileChosen.getAbsolutePath());
            }
            Alert.alert("Chosen path", "Your files will be saved to "+fileChosen.getPath(), Alert.AlertType.INFORMATION_MESSAGE);
            if(Files.notExists(Paths.get(fileChosen.getPath()), LinkOption.NOFOLLOW_LINKS)   )
            {
                try 
                {
                    Files.createFile(Paths.get(fileChosen.getPath()));
                    try {
                        FileOutputStream fos = new FileOutputStream(pathToFile.toAbsolutePath().toString());
                        ObjectOutputStream oos;
                        oos = new ObjectOutputStream(fos);
                        oos.writeObject(myDesign);
                        oos.close();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
        }
                } catch (IOException ex) 
                {
                    Exceptions.printStackTrace(ex);
                }
            }
        } else {
                
            
            
        }
        
    }
}
