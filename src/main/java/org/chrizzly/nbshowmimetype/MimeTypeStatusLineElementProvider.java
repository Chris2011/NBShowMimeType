package org.chrizzly.nbshowmimetype;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.awt.StatusLineElementProvider;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Chrizzly
 */
@ServiceProvider(service = StatusLineElementProvider.class)
public class MimeTypeStatusLineElementProvider implements StatusLineElementProvider {
    private static final JLabel MIME_TYPE_LABEL = new MimeTypeLabel();
    private static final Component COMPONENT = panelWithSeparator(MIME_TYPE_LABEL);

    static {
        // add listeners
        EditorRegistry.addPropertyChangeListener((PropertyChangeListener) MIME_TYPE_LABEL);
    }

    @Override
    public Component getStatusLineElement() {
        return COMPONENT;
    }

    /**
     * Create Component(JPanel) and add separator and JLabel to it.
     *
     * @param label JLabel
     * @return panel
     */
    private static Component panelWithSeparator(JLabel label) {
        // create separator
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL) {
            private static final long serialVersionUID = -6385848933295984637L;

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(3, 3);
            }
        };

        separator.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        // create panel
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(separator, BorderLayout.WEST);
        panel.add(label, BorderLayout.EAST);

        return panel;
    }

    private static class MimeTypeLabel extends JLabel implements PropertyChangeListener {
        private static final long serialVersionUID = 7553842743917776222L;

        public MimeTypeLabel() {
            super.setPreferredSize(null);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateMimeType();
        }

        private void updateMimeType() {
            assert EventQueue.isDispatchThread();
            FileObject fileObject = UiUtils.getFocusedFileObject();
            Dimension size = this.getSize();

            this.setPreferredSize(null);

            if (fileObject == null) {
                this.setPreferredSize(size);
                this.setText(""); // NOI18N

                return;
            }

            String mimeType = getMimeType(fileObject);

            this.setText(String.format(" %s ", mimeType)); // NOI18N
        }

        private String getMimeType(FileObject fileObject) {
            assert fileObject != null;

            String mimeType = fileObject.getMIMEType();

            if (mimeType == null) {
                mimeType = "unknown";
            }

            return mimeType;
        }
    }
}