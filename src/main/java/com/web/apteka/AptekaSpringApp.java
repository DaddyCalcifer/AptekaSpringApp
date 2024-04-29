package com.web.apteka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.web.apteka")
public class AptekaSpringApp {

	public static void main(String[] args) {

		SpringApplication.run(AptekaSpringApp.class, args);
		Thread backgroundThread = new Thread(() -> {
			while (true) {
				// Ваша основная логика здесь
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		backgroundThread.setDaemon(false); // Это предотвращает автоматическое завершение приложения, когда все незавершенные потоки являются демонами
		backgroundThread.start();
	}

}
