package com.example.DiscountBackend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // ✅ pour les batches planifiés

@SpringBootApplication
@EnableScheduling
				
public class DiscountBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscountBackendApplication.class, args);
	}

}
 