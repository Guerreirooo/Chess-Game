package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.*;
import pt.isec.pa.chess.model.data.pieces.MoveType;
import pt.isec.pa.chess.model.data.pieces.Piece;

import java.io.*;
import java.util.Scanner;

public class ChessGameManager {
    private ChessGame game;
    private int tabuleiroX = 170;
    private int tabuleiroY = 144;
    private int SquareSize = 70;

    public ChessGameManager() {
        game = new ChessGame();
    }

    public ChessGameManager(ChessGame game) {
        this.game = game;
    }

    public void alterarValores(ChessGame temp) {
        game.alterarValores(temp);
    }

    public void initGame(){
        game.initGame();
    }

    public void initGame(String blackName, String whiteName){
        game.setBlackName(blackName);
        game.setWhiteName(whiteName);
        game.initGame();
    }

    public void setBlackName(String blackName){
        game.setBlackName(blackName);
    }

    public void setWhiteName(String whiteName){
        game.setWhiteName(whiteName);
    }

    void setCurrentPlayer(PieceColor color){
        game.setCurrentPlayer(color);
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
        return game.savePartialGame(fileName);
    }

    public boolean importGame(String fileName) {
        return game.loadPartialGame(fileName);
    }

    public boolean load(String fileName) {
        ChessGame temp = ChessGameSerialization.load(fileName);
        if (temp == null) {
            return false;
        }
        alterarValores(temp);
        return true;
    }

    public boolean save(String fileName) {
        return ChessGameSerialization.save(fileName, game);
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
        return game.move(getRow(colPiece), getColumn(rowPiece), getRow(col), getColumn(row));
    }

    public void getTypePromote(double rowPiece, double colPiece, String pt){
        game.getTypePromote(getRow(colPiece), getColumn(rowPiece), PieceType.translate(pt));
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
    *   NAO SEI COMO FAZER
    *
    public void gameOver(){
        if(board.checkMate()){
            System.out.println(playerColor + " wins!");
        }
    }*/
}
