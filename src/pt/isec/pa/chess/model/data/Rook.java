package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    private boolean noMove;

    Rook(Board board, int row, ColumnType column, PieceColor color) {
        super(board, row, column, color);
        noMove = true;
    }

    @Override
    PieceType getPieceType() {
        return PieceType.ROOK;
    }

    @Override
    String getPosition() {
        StringBuilder sb = new StringBuilder();

        if (getColor() == PieceColor.WHITE) {
            sb.append("R");
        }
        else {
            sb.append("r");
        }
        sb.append(super.getPosition());

        if (noMove) {
            sb.append("*");
        }
        return sb.toString();
    }

    @Override
    public List<String> possibleMoves() {
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
