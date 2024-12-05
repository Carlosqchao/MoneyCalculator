package software.ulpgc.moneycalculator.fixerws;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.ulpgc.moneycalculator.model.Currency;
import software.ulpgc.moneycalculator.io.CurrencyLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class FixerCurrencyLoader implements CurrencyLoader {

    private static final String API_URL = "http://data.fixer.io/api/symbols";

    @Override
    public List<Currency> load() {
        try {
            return toList(loadJson());
        } catch (IOException e) {
            return emptyList();
        }
    }

    private List<Currency> toList(String json) {
        List<Currency> list = new ArrayList<>();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);

        if (!jsonObject.has("success") || !jsonObject.get("success").getAsBoolean()) {
            System.err.println("Error loading data: " + jsonObject);
            return list;
        }

        JsonObject symbols = jsonObject.getAsJsonObject("symbols");
        for (Map.Entry<String, JsonElement> entry : symbols.entrySet()) {
            String symbol = entry.getKey();
            String description = entry.getValue().getAsString();
            list.add(new Currency(symbol, description));
        }
        return list;
    }

    private String loadJson() throws IOException {
        String urlString = API_URL + "?access_key=" + FixerAPI.key;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error: " + responseCode);
        }

        try (InputStream is = connection.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
