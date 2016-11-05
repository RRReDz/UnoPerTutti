/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.gui;

import unoxtutti.utils.TimeUtils;

/**
 *
 * @author Riccardo Rossi
 */
public class DebugFrame extends javax.swing.JFrame {

    /**
     * Creates new form DebugFrame
     */
    public DebugFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        logText = new javax.swing.JTextArea();
        resetLogButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(650, 450));
        setResizable(false);

        logText.setColumns(20);
        logText.setRows(5);
        logText.setEnabled(false);
        jScrollPane1.setViewportView(logText);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        resetLogButton.setText("Reset");
        resetLogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetLogButtonActionPerformed(evt);
            }
        });
        getContentPane().add(resetLogButton, java.awt.BorderLayout.PAGE_END);

        setBounds(790, 0, 580, 450);
    }// </editor-fold>//GEN-END:initComponents

    private void resetLogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetLogButtonActionPerformed
        logText.setText("");
    }//GEN-LAST:event_resetLogButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DebugFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DebugFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DebugFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DebugFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DebugFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logText;
    private javax.swing.JButton resetLogButton;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Scrive nella jTextArea il messaggio e la data attuale.
     * Eventualmente aggiunge il carattere per andare a capo
     * se non fosse presente.
     * @param line 
     */
    public void writeToTextArea(String line) {
        String prefix = "[" + TimeUtils.getCurrentTimeStamp("HH:mm:ss") + "] ";
        logText.append(prefix);
        logText.append(line.endsWith("\n") ? line : line + "\n");
    }

}
