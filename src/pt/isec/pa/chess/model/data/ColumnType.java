package pt.isec.pa.chess.model.data;

public enum ColumnType {
    a, b, c, d, e, f, g, h;

    public int equivalente() {
        return switch (this) {
            case a -> 1;
            case b -> 2;
            case c -> 3;
            case d -> 4;
            case e -> 5;
            case f -> 6;
            case g -> 7;
            case h -> 8;
        };
    }

    public static ColumnType letra(int numero){
        return switch (numero) {
            case 1 -> a;
            case 2 -> b;
            case 3 -> c;
            case 4 -> d;
            case 5 -> e;
            case 6 -> f;
            case 7 -> g;
            case 8 -> h;
            default -> null;
        };
    }

    public static ColumnType letra(String letra){
        return switch (letra) {
            case "a" -> a;
            case "b" -> b;
            case "c" -> c;
            case "d" -> d;
            case "e" -> e;
            case "f" -> f;
            case "g" -> g;
            case "h" -> h;
            default -> null;
        };
    }
}
