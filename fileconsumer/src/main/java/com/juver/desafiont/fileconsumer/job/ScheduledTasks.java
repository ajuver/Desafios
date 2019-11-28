package com.juver.desafiont.fileconsumer.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.juver.desafiont.fileconsumer.service.FileConsumerService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduledTasks {
	
	@Autowired
	FileConsumerService fileConsumerService;
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedRate = 3000)
    public void scheduleTaskWithFixedRate() {
    	logger.info("Iniciando o Serviço de consumo de arquivos!!! ", dateTimeFormatter.format(LocalDateTime.now()) );
        this.fileConsumerService.execute();
    }

 
}
