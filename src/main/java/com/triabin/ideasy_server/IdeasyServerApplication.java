package com.triabin.ideasy_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.triabin.ideasy_server.mapper")
public class IdeasyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdeasyServerApplication.class, args);
	}

}
