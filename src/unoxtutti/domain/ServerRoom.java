/* 
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.domain;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import unoxtutti.connection.MessageReceiver;
import unoxtutti.connection.P2PConnection;
import unoxtutti.connection.P2PMessage;
import unoxtutti.connection.PartnerShutDownException;
import unoxtutti.connection.ServerCreationException;
import unoxtutti.utils.DebugHelper;

/**
 * La classe ServerRoom rappresenta una Room (Stanza) lato Server La Room lato
 * Server è il Server stesso.
 *
 * @author picardi
 */
public class ServerRoom extends Room implements Runnable, MessageReceiver {

    private final Player owner;
    private ServerSocket serverSock;
    private boolean shouldClose;
    private boolean closed;
    private final Object closeDownMonitor;

    private final HashMap<Player, P2PConnection> connections;
    private final ArrayList<P2PConnection> waitingClients;
    
    /**
     * Partite all'interno della stanza. La chiave utilizzata
     * dalla mappa è il nome della partita.
     */
    private final HashMap<String, ServerMatch> matches;

    /**
     * Il costruttore è privato; una ServerRoom può essere creata solo tramite
     * il factory method
     * <em>createServerRoom</em>
     *
     * @param p Il giocatore che crea la stanza
     * @param roomName Il nome della stanza con cui gli altri giocatori potranno
     * connettersi
     */
    private ServerRoom(Player p, String roomName) {
        super(roomName);
        owner = p;
        closed = false;
        closeDownMonitor = new Object();
        connections = new HashMap<>();
        waitingClients = new ArrayList<>();
        matches = new HashMap<>();
    }

    /**
     * Permette a un thread di aspettare per un certo tempo che il server si
     * chiuda.
     *
     * @param timeout Il tempo per cui aspettare.
     * @throws InterruptedException
     */
    public void waitOnClose(long timeout) throws InterruptedException {
        synchronized (closeDownMonitor) {
            if ((closeDownMonitor != null) && (!isClosed())) {
                closeDownMonitor.wait(timeout);
            }
        }
    }

