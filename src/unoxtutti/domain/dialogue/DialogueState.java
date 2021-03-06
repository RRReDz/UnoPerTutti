/* 
 * Progetto UnoXTutto per l'esame di Sviluppo Applicazione Software.
 * Rossi Riccardo, Giacobino Davide, Sguotti Leonardo
 */
package unoxtutti.domain.dialogue;

import unoxtutti.connection.P2PMessage;

/**
 * Rappresenta un generico stato del BasicDialogueHandler. Gli oggetti di questa
 * classe definiscono i propri passaggi di stato.
 *
 * @author picardi
 * @param <U> Le classi che implementano DialogueState<> dovrebbero specificare
 * se stesse come U.
 */
public interface DialogueState<U extends DialogueState> {

    /**
     * A partire dal messaggio ricevuto e dallo stato corrente stabilisce lo
     * stato successivo
     *
     * @param msg Il messaggio che causa l'eventuale passaggio di stato
     * @return Lo stato risultante (potrebbe anche essere uguale a quello di
     * partenza se nessun passaggio di stato &egrave; avvenuto).
     */
    public U nextState(P2PMessage msg);
}
