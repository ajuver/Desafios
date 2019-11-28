package com.juver.desafiont.fileconsumer.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juver.desafiont.fileconsumer.consumer.FileConsumer;

@Service
public class FileConsumerService {
	
	@Autowired
	FileConsumer fileConsumer;
	
	private static final Logger logger = LoggerFactory.getLogger(FileConsumerService.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	public void execute() {
		logger.info("Iniciando o Processo de consumo de arquivos!!! ", dateTimeFormatter.format(LocalDateTime.now()) );
		fileConsumer.process();
	}
}
