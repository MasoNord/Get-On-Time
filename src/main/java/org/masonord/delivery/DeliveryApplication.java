package org.masonord.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class DeliveryApplication {
	public static void main(String[] args) {
		SpringApplication.run(DeliveryApplication.class, args);
	}
}