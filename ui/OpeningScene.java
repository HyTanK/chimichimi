package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import logic.OpeningController;
import logic.OpeningExplorer;

public class OpeningScene extends JPanel {
    private Image logoTank;
    private Image imgLock;
    private BufferedImage fullSheet;
    private String displayMessage = "";
    private float alpha = 0.0f;
    private int sceneMode = 0;
    private Rectangle cameraRect = new Rectangle(0, 0, 1000, 1000);
    private final List<String> noteList = new ArrayList<>();
    private String inputCode = "";
    private boolean showOverlayTenkey = false;
    private boolean isAlertMessage = false;
    private final OpeningExplorer explorer;
    private final OpeningPainter painter;
    private int curImgX;
    private int curFw;
    private double curScale;

    public OpeningScene() {
        this.setBackground(Color.WHITE);
        this.explorer = new OpeningExplorer(this);
        this.painter = new OpeningPainter(this);
        this.loadResources();
        this.addMouseListener(new OpeningMouseHandler(this, this.explorer));
        new OpeningController(this).startAnimation();
    }

    private void loadResources() {
        try {
            this.logoTank = new ImageIcon(this.getClass().getResource("/images/HyTank.png")).getImage();
            this.imgLock = new ImageIcon(this.getClass().getResource("/images/lock.png")).getImage();
            this.fullSheet = ImageIO.read(this.getClass().getResource("/images/house.png"));
        } catch (Exception e) {
            System.err.println("リソース読み込み失敗: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.updateLayout();
        this.painter.paint((Graphics2D) g, this.getWidth(), this.getHeight(), this.explorer);
    }

    public void updateLayout() {
        int w = this.getWidth();
        int h = this.getHeight();
        this.curScale = Math.min(((double) w - 200.0) / 1000.0, (double) h / 800.0);
        this.curFw = (int) (1000.0 * this.curScale);
        this.curImgX = (w - this.curFw) / 2;
    }

    public boolean isAlertMessage() { return this.isAlertMessage; }
    public void setAlertMessage(boolean alert) { this.isAlertMessage = alert; this.repaint(); }
    public int getSceneMode() { return this.sceneMode; }
    public void setSceneMode(int m) { this.sceneMode = m; this.repaint(); }
    public float getAlpha() { return this.alpha; }
    public void setAlpha(float a) { this.alpha = a; this.repaint(); }
    public String getDisplayMessage() { return this.displayMessage; }
    public void setDisplayMessage(String m) { this.displayMessage = m; this.repaint(); }
    public Image getLogoTank() { return this.logoTank; }
    public BufferedImage getFullSheet() { return this.fullSheet; }
    public void setFullSheet(BufferedImage s) { this.fullSheet = s; this.repaint(); }
    public Rectangle getCameraRect() { return this.cameraRect; }
    public void setCameraRect(Rectangle r) { this.cameraRect = r; this.repaint(); }
    public List<String> getNoteList() { return this.noteList; }
    public void addNote(String hint) { if (hint != null && !this.noteList.contains(hint)) { this.noteList.add(hint); } this.repaint(); }
    public String getInputCode() { return this.inputCode; }
    public void setInputCode(String c) { this.inputCode = c; this.repaint(); }
    public boolean isShowOverlayTenkey() { return this.showOverlayTenkey; }
    public void setShowOverlayTenkey(boolean b) { this.showOverlayTenkey = b; this.repaint(); }
    public int getCurImgX() { return this.curImgX; }
    public int getCurFw() { return this.curFw; }
    public double getCurScale() { return this.curScale; }
    public Image getImgLock() { return this.imgLock; }
}
