/* 
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.gui;

import javax.swing.JOptionPane;
import unoxtutti.domain.Card;
import unoxtutti.utils.GUIUtils;

public class ScegliColoreJollyDialog extends javax.swing.JDialog {
    /**
     * Risposta dell'utente alla domanda.
     */
    private int result;

    /**
     * Creates new form ScegliColoreJollyDialog
     * @param parent
     * @param modal
     */
    public ScegliColoreJollyDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        result = Card.COLORE_NESSUNO;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        redButton = new javax.swing.JButton();
        greenButton = new javax.swing.JButton();
        yellowButton = new javax.swing.JButton();
        blueButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Scegli colore Jolly");
        setMinimumSize(new java.awt.Dimension(300, 125));
        setPreferredSize(new java.awt.Dimension(300, 125));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 10, 0};
        layout.rowHeights = new int[] {0, 10, 0};
        getContentPane().setLayout(layout);

        redButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unoxtutti/resources/icons/square-red.png"))); // NOI18N
        redButton.setText("Rosso");
        redButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(redButton, gridBagConstraints);

        greenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unoxtutti/resources/icons/square-green.png"))); // NOI18N
        greenButton.setText("Verde");
        greenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                greenButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(greenButton, gridBagConstraints);

        yellowButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unoxtutti/resources/icons/square-yellow.png"))); // NOI18N
        yellowButton.setText("Giallo");
        yellowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yellowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(yellowButton, gridBagConstraints);

        blueButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unoxtutti/resources/icons/square-blue.png"))); // NOI18N
        blueButton.setText("Blue");
        blueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blueButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(blueButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    private void redButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redButtonActionPerformed
        result = Card.COLORE_ROSSO;
        setVisible(false);
    }//GEN-LAST:event_redButtonActionPerformed

    private void greenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_greenButtonActionPerformed
        result = Card.COLORE_VERDE;
        setVisible(false);
    }//GEN-LAST:event_greenButtonActionPerformed

    private void yellowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yellowButtonActionPerformed
        result = Card.COLORE_GIALLO;
        setVisible(false);
    }//GEN-LAST:event_yellowButtonActionPerformed

    private void blueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blueButtonActionPerformed
        result = Card.COLORE_BLU;
        setVisible(false);
    }//GEN-LAST:event_blueButtonActionPerformed
    
    public int getResult() {
        return result;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton blueButton;
    private javax.swing.JButton greenButton;
    private javax.swing.JButton redButton;
    private javax.swing.JButton yellowButton;
    // End of variables declaration//GEN-END:variables
}
