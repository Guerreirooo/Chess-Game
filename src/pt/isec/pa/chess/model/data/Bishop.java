package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    Bishop(Board board, int row, ColumnType column, PieceColor color) {
        super(board, row, column, color);
    }

    @Override
    PieceType getPieceType() {
        return PieceType.BISHOP;
    }

    @Override
    String getPosition() {
        StringBuilder sb = new StringBuilder();

        if (getColor() == PieceColor.WHITE) {
            sb.append("B");
        }
        else {
            sb.append("b");
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
