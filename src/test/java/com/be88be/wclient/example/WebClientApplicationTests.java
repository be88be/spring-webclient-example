package com.be88be.wclient.example;

import com.be88be.wclient.example.service.WebClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class WebClientApplicationTests {

	@Autowired
	private WebClientService wcs;

	@Test
	public void get() {
		wcs.getToooByIdNonblock("1000");
	}

}
