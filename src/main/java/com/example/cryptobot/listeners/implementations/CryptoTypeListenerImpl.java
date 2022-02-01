package com.example.cryptobot.listeners.implementations;

import com.example.cryptobot.crypto.service.CryptoService;
import com.example.cryptobot.crypto.service.CryptoType;
import com.example.cryptobot.listeners.CryptoTypeListener;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CryptoTypeListenerImpl implements CryptoTypeListener {

    @Autowired
    private CryptoService cryptoService;

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        String messageContent = event.getMessageContent().toLowerCase();

        if((messageContent.equals("!btc") || messageContent.equals("!bitcoin")) && cryptoService.getCryptoType() != CryptoType.BTC) {
            cryptoService.setCryptoType(CryptoType.BTC, event);
        }
        if((messageContent.equals("!eth") || messageContent.equals("!ethereum")) && cryptoService.getCryptoType() != CryptoType.ETH) {
            cryptoService.setCryptoType(CryptoType.ETH, event);
        }
        if((messageContent.equals("!ada") || messageContent.equals("!cardano")) && cryptoService.getCryptoType() != CryptoType.ADA) {
            cryptoService.setCryptoType(CryptoType.ADA, event);
        }
        if((messageContent.equals("!doge") || messageContent.equals("!dogecoin")) && cryptoService.getCryptoType() != CryptoType.DOGE) {
            cryptoService.setCryptoType(CryptoType.DOGE, event);
        }
    }
}
