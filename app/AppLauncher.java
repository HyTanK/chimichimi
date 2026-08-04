package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Fortune.fortune.UranaiApp;
import bpm.HighPrecisionBpmApp;
import dice.Dice;
import main.MainApp;
import ui.PhonePanel;
import ui.WeatherResult;
import ui.WeatherService;

public class AppLauncher extends JPanel {
	private BufferedImage imgPhone;
	private BufferedImage[] icons = new BufferedImage[6];
	private Rectangle[] iconBaseRects = new Rectangle[6];
	private String[] appNames = new String[] { "BPM", "Fortune", "Dice", "News", "Rine", "Chimi" };
	private PhonePanel phoneUI;
	private WeatherService weatherService = new WeatherService();

	public AppLauncher() {
		main.Main.audio.stopBgm();

		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);

		BufferedImage kabegami = null;
		try {
			this.imgPhone = ImageIO.read(this.getClass().getResource("/images/phone.png"));
			kabegami = ImageIO.read(this.getClass().getResource("/images/kabegami.png"));
			this.icons[0] = ImageIO.read(this.getClass().getResource("/images/bpm.png"));
			this.icons[1] = ImageIO.read(this.getClass().getResource("/images/fortune.png"));
			this.icons[2] = ImageIO.read(this.getClass().getResource("/images/dice.png"));
			this.icons[3] = ImageIO.read(this.getClass().getResource("/images/news.png"));
			this.icons[4] = ImageIO.read(this.getClass().getResource("/images/title.png"));
			this.icons[5] = ImageIO.read(this.getClass().getResource("/images/title.png"));
		} catch (Exception e) {
			System.err.println("画像の読み込みに失敗しました: " + e.getMessage());
		}

		this.iconBaseRects[0] = new Rectangle(280, 1100, 120, 120);
		this.iconBaseRects[1] = new Rectangle(490, 1100, 120, 120);
		this.iconBaseRects[2] = new Rectangle(700, 1100, 120, 120);
		this.iconBaseRects[3] = new Rectangle(280, 1300, 120, 120);
		this.iconBaseRects[4] = new Rectangle(490, 1300, 120, 120);
		this.iconBaseRects[5] = new Rectangle(700, 1300, 120, 120);

		this.phoneUI = new PhonePanel(this.imgPhone, this.icons, this.iconBaseRects,
				index -> this.launchApp((int) index));

		if (kabegami != null) {
			this.phoneUI.setKabegami(kabegami);
		}

		this.phoneUI.setOnAreaClick(() -> {
			String input = JOptionPane.showInputDialog(this, "お住まいの地域は？", "大阪");
			if (input != null && !input.isEmpty()) {
				this.updateWeather(this.weatherService.convertCityName(input));
			}
		});

		this.add(this.phoneUI, BorderLayout.CENTER);
		this.updateWeather("Osaka");
	}

	public void updateWeather(String cityName) {
		new Thread(() -> {
			WeatherResult data = this.weatherService.getLiveWeather(cityName);

			SwingUtilities.invokeLater(() -> {
				if (this.phoneUI != null && data != null) {
					this.phoneUI.drawer.date = new SimpleDateFormat("M/d (E)").format(new Date());
					this.phoneUI.drawer.weather = data.weather;
					this.phoneUI.drawer.temp = data.temp;
					this.phoneUI.drawer.highLow = data.high + " / " + data.low;
					this.phoneUI.repaint();
				}
			});
		}).start();
	}

	public void launchApp(int index) {
		main.Main.audio.playSe("tan.mp3");
		JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		if (mainFrame == null) {
			return;
		}

		switch (index) {
		case 0:
			this.setupApp(new HighPrecisionBpmApp(), mainFrame);
			break;
		case 1:
			this.setupApp(new UranaiApp(), mainFrame);
			break;
		case 2:
			this.setupApp(new Dice(), mainFrame);
			break;
		case 3:
			System.out.println("Newsを起動します");
			portforio.service.LoginDialog newsLog = new portforio.service.LoginDialog(null);
			newsLog.setVisible(true);
			if (!newsLog.isSucceeded()) {
				return;
			}
			portforio.main.Main newsApp = new portforio.main.Main(newsLog.getUsername());
			this.setupApp(newsApp, mainFrame);
			break;
		case 4:
			main.RineApp rApp = new main.RineApp();
			this.setupApp(rApp, mainFrame);
			break;
		case 5:
			// ★【この1行を追加！】起動する直前に、親のプレイヤーインスタンス（main.Main.audio）を注入する
			main.MainApp.setParentAudio(main.Main.audio);

			this.setupApp(new MainApp(), mainFrame);
			break;
		default:
			break;
		}
	}

	private void setupApp(JFrame subApp, final JFrame mainFrame) {
		main.Main.audio.stopBgm();
		mainFrame.setVisible(false);
		subApp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		subApp.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				mainFrame.setVisible(true);
				mainFrame.toFront();
			}
		});
		subApp.setVisible(true);
	}
}
