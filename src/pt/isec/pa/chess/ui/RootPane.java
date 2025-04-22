package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pt.isec.pa.chess.model.data.ChessGame;
import pt.isec.pa.chess.model.data.ChessGameSerialization;

import java.util.Optional;

public class RootPane extends BorderPane { //View-Controller
    ChessGame game;
    Menu mnGame,mnFile, mnView;
    MenuItem mnNew, mnLoad,mnSave,mnLoadPartial,mnSavePartial,mnUndo,mnRedo;

    public RootPane(ChessGame data) {
        this.game = data;

        createViews();
        registerHandlers();
        update();
    }

    private MenuBar createMenu() {
        MenuBar mb = new MenuBar();
        mnGame = new Menu("Game");
        mnNew = new MenuItem("New Game");
        mnLoad = new MenuItem("Load Game");
        mnSave = new MenuItem("Save Game");
        mnGame.getItems().addAll(mnNew,mnLoad,mnSave);

        mnFile = new Menu("File");
        mnLoadPartial = new MenuItem("Load Partial Game");
        mnSavePartial = new MenuItem("Save Partial Game");
        mnFile.getItems().addAll(mnLoadPartial,mnSavePartial);

        mnView = new Menu("View");
        mnUndo = new MenuItem("Undo");
        mnRedo = new MenuItem("Redo");
        mnView.getItems().addAll(mnUndo,mnRedo);

        mb.getMenus().addAll(mnGame,mnFile,mnView);
        return mb;
    }

    private String askPlayerName(String player) {
        String name;
        Optional<String> result;
        do {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nome do " + player);
            dialog.setHeaderText(null);
            dialog.setGraphic(null);
            dialog.getDialogPane().setPrefSize(250, 100);

            result = dialog.showAndWait();

            if (result.isEmpty()) return null; // o utilizador cancelou

            name = result.get().trim();
            if (!name.isEmpty()) {
                return name; // nome válido
            }
        } while (name.isEmpty());
        return null;
    }

    private void createViews() {
        /* create and configure views */
        setTop(createMenu());
    }

    private void registerHandlers() {
        /* handlers/listeners */
        mnNew.setOnAction(e -> {
            String player1,player2;
            player1 = askPlayerName("Jogador 1 (Pretas)");
            player2 = askPlayerName("Jogador 2 (Brancas)");

            //game.initGame();
            game.initGame(player1, player2);
        });

        mnLoad.setOnAction(e -> {
            ChessGame temp = ChessGameSerialization.load("SerializationGame.txt");
            if(temp == null){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erro ao carregar jogo (serialização)");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Não foi possível carregar o jogo");
                errorAlert.showAndWait();
            }
            else{
                game.alterarValores(temp);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Jogo carregado com sucesso (serializacao)");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Jogo carregado com sucesso");
                successAlert.showAndWait();
            }
        });

        mnSave.setOnAction(e -> {
            System.out.println(game);
            if(!ChessGameSerialization.save("SerializationGame.txt", game)){
                System.out.println(game);
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erro ao salvar o jogo (serializacao)");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Não foi possível salvar o jogo");
                errorAlert.showAndWait();
            }
            else{
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Jogo carregado com sucesso (serializacao)");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Jogo carregado com sucesso");
                successAlert.showAndWait();
            }
        });

        mnLoadPartial.setOnAction(e -> {
            if(!game.loadPartialGame("PartialGame.txt")){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erro ao carregar jogo");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Não foi possível carregar o jogo parcial");
                errorAlert.showAndWait();
            }
            else{
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Jogo carregado com sucesso");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Jogo parcial carregado com sucesso!");
                successAlert.showAndWait();
            }
        });

        mnSavePartial.setOnAction(e -> {
            if(!game.savePartialGame("PartialGame.txt")){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erro ao salvar jogo");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Não foi possível salvar o jogo parcial");
                errorAlert.showAndWait();
            }
            else{
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Jogo salvo com sucesso");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Jogo parcial salvo com sucesso!");
                successAlert.showAndWait();
            }
        });
    }

    private void update() {
        /* update views */
    }
}
