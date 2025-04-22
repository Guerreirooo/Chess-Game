package pt.isec.pa.chess;

import javafx.application.Application;
import pt.isec.pa.chess.ui.MainJFX;

public class ChessMain {
    public static void main(String[] args) {
        System.out.println("PA Chess Game");
        Application.launch(MainJFX.class, args);
        /*
        ChessGame chessgame = new ChessGame();
        chessgame.board.initGame();
        System.out.println(chessgame);
        /*ChessGame gameTemp = ChessGameSerialization.load("ChessGame.txt");
        if (gameTemp != null) {
            game = gameTemp;
        }*/

        /*System.out.println(game.board);*/
        /*ChessGameSerialization.save("teste.txt", chessgame);

        String coiso = "BLACK,\n" +
                "Ra1*,Nb1,Bc1,Ke1*,Ng1,Rh1*,Pa2,Pb2,Pc2,Pd2,Pf2,Pg2,Ph2,\n" +
                "Qf3,Bc4,Pe4,qh4,pe5,pa7,pb7,pc7,pd7,pf7,pg7,ph7,ra8*,nb8,\n" +
                "bc8,ke8*,bf8,ng8,rh8*";



        System.out.println(chessgame.move(1, ColumnType.a, 2, ColumnType.b));
        System.out.println(chessgame.move(8, ColumnType.b, 6, ColumnType.c));
        System.out.println(chessgame.move(8, ColumnType.c, 7, ColumnType.b));
        System.out.println(chessgame.move(8, ColumnType.d, 7, ColumnType.d));
        System.out.println(chessgame.move(8, ColumnType.e, 8, ColumnType.a));
        System.out.println(chessgame);
        chessgame = ChessGameSerialization.load("teste.txt");
        //chessgame = ChessGameSerialization.loadPartialGame("teste.txt");

        if (chessgame == null) {
            System.err.println("Error in loading partial game file");
            return;
        }

        System.out.println(chessgame);
        //System.out.println(chessgame.board);
        System.out.println(chessgame.board.checkMate());*/
    }
}
