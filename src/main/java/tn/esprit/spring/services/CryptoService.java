package tn.esprit.spring.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class CryptoService {

    private static final String API_BASE_URL = "https://api.coingecko.com/api/v3";

    public Map<String, Double> getRealTimePrices(String[] symbols) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Double> prices = new HashMap<>();

        for (String symbol : symbols) {
            String url = API_BASE_URL + "/simple/price?ids=" + symbol + "&vs_currencies=usd";
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder responseContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }

            JsonNode jsonNode = objectMapper.readTree(responseContent.toString());
            double price = jsonNode.get(symbol.toLowerCase()).get("usd").asDouble();
            prices.put(symbol, price);
        }

        return prices;
    }
}
