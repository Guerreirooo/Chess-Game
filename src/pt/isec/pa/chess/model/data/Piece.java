package pt.isec.pa.chess.model.data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

abstract class Piece implements Serializable {
    private PiecePosition position;
    private PieceColor color;
    private Board board;

    @Serial
    private static final long serialVersionUID = 1L;

    public Piece(Board board, PiecePosition position, PieceColor color) {
        if (position.getRow() < 1 || position.getRow() > 8) {
            return;
        }
        this.position = position;
        this.color = color;
        this.board = board;
    }

    public void changeBoard(Board b) {
        board = b;
    }

    public abstract List<String> getPossibleMoves();

    abstract PieceType getPieceType();

    @Override
    public String toString() {
        String sb;
        sb = position.getColumn().toString() + position.getRow();
        return sb;
    }

    boolean checkPieceOnBoard(int row, ColumnType column) {
        return board.checkPiecesPosition(row, column);
    }

    PieceColor checkColor(int row, ColumnType column) {
        return board.checkColorPosition(row, column);
    }

    PieceColor getColor() {
        return color;
    }

    ColumnType getColumn() {
        return position.getColumn();
    }

    int getRow() {
        return position.getRow();
    }

    void setRow(int row) {
        if (row < 1 || row > 8) {
            return;
        }
        this.position.setRow(row);
    }

    void setCol(ColumnType col) {
        this.position.setColumn(col);
    }

    boolean checkMove(List<String> possibleMoves, int auxRow, int auxCol, boolean... checkOpponentPieces) {
        StringBuilder sb = new StringBuilder();

        if (auxRow > 8 || auxRow < 1 || auxCol > 8 || auxCol < 1) {
            return false;
        }

        // Caso o checkOponentPieces[0] esteja a true
        // irá verificar se na posição recebida existe alguma peça (independente da cor)
        // (será útil para o peão andar para a frente)
        if (checkOpponentPieces.length == 1 && checkOpponentPieces[0]) {
            if (!checkPieceOnBoard(auxRow, ColumnType.letra(auxCol))) {
                possibleMoves.add(sb.append(ColumnType.letra(auxCol)).append(auxRow).toString());
                return true;
            }
        }
        // Caso o checkOpponentPieces[1] esteja a true
        // irá verificar se na posição recebida existe alguma peça da cor do adversário
        // (será útil para o peão comer as peças de lado)
        else if (checkOpponentPieces.length == 2 && checkOpponentPieces[1] && checkColor(auxRow, ColumnType.letra(auxCol)) != getColor()) {
            if (checkPieceOnBoard(auxRow, ColumnType.letra(auxCol))) {
                possibleMoves.add(sb.append(ColumnType.letra(auxCol)).append(auxRow).toString());
                return true;
            }
        }
        else {
            if (!checkPieceOnBoard(auxRow, ColumnType.letra(auxCol))) {
                possibleMoves.add(sb.append(ColumnType.letra(auxCol)).append(auxRow).toString());
                return true;
            }
            else if (checkPieceOnBoard(auxRow, ColumnType.letra(auxCol)) && checkColor(auxRow, ColumnType.letra(auxCol)) != getColor()) {
                possibleMoves.add(sb.append(ColumnType.letra(auxCol)).append(auxRow).toString());
                return false;
            }
        }

        return false;
    }
}
