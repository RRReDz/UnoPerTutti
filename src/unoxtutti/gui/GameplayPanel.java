/*
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.gui;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.DefaultListModel;
import unoxtutti.GiocarePartitaController;
import unoxtutti.UnoXTutti;
import unoxtutti.domain.Card;
import unoxtutti.domain.MatchStatus;
import unoxtutti.domain.Player;
import unoxtutti.domain.RemoteMatch;
import unoxtutti.utils.GUIUtils;

/**
 *
 * @author Davide
 */
public class GameplayPanel extends MainWindowSubPanel {

    private RemoteMatch remoteMatch;
    
    /**
     * Creates new form GameplayPanel
     */
    public GameplayPanel() {
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

        jButton2 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        turnsListPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        turnsList = new javax.swing.JList<>();
        mainPanel = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        eventPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        eventList = new javax.swing.JList<>();
        cardsPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        cardsList = new javax.swing.JList<>();
        footerPanel = new javax.swing.JPanel();
        playCardButton = new javax.swing.JButton();
        pickCardButton = new javax.swing.JButton();
        checkBluffButton = new javax.swing.JButton();
        declareUNOButton = new javax.swing.JButton();
        checkUnoDeclarationButton = new javax.swing.JButton();

        jButton2.setText("jButton2");

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(150);

        turnsListPanel.setLayout(new java.awt.BorderLayout());

        turnsList.setModel(new DefaultListModel());
        turnsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        turnsList.setEnabled(false);
        jScrollPane1.setViewportView(turnsList);

        turnsListPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(turnsListPanel);

        mainPanel.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setDividerLocation(400);

        eventPanel.setLayout(new java.awt.BorderLayout());

        eventList.setModel(new DefaultListModel());
        eventList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(eventList);

        eventPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane2.setLeftComponent(eventPanel);

        cardsPanel.setLayout(new java.awt.BorderLayout());

        cardsList.setModel(new DefaultListModel());
        cardsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(cardsList);

        cardsPanel.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jSplitPane2.setRightComponent(cardsPanel);

        mainPanel.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(mainPanel);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);

