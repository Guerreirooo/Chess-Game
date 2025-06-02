package pt.isec.pa.chess.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe singleton responsável por alterar log do modelo.
 * <p>
 * Permite armazenar mensagens de log.
 */
public class ModelLog {
    /**
     * Constante usada como identificador do evento de alteração no log.
     */
    public static final String LOG_CHANGE = "log_change";

    private static ModelLog _instance = null;

    PropertyChangeSupport pcs;
    protected ArrayList<String> log;

    /**
     * Retorna a instância única (singleton) de {@code ModelLog}.
     *
     * @return instância única da classe {@code ModelLog}
     */
    public static ModelLog getInstance() {
        if (_instance == null)
            _instance = new ModelLog();
        return _instance;
    }

    /**
     * Construtor privado para implementar o padrão Singleton.
     * Inicializa o log e o suporte a eventos.
     */
    private ModelLog() {
        log = new ArrayList<>();
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Limpa completamente o log atual.
     */
    public void reset() {
        log.clear();
        pcs.firePropertyChange(LOG_CHANGE, null, null);
    }

    /**
     * Adiciona uma nova mensagem ao log.
     *
     * @param msg mensagem a adicionar ao log
     */
    public void log(String msg) {
        log.add(msg);
        pcs.firePropertyChange(LOG_CHANGE, null, null);
    }

    /**
     * Devolve uma cópia do log atual.
     *
     * @return lista de mensagens de log
     */
    public List<String> getLog() {
        return new ArrayList<>(log);
    }

    /**
     * Regista um ouvinte para escutar alterações.
     *
     * @param property propriedade a escutar (neste caso, use {@code LOG_CHANGE})
     * @param listener ouvinte que será notificado quando ocorrer uma alteração
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }
}