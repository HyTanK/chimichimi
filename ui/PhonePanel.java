package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class PhonePanel extends JPanel {
	private BufferedImage imgPhone;
	private BufferedImage imgUturu;
	private BufferedImage imgKabegami;
	private BufferedImage[] icons;
	private Rectangle[] iconBaseRects;
	private Consumer<Integer> onIconClick;
	private Runnable onAreaClick;

	public WeatherDrawer drawer = new WeatherDrawer();
	private int draggingIdx = -1;
	private Point lastP;
	private JButton btn;
	private JSlider brightnessSlider;
	private float screenAlpha = 0.0f;

	private int batteryLevel = 85;
	private int antennaBars = 4;
	private String networkType = "5G";
	private long lastStatusUpdate = System.currentTimeMillis();

	public PhonePanel(BufferedImage phone, BufferedImage[] icons, Rectangle[] rects, Consumer<Integer> callback) {
		this.imgPhone = phone;
		this.icons = icons;
		this.iconBaseRects = rects;
		this.onIconClick = callback;
		this.setLayout(null);

		try {
			this.imgUturu = ImageIO.read(this.getClass().getResource("/images/uturu.png"));
		} catch (Exception e2) {
			System.err.println("uturu.pngの読み込みに失敗しました。");
		}

		this.btn = new JButton("地域設定");
		this.btn.setContentAreaFilled(false);
		this.btn.setFocusPainted(false);
		this.btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.btn.setForeground(Color.BLACK);
		this.btn.setFont(new Font("SansSerif", Font.BOLD, 14));
		this.btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (PhonePanel.this.onAreaClick != null) {
					PhonePanel.this.onAreaClick.run();
				}
			}
		});

		this.brightnessSlider = new JSlider(0, 100, 0);
		this.brightnessSlider.setOpaque(false);
		this.brightnessSlider.addChangeListener(e -> {
			this.screenAlpha = (float) this.brightnessSlider.getValue() / 100.0f;
			if (this.btn != null) {
				this.btn.setVisible(this.screenAlpha < 1.0f);
			}
			this.repaint();
		});

		this.add(this.btn);
		this.add(this.brightnessSlider);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				PhonePanel.this.updateButtonBounds();
			}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				PhonePanel.this.lastP = e.getPoint();
				PhonePanel.this.draggingIdx = PhonePanel.this.findIdx(e.getPoint());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (PhonePanel.this.draggingIdx >= 0 && PhonePanel.this.draggingIdx <= 5
						&& PhonePanel.this.lastP != null && PhonePanel.this.lastP.distance(e.getPoint()) < 5.0) {
					PhonePanel.this.onIconClick.accept(PhonePanel.this.draggingIdx);
				}
				PhonePanel.this.draggingIdx = -1;
			}
		});
	}

	public void setOnAreaClick(Runnable r) {
		this.onAreaClick = r;
	}

	public void setKabegami(BufferedImage img) {
		this.imgKabegami = img;
		this.repaint();
	}

	private double getScale() {
		if (this.imgPhone == null)
			return 1.0;
		return Math.min((double) this.getWidth() / this.imgPhone.getWidth(),
				(double) this.getHeight() / this.imgPhone.getHeight());
	}

	private int findIdx(Point p) {
		double s = this.getScale();
		int fw = (int) (this.imgPhone.getWidth() * s);
		int fh = (int) (this.imgPhone.getHeight() * s);
		int fx = (this.getWidth() - fw) / 2;
		int fy = (this.getHeight() - fh) / 2;

		for (int i = 0; i < this.iconBaseRects.length; i++) {
			Rectangle r = new Rectangle(
					fx + (int) (this.iconBaseRects[i].x * s),
					fy + (int) (this.iconBaseRects[i].y * s),
					(int) (this.iconBaseRects[i].width * s),
					(int) (this.iconBaseRects[i].height * s));
			if (r.contains(p))
				return i;
		}
		return -1;
	}

	private void updateButtonBounds() {
		if (this.btn == null || this.brightnessSlider == null || this.imgPhone == null)
			return;
		double s = this.getScale();
		int fw = (int) (this.imgPhone.getWidth() * s);
		int fh = (int) (this.imgPhone.getHeight() * s);
		int fx = (this.getWidth() - fw) / 2;
		int fy = (this.getHeight() - fh) / 2;

		this.btn.setBounds(fx + (int) (720.0 * s), fy + (int) (400.0 * s), (int) (200.0 * s), (int) (70.0 * s));

		int sliderW = (int) (600.0 * s);
		int sliderH = (int) (80.0 * s);
		int sliderX = fx + (int) (171.0 * s) + (int) ((782.0 * s - sliderW) / 2.0);
		int sliderY = fy + (int) (373.0 * s) + (int) (-100.0 * s);
		this.brightnessSlider.setBounds(sliderX, sliderY, sliderW, sliderH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double s = this.getScale();
		int fw = (int) (this.imgPhone.getWidth() * s);
		int fh = (int) (this.imgPhone.getHeight() * s);
		int fx = (this.getWidth() - fw) / 2;
		int fy = (this.getHeight() - fh) / 2;

		g2d.drawImage(this.imgPhone, fx, fy, fw, fh, null);

		int kX = fx + (int) (171.0 * s);
		int kY = fy + (int) (373.0 * s);
		int kW = (int) (782.0 * s);
		int kH = (int) (1180.0 * s);

		if (this.imgKabegami != null) {
			boolean showUI = this.screenAlpha < 1.0f;
			if (showUI) {
				this.drawPhoneHeader(g2d, kX, kY, kW, s);
			}

			Shape oldClip = g2d.getClip();
			g2d.setClip(kX, kY, kW, kH);
			g2d.drawImage(this.imgKabegami, kX, kY, kW, kH, null);

			if (this.screenAlpha > 0.0f) {
				g2d.setColor(new Color(0, 0, 0, (int) (255.0f * this.screenAlpha)));
				g2d.fillRect(kX, kY, kW, kH);
				if (this.screenAlpha >= 1.0f && this.imgUturu != null) {
					g2d.drawImage(this.imgUturu, kX, kY, kW, kH, null);
					g2d.setColor(new Color(0, 0, 0, 180));
					g2d.fillRect(kX, kY, kW, kH);
				}
			}
			g2d.setClip(oldClip);

			if (showUI) {
				this.drawer.draw(g2d, fx, fy, s);
				String[] appNames = new String[] { "BPM", "Fortune", "Dice", "News", "Rine", "Chimi" };

				for (int i = 0; i < this.icons.length; i++) {
					Rectangle r = new Rectangle(
							fx + (int) (this.iconBaseRects[i].x * s),
							fy + (int) (this.iconBaseRects[i].y * s),
							(int) (this.iconBaseRects[i].width * s),
							(int) (this.iconBaseRects[i].height * s));

					if (i == 4) {
						RineIconDrawer.draw(g2d, r, i == this.draggingIdx);
					} else if (this.icons[i] != null) {
						g2d.drawImage(this.icons[i], r.x, r.y, r.width, r.height, null);
					}

					g2d.setFont(new Font("SansSerif", Font.BOLD, (int) (35.0 * s)));
					String name = i < appNames.length ? appNames[i] : "";
					int fontWidth = g2d.getFontMetrics().stringWidth(name);
					int textX = r.x + r.width / 2 - fontWidth / 2;
					int textY = r.y + r.height + (int) (50.0 * s);

					g2d.setColor(new Color(0, 0, 0, 150));
					g2d.drawString(name, textX + 1, textY + 1);
					g2d.setColor(Color.WHITE);
					g2d.drawString(name, textX, textY);
				}
			}
		}
	}

	private void drawPhoneHeader(Graphics2D g2, int kX, int kY, int kW, double s) {
		this.updateStatus();
		int headerH = (int) (35.0 * s);
		int yOffset = (int) (-210.0 * s);
		int sideMargin = (int) (40.0 * s);
		int textY = kY + yOffset + headerH / 2 + (int) (10.0 * s);

		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(kX, kY + yOffset, kW, headerH);

		g2.setColor(Color.WHITE);
		g2.setFont(new Font("SansSerif", Font.BOLD, (int) (50.0 * s)));
		String timeStr = new SimpleDateFormat("HH:mm").format(new Date());
		g2.drawString(timeStr, kX + sideMargin, textY);

		int rightGroupX = kX + kW - (int) (300.0 * s) - sideMargin;
		g2.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, (int) (50.0 * s)));
		g2.drawString(this.networkType, rightGroupX, textY);

		for (int i = 0; i < 4; i++) {
			int barH = (int) (((i + 1) * 10) * s);
			g2.setColor(i < this.antennaBars ? Color.WHITE : new Color(255, 255, 255, 80));
			g2.fillRect(rightGroupX + (int) (100.0 * s) + (int) ((i * 13) * s), textY - barH, (int) (9.5 * s), barH);
		}
		int batW = (int) (80.0 * s);
		int batH = (int) (35.0 * s);
		int batX = kX + kW - batW - sideMargin - (int) (40.0 * s);
		int batY = textY - (int) (34.0 * s);
		g2.setColor(Color.WHITE);
		g2.drawRect(batX, batY, batW, batH);
		g2.fillRect(batX + batW, batY + (int) (4.0 * s), (int) (3.0 * s), (int) (6.0 * s));
		g2.setColor(this.batteryLevel <= 20 ? Color.RED : Color.GREEN);
		int innerW = (int) ((batW - 4) * ((double) this.batteryLevel / 100.0));
		g2.fillRect(batX + 2, batY + 2, innerW, batH - 3);
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("SansSerif", Font.PLAIN, (int) (25.0 * s)));
		String batText = this.batteryLevel + "%";
		int fontWidth = g2.getFontMetrics().stringWidth(batText);
		g2.drawString(batText, batX + batW / 2 - fontWidth / 2, batY + (int) (27.0 * s));
	}

	private void updateStatus() {
		long now = System.currentTimeMillis();
		if (now - this.lastStatusUpdate > 10000L) {
			if (Math.random() > 0.8 && this.batteryLevel > 1) {
				this.batteryLevel--;
			}
			this.antennaBars = 2 + (int) (Math.random() * 3.0);
			this.networkType = Math.random() > 0.8 ? "4G" : "5G";
			this.lastStatusUpdate = now;
		}
	}
}