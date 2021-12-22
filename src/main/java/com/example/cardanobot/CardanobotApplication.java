package com.example.cardanobot;

import com.example.cardanobot.crypto.service.CryptoService;
import com.example.cardanobot.listeners.CryptoTypeListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@EnableScheduling
public class CardanobotApplication {

	@Autowired
	private Environment env;

	@Autowired
	private CryptoService cryptoService;

	@Autowired
	private CryptoTypeListener cryptoTypeListener;

	public static void main(String[] args) {
		SpringApplication.run(CardanobotApplication.class, args);
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
	public void updateBotStatus() throws IOException, InterruptedException, ExecutionException {
		discordApi().updateActivity(ActivityType.WATCHING, cryptoService.getCurrency().getName() + ": $" + cryptoService.getCurrency().getPrice());
	}

}
