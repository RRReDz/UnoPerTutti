/* 
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.gui;

import javax.swing.JOptionPane;


public class NuovaPartitaDialog extends javax.swing.JDialog {

    private int result;

    /**
     * Creates new form NuovaPartitaDialog
     */
    public NuovaPartitaDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        result = JOptionPane.CANCEL_OPTION;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel4 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        txtNomePartita = new javax.swing.JTextField();
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Creazione partita");
        setPreferredSize(new java.awt.Dimension(300, 180));

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(350, 100));

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("Nome partita:");
        jPanel1.add(jLabel1);

        txtNomePartita.setColumns(20);
        txtNomePartita.setAlignmentX(0.0F);
        jPanel1.add(txtNomePartita);

        jPanel2.add(jPanel1);

        jPanel4.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unoxtutti/resources/icons/check-green.png"))); // NOI18N
        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jPanel3.add(okButton);

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unoxtutti/resources/icons/cross-red.png"))); // NOI18N
        cancelButton.setText("Annulla");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel3.add(cancelButton);

        jPanel4.add(jPanel3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Controllo validità dati al click del tasto OK.
     * @param evt 
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        result = JOptionPane.OK_OPTION;
        boolean ok = true;
        
        if (txtNomePartita.getText().isEmpty()) {
            ok = false;
            JOptionPane.showMessageDialog(this, "Inserire il nome della partita!");
            txtNomePartita.requestFocus();
        }
        
        if (ok) {
            setVisible(false);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        result = JOptionPane.CANCEL_OPTION;
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    public String getMatchName() {
        return this.txtNomePartita.getText().trim();
    }

    public int getResult() {
        return result;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField txtNomePartita;
    // End of variables declaration//GEN-END:variables
}