package org.masonord.delivery;

import org.masonord.delivery.util.FakeDataUtil;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class DeliveryApplication {

	@Autowired
	FakeDataUtil fakeDataUtil;

	@Autowired
	IdUtils idUtils;

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApplication.class, args);
	}
}