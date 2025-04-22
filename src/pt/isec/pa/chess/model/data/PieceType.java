package pt.isec.pa.chess.model.data;

import pt.isec.pa.chess.model.data.pieces.*;

public enum PieceType {
    KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN;

    public Piece createPiece(Board board, PiecePosition position, PieceColor color, boolean... firstMove) {
        boolean checkMove = true;
        if (firstMove.length > 0) {
            checkMove = firstMove[0];
        }
        return switch (this) {
            case KING -> new King(board, position, color, checkMove);
            case QUEEN -> new Queen(board, position, color);
            case BISHOP -> new Bishop(board, position, color);
            case KNIGHT -> new Knight(board, position, color);
            case ROOK -> new Rook(board, position, color, checkMove);
            case PAWN -> new Pawn(board, position, color);
        };
    }

    public static Piece createPiece(Board board, String representation) {
        representation = representation.replaceAll("\n", "");
        if (representation.split("").length < 3 || PieceColor.translate(representation) != null || representation.equals("\n")) {
            return null;
        }

        PieceColor color;
        boolean firstMove = false;

        String pt = representation.split("")[0];
        ColumnType col = ColumnType.letra(representation.split("")[1]);
        int row = Integer.parseInt(representation.split("")[2]);

        if (row < 1 || row > 8) {
           return null;
        }

        if (representation.split("").length > 3) {
            if (representation.split("")[3].equals("*")) {
                firstMove = true;
            }
        }

        if (pt.toUpperCase().equals(pt)) {
            color = PieceColor.WHITE;
        }
        else {
            color = PieceColor.BLACK;
        }

        return switch (pt.toUpperCase()) {
            case "K" -> new King(board, new PiecePosition(row, col), color, firstMove);
            case "Q" -> new Queen(board, new PiecePosition(row, col), color);
            case "B" -> new Bishop(board, new PiecePosition(row, col), color);
            case "N" -> new Knight(board, new PiecePosition(row, col), color);
            case "R" -> new Rook(board, new PiecePosition(row, col), color, firstMove);
            case "P" -> new Pawn(board, new PiecePosition(row, col), color);
            default -> null;
        };
    }

    public static PieceType translate(String text) {
        return switch (text) {
            case "KING" -> KING;
            case "QUEEN" -> QUEEN;
            case "BISHOP" -> BISHOP;
            case "KNIGHT" -> KNIGHT;
            case "ROOK" -> ROOK;
            case "PAWN" -> PAWN;
            default -> null;
        };
    }
}
