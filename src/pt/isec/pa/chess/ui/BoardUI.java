package pt.isec.pa.chess.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.ColumnType;
import pt.isec.pa.chess.model.data.pieces.MoveType;
import pt.isec.pa.chess.model.data.pieces.Piece;
import pt.isec.pa.chess.ui.res.images.ImageManager;

import java.util.List;

public class BoardUI extends Canvas {
    String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    boolean selecionado = false;
    boolean possibleMoves = false;
    ChessGameManager game;
    double xi, yi, xf, yf;
    int SquareSize = 70;

    BoardUI(int x, int y, ChessGameManager game) {
        setWidth(x);
        setHeight(y);
        this.game = game;
    }

    public void setPossibleMoves(boolean newValue){
        this.possibleMoves = newValue;
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
            alert.setContentText(game.getCurrentPlayer() + " perdeu por xeque-mate!");
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

        if(selecionado){
            selecionado = false;
            if (game.move(xf,yf,xi,yi) == MoveType.PROMOTE) {
            }
        }
        else{
            int row = convertCordinatesY(xi);
            int col = convertCordinatesX(yi);
            if(seleciona(col,row) && possibleMoves == true) {
                selecionaPossibleMoves(col, row);
            }
            selecionado = true;
        }
    }

    private boolean seleciona(int xi, int yi) {
        if(game.havePiece(xi, yi)){
            Piece pieceAux = game.getPiece(xi, yi);
            if (pieceAux == null){
                return false;
            }
            if(game.getCurrentPlayer().equals(pieceAux.getColor())){
                GraphicsContext gc = getGraphicsContext2D();
                gc.setFill(Color.rgb(255, 0, 0, 0.3));
                gc.fillRect(yi * SquareSize, xi * SquareSize, SquareSize, SquareSize);
                return true;
            }
            return false;
        }
        return false;
    }

    private int convertCordinatesY(double xi) {
        int tabuleiroX = 170;

        double localY = xi - tabuleiroX;

        int col = (int)(localY / SquareSize) + 1;
        return col;
    }

    private int convertCordinatesX(double yi) {
        int tabuleiroY = 144;

        double localX = yi - tabuleiroY;

        int row = (int)(localX / SquareSize) + 1;
        return row;
    }

    private void selecionaPossibleMoves(int row, int col) {
        Piece temp = game.getPiece(row,col);
        List<String> tempMoves = temp.getPossibleMoves();
        ColumnType colunaTemp;
        String coluna;
        int colunaNum;
        int linha;

        for(String t : tempMoves){
            coluna = t.substring(0,1);
            colunaTemp = ColumnType.letra(coluna);
            colunaNum = colunaTemp.equivalente();
            linha = Integer.parseInt(t.substring(1));

            GraphicsContext gc = getGraphicsContext2D();
            gc.setFill(Color.rgb(255, 0, 0, 0.3));
            gc.fillRect(colunaNum * SquareSize, linha * SquareSize, SquareSize, SquareSize);
        }
    }
}
