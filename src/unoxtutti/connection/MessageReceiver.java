/* 
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.connection;

/**
 * L'interfaccia MessageReceiver va a definire, insieme alla P2PConnection, un
 * pattern Observer relativo alla ricezione di messaggi. Le classi che vogliono
 * osservare i messaggi in arrivo devono implementare questa interfaccia e le
 * loro istanze devono registrarsi presso la P2PConnection tramite il metodo
 * addMessageReceivedObserver, specificando a quale tipo di messaggio sono
 * interessate. Ogni tipo di messaggio (identificato tramite il <em>name</em>
 * dell'oggetto P2PMessage) ha una lista di osservatori separata. Un oggetto può
 * registrarsi pi&ugrave; volte per pi&ugrave; tipi di messaggi, cos&igrave;
 * come pu&ograve; smettere di osservare un certo tipo di messaggio tramite il
 * metodo removeMessageReceivedObserver di P2PConnection.
 *
 * @author picardi
 */
public interface MessageReceiver {

    /**
     * Questo metodo viene invocato dal soggetto osservato (un'istanza di
     * P2PConnection) quando arriva un messaggio del tipo a cui il
     * MessageReceiver si è dichiarato interessato.
     *
     * @param msg Il messaggio ricevuto.
     */
    public void updateMessageReceived(P2PMessage msg);
}
