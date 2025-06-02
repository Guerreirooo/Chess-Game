package pt.isec.pa.chess.model.data;

public enum PieceColor {
    BLACK, WHITE;

    public static PieceColor translate(String representation) {
        return switch (representation) {
            case "BLACK" -> BLACK;
            case "WHITE" -> WHITE;
            case "black" -> BLACK;
            case "white" -> WHITE;
            default -> null;
        };
    }
}
