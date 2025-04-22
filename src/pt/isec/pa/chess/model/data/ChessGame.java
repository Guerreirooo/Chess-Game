package pt.isec.pa.chess.model.data;

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
        System.out.println(board);
    }

    public void initGame(String blackName, String whiteName){
        black = new Player(PieceColor.BLACK, blackName);
        white = new Player(PieceColor.WHITE, whiteName);
        board.initGame();
        System.out.println(board);
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