    /**
     * Indica al Server che dovrebbe iniziare le procedure di chiusura
     */
    public void shouldClose() {
        boolean ok = false;
        synchronized (closeDownMonitor) {
            shouldClose = true;
            ok = isClosed();
        }
        // Sveglia se stesso dall'attesa di connessioni
        // stabilendo una P2PConnection con se stesso
        if (ok) {
            return;
        }

        P2PConnection conn = null;
        try {
            conn = P2PConnection.connectToHost(owner, this.getAddress(), this.getPort());
        } catch (IOException ex) {
            Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void justStarted() {
        synchronized (closeDownMonitor) {
            shouldClose = false;
        }
    }

    private boolean shouldIClose() {
        synchronized (closeDownMonitor) {
            return shouldClose;
        }
    }

    private void setClosed(boolean b) {
        synchronized (closeDownMonitor) {
            boolean prev = isClosed();
            closed = b;
            if (!prev) {
                closeDownMonitor.notifyAll();
            }
        }
    }

    /**
     * Permette di verificare se il Server si è effettivamente chiuso.
     *
     * @return true se il Server è chiuso, false altrimenti.
     */
    public boolean isClosed() {
        boolean ret = false;
        synchronized (closeDownMonitor) {
            ret = closed;
        }
        return ret;
    }

    /**
     * Forza lo shut down del Server interrompendo tutte le connessioni ai
     * client.
     */
    public void forceClose() {
        synchronized (closeDownMonitor) {
            if (isClosed()) {
                return;
            }
        }
        for (P2PConnection p2p : connections.values()) {
            if (!p2p.isClosed()) {
                p2p.forceClose();
            }
        }
        setClosed(true);
    }

    /**
     * Crea una nuova ServerRoom e fa partire il Server.
     *
     * @param p il Giocatore che crea la Room
     * @param roomName il nome della Room
     * @param port la porta su cui aprire la Room
     * @return la ServerRoom appena creata
     * @throws ServerCreationException se non riesce ad ottenere l'indirizzo di
     * "localhost"
     */
    public static ServerRoom createServerRoom(Player p, String roomName, int port) throws ServerCreationException {
        ServerRoom room = new ServerRoom(p, roomName);
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException exc) {
            throw new ServerCreationException("Cannot find localhost. Server creation impossible.");
        }
        room.setAddress(localhost);
        room.setPort(port);
        (new Thread(room)).start();
        return room;
    }
    
    /**
     * L'esecuzione vera e propria del Server thread
     *
     */
    @Override
    public void run() {
        if (closed) {
            return;
        }
        try {
            serverSock = new ServerSocket(getPort());
            justStarted();

            while (!shouldIClose()) {
                P2PConnection playerConnection = P2PConnection.acceptConnectionRequest(serverSock);
                synchronized (waitingClients) {
                    waitingClients.add(playerConnection);
                    /**
                     * Il server si mette in ascolto sulla nuova connessione
                     * di messaggi di richiesta di ingresso 
                     */
                    playerConnection.addMessageReceivedObserver(this, Room.ROOM_ENTRANCE_REQUEST_MSG);
                }
            }
            System.out.println("Server is closing down");
            ArrayList<P2PConnection> disc = new ArrayList<>();
            disc.addAll(connections.values());
            disc.addAll(waitingClients);
            for (P2PConnection p2p : disc) {
                p2p.disconnect();
            }
            boolean canClose = false;
            while (!canClose) {
                canClose = true;
                for (P2PConnection p2p : connections.values()) {
                    if (!p2p.isClosed()) {
                        canClose = false;
                    }
                }
            }
            System.out.println("All helpers stopped");
            setClosed(true);
            serverSock.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void addPlayer(P2PConnection conn) {
        this.connections.put(conn.getPlayer(), conn);
        playerJoinedTheRoom(conn);
    }

    protected void removePlayer(P2PConnection conn) {
        this.connections.remove(conn.getPlayer());
    }

    /**
     * Restituisce il numero di giocatori presenti nella stanza.
     *
     * @return il numero di giocatori presenti nella stanza
     */
    @Override
    public int getPlayerCount() {
        return connections.keySet().size();
    }

    /**
     * Restituisce una copia dell'elenco di giocatori presenti nella stanza.
     *
     * @return l'elenco (in copia) dei giocatori presenti nella stanza
     */
    @Override
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> ret = new ArrayList<>();
        ret.addAll(connections.keySet());
        return ret;
    }

    @Override
    public void updateMessageReceived(P2PMessage msg) {
        String msgName = msg.getName();
        switch(msgName) {
            case Room.ROOM_ENTRANCE_REQUEST_MSG:
                DebugHelper.log("ROOM: ricevuta richiesta di ingresso.");
                handleEntranceRequest(msg);
                break;
            case Room.ROOM_EXIT_MSG:
                DebugHelper.log("ROOM: ricevuta notifica di uscita.");
                handleExit(msg);
                break;
            case Match.MATCH_CREATION_REQUEST_MSG:
                DebugHelper.log("ROOM: ricevuta richiesta di creazione partita.");
                handleMatchCreation(msg);
                break;
            case Match.MATCH_ACCESS_REQUEST_MSG:
                DebugHelper.log("ROOM: ricevuta richiesta di accesso ad una partita.");
                handleMatchAccessRequest(msg);
                break;
            case Match.MATCH_STARTING_MSG:
                DebugHelper.log("ROOM: ricevuta richiesta di avvio di una partita.");
                handleMatchStart(msg);
                break;
            case Match.MATCH_CLOSING_MSG:
                DebugHelper.log("ROOM: ricevuta richiesta di chiusura di una partita.");
                handleMatchClosing(msg);
                break;
        }
    }

    private void handleEntranceRequest(P2PMessage msg) {
        boolean reqOk = true;
        Player player = null;
        if (msg.getParametersCount() != 2) {
            reqOk = false;
        } else {
            try {
                String roomName = (String) msg.getParameter(0);
                player = (Player) msg.getParameter(1);
                if (!roomName.equals(this.getName())) {
                    reqOk = false;
                }
            } catch (ClassCastException ex) {
                reqOk = false;
            }
        }
        
        if(reqOk) {
            DebugHelper.log("ROOM: OK, richiesta di ingresso corretta. Giocatore autorizzato.");
        } else {
            DebugHelper.log("ROOM: ERR, richiesta di ingresso NON corretta. Cambiare nome della stanza e riprovare.");
        }
           
        P2PConnection sender = msg.getSenderConnection();
        P2PMessage reply = new P2PMessage(Room.ROOM_ENTRANCE_REPLY_MSG);
        Object[] parameters = new Object[2]; // reply + players
        reply.setParameters(parameters);
        parameters[0] = reqOk;

        synchronized (this) {
            if (reqOk && player != null) {
                sender.setPlayer(player);
                addPlayer(sender);
                waitingClients.remove(sender);
                parameters[1] = this.getPlayers();
            }
            
            /**
             * In ogni caso mando il messaggio di risposta
             * altrimenti il client rimane bloccato all'infinito sulla wait
             */
            try {
                sender.sendMessage(reply);
                if(reqOk) {
                    sendRoomUpdate();
                }
            } catch (PartnerShutDownException ex) {
                sender.disconnect();
                removePlayer(sender);
            }
            
            this.waitingClients.remove(sender);
        }
    }

    private void handleExit(P2PMessage msg) {
        synchronized (this) {
            this.removePlayer(msg.getSenderConnection());
            sendRoomUpdate();
        }
    }

    void sendRoomUpdate() {
        synchronized(this) {
            P2PMessage upd = new P2PMessage(Room.ROOM_UPDATE_MSG);
            Object[] updpar = new Object[]{this.getPlayers(), this.getAvailableMatches()};
            upd.setParameters(updpar);
            while (upd != null) {
                ArrayList<P2PConnection> unresp = new ArrayList<>();
                for (P2PConnection client : connections.values()) {
                    try {
                        client.sendMessage(upd);
                    } catch (PartnerShutDownException ex) {
                        unresp.add(client);
                    }
                }
                for (P2PConnection p2p : unresp) {
                    p2p.disconnect();
                    removePlayer(p2p);
                }
                if (unresp.size() > 0) {
                    upd.setParameters(new Object[]{this.getPlayers()});
                } else {
                    upd = null;
                }
            }
        }
        
    }
    
    /**
     * Gestisce la richiesta di creazione di una partita.
     * @param msg Messaggio di richiesta
     */
    private void handleMatchCreation(P2PMessage msg) {
        /* Controllo validità dati ricevuti */
        boolean reqOk = true;
        Player matchOwner = null;
        String matchName = null;
        if (msg.getParametersCount() != 2) {
            reqOk = false;
        } else {
            try {
                matchOwner = (Player) msg.getParameter(0);
                matchName = (String) msg.getParameter(1);
                if(matches.containsKey(matchName)) {
                    /* Esiste già una partita con lo stesso nome */
                    reqOk = false;
                }
            } catch (ClassCastException ex) {
                reqOk = false;
            }
        }
        
        /* Costruzione messaggio di risposta */
        P2PConnection sender = msg.getSenderConnection();
        P2PMessage reply = new P2PMessage(Match.MATCH_CREATION_REPLY_MSG);
        Object[] parameters = new Object[1];
        reply.setParameters(parameters);
        parameters[0] = reqOk;
        
        /* Creazione della partita ed invio risposta */
        synchronized(this) {
            if(reqOk && matchOwner != null) {
                /* Creazione della partia */
                createMatch(matchOwner, matchName);
                playerCreatedAMatch(sender);
            }
            
            /* Invio risposta (sia in caso di successo che insuccesso) */
            try {
                sender.sendMessage(reply);
                sendRoomUpdate();
                matches.get(matchName).sendMatchUpdate();
            } catch (PartnerShutDownException ex) {
                sender.disconnect();
                removePlayer(sender);
            }
        }
    }
    
    /**
     * Inizializza una partita e la aggiunge alla lista delle partite.
     * @param matchOwner Proprietario della partita
     * @param matchName Nome della partita
     */
    private void createMatch(Player matchOwner, String matchName) {
        if(matchOwner == null || matchName == null || matchName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Dati mancanti per la creazione di una partita."
            );
        }
        matches.put(
                matchName,
                new ServerMatch(
                    this,
                    matchOwner,
                    matchName,
                    new Object() // TODO: Opzioni
                )
        );
    }
    
    /**
     * Gestisce la richiesta di avvio della partita.
     * @param msg Messaggio di richiesta
     */
    private void handleMatchStart(P2PMessage msg) {
        /* Controllo validità dati ricevuti e valori partita */
        ServerMatch match = null;
        boolean isReqOk = true;
        if (msg.getParametersCount() != 1) {
            isReqOk = false;
        } else {
            try {
                String matchName = (String) msg.getParameter(0);
                if(!matches.containsKey(matchName)) {
                    /* La partita non esiste */
                    isReqOk = false;
                }
                /* La partita desiderata esiste */
                else {
                    /* Recupero il match dal nome */
                    match = matches.get(matchName);
                    /* Controllo se partita già iniziata */
                    if(match.isStarted()) {
                        isReqOk = false;
                    }
                }
            } catch (ClassCastException ex) {
                isReqOk = false;
            }
        }
        
        if(isReqOk) {
            DebugHelper.log("ROOM: OK, richiesta di avvio partita corretta.");
        } else {
            DebugHelper.log("ROOM: ERR, richiesta di avvio partita NON corretta.");
        }
            
        /* Costruzione messaggio di risposta */
        P2PConnection sender = msg.getSenderConnection();
        P2PMessage reply = new P2PMessage(Match.MATCH_STARTING_REPLY_MSG);
        
        /* Parametri messaggio */
        Object[] parameters = new Object[1];
        reply.setParameters(parameters);
        parameters[0] = isReqOk;
        
        /**
        * Invio della risposta al client creatore della partita
        */
        synchronized(this) {
            /* Rimozione listener inutili */
            if(match != null && isReqOk) {
                playerStartedHisMatch(sender);
            }
            /* Invio risposta al proprietario */
            try {
                sender.sendMessage(reply);
            } catch (PartnerShutDownException ex) {
                sender.disconnect();
            }
            /* Avvio partita */
            if(match != null && isReqOk) {
                match.start();
            }
        }
    }
    
    /**
     * Gestisce la chiusura di una partita.
     * @param msg Messaggio di chiusura
     */
    private void handleMatchClosing(P2PMessage msg) {
        /* Controllo validità dati ricevuti e valori partita */
        boolean isReqOk = true;
        ServerMatch match = null;
        if (msg.getParametersCount() != 1) {
            isReqOk = false;
        } else {
            try {
                String matchName = (String) msg.getParameter(0);
                if(!matches.containsKey(matchName)) {
                    /* La partita non esiste */
                    isReqOk = false;
                } else {
                    /**
                     * La partita desiderata esiste
                     * Recupero il match dal nome
                     */
                    match = matches.get(matchName);
                }
            } catch (ClassCastException ex) {
                isReqOk = false;
            }
        }
        
        if(isReqOk) {
            DebugHelper.log("ROOM: OK, richiesta di chiusura partita corretta.");
        } else {
            DebugHelper.log("ROOM: ERR, richiesta di chiusura partita NON corretta.");
        }
        
        /* Costruzione messaggio di risposta */
        P2PConnection sender = msg.getSenderConnection();
        P2PMessage reply = new P2PMessage(Match.MATCH_CLOSING_REPLY_MSG);
        
        /* Parametri messaggio */
        Object[] parameters = new Object[1];
        reply.setParameters(parameters);
        parameters[0] = isReqOk;
        
        /**
         * Invio della risposta al client creatore della partita
         */
        synchronized(this) {
            /**
             * Set parametro che indica che la partita è stata avviata
             * Set dei relativi listener per messaggi successivi
             * sempre se richiesta è ok
             */
            if(match != null && isReqOk) {
                playerClosedHisMatch(sender);
            }
            
            /* Invio risposta */
            try {
                sender.sendMessage(reply);
            } catch (PartnerShutDownException ex) {
                sender.disconnect();
            }
        }
        
        /* Aggiorno gli altri client in partita se la richiesta è andata a buon fine. */
        if(match != null && isReqOk) {
            /* Aggiornamento a giocatori in partita e rimozione della partita */
            deleteMatchFromList(match.getMatchName());
            match.notifyMatchClosure();
        }
    }
    
    /**
     * Ritorna la lista delle partite.
     * @return Lista delle partite.
     */
    @Override
    public ArrayList<String> getAvailableMatches() {
        ArrayList<String> matchesList = new ArrayList<>();
        matches.values().stream().filter((m) -> (!m.isStarted())).forEachOrdered((m) -> {
            matchesList.add(m.getMatchName());
        });
        
        /* Per i plebei: */
        //for(ServerMatch m : matches.values()) {
        //    if(!m.isStarted()) {
        //        matchesList.add(m.getMatchName());
        //    }
        //}
        
        return matchesList;
    }
    
    /**
     * Il giocatore desidera entrare in una partita.
     * @param msg Messaggio di richiesta
     */
    private void handleMatchAccessRequest(P2PMessage msg) {
        /* Controllo validità dati ricevuti */
        ServerMatch match = null;
        boolean reqOk = true;
        if (msg.getParametersCount() != 1) {
            reqOk = false;
        } else {
            try {
                String matchName = (String) msg.getParameter(0);
                if(!matches.containsKey(matchName)) {
                    /* La partita non esiste */
                    reqOk = false;
                } else {
                    /* La partita desiderata esiste */
                    match = matches.get(matchName);
                    if(match.containsPlayer(msg.getSenderConnection().getPlayer())) {
                        throw new IllegalStateException("Il giocatore è già presente nella partita.");
                    }
                    reqOk = match.canPlayerJoin(msg.getSenderConnection().getPlayer());
                }
            } catch (ClassCastException ex) {
                reqOk = false;
            } catch (IllegalStateException ex) {
                reqOk = false;
                Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /* Costruzione messaggio di risposta */
        P2PConnection sender = msg.getSenderConnection();
        P2PMessage reply = new P2PMessage(Match.MATCH_ACCESS_REQUEST_REPLY_MSG);
                
        /**
         * Si avvisa il giocatore che la richiesta è stata presa in carico,
         * oppure che è stata scartata.
         */
        synchronized(this) {
            /**
             * Si chiede al proprietario della partita che cosa vuole fare
             * con la richiesta del giocatore.
             */
            if(reqOk && match != null) {
                reqOk = match.askOwnerIfPlayerCanJoin(msg.getSenderConnection().getPlayer());
                
                /**
                 * A questo punto, se reqOk è true, il proprietario della partita
                 * ha ricevuto la richiesta del giocatore.
                 */
            }
            
            /* Invio risposta */
            try {
                Object[] parameters = new Object[1];
                parameters[0] = reqOk;
                reply.setParameters(parameters);
                sender.sendMessage(reply);
            } catch (PartnerShutDownException ex) {
                sender.disconnect();
                removePlayer(sender);
            }
        }
    }
    
    /**
     * Il giocatore è entrato nella stanza, si aggiornano i listener.
     * 
     * Il giocatore può ora creare ed accedere a partite. Può anche abbandonare
     * la stanza.
     * @param playerConnection 
     */
    void playerJoinedTheRoom(P2PConnection playerConnection) {
        playerConnection.addMessageReceivedObserver(this, Room.ROOM_EXIT_MSG);
        playerConnection.addMessageReceivedObserver(this, Match.MATCH_CREATION_REQUEST_MSG);
        playerConnection.addMessageReceivedObserver(this, Match.MATCH_ACCESS_REQUEST_MSG);
        playerConnection.removeMessageReceivedObserver(this, Room.ROOM_ENTRANCE_REQUEST_MSG);
    }
    
    /**
     * Il giocatore ha creato una stanza, si aggiornano i listener.
     * 
     * Non ha più senso ricevere messaggi di creazione/ingresso dal giocatore,
     * ma ora si possono ricevere messaggi di avvio/distruzione della partita.
     * @param playerConnection Connessione con il giocatore
     */
    private void playerCreatedAMatch(P2PConnection playerConnection) {
        playerConnection.addMessageReceivedObserver(this, Match.MATCH_CLOSING_MSG);
        playerConnection.addMessageReceivedObserver(this, Match.MATCH_STARTING_MSG);
        playerConnection.removeMessageReceivedObserver(this, Match.MATCH_CREATION_REQUEST_MSG);
        playerConnection.removeMessageReceivedObserver(this, Match.MATCH_ACCESS_REQUEST_MSG);
    }
    
    /**
     * Il giocatore ha avviato la stanza, si aggiornano i listener.
     * 
     * Non ha più senso ricevere messaggi di avvio della partita,
     * ma ora si possono ricevere messaggi di chiusura della partita
     * @param playerConnection Connessione con il giocatore
     */
    private void playerStartedHisMatch(P2PConnection playerConnection) {
        playerConnection.removeMessageReceivedObserver(this, Match.MATCH_STARTING_MSG);
        /* TODO: Aggiungere listener messaggi avvio manche */
    }
    
    private void playerClosedHisMatch(P2PConnection playerConnection) {
        playerConnection.removeMessageReceivedObserver(this, Match.MATCH_CLOSING_MSG);
        /* Aggiorno i listener come se l'utente stesse rientrando in stanza */
        playerJoinedTheRoom(playerConnection);
    }
    
    
    /**
     * Ritorna la connessione con un giocatore presente nella stanza
     * @param player Giocatore
     * @return <code>P2PConnection</code> con il giocatore, oppure <code>null</code>
     */
    public P2PConnection getConnectionWithPlayer(Player player) {
        return connections.get(player);
    }

    
    /**
     * Controlla se un determinato giocatore è in una partita oppure no
     * @param player Giocatore
     * @return <code>true</code> se il giocatore si trova in una partita,
     *          <code>false</code> altrimenti.
     */
    boolean playerIsInAMatch(Player player) {
        synchronized(this) {
            return matches.values().stream().anyMatch((m) -> (m.containsPlayer(player)));
        }
    }

    
    /**
     * Elimina tutte le richieste di accesso di un giocatore.
     * @param player Giocatore
     */
    void deleteAccessRequests(Player player) {
        /* Si eliminano tutte le richieste del giocatore */
        synchronized(this) {
            matches.values().forEach((m) -> {
                m.removePlayerAccessRequest(player);
            });
        }
    }
    
    /**
     * Eliminazione di una partita dalla lista
     * @param matchName nome della parita
     * @return <code>true</code> se la partita è stata eliminata,
     *          <code>false</code> altrimenti.
     */
    void deleteMatchFromList(String matchName) {
        synchronized(this) {
            connections.values().forEach((c) -> {
                c.removeMessageReceivedObserver(matches.get(matchName));
            });
            matches.remove(matchName);
        }
    }

    /**
     * Richiamata da una partita quando questa termina.
     * 
     * @param match Elimina la partita dalla lista delle partite.
     */
    void matchEnded(ServerMatch match) {
        synchronized(this) {
            if(matches.containsValue(match)) {
                /* Non usare operatori funzionali! */
                for(String matchName : matches.keySet()) {
                    if(matches.get(matchName) == match) {
                        matches.remove(matchName);
                        break;
                    }
                }
                sendRoomUpdate();
            }
            
            /* Reset listeners */
            match.players.forEach((p) -> {
                playerJoinedTheRoom(connections.get(p));
            });
        }
    }
}
