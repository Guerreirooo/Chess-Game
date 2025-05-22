package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.command.CommandManager;
import pt.isec.pa.chess.model.command.MoveCommand;
import pt.isec.pa.chess.model.data.*;
import pt.isec.pa.chess.model.data.pieces.MoveType;
import pt.isec.pa.chess.model.data.pieces.Piece;
import pt.isec.pa.chess.ui.res.SoundManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class ChessGameManager {
    private ChessGame game;
    private int tabuleiroX = 170;
    private int tabuleiroY = 144;
    private int SquareSize = 70;
    private static int numMovements = 0;
    PropertyChangeSupport pcs;
    public static final String GAME_VALUE = "game";
    public static final String PLAYER_VALUE = "player";
    boolean sounds = false;
    CommandManager cm;

    public ChessGameManager() {
        game = new ChessGame();
        pcs = new PropertyChangeSupport(this);
        newCommandManager();
    }

    public ChessGameManager(ChessGame game) {
        this.game = game;
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property,listener);
    }

    public String getSound(){
        if(sounds){
            return "ON";
        }
        return "OFF";
    }

    public void setSounds(boolean sounds) {
        this.sounds = sounds;
    }

    public void alterarValores(ChessGame temp) {
        game.alterarValores(temp);
        pcs.firePropertyChange(GAME_VALUE, null, null);
    }

    public int getNumMovements(){return numMovements;}

    public void setNumMovements(int newNum){numMovements = newNum;}

    public void incNumMovements(){numMovements++;}

    public void initGame(){
        game.initGame();
        pcs.firePropertyChange(GAME_VALUE, null, null);
        newCommandManager();
        ModelLog.getInstance().log("Jogo iniciado");
    }

    public void initGame(String blackName, String whiteName){
        game.setBlackName(blackName);
        game.setWhiteName(whiteName);
        initGame();
        pcs.firePropertyChange(GAME_VALUE, null, null);
    }

    public void setBlackName(String blackName){
        game.setBlackName(blackName);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
    }

    public void setWhiteName(String whiteName){
        game.setWhiteName(whiteName);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
    }

    public void setCurrentPlayer(PieceColor color){
        game.setCurrentPlayer(color);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
    }

    public PieceColor getCurrentPlayer(){
        return game.getCurrentPlayer();
    }

    public PieceColor getWaitingPlayer(){
        if(game.getCurrentPlayer() == PieceColor.WHITE){
            return PieceColor.BLACK;
        }
        return PieceColor.WHITE;
    }

    public String getPieceImage(int row, int col) {
        Piece temp;
        int rowCorrigida = row + 1;
        int colCorrigida = col + 1;
        ColumnType tempCol = ColumnType.letra(colCorrigida);
        temp = game.getPiece(rowCorrigida,tempCol);

        if(temp == null){
            return null;
        }
        else if(temp.getColor() == PieceColor.WHITE){
            if(temp.getPieceType() == PieceType.PAWN){
                return "pawnW.png";
            }
            else if(temp.getPieceType() == PieceType.KNIGHT){
                return "knightW.png";
            }
            else if(temp.getPieceType() == PieceType.BISHOP){
                return "bishopW.png";
            }
            else if(temp.getPieceType() == PieceType.ROOK){
                return "rookW.png";
            }
            else if(temp.getPieceType() == PieceType.QUEEN){
                return "queenW.png";
            }
            else if(temp.getPieceType() == PieceType.KING){
                return "kingW.png";
            }
        }
        else if(temp.getColor() == PieceColor.BLACK){
            if(temp.getPieceType() == PieceType.PAWN){
                return "pawnB.png";
            }
            else if(temp.getPieceType() == PieceType.KNIGHT){
                return "KnightB.png";
            }
            else if(temp.getPieceType() == PieceType.BISHOP){
                return "bishopB.png";
            }
            else if(temp.getPieceType() == PieceType.ROOK){
                return "rookB.png";
            }
            else if(temp.getPieceType() == PieceType.QUEEN){
                return "queenB.png";
            }
            else if(temp.getPieceType() == PieceType.KING){
                return "kingB.png";
            }
        }
        return null;
    }

    public int getBoardSize(){
        return game.getBoardSize();
    }

    @Override
    public String toString(){
        return game.toString();
    }

    public boolean exportGame(String fileName) {
        if (!game.savePartialGame(fileName)) {
            ModelLog.getInstance().log("Erro ao exportar o jogo em " + fileName);
            return false;
        }
        return true;
    }

    public boolean importGame(String fileName) {
        boolean result = game.loadPartialGame(fileName);
        if(result) {
            pcs.firePropertyChange(PLAYER_VALUE, null, null);
            pcs.firePropertyChange(GAME_VALUE, null, null);
        }
        else {
            ModelLog.getInstance().log("Erro ao carregar o jogo em " + fileName);
        }
        return result;
    }

    public boolean load(String fileName) {
        ChessGame temp = ChessGameSerialization.load(fileName);
        if (temp == null) {
            ModelLog.getInstance().log("Erro ao carregar o jogo em " + fileName);
            return false;
        }
        alterarValores(temp);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
        return true;
    }

    public boolean save(String fileName) {
        if (!ChessGameSerialization.save(fileName, game)) {
            ModelLog.getInstance().log("Erro ao guardar o jogo em " + fileName);
            return false;
        }
        return true;
    }

    int getRow(double col) {
        double localY = col - tabuleiroY;
        int rowTemp = (int)(localY / SquareSize) + 1;

        return rowTemp;
    }

    ColumnType getColumn(double row) {
        double localX = row - tabuleiroX;

        int colTemp = (int)(localX / SquareSize) + 1;

        ColumnType colType = ColumnType.letra(colTemp);

        return colType;
    }

    public boolean havePiece(int row, int col) {
        if (game.getPiece(row, ColumnType.letra(col)) != null) {
            return true;
        }
        return false;
    }

    public Piece getPiece(int row, int col) {
        Piece temp;
        temp = game.getPiece(row, ColumnType.letra(col));
        if (temp == null) {
            return null;
        }
        return temp;
    }

