package software.ulpgc.moneycalculator.io;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import software.ulpgc.moneycalculator.model.Currency;
import software.ulpgc.moneycalculator.fixerws.FixerAPI;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ExchangeRateLoader implements RateLoader {

    private static final String API_URL = "https://api.exchangeratesapi.io/v1/latest";
    private static double percentage;
    @Override
    public double load(Currency from, Currency to) throws IOException {
        String urlString = API_URL
                + "?access_key=" + FixerAPI.key
                + "&symbols=" + from.code() + "," + to.code();
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("API Error: HTTP " + responseCode);
        }

        try (InputStream is = connection.getInputStream()) {
            String jsonResponse = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

            if (!jsonObject.get("success").getAsBoolean()) {
                throw new RuntimeException("Conversion Error: " + jsonObject);
            }

            JsonObject rates = jsonObject.getAsJsonObject("rates");
            if (rates == null) {
                throw new RuntimeException("No exchange rate found");
            }

            Double fromRate = rates.get(from.code()).getAsDouble();
            Double toRate = rates.get(to.code()).getAsDouble();
            percentage = (toRate/fromRate)*100;
            return toRate / fromRate;
        }
    }
    public static double getPercentage() {
        return percentage;
    }
}
