package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    Knight(Board board, int row, ColumnType column, PieceColor color) {
        super(board, row, column, color);
    }

    @Override
    PieceType getPieceType() {
        return PieceType.KNIGHT;
    }

    @Override
    String getPosition() {
        StringBuilder sb = new StringBuilder();

        if (getColor() == PieceColor.WHITE) {
            sb.append("N");
        }
        else {
            sb.append("n");
        }
        sb.append(super.getPosition());
        return sb.toString();
    }

    @Override
    public List<String> possibleMoves() {
        List<String> possibleMoves = new ArrayList<>();
        int auxRow = getRow();
        int auxCol = getColumn().equivalente();

        //Cima direita
        auxRow = auxRow - 2;
        auxCol++;
        checkMove(possibleMoves, auxRow, auxCol);

        //Cima esquerda
        auxCol = auxCol - 2;
        checkMove(possibleMoves, auxRow, auxCol);

        auxCol = getColumn().equivalente();
        auxRow = getRow();

        //Direita cima
        auxRow--;
        auxCol = auxCol + 2;
        checkMove(possibleMoves, auxRow, auxCol);

        //Direita baixo
        auxRow = auxRow + 2;
        checkMove(possibleMoves, auxRow, auxCol);

        auxCol = getColumn().equivalente();
        auxRow = getRow();

        //Baixo direita
        auxRow = auxRow + 2;
        auxCol++;
        checkMove(possibleMoves, auxRow, auxCol);

        //Baixo esquerda
        auxCol = auxCol - 2;
        checkMove(possibleMoves, auxRow, auxCol);

        auxCol = getColumn().equivalente();
        auxRow = getRow();

        //Esquerda cima
        auxRow--;
        auxCol = auxCol - 2;
        checkMove(possibleMoves, auxRow, auxCol);

        //Esquerda baixo
        auxRow = auxRow + 2;
        checkMove(possibleMoves, auxRow, auxCol);

        return possibleMoves;
    }
}
