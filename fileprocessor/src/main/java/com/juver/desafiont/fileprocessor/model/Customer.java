package com.juver.desafiont.fileprocessor.model;

public class Customer {
	private String CNPJ;
	private String name;
	private String businessArea;
	
	public Customer(String CNPJ, String name, String businessArea) {
		this.CNPJ = CNPJ;
		this.name = name;
		this.businessArea = businessArea;
	}
	
	public String getCNPJ() {
		return CNPJ;
	}
	public String getName() {
		return name;
	}
	public String getBusinessArea() {
		return businessArea;
	}
	public void setCNPJ(String cNPJ) {
		CNPJ = cNPJ;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

	@Override
	public String toString() {
		return "Customer [CNPJ=" + CNPJ + ", name=" + name + ", businessArea=" + businessArea + "]";
	}
	
}
