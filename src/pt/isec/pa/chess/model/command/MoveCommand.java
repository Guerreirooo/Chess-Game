package pt.isec.pa.chess.model.command;

import pt.isec.pa.chess.model.ChessGame;
import pt.isec.pa.chess.model.ModelLog;
import pt.isec.pa.chess.model.data.ColumnType;
import pt.isec.pa.chess.model.data.PieceColor;
import pt.isec.pa.chess.model.data.pieces.MoveType;
import pt.isec.pa.chess.model.data.pieces.Piece;

public class MoveCommand extends AbstractCommand {
    int rowPiece, row;
    ColumnType colPiece, col;
    Piece capturedPiece;

    public MoveCommand(ChessGame chessGame, int rowPiece, ColumnType colPiece, int row, ColumnType col) {
        super(chessGame);
        this.rowPiece = rowPiece;
        this.row = row;
        this.colPiece = colPiece;
        this.col = col;
        capturedPiece = chessGame.getPiece(row, col);
    }

    @Override
    public MoveType execute() {
        return receiver.move(rowPiece, colPiece, row, col);
    }

    @Override
    public boolean undo() {
        if (!receiver.moveWithoutConfirmation(row, col, rowPiece, colPiece)) {
            return false;
        }
        ModelLog.getInstance().log("UNDO: de " + col + mudarNumeros(row) + " para " + colPiece + mudarNumeros(rowPiece));
        receiver.setCurrentPlayer(receiver.getCurrentPlayer() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE);
        if (capturedPiece != null) {
            receiver.addPiece(capturedPiece.getPieceType(), capturedPiece.getRow(), capturedPiece.getColumn(), capturedPiece.getColor());
        }
        return true;
    }

    @Override
    public boolean redo() {
        if (!receiver.moveWithoutConfirmation(rowPiece, colPiece, row, col)) {
            return false;
        }
        receiver.setCurrentPlayer(receiver.getCurrentPlayer() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE);
        ModelLog.getInstance().log("REDO: de " + colPiece + mudarNumeros(rowPiece) + " para " + col + mudarNumeros(row));
        // receiver.setCurrentPlayer(receiver.getCurrentPlayer() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE);
        return true;
    }

    private int mudarNumeros(int x) {
        switch (x) {
            case 1:
                return 8;
            case 2:
                return 7;
            case 3:
                return 6;
            case 4:
                return 5;
            case 5:
                return 4;
            case 6:
                return 3;
            case 7:
                return 2;
            case 8:
                return 1;
        }
        return x;
    }

//    @Override
//    public boolean redo() {
//        receiver.moveWithoutConfirmation(row, col, rowPiece, colPiece);
//        if (capturedPiece != null) {
//            receiver.addPiece(capturedPiece.getPieceType(), capturedPiece.getRow(), capturedPiece.getColumn(), capturedPiece.getColor());
//        }
//        return true;
//    }
}
