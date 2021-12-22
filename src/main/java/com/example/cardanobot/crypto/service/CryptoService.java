package com.example.cardanobot.crypto.service;


import com.example.cardanobot.crypto.model.Currency;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;

@Service
public class CryptoService {

    public enum Type {
        BTC("Bitcoin"), ETH("Ethereum"), ADA("Cardano"), DOGE("Dogecoin");

        String name;

        Type(String str) {
            name = str;
        }
    }

    private static String CRYTPO_URL = "https://api.coinstats.app/public/v1/coins?skip=0&limit=20&currency=USD";
    private Currency currency;
    private Type cryptoType = Type.BTC;

    @PostConstruct
    @Scheduled(fixedRate = 25000)
    public void makeCryptoObject() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CRYTPO_URL)).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(response != null) {
            // first parse response
            JsonElement element = JsonParser.parseString(response.body());

            // next get as json object
            JsonObject jsonObject = element.getAsJsonObject();

            // get array from json object
            JsonArray jsonArray = jsonObject.get("coins").getAsJsonArray();

            JsonObject currencyJsonObj = null;
            for(int i = 0; i < jsonArray.size(); i++) {
                currencyJsonObj = jsonArray.get(i).getAsJsonObject();
                if(currencyJsonObj.get("name").getAsString().equals(cryptoType.name)) {
                    break;
                }
            }

            Currency currency = new Currency();
            currency.setName(currencyJsonObj.get("name").getAsString());
            String cardanoPriceStr = new DecimalFormat("0.000").format(currencyJsonObj.get("price").getAsDouble());
            currency.setPrice(Double.parseDouble(cardanoPriceStr));
            System.out.println(currency.getName() + ": " + currency.getPrice());
            this.currency = currency;
        } else {
            System.err.println("Response was null");
        }
    }

    public void setCryptoType(Type cryptoType) {
        this.cryptoType = cryptoType;
        makeCryptoObject();
    }

    public Type getCryptoType() {
        return cryptoType;
    }

    public Currency getCurrency() {
        return currency;
    }
}
