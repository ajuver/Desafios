package com.juver.desafiont.fileprocessor.model;

public class Salesman {
	private String CPF;
	private String name;
	private String salary;
	
	public Salesman(String CPF, String name, String salary) {
		this.CPF = CPF;
		this.name = name;
		this.salary = salary;
	}
	
	public String getCPF() {
		return CPF;
	}

	public String getName() {
		return name;
	}

	public String getSalary() {
		return salary;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Salesman [CPF=" + CPF + ", name=" + name + ", salary=" + salary + "]";
	}
	
	
}
