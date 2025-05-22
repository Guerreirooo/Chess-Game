package pt.isec.pa.chess.model.command;

import pt.isec.pa.chess.model.data.pieces.MoveType;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    private Deque<ICommand> history;
    private Deque<ICommand> redoCmds;
    //private Stack<ICommand> history;
    //private Stack<ICommand> redoCmds;
    public CommandManager() {
        history = new ArrayDeque<>();
        redoCmds = new ArrayDeque<>();
        //history = new Stack<>();
        //redoCmds = new Stack<>();
    }
    public MoveType invokeCommand(ICommand cmd) {
        redoCmds.clear();
        MoveType temp;
        if ((temp = cmd.execute()) != MoveType.FALSE) {
            history.push(cmd);
        }
        return temp;
    }
    public boolean undo() {
        if (history.isEmpty())
            return false;
        ICommand cmd = history.pop();
        cmd.undo();
        redoCmds.push(cmd);
        return true;
    }
    public boolean redo() {
        if (redoCmds.isEmpty())
            return false;
        ICommand cmd = redoCmds.pop();
        cmd.redo();
        history.push(cmd);
        return true;
    }
    public boolean hasUndo() {
        return history.size()>0;
    }
    public boolean hasRedo() {
        return redoCmds.size()>0;
    }
}