        playCardButton.setText("Scarta carta");
        playCardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playCardButtonActionPerformed(evt);
            }
        });
        footerPanel.add(playCardButton);

        pickCardButton.setText("Pesca una carta");
        pickCardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pickCardButtonActionPerformed(evt);
            }
        });
        footerPanel.add(pickCardButton);

        checkBluffButton.setText("Dubita bluff");
        checkBluffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBluffButtonActionPerformed(evt);
            }
        });
        footerPanel.add(checkBluffButton);

        declareUNOButton.setText("Dichiara UNO!");
        declareUNOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declareUNOButtonActionPerformed(evt);
            }
        });
        footerPanel.add(declareUNOButton);

        checkUnoDeclarationButton.setText("Verifica UNO!");
        checkUnoDeclarationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkUnoDeclarationButtonActionPerformed(evt);
            }
        });
        footerPanel.add(checkUnoDeclarationButton);

        add(footerPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    
    
    /**
     * L'utente desidera scartare una carta.
     * @param evt 
     */
    private void playCardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playCardButtonActionPerformed
        Card card = (Card) cardsList.getSelectedValue();
        if (card == null) {
            GUIUtils.showErrorMessage(mainWindow, "Non è stata selezionata alcuna carta!");
            return;
        }
        
        try {
            remoteMatch = GiocarePartitaController.getInstance().getCurrentMatch();
            remoteMatch.playCard(card);
        } catch(Exception e) {
            GUIUtils.showException(e, mainWindow);
        }
    }//GEN-LAST:event_playCardButtonActionPerformed

    
    /**
     * L'utente desidera pescare una carta.
     * @param evt 
     */
    private void pickCardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pickCardButtonActionPerformed
        try {
            remoteMatch = GiocarePartitaController.getInstance().getCurrentMatch();
            remoteMatch.pickCard();
        } catch(Exception e) {
            GUIUtils.showException(e, mainWindow);
        }
    }//GEN-LAST:event_pickCardButtonActionPerformed

    private void checkBluffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBluffButtonActionPerformed
        try {
            remoteMatch = GiocarePartitaController.getInstance().getCurrentMatch();
            remoteMatch.checkBluff();
        } catch(Exception e) {
            GUIUtils.showException(e, mainWindow);
        }
    }//GEN-LAST:event_checkBluffButtonActionPerformed

    private void declareUNOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declareUNOButtonActionPerformed
        try {
            remoteMatch = GiocarePartitaController.getInstance().getCurrentMatch();
            remoteMatch.declareUNO();
        } catch(Exception e) {
            GUIUtils.showException(e, mainWindow);
        }
    }//GEN-LAST:event_declareUNOButtonActionPerformed

    private void checkUnoDeclarationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkUnoDeclarationButtonActionPerformed
        try {
            remoteMatch = GiocarePartitaController.getInstance().getCurrentMatch();
            remoteMatch.checkUNODeclaration();
        } catch(Exception e) {
            GUIUtils.showException(e, mainWindow);
        }
    }//GEN-LAST:event_checkUnoDeclarationButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<Card> cardsList;
    private javax.swing.JPanel cardsPanel;
    private javax.swing.JButton checkBluffButton;
    private javax.swing.JButton checkUnoDeclarationButton;
    private javax.swing.JButton declareUNOButton;
    private javax.swing.JList<String> eventList;
    private javax.swing.JPanel eventPanel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton pickCardButton;
    private javax.swing.JButton playCardButton;
    private javax.swing.JList<String> turnsList;
    private javax.swing.JPanel turnsListPanel;
    // End of variables declaration//GEN-END:variables

    
    /**
     * Aggiorna la lista dei turni.
     * @param status Stato della partita
     */
    public void updateTurns(MatchStatus status) {
        DefaultListModel model = (DefaultListModel) turnsList.getModel();
        model.clear();
        
        ArrayList<Player> turns = status.getTurns();
        turns.forEach((p) -> {
            model.addElement(p.getName());
        });
    }

    /**
     * Aggiorna la lista delle carte e la carta del mazzo scarti.
     * @param mano Carte possedute dal giocatore
     * @param status Stato della partita
     */
    public void updateCards(Collection<Card> mano, MatchStatus status) {
        DefaultListModel model = (DefaultListModel) cardsList.getModel();
        model.clear();
        mano.forEach((c) -> {
            model.addElement(c);
        });
        
        Player currentPlayer = status.getCurrentPlayer();
        boolean isItMyTurn = currentPlayer.equals(UnoXTutti.theUxtController.getPlayer());
        Card cartaMazzoScarti = status.getCartaMazzoScarti();
        
        boolean resetButton = false;
        if(isItMyTurn) {
            pickCardButton.setEnabled(true);
            
            /**
             * Se è il turno del giocatore corrente, controllo se deve
             * pescare più di una carta.
             */
            if(status.getCardsToPick() == 1) {
                /* Il giocatore non è afflitto da penalità */
                resetButton = true;
            } else if(cartaMazzoScarti.isJollyPescaQuattro()) {
                /* Se si tratta di un Jolly Pesca Quattro, si può verificare il bluff */
                checkBluffButton.setEnabled(true);
            }
            
            if(!resetButton) {
                /* Il giocatore è afflitto da penalità */
                pickCardButton.setText("Pesca " + status.getCardsToPick() + " carte");
            }
        } else {
            /* Non si possono pescare carte nei turni altrui */
            pickCardButton.setEnabled(false);
            resetButton = true;
        }
        
        /* Il giocatore non è afflitto da penalità */
        if(resetButton) {
            checkBluffButton.setEnabled(false);
            pickCardButton.setText("Pesca una carta");
        }
        
        /* Se il giocatore corrente ha una carta, può dichiarare UNO! */
        if(mano.size() == 1) {
            declareUNOButton.setEnabled(true);
        } else {
            declareUNOButton.setEnabled(false);
        }
    }

    /**
     * Aggiorna la lista degli eventi
     * @param status Stato della partita
     */
    public void updateEvents(MatchStatus status) {
        DefaultListModel model = (DefaultListModel) eventList.getModel();
        Collection<String> events = status.getEvents();
        if(events.getClass() != ArrayList.class) {
            /* Refresh completo lista */
            model.clear();
            events.forEach((c) -> {
                model.addElement(c);
            });
        } else {
            /* Append dei nuovi elementi */
            ArrayList<String> list = (ArrayList<String>) events;
            for(int i = model.size(); i < list.size(); i++) {
                model.addElement(list.get(i));
            }
        }
    }
}
