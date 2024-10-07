package com.micaelaandrade.orderparser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class OrderParserApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		assertThat(applicationContext).isNotNull();
	}

	@Test
	void testMainMethod() {

		try (var mocked = mockStatic(SpringApplication.class)) {

			OrderParserApplication.main(new String[]{});

			mocked.verify(() -> SpringApplication.run(OrderParserApplication.class, new String[]{}));
		}
	}
}

