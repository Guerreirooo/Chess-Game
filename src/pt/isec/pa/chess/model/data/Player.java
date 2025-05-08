package pt.isec.pa.chess.model.data;

import java.io.Serial;
import java.io.Serializable;

public class Player implements Serializable {
    private PieceColor playerColor;
    private String name;

    @Serial
    private static final long serialVersionUID = 1L;

    public Player(PieceColor playerColor, String name) {
        this.playerColor = playerColor;
        this.name = name;
    }

    String getName(){
        return name;
    }

    PieceColor getPlayerColor(){
        return playerColor;
    }
}
