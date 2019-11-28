package com.juver.desafiont.fileprocessor.processor;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.juver.desafiont.fileprocessor.FileprocessorApplication;
import com.juver.desafiont.fileprocessor.model.Customer;
import com.juver.desafiont.fileprocessor.model.Item;
import com.juver.desafiont.fileprocessor.model.Sale;
import com.juver.desafiont.fileprocessor.model.Salesman;

@Component
public class FileProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);
	
	private List<Customer> listCustumer = new ArrayList<Customer>();
	private List<Salesman> listSalesman = new ArrayList<Salesman>();
	private List<Sale> listSale = new ArrayList<Sale>();	
	
	@Value("${diretorio.pendentes}")
    private String diretorioPendentes;
	
	@Value("${diretorio.saida}")
    private String diretorioSaida;
	
	@Value("${diretorio.processados}")
    private String diretorioProcessados;
	
	@RabbitListener(queues = {"${queue.file.name}"})
    public void receive(@Payload String nameFile) {
		
		logger.info("Iniciando processamento do arquivo " + nameFile + " ... ");
		
		try {
			lerArqivo(nameFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		logger.info("Encerrando processamento do arquivo " + nameFile + " ... ");
        
    }
	
	private void lerArqivo(String fileName) throws IOException, URISyntaxException{
		
		
		FileFilter filter = new FileFilter() {
		    public boolean accept(File file) {
		        return file.getName().equalsIgnoreCase(fileName);
		    }
		};
		
//		//Busca os arquivos no Diretório
		File dir = new File(diretorioPendentes);
		logger.info(">>>>>>> Buscando arquivo no diretório de pendentes... ");
		File[] files = dir.listFiles(filter);
		
		logger.info(files == null?"0":files.length + " Arquivos encontrados... ");
		if(null != files) {
			//Processa cada arquivo encontrado
		 	Arrays.asList(files).forEach(file->{
				logger.info(">>>>>>> Lendo arquivo: " + file.getName());
				if(file.exists()){
					Path caminho = Paths.get(file.getPath());
					Stream<String> rows = null;
					try {
						rows = Files.lines(caminho, StandardCharsets.UTF_8);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					rows.forEach(row -> {
						try {
							String[] valores = row.split("ç");
							
							if("001".equals(valores[0])){
								listSalesman.add(factorySalesman(valores));
							}else if("002".equals(valores[0])){
								listCustumer.add(factoryCustomer(valores));
							}else if("003".equals(valores[0])){
								listSale.add(factorySale(valores));
							}else{
								logger.info("Erro ao ler linha: " + valores);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}		
				file.renameTo(new File(diretorioProcessados, file.getName()));
			});
			processData(fileName);
			listClear();
		}
	}
	
	private void listClear() {
		this.listCustumer.clear();
		this.listSale.clear();
		this.listSalesman.clear();		
	}


	/**
	 * Método que processa os dados
	 * @throws IOException
	 */
	private void processData(String fileName) throws IOException{
		
		if((null == listCustumer || listCustumer.isEmpty()) &&
		   (null == listSalesman || listSalesman.isEmpty()) &&
		   (null == listSale || listSale.isEmpty())) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Quantidade de clientes no arquivo de entrada: " + listCustumer.size() + "\n");
		sb.append("Quantidade de vendedor no arquivo de entrada: " + listSalesman.size() + "\n");

		List<Sale> newListSale = listSale.stream().sorted((sale1, sale2)->
		sale1.getTotalSale().compareTo(sale2.getTotalSale())).collect(Collectors.toList());
		
		sb.append("A venda mais cara foi: id " + newListSale.get(newListSale.size()-1).getId() + " no valor de R$: "+ newListSale.get(newListSale.size()-1).getTotalSale() +"\n");
		
		Map<String, Double> values =  newListSale.stream().collect(Collectors.groupingBy(Sale::getSalesmanName, Collectors.summingDouble(Sale::getTotalSale)))
			.entrySet().stream().sorted(Map.Entry.comparingByValue())
			.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
		
		 Map.Entry<String,Double> entry = values.entrySet().iterator().next();
		 String key= entry.getKey();
		 
		 sb.append("O Pior Vendedor " + key);
		 
		 saveFile(fileName, sb.toString());
		 
	}
	
	/**
	 * Método que salva o arquivo
	 * @param text
	 * @throws IOException
	 */
	private void saveFile(String fileName, String text) throws IOException{
		logger.info(text);
		File dir = new File(diretorioSaida);
		
		FileWriter arq = new FileWriter(dir.getPath()+"/"+fileName + "_RESULT");
		PrintWriter writer = new PrintWriter(arq);
		writer.print(text);
		arq.close();
	}
	
	/**
	 * Método que monta a classe de Vendedor
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private Salesman factorySalesman(String[] row) throws Exception{
		if(!"001".equals(row[0])){
			throw new Exception("Wrong type row!");
		}
		
		Salesman salesman = new Salesman(row[1], row[2], row[3]);
		
		logger.info(salesman.toString());
		return salesman;
		
	}

	/**
	 * Método que monta a classe de Cliente
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private Customer factoryCustomer(String[] row) throws Exception{
		
		if(!"002".equals(row[0])){
			throw new Exception("Wrong type row!");
		}
		
		Customer customer = new Customer(row[1], row[2], row[3]);
		
		System.out.println(customer.toString());
		
		return customer;
	}

	
	/**
	 * Método que monta a classe de vendas
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private Sale factorySale(String[] row) throws Exception{
	
		if(!"003".equals(row[0])){
			throw new Exception("Wrong type row!");
		}
		
		Sale sale = new Sale(Integer.parseInt(row[1]), row[3]);
		
		String[] itemsString = row[2].replace("[", "").replace("]", "").split(",");
		
		Arrays.asList(itemsString).forEach(item ->{
			String[] detailItem = item.split("-");
			Item newItem = new Item(Long.parseLong(detailItem[0]), Integer.parseInt(detailItem[1]), new BigDecimal(detailItem[2]));
			sale.getItems().add(newItem);
		});
		
		logger.info(sale.toString());
		
		return sale;
	}
	
	
}
