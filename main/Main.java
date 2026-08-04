package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import app.AppLauncher;
import audio.AudioPlayer;
import ui.OpeningScene;

public class Main {
    public static final String IMG_PATH = "/images/";
    public static final String SND_PATH = "/sounds/";
    public static final AudioPlayer audio = new AudioPlayer();
    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("MamaPhone");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 900);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            Main.showOpening();
        });
    }

    public static void showOpening() {
        Main.changePanel(new OpeningScene());
    }

    public static void showHome() {
        Main.changePanel(new AppLauncher());
    }

    public static void restoreMainFrame() {
        if (frame != null) {
            frame.setVisible(true);
            frame.toFront();
        }
    }

    private static void changePanel(JPanel panel) {
        if (frame == null || panel == null) {
            return;
        }
        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        panel.requestFocusInWindow();
    }
}
