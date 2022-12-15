package org.chrizzly.nbshowmimetype;

import javax.swing.JOptionPane;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.editor.document.OnSaveTask;
import org.openide.filesystems.FileObject;
import org.openide.text.Line;

public class OnInMemoryFileSave implements OnSaveTask {

    private final OnSaveTask.Context context;

    private OnInMemoryFileSave(OnSaveTask.Context context) {
        this.context = context;
    }

    @Override
    public void performTask() {
        JOptionPane.showMessageDialog(null, "Save to disk");
    }

    @Override
    public void runLocked(Runnable r) {
//        r.run();
        Document document = context.getDocument();
        FileObject fileObject = NbEditorUtilities.getFileObject(document);
        
        JOptionPane.showMessageDialog(null, fileObject.getName());
        JOptionPane.showMessageDialog(null, fileObject.getPath());
        JOptionPane.showMessageDialog(null, document.getDefaultRootElement().getName());

        if (!fileObject.getExt().isEmpty()) {
            return;
        }

        JTextComponent editor = EditorRegistry.lastFocusedComponent();
        if (editor == null) {
            return;
        }

        int caretPosition = editor.getCaretPosition();

        Line line = NbEditorUtilities.getLine(document, 0, false);
        String text = line.getText();
//        if (!ShebangUtils.isShebang(text) && !NoMIMEResolverUtils.isCommentLine(text)) {
//            return;
//        }
    }

    @Override
    public boolean cancel() {
        return true;
    }

    @MimeRegistration(mimeType = "", service = OnSaveTask.Factory.class, position = Integer.MAX_VALUE)
    public static final class FactoryImpl implements OnSaveTask.Factory {

        @Override
        public OnSaveTask createTask(OnSaveTask.Context context) {
            return new OnInMemoryFileSave(context);
        }
    }
}
