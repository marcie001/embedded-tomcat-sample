package com.example.employee;

import java.time.LocalDate;

/**
 * Employee エンティティ
 * 
 * @author marcie
 *
 */
public class Employee {

	private String id;

	private String fullName;

	private LocalDate birthDay;

	private int salary;

	public Employee() {

	}

	public Employee(String id, String fullName, LocalDate birthDay, int salary) {
		this.id = id;
		this.fullName = fullName;
		this.birthDay = birthDay;
		this.salary = salary;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(LocalDate birthDay) {
		this.birthDay = birthDay;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", fullName=" + fullName + ", birthDay=" + birthDay + ", salary=" + salary + "]";
	}

}
