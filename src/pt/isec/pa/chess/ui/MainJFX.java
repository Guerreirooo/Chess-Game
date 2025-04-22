package pt.isec.pa.chess.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.data.ChessGame;

public class MainJFX extends Application {
    ChessGame data;

    public MainJFX() { data = new ChessGame(); } // It can also be created in 'init'

    @Override
    public void start(Stage stage) throws Exception {
        RootPane root = new RootPane(data);
        Scene scene = new Scene(root,800,800);
        stage.setScene(scene);
        stage.setTitle("Chess Game");
        stage.show();
    }
}
