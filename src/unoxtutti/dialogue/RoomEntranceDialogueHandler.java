/* 
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.dialogue;

import java.util.ArrayList;
import unoxtutti.connection.P2PConnection;
import unoxtutti.connection.P2PMessage;
import unoxtutti.connection.PartnerShutDownException;
import unoxtutti.domain.Player;
import unoxtutti.domain.Room;
import unoxtutti.domain.dialogue.BasicDialogueHandler;

/**
 * Questa classe gestisce un dialogo di ingresso in una stanza. Tale dialogo è
 * costituito da una richiesta di ingresso e dall'attesa di una risposta
 * (positiva o negativa). La richiesta ("roomEntranceRequestMsg") ha come
 * parametri il nome della stanza (a mò di verifica) e il giocatore che vuole
 * entrare. La risposta ("roomEntranceReplyMsg") ha come unico parametro la
 * risposta (true o false, accettata o rifiutata) alla richiesta.
 *
 * @author picardi
 */
public class RoomEntranceDialogueHandler extends BasicDialogueHandler<RoomEntranceDialogueState> {

    private final P2PConnection p2pConn;

    /**
     * Crea un handler per una richiesta di ingresso (ci deve essere un handler
     * diverso per ogni richiesta). L'handler è inizialmente in stato
     * BEFORE_REQUEST.
     *
     * @param p2p La connessione su cui i messaggi viaggiano
     */
    public RoomEntranceDialogueHandler(P2PConnection p2p) {
        super(RoomEntranceDialogueState.BEFORE_REQUEST);
        p2pConn = p2p;
    }

    /**
     * Inizia il dialogo richiedendo l'ingresso e passando allo stato REQUESTED.
     * Se l'invio non funziona torna allo stato BEFORE_REQUEST.
     *
     * @param pl Il giocatore che vuole entrare nella stanza
     * @param roomName Il nome della stanza in cui entrare
     * @return true se la richiesta è partita correttamente, false se c'è stato
     * un problema di comunicazione.
     */
    public boolean startDialogue(Player pl, String roomName) {
        boolean ret = true;
        p2pConn.addMessageReceivedObserver(this, Room.ROOM_ENTRANCE_REPLY_MSG);
        P2PMessage msg = new P2PMessage(Room.ROOM_ENTRANCE_REQUEST_MSG);
        Object[] pars = new Object[]{roomName, pl};
        msg.setParameters(pars);
        this.setState(RoomEntranceDialogueState.REQUESTED);
        try {
            p2pConn.sendMessage(msg);
        } catch (PartnerShutDownException ex) {
            this.setState(RoomEntranceDialogueState.BEFORE_REQUEST);
            ret = false;
        }
        return ret;
    }

    /**
     * Dichiara il dialogo terminato. L'oggetto smette di essere registrato per
     * ricevere messaggi presso la connessione P2P
     */
    public void concludeDialogue() {
        p2pConn.removeMessageReceivedObserver(this, Room.ROOM_ENTRANCE_REPLY_MSG);
    }

    /**
     *
     * @return Se l'ingresso nella stanza è completato correttamente,
     * restituisce l'elenco dei giocatori ivi presenti al momento dell'ingresso.
     * Altrimenti restituisce un elenco vuoto.
     */
    public ArrayList<Player> getRemoteRoomPlayers() {
        if (this.getState().equals(RoomEntranceDialogueState.ADMITTED)) {
            return (ArrayList<Player>) this.getStateChangeTrigger().getParameter(1);
        }
        return new ArrayList<>();
    }
}
