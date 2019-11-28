package com.juver.desafiont.fileprocessor;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableRabbit
public class FileprocessorApplication {
	
	@Value("${queue.file.name}")
	private String fileQueue;
	
	public static void main(String[] args) {
		SpringApplication.run(FileprocessorApplication.class, args);
	}

	@Bean
	public Queue queue() {
		return new Queue(fileQueue, true);
	}
	
}
