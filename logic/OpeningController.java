package logic;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Main;
import ui.OpeningScene;

public class OpeningController {
    private final OpeningScene scene;

    public OpeningController(OpeningScene scene) {
        this.scene = scene;
    }

    public void startAnimation() {
        new Thread(() -> {
            try {
                Main.audio.playSe("2s.mp3");
                this.scene.setSceneMode(0);
                this.scene.setAlpha(0.0f);
                Thread.sleep(500L);
                
                for (int i = 0; i <= 50; i++) {
                    this.scene.setAlpha((float) i / 50.0f);
                    Thread.sleep(40L);
                }
                Thread.sleep(1500L);
                
                for (int i = 50; i >= 0; i--) {
                    this.scene.setAlpha((float) i / 50.0f);
                    Thread.sleep(30L);
                }
                
                this.scene.setSceneMode(1);
                this.scene.setAlpha(1.0f);
                Main.audio.playBgm("opening.mp3");
                
                this.updateScene(0, 0, 1000, 1000, "ある日の一般家庭での風景");
                this.updateScene(580, 37, 290, 536, "ちょっとでかけてくるね");
                this.updateScene(46, 36, 492, 266, "分かったー。");
                this.updateScene(46, 36, 492, 266, "いってらっしゃい！");
                
                Main.audio.playSe("door.mp3");
                Thread.sleep(2000L);
                Main.audio.playSe("door2.mp3");
                Thread.sleep(2000L);
                
                this.updateScene(44, 322, 494, 250, "！");
                this.updateScene(44, 322, 494, 250, "ママのケータイだ。");
                this.updateScene(44, 322, 494, 250, "忘れていったんだ・・・");
                this.updateScene(44, 322, 494, 250, "こっそり見ちゃおっかな……");
                this.updateScene(43, 591, 794, 300, "へへへ、チャンスだぞ。");
                Thread.sleep(2500L);
                
                Main.audio.stopBgm();
                this.scene.setCameraRect(new Rectangle(0, 0, 1000, 1000));
                try {
                    BufferedImage phoneImg = ImageIO.read(this.getClass().getResource("/images/phone.png"));
                    this.scene.setFullSheet(phoneImg);
                    this.scene.setCameraRect(new Rectangle(0, 0, phoneImg.getWidth(), phoneImg.getHeight()));
                    this.scene.setSceneMode(3);
                    this.scene.setAlpha(1.0f);
                    this.scene.repaint();
                } catch (Exception e) {
                    System.err.println("phone.pngの読み込みに失敗しました: " + e.getMessage());
                }
                
                Thread.sleep(1000L);
                this.scene.setAlertMessage(true);
                this.typeMessage("暗証番号を入力してください……");
                Thread.sleep(1500L);
                this.scene.setAlertMessage(false);
                
                this.typeMessage("あんしょうばんごう？");
                Thread.sleep(1000L);
                this.typeMessage("知らないや・・・");
                Thread.sleep(1500L);
                
                Main.audio.stopBgm();
                Main.audio.playBgm("opening.mp3");
                try {
                    this.scene.setFullSheet(ImageIO.read(this.getClass().getResource("/images/findRoom.png")));
                    this.scene.setCameraRect(new Rectangle(0, 0, 1000, 1000));
                    this.scene.setSceneMode(2);
                    this.scene.setShowOverlayTenkey(true);
                    this.scene.setAlpha(1.0f);
                    this.scene.repaint();
                } catch (Exception e) {
                    System.err.println("findRoom.pngの読み込みに失敗しました: " + e.getMessage());
                }
                
                this.typeMessage("どこかにヒントがないかな？");
                Thread.sleep(1000L);
                this.typeMessage("部屋の中を探してみよう！");
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Opening-Logic-Thread").start();
    }

    public void updateScene(int x, int y, int w, int h, String text) throws InterruptedException {
        this.scene.setCameraRect(new Rectangle(x, y, w, h));
        this.scene.setDisplayMessage("");
        this.scene.repaint();
        Thread.sleep(500L);
        
        for (int i = 1; i <= text.length(); i++) {
            this.scene.setDisplayMessage(text.substring(0, i));
            Main.audio.playSe("tan.mp3");
            Thread.sleep(150L);
        }
        
        Thread.sleep(1500L);
        this.scene.setDisplayMessage("");
        this.scene.repaint();
    }

    public void typeMessage(String text) throws InterruptedException {
        this.scene.setDisplayMessage("");
        for (int i = 1; i <= text.length(); i++) {
            this.scene.setDisplayMessage(text.substring(0, i));
            Main.audio.playSe("tan.mp3");
            Thread.sleep(150L);
        }
    }
}
