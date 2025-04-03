package pt.isec.pa.chess.model.data;
import java.util.List;

abstract class Piece {
    private ColumnType column;
    private int row;
    private PieceColor color;
    private Board board;

    public Piece(Board board, int row, ColumnType column, PieceColor color) {
        if (row < 1  || row > 8) {
            return;
        }
        this.column = column;
        this.row = row;
        this.color = color;
        this.board = board;
    }

    public abstract List<String> possibleMoves();

    abstract PieceType getPieceType();

    String getPosition() {
        StringBuilder sb = new StringBuilder();
        sb.append(column).append(row);
        return sb.toString();
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
        return column;
    }

    int getRow() {
        return row;
    }

    void setRow(int row) {
        if (row < 1 || row > 8) {
            return;
        }
        this.row = row;
    }

    void setCol(ColumnType col) {
        this.column = col;
    }

    boolean checkMove(List<String> possibleMoves, int auxRow, int auxCol, boolean... checkOpponentPieces) {
        if (auxRow > 8 || auxRow < 1 || auxCol > 8 || auxCol < 1) {
            return false;
        }

        // Caso o checkOponentPieces[0] esteja a true
        // irá verificar se na posição recebida existe alguma peça (independente da cor)
        // (será útil para o peão andar para a frente)
        if (checkOpponentPieces.length == 1 && checkOpponentPieces[0]) {
            if (!checkPieceOnBoard(auxRow, ColumnType.letra(auxCol))) {
                StringBuilder sb = new StringBuilder();
                possibleMoves.add(sb.append(ColumnType.letra(auxCol)).append(auxRow).toString());
                return true;
            }
        }
        // Caso o checkOpponentPieces[1] esteja a true
        // irá verificar se na posição recebida existe alguma peça da cor do adversário
        // (será útil para o peão comer as peças de lado)
        else if (checkOpponentPieces.length == 2 && checkOpponentPieces[1] && checkColor(auxRow, ColumnType.letra(auxCol)) != getColor()) {
            if (checkPieceOnBoard(auxRow, ColumnType.letra(auxCol))) {
                StringBuilder sb = new StringBuilder();
                possibleMoves.add(sb.append(ColumnType.letra(auxCol)).append(auxRow).toString());
                return true;
            }
        }
        else {
            if (!checkPieceOnBoard(auxRow, ColumnType.letra(auxCol))) {
                StringBuilder sb = new StringBuilder();
                possibleMoves.add(sb.append(ColumnType.letra(auxCol)).append(auxRow).toString());
                return true;
            }
            else if (checkPieceOnBoard(auxRow, ColumnType.letra(auxCol)) && checkColor(auxRow, ColumnType.letra(auxCol)) != getColor()) {
                StringBuilder sb = new StringBuilder();
                possibleMoves.add(sb.append(ColumnType.letra(auxCol)).append(auxRow).toString());
                return false;
            }
        }

        return false;
    }
}
