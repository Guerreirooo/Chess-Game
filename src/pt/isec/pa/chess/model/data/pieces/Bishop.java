package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.PieceColor;
import pt.isec.pa.chess.model.data.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Board board, PiecePosition position, PieceColor color) {
        super(board, position, color);
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.BISHOP;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (getColor() == PieceColor.WHITE) {
            sb.append("B");
        }
        else {
            sb.append("b");
        }
        sb.append(super.toString());
        return sb.toString();
    }

    @Override
    public List<String> getPossibleMoves() {
        List<String> possibleMoves = new ArrayList<>();
        int auxRow = getRow();
        int auxCol = getColumn().equivalente();

        do {
            auxRow--;
            auxCol--;
        } while (checkMove(possibleMoves, auxRow, auxCol));

        auxRow = getRow();
        auxCol = getColumn().equivalente();

        do {
            auxRow++;
            auxCol++;
        } while (checkMove(possibleMoves, auxRow, auxCol));

        auxRow = getRow();
        auxCol = getColumn().equivalente();

        do {
            auxRow++;
            auxCol--;
        } while (checkMove(possibleMoves, auxRow, auxCol));

        auxRow = getRow();
        auxCol = getColumn().equivalente();

        do {
            auxRow--;
            auxCol++;
        } while (checkMove(possibleMoves, auxRow, auxCol));

        return possibleMoves;
    }
}
