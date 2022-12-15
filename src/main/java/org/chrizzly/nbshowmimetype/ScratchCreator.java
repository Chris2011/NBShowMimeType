package org.chrizzly.nbshowmimetype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "antlrold.ScratchCreator"
)
@ActionRegistration(
        displayName = "#CTL_ScratchCreator"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 0),
    @ActionReference(path = "Shortcuts", name = "DOS-N")
})
@Messages("CTL_ScratchCreator=New Scratch")
public class ScratchCreator implements ActionListener {

    private static final AtomicInteger _integer = new AtomicInteger(0);

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            FileSystem fs = FileUtil.createMemoryFileSystem();
            FileObject fob = fs.getRoot().createData("Untitled" + getNextCount());

            fob.setAttribute("mimeType", "text/x-json");

            DataObject data = DataObject.find(fob);
            OpenCookie cookie = (OpenCookie) data.getCookie(OpenCookie.class);

            cookie.open();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static int getNextCount() {
        return _integer.incrementAndGet();
    }
}
