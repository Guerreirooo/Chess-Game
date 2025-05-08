package pt.isec.pa.chess.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.pieces.MoveType;
import pt.isec.pa.chess.ui.res.images.ImageManager;

public class BoardUI extends Canvas {
    String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    boolean selecionado = false;
    ChessGameManager game;
    double xi, yi, xf, yf;

    BoardUI(int x, int y, ChessGameManager game) {
        setWidth(x);
        setHeight(y);
        this.game = game;
    }

    void createCanvas() {
        int SquareSize = 70;
        int boardSize = game.getBoardSize();
        int labelMargin = SquareSize;
        int canvasSize = SquareSize * boardSize + 2 * labelMargin;

        this.setWidth(canvasSize);
        this.setHeight(canvasSize);

        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFont(new Font("Arial", 20));
        gc.setFill(Color.BLACK);

        for (int i = 0; i < boardSize; i++) {
            gc.fillText(letters[i], labelMargin + i * SquareSize + labelMargin / 2.0 - 5, labelMargin / 2.0 + 30); // topo
            gc.fillText(letters[i], labelMargin + i * SquareSize + labelMargin / 2.0 - 5, canvasSize - labelMargin / 2.0 - 20); // fundo
        }

        for (int i = 0; i < boardSize; i++) {
            gc.fillText(String.valueOf(boardSize - i), labelMargin / 2.0 + 20, labelMargin + i * SquareSize + labelMargin / 2.0 + 5); // esquerda
            gc.fillText(String.valueOf(boardSize - i), canvasSize - labelMargin / 2.0 - 30, labelMargin + i * SquareSize + labelMargin / 2.0 + 5); // direita
        }

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                String color = (row + col) % 2 == 0 ? "#EEEED2" : "#769656";
                String image = game.getPieceImage(row, col);

                gc.setFill(javafx.scene.paint.Color.web(color));
                gc.fillRect(labelMargin + col * SquareSize, labelMargin + row * SquareSize, SquareSize, SquareSize);
                if(image != null) {
                    gc.drawImage(ImageManager.getImage(image), labelMargin + col * SquareSize, labelMargin + row * SquareSize, 70, 70);
                }
            }
        }
        gc.strokeRect(labelMargin, labelMargin, SquareSize * boardSize, SquareSize * boardSize);
    }

    Canvas getCanvas() {
        return this;
    }

    public void onPressed(double mouseX, double mouseY, boolean gameOver, boolean gameDraw) {
        if (gameOver) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fim do Jogo");
            alert.setHeaderText(null);
            alert.setContentText(game.getCurrentPlayer() + " venceu por xeque-mate!");
            alert.showAndWait();
            return;
        }

        if (gameDraw) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fim do Jogo");
            alert.setHeaderText(null);
            alert.setContentText("Empate!");
            alert.showAndWait();
            return;
        }

        xf = xi;
        yf = yi;
        xi = mouseX;
        yi = mouseY;
        System.out.println("xi: " + xi + " yi: " + yi);
        if(selecionado){
            // System.out.println(game.move(xf,yf,xi,yi));
            System.out.println(false);
            selecionado = false;
            if (game.move(xf,yf,xi,yi) == MoveType.PROMOTE) {
            }
            createCanvas();
        }
        else{
            seleciona(xi,yi);
        }
    }

    private void seleciona(double xi, double yi) {
        int tabuleiroX = 170;
        int tabuleiroY = 144;
        int SquareSize = 70;

        double localX = xi - tabuleiroX;
        double localY = yi - tabuleiroY;

        int col = (int)(localX / SquareSize) + 1;
        int row = (int)(localY / SquareSize) + 1;

        System.out.println("Peca: " + game.getPiece(row, col));

        if (!game.getPiece(row, col)) {
            return;
        }

        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.rgb(255, 0, 0, 0.3));
        System.out.println("col: " + tabuleiroX + col * SquareSize + " row: " + tabuleiroY + row * SquareSize);
        gc.fillRect(col * SquareSize, row * SquareSize, SquareSize, SquareSize);

        selecionado = true;
    }
}
