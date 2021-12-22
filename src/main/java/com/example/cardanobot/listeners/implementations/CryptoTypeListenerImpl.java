package com.example.cardanobot.listeners.implementations;

import com.example.cardanobot.crypto.service.CryptoService;
import com.example.cardanobot.listeners.CryptoTypeListener;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CryptoTypeListenerImpl implements CryptoTypeListener {

    @Autowired
    private CryptoService cryptoService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        String messageContent = messageCreateEvent.getMessageContent().toLowerCase();

        if((messageContent.equals("!btc") || messageContent.equals("!bitcoin")) && cryptoService.getCryptoType() != CryptoService.Type.BTC) {
            cryptoService.setCryptoType(CryptoService.Type.BTC);
            updateActivity(messageCreateEvent);
        }
        if((messageContent.equals("!eth") || messageContent.equals("!ethereum")) && cryptoService.getCryptoType() != CryptoService.Type.ETH) {
            cryptoService.setCryptoType(CryptoService.Type.ETH);
            updateActivity(messageCreateEvent);
        }
        if((messageContent.equals("!ada") || messageContent.equals("!cardano")) && cryptoService.getCryptoType() != CryptoService.Type.ADA) {
            cryptoService.setCryptoType(CryptoService.Type.ADA);
            updateActivity(messageCreateEvent);
        }
        if((messageContent.equals("!doge") || messageContent.equals("!dogecoin")) && cryptoService.getCryptoType() != CryptoService.Type.DOGE) {
            cryptoService.setCryptoType(CryptoService.Type.DOGE);
            updateActivity(messageCreateEvent);
        }
    }

    private void updateActivity(MessageCreateEvent messageCreateEvent) {
        messageCreateEvent.getApi()
                .updateActivity(ActivityType.WATCHING, cryptoService.getCurrency().getName() + ": $" + cryptoService.getCurrency().getPrice());
    }
}
