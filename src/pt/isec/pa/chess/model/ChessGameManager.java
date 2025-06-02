package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.command.CommandManager;
import pt.isec.pa.chess.model.command.MoveCommand;
import pt.isec.pa.chess.model.data.*;
import pt.isec.pa.chess.model.data.pieces.MoveType;
import pt.isec.pa.chess.model.data.pieces.Piece;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * Classe facade responsável por fazer a ligação entre ChessGame e o RootPane ou BoardUI
 */
public class ChessGameManager {
    /** Instância do jogo de xadrez que armazena o estado atual do tabuleiro e jogadores */
    private ChessGame game;
    private int tabuleiroX = 170;
    private int tabuleiroY = 144;
    private int SquareSize = 70;
    private static int numMovements = 0;
    /** Suporte para eventos de mudança de propriedades observadas */
    PropertyChangeSupport pcs;
    public static final String GAME_VALUE = "game";
    public static final String EDITOR_VALUE = "editor";
    public static final String PLAYER_VALUE = "player";
    /** Gerenciador de comandos para implementar ações como movimento, undo e redo */
    CommandManager cm;

    /**
     * Construtor padrão que inicializa um novo jogo e o gerenciador de comandos.
     */
    public ChessGameManager() {
        game = new ChessGame();
        pcs = new PropertyChangeSupport(this);
        newCommandManager();
    }

