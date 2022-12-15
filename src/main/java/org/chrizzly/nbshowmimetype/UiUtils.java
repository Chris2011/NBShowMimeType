package org.chrizzly.nbshowmimetype;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.annotations.common.CheckForNull;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Chrl
 */
public final class UiUtils {
    private UiUtils() {}

    @CheckForNull
    public static FileObject getFocusedFileObject() {
        return getFocusedFileObject(false);
    }

    @CheckForNull
    private static FileObject getFocusedFileObject(boolean last) {
        JTextComponent component;

        if (last) {
            component = EditorRegistry.lastFocusedComponent();
        } else {
            component = EditorRegistry.focusedComponent();
        }

        if (component == null) {
            return null;
        }

        Document document = component.getDocument();

        if (document == null) {
            return null;
        }

        return NbEditorUtilities.getFileObject(document);
    }
}