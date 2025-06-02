package pt.isec.pa.chess.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.ColumnType;
import pt.isec.pa.chess.model.data.PieceColor;
import pt.isec.pa.chess.model.data.PieceType;
import pt.isec.pa.chess.model.data.pieces.MoveType;
import pt.isec.pa.chess.model.data.pieces.Piece;
import pt.isec.pa.chess.ui.res.SoundManager;
import pt.isec.pa.chess.ui.res.images.ImageManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class BoardUI extends Canvas {
    String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    boolean selecionado = false;
    boolean possibleMoves = false;
    boolean sounds = false;
    boolean promote = false;
    ChessGameManager game;
    double xi, yi, xf, yf;
    int SquareSize = 70;
    PropertyChangeSupport pcs;
    public static final String PROMOTE_VALUE = "promote";

    BoardUI(int x, int y, ChessGameManager game) {
        setWidth(x);
        setHeight(y);
        this.game = game;
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property,listener);
    }

    public void setPossibleMoves(boolean newValue){
        this.possibleMoves = newValue;
    }

    public String getSound(){
        if(sounds){
            return "ON";
        }
        return "OFF";
    }

    public boolean getPromote(){return promote;}

    public void setPromote(boolean promote){this.promote = promote;}

    public void setSounds(boolean sounds) {
        this.sounds = sounds;
    }

    public void findSounds(List<String> fileNames) {
        List<String> filesTemp = new ArrayList<>();
        List<String> lista = new ArrayList<>();
        lista.add(".mp3");
        lista.add(".wav");

        for(String filename : fileNames) {
            for (String s : lista) {
                if (SoundManager.getSound(filename + s)) {
                    filesTemp.add(filename + s);
                    break;
                }
            }
        }

        SoundManager.playSequence(filesTemp);
    }

    public void playSounds(Piece temp,ColumnType colOrigin,int rowOrigin,boolean capture) {
        List<String> fileNames = new ArrayList<>();
        fileNames.add(temp.getColor().toString().toLowerCase());
        fileNames.add(temp.getPieceType().toString().toLowerCase());
        fileNames.add(colOrigin.toString().toLowerCase());
        fileNames.add(rowOrigin + "");
        fileNames.add(temp.getColumn().toString().toLowerCase());
        fileNames.add(game.mudarNumeros(temp.getRow())+"");

        if(capture){
            fileNames.add("capture");
        }

        if(game.check(game.getCurrentPlayer().toString())){
            fileNames.add("check");
        }

        findSounds(fileNames);
    }

    void createCanvas() {
        int SquareSize = (int)getWidth()/10;
        int boardSize = game.getBoardSize();
        int labelMargin = SquareSize;
        int canvasSize = SquareSize * boardSize + 2 * labelMargin;

        this.setWidth(canvasSize);
        this.setHeight(canvasSize);

        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFont(new Font("Arial", 20));
        gc.setFill(Color.BLACK);

        for (int i = 0; i < boardSize; i++) {
            gc.fillText(letters[i], labelMargin + i * SquareSize + labelMargin / 2.0 - (this.getHeight()/140), labelMargin / 2.0 + (this.getHeight()/35)); // topo
            gc.fillText(letters[i], labelMargin + i * SquareSize + labelMargin / 2.0 - (this.getHeight()/140), canvasSize - labelMargin / 2.0 - (this.getHeight()/70)); // fundo
        }

        for (int i = 0; i < boardSize; i++) {
            gc.fillText(String.valueOf(boardSize - i), labelMargin / 2.0 , labelMargin + i * SquareSize + labelMargin / 2.0 + (this.getWidth()/140)); // esquerda
            gc.fillText(String.valueOf(boardSize - i), canvasSize - labelMargin / 2.0 - (this.getWidth()/70), labelMargin + i * SquareSize + labelMargin / 2.0 + (this.getWidth()/140)); // direita
        }

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                String color = (row + col) % 2 == 0 ? "#EEEED2" : "#769656";
                String image = game.getPieceImage(row, col);

                gc.setFill(javafx.scene.paint.Color.web(color));
                gc.fillRect(labelMargin + col * SquareSize, labelMargin + row * SquareSize, SquareSize, SquareSize);
                if(image != null) {
                    gc.drawImage(ImageManager.getImage(image), labelMargin + col * SquareSize, labelMargin + row * SquareSize, (int)this.getWidth()/10, (int)this.getWidth()/10);
                }
            }
        }
        gc.strokeRect(labelMargin, labelMargin, SquareSize * boardSize, SquareSize * boardSize);
    }

    Canvas getCanvas() {
        return this;
    }

    public void addPiece(double mouseX, double mouseY, PieceType piece, String selectedColor) {
        xi = convertCordinatesY(mouseX);
        yi = convertCordinatesX(mouseY);
        PieceColor temp = PieceColor.translate(selectedColor.toUpperCase());

        game.addPiece((int)xi,(int)yi,piece,temp);
    }

    public void onPressed(double mouseX, double mouseY, boolean gameOver, boolean gameDraw) {
        MoveType result;
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
        xi = convertCordinatesY(mouseX);
        yi = convertCordinatesX(mouseY);

        if(selecionado){
            selecionado = false;
            Piece temp = game.getPiece((int)yf,(int)xf);
            boolean capture = game.havePiece((int)yi,(int)xi);
            result = game.move(xf,yf,xi,yi);
            if(result != MoveType.FALSE) {
                if(sounds) {
                    playSounds(temp, ColumnType.letra((int)xf), game.mudarNumeros((int)yf), capture);
                }
                if (result == MoveType.PROMOTE) {
                    setPromote(true);
                    pcs.firePropertyChange(PROMOTE_VALUE, null, null);
                }
            }
        }
        else{
            int row = (int)xi;
            int col = (int)yi;
            if(seleciona(col,row) && possibleMoves == true) {
                selecionaPossibleMoves(col, row);
            }
            selecionado = true;
        }
    }

    public void promotePawn(PieceType newPiece){
        int row = (int)yi;
        int col = (int)xi;

        if(game.promotePawn(row,ColumnType.letra(col),newPiece))
            selecionado = false;
    }

    public boolean checkKings (){
        return game.checkKings();
    }

    private boolean seleciona(int xi, int yi) {
        int SquareSize = (int)this.getWidth()/10;
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

    private int convertCordinatesX(double localY) {
        int squareSize = (int) getHeight() / 10;
        int labelMargin = squareSize;

        double yInsideBoard = localY - labelMargin;
        int row = (int)(yInsideBoard / squareSize) + 1;

        return row;
    }

    private int convertCordinatesY(double localX) {
        int squareSize = (int) getWidth() / 10;
        int labelMargin = squareSize;

        double xInsideBoard = localX - labelMargin;
        int col = (int)(xInsideBoard / squareSize) + 1;

        return col;
    }


    private void selecionaPossibleMoves(int row, int col) {
        int SquareSize = (int)this.getWidth()/10;
        Piece temp = game.getPiece(row,col);
        List<String> tempMoves = game.getPossibleMoves(temp);
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