    /**
     * Construtor que recebe uma instância existente de ChessGame.
     *
     * @param game instância do jogo de xadrez a ser gerenciada
     */
    public ChessGameManager(ChessGame game) {
        this.game = game;
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Adiciona um ouvinte para mudanças em uma propriedade específica.
     *
     * @param property nome da propriedade a ser observada
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property,listener);
    }

    /**
     * Atualiza os valores do jogo a partir de outro objeto ChessGame.
     *
     * @param temp novo estado do jogo a ser copiado
     */
    public void alterarValores(ChessGame temp) {
        game.alterarValores(temp);
        pcs.firePropertyChange(GAME_VALUE, null, null);
    }

    /**
     * Retorna o número total de movimentos realizados no jogo.
     *
     * @return número de movimentos
     */
    public int getNumMovements(){return numMovements;}

    /**
     * Define o número total de movimentos realizados.
     *
     * @param newNum novo valor para o contador de movimentos
     */
    public void setNumMovements(int newNum){numMovements = newNum;}

    /**
     * Incrementa em uma unidade o contador de movimentos.
     */
    public void incNumMovements(){numMovements++;}

    /**
     * Inicializa um novo jogo padrão e notifica ouvintes da mudança.
     */
    public void initGame(){
        game.initGame();
        pcs.firePropertyChange(GAME_VALUE, null, null);
        newCommandManager();
        ModelLog.getInstance().log("Jogo iniciado");
    }

    /**
     * Inicializa um novo jogo vazio para o modo editor, com nomes dos jogadores.
     *
     * @param blackName nome do jogador das peças pretas
     * @param whiteName nome do jogador das peças brancas
     */
    public void initGameEmpty(String blackName, String whiteName){
        game.setBlackName(blackName);
        game.setWhiteName(whiteName);
        game.initGameEmpty();
        pcs.firePropertyChange(GAME_VALUE, null, null);
        newCommandManager();
        ModelLog.getInstance().log("Jogo iniciado (Modo editor)");
    }

    /**
     * Inicializa um novo jogo com nomes dos jogadores.
     *
     * @param blackName nome do jogador das peças pretas
     * @param whiteName nome do jogador das peças brancas
     */
    public void initGame(String blackName, String whiteName){
        game.setBlackName(blackName);
        game.setWhiteName(whiteName);
        initGame();
        pcs.firePropertyChange(GAME_VALUE, null, null);
    }

    /**
     * Define o nome do jogador das peças pretas.
     *
     * @param blackName nome do jogador preto
     */
    public void setBlackName(String blackName){
        game.setBlackName(blackName);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
    }

    /**
     * Define o nome do jogador das peças brancas.
     *
     * @param whiteName nome do jogador branco
     */
    public void setWhiteName(String whiteName){
        game.setWhiteName(whiteName);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
    }

    /**
     * Define o jogador atual (turno) da partida.
     *
     * @param color cor do jogador atual (branco ou preto)
     */
    public void setCurrentPlayer(PieceColor color){
        game.setCurrentPlayer(color);
        pcs.firePropertyChange(PLAYER_VALUE, null, null);
    }

    /**
     * Retorna o jogador atual (turno) da partida.
     *
     * @return cor do jogador atual
     */
    public PieceColor getCurrentPlayer(){
        return game.getCurrentPlayer();
    }

    /**
     * Adiciona uma peça no tabuleiro em uma posição específica.
     *
     * @param x coluna (0-based) onde será adicionada a peça
     * @param y linha (0-based) onde será adicionada a peça
     * @param piece tipo da peça a ser adicionada
     * @param color cor da peça a ser adicionada
     */
    public void addPiece(int x, int y, PieceType piece, PieceColor color){
        game.addPiece(piece,y,ColumnType.letra(x),color);
        pcs.firePropertyChange(EDITOR_VALUE, null, null);
    }

    /**
     * Retorna o jogador que está aguardando a sua vez (não atual).
     *
     * @return cor do jogador que não está no turno
     */
    public PieceColor getWaitingPlayer(){
        if(game.getCurrentPlayer() == PieceColor.WHITE){
            return PieceColor.BLACK;
        }
        return PieceColor.WHITE;
    }

    /**
     * Obtém o nome do arquivo da imagem da peça na posição especificada.
     *
     * @param row linha da peça (0-based)
     * @param col coluna da peça (0-based)
     * @return nome do arquivo da imagem da peça, ou null se não houver peça
     */
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

    /**
     * Retorna o tamanho do tabuleiro (número de linhas/colunas).
     *
     * @return tamanho do tabuleiro
     */
    public int getBoardSize(){
        return game.getBoardSize();
    }

    /**
     * Representação em string do estado atual do jogo.
     *
     * @return string representando o jogo
     */
    @Override
    public String toString(){
        return game.toString();
    }

    /**
     * Exporta o estado parcial do jogo para um arquivo.
     *
     * @param fileName caminho do arquivo para salvar
     * @return true se a exportação foi bem-sucedida, false caso contrário
     */
    public boolean exportGame(String fileName) {
        if (!game.savePartialGame(fileName)) {
            ModelLog.getInstance().log("Erro ao exportar o jogo em " + fileName);
            return false;
        }
        return true;
    }

    /**
     * Importa o estado parcial do jogo de um arquivo.
     *
     * @param fileName caminho do arquivo a ser carregado
     * @return true se o carregamento foi bem-sucedido, false caso contrário
     */
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

    /**
     * Carrega um jogo salvo de um arquivo e atualiza o estado atual.
     *
     * @param fileName caminho do arquivo a ser carregado
     * @return true se o carregamento foi bem-sucedido, false caso contrário
     */
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

    /**
     * Salva o jogo atual em um arquivo.
     *
     * @param fileName caminho do arquivo para salvar
     * @return true se o salvamento foi bem-sucedido, false caso contrário
     */
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

    /**
     * Verifica se há uma peça na posição especificada.
     *
     * @param row linha do tabuleiro
     * @param col coluna do tabuleiro
     * @return true se existir uma peça, false caso contrário
     */
    public boolean havePiece(int row, int col) {
        if (game.getPiece(row, ColumnType.letra(col)) != null) {
            return true;
        }
        return false;
    }

    /**
     * Verifica se ambos os reis estão presentes no tabuleiro.
     *
     * @return true se os dois reis estiverem presentes, false caso contrário
     */
    public boolean checkKings (){
        return game.checkKings();
    }

    /**
     * Obtém a peça na posição especificada.
     *
     * @param row linha do tabuleiro
     * @param col coluna do tabuleiro
     * @return peça na posição ou null se não existir
     */
    public Piece getPiece(int row, int col) {
        Piece temp;
        temp = game.getPiece(row, ColumnType.letra(col));
        if (temp == null) {
            return null;
        }
        return temp;
    }

    public int mudarNumeros(int x) {
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

    /**
     * Define o tipo de peça para promoção de um peão.
     *
     * @param rowPiece linha da peça que será promovida
     * @param colPiece coluna da peça que será promovida
     * @param pt tipo da peça para promover
     */
    public void getTypePromote(double rowPiece, double colPiece, String pt){
        game.getTypePromote(getRow(colPiece), getColumn(rowPiece), PieceType.translate(pt));
        pcs.firePropertyChange(GAME_VALUE, null, null);
    }

    /**
     * Verifica movimentos que podem parar um cheque ou xeque-mate para um rei de certa cor.
     *
     * @param kingColor cor do rei a ser verificado
     * @return lista de movimentos possíveis para evitar cheque/xeque-mate
     */
    public List<String> checkStopCheckMate(PieceColor kingColor) {
        return game.checkStopCheckMate(kingColor);
    }

    /**
     * Verifica se ocorreu afogamento do rei (drowned king).
     *
     * @return true se o rei está afogado, false caso contrário
     */
    public boolean drownedKing(){
        return game.drownedKing();
    }

    /**
     * Verifica se há falta de material para continuar o jogo.
     *
     * @return true se houver falta de material, false caso contrário
     */
    public boolean lackOfMaterial(){
        return game.lackOfMaterial();
    }

    /**
     * Obtém os movimentos possíveis para uma peça específica.
     *
     * @param p peça a ser consultada
     * @return lista de movimentos possíveis em formato string
     */
    public List<String> getPossibleMoves(Piece p){
        return game.getPossibleMoves(p);
    }

    /**
     * Verifica se houve xeque-mate para o jogador atual.
     *
     * @return true se for xeque-mate, false caso contrário
     */
    public boolean checkMate(){
        if (game.checkMate()) {
            ModelLog.getInstance().log("CheckMate de " + getCurrentPlayer());
            return true;
        }
        return false;
    }

    /**
     * Promove o peão para uma nova peça.
     *
     * @param row linha da peça
     * @param col coluna da peça
     * @param newPiece tipo da nova peça
     * @return true se a promoção foi realizada com sucesso, false caso contrário
     */
    public boolean promotePawn(int row, ColumnType col, PieceType newPiece){
        if(game.getTypePromote(row,col,newPiece)) {
            pcs.firePropertyChange(GAME_VALUE, null, null);
            pcs.firePropertyChange(PLAYER_VALUE, null, null);
            return true;
        }
        return false;
    }

    /**
     * Verifica se o jogo acabou.
     *
     * @return true se o jogo terminou, false caso contrário
     */
    public boolean gameOver(){
        if(game.gameOver()){
            return true;
        }
        return false;
    }

    /**
     * Define a flag de check.
     *
     * @param value true para indicar cheque, false caso contrário
     */
    public void setChecked(boolean value) {
        game.setChecked(value);
    }

    /**
     * Verifica se o jogador está em cheque.
     *
     * @param playerTurn nome do jogador a ser verificado
     * @return true se estiver em cheque, false caso contrário
     */
    public boolean check(String playerTurn){
        if(game.check(playerTurn)){
            setChecked(true);
            return true;
        }
        setChecked(false);
        return false;
    }

    /**
     * Cria um novo gerenciador de comandos para o controle de ações.
     */
    public void newCommandManager() {
        cm = new CommandManager();
    }

    /**
     * Realiza um movimento no tabuleiro.
     *
     * @param rowPiece linha inicial da peça
     * @param colPiece coluna inicial da peça
     * @param row linha destino da peça
     * @param col coluna destino da peça
     * @return tipo do movimento realizado
     */
    public MoveType move(double rowPiece, double colPiece, double row, double col) {
        MoveType result;
        result = cm.invokeCommand(new MoveCommand(game, (int)colPiece, ColumnType.letra((int)rowPiece), (int)col, ColumnType.letra((int)row)));
        if(result != MoveType.FALSE){
            ModelLog.getInstance().log("Movimento: de " + ColumnType.letra((int)rowPiece) + mudarNumeros((int)colPiece) + " para " + ColumnType.letra((int)row) + mudarNumeros((int)col));
            incNumMovements();
            pcs.firePropertyChange(PLAYER_VALUE, null, null);
        }
        pcs.firePropertyChange(GAME_VALUE, null, null);
        return result;
    }
    /**
     * Verifica se existe movimento para desfazer.
     *
     * @return true se for possível desfazer, false caso contrário
     */
    public boolean hasUndo() { return cm.hasUndo(); }

    /**
     * Desfaz o último movimento feito.
     *
     * @return true se teve sucesso, false caso contrário
     */
    public boolean undo() {
        if (cm.undo()) {
            pcs.firePropertyChange(GAME_VALUE, null, null);
            pcs.firePropertyChange(PLAYER_VALUE, null, null);
            return true;
        }
        return false;
    }
    /**
     * Verifica se existe movimento para refazer.
     *
     * @return true se for possível refazer, false caso contrário
     */
    public boolean hasRedo() { return cm.hasRedo(); }

    /**
     * Refaz o último movimento desfeito.
     *
     * @return true se teve sucesso, false caso contrário
     */
    public boolean redo() {
        if (cm.redo()) {
            pcs.firePropertyChange(GAME_VALUE, null, null);
            pcs.firePropertyChange(PLAYER_VALUE, null, null);
            return true;
        }
        return false;
    }
}