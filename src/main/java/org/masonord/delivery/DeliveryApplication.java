package org.masonord.delivery;

import java.util.Arrays;

import org.hibernate.Session;
import org.masonord.delivery.model.EmployeeEntity;
import org.masonord.delivery.util.HibernateUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DeliveryApplication {
	private static final Session session = HibernateUtil.getSessionFactory().openSession();

//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//
//
//		return args -> {
//
//		};
//	}



	public static void main(String[] args) {
		EmployeeEntity emp = new EmployeeEntity();
		emp.setEmail("demo-user@mail.com");
		emp.setFirstName("demo");
		emp.setLastName("user");
		session.beginTransaction().commit();
		session.save(emp);
		session.close();
		SpringApplication.run(DeliveryApplication.class, args);
	}

}