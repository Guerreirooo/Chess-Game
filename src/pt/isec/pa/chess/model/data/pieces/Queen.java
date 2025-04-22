package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.PieceColor;
import pt.isec.pa.chess.model.data.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(Board board, PiecePosition position, PieceColor color) {
        super(board, position, color);
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.QUEEN;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (getColor() == PieceColor.WHITE) {
            sb.append("Q");
        }
        else {
            sb.append("q");
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
        } while (checkMove(possibleMoves, auxRow, auxCol));

        auxRow = getRow();

        do {
            auxRow++;
        } while (checkMove(possibleMoves, auxRow, auxCol));

        auxRow = getRow();

        do {
            auxCol++;
        } while (checkMove(possibleMoves, auxRow, auxCol));

        auxCol = getColumn().equivalente();

        do {
            auxCol--;
        } while (checkMove(possibleMoves, auxRow, auxCol));

        auxRow = getRow();
        auxCol = getColumn().equivalente();

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
