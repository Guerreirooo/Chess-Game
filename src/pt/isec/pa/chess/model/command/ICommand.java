package pt.isec.pa.chess.model.command;

import pt.isec.pa.chess.model.data.pieces.MoveType;

public interface ICommand {
    MoveType execute();
    boolean undo();
    boolean redo();
}
