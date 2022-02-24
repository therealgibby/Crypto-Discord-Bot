package com.example.cryptobot.crypto.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CoinStatsService {

    private static final String CRYPTO_URL = "https://api.coinstats.app/public/v1/coins?skip=0&limit=20&currency=USD";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CRYPTO_URL)).build();

    private CoinStatsService() {}

    public static String createResponse() {

        HttpResponse<String> httpResponse = null;

        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return checkResponse(httpResponse.body());
    }

    // if response is null the function will generate one from "response.json" and return it
    // otherwise it will return the original response
    private static String checkResponse(String response) {

        if(response == null) {
            Path fileName = Path.of("src/main/resources/response.json");
            try {
                response = Files.readString(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }
}
