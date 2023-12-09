package org.masonord.delivery;

import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.masonord.delivery.config.PropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeliveryApplicationTests {

	@Autowired
	PropertiesConfig propertiesConfig;


	@Test
	@Description("Should get environment variables from custom properties file")
	void testPropertiesConfigClass() {
		String value = propertiesConfig.getConfigValue("user.not.found");
		Assertions.assertEquals("User was not found by the given email", value);
	}

}
