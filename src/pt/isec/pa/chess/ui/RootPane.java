package pt.isec.pa.chess.ui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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
import pt.isec.pa.chess.model.data.PieceType;
import pt.isec.pa.chess.model.data.pieces.MoveType;
import pt.isec.pa.chess.model.data.pieces.Piece;

import java.io.File;
import java.util.Optional;

public class RootPane extends BorderPane {
    ChessGameManager game;
    Stage stage;
    Pane center,right,left,top,bottom;
    Menu mnGame,mnMode;
    Label lblPlayer1,lblPlayer2,lblPlayerTurn;
    MenuItem mnNew,mnOpen,mnSave,mnImport,mnExport,mnQuit,mnUndo,mnRedo,mnEditor;
    CheckMenuItem mnNormal,mnLearning,mnPossibleMoves;
    Button soundButton;
    static String player1,player2,playerTurn = "WHITE";
    BoardUI boardui;
    boolean gameOver, gameDraw, editor;
    String selectedColor;

    public RootPane(ChessGameManager data, Stage stage) {
        this.game = data;
        this.stage = stage;
        gameOver = false;
        gameDraw = false;
        editor = false;

        createViews();
        registerHandlers();
        update();
    }

    private MenuBar createMenu() {
        MenuBar mb = new MenuBar();
        mnGame = new Menu("Game");
        mnNew = new MenuItem("New");
        mnEditor = new MenuItem("New (Editor Mode)");
        mnOpen = new MenuItem("Open");
        mnSave = new MenuItem("Save");
        mnImport = new MenuItem("Import");
        mnExport = new MenuItem("Export");
        mnQuit = new MenuItem("Quit");
        mnGame.getItems().addAll(mnNew,mnEditor,mnOpen,mnSave,mnImport,mnExport,mnQuit);

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

    private void promotePawn() {
        if (!boardui.getPromote())
            return;

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Promoção de Peão");
        alert.setHeaderText("Escolhe uma peça para promover:");

        // Criação dos botões
        ButtonType queenBtn = new ButtonType("Rainha");
        ButtonType rookBtn = new ButtonType("Torre");
        ButtonType bishopBtn = new ButtonType("Bispo");
        ButtonType knightBtn = new ButtonType("Cavalo");

        alert.getButtonTypes().setAll(queenBtn, rookBtn, bishopBtn, knightBtn);

        // Mostra e espera pela escolha do utilizador
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == queenBtn) {
                boardui.promotePawn(PieceType.QUEEN);
            } else if (result.get() == rookBtn) {
                boardui.promotePawn(PieceType.ROOK);
            } else if (result.get() == bishopBtn) {
                boardui.promotePawn(PieceType.BISHOP);
            } else if (result.get() == knightBtn) {
                boardui.promotePawn(PieceType.KNIGHT);
            }
        }
    }

    private PieceType getPieceAdd() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Adicionar peça");
        alert.setHeaderText("Escolhe uma peça para adicionar:");

        ComboBox<String> colorBox = new ComboBox<>();
        colorBox.getItems().addAll("White", "Black");
        colorBox.setValue("White");

        // Layout personalizado
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().add(new Label("Choose color:"));
        content.getChildren().add(colorBox);

        alert.getDialogPane().setContent(content);

        // Criação dos botões
        ButtonType queenBtn = new ButtonType("Rainha");
        ButtonType kingBtn = new ButtonType("Rei");
        ButtonType rookBtn = new ButtonType("Torre");
        ButtonType bishopBtn = new ButtonType("Bispo");
        ButtonType knightBtn = new ButtonType("Cavalo");
        ButtonType pawnBtn = new ButtonType("Peão");
        ButtonType cancelBtn = ButtonType.CANCEL;

        alert.getButtonTypes().setAll(queenBtn, kingBtn, rookBtn, bishopBtn, knightBtn, pawnBtn,cancelBtn);

        // Mostra e espera pela escolha do utilizador
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() != cancelBtn) {
            selectedColor = colorBox.getValue();
            if (result.get() == queenBtn) {
                return PieceType.QUEEN;
            }
            else if (result.get() == kingBtn) {
                return PieceType.KING;
            } else if (result.get() == rookBtn) {
                return PieceType.ROOK;
            } else if (result.get() == bishopBtn) {
                return PieceType.BISHOP;
            } else if (result.get() == knightBtn) {
                return PieceType.KNIGHT;
            }else if (result.get() == pawnBtn) {
                return PieceType.PAWN;
            }
        }
        return null;
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
        boardui = new BoardUI(800,800,game);

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
        soundButton.setText("Sound: " + boardui.getSound());
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

                boardui.setWidth(center.getWidth());
                boardui.setHeight(center.getHeight());

                game.initGame(player1, player2);
                game.setCurrentPlayer(PieceColor.WHITE);
                game.setNumMovements(0);
                editor = false;
            }
        });

        mnEditor.setOnAction(e -> {
            player1 = askPlayerName("Jogador 1 (Pretas)");
            player2 = askPlayerName("Jogador 2 (Brancas)");

            if(player1 != null || player2 != null) {
                gameOver = false;
                gameDraw = false;

                boardui.setWidth(center.getWidth());
                boardui.setHeight(center.getHeight());

                game.initGameEmpty(player1, player2);
                editor = true;
                game.setCurrentPlayer(PieceColor.WHITE);
                game.setNumMovements(0);
            }
        });

        mnOpen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Carregar Jogo");

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Ficheiro do Jogo (*.txt)", "*.txt");
            FileChooser.ExtensionFilter extFilterCsv = new FileChooser.ExtensionFilter("Ficheiro do Jogo (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().addAll(extFilter, extFilterCsv);

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
            FileChooser.ExtensionFilter extFilterCsv = new FileChooser.ExtensionFilter("Ficheiro do Jogo (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().addAll(extFilter, extFilterCsv);

            File file = fileChooser.showSaveDialog(stage);

            boolean temp = false;
            if(editor){
                temp = boardui.checkKings();
                if(!temp){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erro ao salvar jogo");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("É necessário ter um rei para cada cor");
                    errorAlert.showAndWait();
                    return;
                }
            }

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
            FileChooser.ExtensionFilter extFilterCsv = new FileChooser.ExtensionFilter("Ficheiro do Jogo (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().addAll(extFilter, extFilterCsv);

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
            FileChooser.ExtensionFilter extFilterCsv = new FileChooser.ExtensionFilter("Ficheiro do Jogo (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().addAll(extFilter, extFilterCsv);

            File file = fileChooser.showOpenDialog(stage);

            boolean temp = false;
            if(editor){
                temp = boardui.checkKings();
                if(!temp){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erro ao salvar jogo");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("É necessário ter um rei para cada cor");
                    errorAlert.showAndWait();
                    return;
                }
            }
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
            Point2D localCoords = boardui.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            if(editor){
                PieceType piece = getPieceAdd();
                boardui.addPiece(localCoords.getX(), localCoords.getY(), piece, selectedColor);
            }
            else {
                boardui.onPressed(localCoords.getX(), localCoords.getY(), gameOver, gameDraw);
            }
        });

        game.addPropertyChangeListener(
            game.GAME_VALUE, evt -> {
                    update();
        });

        game.addPropertyChangeListener(
            game.PLAYER_VALUE, evt -> {
                    labelsInfo();
        });

        game.addPropertyChangeListener(
                game.EDITOR_VALUE, evt -> {
                    boardui.createCanvas();
                });

        boardui.addPropertyChangeListener(
                boardui.PROMOTE_VALUE, evt -> {
                    promotePawn();
                });

        soundButton.setOnAction(e -> {
            if(boardui.getSound().equals("ON")) {
                boardui.setSounds(false);
                soundButton.setText("Sound: " + boardui.getSound());
            }
            else {
                boardui.setSounds(true);
                soundButton.setText("Sound: " + boardui.getSound());
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
        System.out.println("check: " + game.check(playerTurn) + "\ncheckMate: " + game.checkMate());
        if (game.check(playerTurn) && game.checkMate()) {
            gameOver = true;
//            if (game.checkMate()) {
//                gameOver = true;
//            }
        }
        if (game.drownedKing() && game.getNumMovements() > 10) {
            gameDraw = true;
        }
        if (game.lackOfMaterial() && game.getNumMovements() > 10) {
            gameDraw = true;
        }
    }
}
