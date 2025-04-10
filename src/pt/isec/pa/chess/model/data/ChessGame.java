package pt.isec.pa.chess.model.data;

import java.io.*;
import java.util.Scanner;

public class ChessGame implements Serializable {
    public Board board;
    private PieceColor playerColor;

    @Serial
    private static final long serialVersionUID = 1L;

    public ChessGame(){
        board = new Board();
        setCurrentPlayer(PieceColor.WHITE);
    }

    void setCurrentPlayer(PieceColor color){
        playerColor = color;
    }

    public PieceColor getCurrentPlayer(){
        return playerColor;
    }

    @Override
    public String toString(){
        return board.toString();
    }

    public boolean move(int rowPiece, ColumnType colPiece, int row, ColumnType col) {
        Piece piece = board.getPiece(rowPiece, colPiece);

        if (row > 8 || row < 1 || piece == null) {
            return false;
        }

        PieceColor color = board.checkColorPosition(row, col);
        String nextMove = col.toString() + row;

        if(piece.getPieceType() == PieceType.PAWN && piece.getColor() == PieceColor.BLACK && row == 8){
            if(board.move(piece, row, col)){
                getTypePromote(piece);
            }
            return true;
        }
        else if(piece.getPieceType() == PieceType.PAWN && piece.getColor() == PieceColor.WHITE && row == 1){
            if(board.move(piece, row, col)){
                getTypePromote(piece);
            }
            return true;
        }

        if(piece.getPieceType() == PieceType.KING && color == PieceColor.BLACK){
            if(row == 1 && col == ColumnType.h){
                if (board.roque(piece,board.getPiece(row,col))){
                    piece.setCol(ColumnType.g);
                    board.getPiece(row,col).setCol(ColumnType.f);
                    return true;
                }
            }
            else if(row == 1 && col == ColumnType.a){
                if (board.roque(piece,board.getPiece(row,col))){
                    piece.setCol(ColumnType.c);
                    board.getPiece(row,col).setCol(ColumnType.d);
                    return true;
                }
            }
        }
        else if(piece.getPieceType() == PieceType.KING && color == PieceColor.WHITE) {
            if (row == 8 && col == ColumnType.h) {
                if (board.roque(piece, board.getPiece(row, col))) {
                    piece.setCol(ColumnType.g);
                    board.getPiece(row, col).setCol(ColumnType.f);
                    return true;
                }
            } else if (row == 8 && col == ColumnType.a) {
                if (board.roque(piece, board.getPiece(row, col))) {
                    piece.setCol(ColumnType.c);
                    board.getPiece(row, col).setCol(ColumnType.d);
                    return true;
                }
            }
        }
        return board.move(piece, row, col);
    }

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
    }

    public void gameOver(){
        if(board.checkMate()){
            System.out.println(playerColor + " wins!");
        }
    }
}
