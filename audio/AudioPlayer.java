package audio;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javazoom.jl.player.Player;

public class AudioPlayer {
	private volatile Player bgmPlayer;
	private volatile String currentBgmFile;
	private Thread bgmThread;

	public synchronized void playBgm(String fileName) {
		if (fileName != null && fileName.equals(this.currentBgmFile)) {
			return;
		}

		stopBgm();
		this.currentBgmFile = fileName;

		bgmThread = new Thread(() -> {
			String target = fileName;
			while (target.equals(currentBgmFile) && !Thread.currentThread().isInterrupted()) {
				String fullPath = "/sounds/" + target;
				// ★ getClass() から AudioPlayer.class に変更
				try (InputStream is = AudioPlayer.class.getResourceAsStream(fullPath)) {
					if (is == null) {
						System.out.println("【警告】BGMファイルが見つかりません: " + fullPath);
						break;
					}
					bgmPlayer = new Player(new BufferedInputStream(is));
					bgmPlayer.play();
				} catch (Exception e) {
					break;
				}
			}
			System.out.println("BGMを完全に停止しました: " + target);
		}, "BGM-Thread");

		bgmThread.setDaemon(true);
		bgmThread.start();
	}

	public synchronized void stopBgm() {
		currentBgmFile = null;
		if (bgmPlayer != null) {
			try {
				bgmPlayer.close();
			} catch (Exception ignored) {
			}
			bgmPlayer = null;
		}
		if (bgmThread != null) {
			bgmThread.interrupt();
			bgmThread = null;
		}
	}

	public void playSe(String fileName) {
		new Thread(() -> {
			String fullPath = "/sounds/" + fileName;
			// ★ getClass() から AudioPlayer.class に変更
			try (InputStream is = AudioPlayer.class.getResourceAsStream(fullPath)) {
				if (is != null) {
					new Player(new BufferedInputStream(is)).play();
				} else {
					System.out.println("【警告】SEファイルが見つかりません: " + fullPath);
				}
			} catch (Exception ignored) {
			}
		}).start();
	}

	public String getCurrentBgm() {
		return currentBgmFile;
	}
}
