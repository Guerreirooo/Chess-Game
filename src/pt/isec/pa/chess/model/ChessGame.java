package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.*;
import pt.isec.pa.chess.model.data.pieces.*;

import java.io.*;
import java.util.List;

/**
 * A classe {@code ChessGame} representa um jogo de xadrez completo,
 * com toda a lógica incluida.
 * <p>
 * Esta classe suporta a serialização para armazenamento e carregamento
 * do estado do jogo.
 * </p>
 */

public class ChessGame implements Serializable {
    private Board board;
    private Player white, black;
    private PieceColor playerColor;
    /** Indica se o rei atual está em xeque. */
    boolean isCheck = false;

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que inicializa um novo jogo de xadrez
     * e o jogador branco a começar.
     */
    public ChessGame(){
        board = new Board();
        setCurrentPlayer(PieceColor.WHITE);
    }

    /**
     * Altera os valores da instância atual com base num objeto {@code ChessGame} fornecido.
     *
     * @param temp o objeto {@code ChessGame} de onde copiar os valores.
     */
    public void alterarValores(ChessGame temp) {
        board = temp.board;
        white = temp.white;
        black = temp.black;
        playerColor = temp.playerColor;
    }

    public void initGame(){
        board.initGame();
    }

    /**
     * Inicializa o jogo com um tabuleiro vazio.
     */
    public void initGameEmpty(){
        board.initGameEmpty();
    }

    /**
     * Inicializa o jogo com os nomes dos jogadores.
     *
     * @param blackName nome do jogador com peças pretas.
     * @param whiteName nome do jogador com peças brancas.
     */
    public void initGame(String blackName, String whiteName){
        setBlackName(blackName);
        setWhiteName(whiteName);
        board.initGame();
    }

    /**
     * Define o nome do jogador preto.
     *
     * @param blackName nome a definir.
     */
    public void setBlackName(String blackName){
        black = new Player(PieceColor.BLACK, blackName);
    }

    /**
     * Define o nome do jogador branco.
     *
     * @param whiteName nome a definir.
     */
    public void setWhiteName(String whiteName){
        white = new Player(PieceColor.WHITE, whiteName);
    }

    /**
     * Define o jogador atual.
     *
     * @param color cor do jogador a jogar.
     */
    public void setCurrentPlayer(PieceColor color){
        playerColor = color;
    }

    /**
     * Devolve a cor do jogador atual.
     *
     * @return cor do jogador atual.
     */
    public PieceColor getCurrentPlayer(){
        return playerColor;
    }

    /**
     * Devolve a peça na posição indicada.
     *
     * @param row linha da peça.
     * @param col coluna da peça.
     * @return a peça na posição especificada, ou {@code null} se não existir.
     */
    public Piece getPiece(int row, ColumnType col) {
        return board.getPiece(row,col);
    }
    @Override
    public String toString(){
        return board.toString();
    }

    /**
     * Guarda o estado parcial do jogo num ficheiro de texto.
     *
     * @param fileName nome do ficheiro.
     * @return {@code true} se a operação for bem-sucedida, {@code false} caso contrário.
     */
    public boolean savePartialGame(String fileName) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

