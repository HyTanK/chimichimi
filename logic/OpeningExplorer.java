package logic;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import main.Main;
import ui.OpeningScene;

public class OpeningExplorer {
    private final OpeningScene scene;
    private String pressedKey = null;
    private String errorMessage = "";
    private String currentTypingText = "";
    private Timer typingTimer;
    private float effectAlpha = 0.0f;
    private int rawX = -100;
    private int rawY = -100;

    public OpeningExplorer(OpeningScene scene) {
        this.scene = scene;
    }

    public void handleMousePressed(int x, int y) {
        int baseY;
        int ox;
        int mode = this.scene.getSceneMode();
        int tw = 180;
        
        if (mode == 1) {
            ox = this.scene.getWidth() / 2 - 90;
            baseY = 450;
        } else if (mode == 2) {
            ox = this.scene.getCurImgX() + this.scene.getCurFw();
            baseY = 60;
        } else if (mode == 3) {
            ox = (this.scene.getWidth() - tw) / 2;
            baseY = this.scene.getHeight() / 2 - 100;
        } else {
            return;
        }
        
        String[] keys = new String[] { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "決定" };
        
        for (int i = 0; i < keys.length; i++) {
            int r = i / 3;
            int c = i % 3;
            int bx = ox + 25 + c * 50;
            int by = baseY + 70 + r * 45;
            int bw = "決定".equals(keys[i]) ? 90 : 40;
            int bh = 35;
            
            if (x >= bx && x <= bx + bw && y >= by && y <= by + bh) {
                this.pressedKey = keys[i];
                Main.audio.playSe("tan.mp3");
                this.scene.repaint();
                return;
            }
        }
    }

    public void handleMouseReleased() {
        if (this.pressedKey != null) {
            String code = this.scene.getInputCode();
            if (this.pressedKey.equals("決定")) {
                this.checkPasscode(code);
            } else if (code.length() < 4) {
                this.scene.setInputCode(code + this.pressedKey);
            }
            this.pressedKey = null;
            this.scene.repaint();
        }
    }

    private void checkPasscode(String code) {
        if ("0306".equals(code)) {
            this.errorMessage = "UNLOCKED!";
            Main.audio.playSe("correct.mp3");
            this.scene.repaint();
            
            Timer t1 = new Timer(500, e -> {
                try {
                    BufferedImage openImg = ImageIO.read(this.getClass().getResource("/images/open.png"));
                    this.scene.setFullSheet(openImg);
                    this.scene.repaint();
                } catch (Exception ex) {
                    System.err.println("open.pngの読み込みに失敗しました: " + ex.getMessage());
                }
            });
            t1.setRepeats(false);
            t1.start();
            
            Timer t2 = new Timer(1500, e -> Main.showHome());
            t2.setRepeats(false);
            t2.start();
            
        } else if (code.length() >= 4) {
            Main.audio.playSe("error.mp3");
            this.scene.setInputCode("");
        }
    }

    public void checkClick(int x, int y) {
        if (x < 250 || this.scene.getSceneMode() != 2) {
            return;
        }
        
        this.rawX = x;
        this.rawY = y;
        
        double scale = ((double) this.scene.getWidth() - 250.0) / 1000.0;
        int rx = (int) ((double) (x - 250) / scale);
        int ry = (int) ((double) y / scale);
        
        this.startClickEffect();
        
        if (this.hit(rx, ry, 466, 271)) {
            this.startWritingHint("記念コインだ。シリアルナンバーは1213。");
        } else if (this.hit(rx, ry, 552, 277)) {
            this.startWritingHint("カレンダーの３月６日に大きな〇。ボクの誕生日だ。");
        } else if (this.hit(rx, ry, 288, 382)) {
            this.startWritingHint("ママと撮った写真。日付は８月25日。");
        } else if (this.hit(rx, ry, 234, 472)) {
            this.startWritingHint("カバンの缶バッジが左から2468。");
        } else if (this.hit(rx, ry, 442, 307)) {
            this.startWritingHint("本棚に3639の落書き。");
        }
        this.scene.repaint();
    }

    private boolean hit(int rx, int ry, int tx, int ty) {
        return Math.abs(rx - tx) < 50 && Math.abs(ry - ty) < 50;
    }

    private void startWritingHint(String text) {
        if (this.scene.getNoteList().contains(text)) {
            return;
        }
        this.currentTypingText = "";
        if (this.typingTimer != null) {
            this.typingTimer.stop();
        }
        
        this.typingTimer = new Timer(120, e -> {
            if (this.currentTypingText.length() < text.length()) {
                this.currentTypingText = text.substring(0, this.currentTypingText.length() + 1);
                Main.audio.playSe("tan.mp3");
            } else {
                this.scene.addNote(text);
                this.currentTypingText = "";
                this.typingTimer.stop();
            }
            this.scene.repaint();
        });
        this.typingTimer.start();
    }

    private void startClickEffect() {
        this.effectAlpha = 0.8f;
        Timer fxTimer = new Timer(30, null);
        fxTimer.addActionListener(e -> {
            this.effectAlpha -= 0.05f;
            if (this.effectAlpha <= 0.0f) {
                this.effectAlpha = 0.0f;
                fxTimer.stop();
            }
            this.scene.repaint();
        });
        fxTimer.start();
    }

    public void drawEffect(Graphics2D g2d) {
        if (this.effectAlpha <= 0.0f) {
            return;
        }
        AffineTransform old = g2d.getTransform();
        g2d.setTransform(new AffineTransform());
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.effectAlpha));
        g2d.setColor(new Color(255, 255, 200, 180));
        
        int sz = (int) ((0.8f - this.effectAlpha) * 150.0f) + 20;
        g2d.fillOval(this.rawX - sz / 2, this.rawY - sz / 2, sz, sz);
        
        g2d.setTransform(old);
    }

    public String getCurrentTypingText() {
        return this.currentTypingText;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getPressedKey() {
        return this.pressedKey;
    }
}
