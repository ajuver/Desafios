package com.juver.desafiont.fileconsumer.consumer;

import java.io.File;
import java.io.FileFilter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileConsumer {

	@Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;
    
    @Value("${diretorio.entrada}")
    private String diretorioEntrada;
    
    @Value("${diretorio.pendentes}")
    private String diretorioPendentes;
    
    private static final Logger logger = LoggerFactory.getLogger(FileConsumer.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public void process() {
    
    	logger.info("Verificando se existem arquivos no diretório... ", dateTimeFormatter.format(LocalDateTime.now()) );

    	
    	FileFilter filter = new FileFilter() {
		    public boolean accept(File file) {
		        return file.getName().endsWith(".dat");
		    }
		};
		
//		//Busca os arquivos no Diretório		
		File dir = new File(diretorioEntrada);
		
		File[] files = dir.listFiles();
		
		logger.info(files == null?"0":files.length + " Arquivos encontrados... ", dateTimeFormatter.format(LocalDateTime.now()) );
		
		if(null != files) {
			//Processa cada arquivo encontrado
			Arrays.asList(files).forEach(file->{
				logger.info(file.getName() + " Encontrado!!! ", dateTimeFormatter.format(LocalDateTime.now()) );
				if(file.exists()){
					logger.info(">>>>>>> Mover arquivo: " + file.getName() + " para o diretorio " + diretorioPendentes);
					String nomeArquivo = file.getName() + "_" + (new GregorianCalendar()).getTimeInMillis() + "_" + (new Double((new Random()).nextDouble() * 100000)).longValue();
					logger.info(">>>>>>> Renomeando arquivo para o nome: " + nomeArquivo);
					file.renameTo(new File(diretorioPendentes, nomeArquivo));
					
					logger.info("Chamando serviço de processamento de arquivos", dateTimeFormatter.format(LocalDateTime.now()) );
					rabbitTemplate.convertAndSend(this.queue.getName(), nomeArquivo);
				}
			});
		}
    	
    	logger.info("Processo Finalizado!!!", dateTimeFormatter.format(LocalDateTime.now()) );
    }
	
}
