package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    Queen(Board board, int row, ColumnType column, PieceColor color) {
        super(board, row, column, color);
    }

    @Override
    PieceType getPieceType() {
        return PieceType.QUEEN;
    }

    @Override
    String getPosition() {
        StringBuilder sb = new StringBuilder();

        if (getColor() == PieceColor.WHITE) {
            sb.append("Q");
        }
        else {
            sb.append("q");
        }
        sb.append(super.getPosition());
        return sb.toString();
    }

    @Override
    public List<String> possibleMoves() {
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
