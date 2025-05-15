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
        Scene scene = new Scene(root,1100,800);
        stage.setScene(scene);
        stage.setTitle("Chess Game");
        stage.show();

        Stage stage2 = new Stage();
        RootPane root2 = new RootPane(data,stage2);
        Scene scene2 = new Scene(root2,800,600);
        stage2.setScene(scene2);
        stage2.setTitle("Chess Game 2");
        stage2.show();

        Stage stage3 = new Stage();
        LogPane log = new LogPane(stage3);
        Scene scene3 = new Scene(log,400,500);
        stage3.setScene(scene3);
        stage3.setTitle("Log");
        stage3.show();
    }
}
