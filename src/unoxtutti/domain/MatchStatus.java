/*
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.domain;

import java.io.Serializable;
import java.util.ArrayList;
import unoxtutti.utils.DebugHelper;
import unoxtutti.utils.TimeUtils;

/**
 * Contiene informazioni riguardanti lo stato di una partita.
 * @author Davide
 */
public class MatchStatus implements Serializable {
    /* Messaggio di aggiornamento */
    public static final String STATUS_UPDATE_MSG = "statusUpdate";
    public static final String STATUS_PLAY_CARD_MSG = "statusPlayCard";
    public static final String STATUS_PICK_CARD_MSG = "statusPickCard";
    public static final String STATUS_CHECK_BLUFF_MSG = "statusCheckBluff";
    public static final String STATUS_DECLARE_UNO_MSG = "statusDeclareUno";
    
    /**
     * Possibili direzioni di turno
     */
    protected static final int DIRECTION_FORWARD = 1;
    protected static final int DIRECTION_BACKWARD = 0;
    
    /**
     * Lista dei giocatori in ordine di turno.
     */
    protected ArrayList<Player> turns;
    
    /**
     * Indica la direzione dei turni.
     * <code>true</code> significa avanti,
     * <code>false</code> significa indietro.
     */
    protected int turnsDirection = MatchStatus.DIRECTION_FORWARD;
    
    /**
     * Giocatore a cui spetta il turno.
     */
    protected Player currentPlayer;
    
    /**
     * È l'ultima carta che è stata scartata.
     */
    protected Card cartaMazzoScarti;
    
    /**
     * Descrizione degli eventi accaduti durante la partita.
     */
    protected ArrayList<String> events;
    
    /**
     * Inizializza la lista di eventi
     */
    protected MatchStatus() {
        events = new ArrayList<>(100);
    }
    
    /**
     * Restituisce la direzione dei turni.
     * @return Direzione dei turni.
     */
    public int getDirection() {
        return turnsDirection;
    }
    
    /**
     * Inverte la direzionedei turni
     */
    protected void changeDirection() {
        switch(turnsDirection) {
            case DIRECTION_FORWARD:
                turnsDirection = MatchStatus.DIRECTION_BACKWARD;
                break;
            case DIRECTION_BACKWARD:
                turnsDirection = MatchStatus.DIRECTION_FORWARD;
                break;
        }
    }
    
    
    /**
     * Crea una copia dell'istanza corrente.
     * @return Stato della partita
     */
    protected MatchStatus creaCopia() {
        MatchStatus upd = new MatchStatus();
        upd.turns = new ArrayList<>(turns);
        upd.currentPlayer = currentPlayer;
        upd.turnsDirection = turnsDirection;
        upd.cartaMazzoScarti = cartaMazzoScarti;
        upd.events = events;
        return upd;
    }
    
    
    /**
     * Traccia un evento, aggiunge un prefisso con l'ora dell'evento.
     * @param eventMessage Descrizione dell'evento.
     */
    protected void trackEvent(String eventMessage) {
        String prefix = "[" + TimeUtils.getCurrentTimeStamp("HH:mm:ss") + "] ";
        events.add(prefix + eventMessage);
        DebugHelper.log("EVENTO PARTITA: " + eventMessage);
    }
    
    
    /**
     * Indica se un giocatore può scartare una determinata carta.
     * @param player Giocatore che desidererebbe effettuare l'azione.
     * @param card Carta che il giocatore vorrebbe scartare.
     * @return <code>true</code> se il giocatore potrebbe scartare una determinata carta,
     *          <code>false</code> altrimenti.
     */
    public boolean canPlayerPlayCard(Player player, Card card) {
        // TODO: Fare funzione
        return true;
        /*
        return card.getColore() == cartaMazzoScarti.getColore() ||
                card.getDettaglio() == cartaMazzoScarti.getDettaglio() ||
                card.getTipo() == Card.CARTA_JOLLY;
        */
    }
}