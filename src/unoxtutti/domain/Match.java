/* 
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.domain;

/**
 * Rappresentazione astratta di una Partita.
 * @author Davide
 */
public abstract class Match {
    /* Creazione della stanza */
    public static final String MATCH_CREATION_REQUEST_MSG = "matchCreationRequest";
    public static final String MATCH_CREATION_REPLY_MSG = "matchCreationReply";
    
    /* Richieste di accesso alla partita */
    public static final String MATCH_ACCESS_REQUEST_MSG = "matchAccessRequest";
    public static final String MATCH_ACCESS_REQUEST_REPLY_MSG = "matchAccessRequestReply";
    public static final String MATCH_ACCESS_SUCCESS_NOTIFICATION_MSG = "matchAccessSuccessNotification";
    
    /*  Aggiornamento della lista dei giocatori */
    public static final String MATCH_UPDATE_MSG = "matchUpdate";
    
    /* Uscita dalla partita da parte di un giocatore */
    public static final String MATCH_EXIT_MSG = "matchExit";
    public static final String MATCH_EXIT_REPLY_MSG ="matchExitReply";
    
    /* Chiusura della partita */
    public static final String MATCH_CLOSING_MSG = "matchClosing";
    public static final String MATCH_CLOSING_REPLY_MSG = "matchReplyClosing";
    public static final String MATCH_CLOSED_MSG = "matchClosed"; //Avviso altri utenti in stanza
    
    /* Richiesta di avvio */
    public static final String MATCH_STARTING_MSG = "matchStarting";
    public static final String MATCH_STARTING_REPLY_MSG = "matchStartingReply";
    public static final String MATCH_STARTED_MSG = "matchStarted"; // Per gli altri utenti in stanza
    
    /* Notifica di fine partita */
    public static final String MATCH_ENDED_MSG = "matchEnded";
    
    /* Notifica di espulsione */
    public static final String MATCH_KICKED_OUT = "matchKickedOut";
    
    /**
     * Nome della partita
     */
    protected final String matchName;
    
    /**
     * Impostazioni della partita
     */
    protected Object options;
    
    /**
     * Costruttore utilizzato durante la creazione
     * @param name Nome
     * @param options Opzioni
     */
    protected Match(String name, Object options) {
        this.matchName = name;
        this.options = options;
    }
    
    /**
     * Ritorna il nome della partita
     * @return Nome della partita
     */
    public String getMatchName() {
        return matchName;
    }
}
