package com.vtapadia.playground.redis;

import com.vtapadia.playground.redis.service.Reusable;
import com.vtapadia.playground.redis.service.ReusablePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
    ReusablePool reusablePool;

	@Override
	public void run(String... strings) throws Exception {
		reusablePool.addReusable(Arrays.asList(
				new Reusable("varesh1"),
				new Reusable("varesh2"),
				new Reusable("varesh3"))
		);

		Reusable reusable = reusablePool.acquire();
        logger.info("Got Object {}", reusable.getValue());
		reusablePool.release(reusable);
	}
}
