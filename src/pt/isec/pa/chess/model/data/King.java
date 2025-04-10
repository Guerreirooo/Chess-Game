package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private boolean noMove;

    King(Board board, PiecePosition position, PieceColor color) {
        super(board, position, color);
        noMove = true;
    }

    King(Board board, PiecePosition position, PieceColor color, boolean firstMove) {
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
    PieceType getPieceType() {
        return PieceType.KING;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (getColor() == PieceColor.WHITE) {
            sb.append("K");
        }
        else {
            sb.append("k");
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

        auxRow--;
        auxCol--;

        for (int i = 0; i<3; i++){
            checkMove(possibleMoves, auxRow, auxCol + i);
        }

        auxRow = getRow();
        auxCol = getColumn().equivalente();

        checkMove(possibleMoves, auxRow, auxCol + 1);
        checkMove(possibleMoves, auxRow, auxCol - 1);

        auxRow = getRow() + 1;
        auxCol = getColumn().equivalente() - 1;

        for(int i = 0; i<3; i++){
            checkMove(possibleMoves, auxRow, auxCol + i);
        }
        return possibleMoves;
    }
}