//    public MoveType move(double rowPiece, double colPiece, double row, double col) {
//        int rowTemp = getRow(colPiece);
//        ColumnType colTemp = getColumn(rowPiece);
//        Piece temp = getPiece(rowTemp,colTemp.equivalente());
//        boolean capture = havePiece(getRow(col),getColumn(row).equivalente());
//
//        MoveType result = game.move(getRow(colPiece), getColumn(rowPiece), getRow(col), getColumn(row));
//        if(result != MoveType.FALSE){
//            ModelLog.getInstance().log("Movimento: de " + getColumn(rowPiece) + mudarNumeros(getRow(colPiece)) + " para " + getColumn(row) + mudarNumeros(getRow(col)));
//            incNumMovements();
//            if(sounds) {
//                playSounds(temp, colTemp, mudarNumeros(rowTemp), capture);
//            }
//            pcs.firePropertyChange(PLAYER_VALUE, null, null);
//        }
//        pcs.firePropertyChange(GAME_VALUE, null, null);
//
//        return result;
//    }

    public void findSounds(List<String> fileNames) {
        List<String> filesTemp = new ArrayList<>();
        List<String> lista = new ArrayList<>();
        lista.add(".mp3");
        lista.add(".wav");

        for(String filename : fileNames) {
            for (String s : lista) {
                if (SoundManager.getSound(filename + s)) {
                    filesTemp.add(filename + s);
                    break;
                }
            }
        }

        SoundManager.playSequence(filesTemp);
    }

    public void playSounds(Piece temp,ColumnType colOrigin,int rowOrigin,boolean capture) {
        List<String> fileNames = new ArrayList<>();
        fileNames.add(temp.getColor().toString().toLowerCase());
        fileNames.add(temp.getPieceType().toString().toLowerCase());
        fileNames.add(colOrigin.toString().toLowerCase());
        fileNames.add(rowOrigin + "");
        fileNames.add(temp.getColumn().toString().toLowerCase());
        fileNames.add(mudarNumeros(temp.getRow())+"");

        if(capture){
            fileNames.add("capture");
        }

        if(check(getCurrentPlayer().toString())){
            fileNames.add("check");
        }

        findSounds(fileNames);
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

    public void getTypePromote(double rowPiece, double colPiece, String pt){
        game.getTypePromote(getRow(colPiece), getColumn(rowPiece), PieceType.translate(pt));
        pcs.firePropertyChange(GAME_VALUE, null, null);
    }

    public boolean checkStopCheckMate(PieceColor kingColor) {
        return game.checkStopCheckMate(kingColor);
    }

    public boolean drownedKing(){
        return game.drownedKing();
    }

    public boolean lackOfMaterial(){
        return game.lackOfMaterial();
    }

    public boolean checkMate(){
        if (game.checkMate()) {
            ModelLog.getInstance().log("CheckMate de " + getCurrentPlayer());
            return true;
        }
        return false;
    }

    /*
    *   MAL IMPLEMENTADO
    *
    private void getTypePromote(Piece p){
        String Type;
        PieceType pt = null;
        Piece retorno = null;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Promocao de peao! Nova peca : [QUEEN,ROOK,BISHOP,KNIGHT]\n");
            Type = sc.nextLine();
            if(Type.equalsIgnoreCase("QUEEN") || Type.equalsIgnoreCase("ROOK") || Type.equalsIgnoreCase("BISHOP") || Type.equalsIgnoreCase("KNIGHT")){
                pt = PieceType.translate(Type);
            }
        }
        while(!board.promotePawn(p, pt));
    }*/

    /*
    *   NAO SEI COMO FAZER */

    public boolean gameOver(){
        if(game.gameOver()){
            return true;
        }
        return false;
    }

    public boolean check(String playerTurn){
        if(game.check(playerTurn)){
            return true;
        }
        return false;
    }

    public void newCommandManager() {
        cm = new CommandManager();
    }
    public MoveType move(double rowPiece, double colPiece, double row, double col) {
        MoveType result;
        int rowTemp = getRow(colPiece);
        ColumnType colTemp = getColumn(rowPiece);
        Piece temp = getPiece(rowTemp,colTemp.equivalente());
        boolean capture = havePiece(getRow(col),getColumn(row).equivalente());

        result = cm.invokeCommand(new MoveCommand(game, getRow(colPiece), getColumn(rowPiece), getRow(col), getColumn(row)));
        if(result != MoveType.FALSE){
            ModelLog.getInstance().log("Movimento: de " + getColumn(rowPiece) + mudarNumeros(getRow(colPiece)) + " para " + getColumn(row) + mudarNumeros(getRow(col)));
            incNumMovements();
            if(sounds) {
                playSounds(temp, colTemp, mudarNumeros(rowTemp), capture);
            }
            pcs.firePropertyChange(PLAYER_VALUE, null, null);
        }
        pcs.firePropertyChange(GAME_VALUE, null, null);
        return result;
    }
    public boolean hasUndo() { return cm.hasUndo(); }
    public boolean undo() {
        if (cm.undo()) {
            pcs.firePropertyChange(GAME_VALUE, null, null);
            pcs.firePropertyChange(PLAYER_VALUE, null, null);
            return true;
        }
        return false;
    }
    public boolean hasRedo() { return cm.hasRedo(); }
    public boolean redo() {
        if (cm.redo()) {
            pcs.firePropertyChange(GAME_VALUE, null, null);
            pcs.firePropertyChange(PLAYER_VALUE, null, null);
            return true;
        }
        return false;
    }
}