            if (getCurrentPlayer() == null || board == null) {
                return false;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(getCurrentPlayer()).append(",\n");
            for (Piece p: board.getPiecesList()) {
                sb.append(p.toString()).append(",");
            }

            pw.write(sb.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pw != null)
                pw.close();
        }
    }

    /**
     * Carrega um estado parcial de jogo a partir de um ficheiro de texto.
     *
     * @param fileName nome do ficheiro.
     * @return {@code true} se a operação for bem-sucedida, {@code false} caso contrário.
     */
    public boolean loadPartialGame(String fileName) {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            StringBuilder data = new StringBuilder();

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                data.append(line);
            }

            if (data.isEmpty()) {
                return false;
            }

            ChessGame chessgame = new ChessGame();
            Board b = new Board();
            int length = data.toString().split(",").length;

            if (length < 4) {
                return false;
            }

            PieceColor color = PieceColor.translate(data.toString().split(",")[0]);

            if (color == null) {
                return false;
            }

            for (String peca : data.toString().replaceAll("", "").split(",")) {
                b.addPiece(peca);
            }

            chessgame.board = b;
            chessgame.setCurrentPlayer(color);

            this.board = chessgame.board;
            this.setCurrentPlayer(chessgame.getCurrentPlayer());
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Move uma peça de uma posição para outra, validando regras do xadrez.
     *
     * @param rowPiece linha atual da peça.
     * @param colPiece coluna atual da peça.
     * @param row linha destino.
     * @param col coluna destino.
     * @return tipo do movimento realizado (TRUE, FALSE, PROMOTE).
     */
    public MoveType move(int rowPiece, ColumnType colPiece, int row, ColumnType col) {
        Piece piece = board.getPiece(rowPiece, colPiece);

        if (piece == null || piece.getColor() != getCurrentPlayer() || row > getBoardSize() || row < 1) {
            return MoveType.FALSE;
        }

        PieceColor color = board.checkColorPosition(row, col);
        String nextMove = col.toString() + row;

        if(piece.getPieceType() == PieceType.PAWN && piece.getColor() == PieceColor.BLACK && row == 1){
            if(board.move(piece, row, col, isCheck)){
                return MoveType.PROMOTE;
            }
        }
        else if(piece.getPieceType() == PieceType.PAWN && piece.getColor() == PieceColor.WHITE && row == getBoardSize()){
            if(board.move(piece, row, col, isCheck)){
                return MoveType.PROMOTE;
            }
            return MoveType.TRUE;
        }

        if(piece.getPieceType() == PieceType.KING && color == PieceColor.BLACK && getPiece(row, col).getPieceType() == PieceType.ROOK){
            if(col == ColumnType.h){
                if (board.roque(piece,board.getPiece(row,col))){
                    piece.setCol(ColumnType.g);
                    board.getPiece(row,col).setCol(ColumnType.f);
                    if (getCurrentPlayer() == PieceColor.WHITE) {
                        setCurrentPlayer(PieceColor.BLACK);
                    }
                    else {
                        setCurrentPlayer(PieceColor.WHITE);
                    }
                    return MoveType.TRUE;
                }
            }
            else if(col == ColumnType.a){
                if (board.roque(piece,board.getPiece(row,col))){
                    piece.setCol(ColumnType.c);
                    board.getPiece(row,col).setCol(ColumnType.d);
                    if (getCurrentPlayer() == PieceColor.WHITE) {
                        setCurrentPlayer(PieceColor.BLACK);
                    }
                    else {
                        setCurrentPlayer(PieceColor.WHITE);
                    }
                    return MoveType.TRUE;
                }
            }
        }
        else if(piece.getPieceType() == PieceType.KING && color == PieceColor.WHITE && getPiece(row, col).getPieceType() == PieceType.ROOK) {
            if (col == ColumnType.h) {
                if (board.roque(piece, board.getPiece(row, col))) {
                    piece.setCol(ColumnType.g);
                    board.getPiece(row, col).setCol(ColumnType.f);
                    if (getCurrentPlayer() == PieceColor.WHITE) {
                        setCurrentPlayer(PieceColor.BLACK);
                    }
                    else {
                        setCurrentPlayer(PieceColor.WHITE);
                    }
                    return MoveType.TRUE;
                }
            } else if (col == ColumnType.a) {
                if (board.roque(piece, board.getPiece(row, col))) {
                    piece.setCol(ColumnType.c);
                    board.getPiece(row, col).setCol(ColumnType.d);
                    if (getCurrentPlayer() == PieceColor.WHITE) {
                        setCurrentPlayer(PieceColor.BLACK);
                    }
                    else {
                        setCurrentPlayer(PieceColor.WHITE);
                    }
                    return MoveType.TRUE;
                }
            }
        }

        if (board.move(piece, row, col, isCheck)) {
            if (getCurrentPlayer() == PieceColor.WHITE) {
                setCurrentPlayer(PieceColor.BLACK);
            }
            else {
                setCurrentPlayer(PieceColor.WHITE);
            }
            return MoveType.TRUE;
        }

        return MoveType.FALSE;
    }

    /**
     * Adiciona uma peça ao tabuleiro.
     *
     * @param pt tipo da peça.
     * @param row linha da peça.
     * @param col coluna da peça.
     * @param color cor da peça.
     */
    public void addPiece(PieceType pt, int row, ColumnType col, PieceColor color) {
        board.addPiece(pt, row, col, color);
    }

    /**
     * Move uma peça sem confirmação nem validação completa das regras.
     *
     * @param rowP linha atual.
     * @param colP coluna atual.
     * @param row linha destino.
     * @param col coluna destino.
     * @return {@code true} se o movimento for possível, {@code false} caso contrário.
     */
    public boolean moveWithoutConfirmation(int rowP, ColumnType colP, int row, ColumnType col) {
        Piece p = board.getPiece(rowP, colP);
        return board.moveWithoutConfirmation(p, row, col);
    }

    /**
     * Verifica se o jogo terminou por falta de material para dar mate.
     *
     * @return {@code true} se for impossível dar mate, {@code false} caso contrário.
     */
    public boolean lackOfMaterial(){
        return board.lackOfMaterial();
    }

    /**
     * Devolve o tamanho do tabuleiro.
     *
     * @return número de linhas (ou colunas) do tabuleiro.
     */
    public int getBoardSize(){
        return board.getBoardSize();
    }

    /**
     * Promove um peão à peça indicada.
     *
     * @param row linha do peão.
     * @param col coluna do peão.
     * @param pt tipo de peça para promoção.
     * @return {@code true} se a promoção for bem-sucedida.
     */

    public boolean getTypePromote(int row, ColumnType col, PieceType pt){
        boolean temp = board.promotePawn(getPiece(row, col), pt);
        if (getCurrentPlayer() == PieceColor.WHITE) {
            setCurrentPlayer(PieceColor.BLACK);
        }
        else {
            setCurrentPlayer(PieceColor.WHITE);
        }
        return temp;
    }

    /**
     * Verifica se o jogo terminou por xeque-mate.
     *
     * @return {@code true} se houver xeque-mate, {@code false} caso contrário.
     */
    public boolean gameOver(){
        if(board.checkMate()){
            return true;
        }
        return false;
    }

    /**
     * Verifica se existem movimentos para escapar ao xeque-mate.
     *
     * @param kingColor cor do rei sob ameaça.
     * @return lista de movimentos possíveis para escapar.
     */
    public List<String> checkStopCheckMate(PieceColor kingColor) {
        return board.checkStopCheckMate(kingColor);
    }

    /**
     * Verifica se o rei está afogado (empate por afogamento).
     *
     * @return {@code true} se houver afogamento, {@code false} caso contrário.
     */
    public boolean drownedKing(){
        return board.drownedKing();
    }

    /**
     * Verifica se há xeque-mate no estado atual.
     *
     * @return {@code true} se for xeque-mate.
     */
    public boolean checkMate(){
        return board.checkMate();
    }

    /**
     * Atualiza a flag do check.
     *
     * @param value {@code true} para indicar xeque.
     */
    public void setChecked(boolean value) {
        isCheck = value;
    }

    /**
     * Verifica se o rei atual está em xeque.
     *
     * @return {@code true} se estiver em xeque.
     */
    public boolean getCheck() {
        return isCheck;
    }

    /**
     * Devolve os movimentos possíveis para uma peça.
     *
     * @param p peça a analisar.
     * @return lista de movimentos válidos.
     */
    public List<String> getPossibleMoves(Piece p){
        if (isCheck) {
            return board.getPossibleMoves(p.getRow(), p.getColumn(), isCheck);
        }
        return board.getPossibleMoves(p);
    }

    /**
     * Verifica se ambos os reis estão presentes no tabuleiro.
     *
     * @return {@code true} se ambos os reis estiverem presentes.
     */
    public boolean checkKings (){
        List<Piece> Pieces = board.getPiecesList();
        int numKings = 0;
        PieceColor helper = PieceColor.BLACK;
        for(int i=0; i<2; i++) {
            for (Piece p : Pieces) {
                if (p.getPieceType() == PieceType.KING && p.getColor() == helper) {
                    numKings++;
                    break;
                }
            }
            helper = PieceColor.WHITE;
        }
        if(numKings >= 2)
            return true;
        return false;
    }

    /**
     * Verifica se o jogador atual está em xeque.
     *
     * @param playerTurn cor do jogador atual.
     * @return {@code true} se o jogador estiver em xeque.
     */
    public boolean check(String playerTurn){
        PieceColor temp;
        temp = PieceColor.translate(playerTurn);
        if(board.check(temp ,board)){
            return true;
        }
        return false;
    }
}
