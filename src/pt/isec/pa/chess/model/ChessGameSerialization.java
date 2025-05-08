package pt.isec.pa.chess.model;

import java.io.*;

public class ChessGameSerialization {
    private ChessGameSerialization() {}

    // Serialização
    public static boolean save(String filename, ChessGame obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename)))
        {
            oos.writeObject(obj);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving data");
            return false;
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
}
