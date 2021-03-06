/* 
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.domain;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import unoxtutti.UnoXTutti;
import unoxtutti.connection.ClientConnectionException;
import unoxtutti.connection.CommunicationException;
import unoxtutti.connection.MessageReceiver;
import unoxtutti.connection.P2PConnection;
import unoxtutti.connection.P2PMessage;
import unoxtutti.connection.PartnerShutDownException;
import unoxtutti.dialogue.RoomEntranceDialogueHandler;
import unoxtutti.dialogue.RoomEntranceDialogueState;
import unoxtutti.domain.dialogue.DialogueHandler;
import unoxtutti.domain.dialogue.DialogueObserver;
import unoxtutti.utils.DebugHelper;

/**
 * Rappresenta una stanza dal punto di vista del client (per questa ragione
 * "remote": perchè la stanza dal suo punto di vista si trova in remoto). Questo
 * oggetto riceve messaggi di tipo "roomUpdate" per essere notificato di
 * modifiche alla stanza. Inoltre partecipa nei dialoghi di tipo "RoomEntrance"
 * (di cui riceve le notifiche di stato) per gestire l'ingresso del giocatore
 * nella stanza remota da esso rappresentata.
 *
 * @author picardi
 */
public class RemoteRoom extends Room implements MessageReceiver, DialogueObserver {

    private Player myPlayer;
    private P2PConnection p2pConn;
    private DefaultListModel<Player> allPlayers;
    private RoomEntranceDialogueHandler entranceHandler;
    
    private DefaultListModel<String> availableMatches;

    /**
     * Factory method per creare una RemoteRoom. Crea la connessione al server e
     * inizia il dialogo di tipo "RoomEntrance" necessario per completare
     * l'ingresso nella stanza. Registra l'oggetto come Observer dove necessario
     * (presso la connessione P2P per i messaggi "roomUpdate" e presso il
     * DialogueHandler per notifiche sull'evoluzione del dialogo).
     *
     * @param player Il giocatore che vorrebbe collegarsi alla stanza
     * @param roomName Il nome della stanza
     * @param roomAddr L'indirizzo della stanza
     * @param roomPort La porta a cui si trova la stanza
     * @return L'oggetto RemoteRoom corrispondente alla stanza richiesta
     * @throws ClientConnectionException Se la connessione non riesce
     */
    public static RemoteRoom createRemoteRoom(Player player, String roomName, String roomAddr, int roomPort) 
            throws ClientConnectionException {
        InetAddress inetaddr;
        try {
            inetaddr = InetAddress.getByName(roomAddr);
            P2PConnection p2p = P2PConnection.connectToHost(player, inetaddr, roomPort);
            RemoteRoom r = new RemoteRoom(player, roomName, p2p);
            boolean ok = r.enter();
            if (ok) {
                return r;
            }
            return null;
        } catch (IOException ex) {
            throw new ClientConnectionException("Attenzione, l'indirizzo " + roomAddr + " NON è corretto.");
        }
    }

    private RemoteRoom(String name) {
        super(name);
        allPlayers = new DefaultListModel<>();
        availableMatches = new DefaultListModel<>();
    }

    private RemoteRoom(Player pl, String name, P2PConnection p2p) {
        this(name);
        myPlayer = pl;
        p2pConn = p2p;
    }

    /**
     * @return fornisce un ListModel contenente i giocatori presenti nella
     * stanza, per poterli visualizzare in modo che restino aggiornati quando
     * l'elenco cambia.
     */
    public ListModel<Player> getPlayersAsList() {
        return allPlayers;
    }

    private boolean enter() {
        entranceHandler = new RoomEntranceDialogueHandler(p2pConn);
        p2pConn.addMessageReceivedObserver(this, Room.ROOM_UPDATE_MSG);
        entranceHandler.addStateChangeObserver(this);
        return entranceHandler.startDialogue(myPlayer, getName());
    }

    /**
     * @return il giocatore per cui questo oggetto è stato creato.
     */
    public Player getConnectedPlayer() {
        return myPlayer;
    }

    /**
     *
     * @return la P2P connection con cui questa RemoteRoom si collega alla
     * corrispondente ServerRoom
     */
    public P2PConnection getConnection() {
        return p2pConn;
    }

    @Override
    public int getPlayerCount() {
        return allPlayers.getSize();
    }

    @Override
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < getPlayerCount(); i++) {
            players.add(allPlayers.getElementAt(i));
        }
        return players;
    }

    @Override
    public synchronized void updateMessageReceived(P2PMessage msg) {
        if (msg.getName().equals(Room.ROOM_UPDATE_MSG)) {
            DebugHelper.log("Ricevuto aggiornamento della stanza da parte di RoomServer.");
            try {
                /* Aggiornamento lista giocatori */
                ArrayList<Player> players = (ArrayList<Player>) msg.getParameter(0);
                allPlayers.removeAllElements();
                players.forEach((p) -> {
                    allPlayers.addElement(p);
                });
                
                /* Aggiornamento lista partite */
                ArrayList<String> matches = (ArrayList<String>) msg.getParameter(1);
                availableMatches.removeAllElements();
                matches.forEach((m) -> {
                    availableMatches.addElement(m);
                });
            } catch (ClassCastException ex) {
                throw new CommunicationException("Wrong parameter type in message " + msg.getName());
            }
        }
        UnoXTutti.theUxtController.roomUpdated();
    }

    @Override
    public synchronized void updateDialogueStateChanged(DialogueHandler source) {
        if (source.equals(entranceHandler)) {
            RoomEntranceDialogueState state = entranceHandler.getState();
            switch (state) {
                case ADMITTED:
                    for (Player p : entranceHandler.getRemoteRoomPlayers()) {
                        allPlayers.addElement(p);
                    }
                    entranceHandler.concludeDialogue();
                    UnoXTutti.theUxtController.roomEntranceCompleted(this);
                    DebugHelper.log("OK: Richiesta di accesso alla stanza andata a buon fine.");
                    break;
                case REJECTED:
                    entranceHandler.concludeDialogue();
                    UnoXTutti.theUxtController.roomEntranceFailed(this);
                    DebugHelper.log("ERR: C'è stato un problema durante la richiesta di accesso alla stanza.");
                    break;
                default:
            }
        }
    }

    /**
     * Attua l'uscita del giocatore dalla stanza, notificando il server e
     * chiudendo quindi la connessione.
     */
    public void exit() {
        P2PMessage exitMsg = new P2PMessage(Room.ROOM_EXIT_MSG);
        try {
            this.p2pConn.sendMessage(exitMsg);
        } catch (PartnerShutDownException ex) {
            // Non fa nulla
            // Tanto si stava chiudendo in ogni caso
        }
        this.p2pConn.disconnect();
    }
    
    
    /**
     * Restituisce una copia dei nomi delle partite disponibili.
     * @return Lista dei nomi delle partite disponibili.
     */
    @Override
    public ArrayList<String> getAvailableMatches() {
        ArrayList<String> matches = new ArrayList<>();
        for (int i = 0; i < getPlayerCount(); i++) {
            matches.add(availableMatches.getElementAt(i));
        }
        return matches;
    }
    
    /**
     * Ritorna il modello lista delle partite disponibili per
     * l'aggiornamento dell'interfaccia.
     * @return Partite disponibili
     */
    public ListModel<String> getAvailableMatchesAsList() {
        return availableMatches;
    }
}
