package com.juver.desafiont.fileprocessor.model;

import java.math.BigDecimal;

public class Item {
	private Long id;
	private Integer quantity;
	private BigDecimal price;
	
	public Item(Long id, Integer quantity, BigDecimal price) {
		this.id = id;
		this.quantity = quantity;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getTotalItem(){
		return this.price.multiply(new BigDecimal(this.quantity));
	}
	
	@Override
	public String toString() {
		return "Item [id=" + id + ", quantity=" + quantity + ", price=" + price + ", totalItem="+ getTotalItem()+"]";
	}
	
}
