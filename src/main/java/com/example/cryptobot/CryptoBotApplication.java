package com.example.cryptobot;

import com.example.cryptobot.crypto.service.CryptoService;
import com.example.cryptobot.listeners.CryptoTypeListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class CryptoBotApplication {

	@Autowired
	private Environment env;

	@Autowired
	private CryptoService cryptoService;

	@Autowired
	private CryptoTypeListener cryptoTypeListener;

	public static void main(String[] args) {
		SpringApplication.run(CryptoBotApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(value = "discord-api")

	public DiscordApi discordApi() throws IOException, InterruptedException {
		String token = env.getProperty("TOKEN");
		DiscordApi api = new DiscordApiBuilder().setToken(token).setAllNonPrivilegedIntents().login().join();

		api.addMessageCreateListener(cryptoTypeListener);

		return api;
	}

	@Scheduled(fixedRate = 12500)
	private void updateActivity() throws IOException, InterruptedException {
		discordApi().updateActivity(ActivityType.WATCHING, cryptoService.getCurrency().getName() + ": $" + cryptoService.getCurrency().getPrice());
	}
}
