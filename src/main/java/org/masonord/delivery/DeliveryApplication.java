package org.masonord.delivery;

import org.hibernate.Session;
import org.masonord.delivery.util.HibernateUtil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeliveryApplication {
	private static final Session session = HibernateUtil.getSessionFactory().openSession();

	public static void main(String[] args) {
		session.beginTransaction().commit();
		session.close();
		SpringApplication.run(DeliveryApplication.class, args);
	}

}