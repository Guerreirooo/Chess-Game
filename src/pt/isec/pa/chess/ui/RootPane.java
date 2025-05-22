package pt.isec.pa.chess.ui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.*;
import pt.isec.pa.chess.model.data.PieceColor;

import java.io.File;
import java.util.Optional;

public class RootPane extends BorderPane { //View-Controller
    ChessGameManager game;
    Stage stage;
    Pane center,right,left,top,bottom;
    Menu mnGame,mnMode;
    Label lblPlayer1,lblPlayer2,lblPlayerTurn;
    MenuItem mnNew,mnOpen,mnSave,mnImport,mnExport,mnQuit,mnUndo,mnRedo;
    CheckMenuItem mnNormal,mnLearning,mnPossibleMoves;
    Button soundButton;
    static String player1,player2,playerTurn = "WHITE";
    BoardUI boardui;
    boolean gameOver, gameDraw;

    public RootPane(ChessGameManager data, Stage stage) {
        this.game = data;
        this.stage = stage;
        gameOver = false;
        gameDraw = false;

        createViews();
        registerHandlers();
        update();
    }

    private MenuBar createMenu() {
        MenuBar mb = new MenuBar();
        mnGame = new Menu("Game");
        mnNew = new MenuItem("New");
        mnOpen = new MenuItem("Open");
        mnSave = new MenuItem("Save");
        mnImport = new MenuItem("Import");
        mnExport = new MenuItem("Export");
        mnQuit = new MenuItem("Quit");
        mnGame.getItems().addAll(mnNew,mnOpen,mnSave,mnImport,mnExport,mnQuit);

        mnMode = new Menu("Mode");
        mnNormal= new CheckMenuItem("Normal");
        mnLearning = new CheckMenuItem("Learning");
        mnPossibleMoves = new CheckMenuItem("Possible Moves");
        mnUndo = new MenuItem("Undo");
        mnRedo = new MenuItem("Redo");
        mnMode.getItems().addAll(mnNormal,mnLearning,mnPossibleMoves,mnUndo,mnRedo);
        mnNormal.setSelected(true);
        mnPossibleMoves.setVisible(false);
        mnUndo.setVisible(false);
        mnRedo.setVisible(false);



        mb.getMenus().addAll(mnGame,mnMode);
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

    private void labelsInfo(){
        right.setPrefWidth(300);

        playerTurn = game.getCurrentPlayer().toString();

        if(player1 == null || player2 == null) {
            lblPlayer1.setText("Player 1 : ");
            lblPlayer2.setText("Player 2 : ");
            lblPlayerTurn.setText("Player Turn : ");
        }
        else {
            lblPlayer1.setText("Player 1 : " + player1);
            lblPlayer2.setText("Player 2 : " + player2);
            lblPlayerTurn.setText("Player Turn : " + playerTurn);
        }
        lblPlayer1.setFont(new Font(26));
        lblPlayer1.setPrefWidth(right.getPrefWidth());
        lblPlayer1.setLayoutY(275);
        lblPlayer1.setAlignment(Pos.CENTER);

        lblPlayer2.setFont(new Font(26));
        lblPlayer2.setPrefWidth(right.getPrefWidth());
        lblPlayer2.setLayoutY(350);
        lblPlayer2.setAlignment(Pos.CENTER);

        lblPlayerTurn.setFont(new Font(26));
        lblPlayerTurn.setPrefWidth(right.getPrefWidth());
        lblPlayerTurn.setLayoutY(425);
        lblPlayerTurn.setAlignment(Pos.CENTER);
    }

    private void createViews() {
        /* create and configure views */
        center = new Pane();
        center.setStyle("-fx-background-color: #D3D3D3");
        boardui = new BoardUI(800, 800, game);

        top = new Pane();
        top.setPrefHeight(50);
        top.setStyle("-fx-background-color: #D3D3D3");
        left = new Pane();
        left.setPrefWidth(100);
        left.setStyle("-fx-background-color: #D3D3D3");
        bottom = new Pane();
        bottom.setPrefHeight(50);
        bottom.setStyle("-fx-background-color: #D3D3D3");

        right = new Pane();
        right.setStyle("-fx-background-color: #D3D3D3");
        lblPlayer1 = new Label();
        lblPlayer2 = new Label();
        lblPlayerTurn = new Label();

        soundButton = new Button();
        soundButton.setPrefSize(right.getPrefWidth(), 40);
        soundButton.setText("Sound: " + game.getSound());
        soundButton.prefWidthProperty().bind(Bindings.subtract(right.widthProperty(), 40));
        soundButton.setTranslateX(15);
        labelsInfo();


        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(createMenu(), top);

        setTop(topContainer);
        setRight(right);
        setLeft(left);
        setCenter(center);
        setBottom(bottom);
        center.getChildren().add(boardui.getCanvas());
        right.getChildren().addAll(lblPlayer1,lblPlayer2,lblPlayerTurn,soundButton);
    }

    private void registerHandlers() {
        /* handlers/listeners */
        mnNew.setOnAction(e -> {
            player1 = askPlayerName("Jogador 1 (Pretas)");
            player2 = askPlayerName("Jogador 2 (Brancas)");

            if(player1 != null || player2 != null) {
                gameOver = false;
                gameDraw = false;
                game.initGame(player1, player2);
                game.setCurrentPlayer(PieceColor.WHITE);
                game.setNumMovements(0);
            }
        });

        mnOpen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Carregar Jogo");

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Ficheiro do Jogo (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                mnNew.fire();

                if (!game.load(file.getAbsolutePath())) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erro ao carregar jogo (serialização)");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Não foi possível carregar o jogo");
                    errorAlert.showAndWait();
                } else {
                    gameOver = false;
                    gameDraw = false;
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Jogo carregado com sucesso (serializacao)");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Jogo carregado com sucesso");
                    successAlert.showAndWait();
                }
            }
        });

        mnSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Jogo (serialização)");

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Ficheiro do Jogo (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                if (!game.save(file.getAbsolutePath())) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erro ao salvar o jogo (serializacao)");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Não foi possível salvar o jogo");
                    errorAlert.showAndWait();
                } else {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Jogo salvo com sucesso");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Jogo salvo com sucesso");
                    successAlert.showAndWait();
                }
            }
        });

        mnImport.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Carregar Jogo");

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Ficheiro do Jogo (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                mnNew.fire();

                if (!game.importGame(file.getAbsolutePath())) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erro ao carregar jogo");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Não foi possível carregar o jogo parcial");
                    errorAlert.showAndWait();
                } else {
                    gameOver = false;
                    gameDraw = false;
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Jogo carregado com sucesso");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Jogo parcial carregado com sucesso!");
                    successAlert.showAndWait();
                }
            }
        });

        mnExport.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Abrir Jogo");

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Ficheiro do Jogo (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                if (!game.exportGame(file.getAbsolutePath())) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erro ao salvar jogo");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Não foi possível salvar o jogo parcial");
                    errorAlert.showAndWait();
                } else {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Jogo salvo com sucesso");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Jogo parcial salvo com sucesso!");
                    successAlert.showAndWait();
                }
            }
        });

        mnLearning.selectedProperty().addListener((obs, oldVal, newVal) -> {
                mnPossibleMoves.setVisible(newVal);
                mnUndo.setVisible(newVal);
                mnRedo.setVisible(newVal);
                mnNormal.setSelected(oldVal);
        });

        mnPossibleMoves.selectedProperty().addListener((obs, oldVal, newVal) -> {
            boardui.setPossibleMoves(newVal);
        });

        mnNormal.selectedProperty().addListener((obs, oldVal, newVal) -> {
                mnLearning.setSelected(oldVal);
        });

        mnQuit.setOnAction(e -> {
            Platform.exit();
        });

        center.widthProperty().addListener((_,_,_) -> {
            boardui.setWidth(center.getWidth());
            boardui.setHeight(center.getHeight());
            boardui.createCanvas();
        });

        center.heightProperty().addListener((_,_,_) -> {
            boardui.setWidth(center.getWidth());
            boardui.setHeight(center.getHeight());
            boardui.createCanvas();
        });

        boardui.setOnMousePressed(mouseEvent -> {
            boardui.onPressed(mouseEvent.getSceneX(), mouseEvent.getSceneY(), gameOver, gameDraw);
        });

        game.addPropertyChangeListener(
            game.GAME_VALUE, evt -> {
                    update();
        });

        game.addPropertyChangeListener(
            game.PLAYER_VALUE, evt -> {
                    labelsInfo();
        });

        soundButton.setOnAction(e -> {
            if(game.getSound().equals("ON")) {
                game.setSounds(false);
                soundButton.setText("Sound: " + game.getSound());
            }
            else {
                game.setSounds(true);
                soundButton.setText("Sound: " + game.getSound());
            }
        });

        mnRedo.setOnAction(e -> {
            game.redo();
        });

        mnUndo.setOnAction(e -> {
            game.undo();
        });
    }

    private void update() {
        /* update views */

        if (game.hasRedo()) {
            mnRedo.setDisable(false);
        }
        else {
            mnRedo.setDisable(true);
        }

        if (game.hasUndo()) {
            mnUndo.setDisable(false);
        }
        else {
            mnUndo.setDisable(true);
        }

        boardui.createCanvas();
        if (game.check(playerTurn)) {
            gameOver = true;
            if (game.checkMate()) {
                gameOver = true;
            }
        }
        if (game.drownedKing() && game.getNumMovements() > 10) {
            gameDraw = true;
        }
        if (game.lackOfMaterial() && game.getNumMovements() > 10) {
            gameDraw = true;
        }
    }
}
