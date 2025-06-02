package pt.isec.pa.chess.model.data;

import pt.isec.pa.chess.model.data.pieces.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    private List<Piece> Pieces;
    int boardSize = 8;

    @Serial
    private static final long serialVersionUID = 1L;

    public Board() {
        Pieces = new ArrayList<>();
        //initGame();
        //PieceType.KING.createPiece(this, new PiecePosition(4, ColumnType.h), PieceColor.BLACK);
    }

    public Board(Board b) {
        Pieces = new ArrayList<>();
        for (Piece p: b.Pieces) {
            addPiece(p.getPieceType(), p.getRow(), p.getColumn(), p.getColor());
        }
        //initGame();
        //PieceType.KING.createPiece(this, new PiecePosition(4, ColumnType.h), PieceColor.BLACK);
    }

    public void initGame(){
        Pieces = new ArrayList<>();
        addPiece(PieceType.ROOK, new PiecePosition(1, ColumnType.h),  PieceColor.BLACK);
        addPiece(PieceType.KNIGHT, new PiecePosition(1, ColumnType.g), PieceColor.BLACK);
        addPiece(PieceType.BISHOP, new PiecePosition(1, ColumnType.f), PieceColor.BLACK);
        addPiece(PieceType.QUEEN, new PiecePosition(1, ColumnType.d), PieceColor.BLACK);
        addPiece(PieceType.KING, new PiecePosition(1, ColumnType.e), PieceColor.BLACK);
        addPiece(PieceType.BISHOP, new PiecePosition(1, ColumnType.c), PieceColor.BLACK);
        addPiece(PieceType.KNIGHT, new PiecePosition(1, ColumnType.b), PieceColor.BLACK);
        addPiece(PieceType.ROOK, new PiecePosition(1, ColumnType.a), PieceColor.BLACK);

        for(ColumnType c : ColumnType.values()){
            addPiece(PieceType.PAWN, new PiecePosition(2, c), PieceColor.BLACK);
            addPiece(PieceType.PAWN, new PiecePosition(7, c), PieceColor.WHITE);
        }

        addPiece(PieceType.ROOK, new PiecePosition(8, ColumnType.h), PieceColor.WHITE);
        addPiece(PieceType.KNIGHT, new PiecePosition(8, ColumnType.g), PieceColor.WHITE);
        addPiece(PieceType.BISHOP, new PiecePosition(8, ColumnType.f), PieceColor.WHITE);
        addPiece(PieceType.KING, new PiecePosition(8, ColumnType.e), PieceColor.WHITE);
        addPiece(PieceType.QUEEN, new PiecePosition(8, ColumnType.d), PieceColor.WHITE);
        addPiece(PieceType.BISHOP, new PiecePosition(8, ColumnType.c), PieceColor.WHITE);
        addPiece(PieceType.KNIGHT, new PiecePosition(8, ColumnType.b), PieceColor.WHITE);
        addPiece(PieceType.ROOK, new PiecePosition(8, ColumnType.a), PieceColor.WHITE);
    }

    public void initGameEmpty(){
        Pieces = new ArrayList<>();
    }

    public void addPiece(PieceType pt, int row, ColumnType col, PieceColor color) {
        if (row < 1 || row > getBoardSize()) {
            return;
        }
        Pieces.add(pt.createPiece(this, new PiecePosition(row, col), color));
    }

    public int getBoardSize(){
        return boardSize;
    }

    void addPiece(PieceType pt, PiecePosition pos, PieceColor color) {
        Pieces.add(pt.createPiece(this, pos, color));
    }

    public void addPiece(String piece) {
        Piece temp = PieceType.createPiece(this, piece);
        if (temp != null) {
            Pieces.add(temp);
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Piece p;
        for(int i = 1; i <= getBoardSize(); i++){
            for(ColumnType c : ColumnType.values()){
                p = getPiece(i, c);
                if(p != null){
                    sb.append(p).append(" ");
                }
                else {
                    sb.append(" __ ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean moveWithoutConfirmation(Piece piece, int row, ColumnType col) {
        if (row > getBoardSize() || row < 1 || piece == null) {
            return false;
        }

        PieceColor color = checkColorPosition(row, col);
        String nextMove = col.toString() + row;

        if (piece.getPieceType() == PieceType.PAWN && ((row == 2 && piece.getColor() == PieceColor.WHITE) || (row == 7 && piece.getColor() == PieceColor.BLACK))) {
            if (checkColorPosition(row, col) != piece.getColor()) {
                remove(getPiece(row, col));
            }
            PiecePosition positionTemp = new PiecePosition(row, col);
            PieceColor colorTemp = piece.getColor();
            remove(piece);
            addPiece(PieceType.PAWN, positionTemp, colorTemp);
            return true;
        }

        if (color == null) {
            piece.setRow(row);
            piece.setCol(col);
            return true;
        }
        else if (color == piece.getColor()) {
            return false;
        }
        else if (checkColorPosition(row, col) != piece.getColor()) {
            remove(getPiece(row, col));
            piece.setRow(row);
            piece.setCol(col);
            return true;
        }

        return true;
    }

    public boolean move(Piece piece, int row, ColumnType col, boolean isCheck) {
        if (row > getBoardSize() || row < 1 || piece == null) {
            return false;
        }

        PieceColor color = checkColorPosition(row, col);
        String nextMove = col.toString() + row;

        for (String moves: getPossibleMoves(piece.getRow(), piece.getColumn(), isCheck)) {
            if (moves.equals(nextMove) && color == null) {
                piece.setRow(row);
                piece.setCol(col);
                return true;
            }
            else if (moves.equals(nextMove) && color == piece.getColor()) {
                return false;
            }
            else if (moves.equals(nextMove) && checkColorPosition(row, col) != piece.getColor()) {
                remove(getPiece(row, col));
                piece.setRow(row);
                piece.setCol(col);
                return true;
            }
        }

        return false;
    }

    public Piece getPiece(int row, ColumnType col) {
        for(Piece p: Pieces) {
            if(p.getRow() == row && p.getColumn() == col){
                return p;
            }
        }
        return null;
    }

    public List<String> getPossibleMoves(int row, ColumnType col, boolean isCheck) {
        if (isCheck) {
            List<String> possibleMovesPiece = getPossibleMoves(getPiece(row, col));
            List<String> movesToRunCheckmate = checkStopCheckMate(getPiece(row, col).getColor());
            List<String> finalMoves = new ArrayList<>();

            if (getPiece(row, col).getPieceType() == PieceType.KING) {
                finalMoves.addAll(checkStopCheckMateKing(getPiece(row, col).getColor()));
                return finalMoves;
            }

            for (String move: possibleMovesPiece) {
                for (String moveToRun: movesToRunCheckmate) {
                    if (move.equals(moveToRun)) {
                        finalMoves.add(move);
                    }
                }
            }
            return finalMoves;
        }
        return getPiece(row, col) == null ? new ArrayList<>(): getPiece(row, col).getPossibleMoves();
    }

    public List<String> getPossibleMoves(Piece p){
        Board bTemp = new Board(this);
        Board bTempCopy = new Board(bTemp);
        List<String> possibleMoves = new ArrayList<>();

        for (String move: p.getPossibleMoves()) {
            ColumnType col = ColumnType.letra(move.split("")[0]);
            int row = Integer.parseInt(move.split("")[1]);
            bTempCopy.moveWithoutConfirmation(bTempCopy.getPiece(p.getRow(), p.getColumn()), row, col);
            if (!check(p.getColor(), bTempCopy)) {
                possibleMoves.add(move);
            }
            bTempCopy = new Board(bTemp);
        }
        return possibleMoves;
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
        return piece.toString();
    }

    PiecePosition getPos(PieceType pieceType, PieceColor color) {
        PiecePosition position;
        for (Piece p: Pieces) {
            if (p.getPieceType() == pieceType && p.getColor() == color) {
                return new PiecePosition(p.getRow(), p.getColumn());
            }
        }
        return null;
    }

    public boolean checkMate() {
        for (Piece p : Pieces) {
            if (p.getPieceType() == PieceType.KING) {
                if(checkStopCheckMate(p.getColor()).isEmpty() && checkStopCheckMateKing(p.getColor()).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> checkStopCheckMateKing(PieceColor kingColor) {
        Board bTemp = new Board(this);
        Board bTempCopy = new Board(bTemp);
        List<String> possibleMovesChecked = new ArrayList<>();

        for (Piece p: bTemp.getPiecesList()) {
            if (p.getColor() != kingColor || p.getPieceType() != PieceType.KING) {
                continue;
            }

            for (String move: p.getPossibleMoves()) {
                ColumnType col = ColumnType.letra(move.split("")[0]);
                int row = Integer.parseInt(move.split("")[1]);
                bTempCopy.moveWithoutConfirmation(bTempCopy.getPiece(p.getRow(), p.getColumn()), row, col);
                if (!check(kingColor, bTempCopy)) {
                    possibleMovesChecked.add(move);
                }
                bTempCopy = new Board(bTemp);
            }
        }
        return possibleMovesChecked;
    }

    public List<String> checkStopCheckMate(PieceColor kingColor) {
        Board bTemp = new Board(this);
        Board bTempCopy = new Board(bTemp);
        List<String> possibleMovesChecked = new ArrayList<>();

        for (Piece p: bTemp.getPiecesList()) {
            if (p.getColor() != kingColor || p.getPieceType() == PieceType.KING) {
                continue;
            }

            for (String move: p.getPossibleMoves()) {
                ColumnType col = ColumnType.letra(move.split("")[0]);
                int row = Integer.parseInt(move.split("")[1]);
                bTempCopy.moveWithoutConfirmation(bTempCopy.getPiece(p.getRow(), p.getColumn()), row, col);
                if (!check(kingColor, bTempCopy)) {
                    possibleMovesChecked.add(move);
                }
                bTempCopy = new Board(bTemp);
            }
        }
        return possibleMovesChecked;
    }

    public boolean check(PieceColor playerColor, Board b) {
        PiecePosition kingPos = b.getPos(PieceType.KING,playerColor);
        List<String>pieceMoves = new ArrayList<>();

        for(Piece p: b.Pieces) {
            if(p.getColor() != playerColor){
                pieceMoves = p.getPossibleMoves();
                for (String move : pieceMoves) {
                    if(move.equals(kingPos.toString())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean check(PieceColor playerColor) {
        PiecePosition kingPos = getPos(PieceType.KING,playerColor);
        List<String>pieceMoves = new ArrayList<>();

        for(Piece p: Pieces) {
            if(p.getColor() != playerColor){
                pieceMoves = p.getPossibleMoves();
                for (String move : pieceMoves) {
                    if(move.equals(kingPos.toString())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean promotePawn(Piece p, PieceType type) {
        if(type == PieceType.KING || type == PieceType.PAWN || type == null){
            return false;
        }
        if(remove(p)){
            addPiece(type, new PiecePosition(p.getRow(), p.getColumn()), p.getColor());
            return true;
        }
        return false;
    }

    public boolean roque(Piece rei, Piece torre){
        King reiTemp = (King) rei;
        Rook torreTemp = (Rook) torre;

        if(reiTemp.getRow() != torreTemp.getRow()){
            return false;
        }
        if(reiTemp.getNoMove() && torreTemp.getNoMove()){
            if(torreTemp.getColumn() == ColumnType.a && getPiece(torreTemp.getRow(), ColumnType.b) == null && getPiece(torreTemp.getRow(), ColumnType.c) == null && getPiece(torreTemp.getRow(), ColumnType.d) == null){
                ((King) rei).alreadyMoved();
                ((Rook) torre).alreadyMoved();
                return true;
            }
            else if(torreTemp.getColumn() == ColumnType.h && getPiece(torreTemp.getRow(), ColumnType.g) == null && getPiece(torreTemp.getRow(), ColumnType.f) == null){
                ((King) rei).alreadyMoved();
                ((Rook) torre).alreadyMoved();
                return true;
            }
        }
        return false;
    }

    public boolean lackOfMaterial(){
        int contador = 0;
        for(Piece p : Pieces){
            if(p.getPieceType() == PieceType.QUEEN || p.getPieceType() == PieceType.ROOK){
                return false;
            }
            contador++;
        }
        if(contador <= 3){
            return true;
        }
        return false;
    }

    public boolean drownedKing(){
        PieceColor kingColor = PieceColor.WHITE;
        List<String> possibleMovesRei = new ArrayList<>();
        List<String> possibleMovesReiTemp = new ArrayList<>();
        List<String> possibleMovesTemp = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            for (Piece p : Pieces) {
                if (p.getColor() == kingColor) {
                    possibleMovesRei.addAll(p.getPossibleMoves());
                }
            }

            for (Piece p : Pieces) {
                if (p.getColor() != kingColor) {
                    possibleMovesTemp = p.getPossibleMoves();
                    for (String move : possibleMovesTemp) {
                        possibleMovesReiTemp.clear();
                        possibleMovesReiTemp.addAll(possibleMovesRei);
                        for (String possibleMove : possibleMovesReiTemp) {
                            if (move.equals(possibleMove) || possibleMove.equals(p.getColumn() + "" + p.getRow())) {
                                possibleMovesRei.remove(possibleMove);

                            }
                        }
                        if (possibleMovesRei.isEmpty()) {
                            if (!check(kingColor)) {
                                return true;
                            }
                        }
                    }
                }
            }
            kingColor = PieceColor.BLACK;
        }
        return false;
    }

    boolean remove(int row, ColumnType col) {
        if (row < 1 || row > getBoardSize()) {
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

    public List<Piece> getPiecesList() {
        return Pieces;
    }
}
