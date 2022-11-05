package com.example.cryptobot.crypto.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CoinStatsService {

    private final String CRYPTO_URL = "https://api.coinstats.app/public/v1/coins?skip=0&limit=20&currency=USD";
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CRYPTO_URL)).build();

    public CoinStatsService() {}

    public String createResponse() {

        HttpResponse<String> httpResponse = null;

        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return checkResponse(httpResponse);
    }

    // if response is null the function will generate one from "response.json" and return it
    // otherwise it will return the original response
    private String checkResponse(HttpResponse<String> response) {
        String responseBody = null;

        if(response == null) {
            Path fileName = Path.of("src/main/resources/response.json");
            try {
                responseBody = Files.readString(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            responseBody = response.body();
        }

        return responseBody;
    }
}
