package de.lubowiecki;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PrimaryController {

    @FXML
    private PieChart chart;

    @FXML
    private void getDataFromApi() throws IOException, InterruptedException {

        // TODO: RapidApi API Key einsetzen
        final String API_KEY = "...";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://imdb236.p.rapidapi.com/api/imdb/lowest-rated-movies"))
                .header("x-rapidapi-key", API_KEY)
                .header("x-rapidapi-host", "imdb236.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        parseJson(response.body());
    }

    private void parseJson(String jsonStr) {

        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArr = (JSONArray) parser.parse(jsonStr);

            Map<String, Integer> map = new HashMap<>();
            map.put("Drama", 1);

            for (int i = 0; i < jsonArr.size(); i++) {
                JSONObject obj = (JSONObject) jsonArr.get(i);
                JSONArray genres =  (JSONArray) obj.get("genres");
                for (int j = 0; j < genres.size(); j++) {
                    String genre = (String) genres.get(j);
                    map.put(genre, map.getOrDefault(genre, 0) + 1);
                }
            }

            ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                chartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

            chart.setData(chartData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
