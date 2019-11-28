package com.juver.desafiont.fileprocessor.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Sale {

	private Integer id;
	private List<Item> items;
	private String salesmanName;
	
	public Sale(Integer id, String salesmanName) {
		this.id = id;
		this.salesmanName = salesmanName;
	}

	public Integer getId() {
		return id;
	}

	public List<Item> getItems() {
		if(this.items == null){
			this.items = new ArrayList<Item>();
		}
		return items;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}
	
	public Double getTotalSale(){
		BigDecimal total = BigDecimal.ZERO;
		for(Item item:getItems()){
			total = total.add(item.getTotalItem());
		};
		
		return total.doubleValue();
		
	}
	
	@Override
	public String toString() {
		return "Sale [id=" + id + ", items=" + items + ", salesmanName=" + salesmanName + ", TotalSale="+ getTotalSale() +"]";
	}
	
}
