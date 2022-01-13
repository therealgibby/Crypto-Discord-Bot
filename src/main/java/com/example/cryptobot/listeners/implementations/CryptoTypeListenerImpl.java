package com.example.cryptobot.listeners.implementations;

import com.example.cryptobot.crypto.service.CryptoService;
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

        if((messageContent.equals("!btc") || messageContent.equals("!bitcoin")) && cryptoService.getCryptoType() != CryptoService.Type.BTC) {
            cryptoService.setCryptoType(CryptoService.Type.BTC, event);
        }
        if((messageContent.equals("!eth") || messageContent.equals("!ethereum")) && cryptoService.getCryptoType() != CryptoService.Type.ETH) {
            cryptoService.setCryptoType(CryptoService.Type.ETH, event);
        }
        if((messageContent.equals("!ada") || messageContent.equals("!cardano")) && cryptoService.getCryptoType() != CryptoService.Type.ADA) {
            cryptoService.setCryptoType(CryptoService.Type.ADA, event);
        }
        if((messageContent.equals("!doge") || messageContent.equals("!dogecoin")) && cryptoService.getCryptoType() != CryptoService.Type.DOGE) {
            cryptoService.setCryptoType(CryptoService.Type.DOGE, event);
        }
    }
}
