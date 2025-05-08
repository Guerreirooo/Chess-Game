package pt.isec.pa.chess.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;

public class MainJFX extends Application {
    ChessGameManager data;

    public MainJFX() { data = new ChessGameManager(); } // It can also be created in 'init'

    @Override
    public void start(Stage stage) throws Exception {
        RootPane root = new RootPane(data,stage);
        Scene scene = new Scene(root,1300,1000);
        stage.setScene(scene);
        stage.setTitle("Chess Game");
        stage.show();
    }
}
