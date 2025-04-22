package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.PieceColor;
import pt.isec.pa.chess.model.data.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private boolean move;

    public Pawn(Board board, PiecePosition position, PieceColor color) {
        super(board, position, color);
        move = false;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.PAWN;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (getColor() == PieceColor.WHITE) {
            sb.append("P");
        }
        else {
            sb.append("p");
        }

        sb.append(super.toString());
        return sb.toString();
    }

    @Override
    public List<String> getPossibleMoves() {
        List<String> possibleMoves = new ArrayList<>();
        int auxRow = getRow();
        int auxCol = getColumn().equivalente();
        int n = 2;

        if (move) {
            n = 1;
        }

        for (int i = 0; i < n; i++) {
            if (getColor() == PieceColor.WHITE) {
                auxRow--;
            }
            else if (getColor() == PieceColor.BLACK) {
                auxRow++;
            }
            else {
                break;
            }

            checkMove(possibleMoves, auxRow, auxCol, true);
        }

        auxRow = getRow();
        auxCol = getColumn().equivalente();

        if (getColor() == PieceColor.WHITE) {
            auxRow--;
        }
        else if (getColor() == PieceColor.BLACK) {
            auxRow++;
        }

        auxCol--;
        checkMove(possibleMoves, auxRow, auxCol, true, true);

        auxRow = getRow();
        auxCol = getColumn().equivalente();

        if (getColor() == PieceColor.WHITE) {
            auxRow--;
        }
        else if (getColor() == PieceColor.BLACK) {
            auxRow++;
        }

        auxCol++;
        checkMove(possibleMoves, auxRow, auxCol, true, true);

        return possibleMoves;
    }
}
