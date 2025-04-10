package pt.isec.pa.chess.model.data;

import java.io.*;

public class ChessGameSerialization {
    private ChessGameSerialization() {}

    // Serialização
    public static void save(String filename, ChessGame obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename)))
        {
            oos.writeObject(obj);
        } catch (Exception e) {
            System.err.println("Error saving data");
        }
    }

    // Descerialização
    public static ChessGame load(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename)))
        {
            return (ChessGame) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error loading data");
        }
        return null;
    }

    public static void savePartialGame(String fileName, ChessGame chessgame) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

            if (chessgame.getCurrentPlayer() == null || chessgame.board == null) {
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(chessgame.getCurrentPlayer()).append(",\n");
            for (Piece p: chessgame.board.getPiecesList()) {
                sb.append(p.toString()).append(",");
            }

            pw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null)
                pw.close();
        }
    }

    public static ChessGame loadPartialGame(String fileName) {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            StringBuilder data = new StringBuilder();

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                data.append(line);
            }

            System.out.println(data.toString());

            if (data.isEmpty()) {
                return null;
            }

            ChessGame chessgame = new ChessGame();
            Board b = new Board();
            int length = data.toString().split(",").length;

            if (length < 4) {
                return null;
            }

            PieceColor color = PieceColor.translate(data.toString().split(",")[0]);

            if (color == null) {
                return null;
            }

            for (String peca : data.toString().split(",")) {
                b.addPiece(peca);
            }

            chessgame.board = b;
            chessgame.setCurrentPlayer(color);
            return chessgame;
        } catch (IOException e) {
            return null;
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
}
