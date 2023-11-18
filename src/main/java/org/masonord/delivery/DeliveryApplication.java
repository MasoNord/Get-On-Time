package org.masonord.delivery;

import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.dao.CourierDao;
import org.masonord.delivery.repository.dao.UserDao;
import org.masonord.delivery.util.FakeDataUtil;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class DeliveryApplication {

	@Autowired
	FakeDataUtil fakeDataUtil;

	@Autowired
	IdUtils idUtils;

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApplication.class, args);
	}

	@Bean
	CommandLineRunner inti(UserDao userDao) {
		return args -> {
			User user = userDao.findUserByEmail("dummy@gamil.com");
			if (user == null) {
				user = new User()
						.setRole(UserRoles.ADMIN)
						.setPassword("123456789")
						.setFirstName("DummyF")
						.setEmail("dummy@gamil.com")
						.setLastName("DummyL");

				userDao.creatUser(user);
			}
		};
	}
}