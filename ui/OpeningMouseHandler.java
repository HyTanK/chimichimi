package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import logic.OpeningExplorer;

public class OpeningMouseHandler extends MouseAdapter {
    private final OpeningScene scene;
    private final OpeningExplorer explorer;

    public OpeningMouseHandler(OpeningScene scene, OpeningExplorer explorer) {
        this.scene = scene;
        this.explorer = explorer;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mode = this.scene.getSceneMode();
        if (mode == 3 || mode == 2 || (mode == 1 && this.scene.isShowOverlayTenkey())) {
            this.explorer.handleMousePressed(e.getX(), e.getY());
        }
        if (mode == 2) {
            this.explorer.checkClick(e.getX(), e.getY());
        }
        this.scene.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int mode = this.scene.getSceneMode();
        if (mode >= 2 || (mode == 1 && this.scene.isShowOverlayTenkey())) {
            this.explorer.handleMouseReleased();
        }
        this.scene.repaint();
    }
}
