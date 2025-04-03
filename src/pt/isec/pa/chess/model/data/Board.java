package pt.isec.pa.chess.model.data;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Piece> Pieces;

    public Board() {
        initGame();
    }

    public void initGame(){
        Pieces = new ArrayList<>();
        Pieces.add(PieceType.ROOK.createPiece(this, 1, ColumnType.h,  PieceColor.BLACK));
        Pieces.add(PieceType.KNIGHT.createPiece(this, 1, ColumnType.g, PieceColor.BLACK));
        Pieces.add(PieceType.BISHOP.createPiece(this, 1, ColumnType.f, PieceColor.BLACK));
        Pieces.add(PieceType.QUEEN.createPiece(this, 1, ColumnType.e, PieceColor.BLACK));
        Pieces.add(PieceType.KING.createPiece(this, 1, ColumnType.d, PieceColor.BLACK));
        Pieces.add(PieceType.BISHOP.createPiece(this, 1, ColumnType.c, PieceColor.BLACK));
        Pieces.add(PieceType.KNIGHT.createPiece(this, 1, ColumnType.b, PieceColor.BLACK));
        Pieces.add(PieceType.ROOK.createPiece(this, 1, ColumnType.a, PieceColor.BLACK));

        for(ColumnType c : ColumnType.values()){
            Pieces.add(PieceType.PAWN.createPiece(this, 2, c, PieceColor.BLACK));
            Pieces.add(PieceType.PAWN.createPiece(this, 7, c, PieceColor.WHITE));
        }

        Pieces.add(PieceType.ROOK.createPiece(this, 8, ColumnType.h, PieceColor.WHITE));
        Pieces.add(PieceType.KNIGHT.createPiece(this, 8, ColumnType.g, PieceColor.WHITE));
        Pieces.add(PieceType.BISHOP.createPiece(this, 8, ColumnType.f, PieceColor.WHITE));
        Pieces.add(PieceType.QUEEN.createPiece(this, 8, ColumnType.e, PieceColor.WHITE));
        Pieces.add(PieceType.KING.createPiece(this, 8, ColumnType.d, PieceColor.WHITE));
        Pieces.add(PieceType.BISHOP.createPiece(this, 8, ColumnType.c, PieceColor.WHITE));
        Pieces.add(PieceType.KNIGHT.createPiece(this, 8, ColumnType.b, PieceColor.WHITE));
        Pieces.add(PieceType.ROOK.createPiece(this, 8, ColumnType.a, PieceColor.WHITE));
    }

    public void show(){
        Piece p;
        for(int i = 1; i <= 8; i++){
            for(ColumnType c : ColumnType.values()){
                p = getPiece(i, c);
                if(p != null){
                    System.out.print(p.getPosition() + " ");
                }
                else {
                    System.out.print(" __ ");
                }
            }
            System.out.println("\n");
        }
    }

    boolean move(Piece piece, int row, ColumnType col) {
        piece.setRow(row);
        piece.setCol(col);
        return true;
    }

    Piece getPiece(int row, ColumnType col) {
        for(Piece p: Pieces) {
            if(p.getRow() == row && p.getColumn() == col){
                return p;
            }
        }
        return null;
    }

    public List<String> getPossibleMoves(int row, ColumnType col) {
        return getPiece(row, col) == null ? new ArrayList<>(): getPiece(row, col).possibleMoves();
    }

    public boolean checkPiecesPosition(int row, ColumnType col) {
        for(Piece p: Pieces) {
            if(p.getRow() == row && p.getColumn().equivalente() == col.equivalente()){
                return true;
            }
        }
        return false;
    }

    public PieceColor checkColorPosition(int row, ColumnType col) {
        for(Piece p: Pieces) {
            if(p.getRow() == row && p.getColumn() == col){
                return p.getColor();
            }
        }
        return null;
    }

    String getPos(Piece piece) {
        return piece.getPosition();
    }

    String getPos(PieceType pieceType, PieceColor color) {
        StringBuilder sb = new StringBuilder();
        for (Piece p: Pieces) {
            if (p.getPieceType() == pieceType && p.getColor() == color) {
                sb.append(p.getPosition()).append(" ");
            }
        }
        return sb.toString();
    }

    boolean remove(int row, ColumnType col) {
        if (row < 1 || row > 8) {
            return false;
        }

        for (Piece p: Pieces) {
            if (p.getRow() == row && p.getColumn() == col) {
                Pieces.remove(p);
                return true;
            }
        }
        return false;
    }

    boolean remove(Piece piece) {
        return Pieces.remove(piece);
    }
}
