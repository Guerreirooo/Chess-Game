package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.*;
import pt.isec.pa.chess.model.data.pieces.*;

import java.io.*;
import java.util.Scanner;

public class ChessGame implements Serializable {
    private Board board;
    private Player white, black;
    private PieceColor playerColor;

    @Serial
    private static final long serialVersionUID = 1L;

    public ChessGame(){
        board = new Board();
        setCurrentPlayer(PieceColor.WHITE);
    }

    public void alterarValores(ChessGame temp) {
        board = temp.board;
        white = temp.white;
        black = temp.black;
        playerColor = temp.playerColor;
    }

    public void initGame(){
        board.initGame();
    }

    public void initGame(String blackName, String whiteName){
        setBlackName(blackName);
        setWhiteName(whiteName);
        board.initGame();
    }

    public void setBlackName(String blackName){
        black = new Player(PieceColor.BLACK, blackName);
    }

    public void setWhiteName(String whiteName){
        white = new Player(PieceColor.WHITE, whiteName);
    }

    void setCurrentPlayer(PieceColor color){
        playerColor = color;
    }

    public PieceColor getCurrentPlayer(){
        return playerColor;
    }

    public Piece getPiece(int row, ColumnType col) {
        return board.getPiece(row,col);
    }
    @Override
    public String toString(){
        return board.toString();
    }

    public boolean savePartialGame(String fileName) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

            if (getCurrentPlayer() == null || board == null) {
                return false;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(getCurrentPlayer()).append(",\n");
            for (Piece p: board.getPiecesList()) {
                sb.append(p.toString()).append(",");
            }

            pw.write(sb.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pw != null)
                pw.close();
        }
    }

    public boolean loadPartialGame(String fileName) {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            StringBuilder data = new StringBuilder();

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                data.append(line);
            }

            System.out.println(data.toString());

            if (data.isEmpty()) {
                return false;
            }

            ChessGame chessgame = new ChessGame();
            Board b = new Board();
            int length = data.toString().split(",").length;

            if (length < 4) {
                return false;
            }

            PieceColor color = PieceColor.translate(data.toString().split(",")[0]);

            if (color == null) {
                return false;
            }

            for (String peca : data.toString().split(",")) {
                b.addPiece(peca);
            }

            chessgame.board = b;
            chessgame.setCurrentPlayer(color);

            this.board = chessgame.board;
            this.setCurrentPlayer(chessgame.getCurrentPlayer());
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public MoveType move(int rowPiece, ColumnType colPiece, int row, ColumnType col) {
        Piece piece = board.getPiece(rowPiece, colPiece);

        if (piece.getColor() != getCurrentPlayer() || row > getBoardSize() || row < 1 || piece == null) {
            return MoveType.FALSE;
        }

        PieceColor color = board.checkColorPosition(row, col);
        String nextMove = col.toString() + row;

        if(piece.getPieceType() == PieceType.PAWN && piece.getColor() == PieceColor.BLACK && row == getBoardSize()){
            if(board.move(piece, row, col)){
                return MoveType.PROMOTE;
                // getTypePromote(piece);
            }
        }
        else if(piece.getPieceType() == PieceType.PAWN && piece.getColor() == PieceColor.WHITE && row == 1){
            if(board.move(piece, row, col)){
                return MoveType.PROMOTE;
                // getTypePromote(piece);
            }
            return MoveType.TRUE;
        }

        if(piece.getPieceType() == PieceType.KING && color == PieceColor.BLACK){
            if(row == 1 && col == ColumnType.h){
                if (board.roque(piece,board.getPiece(row,col))){
                    piece.setCol(ColumnType.g);
                    board.getPiece(row,col).setCol(ColumnType.f);
                    return MoveType.TRUE;
                }
            }
            else if(row == 1 && col == ColumnType.a){
                if (board.roque(piece,board.getPiece(row,col))){
                    piece.setCol(ColumnType.c);
                    board.getPiece(row,col).setCol(ColumnType.d);
                    return MoveType.TRUE;
                }
            }
        }
        else if(piece.getPieceType() == PieceType.KING && color == PieceColor.WHITE) {
            if (row == getBoardSize() && col == ColumnType.h) {
                if (board.roque(piece, board.getPiece(row, col))) {
                    piece.setCol(ColumnType.g);
                    board.getPiece(row, col).setCol(ColumnType.f);
                    return MoveType.TRUE;
                }
            } else if (row == getBoardSize() && col == ColumnType.a) {
                if (board.roque(piece, board.getPiece(row, col))) {
                    piece.setCol(ColumnType.c);
                    board.getPiece(row, col).setCol(ColumnType.d);
                    return MoveType.TRUE;
                }
            }
        }

        if (board.move(piece, row, col)) {
            if (getCurrentPlayer() == PieceColor.WHITE) {
                setCurrentPlayer(PieceColor.BLACK);
            }
            else {
                setCurrentPlayer(PieceColor.WHITE);
            }
            return MoveType.TRUE;
        }

        return MoveType.FALSE;
    }

    public boolean lackOfMaterial(){
        return board.lackOfMaterial();
    }

    public int getBoardSize(){
        return board.getBoardSize();
    }

    public void getTypePromote(int row, ColumnType col, PieceType pt){
        board.promotePawn(getPiece(row, col), pt);
    }

    public boolean gameOver(){
        if(board.checkMate()){
            System.out.println(playerColor + " wins!");
            return true;
        }
        return false;
    }

    public boolean checkStopCheckMate(PieceColor kingColor) {
        return board.checkStopCheckMate(kingColor);
    }

    public boolean drownedKing(){
        return board.drownedKing();
    }

    public boolean checkMate(){
        return board.checkMate();
    }

    public boolean check(String playerTurn){
        PieceColor temp;
        temp = PieceColor.cor(playerTurn);
        if(board.check(temp ,board)){
            return true;
        }
        return false;
    }
}
