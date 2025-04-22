package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.PieceColor;
import pt.isec.pa.chess.model.data.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    private boolean noMove;

    Rook(Board board, PiecePosition position, PieceColor color) {
        super(board, position, color);
        noMove = true;
    }

    public Rook(Board board, PiecePosition position, PieceColor color, boolean firstMove) {
        super(board, position, color);
        noMove = firstMove;
    }

    public void alreadyMoved() {
        noMove = false;
    }

    public boolean getNoMove() {
        return noMove;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.ROOK;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (getColor() == PieceColor.WHITE) {
            sb.append("R");
        }
        else {
            sb.append("r");
        }
        sb.append(super.toString());

        if (noMove) {
            sb.append("*");
        }
        return sb.toString();
    }

    @Override
    public List<String> getPossibleMoves() {
        List<String> possibleMoves = new ArrayList<>();
        int auxRow = getRow();
        int auxCol = getColumn().equivalente();

        do {
            auxRow--;
        } while(checkMove(possibleMoves, auxRow, auxCol));

        auxRow = getRow();
        auxCol = getColumn().equivalente();

        do {
            auxRow++;
        } while(checkMove(possibleMoves, auxRow, auxCol));

        auxRow = getRow();
        auxCol = getColumn().equivalente();

        do {
            auxCol++;
        } while(checkMove(possibleMoves, auxRow, auxCol));

        auxRow = getRow();
        auxCol = getColumn().equivalente();

        do {
            auxCol--;
        } while(checkMove(possibleMoves, auxRow, auxCol));

        return possibleMoves;
    }
}
