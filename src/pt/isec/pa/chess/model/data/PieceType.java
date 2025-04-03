package pt.isec.pa.chess.model.data;

enum PieceType {
    KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN;

    Piece createPiece(Board board, int row, ColumnType column, PieceColor color) {
        return switch (this) {
            case KING -> new King(board, row, column, color);
            case QUEEN -> new Queen(board, row, column, color);
            case BISHOP -> new Bishop(board, row, column, color);
            case KNIGHT -> new Knight(board, row, column, color);
            case ROOK -> new Rook(board, row, column, color);
            case PAWN -> new Pawn(board, row, column, color);
        };
    }
}
