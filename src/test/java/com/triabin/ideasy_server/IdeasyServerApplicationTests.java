package com.triabin.ideasy_server;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.logging.log4j.Logger;

@SpringBootTest
class IdeasyServerApplicationTests {

	private final Logger logger = LogManager.getLogger(IdeasyServerApplicationTests.class);

	@Test
	void contextLoads() {
		logger.info("INFO级别日志{}", "占位符1");
		logger.warn("WARN级别日志{}", "占位符1");
		logger.error("ERROR级别日志", new Exception("异常"));
	}

}
