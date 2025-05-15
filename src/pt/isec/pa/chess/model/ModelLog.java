package pt.isec.pa.chess.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class ModelLog {
    PropertyChangeSupport pcs;
    public static final String LOG_CHANGE = "log_change";

    private static ModelLog _instance=null;
    public static ModelLog getInstance() {
        if (_instance == null)
            _instance = new ModelLog();
        return _instance;
    }
    protected ArrayList<String> log;
    private ModelLog() {
        log = new ArrayList<>();
        pcs = new PropertyChangeSupport(this);
    }
    public void reset() {
        log.clear();
        pcs.firePropertyChange(LOG_CHANGE, null, null);
    }
    public void log(String msg) {
        log.add(msg);
        pcs.firePropertyChange(LOG_CHANGE, null, null);
    }
    public List<String> getLog() {
        return new ArrayList<>(log);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property,listener);
    }
}