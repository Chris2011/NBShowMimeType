/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chrizzly.nbshowmimetype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import java.util.logging.Logger;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.RequestProcessor;

@ActionID(
        category = "Bugtracking",
        id = "org.chrizzly.nbshowmimetype.ChangeMimeType"
)
@ActionRegistration(
        iconBase = "org/chrizzly/nbshowmimetype/todo.png",
        displayName = "#CTL_ChangeMimeType"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = -100),
    @ActionReference(path = "Toolbars/File", position = 0)
})
@Messages("CTL_ChangeMimeType=Mime ändern")
public final class ChangeMimeType implements ActionListener {
    private static final RequestProcessor RP = new RequestProcessor(ChangeMimeType.class);
    private static final Logger LOGGER = Logger.getLogger(ChangeMimeType.class.getName());

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            FileObject fileObject = UiUtils.getFocusedFileObject();

            JOptionPane.showMessageDialog(null, fileObject.getMIMEType());
            DataObject dataObject = DataObject.find(fileObject);

            this.reopen(dataObject, fileObject, 0);
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void reopen(DataObject dataObject, FileObject fileObject, int caretPosition) {
        RP.schedule(() -> {
            try {
                dataObject.setValid(false);
                org.netbeans.modules.csl.api.UiUtils.open(fileObject, caretPosition);
                fileObject.setAttribute("mimeType", "text/x-java");
            } catch (PropertyVetoException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }, 100, TimeUnit.MILLISECONDS);
    }
}
