package com.example.cryptobot.crypto.service;

import com.example.cryptobot.crypto.model.Currency;
import com.google.gson.*;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;

@Service
public class CryptoService {

    private final Currency currency = new Currency();
    private CryptoType cryptoType = CryptoType.BTC;
    private CoinStatsService coinStatsService;

    public CryptoService(CoinStatsService coinStatsService) {
        this.coinStatsService = coinStatsService;
    }

    // finds the correct cryptocurrency
    private JsonObject createJsonObject() {

        // call public api for information on the cryptocurrency
        String response = coinStatsService.createResponse();

        // first parse response
        JsonElement element = JsonParser.parseString(response);

        // next get as json object
        JsonObject jsonObject = element.getAsJsonObject();

        // get array from json object
        JsonArray jsonArray = jsonObject.get("coins").getAsJsonArray();

        JsonObject currencyJsonObj = null;

        // find cryptocurrency that corresponds with the cryptoType
        for(int i = 0; i < jsonArray.size(); i++) {
            currencyJsonObj = jsonArray.get(i).getAsJsonObject();
            if(currencyJsonObj.get("name").getAsString().equals(cryptoType.name))
                break;
        }
        return currencyJsonObj;
    }

    // use json object to create currency object
    @Scheduled(fixedRate = 10000)
    public void createCryptoObject() {

        // create the json object of the currency
        JsonObject jsonCurrency = createJsonObject();

        // set the currency name
        this.currency.setName(jsonCurrency.get("name").getAsString());

        // format the price and set it as the currency price
        String cryptoPriceStr = new DecimalFormat("0.000").format(jsonCurrency.get("price").getAsDouble());
        this.currency.setPrice(Double.parseDouble(cryptoPriceStr));

        System.out.println(this.currency.getName() + ": " + this.currency.getPrice());
    }

    // sets which cryptocurrency the discord bot is watching
    public void setCryptoType(CryptoType cryptoType, MessageCreateEvent event) {
        this.cryptoType = cryptoType;
        createCryptoObject();
        updateActivity(event);
    }

    private void updateActivity(MessageCreateEvent event) {
        event.getApi().updateActivity(ActivityType.WATCHING, this.currency.getName() + ": $" + this.currency.getPrice());
    }

    public CryptoType getCryptoType() {
        return cryptoType;
    }

    public Currency getCurrency() {
        return currency;
    }
}
