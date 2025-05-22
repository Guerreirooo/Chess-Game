package pt.isec.pa.chess.ui.res;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SoundManager {
    private SoundManager() { }
    private static MediaPlayer mp;

    public static void playSequence(List<String> fileNames) {
        play(fileNames,0);
    }

    public static void play(List<String> fileNames, int index) {
        try {
            if (index >= fileNames.size())
                return;

            var url = SoundManager.class.getResource("sounds/en/" + fileNames.get(index));
            if (url == null) {
                play(fileNames, index + 1);
                return;
            }
            String path = url.toExternalForm();
            Media music = new Media(path);

            mp = new MediaPlayer(music);
            mp.setStartTime(Duration.ZERO);
            mp.setStopTime(music.getDuration());

            mp.setOnEndOfMedia(() -> play(fileNames, index + 1));

            mp.setAutoPlay(true);
        } catch (Exception e) {
            System.err.println("Error playing sound file");
        }
    }

    public static boolean getSound(String filename) {
        var url = SoundManager.class.getResource("sounds/en/" + filename);
        if (url == null)
            return false;
        return true;
    }
}