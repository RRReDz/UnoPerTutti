/* 
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.gui;

import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Rectangle;
import javax.swing.JOptionPane;
import unoxtutti.UnoXTutti;

/**
 *
 * @author picardi
 */
public class UnoXTuttiGUI extends javax.swing.JFrame {

    private GUIState guiState;
    private Cursor cursor;

    public enum GUIState {
        OUTSIDE_ROOM("outside room", new OutsideRoomPanel()),
        INSIDE_ROOM("inside room", new InsideRoomPanel());

        protected final String name;
        protected final MainWindowSubPanel panel;

        private GUIState(String s, MainWindowSubPanel p) {
            name = s;
            panel = p;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the panel
         */
        public MainWindowSubPanel getPanel() {
            return panel;
        }

    }

    /**
     * Creates new form UnoXTuttiMainWindow
     */
    public UnoXTuttiGUI() {
        initComponents();
        this.userNameLabel.setText("Giocatore: " + UnoXTutti.theUxtController.getPlayer().getName());
        for (GUIState s : GUIState.values()) {
            s.getPanel().setMainWindow(this);
            mainPanel.add(s.getPanel(), s.getName());
        }
        setGuiState(GUIState.OUTSIDE_ROOM);

    }

    public void setWaiting(boolean waiting) {
        if (waiting) {
            if (cursor == null) {
                cursor = this.getCursor();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            }
        } else if (cursor != null) {
            this.setCursor(cursor);
            cursor = null;
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public final void setGuiState(GUIState s) {
        guiState = s;
        guiState.getPanel().initializeContent();
        ((CardLayout) this.mainPanel.getLayout()).show(mainPanel, guiState.getName());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        userNameLabel = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        userNameLabel.setBackground(new java.awt.Color(255, 255, 255));
        userNameLabel.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        userNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userNameLabel.setText("Giocatore: ");
        userNameLabel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        userNameLabel.setOpaque(true);
        getContentPane().add(userNameLabel, java.awt.BorderLayout.PAGE_START);

        mainPanel.setLayout(new java.awt.CardLayout());
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        UnoXTutti.theUxtController.esciDalGioco();
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel userNameLabel;
    // End of variables declaration//GEN-END:variables
}
