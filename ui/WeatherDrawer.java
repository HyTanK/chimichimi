package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class WeatherDrawer {
	public int x = 216;
	public int y = 472;
	public int size = 62;
	public String date = "";
	public String weather = "晴れ";
	public String temp = "--°C";
	public String highLow = "-- / --";

	private String translateWeather(String w) {
		if (w == null)
			return "";
		switch (w.toLowerCase()) {
		case "clear":
			return "晴れ";
		case "clouds":
			return "曇り";
		case "rain":
			return "雨";
		case "snow":
			return "雪";
		case "drizzle":
			return "霧雨";
		case "thunderstorm":
			return "雷雨";
		case "fog":
		case "haze":
		case "mist":
			return "霧";
		default:
			return w;
		}
	}

	public void draw(Graphics2D g2d, int fx, int fy, double s) {
		if (this.date == null)
			this.date = "";
		if (this.weather == null)
			this.weather = "";
		if (this.temp == null)
			this.temp = "";
		if (this.highLow == null)
			this.highLow = "";

		g2d.setColor(Color.CYAN);
		int curX = fx + (int) (this.x * s) - (int) (70.0 * s);
		int curY = fy + (int) (this.y * s) + (int) (50.0 * s);

		g2d.setFont(new Font("SansSerif", Font.PLAIN, (int) (this.size * 1.2 * s)));
		g2d.drawString(this.date, curX + (int) (70.0 * s), curY - (int) (50.0 * s));

		curY += (int) (this.size * 2.0 * s);
		g2d.setFont(new Font("SansSerif", Font.PLAIN, (int) (this.size * 2.0 * s)));
		g2d.drawString(this.getSymbol(this.weather), curX + (int) (70.0 * s), curY - (int) (this.size * 1.2 * s));

		int offset = (int) (this.size * 0.5 * s);
		int textBaseX = curX + (int) (70.0 * s) + offset;
		g2d.setFont(new Font("SansSerif", Font.BOLD, (int) (this.size * 0.7 * s)));
		g2d.drawString(this.translateWeather(this.weather), textBaseX, curY - (int) (this.size * 0.5 * s));

		int tempOffset = (int) (this.size * 2.5 * s);
		int tempX = curX + (int) (70.0 * s) + tempOffset;
		g2d.setFont(new Font("SansSerif", Font.BOLD, (int) (this.size * 1.5 * s)));
		g2d.drawString(this.temp, tempX, curY - (int) (this.size * 0.6 * s));

		String[] hl = this.highLow.split(" / ");
		int hlX = curX + offset + (int) (this.size * 7.0 * s);
		g2d.setFont(new Font("SansSerif", Font.PLAIN, (int) (this.size * 0.7 * s)));
		g2d.setColor(new Color(255, 100, 100));
		g2d.drawString("最高 " + (hl.length > 0 ? hl[0] : "-"), hlX, curY - (int) (this.size * 0.6 * s));
		g2d.setColor(new Color(100, 150, 255));
		g2d.drawString("最低 " + (hl.length > 1 ? hl[1] : "-"), hlX, curY + (int) (this.size * 0.2 * s));
	}

	private String getSymbol(String w) {
		if (w == null)
			return "✨";
		if (w.contains("Clear") || w.contains("晴"))
			return "☀️";
		if (w.contains("Cloud") || w.contains("曇"))
			return "☁️";
		if (w.contains("Rain") || w.contains("雨"))
			return "☔";
		if (w.contains("Snow") || w.contains("雪"))
			return "❄️";
		return "✨";
	}
}
