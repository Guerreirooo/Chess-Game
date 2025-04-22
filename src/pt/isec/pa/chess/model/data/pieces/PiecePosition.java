package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.ColumnType;

import java.io.Serial;
import java.io.Serializable;

public class PiecePosition implements Serializable {
    private ColumnType column;
    private int row;

    @Serial
    private static final long serialVersionUID = 1L;

    public PiecePosition(int row, ColumnType column) {
        if (row < 1) {
            this.row = 1;
        }
        else if (row > 8) {
            this.row = 8;
        }
        else {
            this.row = row;
        }
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        if (row < 1 || row > 8) {
            return;
        }
        this.row = row;
    }

    public ColumnType getColumn() {
        return column;
    }

    public void setColumn(ColumnType column) {
        this.column = column;
    }

    @Override
    public String toString() {
        String sb;
        sb = column.toString() + row;
        return sb;
    }
}
