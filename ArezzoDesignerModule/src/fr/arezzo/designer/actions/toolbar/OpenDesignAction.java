/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.arezzo.designer.actions.toolbar;

import fr.arezzo.designer.ArezzoWindowTopComponent;
import fr.arezzo.designer.Dialogs.DialogOutputMessages.Alert;
import fr.arezzo.designer.Domain.PropertiesOfNodesOfType3;
import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import fr.arezzo.designer.EditeurModule.Repositories.WorkstationRepository;
import fr.arezzo.designer.Scene.Scene;
import static fr.arezzo.designer.Scene.Scene.ccpShapeNode;
import static fr.arezzo.designer.Scene.Scene.ccpWidget;
import static fr.arezzo.designer.Scene.Scene.mainLayer;
import static fr.arezzo.designer.Scene.Scene.pastePosition;
import fr.arezzo.designer.palette.Shape;
import fr.arezzo.designer.palette.ShapeNode;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import org.netbeans.api.visual.widget.Widget;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "fr.arezzo.designer.actions.toolbar.OpenDesignAction"
)
@ActionRegistration(
        iconBase = "fr/arezzo/designer/resources/Open_design.png",
        displayName = "#CTL_OpenDesignAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 550),
    @ActionReference(path = "Shortcuts", name = "D-O")
})
@Messages("CTL_OpenDesignAction=Open Design")
public final class OpenDesignAction implements ActionListener {
    List<PropertiesOfNodesOfType3> worktationRepositoryTest = new ArrayList<>();
    boolean loadUnload = false;
    ArezzoDesignFile myDesign;
    //folder choser
    JFileChooser folderChooser = new JFileChooser(new File("."));
    File fileChosen = null;
    @Override
    public void actionPerformed(ActionEvent e) {
        if(!loadUnload)
        {
            
            for( MyWorkstationWidget w : WorkstationRepository.getInstance().getAll())
            {
                worktationRepositoryTest.add(w.getParentWorkstationProperties());
            }
            
        }
        if(loadUnload)
        {
            for(PropertiesOfNodesOfType3 pw : worktationRepositoryTest)
            {
                //Alert.alert("shape added 1", "shape added 1", Alert.AlertType.INFORMATION_MESSAGE);
                Scene.ccpShapeNode = new ShapeNode(new Shape(0, "Workstations", "Workstation", WidgetCommonInfo.WidgetType.WORKSTATION,pw.getNumber()+"" ,"fr/arezzo/designer/resources/workstation.png"));
                ccpShapeNode.getShape().setName("Copy of Workstation");
                
                
                MyWorkstationWidget myWw1 = new MyWorkstationWidget(Scene.globalScene, ccpShapeNode);
                Scene.ccpWidget = myWw1;
                
                
                
                
//                Widget w = myWw.getWidget();

//                
//                Scene.globalScene.attachNodeWidget(ccpShapeNode);
                
                //Widget w = Scene.globalScene.addNode(ccpShapeNode);
                Widget w = Scene.globalScene.attachNodeWidget(ccpShapeNode);
                Scene.globalScene.validate();
                Scene.globalScene.repaint();
                Scene.globalScene.addChild(w);
                Scene.globalScene.validate();
                Scene.globalScene.repaint();
                

//                    //initialize the connections of My new Widget
//                    myWw.setConnections(new ArrayList<MyConnectorWidget>());
//                    //assign the generic widget that has to be attached to the scene
//                    widget = myWw.getWidget();
//                    widget.setPreferredLocation(pastePosition);
//                    //reinitialize copied or cutted widget
//                    //ccpWidget = null;
//                    pasted = true;
//                    //map the widget to myWidget to access its properties from the connector widget using the widget key
//                    myWidgetsAdded.put(widget, myWw);
            }
        }
        Scene.globalScene.validate();
        Scene.globalScene.repaint();
        loadUnload = !loadUnload;
        
        
//        //chose folder where to export files
//        folderChooser.setDialogTitle("Chose the design file to open your design");
//        folderChooser.setDialogType(JFileChooser.OPEN_DIALOG);
//        
//        
//        
//        folderChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        if (folderChooser.showOpenDialog(Scene.globalScene.getView()) == JFileChooser.APPROVE_OPTION) 
//        {
//            fileChosen = folderChooser.getSelectedFile().getAbsoluteFile();
//            Path pathToFile ;
//            if(!folderChooser.getSelectedFile().getAbsoluteFile().toString().endsWith(".are"))
//            {
//                pathToFile = Paths.get(fileChosen.getAbsolutePath() + ".are");
//            }
//            else
//            {
//                pathToFile = Paths.get(fileChosen.getAbsolutePath());
//            }
//            //Alert.alert("Chosen path", "Your files will be saved to "+fileChosen.getPath(), Alert.AlertType.INFORMATION_MESSAGE);
//            if(Files.exists(Paths.get(fileChosen.getPath()), LinkOption.NOFOLLOW_LINKS)   )
//            {
//                try {
//                    FileInputStream fis = new FileInputStream(pathToFile.toAbsolutePath().toString());
//                    ObjectInputStream oos;
//                    oos = new ObjectInputStream(fis);
//                    try {
//                        myDesign = (ArezzoDesignFile) oos.readObject();
//                        Scene scene = new Scene();
//                        for(Widget w : myDesign.getMainLayer().getChildren())
//                        {
//                            scene.addChild(w);
//                        }
//                        scene.validate();
//                        scene.repaint();
//                        scene.setConnectionLayer(myDesign.getConnectionLayer());
//                        scene.validate();
//                        scene.repaint();
//                        scene.setInteractionLayer(myDesign.getInteractionLayer());
//                        scene.validate();
//                        scene.repaint();
//                        ArezzoWindowTopComponent.instance.setScene(scene);
//                        scene.validate();
//                        scene.repaint();
//                    } catch (ClassNotFoundException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//                    oos.close();
//                } catch (IOException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//            }
//        } else {
//                
//            
//            
//        }
    }
        
        
}
