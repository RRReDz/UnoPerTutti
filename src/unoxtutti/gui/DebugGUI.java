/* 
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.gui;

import java.awt.Toolkit;
import unoxtutti.configuration.DebugConfig;
import unoxtutti.utils.TimeUtils;

/**
 *
 * @author Riccardo Rossi
 */
public class DebugGUI extends javax.swing.JFrame {

    /**
     * Creates new form DebugFrame
     */
    public DebugGUI() {
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
        resetLogButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(DebugConfig.CONSOLE_TITLE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("../resources/icons/terminal.png")));
        setResizable(false);

        logText.setEditable(false);
        logText.setColumns(20);
        logText.setRows(5);
        jScrollPane1.setViewportView(logText);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        resetLogButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unoxtutti/resources/icons/trash.png"))); // NOI18N
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private final javax.swing.JTextArea logText = new javax.swing.JTextArea();
    private javax.swing.JButton resetLogButton;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Scrive nella jTextArea il messaggio e la data attuale.
     * Eventualmente aggiunge il carattere per andare a capo
     * se non fosse presente.
     * @param line 
     */
    public void appendMessageToConsole(String line) {
        synchronized(logText) {
            String prefix = "[" + TimeUtils.getCurrentTimeStamp("HH:mm:ss") + "] ";
            logText.append(prefix);
            logText.append(line.endsWith("\n") ? line : line + "\n");
            System.out.println(prefix + line);
        }
    }

}
