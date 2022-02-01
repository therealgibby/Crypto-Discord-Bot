package com.example.cryptobot.crypto.service;

public enum CryptoType {
    BTC("Bitcoin"), ETH("Ethereum"), ADA("Cardano"), DOGE("Dogecoin");

    String name;

    CryptoType(String str) {
        name = str;
    }
}
