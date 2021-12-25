package com.example.cryptobot.crypto.service;

import com.example.cryptobot.crypto.model.Currency;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.event.message.MessageCreateEvent;
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

    private static final String CRYPTO_URL = "https://api.coinstats.app/public/v1/coins?skip=0&limit=20&currency=USD";
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CRYPTO_URL)).build();
    private HttpResponse<String> response = null;

    private Currency currency;
    private Type cryptoType = Type.BTC;

    private void receiveResponse() {
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // finds the correct cryptocurrency
    private JsonObject createJsonObject() {
        // first parse response
        JsonElement element = JsonParser.parseString(response.body());

        // next get as json object
        JsonObject jsonObject = element.getAsJsonObject();

        // get array from json object
        JsonArray jsonArray = jsonObject.get("coins").getAsJsonArray();

        JsonObject currencyJsonObj = null;
        for(int i = 0; i < jsonArray.size(); i++) {
            currencyJsonObj = jsonArray.get(i).getAsJsonObject();
            if(currencyJsonObj.get("name").getAsString().equals(cryptoType.name))
                break;
        }
        return currencyJsonObj;
    }

    @PostConstruct
    @Scheduled(fixedRate = 25000)
    public void createCryptoObject() {

        // call public api for information on the cryptocurrency
        receiveResponse();

        // create the json object of the currency
        JsonObject jsonCurrency = createJsonObject();

        // create currency object
        Currency currency = new Currency();

        // set the currency name
        currency.setName(jsonCurrency.get("name").getAsString());

        // format the price and set it as the currency price
        String cardanoPriceStr = new DecimalFormat("0.000").format(jsonCurrency.get("price").getAsDouble());
        currency.setPrice(Double.parseDouble(cardanoPriceStr));

        System.out.println(currency.getName() + ": " + currency.getPrice());
        this.currency = currency;
    }

    public void setCryptoType(Type cryptoType, MessageCreateEvent event) {
        this.cryptoType = cryptoType;
        createCryptoObject();
        updateActivity(event);
    }

    private void updateActivity(MessageCreateEvent event) {
        event.getApi().updateActivity(ActivityType.WATCHING, this.currency.getName() + ": $" + this.currency.getPrice());
    }

    public Type getCryptoType() {
        return cryptoType;
    }

    public Currency getCurrency() {
        return currency;
    }
}
