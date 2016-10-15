/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.arezzo.designer.actions.toolbar;

import fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.AsynchrnousWaitNoBlockGUIFillAllWorkstationsOperations;
import static fr.arezzo.designer.DomainWidgets.WidgetCommonInfo.latchYesNo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * AddOperationsAction is an action class for managing the add of new operations
 * for the workstations from the menu
 *
 * @author akram.sahbi@esprit.tn
 */
@ActionID(
        category = "Debug",
        id = "fr.arezzo.designer.actions.toolbar.AddOperationsAction"
)
@ActionRegistration(
        iconBase = "fr/arezzo/designer/resources/operations.png",
        displayName = "#CTL_AddOperationsAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 3333),
    @ActionReference(path = "Toolbars/Redo", position = 1150, separatorAfter = 1175) //position = 200)
})
@Messages("CTL_AddOperationsAction=Add operations for workstations")
public final class CreateOperationsAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        //start the process for adding new operations
        new AsynchrnousWaitNoBlockGUIAddWorkstationsOperations();

    }
    
    /**
     * this asynchronous thread contains the filling new operations procedure which
     * will call the dialogs threads and wait for them, initialize the new operations
     * using another thread that runs a dialog box in another thread
     * without blocking the GUI thread
     *
     */
    public static class AsynchrnousWaitNoBlockGUIAddWorkstationsOperations extends Thread {
        /**
         * constructor that resets the latch
         *
         */
        public AsynchrnousWaitNoBlockGUIAddWorkstationsOperations() {
            //reset the latch
            latchYesNo = new CountDownLatch(1);
            this.start();
            
            
        }

        /**
         * invoked when thread.start is called operations to be executed by this
         * global thread is to call the beginning of the operations to fill the
         * properties asynchronously by another thread and wait for him to
         * terminate
         *
         */
        @Override
        public synchronized void run() {
            new AsynchrnousWaitNoBlockGUIFillAllWorkstationsOperations().start();
        }
            
    }
}
