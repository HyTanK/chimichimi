package ui;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class WeatherService {
    private final String apiKey = "3b26363f572445b02efe7efbaf0bd6a2";
    private static final Map<String, String> CITY_MAP = new HashMap<>();

    static {
        CITY_MAP.put("北海道", "Sapporo");
        CITY_MAP.put("青森", "Aomori");
        CITY_MAP.put("岩手", "Morioka");
        CITY_MAP.put("宮城", "Sendai");
        CITY_MAP.put("秋田", "Akita");
        CITY_MAP.put("山形", "Yamagata");
        CITY_MAP.put("福島", "Fukushima");
        CITY_MAP.put("茨城", "Mito");
        CITY_MAP.put("栃木", "Utsunomiya");
        CITY_MAP.put("群馬", "Maebashi");
        CITY_MAP.put("埼玉", "Saitama");
        CITY_MAP.put("千葉", "Chiba");
        CITY_MAP.put("東京", "Tokyo");
        CITY_MAP.put("神奈川", "Yokohama");
        CITY_MAP.put("新潟", "Niigata");
        CITY_MAP.put("富山", "Toyama");
        CITY_MAP.put("石川", "Kanazawa");
        CITY_MAP.put("福井", "Fukui");
        CITY_MAP.put("山梨", "Kofu");
        CITY_MAP.put("長野", "Nagano");
        CITY_MAP.put("岐阜", "Gifu");
        CITY_MAP.put("静岡", "Shizuoka");
        CITY_MAP.put("愛知", "Nagoya");
        CITY_MAP.put("三重", "Tsu");
        CITY_MAP.put("滋賀", "Otsu");
        CITY_MAP.put("京都", "Kyoto");
        CITY_MAP.put("大阪", "Osaka");
        CITY_MAP.put("兵庫", "Kobe");
        CITY_MAP.put("奈良", "Nara");
        CITY_MAP.put("和歌山", "Wakayama");
        CITY_MAP.put("鳥取", "Tottori");
        CITY_MAP.put("島根", "Matsue");
        CITY_MAP.put("岡山", "Okayama");
        CITY_MAP.put("広島", "Hiroshima");
        CITY_MAP.put("山口", "Yamaguchi");
        CITY_MAP.put("徳島", "Tokushima");
        CITY_MAP.put("香川", "Takamatsu");
        CITY_MAP.put("愛媛", "Matsuyama");
        CITY_MAP.put("高知", "Kochi");
        CITY_MAP.put("福岡", "Fukuoka");
        CITY_MAP.put("佐賀", "Saga");
        CITY_MAP.put("長崎", "Nagasaki");
        CITY_MAP.put("熊本", "Kumamoto");
        CITY_MAP.put("大分", "Oita");
        CITY_MAP.put("宮崎", "Miyazaki");
        CITY_MAP.put("鹿児島", "Kagoshima");
        CITY_MAP.put("沖縄", "Naha");
    }

    public String convertCityName(String input) {
        if (input == null || input.isEmpty()) return "Osaka";
        String clean = input.replace("県", "").replace("府", "").replace("都", "").replace("道", "");
        return CITY_MAP.getOrDefault(clean, input);
    }

    public WeatherResult getLiveWeather(String city) {
        WeatherResult res = new WeatherResult();
        try {
            String url = "https://openweathermap.org" + city + "&appid=" + apiKey + "&units=metric";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            String json = resp.body();
            
            res.temp = this.round(this.getValue(json, "\"temp\":", ","));
            res.high = this.round(this.getValue(json, "\"temp_max\":", ","));
            res.low = this.round(this.getValue(json, "\"temp_min\":", ","));
            
            String weatherKey = "\"main\":\"";
            int start = json.indexOf(weatherKey) + weatherKey.length();
            res.weather = json.substring(start, json.indexOf("\"", start));
        } catch (Exception e) {
            res.temp = "--°C";
            res.high = "--°C";
            res.low = "--°C";
            res.weather = "取得失敗";
        }
        return res;
    }

    private String round(String val) {
        try {
            double d = Double.parseDouble(val.replaceAll("[^0-9.-]", ""));
            return Math.round(d) + "°C";
        } catch (Exception e) {
            return "--°C";
        }
    }

    private String getValue(String json, String key, String end) {
        if (!json.contains(key)) return "0";
        int start = json.indexOf(key) + key.length();
        int stop = json.indexOf(end, start);
        return stop != -1 ? json.substring(start, stop) : "0";
    }
}
