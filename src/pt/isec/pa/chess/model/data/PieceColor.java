package pt.isec.pa.chess.model.data;

public enum PieceColor {
    BLACK, WHITE;

    public static PieceColor translate(String representation) {
        return switch (representation) {
            case "BLACK" -> BLACK;
            case "WHITE" -> WHITE;
            default -> null;
        };
    }

    public static PieceColor cor(String color){
        return switch (color) {
            case "WHITE" -> WHITE;
            case "BLACK" -> BLACK;
            default -> null;
        };
    }
}
