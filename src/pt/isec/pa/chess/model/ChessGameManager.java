package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.*;
import pt.isec.pa.chess.model.data.pieces.MoveType;
import pt.isec.pa.chess.model.data.pieces.Piece;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class ChessGameManager {
    private ChessGame game;
    private int tabuleiroX = 170;
    private int tabuleiroY = 144;
    private int SquareSize = 70;
    private static int numMovements = 0;
    PropertyChangeSupport pcs;
    public static final String GAME_VALUE = "game";
    public static final String PLAYER_VALUE = "player";

    public ChessGameManager() {
        game = new ChessGame();
        pcs = new PropertyChangeSupport(this);
    }

    public ChessGameManager(ChessGame game) {
        this.game = game;
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property,listener);
    }

    public void alterarValores(ChessGame temp) {
        game.alterarValores(temp);
        pcs.firePropertyChange(GAME_VALUE, null, null);
    }

    public int getNumMovements(){return numMovements;}

    public void setNumMovements(int newNum){numMovements = newNum;}

    public void incNumMovements(){numMovements++;}

    public void initGame(){
        game.initGame();
        pcs.firePropertyChange(GAME_VALUE, null, null);
        ModelLog.getInstance().log("Jogo iniciado");
    }

    public void initGame(String blackName, String whiteName){
        game.setBlackName(blackName);
        game.setWhiteName(whiteName);
        initGame();
        pcs.firePropertyChange(GAME_VALUE, null, null);
    }

    public void setBlackName(String blackName){
        game.setBlackName(blackName);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
    }

    public void setWhiteName(String whiteName){
        game.setWhiteName(whiteName);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
    }

    public void setCurrentPlayer(PieceColor color){
        game.setCurrentPlayer(color);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
    }

    public PieceColor getCurrentPlayer(){
        return game.getCurrentPlayer();
    }

    public String getPieceImage(int row, int col) {
        Piece temp;
        int rowCorrigida = row + 1;
        int colCorrigida = col + 1;
        ColumnType tempCol = ColumnType.letra(colCorrigida);
        temp = game.getPiece(rowCorrigida,tempCol);

        if(temp == null){
            return null;
        }
        else if(temp.getColor() == PieceColor.WHITE){
            if(temp.getPieceType() == PieceType.PAWN){
                return "pawnW.png";
            }
            else if(temp.getPieceType() == PieceType.KNIGHT){
                return "knightW.png";
            }
            else if(temp.getPieceType() == PieceType.BISHOP){
                return "bishopW.png";
            }
            else if(temp.getPieceType() == PieceType.ROOK){
                return "rookW.png";
            }
            else if(temp.getPieceType() == PieceType.QUEEN){
                return "queenW.png";
            }
            else if(temp.getPieceType() == PieceType.KING){
                return "kingW.png";
            }
        }
        else if(temp.getColor() == PieceColor.BLACK){
            if(temp.getPieceType() == PieceType.PAWN){
                return "pawnB.png";
            }
            else if(temp.getPieceType() == PieceType.KNIGHT){
                return "KnightB.png";
            }
            else if(temp.getPieceType() == PieceType.BISHOP){
                return "bishopB.png";
            }
            else if(temp.getPieceType() == PieceType.ROOK){
                return "rookB.png";
            }
            else if(temp.getPieceType() == PieceType.QUEEN){
                return "queenB.png";
            }
            else if(temp.getPieceType() == PieceType.KING){
                return "kingB.png";
            }
        }
        return null;
    }

    public int getBoardSize(){
        return game.getBoardSize();
    }

    @Override
    public String toString(){
        return game.toString();
    }

    public boolean exportGame(String fileName) {
        if (!game.savePartialGame(fileName)) {
            ModelLog.getInstance().log("Erro ao exportar o jogo em " + fileName);
            return false;
        }
        return true;
    }

    public boolean importGame(String fileName) {
        boolean result = game.loadPartialGame(fileName);
        if(result) {
            pcs.firePropertyChange(PLAYER_VALUE, null, null);
            pcs.firePropertyChange(GAME_VALUE, null, null);
        }
        else {
            ModelLog.getInstance().log("Erro ao carregar o jogo em " + fileName);
        }
        return result;
    }

    public boolean load(String fileName) {
        ChessGame temp = ChessGameSerialization.load(fileName);
        if (temp == null) {
            ModelLog.getInstance().log("Erro ao carregar o jogo em " + fileName);
            return false;
        }
        alterarValores(temp);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
        return true;
    }

    public boolean save(String fileName) {
        if (!ChessGameSerialization.save(fileName, game)) {
            ModelLog.getInstance().log("Erro ao guardar o jogo em " + fileName);
            return false;
        }
        return true;
    }

    int getRow(double col) {
        double localY = col - tabuleiroY;
        int rowTemp = (int)(localY / SquareSize) + 1;

        return rowTemp;
    }

    ColumnType getColumn(double row) {
        double localX = row - tabuleiroX;

        int colTemp = (int)(localX / SquareSize) + 1;

        ColumnType colType = ColumnType.letra(colTemp);

        return colType;
    }

    public boolean getPiece(int row, int col) {
        if (game.getPiece(row, ColumnType.letra(col)) != null) {
            return true;
        }
        return false;
    }

    public MoveType move(double rowPiece, double colPiece, double row, double col) {
        MoveType result = game.move(getRow(colPiece), getColumn(rowPiece), getRow(col), getColumn(row));
        if(result != MoveType.FALSE){
            ModelLog.getInstance().log("Movimento: de " + getColumn(rowPiece) + mudarNumeros(getRow(colPiece)) + " para " + getColumn(row) + mudarNumeros(getRow(col)));
            incNumMovements();
            pcs.firePropertyChange(PLAYER_VALUE, null, null);
        }
        pcs.firePropertyChange(GAME_VALUE, null, null);


        return result;
    }

    private int mudarNumeros(int x) {
        switch (x) {
            case 1:
                return 8;
            case 2:
                return 7;
            case 3:
                return 6;
            case 4:
                return 5;
            case 5:
                return 4;
            case 6:
                return 3;
            case 7:
                return 2;
            case 8:
                return 1;
        }
        return x;
    }

    public void getTypePromote(double rowPiece, double colPiece, String pt){
        game.getTypePromote(getRow(colPiece), getColumn(rowPiece), PieceType.translate(pt));
        pcs.firePropertyChange(GAME_VALUE, null, null);
    }

    public boolean checkStopCheckMate(PieceColor kingColor) {
        return game.checkStopCheckMate(kingColor);
    }

    public boolean drownedKing(){
        return game.drownedKing();
    }

    public boolean lackOfMaterial(){
        return game.lackOfMaterial();
    }

    public boolean checkMate(){
        if (game.checkMate()) {
            ModelLog.getInstance().log("CheckMate de " + getCurrentPlayer());
            return true;
        }
        return false;
    }

    /*
    *   MAL IMPLEMENTADO
    *
    private void getTypePromote(Piece p){
        String Type;
        PieceType pt = null;
        Piece retorno = null;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Promocao de peao! Nova peca : [QUEEN,ROOK,BISHOP,KNIGHT]\n");
            Type = sc.nextLine();
            if(Type.equalsIgnoreCase("QUEEN") || Type.equalsIgnoreCase("ROOK") || Type.equalsIgnoreCase("BISHOP") || Type.equalsIgnoreCase("KNIGHT")){
                pt = PieceType.translate(Type);
            }
        }
        while(!board.promotePawn(p, pt));
    }*/

    /*
    *   NAO SEI COMO FAZER */

    public boolean gameOver(){
        if(game.gameOver()){
            return true;
        }
        return false;
    }

    public boolean check(String playerTurn){
        if(game.check(playerTurn)){
            return true;
        }
        return false;
    }
}
