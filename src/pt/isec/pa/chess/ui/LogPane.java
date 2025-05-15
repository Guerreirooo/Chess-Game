package pt.isec.pa.chess.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ModelLog;
import pt.isec.pa.chess.model.data.PieceColor;


public class LogPane extends BorderPane {
    Menu mnLog;
    MenuItem mnClear;
    ListView<String> lvLog;
    ObservableList<String> logItems;

    public LogPane(Stage stage) {
        createViews();
        registerHandlers();
        update();
    }

    private MenuBar createMenu() {
        MenuBar mb = new MenuBar();
        mnLog = new Menu("Log");
        mnClear = new MenuItem("clear");
        mnLog.getItems().addAll(mnClear);
        mb.getMenus().addAll(mnLog);
        return mb;
    }

    private ListView<String> createListView() {
        lvLog = new ListView<>();
        return lvLog;
    }

    void createViews(){
        // Menu
        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(createMenu());
        setTop(topContainer);

        // TextArea
        setCenter(createListView());
    }

    void registerHandlers(){
        mnClear.setOnAction(e -> {
            ModelLog.getInstance().reset();
        });
        ModelLog.getInstance().addPropertyChangeListener(
                ModelLog.getInstance().LOG_CHANGE, evt -> {
                    update();
                }
        );
    }

    void update(){
        lvLog.getItems().clear();
        lvLog.getItems().addAll(ModelLog.getInstance().getLog());
    }
}
