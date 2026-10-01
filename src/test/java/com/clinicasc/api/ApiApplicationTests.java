package com.clinicasc.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"security.jwt.secret=teste-chave-jwt-com-pelo-menos-32-caracteres"
})
class ApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
